package dzon.pinboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import dzon.pinboard.PinboardApplication;
import dzon.pinboard.contracts.DatabaseContract;

@Configuration
@EnableMongoRepositories(basePackageClasses = PinboardApplication.class)
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return DatabaseContract.repositoryName;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}

}
