package dzon.pinboard.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

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

import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.domain.BoardRole;
import dzon.pinboard.service.BoardPermissionService;
import dzon.pinboard.web.ControllerContract.RestUri;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardPermissionRestControllerTest extends RestControllerTest {
	@Mock
	private BoardPermissionService service;
	@Autowired @InjectMocks
	private BoardPermissionRestController controller;
	private final String boardId = "board_id_1";
	private final String ownerId = "owner_id_1";
	private final String userId = "user_id_1";
	private final String readerId = "reader_id_1";
	
	@After
	public void validate() throws Exception {
		Mockito.validateMockitoUsage();
	}
	
	@Test
	@WithMockUser
	public void testAddUser() throws Exception {
		when(service.save(getNotSavedUser())).thenReturn(getUser());
		
		String jsonPermission = json(getNotSavedUser());
		
		mockMvc.perform(post(RestUri.permissions, boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isCreated());
		
		verify(service, times(1)).save(getNotSavedUser());
	}

	@Test
	@WithMockUser
	public void testAddReader() throws Exception {
		when(service.save(getNotSavedReader())).thenReturn(getReader());
		
		String jsonPermission = json(getNotSavedReader());
		
		mockMvc.perform(post(RestUri.permissions, boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isCreated());
		
		verify(service, times(1)).save(getNotSavedReader());
	}
	
	@Test
	@WithMockUser
	public void testAddOwnerAndForbid() throws Exception {
		String jsonPermission = json(getNotSavedOwner());
		
		mockMvc.perform(post(RestUri.permissions, boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isForbidden());
		
		verify(service, times(0)).save(any());
	}
	
	@Test
	@WithMockUser
	public void testAddWithId() throws Exception {
		when(service.save(getNotSavedUser())).thenReturn(getUser());
		
		String jsonPermission = json(getUser());
		
		mockMvc.perform(post(RestUri.permissions, boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isCreated());
		
		ArgumentCaptor<BoardPermission> captor = ArgumentCaptor.forClass(BoardPermission.class);
		verify(service).save(captor.capture());
		BoardPermission permission = captor.getValue();
		assertNull(permission.getId());
	}
	
	@Test
	@WithMockUser
	public void testDeleteUser() throws Exception {
		BoardPermission permission = getUser();
		when(service.get(permission.getId())).thenReturn(permission);
		
		mockMvc.perform(delete(RestUri.permissions + "/" + permission.getId(), boardId))
			.andExpect(status().isNoContent());
		
		verify(service, times(1)).delete(permission.getId());
	}

	@Test
	@WithMockUser
	public void testDeleteReader() throws Exception {
		BoardPermission permission = getReader();
		when(service.get(permission.getId())).thenReturn(permission);
		
		mockMvc.perform(delete(RestUri.permissions + "/" + permission.getId(), boardId))
			.andExpect(status().isNoContent());
		
		verify(service, times(1)).delete(permission.getId());
	}
	
	@Test
	@WithMockUser
	public void testDeleteOwnerAndForbid() throws Exception {
		BoardPermission permission = getOwner();
		when(service.get(permission.getId())).thenReturn(permission);
		
		mockMvc.perform(delete(RestUri.permissions + "/" + permission.getId(), boardId))
		.andExpect(status().isForbidden());
		
		verify(service, times(0)).delete(any());
	}
	
	@Test
	@WithMockUser
	public void testUpdateUser() throws Exception {
		when(service.save(getUpdatedUser())).thenReturn(getUpdatedUser());
		when(service.get(getUpdatedUser().getId())).thenReturn(getUpdatedUser());
		
		String jsonPermission = json(getUpdatedUser());
		
		mockMvc.perform(put(RestUri.permissions + "/" + getUpdatedUser().getId(), boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isOk());
		
		verify(service, times(1)).save(getUpdatedUser());
	}

	@Test
	@WithMockUser
	public void testUpdateReader() throws Exception {
		when(service.save(getUpdatedReader())).thenReturn(getUpdatedReader());
		when(service.get(getUpdatedReader().getId())).thenReturn(getUpdatedReader());
		
		String jsonPermission = json(getUpdatedReader());
		
		mockMvc.perform(put(RestUri.permissions + "/" + getUpdatedReader().getId(), boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isOk());
		
		verify(service, times(1)).save(getUpdatedReader());
	}
	
	@Test
	@WithMockUser
	public void testUpdateToOwnerAndForbid() throws Exception {
		BoardPermission permission = getUser();
		permission.setRole(BoardRole.OWNER);
		
		when(service.get(permission.getId())).thenReturn(getUser());
		
		String jsonPermission = json(permission);
		
		mockMvc.perform(put(RestUri.permissions + "/" + permission.getId(), boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isForbidden());
		
		verify(service, times(0)).save(any());
	}
	
	@Test
	@WithMockUser
	public void testUpdateFromOwnerAndForbid() throws Exception {
		when(service.get(getUpdatedOwner().getId())).thenReturn(getOwner());
		
		String jsonPermission = json(getUpdatedOwner());
		
		mockMvc.perform(put(RestUri.permissions + "/" + getUpdatedOwner().getId(), boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isForbidden());
		
		verify(service, times(0)).save(any());
	}
	
	@Test
	@WithMockUser
	public void testUpdateWithWrongId() throws Exception {
		when(service.get(getUser().getId())).thenReturn(getUser());
		when(service.save(getUser())).thenReturn(getUser());
		
		String jsonPermission = json(getNotSavedUser());
		
		mockMvc.perform(put(RestUri.permissions + "/" + getUser().getId(), boardId)
				.contentType(contentType)
				.content(jsonPermission))
			.andExpect(status().isOk());
		
		verify(service, times(1)).save(getUser());
	}
	
	@Test
	@WithMockUser
	public void testGet() throws Exception {
		BoardPermission permission = getUser();
		when(service.get(permission.getId())).thenReturn(permission);
		
		mockMvc.perform(get(RestUri.permissions + "/" + getUser().getId(), boardId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id", is(permission.getId())))
			.andExpect(jsonPath("$.boardId", is(permission.getBoardId())))
			.andExpect(jsonPath("$.userId", is(permission.getUserId())))
			.andExpect(jsonPath("$.role", is(permission.getRole().toString())));
		
		verify(service, times(1)).get(permission.getId());
	}
	
	@Test
	@WithMockUser
	public void testGetAll() throws Exception {
		List<BoardPermission> permissions = getPermissions();
		when(service.getBoardPermissions(permissions.get(0).getBoardId())).thenReturn(permissions);
		
		mockMvc.perform(get(RestUri.permissions, boardId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(permissions.size())))
			.andExpect(jsonPath("$[0]id", is(permissions.get(0).getId())))
			.andExpect(jsonPath("$[0]boardId", is(permissions.get(0).getBoardId())))
			.andExpect(jsonPath("$[0]userId", is(permissions.get(0).getUserId())))
			.andExpect(jsonPath("$[0]role", is(permissions.get(0).getRole().toString())))
			.andExpect(jsonPath("$[1]id", is(permissions.get(1).getId())))
			.andExpect(jsonPath("$[1]boardId", is(permissions.get(1).getBoardId())))
			.andExpect(jsonPath("$[1]userId", is(permissions.get(1).getUserId())))
			.andExpect(jsonPath("$[1]role", is(permissions.get(1).getRole().toString())))
			.andExpect(jsonPath("$[2]id", is(permissions.get(2).getId())))
			.andExpect(jsonPath("$[2]boardId", is(permissions.get(2).getBoardId())))
			.andExpect(jsonPath("$[2]userId", is(permissions.get(2).getUserId())))
			.andExpect(jsonPath("$[2]role", is(permissions.get(2).getRole().toString())));
		
		verify(service, times(1)).getBoardPermissions(permissions.get(0).getBoardId());
	}

	private BoardPermission getNotSavedUser() {
		return new BoardPermission(null, userId, boardId, BoardRole.USER);
	}
	
	private BoardPermission getUser() {
		return new BoardPermission("permission_id_1", userId, boardId, BoardRole.USER);
	}
	
	private BoardPermission getUpdatedUser() {
		return new BoardPermission("permission_id_1", userId, boardId, BoardRole.READER);
	}
	
	private BoardPermission getNotSavedOwner() {
		return new BoardPermission(null, ownerId, boardId, BoardRole.OWNER);
	}
	
	private BoardPermission getOwner() {
		return new BoardPermission("permission_id_2", ownerId, boardId, BoardRole.OWNER);
	}
	
	private BoardPermission getUpdatedOwner() {
		return new BoardPermission("permission_id_2", ownerId, boardId, BoardRole.USER);
	}
	
	private BoardPermission getNotSavedReader() {
		return new BoardPermission(null, readerId, boardId, BoardRole.READER);
	}
	
	private BoardPermission getReader() {
		return new BoardPermission("permission_id_3", readerId, boardId, BoardRole.READER);
	}
	
	private BoardPermission getUpdatedReader() {
		return new BoardPermission("permission_id_3", readerId, boardId, BoardRole.USER);
	}
	
	private List<BoardPermission> getPermissions() {
		List<BoardPermission> permissions = new ArrayList<>();
		permissions.add(getOwner());
		permissions.add(getUser());
		permissions.add(getReader());
		return permissions;
	}
}
