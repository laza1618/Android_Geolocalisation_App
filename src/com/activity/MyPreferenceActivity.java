package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

/**
 * L'Acitvité lancé au selection du menu paramètre
 * @author Herilaza
 *
 */
public class MyPreferenceActivity extends android.preference.PreferenceActivity{
	/**
	 * Variable statique pour identifier le requestCode envoyé à l'activité de selection du type de lieu à afficher (MyTypeListActivity)
	 */
	private static final int REQUEST_CODE = 1;
	/**
	 * Variable booléene retourne à l'activité principale lors de la fin de cette activité,
	 * si elle est égale à true, on actualise la liste des points d'interêts affichés sur la carte
	 */
	private boolean result = false;
	/** (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("PreferenceActivity","onCreate");
		addPreferencesFromResource(R.xml.prefs);
		Preference pref = findPreference("test");
		pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			/** (non-Javadoc)
			 * @see android.preference.Preference.OnPreferenceClickListener#onPreferenceClick(android.preference.Preference)
			 */
			public boolean onPreferenceClick(Preference arg0) {
				Intent intent = new Intent(MyPreferenceActivity.this,MyTypeListActivity.class);
				startActivityForResult(intent,REQUEST_CODE);
				return false;
			}
		});
	}

	/** (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (result) {
			setResult(RESULT_OK);
		}
		else {
			setResult(RESULT_CANCELED);
		}
		Log.i("PreferenceActivity","OnDestroy");
	}

	/** 
	 * Cette méthode est appelé lorsque l'activité de selection de type de lieu est terminé,
	 * on evalue ansi s'il y eu modification ou non de la liste des types de lieu à afficher 
	 * @see android.preference.PreferenceActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case REQUEST_CODE : 
				if (resultCode == RESULT_OK) {
					Log.i("resultat","RESULT OK");
					setResult(true);
					setResult(RESULT_OK);
				} 
				if (resultCode == RESULT_CANCELED) {
					Log.i("resultat","RESULT CANCEL");
					setResult(RESULT_CANCELED);
				}
				break;
			default :
				break;
		}
	}

	/**
	 * @return retourne la variable result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result modifie la valeur de la variable result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}
	
}
