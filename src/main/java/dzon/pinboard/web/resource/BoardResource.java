package dzon.pinboard.web.resource;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import dzon.pinboard.domain.Board;
import dzon.pinboard.web.BoardRestController;
import dzon.pinboard.web.NoteRestController;

public class BoardResource extends ResourceSupport {
	private final Board board;
	
	public BoardResource(Board board) {
		this.board = board;
		this.add(linkTo(methodOn(BoardRestController.class)
				.getBoard(board.getId())).withSelfRel());
		this.add(linkTo(BoardRestController.class).withRel("boards"));
		this.add(linkTo(methodOn(NoteRestController.class, board.getId())
				.getNotes(board.getId())).withRel("notes"));
	}
	
	public Board getBoard() {
		return board;
	}
}
