package com.joshcummings.codeplay.terracotta.service;

import com.joshcummings.codeplay.terracotta.AbstractEmbeddedTomcatTest;
import com.joshcummings.codeplay.terracotta.model.User;
import org.junit.Assert;
import org.testng.annotations.Test;

public class UserServiceTest extends AbstractEmbeddedTomcatTest {
	@Test(groups="passwordstorage")
	public void testSamePasswordDifferentHash() {
		UserService userService = new UserService();
		User one = new User("one", "one", "password", "name", "one@terracotta");
		userService.addUser(one);

		User two = new User("two", "two", "password", "name", "two@terracotta");
		userService.addUser(two);

		one = userService.findByUsername("one");
		two = userService.findByUsername("two");

		Assert.assertNotEquals(one.getPassword(), two.getPassword());
	}

	@Test(groups="passwordstorage")
	public void testStrongPasswordHashAlgorithm() {
		UserService userService = new UserService();
		User user = new User("user", "user", "password", "name", "user@terracotta");
		userService.addUser(user);

		user = userService.findByUsername("user");
		String hashed = user.getPassword();

		Assert.assertTrue(hashed.startsWith("$2a$"));
		String[] parts = hashed.split("\\$");
		Assert.assertTrue(parts.length == 3);
		Integer strength = Integer.parseInt(parts[1]);
		Assert.assertTrue(strength >= 10); // str
	}
}