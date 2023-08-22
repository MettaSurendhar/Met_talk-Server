package com.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.met_talk.dao.Database;
import com.met_talk.tools.Email;
import com.met_talk.tools.RandomString;


public class Server {

	private static ServerSocket serverSocket;
	private static Database database = Database.getInstance();
	private static Scanner scan = new Scanner(System.in);
	private static String timePeriod;
	private static String command;
	
	public Server(ServerSocket serverSocket) {
		Server.serverSocket = serverSocket;
		System.out.println("Server is Connected !");
	}
	

	public static void main(String[] args) {
		
		database.connect();
		RandomString randomString = RandomString.getInstance();
		String stPortNo = null;
		
		try {
			
			System.out.println("Enter the port no to start the session:");
			stPortNo = scan.nextLine();
			

			System.out.println("Enter the time period for join (in minutes) : ");
			timePeriod = scan.nextLine();

			String strSessionId =  randomString.getRandomString(8);
			System.out.println("Session Id is : "+ strSessionId);

			Email email = new Email(strSessionId);
			email.start();
		
			boolean isInserted = database.insertSessionDetails(strSessionId, stPortNo);
			int portNo = Integer.parseInt(stPortNo);

			if(isInserted) {
				Server server = null;
				try {
					server = new Server(new ServerSocket(portNo));
					server.startServer(strSessionId);
				} catch (IOException e) {
					closeServerSocket();
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			scan.close();
		}
		
		
	System.exit(0);	
	}
	
	public void startServer(String strSessionId) {
		try{

				command = "#run";
			
				while(command.equals("#run") && !command.equals("#end")){

					long startTime = System.currentTimeMillis();
					long endTime = startTime + Integer.parseInt(timePeriod) * 60 * 1000;
					
						while(!serverSocket.isClosed() && (System.currentTimeMillis() < endTime)) {
							System.out.println("Waiting for a client..... ");
							Socket socket = serverSocket.accept();
							System.out.println("a new client is connected");
							ClientHandler clientHandler = new ClientHandler(socket);
							
							Thread thread = new Thread(clientHandler);
							thread.start();
						}

						do{
							System.out.println("Enter a command : ");
						command = scan.nextLine();
						}while(!command.equals("#end") && !command.equals("#run"));
					if(command.equals("#end")) {
						System.out.println("closing");
					closeServerSocket();
				}
				
			}
		}
		catch(IOException e) {
			closeServerSocket();
		}
		
		System.exit(0);
		
	}
	
	
	public static void closeServerSocket() {
		try {
			if(serverSocket != null) serverSocket.close();
			database.close();
		}
		catch(IOException e) {
			e.getStackTrace();
		}
	}
	
}
