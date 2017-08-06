package dzon.pinboard.service;

import java.util.Collection;

import dzon.pinboard.domain.Note;

public interface NoteService {
	Collection<Note> getNotes(String boardId);
	Note get(String noteId);
	Note save(Note note);
	void delete(String noteId);
}
