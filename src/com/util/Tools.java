package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.android.maps.GeoPoint;

/**
 * Classe utilitaire
 * @author Herilaza
 *
 */
public class Tools {
	/**
	 * Cette m�thode convertit un objet InputStream en chaine de caract�re
	 * @param in objet de type Inputstream
	 * @return une chaine de caract�re
	 * @throws IOException
	 */
	public static String convertInputStreamtoString(InputStream in) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String lg = br.readLine();
		while(lg!=null){
			sb.append(lg+"\n");
			lg = br.readLine();
		}
		return sb.toString();				
	}
	/**
	 * Cette m�thode permet d'avoir un objet geopoint a partir de deux param�tre de type double
	 * @param lat latitude d'un lieu
	 * @param longt longitude d'un lieu
	 * @return un objet de type geopoint
	 */
	public static GeoPoint makePoint(Double lat,Double longt){
		return new GeoPoint((int)(lat*1E6),(int)(longt*1E6));
	}
}
