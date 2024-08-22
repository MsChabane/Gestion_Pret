package com.app.model;

public class Sanction {
	private int id;
	private String intitule;
	private int nbr;
	public int getNbr() {
		return nbr;
	}
	public void setNbr(int nbr) {
		this.nbr = nbr;
	}
	public Sanction(int id, String intitule, int nbr) {
		super();
		this.id = id;
		this.intitule = intitule;
		this.nbr = nbr;
	}
	public Sanction(int id, String intitule) {
		super();
		this.id = id;
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
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
	@Override
	public String toString() {
		return  intitule ;
	}
	
}
