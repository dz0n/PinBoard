package dzon.pinboard.service;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;

public interface UserService {
	User getUserById(String id);
	User getUserByEmail(String email);
	User createUser(CreateUserForm form);
}
