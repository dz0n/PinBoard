package dzon.pinboard.persist;

import org.springframework.data.mongodb.repository.MongoRepository;

import dzon.pinboard.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
	
}
