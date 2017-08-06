package dzon.pinboard.service;

import java.util.Collection;

import dzon.pinboard.domain.Board;

public interface BoardService {
	Collection<Board> getBoards(String userId);
	Board get(String id);
	Board save(Board board, String userId);
	void delete(Board board);
	void delete(String boardId);
}
