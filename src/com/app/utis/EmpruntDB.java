package com.app.utis;


import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;

public class EmpruntDB {
	
	private static void createId(Emprunt emprunt) throws SQLException {
		String id =new SimpleDateFormat("ddMMyyyy").format(emprunt.getDatePret()).concat(emprunt.getOuvrage().getId());		 
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("select max(substring(id_emp,19,21)) from emprunt where datePret = ?  ");
		stmt.setDate(1, new java.sql.Date(emprunt.getDatePret().getTime()));
		ResultSet res =stmt .executeQuery();
		if( res.next() &&res.getString(1)!=null) 
			id+=String.format("%02d",Integer.parseInt(res.getString(1))+1);
		else 
			id+="00";
		stmt.close();
		res.close();
		 emprunt.setId(id);
	}
	public static boolean add(Emprunt emprunt) throws SQLException {
		createId(emprunt);
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("insert into emprunt (id_emp,datePret,dateRemis,ouvrage,lecteur)values (?,?,date_add(datePret,INTERVAL 15 DAY),?,?);");
		stmt.setString(1,emprunt.getId() );
		stmt .setDate(2,new java.sql.Date(emprunt.getDatePret().getTime()));
		stmt.setString(3,emprunt.getOuvrage().getId());
		stmt.setString(4,emprunt.getLecteur().getId());
		if( stmt.executeUpdate()>0) {
			OuvrageDB.doOpeartionOf(true, emprunt.getOuvrage());
			emprunt.setDateRmis(getDate(emprunt.getId()));
			return true;
		}
		return false;
	}
	private static Date getDate(String id) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("select dateRemis from emprunt where id_emp = ? ");
		stmt.setString(1,id);
		ResultSet res =stmt.executeQuery();
		return res.next()?res.getDate(1):null;
	}
	public static boolean modify(Emprunt emprunt) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("update emprunt set dateRemis= ? where id_emp=? ");
		stmt .setDate(1,new java.sql.Date(emprunt.getDateRmis().getTime()));
		stmt.setString(2,emprunt.getId() );
		return stmt.executeUpdate()>0;
	}
	public static boolean delete(Emprunt emprunt) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("delete from emprunt where id_emp=? ");
		stmt.setString(1,emprunt.getId() );
		return stmt.executeUpdate()>0;
	}
	
	public static String getMaxDatePretFor(Lecteur lecteur) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("select max(datePret) from emprunt where lecteur=? ");
		stmt.setString(1,lecteur.getId());
		ResultSet res= stmt.executeQuery();
		return res.next()?res.getDate(1).toString():null;
	}
	public static boolean canDo(Lecteur lecteur) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("select count(*) from emprunt where lecteur=? and etat='EN ATTENTE';");
		stmt.setString(1,lecteur.getId());
		ResultSet res= stmt.executeQuery();
		return res.next() && res.getInt(1)<3;
	}
	public static boolean remis(Emprunt emprunt) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("update emprunt set dateRemis= ?,etat='REMIS' where id_emp=? ");
		stmt .setDate(1,new java.sql.Date(emprunt.getDateRmis().getTime()));
		stmt.setString(2,emprunt.getId() );
		if ( stmt.executeUpdate()>0) {
			OuvrageDB.doOpeartionOf(false, emprunt.getOuvrage());
			return true;
		};		
		return false;
	}
	public static List<Emprunt> getData() throws SQLException{
		List<Emprunt>emprunts = new ArrayList<Emprunt>();
		ResultSet res= ConnectionBD.connect().prepareStatement("select id_emp,datePret,dateRemis,ouvrage,titre,lecteur,nom_lec,prenom_lec,etat from emprunt inner join lecteur on id_lec = lecteur inner join ouvrage on ouvrage=id_ouv;").executeQuery();
		while(res.next())
			emprunts.add(new Emprunt(res.getString(1), res.getDate(2), res.getDate(3),new Ouvrage(res.getString(4),res.getString(5)), new Lecteur(res.getString(6)
					,res.getString(7),res.getString(8)),res.getString(9)));
		res.close();
		return emprunts;
	}
	public static boolean test(Date datePret,Date dateRemis) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("select datediff(?,?); ");
		stmt .setDate(1,new java.sql.Date(dateRemis.getTime()));
		stmt .setDate(2,new java.sql.Date(datePret.getTime()));
		ResultSet res = stmt.executeQuery();
		return res.getInt(1)>15;	
	}
	public static List<Emprunt> getDataNnReturn() throws SQLException {
		List<Emprunt>emprunts = new ArrayList<Emprunt>();
		ResultSet res= ConnectionBD.connect().prepareStatement("select id_emp,datePret,dateRemis,ouvrage,titre,lecteur,nom_lec,prenom_lec,datediff(dateRemis,curdate()) from emprunt inner join lecteur on id_lec = lecteur inner join ouvrage on ouvrage=id_ouv where not etat ='REMIS';").executeQuery();
		while(res.next())
			emprunts.add(new Emprunt(res.getString(1), res.getDate(2), res.getDate(3),new Ouvrage(res.getString(4),res.getString(5)), new Lecteur(res.getString(6)
					,res.getString(7),res.getString(8)),res.getInt(9)));
		res.close();
		return emprunts;
	}
	public static int count( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(id_emp) from emprunt ; ").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}
	public static int countEnAtt( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(id_emp) from emprunt where etat ='EN ATTENTE' ; ").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}
	public static int countRemis( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(id_emp) from emprunt where etat ='REMIS' ; ").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}
	public static int countDepDelai( ) throws SQLException {
		Connection connect = ConnectionBD.connect();
		ResultSet stmt = connect.prepareStatement("select count(*) from emprunt where datediff(dateRemis,curdate())<0 and etat='EN ATTENTE';").executeQuery();
		return stmt.next()?stmt.getInt(1):-1;
	}
	
}
