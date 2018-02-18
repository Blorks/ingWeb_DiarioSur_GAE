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
import entity.Tagevento;

public class TageventoFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Transaction conexion;
	
	public TageventoFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.DESCENDING);
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


	//Métodos Públicos - CRUD
	public void crearTagevento(Tagevento tag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Tagevento");
		Integer init = -1;
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("eventoId", tag.getEventoId() != null ? tag.getEventoId() : init);
			entidad.setProperty("tagId", tag.getTagId() != null ? tag.getTagId() : init);
			
			conexion = datastore.beginTransaction();
			
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en TageventoFacade -> crearTagevento");
		}finally {
			conexion.commit();
		}
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
			System.out.println("Error en TageventoFacade -> eliminarTagEventoPorID");
		}finally {
			conexion.commit();
		}
	}

	
	//Métodos Públicos - FIND
	public List<Tagevento> encontrarTageventoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en TageventoFacade -> encontrarTageventoID");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Tagevento> encontrarTagEventoPorTagYEvento(Integer idTag, Integer idEvento){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		List<Tagevento> tageventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if((lista.get(i).getEventoId() == idEvento) && (lista.get(i).getTagId() == idTag)) {
					tageventos.add(lista.get(i));
				}
			}
			
		}catch (Exception e) {
			System.out.println("Error en TageventoFacade -> encontrarTagEventoPorTagYEvento");
		}finally {
			conexion.commit();
		}

		return tageventos;
	}
	
	public List<Tagevento> encontrarTageventoPorEvento(Integer idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		List<Tagevento> tageventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getEventoId() == idEvento) {
					tageventos.add(lista.get(i));
				}
			}
			
		}catch (Exception e) {
			System.out.println("Error en TageventoFacade -> encontrarTageventoPorEvento");
		}finally {
			conexion.commit();
		}

		return tageventos;
	}
	
	public List<Tagevento> encontrarTageventoPorTag(Integer idTag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagevento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("tagId", FilterOperator.EQUAL, idTag);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en TageventoFacade -> encontrarTageventoPorTag");
		}finally {
			conexion.commit();
		}

		return lista;
	}

}
