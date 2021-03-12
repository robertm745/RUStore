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
		System.out.println("Server initialized on port " + port);
		for (;;) {
			Socket conn = svc.accept();	 // wait for a connection
	
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());

			String line;
			while  ((line = fromClient.readLine()) != null) {	// read the data from the client
				System.out.println("got line \"" + line + "\"");	// show what we got

				String result = line.length() + ": " + line.toUpperCase() + '\n';	// do the work

				toClient.writeBytes(result);	// send the result
			}
			System.out.println("closing the connection");
			conn.close();		// close connection
		}

		// svc.close();
	}

}
