package com.overlay;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.R;

/**
 * Cette classe crée la vue (infobulle) qui sera affiché lors de click sur un point d'interet
 * @author Herilaza
 *
 */
public class InfoView extends FrameLayout {
	/**
	 * Animatino affiché lors du chargement de l'image
	 */
	private static AnimationDrawable animLoad;
	/**
	 * 
	 * la vue qui affcihe l'image du lieu
	 */
	private ImageView iView;
	/**
	 * objets contenant les informations sur un lieu
	 */
	private MyOverlayItem mItem;
	/**
	 * @param ctx contexte de l'applciation
	 * @param item objet de type MyOverayItem, 
	 */
	public InfoView(Context ctx, MyOverlayItem item) {
		super(ctx);
		final LinearLayout layout = new LinearLayout(ctx);
		layout.setVisibility(VISIBLE);
		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.infoview,layout);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
		addView(layout, params);
		iView  = (ImageView)layout.findViewById(R.id.image);
		iView.setBackgroundResource(R.drawable.loadanim);
		animLoad = (AnimationDrawable)iView.getBackground();
		mItem = item;
	}
	/**
	 * démarrer l'animation
	 */
	public static void startAnim(){
		animLoad.start();
	}
	/**
	 * stopper l'animation
	 */
	public static void stopAnim(){
		animLoad.stop();
	}
	/**
	 * permet d'initialiser les données à afficher
	 *  sur la vue à partie d'un objet MyOverlayItem
	 */
	public void setupView() {
		iView= (ImageView)findViewById(R.id.image);
		iView.setImageBitmap(null);
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(mItem.getTitle());
		TextView snippet = (TextView)findViewById(R.id.snippet);
		snippet.setText(mItem.getSnippet());
	}
	/**
	 * @return la vue destiné à afficher l'image du lieu
	 */
	public ImageView getiView() {
		return iView;
	}
	/**
	 * @return l'objet MyOverlayItem
	 */
	public MyOverlayItem getmItem() {
		return mItem;
	}
	/**
	 * Modifie l'objet MyOverlayItem
	 * @param mItem le nouveau objet MyOverlayItem
	 */
	public void setmItem(MyOverlayItem mItem) {
		this.mItem = mItem;
	}
	
}