package com.app.model;


import java.util.Date;


public class Emprunt {
	private String id; 
	private Date datePret;
	private Date dateRmis;
	private Ouvrage ouvrage;
	private Lecteur lecteur ;
	private String etat;
	private int joursRest;
	
	
	public int getJoursRest() {
		return joursRest;
	}
	public void setJoursRest(int joursRest) {
		this.joursRest = joursRest;
	}
	public Emprunt(String id, Date datePret, Date dateRmis, Ouvrage ouvrage, Lecteur lecteur, int joursRest) {
		super();
		this.id = id;
		this.datePret = datePret;
		this.dateRmis = dateRmis;
		this.ouvrage = ouvrage;
		this.lecteur = lecteur;
		this.joursRest = joursRest;
	}
	public Emprunt(String id, Date datePret, Date dateRmis, Ouvrage ouvrage, Lecteur lecteur,String etat) {
		this(id,datePret,dateRmis,ouvrage,lecteur);
		this.etat = etat;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDatePret() {
		return datePret;
	}
	public void setDatePret(Date datePret) {
		this.datePret = datePret;
	}
	public Date getDateRmis() {
		return dateRmis;
	}
	public void setDateRmis(Date dateRmis) {
		this.dateRmis = dateRmis;
	}
	public Ouvrage getOuvrage() {
		return ouvrage;
	}
	public void setOuvrage(Ouvrage ouvrage) {
		this.ouvrage = ouvrage;
	}
	public Lecteur getLecteur() {
		return lecteur;
	}
	public void setLecteur(Lecteur lecteur) {
		this.lecteur = lecteur;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public Emprunt(String id, Date datePret, Date dateRmis, Ouvrage ouvrage, Lecteur lecteur) {
		this.id = id;
		this.datePret = datePret;
		this.dateRmis = dateRmis;
		this.ouvrage = ouvrage;
		this.lecteur = lecteur;
	}
}
