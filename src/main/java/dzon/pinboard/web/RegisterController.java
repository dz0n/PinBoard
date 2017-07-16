package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.service.UserService;
import dzon.pinboard.web.ControllerContract.Uri;
import dzon.pinboard.web.ControllerContract.View;

@Controller
@RequestMapping(Uri.register)
public class RegisterController {
	@Autowired
	private UserService userService;
	
	@ModelAttribute(name="form")
	public CreateUserForm form() {
		return new CreateUserForm();
	}
	
	@RequestMapping(method=GET)
	public String showRegistrationPage() {
		return View.register;
	}
	
	@RequestMapping(method=POST)
	public String processRegistration(@ModelAttribute("form") CreateUserForm form) {
		if(passwordsAreEqual(form)) {
			userService.createUser(form);
			return "redirect:" + Uri.home;
		} else {
			return View.register;
		}
	}

	private boolean passwordsAreEqual(CreateUserForm form) {
		return form.getPassword().equals(form.getRepeatedPassword());
	}
}
