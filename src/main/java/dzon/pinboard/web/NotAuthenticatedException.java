package dzon.pinboard.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class NotAuthenticatedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotAuthenticatedException() {
		super("Authentication required.");
	}
}
