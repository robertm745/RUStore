package com.RUStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestSample{

	public static void main(String[] args){

		String host;
		int port;
		RUStoreClient client;

		String stringKey;
		String stringValue;
		int ret;
		byte[] retBytes;
		String outString;

		String fileKey;
		String inputPath;
		String outputPath;
		File fileIn;
		File fileOut;
		byte[] fileInBytes;
		byte[] fileOutBytes;   

		// Check if at least two argument that is a host and port number
		if(args.length != 2) {
			System.out.println("Invalid number of arguments. You must provide a host and port number");
			System.out.println("Usage: TestSample <host> <port>");
			return;
		}

		// Get arguments
		host = args[0];
		port = Integer.parseInt(args[1]);

		// Creating new Client Object
		client = new RUStoreClient(host, port);

		// Connect to server
		System.out.println("Connecting to object server at " + host + ":" + port + "...");
		try {
			client.connect();
			System.out.println("Sucessfully established connection to object server.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to object server. Exiting...");
			return;
		}

		// Store String
		stringKey = "str_1";
		stringValue = "Hello World";

		// PUT String
		try {
			System.out.println("Putting string \"" + stringValue + "\" with key \"" + stringKey + "\"");
			ret = client.put(stringKey, stringValue.getBytes());
			if(ret == 0) {
				System.out.println("Successfully put string and key!");
			}else {
				System.out.println("Failed to put string \"" + stringValue + "\" with key \"" + stringKey + "\". Key already exists. (INCORRECT RETURN)");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Failed to put string \"" + stringValue + "\" with key \"" + stringKey + "\". Exception occured.");
		} 


		// GET String and test it
		try {
			System.out.println("Getting object with key \"" + stringKey + "\"");
			retBytes = client.get(stringKey);
			outString = new String(retBytes);
			if(retBytes != null) {
				if(stringValue.equals(outString)) {
					System.out.println("Successfully got string: " + outString);
				}else {
					System.out.println("Failed to get back string, got garbage data.");
				}
			}else {
				System.out.println("Failed getting object with key \"" + stringKey + "\". Key doesn't exist. (INCORRECT RETURN)");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Failed getting object with key \"" + stringKey + "\" Exception occured.");
		}

		/*
		// Delay 5 seconds
		try {s
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/


		/* Test with a file */

		fileKey = "SceneBuilder";
		inputPath = "../inputfiles/SceneBuilder-15.0.0.msi";
		outputPath = "../outputfiles/SceneBuilder-15.0.0_.msi";

		// PUT File
		System.out.println("Trying initial PUT...");
		try {
			System.out.println("Putting file \"" + inputPath + "\" with key \"" + fileKey + "\"");
			ret = client.put(fileKey, inputPath);
			if(ret == 0) {
				System.out.println("Successfully put file!");
			}else {
				System.out.println("Failed to put file \"" + inputPath + "\" with key \"" + fileKey + "\". Key already exists. (INCORRECT RETURN)");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Failed to put file \"" + inputPath + "\" with key \"" + fileKey + "\". Exception occured.");
		} 
		
		// try to PUT duplicate file
		System.out.println("Attempt to put duplicate...");
		try {
			System.out.println("Putting file \"" + inputPath + "\" with key \"" + fileKey + "\"");
			ret = client.put(fileKey, inputPath);
			if(ret == 0) {
				System.out.println("Successfully put file!");
			}else {
				System.out.println("Failed to put file \"" + inputPath + "\" with key \"" + fileKey + "\". Key already exists. (INCORRECT RETURN)");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Failed to put file \"" + inputPath + "\" with key \"" + fileKey + "\". Exception occured.");
		} 

		System.out.println("First attempt to GET...");
		// GET File
		try {
			System.out.println("Getting object with key \"" + fileKey + "\"");
			ret = client.get(fileKey, outputPath);
			if(ret == 0) {
				fileIn = new File(inputPath);
				fileOut = new File(outputPath);
				if(fileOut.exists()) {
					fileInBytes = Files.readAllBytes(fileIn.toPath());
					fileOutBytes = Files.readAllBytes(fileOut.toPath());
					if(Arrays.equals(fileInBytes, fileOutBytes)) {
						System.out.println("File contents are equal! Successfully Retrieved File");
					}else {
						System.out.println("File contents are not equal! Got garbage data. (BAD FILE DOWNLOAD)");
					}
					System.out.println("Deleting downloaded file.");
					Files.delete(fileOut.toPath());
					String[] list = client.list();
					System.out.println("Listing keys before remove...");
					Arrays.stream(list).forEach(System.out::println);
					client.remove("SceneBuilder");
					list = client.list();
					System.out.println("Listing keys after remove...");
					Arrays.stream(list).forEach(System.out::println);
				}else {
					System.out.println("No file downloaded. (BAD FILE DOWNLOAD)");
				}
			}else {
				System.out.println("Failed getting object with key \"" + stringKey + "\". Key doesn't exist. (INCORRECT RETURN)");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Failed getting object with key \"" + stringKey + "\" Exception occured.");
		}
		
		// retry GET File after deletion
		System.out.println("File is deleted, retrying GET...");
		try {
			System.out.println("Getting object with key \"" + fileKey + "\"");
			ret = client.get(fileKey, outputPath);
			if(ret == 0) {
				fileIn = new File(inputPath);
				fileOut = new File(outputPath);
				if(fileOut.exists()) {
					fileInBytes = Files.readAllBytes(fileIn.toPath());
					fileOutBytes = Files.readAllBytes(fileOut.toPath());
					if(Arrays.equals(fileInBytes, fileOutBytes)) {
						System.out.println("File contents are equal! Successfully Retrieved File");
					}else {
						System.out.println("File contents are not equal! Got garbage data. (BAD FILE DOWNLOAD)");
					}
					System.out.println("Deleting downloaded file.");
					Files.delete(fileOut.toPath());
				}else {
					System.out.println("No file downloaded. (BAD FILE DOWNLOAD)");
				}
			}else {
				System.out.println("Failed getting object with key \"" + stringKey + "\". Key doesn't exist. (INCORRECT RETURN)");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Failed getting object with key \"" + stringKey + "\" Exception occured.");
		}

		// Disconnect
		System.out.println("Attempting to disconnect...");
		try {
			client.disconnect();
			System.out.println("Sucessfully disconnected.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to disconnect.");
		}

	}


}
