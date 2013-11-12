package com.overlay;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Cette classe est utlisé pour contenir les differentes informations affichés concernant un sur la carte
 * @author Herilaza
 *
 */
public class MyOverlayItem extends OverlayItem {
	/**
	 * coordonnées GPS du lieu
	 */
	private GeoPoint point;
	/**
	 * le nom du lieu
	 */
	private String title;
	/**
	 * la description du lieu
	 */
	private String snippet;
	/**
	 * le nom de l'image du lieu
	 */
	private String pic;
	/**
	 * libellé du type du lieu
	 */
	private String type;

	/**
	 * Constructeur
	 * @param context contexte de l'application
	 * @param point coordonnées GPS
	 * @param title le nom du lieu
	 * @param snippet la description du lieu
	 * @param pic le nom de l'image du leiu
	 * @param type le type du lieu
	 */
	public MyOverlayItem(Context context,GeoPoint point ,String title, String snippet,String pic, String type) {
		super(point, title, snippet);
		this.point = point;
		this.title = title;
		this.snippet = snippet;
		this.pic = pic;
		this.type = type;
		int res = context.getResources().getIdentifier(type,"drawable",context.getPackageName());
		setMarker(context.getResources().getDrawable(res));
	}
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.OverlayItem#setMarker(android.graphics.drawable.Drawable)
	 */
	@Override
	public void setMarker(Drawable marker) {
		marker.setBounds(-marker.getIntrinsicWidth()/2,-marker.getIntrinsicHeight(),marker.getIntrinsicWidth()/2,0);
		super.setMarker(marker);
	}
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.OverlayItem#getPoint()
	 */
	@Override
	public GeoPoint getPoint() {
		return point;
	}
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.OverlayItem#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.OverlayItem#getSnippet()
	 */
	public String getSnippet() {
		return snippet;
	}
	/**
	 * @return le nom de l'image de l'application
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * @return le libellé du type de l'application
	 */
	public String getType() {
		return type;
	}
	
}
