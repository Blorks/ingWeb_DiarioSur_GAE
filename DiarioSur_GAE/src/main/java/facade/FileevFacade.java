package facade;

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
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import entity.Fileev;

public class FileevFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public FileevFacade(){}
	
	
	private Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Fileev").addSort("ID", Query.SortDirection.DESCENDING);
		Integer id;
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			id = Integer.parseInt(listaEntidades.get(0).getProperty("ID").toString());
		}catch (Exception e) {
			id = 0;
		}
		
		return id;
	}
	
	private Integer incrementarID(Integer id) {
		id = id + 1;
		return id;
	}
	
	private List<Fileev> crearEntidades(List<Entity> listaEntidades) {
		List<Fileev> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Fileev fe = new Fileev();
			
			Object val = e.getProperty("ID");
			fe.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("nombre");
			fe.setNombre(val.toString());
			
			val = e.getProperty("url");
			fe.setUrl(val.toString());
			
			val = e.getProperty("tipo");
			fe.setTipo(val.toString());
			
			val = e.getProperty("usuarioId");
			fe.setUsuarioId(Integer.parseInt(val.toString()));
				
			lista.add(fe);
		}
		
		return lista;
	}


	
	
	//M�todos P�blicos
	public void crearFileev(Fileev fe) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Fileev");
		String initStr = "vacio";
		Integer initInt = -1;
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", fe.getNombre() != null ? fe.getNombre() : initStr);
		entidad.setProperty("url", fe.getUrl() != null ? fe.getUrl() : initStr);
		entidad.setProperty("tipo", fe.getTipo() != null ? fe.getTipo() : initStr);
		entidad.setProperty("usuarioId", fe.getUsuarioId() != null ? fe.getUsuarioId() : initInt);
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Fileev> encontrarArchivoPorURL(String url) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Fileev> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Fileev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("url", FilterOperator.EQUAL, url);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Fileev> encontrarArchivoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Fileev> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Fileev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			System.out.println(listaEntidades.size() + " lista encontrarArchivo");
			lista = crearEntidades(listaEntidades.subList(0, 1));
		}catch (Exception e) {
			System.out.println("Fileev " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	
}
