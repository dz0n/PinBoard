package dzon.pinboard.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({
	@CompoundIndex(name="userid_boardid_role", unique=true, def="{'userId': 1, 'boardId': 1, 'role': 1}")
})
public class BoardPermission {
	@Id
	private String id;
	private String userId;
	private String boardId;
	private BoardRole role;
	
	public BoardPermission() { }
	
	public BoardPermission(String id, String userId, String boardId, BoardRole role) {
		this.id = id;
		this.userId = userId;
		this.boardId = boardId;
		this.role = role;
	}

	public final String getId() {
		return id;
	}

	public final String getUserId() {
		return userId;
	}

	public final String getBoardId() {
		return boardId;
	}

	public final BoardRole getRole() {
		return role;
	}
	
	public final void setId(String id) {
		this.id = id;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public final void setRole(BoardRole role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardId == null) ? 0 : boardId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardPermission other = (BoardPermission) obj;
		if (boardId == null) {
			if (other.boardId != null)
				return false;
		} else if (!boardId.equals(other.boardId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (role != other.role)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}