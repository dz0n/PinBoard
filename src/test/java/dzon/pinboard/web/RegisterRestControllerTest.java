package dzon.pinboard.web;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;
import dzon.pinboard.service.UserService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterRestControllerTest {
	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	@Autowired @InjectMocks
	private RegisterRestController controller;
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private UserService userService;
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);
		
		assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}
	
	@Before
	public void setUp() throws Exception {
		Mockito.when(userService.createUser(getForm())).thenReturn(getUser());
	}

	@Test
	public void testAdd() throws Exception {
		CreateUserForm form = getForm();
		String userJson = json(form);
		
		this.mockMvc.perform(post(RestUri.register)
				.contentType(contentType)
				.content(userJson))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void testAddWithWrongPassword() throws Exception {
		CreateUserForm form = getFormWithDifferentPassword();
		String userJson = json(form);
		
		this.mockMvc.perform(post(RestUri.register)
				.contentType(contentType)
				.content(userJson))
			.andExpect(status().isBadRequest());
	}

	private CreateUserForm getForm() {
		CreateUserForm form = new CreateUserForm();
		form.setEmail("abc@def.com");
		form.setPassword("QweRty456");
		form.setRepeatedPassword("QweRty456");
		return form;
	}
	
	private User getUser() {
		User user = new User("abc@def.com");
		user.setHash("dsfsgf34gg54tg4");
		user.setId("gfvrtfg4g54245g2g254");
		return user;
	}
	
	private CreateUserForm getFormWithDifferentPassword() {
		CreateUserForm form = new CreateUserForm();
		form.setEmail("abc@def.com");
		form.setPassword("QweRty456");
		form.setRepeatedPassword("Asdfgh789");
		return form;
	}
	
	@SuppressWarnings("unchecked")
	private String json(CreateUserForm object) throws IOException {
		MockHttpOutputMessage httpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				object, MediaType.APPLICATION_JSON, httpOutputMessage);
		return httpOutputMessage.getBodyAsString();
	}
	
	
}
