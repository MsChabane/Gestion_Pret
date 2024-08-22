package com.app.utis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.app.model.Categorie;
import com.app.model.Ouvrage;

public class OuvrageDB {
	public static String createId(Date date) throws SQLException {
		String id =new SimpleDateFormat("ddMMyyyy").format(date);
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("select max(substring(id_ouv,9,11)) from ouvrage where dateEmtre_ouv=?;");
		stmt.setDate(1,new java.sql.Date(date.getTime()));
		ResultSet res = stmt.executeQuery();
		if(res.next() && res.getString(1)!=null) {
			
			id+=String.format("%03d",Integer.parseInt(res.getString(1))+1);
		}else {
			id+="000";
		}
		connect.close();
		stmt.close();
		res.close();
		return id;
	}
	public static boolean add(Ouvrage ouvrage ) throws SQLException {
		ouvrage.setId(createId(ouvrage.getDateEntree()));
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("insert into ouvrage (id_ouv,titre,dateEmtre_ouv,nombre_Examplaire_ouv,prix,categorie)values(?,?,?,?,?,?);");
		stmt.setString(1,ouvrage.getId());
		stmt.setString(2,ouvrage.getTitre());
		stmt.setDate(3,new java.sql.Date(ouvrage.getDateEntree().getTime()));
		stmt.setInt(4,ouvrage.getNombreExamplaire());
		stmt.setDouble(5,ouvrage.getPrix());
		stmt.setInt(6,ouvrage.getCategorie().getId());
		return stmt.execute();
	}
	public static boolean modify(Ouvrage ouvrage) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update ouvrage set titre=?,nombre_Examplaire_ouv=?,prix=?,categorie=? where id_ouv=? ;");
		stmt.setString(1,ouvrage.getTitre());
		stmt.setInt(2,ouvrage.getNombreExamplaire());
		stmt.setDouble(3,ouvrage.getPrix());
		stmt.setInt(4,ouvrage.getCategorie().getId());
		stmt.setString(5,ouvrage.getId());
		return stmt.execute();
	}
	public static List<Ouvrage>getData(boolean all) throws SQLException{
		ArrayList<Ouvrage>ouvrages = new ArrayList<Ouvrage>();
		Connection connect = ConnectionBD.connect();
		String query="select id_ouv,titre,dateEmtre_ouv,nombre_Examplaire_ouv,prix,id_cat,intitule_cat from ouvrage inner join categorie on categorie=id_cat where supprimer_ouv=false ";
		if(!all) 
				query.concat(" and nombre_Examplaire_ouv >0 ; ");
		
		PreparedStatement stmt = connect.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while(res.next()){
			ouvrages.add(new Ouvrage(res.getString(1),res.getString(2),res.getDate(3), res.getDouble(5), res.getInt(4),new Categorie( res.getInt(6),res.getString(7))));
		}
		connect.close();
		res.close();
		stmt.close();
		return ouvrages;
	}
	public static List<Ouvrage>getDataSup() throws SQLException{
		ArrayList<Ouvrage>ouvrages = new ArrayList<Ouvrage>();
		Connection connect = ConnectionBD.connect();
		String query="select id_ouv,titre,dateEmtre_ouv,nombre_Examplaire_ouv,prix,id_cat,intitule_cat from ouvrage inner join categorie on categorie=id_cat where supprimer_ouv=true ";
		PreparedStatement stmt = connect.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while(res.next()){
			ouvrages.add(new Ouvrage(res.getString(1),res.getString(2),res.getDate(3), res.getDouble(5), res.getInt(4),new Categorie( res.getInt(6),res.getString(7))));
		}
		connect.close();
		res.close();
		stmt.close();
		return ouvrages;
	}
	
	public static boolean isExist(Ouvrage ouvrage) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("SELECT titre FROM ouvrage where titre= ?;");
		stmt.setString(1,ouvrage.getTitre());
		return stmt.executeQuery().next();
	}
	public static boolean delete(Ouvrage ouvrage) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update ouvrage set supprimer_ouv=true where id_ouv=? ;");
		stmt.setString(1,ouvrage.getId());
		return stmt.executeUpdate()>0;
	}
	public static boolean deleteAll(Ouvrage ouvrage) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("delete from ouvrage where supprimer_ouv=true  ;");
		return stmt.executeUpdate()>0;
	}
	public static boolean restor(Ouvrage ouvrage) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update ouvrage set supprimer_ouv=false where id_ouv=? ;");
		stmt.setString(1,ouvrage.getId());
		return stmt.executeUpdate()>0;
	}
	
	public static void doOpeartionOf(boolean pris,Ouvrage ouvrage) throws SQLException {
		String query;
		if(pris) 
			query="update ouvrage set nombre_Examplaire_ouv=nombre_Examplaire_ouv-1 where id_ouv=? ;";
		
		else query="update ouvrage set nombre_Examplaire_ouv=nombre_Examplaire_ouv+1 where id_ouv=? ;";
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement(query);
		stmt.setString(1,ouvrage.getId());
		stmt.execute();
	}
	public static int count( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(id_ouv) from ouvrage ; ").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}
	

	
}
