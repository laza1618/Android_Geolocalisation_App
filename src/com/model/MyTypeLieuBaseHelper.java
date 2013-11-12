package com.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Classe utilisé pour la création de la base de données
 * @author Herilaza
 *
 */
public class MyTypeLieuBaseHelper extends SQLiteOpenHelper {
	
	/**
	 * le nom de la table 
	 */
	private static final String TABLE_TYPE_LIEU = "table_type_lieu";
	
	/**
	 * la colonne ID_TYPE
	 */
	private static final String COLONNE_ID_TYPE = "id_type";
	/**
	 * la colonne NOM_TYPE
	 */
	private static final String COLONNE_NOM_TYPE = "nom_type";
	/**
	 * la colonne SELECTED_TYPE
	 */
	private static final String COLONNE_SELECTED_TYPE = "selected_type";
	
	/**
	 * la requete de création de la base de données
	 */
	private static final String REQUETE_CREATION_DB = "create table " + TABLE_TYPE_LIEU  + " ("+ COLONNE_ID_TYPE + " INT PRIMARY KEY, "  + COLONNE_NOM_TYPE + " VARCHAR(40), " + COLONNE_SELECTED_TYPE + " INT);";

	/**
	 * Constructeur 
	 * @param context contexte de l'application
	 * @param name nom de la base de données
	 * @param factory
	 * @param version version de la base de données
	 */
	public MyTypeLieuBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 *  (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		Log.i("MyTypeLieuBaseHelper","onCreate");
		arg0.execSQL(REQUETE_CREATION_DB);
	}

	/**
	 *  (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		Log.i("MyTypeLieuBaseHelper","onUpgrade");
		arg0.execSQL("DROP TABLE " + TABLE_TYPE_LIEU + ";");
		onCreate(arg0);
	}

}
