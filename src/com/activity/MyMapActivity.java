package com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.overlay.MyCurrentLocationOverlay;

/**
 * L'Activité Principale Lancée au demarrage de l'application
 * 
 * 
 * @author Herilaza
 *
 */
public class MyMapActivity extends MapActivity implements OnSharedPreferenceChangeListener{
	
    /**
     * 
     * Variable statique pour faciliter la gestion de création de Menu
     */
    private static final int MENU_PARAMETRE = 3;
	/**
	 * Variable statique pour faciliter la gestion de création de Menu
	 */
	private static final int MENU_QUITTER = 2;
	/**
	 * Variable statique pour identifier le requestCode au démarrage de l'Activité paramètre
	 */
	private static final int PREF_REQUEST = 1;
	
	/**
	 * Reference de la carte Google Maps
	 */
	private MapView map;
	/**
	 * Reference du controleur de la carte gogle maps
	 */
	private MapController crtl;
	/**
	 * Reference de la couche qui permet l'affichage de la position de l'utilisateur sur la carte
	 */
	private MyCurrentLocationOverlay myLocation = null;
	/**
	 * Reference du Handler principale pour les processus en arrière-plan
	 */
	private Handler handler;
	/**
	 * Reference du marker utilisé par defaut pour un point d'interet, cette variable est nécessaire a la création de l'instance de la classe Siteoverlay, 
	 * responsable de l'affichage des points d'interets
	 */
	private Drawable marker;
    /** (non-Javadoc)
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("oncreate","oncreate");
        setContentView(R.layout.main);
        handler= new Handler();
        marker = getResources().getDrawable(R.drawable.star);
        map = ((MapView)findViewById(R.id.mymap));
        myLocation = new MyCurrentLocationOverlay(this, map, marker,handler);
        myLocation.enableMyLocation();
        myLocation.runOnFirstFix(new Runnable() {
			
			public void run() {
				Log.i("On create","OnFirstFix");
				if(myLocation.getMyLocation() != null)
                {
                map.getController().animateTo(myLocation.getMyLocation());
                    map.postInvalidate();
                }

			}
		});
        map.getOverlays().add(myLocation);
        map.displayZoomControls(true);
        map.setBuiltInZoomControls(true);
        crtl = map.getController();
        crtl.setZoom(15);
        map.setSatellite(false);
        map.setTraffic(false);
        setupButton();
    }

	/**
	 * (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPause","Pause");
		if (myLocation!=null) myLocation.disableMyLocation();
	}

	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("MyMapActivity","onDestroy");
	}

	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onresume","onresume");
		myLocation.enableMyLocation();
	}

	/**
	 *  (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 *  (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,MENU_PARAMETRE,Menu.NONE,"Parametres");
		menu.add(0,MENU_QUITTER, Menu.NONE,"Quitter");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 *  (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PARAMETRE :
			Intent intent =  new Intent(this,MyPreferenceActivity.class);
			startActivityForResult(intent, PREF_REQUEST);
			break;
		case MENU_QUITTER :
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 *  (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key.equals(getResources().getString(R.string.preference_rayons))){
			myLocation.getPoiSite().setRayon(Double.valueOf(sharedPreferences.getString(key, "null")));
		}
		if(key.equals(getResources().getString(R.string.preference_disque))){
			Log.i("preference disque changé", key);
			myLocation.setShowDisk(sharedPreferences.getBoolean(key,true));
			map.invalidate();
		}
	}

	/**
	 *  (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case PREF_REQUEST : 
				if (resultCode == RESULT_OK) {
					Log.i("result","REDRAW");
					myLocation.getPoiSite().setupPoiToDraw();
				}
				if (resultCode == RESULT_CANCELED) {
					Log.i("result","NOT REDRAW");
				}
				break;
			default :
				break;
		} 
	}
	
	/**
	 * Méthode utilisé pour factoriser le code de création des deux bouttons satellite view et traffic view dans la methode onCreate()
	 * 
	 */
	public void setupButton() {
		final ToggleButton satButton;
		final ToggleButton traButton;
		satButton = ((ToggleButton)findViewById(R.id.satellite));
        traButton = ((ToggleButton)findViewById(R.id.traffic));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
        satButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				if(satButton.isChecked()) {
					map.setSatellite(true);
					map.invalidate();
				} 
				else {
					map.setSatellite(false);
					map.invalidate();
				}
			}
		});
        traButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				if(traButton.isChecked()) {
					map.setTraffic(true);
					map.invalidate();
				}
				else {
					map.setTraffic(false);
					map.invalidate();
				}
			}
		});
        map.postInvalidate();
		map.setClickable(true);
	}
}