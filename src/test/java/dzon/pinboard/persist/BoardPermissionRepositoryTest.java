package dzon.pinboard.persist;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dzon.pinboard.config.MongoTestConfig;
import dzon.pinboard.domain.BoardPermission;
import dzon.pinboard.domain.BoardRole;
import static dzon.pinboard.domain.BoardRole.*;

@ContextConfiguration(classes=MongoTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BoardPermissionRepositoryTest {
	@Autowired
	private BoardPermissionRepository repository;
	
	@Before
	public void setUp() throws Exception {
		repository.deleteAll();
	}

	@Test
	public void testFindBoardPermissionsByUserId() {
		BoardPermission permission1 = getBoardPermission(OWNER);
		BoardPermission permission2 = getBoardPermission(USER);
		BoardPermission permission3 = getBoardPermission(READER);
		
		repository.save(permission1);
		repository.save(permission2);
		repository.save(permission3);
		
		Collection<BoardPermission> permissions = repository.findBoardPermissionsByBoardId(permission1.getBoardId());
		assertEquals(3,  permissions.size());
		assertTrue(permissions.contains(permission1));
		assertTrue(permissions.contains(permission2));
		assertTrue(permissions.contains(permission3));
	}

	@Test
	public void testFindBoardPermissionsByBoardId() {
		BoardPermission permission1 = getBoardPermission(OWNER);
		BoardPermission permission2 = getBoardPermission(USER);
		BoardPermission permission3 = getBoardPermission(READER);
		
		repository.save(permission1);
		repository.save(permission2);
		repository.save(permission3);
		
		Collection<BoardPermission> permissions = repository.findBoardPermissionsByUserId(permission1.getUserId());
		assertEquals(3,  permissions.size());
		assertTrue(permissions.contains(permission1));
		assertTrue(permissions.contains(permission2));
		assertTrue(permissions.contains(permission3));
	}
	
	@Test
	public void testDuplicatePermissionSave() {
		assertEquals(0,  repository.count());
		BoardPermission permission = getBoardPermission(OWNER);
		repository.save(permission);
		repository.save(permission);
		repository.save(permission);
		assertEquals(1,  repository.count());
	}
	
	private BoardPermission getBoardPermission(BoardRole role) {
		return new BoardPermission(
				null,
				"user_id_1",
				"board_id_1",
				role);
	}
}
