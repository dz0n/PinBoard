package dzon.pinboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dzon.pinboard.config.PasswordEncoderConfig;
import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;
import dzon.pinboard.persist.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoderConfig passwordEncoderConfig;
	
	@Override
	public User getUserById(String id) {
		return userRepository.findOne(id);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public User createUser(CreateUserForm form) {
		User user = new User(form.getEmail());
		user.setHash(passwordEncoderConfig.passwordEncoder().encode(form.getPassword()));
		return userRepository.save(user);
	}

}
