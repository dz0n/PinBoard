package dzon.pinboard.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.domain.Note;
import dzon.pinboard.persist.NoteRepository;

@ContextConfiguration(classes=PinboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NoteServiceImplTest {
	@Mock
	private NoteRepository repository;
	@Autowired @InjectMocks
	private NoteService service;
	private final String boardId = "board_id_1";
	private int counter = 0;
	
	@After
	public void validate() throws Exception {
		Mockito.validateMockitoUsage();
	}

	@Test
	public void testGetNotes() {
		Collection<Note> notes = getNotes();
		when(repository.findNotesByBoardId(boardId)).thenReturn(notes);
		
		Collection<Note> results = service.getNotes(boardId);
		assertEquals(notes, results);
		
		verify(repository, times(1)).findNotesByBoardId(boardId);
	}

	@Test
	public void testGet() {
		Note note = getNote();
		when(repository.findOne(note.getId())).thenReturn(note);
		
		assertEquals(note, service.get(note.getId()));
		verify(repository, times(1)).findOne(note.getId());
	}

	@Test
	public void testSave() {
		Note notSavedNote = getNotSavedNote();
		Note note = getNote(notSavedNote);
		when(repository.save(notSavedNote)).thenReturn(note);
		
		Note result = service.save(notSavedNote);
		assertEquals(note, result);
		verify(repository, times(1)).save(notSavedNote);
	}

	@Test
	public void testDelete() {
		String noteId = "note_id_2";
		service.delete(noteId);
		
		verify(repository, times(1)).delete(noteId);
	}
	
	@Test(expected=NotFoundException.class)
	public void testGetNull() {
		when(repository.findOne(anyString())).thenReturn(null);
		
		service.get("wrong_id");
	}
	
	private Note getNotSavedNote() {
		counter++;
		Note note = new Note();
		note.setBoardId(boardId);
		note.setTitle("Title of note " + counter);
		note.setText("Text of note " + counter);
		return note;
	}
	
	private Collection<Note> getNotes() {
		Collection<Note> notes = new ArrayList<>();
		notes.add(getNote());
		notes.add(getNote());
		notes.add(getNote());
		notes.add(getNote());
		notes.add(getNote());
		return notes;
	}
	
	private Note getNote() {
		Note note = getNotSavedNote();
		note.setId("note_id_" + counter);
		return note;
	}
	
	private Note getNote(Note noteWithoutId) {
		Note note = new Note();
		note.setBoardId(noteWithoutId.getBoardId());
		note.setTitle(noteWithoutId.getTitle());
		note.setText(noteWithoutId.getText());
		note.setId("note_id_" + noteWithoutId.getTitle().replaceAll("\\D+",""));
		return note;
	}
}
