package dzon.pinboard.web;

public class ControllerContract {
	
	public class Uri {

		public static final String home = "/";
		public static final String register = "/register";
		public static final String login = "/login";
		
	}
	
	public class RestUri {
		public static final String api = "/api";
		public static final String register = api + "/register";
		public static final String users = api + "/users";
		public static final String boards = api + "/boards";
		
	}
	
	public class View {

		public static final String register = "register";
		
	}
	
}
