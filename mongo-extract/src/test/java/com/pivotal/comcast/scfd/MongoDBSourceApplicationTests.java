package com.pivotal.comcast.scfd;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.pivotal.comcast.scdf.MongoDBSourceConfiguration;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MongoDBSourceApplicationTests.MongoSourceApplication.class)
@WebIntegrationTest(randomPort = true)
@DirtiesContext
public abstract class MongoDBSourceApplicationTests {

	private static final MongodStarter starter = MongodStarter.getDefaultInstance();
	private MongodExecutable _mongodExe;
	private MongodProcess _mongod;
	private MongoClient _mongo;

//	@Autowired
//	@Bindings(MongoDBSourceConfiguration.class)
//	protected Source source;
//
//	@Autowired
//	protected MessageCollector messageCollector;

	@Before
	public void setUp() throws Exception {
		_mongodExe = starter.prepare(new MongodConfigBuilder()
				.version(Version.Main.PRODUCTION)
				.net(new Net(12345, Network.localhostIsIPv6()))
				.build());
		_mongod = _mongodExe.start();
		_mongo = new MongoClient("localhost", 12345);

		DB db = _mongo.getDB("test");
		DBCollection col = db.createCollection("testing", new BasicDBObject());
		col.save(new BasicDBObject("testDoc", new Date()));
		col.save(new BasicDBObject("testDoc1", new Date()));
		col.save(new BasicDBObject("testDoc2", new Date()));
	}

	@After
	public void tearDown() throws Exception {
		_mongod.stop();
		_mongodExe.stop();
	}

//	public static class DefaultPropertiesTests extends MongoDBSourceApplicationTests {
//		@Test
//		public void test() throws InterruptedException {
//			Message<?> received = messageCollector.forChannel(source.output()).poll(11, TimeUnit.SECONDS);
//			assertThat(received, CoreMatchers.notNullValue());
//			assertEquals(3, messageCollector.forChannel(source.output()).size());
//		}
//	}

	@SpringBootApplication
	public static class MongoSourceApplication {

	}


}
