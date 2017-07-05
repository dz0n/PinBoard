package dzon.pinboard.persist;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.domain.User;

@ContextConfiguration(classes=MongoTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	private int userCount;
	
	@Before
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

	@After
	public void cleanUp() {
		userRepository.deleteAll();
	}

	@Test(expected=DuplicateKeyException.class)
	public void testDuplicateEmail() {
		String email = "user@domain.com";
		User user = new User(email);
		userRepository.save(user);
		
		User duplicatedUser = new User(email);
		userRepository.save(duplicatedUser);
	}
	
	@Test
	public void finUserByEmail() {
		userRepository.save(getUser());
		userRepository.save(getUser());
		User user = getUser();
		user = userRepository.save(user);
		userRepository.save(getUser());
		userRepository.save(getUser());
		
		User foundUser = userRepository.findUserByEmail(user.getEmail());
		assertEquals(user, foundUser);
	}
	
	@Test
	public void saveTest() {
		assertEquals(0, userRepository.count());
		
		User user = getUser();
		String email = user.getEmail();
		user = userRepository.save(user);
		assertEquals(email, user.getEmail());
		
		assertEquals(1,  userRepository.count());
		List<User> users = userRepository.findAll();
		assertEquals(user, users.get(0));
	}

	@Test
	public void deleteTest() {
		userRepository.save(getUser());
		User user = userRepository.save(getUser());
		userRepository.save(getUser());
		assertEquals(3, userRepository.count());
		
		userRepository.delete(user);
		assertEquals(2, userRepository.count());
	}
	
	@Test
	public void updateTest() {
		User user = userRepository.save(getUser());
		String email = "newemail@newdomain.com";
		user.setEmail(email);
		user = userRepository.save(user);
		
		assertEquals(user, userRepository.findAll().get(0));
		assertEquals(email, userRepository.findAll().get(0).getEmail());
	}

	private User getUser() {
		userCount++;
		User user = new User("user" + userCount + "@domain.com");
		user.setHash("long_hash_" + userCount);
		return user;
	}
}
