package com.app.utis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
	public static Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/PretBibilio","root","chabane");
	}

}
