package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;


public class DiarioSurFachada implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;

	public DiarioSurFachada() {
		
	}
	
	public void introducirNumero(String num) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		entidad = new Entity("Numero");
		key = entidad.getKey();
		
		entidad.setProperty("valor", num);
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<String> encontrarNumeros(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<String> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Numero").addSort("valor", Query.SortDirection.ASCENDING);
		
		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		for(Entity e: listaEntidades) {
			Object val = e.getProperty("valor");
			
			lista.add(val.toString());
		}
		
		return lista;
	}
}
