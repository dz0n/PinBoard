package dzon.pinboard.config;

import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.persist.DatabaseContract;

@EnableMongoRepositories(basePackageClasses = PinboardApplication.class)
public class MongoTestConfig extends AbstractMongoConfiguration {
	@Override
	protected String getDatabaseName() {
		return DatabaseContract.testRepositoryName;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}
}
