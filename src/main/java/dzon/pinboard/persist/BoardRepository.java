package dzon.pinboard.persist;

import org.springframework.data.mongodb.repository.MongoRepository;

import dzon.pinboard.domain.Board;

public interface BoardRepository extends MongoRepository<Board, String> {
	public long countById(String id);
}
