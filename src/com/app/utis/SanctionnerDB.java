package com.app.utis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.model.Sanction;
import com.app.model.Sanctionner;

public class SanctionnerDB {
	public static boolean add(Sanctionner sanctionner) throws SQLException {
		PreparedStatement stmt = ConnectionBD.connect().prepareStatement("insert into sanctioner (dateSanction,ouvrage,lecteur,sanction) values(?,?,,?,?)");
		stmt.setDate(1, new java.sql.Date(sanctionner.getDateSaction().getTime()));
		stmt.setString(2, sanctionner.getOuvrage().getId());
		stmt.setString(3, sanctionner.getLecteur().getId());
		stmt.setInt(4, sanctionner.getSanction().getId());
		return stmt.executeUpdate()>0;
	}
	
	public static List<Sanctionner> getData() throws SQLException{
		List<Sanctionner>sanctionner = new ArrayList<Sanctionner>();
		ResultSet res = ConnectionBD.connect()
				.prepareStatement("select id_snc,dateSanction,lecteur,nom_lec,prenom_lec,intitule_san ,titre from "
						+ "	sanctioner inner join lecteur on lecteur= id_lec"
						+ "  inner join sanction on sanction=id_san "
						+ "inner join ouvrage on ouvrage=id_ouv;")
				.executeQuery();
		while(res.next()) 
			sanctionner.add(new Sanctionner(res.getInt(1),res.getDate(2),new Ouvrage(null,res.getString(7)),
					new Lecteur(res.getString(3),res.getString(4),res.getString(5), null), 
					new Sanction(0, res.getString(6))));
		res.close();
		return sanctionner;
	}
	public static boolean delete(Sanctionner san) throws SQLException {
		Connection con = ConnectionBD.connect();
		PreparedStatement stmt = con.prepareStatement("delete from sanctioner where id_Snc =? ");
		stmt.setInt(1, san.getId());
		return stmt.executeUpdate()>0;
	}
}
