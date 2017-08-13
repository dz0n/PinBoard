package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.domain.BoardRole;
import dzon.pinboard.service.BoardPermissionService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RestController
@RequestMapping(RestUri.permissions)
public class BoardPermissionRestController {
	@Autowired
	private BoardPermissionService service;
	
	@RequestMapping(method=POST)
	public ResponseEntity<?> add(@RequestBody BoardPermission permission, @PathVariable String boardId) {
		if(permission.getRole()==BoardRole.OWNER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		permission.setId(null);
		
		BoardPermission savedPermission = service.save(permission);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedPermission.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(method=DELETE, value="/{permissionId}")
	public ResponseEntity<?> delete(@PathVariable String permissionId, @PathVariable String boardId) {
		BoardPermission permission = service.get(permissionId);
		if(permission.getRole()==BoardRole.OWNER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		service.delete(permissionId);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=PUT, value="/{permissionId}")
	public ResponseEntity<?> update(@PathVariable String permissionId, 
			@PathVariable String boardId, 
			@RequestBody BoardPermission permission) {
		permission.setId(permissionId);
		if(permission.getRole()==BoardRole.OWNER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if(service.get(permissionId).getRole()==BoardRole.OWNER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		service.save(permission);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method=GET, value="/{permissionId}")
	public BoardPermission get(@PathVariable String permissionId, @PathVariable String boardId) {
		return service.get(permissionId);
	}
	
	@RequestMapping(method=GET)
	public Collection<BoardPermission> getAll(@PathVariable String boardId) {
		return service.getBoardPermissions(boardId);
	}
	
}
