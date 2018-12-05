package com.amazonaws.lambda.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManage {
	// These are to be configured and NEVER stored in the code.
		// once you retrieve this code, you can update
		public final static String rdsMySqlDatabaseUrl = "calendardb.cdfwibjyhe4s.us-east-2.rds.amazonaws.com";
		public final static String dbUsername = "cs509";
		public final static String dbPassword = "cs5091234";
			
		public final static String jdbcTag = "jdbc:mysql://";
		public final static String rdsMySqlDatabasePort = "3306";
		public final static String multiQueries = "?allowMultiQueries=true";
		   
		public final static String dbName = "innodb";    // default created from MySQL WorkBench

		// pooled across all usages.
		static Connection conn;
	 
		/**
		 * Singleton access to DB connection to share resources effectively across multiple accesses.
		 */
		protected static Connection connect() throws Exception {
			if (conn != null) { return conn; }
			
			try {
//				System.out.println("start connecting......");
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + dbName + multiQueries,
						dbUsername,
						dbPassword);
				System.out.println("Database has been connected successfully.");
				return conn;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception("Failed in database connection "+ex.getMessage());
			}
		}
}
