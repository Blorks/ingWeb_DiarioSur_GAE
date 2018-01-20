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
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Fileev").addSort("ID", Query.SortDirection.DESCENDING);
		String id;
		
		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
				
		if(listaEntidades.isEmpty()) {
			id = "0";
		}else {
			id = listaEntidades.get(0).getProperty("ID").toString();
		}
				
		return id;
	}
	
	private String incrementarID(String id) {
		int num = Integer.parseInt(id);
		num++;
		return String.valueOf(num);
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


	
	
	//Métodos Públicos
	public void crearFileev(Fileev fe) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Fileev");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", fe.getNombre());
		entidad.setProperty("url", fe.getUrl());
		entidad.setProperty("tipo", fe.getTipo());
		entidad.setProperty("usuasrioId", fe.getUsuarioId());
		
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
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Fileev " + id + " no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	
}
