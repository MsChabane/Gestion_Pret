package com.app.model;

import java.util.Date;

public class Sanctionner {
	private int id;
	private Date dateSaction;
	private Lecteur lecteur;
	private Sanction sanction;
	private Ouvrage ouvrage; 
	public Sanctionner(int id, Date dateSaction,Ouvrage ouvrage, Lecteur lecteur, Sanction sanction) {
		this.id = id;
		this.dateSaction = dateSaction;
		this.lecteur = lecteur;
		this.sanction = sanction;
		this.ouvrage = ouvrage;
	}
	public Ouvrage getOuvrage() {
		return ouvrage;
	}
	public void setOuvrage(Ouvrage ouvrage) {
		this.ouvrage = ouvrage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateSaction() {
		return dateSaction;
	}
	public void setDateSaction(Date dateSaction) {
		this.dateSaction = dateSaction;
	}
	public Lecteur getLecteur() {
		return lecteur;
	}
	public void setLecteur(Lecteur lecteur) {
		this.lecteur = lecteur;
	}
	public Sanction getSanction() {
		return sanction;
	}
	public void setSanction(Sanction sanction) {
		this.sanction = sanction;
	}
	
	
	
}
