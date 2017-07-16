package dzon.pinboard.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;
import dzon.pinboard.service.UserService;
import dzon.pinboard.web.ControllerContract.Uri;
import dzon.pinboard.web.ControllerContract.View;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {
	@Autowired @InjectMocks
	private RegisterController controller;
	@Mock
	private UserService userService;
	@Autowired
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		Mockito.when(userService.createUser(Mockito.any(CreateUserForm.class))).thenReturn(getUser(getForm()));
	}

	@Test
	public void testShowRegistrationPage() throws Exception {
		mockMvc.perform(get(Uri.register))
				.andExpect(view().name(View.register))
				.andExpect(model().attribute("form", new CreateUserForm()));
	}

	@Test
	public void testProcessRegistration() throws Exception {
		CreateUserForm form = getForm();
		
		mockPostRequest(form).andExpect(redirectedUrl("/"));
		
		Mockito.verify(userService, Mockito.times(1)).createUser(form);
	}
	
	@Test
	public void testMismatchedPasswords() throws Exception {
		CreateUserForm form = getForm();
		form.setRepeatedPassword(form.getPassword() + "foo");
		
		mockPostRequest(form).andExpect(view().name(View.register));
		
		Mockito.verify(userService, Mockito.never()).createUser(form);
	}
	
	private ResultActions mockPostRequest(CreateUserForm form) throws Exception {
		return mockMvc.perform(post(Uri.register)
						.with(csrf())
						.param("email", form.getEmail())
						.param("password", form.getPassword())
						.param("repeatedPassword", form.getRepeatedPassword()));
	}
	
	private CreateUserForm getForm() {
		CreateUserForm form = new CreateUserForm();
		form.setEmail("someone@somewhere.com");
		form.setPassword("QweRty123");
		form.setRepeatedPassword("QweRty123");
		return form;
	}
	
	private User getUser(CreateUserForm form) {
		User user = new User(form.getEmail());
		user.setHash("hashIsHere#@$@$");
		user.setId("IdIsHere$^#^#$");
		return user;
	}
	
}
