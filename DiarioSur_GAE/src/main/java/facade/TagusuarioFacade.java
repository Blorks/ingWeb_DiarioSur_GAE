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

import entity.Tagusuario;

public class TagusuarioFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public TagusuarioFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Tagusuario> crearEntidades(List<Entity> listaEntidades) {
		List<Tagusuario> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Tagusuario tag = new Tagusuario();
			
			Object val = e.getProperty("ID");
			tag.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("usuarioId");
			tag.setUsuarioId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("tagId");
			tag.setTagId(Integer.parseInt(val.toString()));
			
			lista.add(tag);
		}
		
		return lista;
	}

	

	//Métodos Públicos
	public void crearTagusuario(Tagusuario tag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Tagusuario");
		Integer initInt = -1;
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("usuarioId", tag.getUsuarioId() != null ? tag.getUsuarioId() : initInt);
		entidad.setProperty("tagId", tag.getTagId() != null ? tag.getTagId() : initInt);
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Tagusuario> encontrarTagusuarioPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagusuario> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagusuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Tagusuario> encontrarTagEventoPorTagYUsuario(String idTag, String idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagusuario> lista = new ArrayList<>();
		int idT = Integer.parseInt(idTag);
		int idE = Integer.parseInt(idUsuario);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idE);
		FilterPredicate filtro2 = new FilterPredicate("tagId", FilterOperator.EQUAL, idT);
		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);
		
		q.setFilter(filtro3);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Tagusuario> encontrarTagusuarioPorUsuario(String idUsuario) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tagusuario> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(idUsuario);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tagevento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	
}
