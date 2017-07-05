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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.config.PasswordEncoderConfig;
import dzon.pinboard.domain.CreateUserForm;
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
	@Mock
	private PasswordEncoderConfig passwordEncoderConfig;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUserById() {
		User user = getUser();
		Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
		
		assertEquals(user, userService.getUserById(user.getId()));
	}

	@Test
	public void testGetUserByEmail() {
		User user = getUser();
		Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
		
		assertEquals(user, userService.getUserByEmail(user.getEmail()));
	}

	@Test
	public void testCreateUser() {
		CreateUserForm form = getForm();
		User userBeforeSave = getUserWithoutHash(form);
		User userAfterSave = getUserWithHash(form);
		
		PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
		
		Mockito.when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
		Mockito.when(passwordEncoder.encode(form.getPassword())).thenReturn(getHash(form.getPassword()));
		Mockito.when(userRepository.save(userBeforeSave)).thenReturn(userAfterSave);
		
		User user = userService.createUser(form);
		assertEquals(userAfterSave, user);
	}
	
	private User getUser() {
		User user = new User("user@domain.com");
		user.setId("id_of_user");
		user.setHash("hash_of_password");
		return user;
	}

	private CreateUserForm getForm() {
		CreateUserForm form = new CreateUserForm();
		form.setEmail("user@domain.com");
		form.setPassword("Testowe123");
		form.setRepeatedPassword("Testowe123");
		return form;
	}
	
	private User getUserWithHash(CreateUserForm form) {
		User user = getUserWithoutHash(form);
		user.setId("regf43t42r");
		return user;
	}
	
	private User getUserWithoutHash(CreateUserForm form) {
		User user = new User(form.getEmail());
		user.setHash(getHash(form.getPassword()));
		return user;
	}
	
	private String getHash(String password) {
		return "fdsf43fffb$T%G%b256b$bhnjhklgjgb46yn";
	}
}
