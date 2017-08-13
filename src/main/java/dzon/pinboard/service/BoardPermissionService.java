package dzon.pinboard.service;

import java.util.Collection;

import dzon.pinboard.domain.BoardPermission;

public interface BoardPermissionService {
	Collection<BoardPermission> getBoardPermissionsByUser(String userId);
	Collection<BoardPermission> getBoardPermissions(String boardId);
	BoardPermission get(String permissionId);
	BoardPermission save(BoardPermission permission);
	void delete(String id);
}
