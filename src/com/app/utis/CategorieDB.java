package com.app.utis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.model.Categorie;

public class CategorieDB {
	public static boolean add(Categorie cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="INSERT INTO categorie (intitule_cat) values (?)";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, cat.getIntitule());
		if( prpStmt.executeUpdate()>0) {
			cat.setId(getId(cat.getIntitule()));
			return true;
		}
		return false;
	}
	private static int getId(String cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="select id_cat from categorie where intitule_cat = ?";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, cat);
		ResultSet set= prpStmt.executeQuery();
		return set.next()?set.getInt(1):0;
	}
	public static boolean modify(Categorie cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		String query="UPDATE  categorie set intitule_cat=? where id_cat=?";
		PreparedStatement prpStmt = con.prepareStatement(query);
		prpStmt.setString(1, cat.getIntitule());
		prpStmt.setInt(2, cat.getId());
		return prpStmt.executeUpdate()>0;
	}
	public static boolean alreadyExist(String cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		PreparedStatement stmt = con.prepareStatement("select intitule_cat from categorie where intitule_cat= ?  ");
		stmt.setString(1, cat);
		return stmt.executeQuery().next();	
	}
	public static boolean delete(Categorie cat) throws SQLException {
		Connection con = ConnectionBD.connect();
		PreparedStatement stmt = con.prepareStatement("delete from categorie where id_cat =? ");
		stmt.setInt(1, cat.getId());
		return stmt.executeUpdate()>0;	
	}
	public static List<Categorie>getData() throws SQLException{
		ArrayList<Categorie> categories = new ArrayList<>();
		Connection con = ConnectionBD.connect();
		String query="SELEct id_cat,intitule_cat,count(id_ouv) FROM categorie left join ouvrage on id_cat=categorie  "
				+ "	GROUP BY id_cat;";
		PreparedStatement prpStmt = con.prepareStatement(query);
		ResultSet res=  prpStmt.executeQuery();
		while(res.next())
			categories.add(new Categorie(res.getInt(1), res.getString(2),res.getInt(3)));
		con.close();
		res.close();
		prpStmt.close();
		return categories;
	}
	public static Map<String,Integer>getbarData() throws SQLException{
		Map<String,Integer> barData = new HashMap<>();
		Connection con = ConnectionBD.connect();
		String query="SELEct intitule_cat,count(id_ouv) FROM categorie left join ouvrage on id_cat=categorie  "
				+ "	GROUP BY id_cat;";
		PreparedStatement prpStmt = con.prepareStatement(query);
		ResultSet res=  prpStmt.executeQuery();
		while(res.next())
			barData.put(res.getString(1),res.getInt(2));
		con.close();
		res.close();
		prpStmt.close();
		return barData;
	}

}
