package com.RUStore;


/* any necessary Java packages here */
import java.net.*;
import java.io.*;
import java.util.*;

public class RUStoreServer {

	/* any necessary class members here */
	private static HashMap<String,ServerStorage> objstore;
	private static DataInputStream fromClient;
	private static DataOutputStream toClient;
	
	private static final int PUT = 0;
	private static final int GET = 1;
	private static final int LIST = 2;
	private static final int REMV = 3;
	private static final int DISC = 4;
	private static final int UNIQ = 5;
	private static final int DUPL = 6;

	/* any necessary helper methods here */
	private static String readKey() throws IOException {
		int keylen = fromClient.readInt();
		byte[] bkey = fromClient.readNBytes(keylen);
		return (bkey.length == keylen) ? new String(bkey) : null;
	}
	
	private static void goPut(String key) throws IOException {
		System.out.println("Putting object with key " + key);
		toClient.writeInt(UNIQ);
		int datalen = fromClient.readInt();
		byte[] data = fromClient.readNBytes(datalen);
		objstore.put(key, new ServerStorage(data));
	}
	
	private static void goGet(String key) throws IOException {
		System.out.println("Retrieving object with key " + key);
		toClient.writeInt(UNIQ);
		byte[] data = objstore.get(key).getBytes();
		toClient.writeInt(data.length);
		toClient.write(data);
	}
	
	private static void goRemove(String key) throws IOException {
		System.out.println("Removing object with key " + key);
		objstore.remove(key);
		toClient.writeInt(UNIQ);
	}

	private static void goList() throws IOException {
		System.out.println("Sending list of object keys...");
		toClient.writeInt(objstore.keySet().size());
		byte[] strbytes;
		for (String s : objstore.keySet()) {
			strbytes = s.getBytes();
			toClient.writeInt(strbytes.length);
			toClient.write(strbytes);
		}
	}
	/**
	 * RUObjectServer Main(). Note: Accepts one argument -> port number
	 * 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {

		// Check if at least one argument that is potentially a port number
		if (args.length != 1) {
			System.out.println("Invalid number of arguments. You must provide a port number.");
			return;
		}

		// Try and parse port # from argument
		int port = Integer.parseInt(args[0]);

		// Implement here //
		objstore = new HashMap<String, ServerStorage>();
		ServerSocket svc = new ServerSocket(port, 5);
		System.out.println("Server initialized on port " + port);
		for (;;) {
			Socket conn = svc.accept();	 // wait for a connection
			System.out.println("Connected with new client.");
			
			fromClient = new DataInputStream(conn.getInputStream());
			toClient = new DataOutputStream(conn.getOutputStream());
			
			int optype;
			try {
				while ((optype = fromClient.readInt()) != DISC) {
					if (optype == PUT || optype == GET || optype == REMV) {
						String key = readKey();
						if (key == null) {
							System.out.println("Error: server received corrupted key");
							toClient.writeInt(DUPL);
							break;
						}
						if (!objstore.containsKey(key)) {
							if (optype == PUT) 
								goPut(key);
							else 
								toClient.writeInt(DUPL);
						} else {
							if (optype == GET) 
								goGet(key);
							else if (optype == REMV)
								goRemove(key);
							else
								toClient.writeInt(DUPL);
						}
					} else if (optype == LIST) {
						goList();
					} else {
						System.out.println("Received invalid optype: " + optype);
						break;
					}
				}
				
				System.out.println("closing the connection");
				fromClient.close();
				toClient.close();
				conn.close();		// close connection
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Server and client connection compromised.");
			}

		}
		// svc.close();
	}
}