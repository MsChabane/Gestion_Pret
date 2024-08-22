package com.app.utis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.app.model.Sanction;

public class SanctionDB {
	public static boolean add(Sanction san) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="INSERT INTO sanction (intitule_san) values (?)";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, san.getIntitule());
		if( prpStmt.executeUpdate()>0) {
			san.setId(getId(san.getIntitule()));
			return true;
		}
		return false;
	}
	private static int getId(String cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="select id_san from sanction where intitule_san = ?";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, cat);
		ResultSet set= prpStmt.executeQuery();
		return set.next()?set.getInt(1):0;
	}
	public static boolean modify(Sanction san) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="UPDATE  sanction set intitule_san=? where id_san=?";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, san.getIntitule());
		prpStmt.setInt(2, san.getId());
		return prpStmt.executeUpdate()>0;
	}
	public static boolean alreadyExist(String san) throws SQLException {
		Connection con = ConnectionBD.connect();
		PreparedStatement stmt = con.prepareStatement("select intitule_san from sanction where intitule_san= ?  ");
		stmt.setString(1, san);
		return stmt.executeQuery().next();	
	}
	public static List<Sanction>getData() throws SQLException{
		ArrayList<Sanction> sanctiones = new ArrayList<>();
		Connection con = ConnectionBD.connect();
		String query="SELEct id_san,intitule_san,count(id_Snc) FROM sanction left join sanctioner on id_san=sanction "
				+ "	GROUP BY id_san;";
		PreparedStatement prpStmt = con.prepareStatement(query);
		ResultSet res=  prpStmt.executeQuery();
		while(res.next())
			sanctiones.add(new Sanction(res.getInt(1), res.getString(2),res.getInt(3)));
		con.close();
		res.close();
		prpStmt.close();
		return sanctiones;
	}
	public static boolean delete(Sanction san) throws SQLException {
		Connection con = ConnectionBD.connect();
		PreparedStatement stmt = con.prepareStatement("delete from sanction where id_san =? ");
		stmt.setInt(1, san.getId());
		return stmt.executeUpdate()>0;
	}

}
