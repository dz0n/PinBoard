package dzon.pinboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import dzon.pinboard.web.ControllerContract.RestUri;
import dzon.pinboard.web.ControllerContract.Uri;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoderConfig passwordEncoderConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(
					Uri.home, 
					Uri.register, 
					Uri.login,
					RestUri.register
				).permitAll()
				.anyRequest().authenticated()
			.and()
				.httpBasic()
			.and()
				.csrf().ignoringAntMatchers(RestUri.api + "/**");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoderConfig.passwordEncoder());
	}
}
