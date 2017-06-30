package dzon.pinboard.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.domain.User;
import dzon.pinboard.persist.UserRepository;

@ContextConfiguration(classes=PinboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
	@Autowired
	@InjectMocks
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUserById() {
		String id = "id_of_user";
		User user = new User("user@domain.com");
		user.setId(id);
		user.setHash("hash_of_password");
		Mockito.when(userRepository.findOne(id)).thenReturn(user);
		
		assertEquals(user, userService.getUserById(id));
	}

	@Test
	public void testGetUserByEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateUser() {
		fail("Not yet implemented");
	}

}
