package dzon.pinboard.service;

import java.util.Collection;

import dzon.pinboard.domain.BoardPermission;

public interface BoardPermissionService {
	Collection<BoardPermission> getBoardPermissionsByUser(String userId);
	Collection<BoardPermission> getBoardPermissions(String boardId);
	BoardPermission save(BoardPermission permission);
	void delete(BoardPermission permission);
}
