package dzon.pinboard.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dzon.pinboard.domain.Note;
import dzon.pinboard.persist.NoteRepository;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteRepository noteRepository;
	
	@Override
	public Collection<Note> getNotes(String boardId) {
		return noteRepository.findNotesByBoardId(boardId);
	}

	@Override
	public Note get(String noteId) {
		Note note = noteRepository.findOne(noteId);
		if(note==null) {
			throw new NotFoundException("note");
		} else {
			return note;
		}
	}

	@Override
	public Note save(Note note) {
		return noteRepository.save(note);
	}

	@Override
	public void delete(String noteId) {
		noteRepository.delete(noteId);
	}

}