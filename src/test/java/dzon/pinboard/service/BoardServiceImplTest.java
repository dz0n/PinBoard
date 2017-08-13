package dzon.pinboard.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.domain.Board;
import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.domain.BoardRole;
import dzon.pinboard.persist.BoardRepository;

@ContextConfiguration(classes=PinboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BoardServiceImplTest {
	@Mock
	private BoardRepository repository;
	@Mock
	private BoardPermissionService boardPermissionService;
	@Autowired @InjectMocks
	private BoardService service;
	private int counter = 0;
	private final String userId = "id_of_user_1";
	
	@After
	public void validate() throws Exception {
		Mockito.validateMockitoUsage();
	}

	@Test
	public void testGetBoards() {
		Collection<Board> boards = getBoardsCollection();
		Collection<BoardPermission> permissions = getBoardPermissions(boards);
		
		when(boardPermissionService.getBoardPermissionsByUser(userId)).thenReturn(permissions);
		for(Board board : boards) {
			when(repository.findOne(board.getId())).thenReturn(board);
		}
		
		Collection<Board> resultBoards = service.getBoards(userId);
		assertEquals(3, resultBoards.size());
		assertEquals(boards, resultBoards);
		
		verify(boardPermissionService, times(1)).getBoardPermissionsByUser(userId);
		verify(repository, times(3)).findOne(any(String.class));
	}
	
	@Test
	public void testGetBoardsWithNullBoardPermission() {
		Collection<Board> boards = getBoardsCollection();
		Collection<BoardPermission> permissions = getBoardPermissions(boards);
		
		when(boardPermissionService.getBoardPermissionsByUser(userId)).thenReturn(permissions);
		for(Board board : boards) {
			when(repository.findOne(board.getId())).thenReturn(null);
		}
		
		Collection<Board> resultBoards = service.getBoards(userId);
		for(Board board : resultBoards) {
			assertNotNull(board);
		}
		
		verify(boardPermissionService, times(1)).getBoardPermissionsByUser(userId);
		verify(repository, times(3)).findOne(any(String.class));
	}
	
	@Test
	public void testGet() {
		Board board = getBoard();
		Mockito.when(repository.findOne(board.getId())).thenReturn(board);
		
		assertEquals(board, service.get(board.getId()));
		Mockito.verify(repository, Mockito.times(1)).findOne(board.getId());
	}
	
	@Test(expected=NotFoundException.class)
	public void testGetNull() {
		Mockito.when(repository.findOne(anyString())).thenReturn(null);
		
		service.get("wrong_id");
	}

	@Test
	public void testSave() {
		Board board = getBoard();
		Board initialBoard = new Board();
		initialBoard.setName(board.getName());
		when(repository.save(initialBoard)).thenReturn(board);
		when(repository.countById(null)).thenReturn(0L);
		
		assertEquals(board, service.save(initialBoard, userId));
		verify(repository, times(1)).save(initialBoard);
		
		ArgumentCaptor<BoardPermission> captorPermission = ArgumentCaptor.forClass(BoardPermission.class);
		verify(boardPermissionService).save(captorPermission.capture());
		BoardPermission permission = captorPermission.getValue();
		assertEquals(BoardRole.OWNER, permission.getRole());
		verify(boardPermissionService, times(1)).save(any());
	}
	
	@Test
	public void testUpdate() {
		Board board = getBoard();
		when(repository.save(board)).thenReturn(board);
		when(repository.countById(board.getId())).thenReturn(1L);
		
		assertEquals(board, service.save(board, userId));
		verify(repository, times(1)).save(board);
		
		verify(boardPermissionService, times(0)).save(any());
	}
	
	@Test
	public void testDelete() {
		Board board = getBoard();
		service.delete(board);
		
		Mockito.verify(repository, Mockito.times(1)).delete(board);
	}
	
	@Test
	public void testDeleteById() {
		String boardId = "board_id_01";
		service.delete(boardId);
		
		Mockito.verify(repository, Mockito.times(1)).delete(boardId);
	}
	
	private Board getBoard() {
		counter++;
		Board board = new Board();
		board.setName("Board #" + counter);
		board.setId("board_id_no_" + counter);
		return board;
	}

	private Collection<Board> getBoardsCollection() {
		Collection<Board> boards = new ArrayList<>();
		boards.add(getBoard());
		boards.add(getBoard());
		boards.add(getBoard());
		return boards;
	}

	private Collection<BoardPermission> getBoardPermissions(Collection<Board> boards) {
		Collection<BoardPermission> permissions = new ArrayList<>();
		for(Board board : boards) {
			permissions.add(
					new BoardPermission(
						"id_" + board.getId(), 
						userId, 
						board.getId(), 
						BoardRole.OWNER));
		}
		return permissions;
	}

}
