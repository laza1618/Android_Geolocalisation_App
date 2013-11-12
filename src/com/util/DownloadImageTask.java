package com.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.activity.R;
import com.overlay.InfoView;

/**
 * Cette classe est utilis� pour t�l�charger l'image du lieu depuis le serveur
 * @author Herilaza
 *
 */
public class DownloadImageTask {
	/**
	 * gestionnaire de Processus Android
	 */
	private Handler handler;
	/**
	 * l'image au format bitmap
	 */
	private Bitmap img;
	/**
	 * repertoire du serveur contenant les images
	 */
	private String url;
	/**
	 * 
	 */
	private static DefaultHttpClient client;
	/**
	 * 
	 */
	private static HttpGet get;
	/**
	 * 
	 */
	private static HttpResponse response;
	/**
	 * 
	 */
	private static HttpEntity entity;
	/**
	 * le thread de telechargement de l'image
	 */
	public static Thread downThread;
	/**
	 * Constructeur
	 * @param handler gestionnaire de processus Android
	 */
	public DownloadImageTask(Handler handler) {
		this.handler = handler;
		img = null;
		this.url = null;
		downThread = null;
	}
	
	/**
	 * Cette m�thode est app�l� pour d�marrer le t�l�chargement de l'image
	 * @param url URL de la ressource sur le serveur
	 * @param v la vue qui va afficher l'image
	 */
	public synchronized void DownloadImage(String url,final ImageView v){
		setUrl(url);
		downThread = new Thread(new Runnable(){
			public void run() {
				try {
					img = getImageStream();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e){
					startanimation(v);
					System.gc();
				}
				handler.post(new Runnable(){

					public void run() {
						v.setBackgroundResource(0);
						updateView(v);
					}
					
				});
			}
			
		});
		downThread.start();
	}
	/**
	 * @return l'URL de la ressource sur le serveur
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Cette m�thode modifie l'URL de la ressoruce sur le serveur
	 * @param URL de la ressource sur le serveur
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Convertit un InputStream en Bitmap
	 * @return l'image au format bitmap
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws OutOfMemoryError
	 */
	public Bitmap getImageStream() throws ClientProtocolException, IOException, OutOfMemoryError{
		InputStream is;
		client = new DefaultHttpClient();
		get = new HttpGet(getUrl());
		response = client.execute(get);
		entity = response.getEntity();
		is = entity.getContent();
		img = BitmapFactory.decodeStream(is);
		is.close();
		return img;
	}
	/**
	 * permet de mettre � jour la vue qui affiche l'image
	 * @param v la vue qui affiche l'image
	 */
	public void updateView(ImageView v){
		InfoView.stopAnim();
		v.setImageBitmap(img);
		System.gc();
	}
	/**
	 * Cette m�thode d�marre l'animation de chargement du telechargemenr
	 * @param v la vue qui affiche l'image
	 */
	public void startanimation(ImageView v){
		v.setBackgroundResource(R.drawable.loadanim_2);
		AnimationDrawable anim = (AnimationDrawable)v.getBackground();
		anim.start();
	}
	/**
	 * Cette m�thode annule le telechergement en cours
	 */
	public static void abortDownload(){
		get = null;
		response = null;
		entity = null;
	}
}
