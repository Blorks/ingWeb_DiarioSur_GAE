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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import entity.Tagevento;

public class TageventoFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public TageventoFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.DESCENDING);
		Integer id;
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			id = Integer.parseInt(listaEntidades.get(0).getProperty("ID").toString());
		}catch (Exception e) {
			id = 0;
		}
		
		return id;
	}
	
	private Integer incrementarID(Integer id) {
		Integer aux = id + 1;
		return aux;
	}
	
	private List<Tagevento> crearEntidades(List<Entity> listaEntidades) {
		List<Tagevento> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Tagevento tag = new Tagevento();
			
			Object val = e.getProperty("ID");
			tag.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("eventoId");
			tag.setEventoId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("tagId");
			tag.setTagId(Integer.parseInt(val.toString()));
			
			lista.add(tag);
		}
		
		return lista;
	}

	
	

	//M�todos P�blicos
	public void crearTagevento(Tagevento tag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Tagevento");
		Integer init = -1;
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("eventoId", tag.getEventoId() != null ? tag.getEventoId() : init);
		entidad.setProperty("tagId", tag.getTagId() != null ? tag.getTagId() : init);
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Tagevento> encontrarTageventoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			//System.out.println("Evento " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Tagevento> encontrarTagEventoPorTagYEvento(Integer idTag, Integer idEvento){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idEvento);
		FilterPredicate filtro2 = new FilterPredicate("tagId", FilterOperator.EQUAL, idTag);
		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);

		q.setFilter(filtro3);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			//System.out.println("Evento " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Tagevento> encontrarTageventoPorEvento(Integer idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idEvento);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			//System.out.println("Evento " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Tagevento> encontrarTageventoPorTag(Integer idTag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("tagId", FilterOperator.EQUAL, idTag);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			//System.out.println("Evento " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}

	public void eliminarTagEventoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			datastore.delete(conexion, key);
			
		}catch (Exception e) {
			//System.out.println("Fecha " + id + " no encontrada");
		}finally {
			conexion.commit();
		}
		
	}
	
}
