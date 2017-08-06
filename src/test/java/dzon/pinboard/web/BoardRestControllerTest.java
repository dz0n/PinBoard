package dzon.pinboard.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
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
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.domain.Board;
import dzon.pinboard.domain.User;
import dzon.pinboard.service.BoardService;
import dzon.pinboard.service.NotFoundException;
import dzon.pinboard.service.UserService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardRestControllerTest extends RestControllerTest {
	@Mock
	private BoardService boardService;
	@Mock
	private UserService userService;
	@Autowired @InjectMocks
	private BoardRestController controller;
	private final String userName = "user@email.com";
	private final String userId = "user_id";
	private final String hash = "dfewf34r3t4354gbq3gesgt";
	
	@Before
	public void setUp() throws Exception {
		User user = new User(userName);
		user.setId(userId);
		user.setHash(hash);
		when(userService.getUserByEmail(userName)).thenReturn(user);
	}
	
	@After
	public void validate() throws Exception {
		Mockito.validateMockitoUsage();
	}

	@Test
	@WithMockUser(username=userName)
	public void testAdd() throws Exception {
		when(boardService.save(getInitialBoard(), userId)).thenReturn(getBoard(getInitialBoard()));
		
		Board board = getInitialBoard();
		String jsonBoard = json(board);
		
		this.mockMvc.perform(post(RestUri.boards)
				.contentType(contentType)
				.content(jsonBoard))
			.andExpect(status().isCreated());
		
		verify(boardService, times(1)).save(getInitialBoard(), userId);
	}
	
	@Test
	@WithMockUser(username=userName)
	public void testUpdate() throws Exception {
		when(boardService.save(getInitialBoard(), userId)).thenReturn(getBoard(getInitialBoard()));
		
		Board board = getBoard(getInitialBoard());
		String jsonBoard = json(board);
		
		mockMvc.perform(put(RestUri.boards + "/" + board.getId())
				.contentType(contentType)
				.content(jsonBoard))
			.andExpect(status().isOk());
		
		verify(boardService, times(1)).save(board, userId);
	}

	@Test
	@WithMockUser(username=userName)
	public void testGetBoard() throws Exception {
		Board board = getBoard(getInitialBoard());
		when(boardService.get(board.getId())).thenReturn(board);
		
		mockMvc.perform(get(RestUri.boards + "/" + board.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(board.getId())))
				.andExpect(jsonPath("$.name", is(board.getName())));
		
		verify(boardService, times(1)).get(board.getId());
	}

	@Test
	@WithMockUser(username=userName)
	public void testGetAllBoards() throws Exception {
		List<Board> boards = getBoards();
		when(boardService.getBoards(userId)).thenReturn(boards);
		
		mockMvc.perform(get(RestUri.boards))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(boards.get(0).getId())))
				.andExpect(jsonPath("$[0].name", is(boards.get(0).getName())))
				.andExpect(jsonPath("$[1].id", is(boards.get(1).getId())))
				.andExpect(jsonPath("$[1].name", is(boards.get(1).getName())))
				.andExpect(jsonPath("$[2].id", is(boards.get(2).getId())))
				.andExpect(jsonPath("$[2].name", is(boards.get(2).getName())));
		
		verify(boardService, times(1)).getBoards(userId);
	}
	
	@Test
	@WithMockUser(username=userName)
	public void testDelete() throws Exception {
		Board board = getBoard(getInitialBoard());
		
		mockMvc.perform(delete(RestUri.boards + "/" + board.getId()))
				.andExpect(status().isNoContent());
		
		verify(boardService, times(1)).delete(board.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser(username=userName)
	public void testBoardNotFound() throws Exception {
		when(boardService.get(any(String.class))).thenThrow(NotFoundException.class);
		
		mockMvc.perform(get(RestUri.boards + "/wrong_id"))
			.andExpect(status().isNotFound());
	}
	
	private Board getInitialBoard() {
		Board board = new Board();
		board.setName("Board #x");
		return board;
	}
	
	private Board getBoard(Board board) {
		board.setId("board_id_no_x");
		return board;
	}
	
	private List<Board> getBoards() {
		List<Board> boards = new ArrayList<>();
		for(int i=1; i<=3; i++) {
			Board board = getBoard(getInitialBoard());
			board.setName(board.getName() + i);
			board.setId(board.getId() + i);
			boards.add(board);
		}
		return boards;
	}
}