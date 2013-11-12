package com.overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.activity.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.model.Lieu;
import com.model.MyTypeLieuBaseAdapter;
import com.util.DownloadImageTask;
import com.util.ServiceRequest;
import com.util.Tools;

/**
 * 
 * classe permettant de gerer l'affichage des points d'interets sur la carte
 * @author Herilaza
 *
 */
public class SiteOverlay extends ItemizedOverlay<MyOverlayItem>{
	/**
	 * URL pointant sur une méthode du service web pour effectuer les requetes sur la liste des lieu à afficher 
	 */
	private static final String RESSOURCE = "http://10.0.2.2:8080/service-rest/ressources/poi";
	/**
	 * repertoire sur le serveur contenant les images des lieus
	 */
	private static final String IMG_RESSOURCE_DIR = "http://10.0.2.2:8080/service-rest/img/";
	
	/**
	 * liste contenant des objets de type MyOverlayItem
	 */
	private List<MyOverlayItem> items = new ArrayList<MyOverlayItem>();
	/**
	 * marquer par défaut
	 */
	private Drawable marker = null;
	/**
	 * contexte de l'application
	 */
	private Context context = null;
	/**
	 * getionnaire de processus Android
	 */
	private Handler handler = null;
	/**
	 * reference de la carte Google Maps
	 */
	private MapView mapView = null;
	/**
	 * rayon de précision de la localisation
	 */
	private Double rayon = null;
	/**
	 * latitude du lieu
	 */
	private Double latitude = null;
	/**
	 * longitude du lieu
	 */
	private Double longitude = null;
	/**
	 * la vue affichant les information du lieu lors du click de l'utilisateur sur ce dernier
	 */
	private InfoView info = null;
	/**
	 * Instance de la classe DownloadImageTask, qui gere le telechargement de l'image du lieu à affciher depuis le serveur web
	 */
	private DownloadImageTask dTask = null;
	/**
	 * liste contenant des objets de type lieu
	 */
	private ArrayList<Lieu> listLieu = null;
	/**
	 * reponse des requetes effectuées au serveur
	 */
	private String response = null;
	/**
	 * la liste des identifiant du type de lieu à ne pas afficher
	 */
	private ArrayList<Integer> listItemToNotDisplay = null;
	/**
	 * constructeur
	 * @param defaultMarker marqueur par défaut
	 * @param context contexte de l'application
	 * @param mapView référence de la carte
	 * @param handler gestionnaire de processus Android
	 */
	public SiteOverlay(Drawable defaultMarker,Context context,MapView mapView, Handler handler) {
		super(defaultMarker);
		marker = defaultMarker;
		this.context = context;
		this.mapView = mapView;
		this.handler = handler;
		dTask  = new DownloadImageTask(handler);
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
		this.rayon = Double.valueOf(shared.getString(context.getResources().getString(R.string.preference_rayons),"0.5"));
		Log.i("Valeur Rayon",String.valueOf(rayon));
		marker.setBounds(-marker.getIntrinsicWidth()/2,-marker.getIntrinsicHeight(),marker.getIntrinsicWidth()/2,0);
		populate();
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected MyOverlayItem createItem(int i) {
		return items.get(i);
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return items.size();
	}

	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int index) {
		boolean isNull;
		Log.i("on tap","on tap");
		if(DownloadImageTask.downThread != null){
			Log.i("Thread download","not null");
			if(DownloadImageTask.downThread.isAlive()){
				Log.i("Thread download","Alive");
				DownloadImageTask.downThread.interrupt();
			}
		}
		MyOverlayItem item = this.createItem(index);
		mapView.getController().animateTo(item.getPoint());
		if (info == null){
			Log.i("info","null");
			info  = new InfoView(context,item);
			isNull = false;
		}
		else {
			isNull = true;
			info.setmItem(item);
			Log.i("info","not null");
		}
		info.setVisibility(View.GONE);
		info.setupView();
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,item.getPoint(),MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		if (isNull){
			info.setLayoutParams(params);
			if(!info.isShown()){
				info.setVisibility(View.VISIBLE);
				Log.i("info","invisible");
			}
			Log.i("isNUll","true");
		}
		else {
			mapView.addView(info, params);
			Log.i("isNUll","false");
		}
		startanimation(info.getiView());
		info.setVisibility(View.VISIBLE);
		Log.i("url image",IMG_RESSOURCE_DIR+item.getPic());
		dTask.DownloadImage(IMG_RESSOURCE_DIR+item.getPic(), info.getiView());
		setCloseClickListener();
		return isNull;
	}
	/**
	 * methode est appelé lorsqu'on veut effacer les elements de la liste contenant des objets MyOverlayitem
	 */
	public void clearList(){
		if(!items.isEmpty()) {
			items.clear();
			setLastFocusedIndex(-1);
			populate();
		}
	}
	/**
	 * Cette méthode effectue la requete vers le service web, prend la liste des leius à afficher et les affiche sur la carte
	 * @param latitude la latitude du lieu
	 * @param longitude la longitude du lieu
	 */
	public void chargePoiArround(double latitude,double longitude){
		Log.i("test","dans chargepoiarround");
		this.latitude = latitude;
		this.longitude = longitude;
		ServiceRequest servReq = new ServiceRequest(RESSOURCE);
		servReq.setParameter("latitude",String.valueOf(latitude));
		servReq.setParameter("longitude",String.valueOf(longitude));
		servReq.setParameter("rayon",String.valueOf(this.rayon));
		Log.i("info","latitude="+latitude+"longitude="+longitude+"rayon="+rayon);
		try {
			Log.i("test","handefa requete GET");
			response = servReq.sendGetRequest();
			Log.i("response",response);
			if (response != null){
				this.listLieu = servReq.parseResponseToLieu(response);
				setupPoiToDraw();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * cette méthode est appelé lorsque le rayon de précicion à changé depuis le menu paramètre de l'application
	 * @param rayon le nouveau rayon de précision
	 */
	public void setRayon(double rayon){
		this.rayon = rayon;
		Log.i("rayon vaovao",String.valueOf(this.rayon));
		if(longitude != null && latitude != null){
			Thread myThread = new Thread(new Runnable() {
				
				public void run() {
					chargePoiArround(latitude,longitude);
					GeoPoint curPoint = Tools.makePoint(latitude,longitude);
					mapView.getController().setCenter(curPoint);
					mapView.setClickable(false);
					handler.post(new Runnable() {
						
						public void run() {
							mapView.postInvalidate();
							mapView.setClickable(true);
						}
					});
				}
			});
			myThread.start();
		}
	}
	/**
	 * @return retourne la valeur du rayon de précision
	 */
	public double getRayon() {
		return rayon;
	}
	/**
	 * enregistre un écouteur d'evenement sur le bouton fermer de la bulle d'info
	 */
	public void setCloseClickListener() {
		ImageView close = (ImageView)info.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				info.setVisibility(View.GONE);
			}
		});
	}
	/**
	 * démmare l'animation de chargement de l'image
	 * @param v la vue qui affiche l'image du lieu
	 */
	public void startanimation(ImageView v){
		v.setBackgroundResource(R.drawable.loadanim_2);
		AnimationDrawable anim = (AnimationDrawable)v.getBackground();
		anim.start();
	}
	/**
	 * ajoute un objet de type MyOverlayItem dans la liste
	 * @param lieu le lieu a mettre dans un objet MyOverlayitem
	 */
	public void addMyOverlayItem(Lieu lieu){
		String title,pic,snippet,type;
		GeoPoint point = Tools.makePoint(lieu.getLatLieu(),lieu.getLongLieu());
		title = lieu.getNomLieu();
		pic = lieu.getImgLieu();
		snippet = lieu.getDescLieu();
		type = lieu.getTypeLieu().getNomType();
		items.add(new MyOverlayItem(context, point, title, snippet, pic, type));
		setLastFocusedIndex(-1);
		populate();
	}
	/**
	 * cette méthode filtre les lieus à afficher sur la carte en fonction des parametres de l'utilisateur
	 */
	public void setupPoiToDraw() {
		if (this.listLieu != null) {
			this.clearList();
			MyTypeLieuBaseAdapter mBaseAdapter = new MyTypeLieuBaseAdapter(context);
			this.listItemToNotDisplay = mBaseAdapter.getListTypeToNotDisplay();
			for (Lieu lieu : this.listLieu) {
				if (!(this.listItemToNotDisplay.contains(lieu.getTypeLieu().getIdType()))) {
					addMyOverlayItem(lieu);
				}
			}
			mapView.postInvalidate();
		}
	}
}
