package dzon.pinboard.persist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.domain.Board;
import dzon.pinboard.domain.BoardRole;

@ContextConfiguration(classes=MongoTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BoardRepositoryTest {
	@Autowired
	private BoardRepository repository;
	
	@Before
	public void setUp() throws Exception {
		repository.deleteAll();
	}

	@Test
	public void testSave() {
		assertEquals(0, repository.count());
		Board board = getBoard();
		repository.save(board);
		assertEquals(1, repository.count());
	}
	
	@Test
	public void testFind() {
		Board board = getBoard();
		board = repository.save(board);
		
		Board boardFromRepo = repository.findOne(board.getId());
		
		assertEquals(board, boardFromRepo);
	}
	
	private Board getBoard() {
		Board board = new Board();
		board.setName("bla board");
		board.addUserId("bla1", BoardRole.OWNER);
		board.addUserId("bla2", BoardRole.USER);
		return board;
	}
}
