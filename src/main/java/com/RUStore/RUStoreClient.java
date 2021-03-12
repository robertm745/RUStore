package com.RUStore;

import java.net.*;
import java.io.*;

/* any necessary Java packages here */

public class RUStoreClient {

	/* any necessary class members here */
	String host;
	int port;
	Socket conn;
	BufferedReader fromServer;
	DataOutputStream toServer;

	/**
	 * RUStoreClient Constructor, initializes default values for class members
	 *
	 * @param host host url
	 * @param port port number
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public RUStoreClient(String host, int port) {

		// Implement here

		this.host = host;
		this.port = port;


	}

	/**
	 * Opens a socket and establish a connection to the object store server
	 * running on a given host and port.
	 *
	 * @return		n/a, however throw an exception if any issues occur
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void connect() throws UnknownHostException, IOException {

		// Implement here
		this.conn = new Socket(host, port);
		this.fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		this.toServer = new DataOutputStream(conn.getOutputStream());

	}

	/**
	 * Sends an arbitrary data object to the object store server. If an 
	 * object with the same key already exists, the object should NOT be 
	 * overwritten
	 * 
	 * @param key	key to be used as the unique identifier for the object
	 * @param data	byte array representing arbitrary data object
	 * 
	 * @return		0 upon success
	 *        		1 if key already exists
	 *        		Throw an exception otherwise
	 * @throws IOException 
	 */
	public int put(String key, byte[] data) throws IOException {

		// Implement here
		System.out.println("Internal client writing: " + key);
		toServer.writeBytes(key + '\n');
		System.out.println("Internal client wrote: " + key);
		return -1;

	}

	/**
	 * Sends an arbitrary data object to the object store server. If an 
	 * object with the same key already exists, the object should NOT 
	 * be overwritten.
	 * 
	 * @param key	key to be used as the unique identifier for the object
	 * @param file_path	path of file data to transfer
	 * 
	 * @return		0 upon success
	 *        		1 if key already exists
	 *        		Throw an exception otherwise
	 */
	
	public int put(String key, String file_path) {

		// Implement here
		return -1;

	}
	

	/**
	 * Downloads arbitrary data object associated with a given key from the object
	 * store server.
	 * 
	 * @param key key associated with the object
	 * 
	 * @return object data as a byte array, null if key doesn't exist. Throw an
	 *         exception if any other issues occur.
	 * @throws IOException
	 */
	public byte[] get(String key) throws IOException {

		// Implement here
		System.out.println("Attempting to read from server (internal client get):");
		String line = fromServer.readLine();
		System.out.println("Got (internal @ client get): " + line);
		return line.getBytes();

		//return null;

	}

	/**
	 * Downloads arbitrary data object associated with a given key
	 * from the object store server and places it in a file. 
	 * 
	 * @param key	key associated with the object
	 * @param	file_path	output file path
	 * 
	 * @return		0 upon success
	 *        		1 if key doesn't exist
	 *        		Throw an exception otherwise
	 */
	public int get(String key, String file_path) {

		// Implement here
		return -1;

	}

	/**
	 * Removes data object associated with a given key 
	 * from the object store server. Note: No need to download the data object, 
	 * simply invoke the object store server to remove object on server side
	 * 
	 * @param key	key associated with the object
	 * 
	 * @return		0 upon success
	 *        		1 if key doesn't exist
	 *        		Throw an exception otherwise
	 */
	public int remove(String key) {

		// Implement here
		return -1;

	}

	/**
	 * Retrieves of list of object keys from the object store server
	 * 
	 * @return		List of keys as string array, null if there are no keys.
	 *        		Throw an exception if any other issues occur.
	 */
	public String[] list() {

		// Implement here
		return null;

	}

	/**
	 * Signals to server to close connection before closes 
	 * the client socket.
	 * 
	 * @return		n/a, however throw an exception if any issues occur
	 */
	public void disconnect() {

		// Implement here

	}

}
