package dzon.pinboard.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dzon.pinboard.domain.Board;
import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.domain.BoardRole;
import dzon.pinboard.persist.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private BoardPermissionService boardPermissionService;
	
	@Override
	public Collection<Board> getBoards(String userId) {
		Collection<BoardPermission> permissions = boardPermissionService.getBoardPermissionsByUser(userId);
		Collection<Board> boards = new ArrayList<>();
		for(BoardPermission permission : permissions) {
			Board board = boardRepository.findOne(permission.getBoardId());
			if(board!=null) {
				boards.add(board);
			}
		}
		return boards;
	}

	@Override
	public Board get(String id) {
		Board board = boardRepository.findOne(id);
		if(board==null) {
			throw new NotFoundException("Board");
		}
		return board;
	}

	@Override
	public Board save(Board board, String userId) {
		boolean isInRepository = boardRepository.countById(board.getId()) > 0;
		Board savedBoard = boardRepository.save(board);
		
		if(!isInRepository) {
			BoardPermission permission = new BoardPermission(
					null, 
					userId, 
					savedBoard.getId(), 
					BoardRole.OWNER);
			boardPermissionService.save(permission);
		}
		
		return savedBoard;
	}

	@Override
	public void delete(Board board) {
		boardRepository.delete(board);
	}
	
	@Override
	public void delete(String boardId) {
		boardRepository.delete(boardId);
	}

}
