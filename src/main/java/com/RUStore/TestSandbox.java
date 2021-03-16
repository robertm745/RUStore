package com.RUStore;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This TestSandbox is meant for you to implement and extend to 
 * test your object store as you slowly implement both the client and server.
 * 
 * If you need more information on how an RUStorageClient is used
 * take a look at the RUStoreClient.java source as well as 
 * TestSample.java which includes sample usages of the client.
 */
public class TestSandbox{

	public static void main(String[] args) {

		// Create a new RUStoreClient
		RUStoreClient client = new RUStoreClient("localhost", 12345);
		String line, type, key;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Open a connection to a remote service
		System.out.println("Connecting to object server...");
		try {
			client.connect();
			System.out.println("Established connection to server.");
			while ((line = br.readLine()) != null) {
				String[] values = line.split(":");
				if (values[0].equals("P")) {
					key = values[1];					
					System.out.println("Sending " + key);
					int confirm = client.put(key, values[2].getBytes());
					if (confirm != 0) {
						System.out.println("Error: message unconfirmed");
					}
				} else if (values[0].equals("L")){
					System.out.println("Reqesting list...");
					String[] list = client.list();
					System.out.println("Listing keys in client app:");
					Arrays.stream(list).forEach(System.out::println);
					System.out.println("End of keys recvd in client app");
				} else if (values[0].equals("G")) {
					key = values[1];
					System.out.println("Requesting data for key: " + key);
					byte[] data = client.get(key);
					if (data.length > 0)
						System.out.println("Client app - Data received: " + new String(data));
				} else if (values[0].equals("R")) {
					int res = client.remove(values[1]);
					System.out.println("Client app got removal result: " + res);
				} else if (values[0].equals("D")) {
					client.disconnect();
					break;
				} else {
					System.out.println("Error: invalid operation -- " + line);
				}
				System.out.println("");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to server.");
		}

	}

}
