package utils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.typesafe.config.ConfigFactory;

import play.Logger;

public class CouchbaseConfig {
	
	
	
	private static final Logger.ALogger logger=Logger.of(CouchbaseConfig.class);
	
	
	public static Bucket student = null;

	public CouchbaseConfig() {
	logger.info("Couchbase cluster connected from Service Master MS...");
	connect();
	}

	private void connect() {
	CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
	.connectTimeout(100000)
	.socketConnectTimeout(1000000)
	.autoreleaseAfter(500000).build();

	Cluster cluster = CouchbaseCluster.create(env, ConfigFactory.load()
	.getString("doc.couchbase.nodes"));
	cluster.authenticate(ConfigFactory.load().getString("doc.couchbase.username"),ConfigFactory.load().getString("doc.couchbase.password"));

	logger.info("connectivity established with Service Master bucket...");

	student = cluster.openBucket(ConfigFactory.load()
	.getString("doc.couchbase.url"));
	}
	
	
	
	
	

}
