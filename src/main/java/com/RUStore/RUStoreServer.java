package com.RUStore;


/* any necessary Java packages here */
import java.net.*;
import java.io.*;

public class RUStoreServer {

	/* any necessary class members here */

	/* any necessary helper methods here */

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
		ServerSocket svc = new ServerSocket(port, 5);
		System.out.println("Server initialized...");
		for (;;) {
			Socket conn = svc.accept();
			System.out.println("Got a new connection");
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());
			// DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());
			String line;
			line = fromClient.readLine();
			toClient.writeBytes(line.toUpperCase() + '\n');
			
			fromClient.close();
			toClient.close();
			conn.close();
		}
		// svc.close();
	}

}
