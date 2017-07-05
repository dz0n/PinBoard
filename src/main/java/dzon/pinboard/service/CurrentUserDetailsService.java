package dzon.pinboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dzon.pinboard.domain.User;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(username);
		if(user==null)
			throw new UsernameNotFoundException(String.format("User with email=%s was not found", username));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHash(), AuthorityUtils.NO_AUTHORITIES);
	}

}
