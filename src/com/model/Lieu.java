package com.model;

import java.io.Serializable;

/**
 * Classe modèle équivalent à la table Lieu de la base de données du service web
 * @author Herilaza
 *
 */
public class Lieu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  Identifiant du Lieu
	 */
	private Integer idLieu;
	/**
	 *  Nom du Lieu
	 */
	private String nomLieu;
	/**
	 *  Latitude du Lieu
	 */
	private double latLieu;
	/**
	 * 	Longitude du Lieu
	 */
	private double longLieu;
	/**
	 *  Description de Lieu
	 */
	private String descLieu;
	/**
	 * Nom de l'image associé à ce Lieu
	 */
	private String imgLieu;
	/**
	 * Type de ce Lieu
	 */
	private Type_Lieu typeLieu;
	/**
	 * @return l'identifiant du lieu
	 */
	public Integer getIdLieu() {
		return idLieu;
	}
	/**
	 * modifie l'identifiant du lieu
	 * @param idLieu le nouvel identifant du lieu
	 */
	public void setIdLieu(Integer idLieu) {
		this.idLieu = idLieu;
	}
	/**
	 * @return le nom du lieu
	 */
	public String getNomLieu() {
		return nomLieu;
	}
	/**
	 * Modifie le nom du lieu
	 * @param nomLieu le nouveau nom du lieu
	 */
	public void setNomLieu(String nomLieu) {
		this.nomLieu = nomLieu;
	}
	/**
	 * @return la latitude du lieu
	 */
	public double getLatLieu() {
		return latLieu;
	}
	/**
	 * modifie la laitude du lieu
	 * @param latLieu la nouvelle latitude du lieu
	 */
	public void setLatLieu(double latLieu) {
		this.latLieu = latLieu;
	}
	/**
	 * @return la longitude du lieu
	 */
	public double getLongLieu() {
		return longLieu;
	}
	/**
	 * modifie la longitude du lieu
	 * @param longLieu la nouvelle longitude du lieu
	 */
	public void setLongLieu(double longLieu) {
		this.longLieu = longLieu;
	}
	/**
	 * @return la description du lieu
	 */
	public String getDescLieu() {
		return descLieu;
	}
	/**
	 * modifie la description du lieu
	 * @param descLieu la nouvelle description du lieu
	 */
	public void setDescLieu(String descLieu) {
		this.descLieu = descLieu;
	}
	/**
	 * @return le nom de l'image du lieu
	 */
	public String getImgLieu() {
		return imgLieu;
	}
	/**
	 * modifie le nom de l'image du lieu
	 * @param imgLieu le nouveau nom de l'image
	 */
	public void setImgLieu(String imgLieu) {
		this.imgLieu = imgLieu;
	}
	/**
	 * @return le type du lieu
	 */
	public Type_Lieu getTypeLieu() {
		return typeLieu;
	}
	/**
	 * modifie le type du lieu
	 * @param typeLieu  le nouveau type du lieu
	 */
	public void setTypeLieu(Type_Lieu typeLieu) {
		this.typeLieu = typeLieu;
	}
}
