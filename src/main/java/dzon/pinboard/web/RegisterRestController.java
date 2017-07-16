package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;
import dzon.pinboard.service.UserService;

@RestController
@RequestMapping(ControllerContract.RestUri.register)
public class RegisterRestController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(method=POST)
	ResponseEntity<?> add(@RequestBody CreateUserForm form) {
		if(passwordsAreEqual(form)) {
			User user = userService.createUser(form);
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest().path("{id}")
					.buildAndExpand(user.getId()).toUri();
			return ResponseEntity.created(location).build();
		} else {
			throw new BadPasswordException();
		}
	}
	
	private boolean passwordsAreEqual(CreateUserForm form) {
		return form.getPassword().equals(form.getRepeatedPassword());
	}
}
