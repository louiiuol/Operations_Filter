package com.mybank.app.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.mybank.app.exceptions.HostUnavailableException;


/**
 *
 * <p>
 * 		This class configures and creates a connection to Elastic Search
 * </p>
 *
 * @see java.net.InetAddress
 * @see org.elasticsearch.client.transport.TransportClient
 * @see org.elasticsearch.common.settings.Settings
 *
 */

public class ElasticSearchConfig {

	private static final Logger LOG = LogManager.getLogger();

	private static final String HOST = "localhost";
	private static final String CLUSTER = "mybank";
	private static final int PORT = 9300;

	private static final Settings SETTINGS = Settings.builder().put("cluster.name", CLUSTER).build();
	private static TransportClient client = null;

	private ElasticSearchConfig() { }


	/**
	 *
	 * <p>
	 * 		This method return a singleton instance of ElasticSearchConfig
	 * </p>
	 *
	 */

	public static ElasticSearchConfig getInstance() { return new ElasticSearchConfig(); }


	/**
	 *
	 * <p>
	 * 		This method create connection to Elastic Search
	 * </p>
	 *
	 * @return 	client: entity containing the connection SETTINGS and addresses to Elastic Search
	 * @see 	org.elasticsearch.client.transport.TransportClient
	 * @see 	org.elasticsearch.common.transport.InetSocketTransportAddress
	 * @see 	org.elasticsearch.transport.client.PreBuiltTransportClient
	 *
	 */

	public TransportClient openClient() throws HostUnavailableException {
		if (client == null) {
			try {

				// Creating connection to Elastic Search
				LOG.info("Creating connection to Elastic Search ...");
				client = new PreBuiltTransportClient(SETTINGS);
				client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

				// Testing the connection
				testClient(client);
				LOG.info("Connection established on the address: " + client.transportAddresses());
			} catch (UnknownHostException e) {

				// Throwing exception if the address is unreachable
				LOG.error("Cluster: " + CLUSTER + " - Host: " + HOST + " - Port: " + PORT);
				throw new HostUnavailableException("The address is unreachable, please check the hostname: " + HOST);
			}
		}
		return client;
	}


	/**
	 *
	 * <p>
	 * 		This method test the Elastic Search connection
	 * </p>
	 *
	 * @param 	client entity containing the connection SETTINGS and addresses to Elastic Search
	 *
	 */

	public void testClient(TransportClient client) throws HostUnavailableException {
		LOG.info("Searching for nodes...");
		if (client.connectedNodes().isEmpty()) {
			// Throwing exception if the address is unreachable
			LOG.error("No nodes available. Verify ES is running!");
			throw new HostUnavailableException("No nodes available. Verify ES is running!");
		} else {
			LOG.info("connected to nodes: " + client.connectedNodes().toString());
		}
	}


	/**
	 *
	 * <p>
	 * 		This method close the Elastic Search connection
	 * </p>
	 *
	 */

	public void closeClient() {
		// Closing Elastic Search Connection
		LOG.info("Closing connection ...");
		client.close();
		LOG.info("Connection closed");

	}

}
