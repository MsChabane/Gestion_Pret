package com.app.utis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.model.Lecteur;


public class LecteurDB {
	public static String createId(Date date) throws SQLException {
		String id =new SimpleDateFormat("ddMMyyyy").format(date);
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("select max(substring(id_lec,9,11)) from lecteur where dateInscrire=?;");
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
	public static boolean add(Lecteur lecteur ) throws SQLException {
		lecteur.setId(createId(lecteur.getDateInscrire()));
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("insert into lecteur (id_lec,nom_lec,prenom_lec,date_naissance_lec,dateInscrire)values(?,?,?,?,?);");
		stmt.setString(1,lecteur.getId());
		stmt.setString(2,lecteur.getNom());
		stmt.setString(3,lecteur.getPrenom());
		stmt.setDate(4,new java.sql.Date(lecteur.getDateNaissance().getTime()));
		stmt.setDate(5,new java.sql.Date(lecteur.getDateInscrire().getTime()));
		return stmt.executeUpdate()>0;
	}
	public static boolean modify(Lecteur lecteur) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update lecteur set nom_lec=?,prenom_lec=?,date_naissance_lec=? where id_lec=? ;");
		stmt.setString(1,lecteur.getNom());
		stmt.setString(2,lecteur.getPrenom());
		stmt.setDate(3,new java.sql.Date(lecteur.getDateNaissance().getTime()));
		stmt.setString(4,lecteur.getId());
		return stmt.executeUpdate()>0;
	}
	public static List<Lecteur>getData(boolean supp) throws SQLException{
		ArrayList<Lecteur>lecteurs = new ArrayList<>();
		String query;
		Connection connect = ConnectionBD.connect();
		if(!supp) 
			query="SELEct id_lec,nom_lec,prenom_lec,date_naissance_lec,dateInscrire,count(id_Snc) FROM lecteur left join sanctioner on id_lec=lecteur "
					+ "where supprimer_lec=false  GROUP BY id_lec";
		else 
			query="SELEct id_lec,nom_lec,prenom_lec,date_naissance_lec,dateInscrire,count(id_Snc) FROM lecteur left join sanctioner on id_lec=lecteur "
					+ "where supprimer_lec=true GROUP BY id_lec";
			
		PreparedStatement stmt = connect.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while(res.next()){
			lecteurs.add(new Lecteur(res.getString(1), res.getString(2),res.getString(3), res.getDate(4),res.getDate(5), res.getInt(6)));
		}
		connect.close();
		res.close();
		stmt.close();
		return lecteurs;
	}
	public static boolean delete(Lecteur lecteur) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update lecteur set supprimer_lec=true where id_lec= ? ;");
		stmt.setString(1,lecteur.getId());
		return stmt.executeUpdate()>0;
	}
	
	public static boolean restor(Lecteur lecteur) throws SQLException {
		Connection connect = ConnectionBD.connect();
		PreparedStatement stmt = connect.prepareStatement("update lecteur set supprimer_lec=false where id_lec=? ;");
		stmt.setString(1,lecteur.getId());
		return stmt.executeUpdate()>0;
	}
	public static int count( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(id_lec) from lecteur ; ").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}

	

}
