package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.service.UserService;

@Controller
@RequestMapping(ControllerContract.Register.path)
public class RegisterController {
	@Autowired
	private UserService userService;
	
	@ModelAttribute(name="form")
	public CreateUserForm form() {
		return new CreateUserForm();
	}
	
	@RequestMapping(method=GET)
	public String showRegistrationPage() {
		return ControllerContract.Register.view;
	}
	
	@RequestMapping(method=POST)
	public String processRegistration(@ModelAttribute("form") CreateUserForm form) {
		if(passwordsAreEqual(form)) {
			userService.createUser(form);
			return "redirect:" + ControllerContract.Home.path;
		} else {
			return ControllerContract.Register.view;
		}
	}

	private boolean passwordsAreEqual(CreateUserForm form) {
		return form.getPassword().equals(form.getRepeatedPassword());
	}
}
