package com.RUStore;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/* any necessary Java packages here */

public class RUStoreClient {

	/* any necessary class members here */
	String host;
	int port;
	Socket conn;
	DataInputStream fromServer;
	DataOutputStream toServer;
	private static final int PUT = 0;
	private static final int GET = 1;
	private static final int LIST = 2;
	private static final int REMV = 3;
	private static final int DISC = 4;
	private static final int UNIQ = 5;
	private static final int DUPL = 6;
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
		this.fromServer = new DataInputStream(conn.getInputStream());
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
		toServer.writeInt(PUT);
		byte[] bkey = new String(key).getBytes();
		toServer.writeInt(bkey.length);
		toServer.write(bkey);
		int c = fromServer.readInt();
		if (c == DUPL) {
			return 1;
		}
		toServer.writeInt(data.length);
		toServer.write(data);
		return 0;
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
	 * @throws IOException 
	 */
	
	public int put(String key, String file_path) throws IOException {

		// Implement here
		toServer.writeInt(PUT);
		byte[] bkey = new String(key).getBytes();
		toServer.writeInt(bkey.length);
		toServer.write(bkey);
		int c = fromServer.readInt();
		if (c == DUPL) {
			return 1;
		}
		File file = new File(file_path);
		byte[] data = Files.readAllBytes(Paths.get(file.toURI()));
		toServer.writeInt(data.length);
		toServer.write(data);
		return 0;
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
		toServer.writeInt(GET);
		byte[] bkey = key.getBytes();
		toServer.writeInt(bkey.length);
		toServer.write(bkey);
		int c = fromServer.readInt();
		if (c == DUPL) 
			return null;
		int size = fromServer.readInt();
		byte[] data = fromServer.readNBytes(size);
		if (data.length == size) 
			return data;
		else 
			return null;
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
	 * @throws IOException 
	 */
	public int get(String key, String file_path) throws IOException {

		// Implement here
		toServer.writeInt(GET);
		byte[] bkey = key.getBytes();
		toServer.writeInt(bkey.length);
		toServer.write(bkey);
		int c = fromServer.readInt();
		if (c == DUPL) {
			return 1;
		}
		int size = fromServer.readInt();
		byte[] data = fromServer.readNBytes(size);
		FileOutputStream fos = new FileOutputStream(file_path);
		fos.write(data);
		fos.close();
		return 0;

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
	 * @throws IOException 
	 */
	public int remove(String key) throws IOException {

		// Implement here
		byte[] strbytes = key.getBytes();
		toServer.writeInt(REMV);
		toServer.writeInt(strbytes.length);
		toServer.write(strbytes);
		int res = fromServer.readInt();
		return (res == DUPL) ? 1 : 0;
	}

	/**
	 * Retrieves of list of object keys from the object store server
	 * 
	 * @return		List of keys as string array, null if there are no keys.
	 *        		Throw an exception if any other issues occur.
	 * @throws IOException 
	 */
	public String[] list() throws IOException {

		// Implement here
		toServer.writeInt(LIST);
		int listlen = fromServer.readInt();
		if (listlen == 0) 
			return null;
		String[] keylist = new String[listlen];
		int bytlen;
		byte[] strbytes;
		for (int i = 0; i < listlen; i++) {
			bytlen = fromServer.readInt();
			strbytes = fromServer.readNBytes(bytlen);
			keylist[i] = new String(strbytes);
		}
		return keylist;

	}

	/**
	 * Signals to server to close connection before closes 
	 * the client socket.
	 * 
	 * @return		n/a, however throw an exception if any issues occur
	 * @throws IOException 
	 */
	public void disconnect() throws IOException {

		// Implement here
		this.toServer.writeInt(DISC);
		this.fromServer.close();
		this.toServer.close();
		conn.close();
	}

}




























































