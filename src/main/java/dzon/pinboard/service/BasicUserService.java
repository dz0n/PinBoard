package dzon.pinboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dzon.pinboard.domain.CreateUserForm;
import dzon.pinboard.domain.User;
import dzon.pinboard.persist.UserRepository;

@Service
public class BasicUserService implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUserById(String id) {
		return userRepository.findOne(id);
	}

	@Override
	public User getUserByEmail(String email) {
		return null;
	}

	@Override
	public User createUser(CreateUserForm form) {
		// TODO Auto-generated method stub
		return null;
	}

}
