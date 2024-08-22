package com.app.model;

public class Categorie {
	@Override
	public String toString() {
		return intitule;
	}
	private int id;
	private String intitule;
	private int nbr;
	public Categorie(int id, String intitule, int nbr) {
		
		this.id = id;
		this.intitule = intitule;
		this.nbr = nbr;
	}
	public int getNbr() {
		return nbr;
	}
	public void setNbr(int nbr) {
		this.nbr = nbr;
	}
	public Categorie(int id, String intitule) {
		
		this.id = id;
		this.intitule = intitule;
	}
	public Categorie( String intitule) {
		this.intitule = intitule;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIntitule() {
		return intitule;
	}
	public void setintitule(String intitule) {
		this.intitule = intitule;
	}

}
