package dzon.pinboard.persist;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dzon.pinboard.domain.Note;

public interface NoteRepository extends MongoRepository<Note, String> {
	List<Note> findNotesByBoardId(String boardId);
}
