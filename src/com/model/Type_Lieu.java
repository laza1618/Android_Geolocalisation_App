package com.model;

import java.io.Serializable;


/**
 * Classe modèle équivalent à la table Type_Lieu de la base de données du service web
 * @author Herilaza
 *
 */
public class Type_Lieu implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * identifiant du type de lieu
	 */
	private Integer idType;
	/**
	 * nom du type de lieu
	 */
	private String nomType;
	/**
	 * etat de selection du type de lieu
	 * true si selectionnée et false sinon
	 */
	private Boolean selected;
	/**
	 * etat de selection du type de lieu
	 * 1 si selectionée
	 * 0 sinon
	 */
	private int bit;
		
	/**
	 * @return l'identfifiant du type de lieu
	 */
	public Integer getIdType() {
		return idType;
	}
	/**
	 * Modifie l'identifiant du type
	 * @param idType le nouveau identifiant du type
	 */
	public void setIdType(Integer idType) {
		this.idType = idType;
	}
	/**
	 * @return le nom du type
	 */
	public String getNomType() {
		return nomType;
	}
	/**
	 * Modifie le nom du type
	 * @param nomType le nouveau nom du type
	 */
	public void setNomType(String nomType) {
		this.nomType = nomType;
	}
	/**
	 * @return etat de la selection du lieu
	 */
	public Boolean getSelected() {
		return selected;
	}
	/**
	 * Modifie l'etat du type de lieu
	 * @param selected le nouveau etat du type de lieu
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
		if(this.selected == true) bit = 1;
		else bit = 0;
	}
	
	/**
	 * @return etat de la selection du lieu
	 */
	public int getBit() {
		return bit;
	}
	/**
	 * Modifie l'etat du type de lieu
	 * @param bit le nouveau etat du type de lieu
	 */
	public void setBit(int bit) {
		this.bit = bit;
		if(this.bit == 1) selected = true;
		else selected = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		Type_Lieu tLieu = null;		
		try {
			tLieu =  (Type_Lieu) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tLieu;
	}
	
}
