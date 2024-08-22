package com.app.model;

import java.util.Date;

public class Lecteur {
	private String id,nom,prenom;
	
	private Date dateNaissance,dateInscrire;
	private int nombreSection;
	
	public Lecteur(String id, String nom, String prenom, Date dateNaissance, Date dateInscrire) {
		this(id,nom,prenom,dateNaissance);
		this.dateInscrire = dateInscrire;
	}
	public Lecteur(String id, String nom, String prenom, Date dateNaissance, Date dateInscrire, int nombreSection) {
		this(id,nom,prenom,dateNaissance,dateInscrire);
		this.nombreSection = nombreSection;
	}
	public void setNombreSection(int nombreSection) {
		this.nombreSection = nombreSection;
	}
	public int getNombreSection() {
		return nombreSection;
	}
	public Date getDateInscrire() {
		return dateInscrire;
	}
	
	@Override
	public String toString() {
		return  "( "+id + ") " + nom + "  " + prenom ;
	}
	public Lecteur(String id, String nom, String prenom) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}
	public void setDateInscrire(Date dateInscrire) {
		this.dateInscrire = dateInscrire;
	}
	public Lecteur(String id, String nom, String prenom, Date dateNaissance) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
	}
	public Lecteur(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Date getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
}
