package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dzon.pinboard.domain.Board;
import dzon.pinboard.domain.User;
import dzon.pinboard.service.BoardService;
import dzon.pinboard.service.UserService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RestController
@RequestMapping(RestUri.boards)
public class BoardRestController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(method=POST)
	public ResponseEntity<?> add(@RequestBody Board board, Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		
		board.setId(null);
		Board savedBoard = boardService.save(board, user.getId());
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedBoard.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(method=PUT, value="/{boardId}")
	public ResponseEntity<?> update(@PathVariable String boardId, @RequestBody Board board, Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		
		board.setId(boardId);
		
		boardService.save(board, user.getId());
		
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method=GET)
	public Collection<Board> getBoards(Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		
		return boardService.getBoards(user.getId());
	}
	
	@RequestMapping(method=GET, value="/{boardId}")
	public Board getBoard(@PathVariable String boardId) {
		return boardService.get(boardId);
	}
	
	@RequestMapping(method=DELETE, value="/{boardId}")
	public ResponseEntity<?> delete(@PathVariable String boardId) {
		boardService.delete(boardId);
		
		return ResponseEntity.noContent().build();
	}
}
