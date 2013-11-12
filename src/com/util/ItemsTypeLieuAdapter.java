package com.util;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.activity.R;
import com.model.Type_Lieu;

/**
 * Adapteur, qui permet de gerer l'affichage des tyoe de lieu dans la liste
 * @author Herilaza
 *
 */
public class ItemsTypeLieuAdapter extends BaseAdapter{

	/**
	 * la liste des type de lieu à afficher
	 */
	private List<Type_Lieu> listType = null; 
	/**
	 * 
	 */
	private LayoutInflater inflater = null;
	/**
	 * Constructeur
	 * @param ctx contexte de l'application
	 * @param list la liste à afficher
	 */
	public ItemsTypeLieuAdapter(Context ctx,List<Type_Lieu> list) {
		super();
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listType = list;
	}

	/**
	 *  (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		Log.i("lisType Size adapter",String.valueOf(listType.size()));
		return listType.size();
	}

	/**
	 *  (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Type_Lieu getItem(int position) {
		return listType.get(position);
	}

	/**
	 *  (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View row, ViewGroup arg2) {
		if (row == null) {
			row = inflater.inflate(R.layout.item_typelieu_view, null);
			Log.i("Row create","null");
		}
		Type_Lieu tLieu = listType.get(position);
		Log.i("Type_Lieu get View",tLieu.getNomType());
		CheckedTextView cbt = (CheckedTextView)row.findViewById(R.id.item);
		cbt.setText(tLieu.getNomType());
		cbt.setChecked(tLieu.getSelected());
		return row;
	}	
	
	

}