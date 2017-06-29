package dzon.pinboard.persist;

import static org.junit.Assert.assertEquals;

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
		assertEquals(note.getText(), notes.get(0).getText());
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

	private Note getNote() {
		noteCount++;
		return new Note("Text no. " + noteCount);
	}

}
