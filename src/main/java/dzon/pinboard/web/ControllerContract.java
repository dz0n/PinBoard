package dzon.pinboard.web;

public class ControllerContract {
	public class Home {
		public static final String path = "/";
	}
	
	public class Register {
		public static final String path = "/register";
		public static final String view = "register";
	}
	
	public class Login {
		public static final String path = "/login";
	}
}
