package com.overlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.activity.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.util.Tools;

/**
 * Cette classe gère l'affichage de la position de l'utilisateuur sur la carte
 * @author Herilaza
 *
 */
public class MyCurrentLocationOverlay extends MyLocationOverlay {
	/**
	 * Reference de la carte sur laquelle va etre affiché la position de l'utilisateur
	 */
	private MapView mapView;
	/**
	 * Référence du calque qui contient l'affichage des points d'interets
	 */
	private SiteOverlay poiSite;
	/**
	 * le dernier coordonées GPS reçu par l'application
	 */
	private Location lastLoc;
	/**
	 * état de l'affichage du disque de précision de la localisation,
	 * true : on affiche le disque
	 * false : on n'affiche pas le disque
	 */
	private Boolean showDisk;
	/**
	 * Constructeur
	 * @param context contexte de l'application
	 * @param mapView reference de la carte
	 * @param marker marqueur par défaut
	 * @param handler gestionnaire de processus Android
	 */
	public MyCurrentLocationOverlay(Context context, MapView mapView,Drawable marker, Handler handler) {
		super(context, mapView);
		this.mapView = mapView;
		poiSite = new SiteOverlay(marker, context, mapView, handler);
		this.mapView.getOverlays().add(poiSite);
		lastLoc  = null;
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
		showDisk = shared.getBoolean(context.getResources().getString(R.string.preference_disque), false);
	}
	

	/**
	 * @param context contexte de l'application
	 * @param mapView reference de la carte
	 */
	public MyCurrentLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		this.mapView = mapView;	
	}


	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.MyLocationOverlay#onLocationChanged(android.location.Location)
	 */
	@Override
	public synchronized void onLocationChanged(final Location location) {
		super.onLocationChanged(location);
		if(lastLoc!=null) {
			if (lastLoc == location) return;
			else {
				if(location.distanceTo(lastLoc)<=100) return;
			}
		}
		lastLoc = location;
		mapView.setClickable(false);
		final GeoPoint curPoint = Tools.makePoint(location.getLatitude(),location.getLongitude());
		mapView.getController().animateTo(curPoint);
		poiSite.chargePoiArround(location.getLatitude(),location.getLongitude());
		mapView.setClickable(true);
	}

	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.MyLocationOverlay#drawMyLocation(android.graphics.Canvas, com.google.android.maps.MapView, android.location.Location, com.google.android.maps.GeoPoint, long)
	 */
	@Override
	protected void drawMyLocation(Canvas arg0, MapView arg1, Location arg2,
			GeoPoint arg3, long arg4) {
		super.drawMyLocation(arg0, arg1, arg2, arg3, arg4);
		if(showDisk){
			Point ctr = new Point();
			arg1.getProjection().toPixels(arg3,ctr);
			Paint myPaint = new Paint();
			myPaint.setColor(Color.RED);
			myPaint.setAlpha(100);
			myPaint.setAntiAlias(true);
			myPaint.setStyle(Style.STROKE);
			arg0.drawCircle(ctr.x, ctr.y,metersToRadius((float)poiSite.getRayon(),mapView,(double)arg3.getLatitudeE6()/1000000), myPaint);
			Paint myPaint2 = new Paint();
			myPaint2.setColor(Color.BLUE);
			myPaint2.setAlpha(60);
			myPaint2.setAntiAlias(true);
			myPaint2.setStyle(Style.FILL);
			arg0.drawCircle(ctr.x, ctr.y,metersToRadius((float)poiSite.getRayon(),mapView,(double)arg3.getLatitudeE6()/1000000), myPaint2);
		}
	}
	public static int metersToRadius(float meters, MapView map, double latitude) {
	    return (int) (map.getProjection().metersToEquatorPixels(meters*1000) * (1/ Math.cos(Math.toRadians(latitude))));         
	}

	/**
	 * Modifie l'etat de l'affichage du disque
	 * @param showDisk nouvel état de l'affichage du disque
	 */
	public void setShowDisk(Boolean showDisk) {
		this.showDisk = showDisk;
	}


	/**
	 * @return la référence du calque destiné à afficher les points d'interets
	 */
	public SiteOverlay getPoiSite() {
		return poiSite;
	}
	
}
