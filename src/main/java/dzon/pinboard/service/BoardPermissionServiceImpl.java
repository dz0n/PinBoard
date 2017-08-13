package dzon.pinboard.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.persist.BoardPermissionRepository;

@Service
public class BoardPermissionServiceImpl implements BoardPermissionService {
	@Autowired
	private BoardPermissionRepository repository;
	
	@Override
	public Collection<BoardPermission> getBoardPermissionsByUser(String userId) {
		return repository.findBoardPermissionsByUserId(userId);
	}

	@Override
	public Collection<BoardPermission> getBoardPermissions(String boardId) {
		return repository.findBoardPermissionsByBoardId(boardId);
	}

	@Override
	public BoardPermission save(BoardPermission permission) {
		return repository.save(permission);
	}

	@Override
	public void delete(String id) {
		repository.delete(id);
	}

	@Override
	public BoardPermission get(String permissionId) {
		return repository.findOne(permissionId);
	}

}
