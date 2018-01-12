package com;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class DiarioSurFachada {
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;

	public DiarioSurFachada() {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
	}
	
	public void introducirNumero(int num) {
		entidad = new Entity("Numero");
		key = entidad.getKey();
		
		entidad.setProperty("valor", num);
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Integer> encontrarNumeros(){
		List<Integer> lista = new ArrayList<>();
		int num = 0;
		
		entidad = new Entity("Numero");
		key = entidad.getKey();
		
		conexion = datastore.beginTransaction();
		try {
			entidad = datastore.get(conexion, key);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		num = (int) entidad.getProperty("valor");
		
		lista.add(num);
		
		return lista;
	}
}
