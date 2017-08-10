package dzon.pinboard.web;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dzon.pinboard.domain.Note;
import dzon.pinboard.service.NoteService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RestController
@RequestMapping(RestUri.notes)
public class NoteRestController {
	@Autowired
	private NoteService noteService;
	
	@RequestMapping(method=POST)
	public ResponseEntity<?> add(@RequestBody Note note, @PathVariable String boardId) {
		note.setId(null);
		Note newNote = noteService.save(note);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newNote.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(method=PUT, value="/{noteId}")
	public ResponseEntity<?> update(@PathVariable String noteId, @PathVariable String boardId, @RequestBody Note note) {
		note.setId(noteId);
		
		noteService.save(note);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method=GET)
	public Collection<Note> getNotes(@PathVariable String boardId) {
		return noteService.getNotes(boardId);
	}
	
	@RequestMapping(method=GET, value="/{noteId}")
	public Note get(@PathVariable String noteId, @PathVariable String boardId) {
		return noteService.get(noteId);
	}
	
	@RequestMapping(method=DELETE, value="/{noteId}")
	public ResponseEntity<?> delete(@PathVariable String noteId, @PathVariable String boardId) {
		noteService.delete(noteId);
		
		return ResponseEntity.noContent().build();
	}
}
