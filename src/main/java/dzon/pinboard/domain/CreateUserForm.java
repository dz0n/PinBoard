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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((repeatedPassword == null) ? 0 : repeatedPassword.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateUserForm other = (CreateUserForm) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (repeatedPassword == null) {
			if (other.repeatedPassword != null)
				return false;
		} else if (!repeatedPassword.equals(other.repeatedPassword))
			return false;
		return true;
	}
	
}
