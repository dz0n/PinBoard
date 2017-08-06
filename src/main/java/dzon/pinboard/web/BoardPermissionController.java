package dzon.pinboard.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dzon.pinboard.service.BoardPermissionService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RestController
@RequestMapping(RestUri.permissions)
public class BoardPermissionController {
	@Autowired
	private BoardPermissionService boardPermissionService;
	
	
}
