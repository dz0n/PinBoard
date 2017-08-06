package dzon.pinboard.persist;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;

import dzon.pinboard.domain.BoardPermission;

public interface BoardPermissionRepository extends MongoRepository<BoardPermission, String> {
	Collection<BoardPermission> findBoardPermissionsByUserId(String userId);
	Collection<BoardPermission> findBoardPermissionsByBoardId(String boardId);
}
