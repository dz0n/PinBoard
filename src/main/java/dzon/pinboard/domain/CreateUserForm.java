package dzon.pinboard.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateUserForm {
	@NotEmpty
	private String email = "";
	
	@NotEmpty
	private String password = "";
	
	@NotEmpty
	private String repeatedPassword = "";

	public final String getEmail() {
		return email;
	}

	public final String getPassword() {
		return password;
	}

	public final String getRepeatedPassword() {
		return repeatedPassword;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	public final void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}
}
