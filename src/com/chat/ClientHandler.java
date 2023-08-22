package com.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private String clientUserDetails;
	
	public ClientHandler(Socket socket) {
		try {
			this.clientSocket = socket;
			this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.out = new PrintWriter(clientSocket.getOutputStream(),true);
			this.clientUserDetails = in.readLine();
			
			addClient();
			
		}
		catch(IOException e) {
			closeAll(clientSocket,in,out);
		}
	}
	
	@Override
	public void run() {
		
		String msgFromClient;
		
		while(clientSocket.isConnected()) {
			try {
				msgFromClient = in.readLine();
				if(!msgFromClient.equals("null")){
					broadCastMsg(msgFromClient);
				}
			}
			catch(IOException e) {
				closeAll(clientSocket,in,out);
				break;
			}
		}
		
	}
	
	public void addClient() {
		clients.add(this);
		broadCastMsg("Joined the chat !");
	}
	
	public void removeClient() {
		
		clients.remove(this);
		broadCastMsg("Left the chat !!! ");
	}
	
	public void broadCastMsg(String msg) {
		if(!msg.equals("null")) {
			try {
				for(ClientHandler aClient : clients) {	
					
					String userDetails = msg.substring(0, msg.lastIndexOf(":"));
					if(!aClient.clientUserDetails.equals(userDetails)){
						aClient.out.println(msg);
					}
				}
			}
			catch(Exception e) {
				e.getStackTrace();
			}
		}
		
	}
	
	public void closeAll(Socket socket, BufferedReader input , PrintWriter output ) {
		removeClient();
		
		try {
			if(input != null) input.close();
			if(output != null) output.close();
			if(socket != null) socket.close();
		}
		catch(IOException e) {
			e.getStackTrace();
		}
	}
}
