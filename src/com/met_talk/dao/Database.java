package com.met_talk.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

	static Connection con ;
	static PreparedStatement stmt ;
	static ResultSet rs ;
	
	private static Database instance;
	
	private Database() {}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	// -------------------------- CONNECTING TO THE "MYSQL" => "JAVA" DATABASE ------------------------------- //
	
	public void connect(){
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("database going to connected in db");
			Database.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/met_talk","root","Suren@19_2004");
			System.out.println("database connected in db");
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void close() {
		try {
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}


	
	// ------------------------- ACCESSING THE DATABASE TO INSERT Session Details ------------------------------ //

		public boolean insertSessionDetails(String sessionId, String portNo) {
			
			boolean isUploaded = false;
			
			try {
				if (con == null) {
					System.out.println(" con is null"); 
					return isUploaded;
	            }
				stmt = con.prepareStatement("insert into session_details values(?,?)");      
				stmt.setString(1,sessionId);
				stmt.setString(2, portNo);
				
				stmt.executeUpdate();
				isUploaded = true;
				System.out.println("uploaded at db");
			}
			catch(SQLException e){
				System.out.println("Error : " + e.getMessage());
			}
			finally {
				try {
					stmt.close();	
				}
				catch(Exception e){
					System.out.println("Error : " + e);
				}
			}
			
			return isUploaded;
		}
		
		// ------------------------- ACCESSING THE DATABASE TO delete Session Details ------------------------------ //

				public boolean deleteSessionDetails(String sessionId) {
					
					boolean isDeleted = false;
					
					try {
						if (con == null) {
							System.out.println(" con is null"); 
							return isDeleted;
			            }
						stmt = con.prepareStatement("delete from session_details where sessionId=?");      
						stmt.setString(1,sessionId);
						
						stmt.executeUpdate();
						isDeleted = true;
						System.out.println("deleted session : " + sessionId);
					}
					catch(SQLException e){
						System.out.println("Error : " + e.getMessage());
					}
					finally {
						try {
							stmt.close();	
						}
						catch(Exception e){
							System.out.println("Error : " + e);
						}
					}
					
					return isDeleted;
				}
				

		// --------------------------- ACCESSING DATABASE TO SELECT EMAIL  ------------------------------ //
	
	public List<String> selectAllEmail ( ) {
		
		List<String> emails =  new ArrayList<> ();
		
		try {
			stmt = con.prepareStatement("select email from user_details");
			rs = stmt.executeQuery();
			while(rs.next()) {
				emails.add(rs.getString("email"));
			}
		}
		catch(SQLException e) {
			System.out.println("Error : " + e.getMessage());
		}
		finally {
			try {
				stmt.close();
			}
			catch(Exception e) {
				System.out.println("Error: " + e);
			}
		}
		
		return emails;
	}
			
}
