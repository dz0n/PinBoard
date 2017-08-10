package dzon.pinboard.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.domain.Note;

@ContextConfiguration(classes=MongoTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NoteRepositoryTest {
	@Autowired
	private NoteRepository noteRepository;
	private static int noteCount = 0;
	
	@Before
	public void setUp() throws Exception {
		noteRepository.deleteAll();
	}
	
	@After
	public void cleanUp() {
		noteRepository.deleteAll();
	}
	
	@Test
	public void saveTest() {
		assertEquals(0, noteRepository.count());
		
		Note note = getNote();
		noteRepository.save(note);
		
		assertEquals(1,  noteRepository.count());
		List<Note> notes = noteRepository.findAll();
		assertEquals(note, notes.get(0));
	}

	@Test
	public void deleteTest() {
		noteRepository.save(getNote());
		Note note = noteRepository.save(getNote());
		noteRepository.save(getNote());
		assertEquals(3, noteRepository.count());
		
		noteRepository.delete(note);
		assertEquals(2, noteRepository.count());
	}
	
	@Test
	public void updateTest() {
		Note note = noteRepository.save(getNote());
		String text = "another test's text";
		note.setText(text);
		noteRepository.save(note);
		
		assertEquals(text, noteRepository.findAll().get(0).getText());
	}
	
	@Test
	public void findOneByIdTest() {
		Note note = noteRepository.save(getNote());
		
		Note noteFromRepo = noteRepository.findOne(note.getId());
		assertEquals(note,  noteFromRepo);
	}
	
	@Test
	public void findAllInBoardTest() {
		Note note1 = noteRepository.save(getNote());
		Note note2 = noteRepository.save(getNote());
		Note note3 = noteRepository.save(getNote());
		
		Collection<Note> notes = noteRepository.findNotesByBoardId(note1.getBoardId());
		assertEquals(3, notes.size());
		assertTrue("note1 not found in list of notes", notes.contains(note1));
		assertTrue("note2 not found in list of notes", notes.contains(note2));
		assertTrue("note3 not found in list of notes", notes.contains(note3));
	}

	private Note getNote() {
		noteCount++;
		Note note = new Note();
		note.setText("Text no. " + noteCount);
		note.setBoardId("id_board");
		note.setId("note_id_" + noteCount);
		note.setTitle("Title no. " + noteCount);
		return note;
	}

}
