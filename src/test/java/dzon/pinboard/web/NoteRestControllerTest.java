package dzon.pinboard.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.domain.Note;
import dzon.pinboard.service.NotFoundException;
import dzon.pinboard.service.NoteService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteRestControllerTest  extends RestControllerTest {
	@Mock
	private NoteService noteService;
	@Autowired @InjectMocks
	private NoteRestController controller;
	private int counter = 0;
	private final String boardId = "board_id_1";
	
	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void validate() throws Exception {
		Mockito.validateMockitoUsage();
	}

	@Test
	@WithMockUser
	public void testAdd() throws Exception {
		Note notSavedNote = getNotSavedNote();
		Note note = getNote(notSavedNote);
		when(noteService.save(notSavedNote)).thenReturn(note);
		
		String jsonNote = json(notSavedNote);
		
		mockMvc.perform(post(RestUri.notes, boardId)
				.contentType(contentType)
				.content(jsonNote))
			.andExpect(status().isCreated());
		
		verify(noteService, times(1)).save(notSavedNote);
	}
	
	@Test
	@WithMockUser
	public void testUpdate() throws Exception {
		Note note = getNote();
		when(noteService.save(note)).thenReturn(note);
		
		String jsonNote = json(note);
		
		mockMvc.perform(put(RestUri.notes + "/" + note.getId(), boardId)
				.contentType(contentType)
				.content(jsonNote))
			.andExpect(status().isOk());
		
		verify(noteService, times(1)).save(note);
	}
	
	@Test
	@WithMockUser
	public void testGet() throws Exception {
		Note note = getNote();
		when(noteService.get(note.getId())).thenReturn(note);
		
		mockMvc.perform(get(RestUri.notes + "/" + note.getId(), boardId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id", is(note.getId())))
			.andExpect(jsonPath("$.boardId", is(note.getBoardId())))
			.andExpect(jsonPath("$.title", is(note.getTitle())))
			.andExpect(jsonPath("$.text", is(note.getText())));
		
		verify(noteService, times(1)).get(note.getId());
	}
	
	@Test
	@WithMockUser
	public void testGetAll() throws Exception {
		List<Note> notes = new ArrayList<>(getNotes());
		when(noteService.getNotes(boardId)).thenReturn(notes);
		
		mockMvc.perform(get(RestUri.notes, boardId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(notes.size())))
			.andExpect(jsonPath("$[0]id", is(notes.get(0).getId())))
			.andExpect(jsonPath("$[0]boardId", is(notes.get(0).getBoardId())))
			.andExpect(jsonPath("$[0]title", is(notes.get(0).getTitle())))
			.andExpect(jsonPath("$[0]text", is(notes.get(0).getText())))
			.andExpect(jsonPath("$[1]id", is(notes.get(1).getId())))
			.andExpect(jsonPath("$[1]boardId", is(notes.get(1).getBoardId())))
			.andExpect(jsonPath("$[1]title", is(notes.get(1).getTitle())))
			.andExpect(jsonPath("$[1]text", is(notes.get(1).getText())))
			.andExpect(jsonPath("$[2]id", is(notes.get(2).getId())))
			.andExpect(jsonPath("$[2]boardId", is(notes.get(2).getBoardId())))
			.andExpect(jsonPath("$[2]title", is(notes.get(2).getTitle())))
			.andExpect(jsonPath("$[2]text", is(notes.get(2).getText())));
		
		verify(noteService, times(1)).getNotes(boardId);
	}
	
	@Test
	@WithMockUser
	public void testDelete() throws Exception {
		Note note = getNote();
		
		mockMvc.perform(delete(RestUri.notes + "/" + note.getId(), boardId))
			.andExpect(status().isNoContent());
		
		verify(noteService, times(1)).delete(note.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testNoteNotFound() throws Exception {
		when(noteService.get(anyString())).thenThrow(NotFoundException.class);
		
		mockMvc.perform(get(RestUri.notes + "/wrong_id", boardId))
			.andExpect(status().isNotFound());
		
		verify(noteService, times(1)).get(anyString());
	}
	
	@Test
	@WithMockUser
	public void testAddNoteWithId() throws Exception {
		Note notSavedNote = getNotSavedNote();
		Note note = getNote(notSavedNote);
		notSavedNote.setId("wrong_id");
		when(noteService.save(notSavedNote)).thenReturn(note);
		
		String jsonNote = json(notSavedNote);
		
		mockMvc.perform(post(RestUri.notes, boardId)
				.contentType(contentType)
				.content(jsonNote))
			.andExpect(status().isCreated());
		
		ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
		verify(noteService).save(captor.capture());
		Note capturedNote = captor.getValue();
		assertNull(capturedNote.getId());
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
