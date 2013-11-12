package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import android.util.Log;

import com.model.Lieu;
import com.model.Lieus;
import com.model.Type_Lieu;
import com.model.Type_Lieus;

/**
 * Cette est utilisé pour gérer les requetes et les reponses vers le service web
 * @author Herilaza
 *
 */
public class ServiceRequest {
	/**
	 * L'adresse de la ressource du service web
	 */
	private String resAdress;
	/**
	 * Collections soous-forme de clé valeur pour stocker les differents parametres de la requete (requete GET)
	 */
	private Map<String,String> params;
	/**
	 * URL complet de la requete
	 */
	private String url;
	/**
	 * 
	 */
	private ObjectMapper oMapper = null;
	/**
	 * 
	 */
	private JsonFactory jFactory = null;
	/**
	 * 
	 */
	private JsonParser jParser = null;
	/**
	 * 
	 */
	private Lieus lieus = null;
	/**
	 * 
	 */
	private Type_Lieus typeLieus = null;

	/**
	 * Cosntructeur
	 * @param resAdress adresse de la ressource
	 */
	public ServiceRequest(String resAdress){
		this.resAdress = resAdress;
		this.params = new HashMap<String,String>();
		this.url = null;
		jFactory = new JsonFactory();
		oMapper = new ObjectMapper();

	}
	/**
	 * Conestructeur
	 */
	public ServiceRequest(){
		this.resAdress = null;
		this.params = new HashMap<String,String>();
		this.url = null;
		jFactory = new JsonFactory();
		oMapper = new ObjectMapper();

	}
	/**
	 * Ajoute une nouvelle parametre à la collection
	 * @param name nom du parametre
	 * @param value valeur du parametre
	 */
	public void setParameter(String name,String value){
		params.put(name, value);
	}
	/**
	 * Cette methode est utilisé pour envoyer une requete GET vers le service web
	 * @return une chaine de caractère contenant la réponse du serveur
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws JSONException
	 */
	public String sendGetRequest() throws ClientProtocolException, IOException, IllegalStateException, JSONException {
		Set<String> paramKey;
		Iterator<String> iterParamKey;
		Boolean firstParam = true;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get;
		HttpResponse response;
		HttpEntity entity;
		if (params.isEmpty()){
			url = resAdress;
		}
		else {
			paramKey = params.keySet();
			iterParamKey = paramKey.iterator();
			while (iterParamKey.hasNext()){
				String key = iterParamKey.next();
				if(firstParam){
					url = resAdress+"?"+key+"="+params.get(key);
					firstParam = false;
					Log.i("test","url = "+url);
				}
				else {
					url = url+"&"+key+"="+params.get(key);
					Log.i("test","url = "+url);
				}
			}
			Log.i("test","url = "+url);
		}
		get = new HttpGet(url);
		response = client.execute(get);
		entity = response.getEntity();
		String resultat = Tools.convertInputStreamtoString(entity.getContent());
		return resultat;
	}
	
	/**
	 * Permet de convertir le format JSON en un objet de type lieu
	 * @param response chaine de caractere au format JSON
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	
	public ArrayList<Lieu> parseResponseToLieu(String response) throws JsonParseException, IOException {
		if (response != null) {
			if (!(response.equals("null\n"))) {
				List<Lieu> list = new ArrayList<Lieu>();
				try {
					jParser = jFactory.createJsonParser(response);
					lieus = oMapper.readValue(jParser,Lieus.class);
					list =  lieus.get("lieu");
				}
				catch (JsonMappingException e){
					Lieu lieu = oMapper.readValue(jParser,Lieu.class);
					list.add(lieu);
				}
				return (ArrayList<Lieu>) list;
			}
			else return null;
		}
		else return null;
	}
	
	/**
	 * Permet de convertir le format JSON en une liste d'objet de type lieu
	 * @param response chaine de caractere au format JSON
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public ArrayList<Type_Lieu> parseresponseToTypeLieu(String response) throws JsonParseException, IOException {
		if (response != null) {
			List<Type_Lieu> list = new ArrayList<Type_Lieu>();
			try {
				jParser = jFactory.createJsonParser(response);
				typeLieus = oMapper.readValue(jParser,Type_Lieus.class);
				list =  typeLieus.get("typeLieu");
			}
			catch (JsonMappingException e){
				Type_Lieu tLieu = oMapper.readValue(jParser,Type_Lieu.class);
				list.add(tLieu);
			}
			return (ArrayList<Type_Lieu>) list;
		}
		else return null;
	}
	
	
}
