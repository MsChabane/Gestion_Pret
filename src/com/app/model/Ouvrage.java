package com.app.model;

import java.util.Date;

public class Ouvrage {
	public Ouvrage(String id, String titre) {
		
		this.id = id;
		this.titre = titre;
	}
	private String id,titre;
	private Date dateEntree;
	private double prix;
	private int nombreExamplaire;
	private Categorie categorie;
	public Ouvrage(String id) {
		super();
		this.id = id;
	}
	public Ouvrage(String id, String titre, Date dateEntree, double prix, int nombreExamplaire, Categorie categorie) {
		this(id,titre,prix,nombreExamplaire,categorie);
		this.dateEntree = dateEntree;
		
	}
	
	@Override
	public String toString() {
		return " (" +  id + ") " + titre  ;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public Date getDateEntree() {
		return dateEntree;
	}
	public void setDateEntree(Date dateEntree) {
		this.dateEntree = dateEntree;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getNombreExamplaire() {
		return nombreExamplaire;
	}
	public void setNombreExamplaire(int nombreExamplaire) {
		this.nombreExamplaire = nombreExamplaire;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	public Ouvrage(String id, String titre, double prix, int nombreExamplaire, Categorie categorie) {
		super();
		this.id = id;
		this.titre = titre;
		this.prix = prix;
		this.nombreExamplaire = nombreExamplaire;
		this.categorie = categorie;
	}
	
}
