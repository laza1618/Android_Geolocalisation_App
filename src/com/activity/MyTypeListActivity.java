package com.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.model.MyTypeLieuBaseAdapter;
import com.model.Type_Lieu;
import com.util.ItemsTypeLieuAdapter;

/**
 * Activité lancé lors de la selection du type de lieu à afficher dans le menu paramètre
 * @author Herilaza
 *
 */
public class MyTypeListActivity extends ListActivity {
	
	/**
	 * Référence de la vue qui va contenir la liste des éléménts
	 */
	private ListView lView = null;
	/**
	 * liste des éléments qui vont être affichées dans la liste
	 */
	private ArrayList<Type_Lieu> listType = null;
	/**
	 * clone de la liste affichées mais contient les différents choix des utilisteurs pour une éventuelle sauvegarde
	 */
	private ArrayList<Type_Lieu> listTypeTemp = null;
	/**
	 * Instance de la classe MyTypeLieuBaseAdapter utilisé pour manipuler na base de données SQLite
	 */
	private MyTypeLieuBaseAdapter mBaseAdapter = null;
	/** 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.list_typelieu);
		super.onCreate(savedInstanceState);
		Log.i("ListActivity","onCreateMethod");
		mBaseAdapter = new MyTypeLieuBaseAdapter(this);
		listType = new ArrayList<Type_Lieu>();
		listTypeTemp = new ArrayList<Type_Lieu>();
		listType = mBaseAdapter.getAllTypeLieu();
		this.clone(listTypeTemp,listType);
		lView = getListView();
		lView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		lView.setAdapter(new ItemsTypeLieuAdapter(this,listTypeTemp));
		Button ok = (Button)findViewById(R.id.ok_list);
		Button ko = (Button)findViewById(R.id.ko_list);
		lView.setOnItemClickListener(new OnItemClickListener() {

			/** 
			 * Cette méthode est appelée lors du click sur un élément de la liste
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				checkView(v, position);
			}
		
		});
		ok.setOnClickListener(new OnClickListener() {
			
			/**
			 * Cette fonction est executée lors du click sur le bouton valider de l'activité,
			 * On sauvegarde les choix de l'utilisateur dans la base de données  puis on termine l'activité
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View arg0) {
				ArrayList<Type_Lieu> newList = new ArrayList<Type_Lieu>();
				for (int i = 0 ; i < listType.size() ; i++) {
					if(listType.get(i).getSelected() != listTypeTemp.get(i).getSelected()) {
						newList.add((Type_Lieu) listTypeTemp.get(i));
					}
				}
				Log.i("nombre modifiée",String.valueOf(newList.size()));
				
				if (newList.size() > 0) {
					mBaseAdapter.updatedb(newList);
				}
				setResult(RESULT_OK);
				finish();
			}
		});
		ko.setOnClickListener(new OnClickListener() {
			
			/** 
			 * Cette fonction est executé lors du click sur le boutton annuler,
			 * On termine cette activité
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	/**
	 * Methode  qui consiste à cloner deux ArrayList contenant des objets de type Type_Lieu
	 * @param dest la variable qui va contenir la copie de la liste
	 * @param source la liste qui va etre clonée
	 */
	private void clone(ArrayList<Type_Lieu> dest, ArrayList<Type_Lieu> source) {
		for (Type_Lieu tLieu : source) {
			dest.add((Type_Lieu)tLieu.clone());
		}
	}
	/**
	 * Cette méthode consiste à modifier l'etat du checkbox pendant le click de l'utilisateur sur un élément de la liste affiché,
	 * et modifie l'état de la variable booléene associée à l'élement dans la liste temporaire (listeTemp)
	 * @param v la vue qui contient l'élément de la liste concerné
	 * @param position la position de l'élément concerné
	 */
	private void checkView(final View v,final int position) {
		CheckedTextView check = (CheckedTextView)v.findViewById(R.id.item);
		check.setChecked(!check.isChecked());
		listTypeTemp.get(position).setSelected(check.isChecked());
		Log.i("listTypeTemp",listTypeTemp.get(position).getNomType() + " -- " + String.valueOf(listTypeTemp.get(position).getSelected()));
		Log.i("listType",listType.get(position).getNomType() + " -- " + String.valueOf(listType.get(position).getSelected()));
	}
}
