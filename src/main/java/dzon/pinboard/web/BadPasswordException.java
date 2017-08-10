package dzon.pinboard.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
class BadPasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BadPasswordException() {
		super("Repeated password is different than password");
	}
}
