package com.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonParseException;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.util.ServiceRequest;

/**
 * Cette classe est utilisé pour manipuler la base de données SQLite de l'application qui contient des objets de type Type_Lieu
 * @author Herilaza
 *
 */
public class MyTypeLieuBaseAdapter {
	/**
	 * le nom de la table dans la base de données
	 */
	private static final String TABLE_TYPE_LIEU = "table_type_lieu";
	/**
	 * la version de la base de données
	 */
	private static final int BASE_VERSION = 1;
	/**
	 * identifiant de la colonne ID_TYPE dans la base de données
	 */
	private static final int COLONNE_ID_TYPE_ID = 0;
	/**
	 * nom de la colonne ID_TYPE dans la base de données
	 */
	private static final String COLONNE_ID_TYPE = "id_type";
	/**
	 * nom de la colonne NOM_TYPE dans la base de données
	 */
	private static final String COLONNE_NOM_TYPE = "nom_type";
	/**
	 * idientifiant de la colonne NOM_TYPE dans la base de données
	 */
	private static final int COLONNE_NOM_TYPE_ID = 1;
	/**
	 * nom de la colonne SELECTED_TYPE dans la base de données
	 */
	private static final String COLONNE_SELECTED_TYPE = "selected_type";
	/**
	 * identifiant de  la colonne SELECTED_TYPE dans la base de données
	 */
	private static final int COLONNE_SELECTED_TYPE_ID = 2;
	/**
	 * URL de la ressource permettant d'avoir la liste des type de lieu depuis la base de données du service web pou initialiser la base de données SQLite de l'application 
	 */
	private static final String RESSOURCE = "http://10.0.2.2:8080/service-rest/ressources/typelieu";
	/**
	 * la liste qui contient les type de lieu disponible depuis le service web
	 */
	private ArrayList<Type_Lieu> listType = null;
	
	/**
	 * Instance de la classe SQLiteDataBase permettant d'effectuer les opérations d'ouverture,fermeture,requetes sur la base
	 */
	private static SQLiteDatabase myBase;
	/**
	 * Instance de la classe MyTypeLieuBaseHelper qui contient la requete de création de la base de données
	 */
	private MyTypeLieuBaseHelper myTypeLieuHelper;
	/**
	 * Instance de la classe ServiceRequest permettant d'effectuer des requetes HTTP vers le Service Web
	 */
	private ServiceRequest servReq;
	
	/**
	 * Constructeur
	 * @param ctx contexte de l'application
	 */
	public MyTypeLieuBaseAdapter(Context ctx) {
		myTypeLieuHelper = new MyTypeLieuBaseHelper(ctx, TABLE_TYPE_LIEU,null,BASE_VERSION);
		if (this.getAllTypeLieu().size() == 0) {
			servReq = new ServiceRequest(RESSOURCE);
			try {
				listType = servReq.parseresponseToTypeLieu(servReq.sendGetRequest());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			initData(listType);
		}
	}
	
	/**
	 * Ouvre la base de données en mode écriture
	 */
	public synchronized void openWrite() {
		myBase = myTypeLieuHelper.getWritableDatabase();
		Log.i("Database","Open Write ok");
	}
	
	/**
	 * Ouvre la base de données en mode lecture
	 */
	public synchronized void openRead() {
		myBase = myTypeLieuHelper.getReadableDatabase();
		Log.i("Database","Open Read ok");
	}
	
	/**
	 * Ferme la base de données
	 */
	public synchronized void close() {
		if (myBase != null) {
			myBase.close();
			myBase = null;
			Log.i("Database","Close");
		}
	}
	
	/**
	 * @return la variable myBase
	 */
	public SQLiteDatabase getMyBase() {
		return myBase;
	}
	
	/**
	 * permet de convertir un objet de type curseur en un objet de type Type_Lieu
	 * @param c curseur, contenant les résulats des requetes de la base de données
	 * @return
	 */
	private Type_Lieu cursorToTypeLieu (Cursor c) {
		if (c.getCount() == 0 ) {
			//c.close();
			return null;
		}
		else {
			Type_Lieu tLieu = new Type_Lieu();
			tLieu.setIdType(c.getInt(COLONNE_ID_TYPE_ID));
			tLieu.setNomType(c.getString(COLONNE_NOM_TYPE_ID));
			tLieu.setBit(c.getInt(COLONNE_SELECTED_TYPE_ID));
			//c.close();
			return tLieu;
		}
	}
	
	/**
	 * permet de convertir un objet de type curseur en une liste d'objet de type Type_Lieu
	 * @param c curseur contenant les résulats des requetes de la base de données
	 * @return
	 */
	private ArrayList<Type_Lieu> cursorToTypeLieus (Cursor c) {
		ArrayList<Type_Lieu> list;
		if (c.getCount() == 0 ) {
			//c.close();
			return list = new ArrayList<Type_Lieu>(0);
		}
		else {
			//openRead();
			list = new ArrayList<Type_Lieu>();
			c.moveToFirst();
			do {
				list.add(cursorToTypeLieu(c));
			}
			while (c.moveToNext());
			//c.close();
			//close();
			return list;
		}
	}
	
	/**
	 * @return la liste de tous les type de lieu existant dans la base de données
	 */
	public ArrayList<Type_Lieu> getAllTypeLieu() {
		openRead();
		Cursor c = myBase.query(TABLE_TYPE_LIEU, null, null, null, null,null, COLONNE_NOM_TYPE);
		ArrayList<Type_Lieu> list = cursorToTypeLieus(c);
		c.close();
		close();
		return list;
	}
	
	/**
	 * cette méthode est utilisée pour inserer une liste de type lieu dans la base de données
	 * @param list la liste des type de lieu à insérer
	 */
	public void insertTypeLieus(ArrayList<Type_Lieu> list){
		Iterator<Type_Lieu> iter = list.iterator();
		while (iter.hasNext()) {
			Type_Lieu tLieu = iter.next();
			insertTypeLieu(tLieu);
		}
	}
	
	/**
	 * permet d'initialiser les données dans la base de données
	 * @param list la liste des type de lieu à insérer
	 */
	public void initData(ArrayList<Type_Lieu> list){
		Iterator<Type_Lieu> iter = list.iterator();
		while (iter.hasNext()) {
			Type_Lieu tLieu = iter.next();
			tLieu.setSelected(true);
			insertTypeLieu(tLieu);
		}
	}
	/**
	 * permet d'insérer un type de lieu dans la base de données
	 * @param tLieu l'objet de type lieu à insérer
	 */
	public void insertTypeLieu(Type_Lieu tLieu) {
		openWrite();
		ContentValues value = new ContentValues();
		value.put(COLONNE_ID_TYPE,tLieu.getIdType());
		value.put(COLONNE_NOM_TYPE,tLieu.getNomType());
		value.put(COLONNE_SELECTED_TYPE,tLieu.getBit());
		myBase.insert(TABLE_TYPE_LIEU, null, value);
		close();
	}
	/**
	 * cette methode met à jour des types de lieu dans la base de données à partir d'une liste fournit en paramètre
	 * @param list la liste des types de lieu à mettre à jour
	 */
	public void updatedb(ArrayList<Type_Lieu> list) {
		for (Type_Lieu tLieu : list) {
			updateTypeLieu(tLieu);
		}
		
	}
	/**
	 * met à jour un type de lieu dans la base de données
	 * @param tLieu le type de lieu à modifier
	 */
	public void updateTypeLieu(Type_Lieu tLieu) {
		openWrite();
		ContentValues value = new ContentValues();
		value.put(COLONNE_SELECTED_TYPE,tLieu.getBit());
		myBase.update(TABLE_TYPE_LIEU, value, COLONNE_ID_TYPE + " = " + tLieu.getIdType(), null);
		close();
	}
	/**
	 * @return la liste des identifiants de type de lieu à ne pas afficher sur la carte
	 */
	public ArrayList<Integer> getListTypeToNotDisplay() {
		ArrayList<Integer> list;
		openRead();
		Cursor c = myBase.query(TABLE_TYPE_LIEU, new String[]{COLONNE_ID_TYPE}, COLONNE_SELECTED_TYPE + " = 0", null, null,null,null);
		if (c.getCount() == 0 ) {
			c.close();
			return new ArrayList<Integer>(0);
		}
		else {
			list = new ArrayList<Integer>();
			c.moveToFirst();
			do {
				list.add(c.getInt(COLONNE_ID_TYPE_ID));
			}
			while(c.moveToNext());
			c.close();
		}
		close();
		return list;
	}
}
