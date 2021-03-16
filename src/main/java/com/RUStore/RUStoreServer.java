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
		System.out.println("Server got keylen:" + keylen);

		byte[] bkey = fromClient.readNBytes(keylen);
		String keystr = new String(bkey);
		System.out.println("Server got key: " + keystr);
		return keystr;
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
	
			fromClient = new DataInputStream(conn.getInputStream());
			toClient = new DataOutputStream(conn.getOutputStream());
			
			int optype;
			while ((optype = fromClient.readInt()) != DISC) {
				System.out.println("Server got optype:" + optype);
				if (optype == PUT || optype == GET || optype == REMV) {
					String key = readKey();
					
					if (optype == PUT) {
						if (objstore.containsKey(key)) {
							System.out.println("Duplicate key");
							toClient.writeInt(DUPL);
							continue;
						}
						toClient.writeInt(UNIQ);
						System.out.println("Sent confirm unique key");
						int datalen = fromClient.readInt();
						System.out.println("Server got datalen " + datalen);
						
						byte[] data = fromClient.readNBytes(datalen);
						objstore.put(key, new ServerStorage(data));
						System.out.println("Server read " + data.length + " bytes");
						// System.out.println("Server got data: " + new String(data));
						/*
						Byte[] boxdata = new Byte[datalen];
						int i = 0;
						for (byte b : data) {
							boxdata[i++] = b;
						}
						objstore.put(key, boxdata);
						*/
						
					} else if (optype == GET) {
						if (objstore.containsKey(key)) {
							System.out.println("Sending client lib confirm key exists");
							toClient.writeInt(UNIQ);
							byte[] data = objstore.get(key).getBytes();
							toClient.writeInt(data.length);
							toClient.write(data);
							/*
							// byte[] data = new byte[boxdata.length];
							// int i = 0;
							for (Byte b : boxdata) {
								// data[i++] = b.byteValue();
								toClient.writeByte(b.byteValue());
							}
							System.out.println("Server wrote " + boxdata.length + " bytes");
							*/
						}  else {
							toClient.writeInt(DUPL);
							System.out.println("Key doesn't exist in server");
							continue;
						}
					} else {
						// optype == REM
						if (objstore.containsKey(key)) {
							System.out.println("Removing " + key);
							objstore.remove(key);
							toClient.writeInt(0);
						} else {
							System.out.println("Error: " + key + " not in objstore");
							toClient.writeInt(1);
						}
					}
				} else if (optype == LIST) { 
					String[] keys = objstore.keySet().toArray(new String[objstore.keySet().size()]);
					toClient.writeInt(keys.length);
					// Arrays.stream(keys).map(i -> i.getBytes()).forEach(j -> { toClient.writeInt(j.length); toClient.write(j);});
					byte[] strbytes;
					for (String s : keys) {
						strbytes = s.getBytes();
						toClient.writeInt(strbytes.length);
						toClient.write(strbytes);
					}
					System.out.println("Server sent " + keys.length + " keys");
				} else {
					System.out.println("Invalid optype");
				}
			}

			System.out.println("closing the connection");
			fromClient.close();
			toClient.close();
			conn.close();		// close connection
		}
		// svc.close();
	}
}