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

import entity.Tag;

public class TagFacade implements Serializable{
	private static final long serialVersionUID = 1L;

	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public TagFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.DESCENDING);
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
		Integer aux = id + 1;
		return aux;
	}
	
	private List<Tag> crearEntidades(List<Entity> listaEntidades) {
		List<Tag> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Tag tag = new Tag();
			
			Object val = e.getProperty("ID");
			tag.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("nombre");
			tag.setNombre(val.toString());


			lista.add(tag);
		}
		
		return lista;
	}

	
	
	//Métodos Públicos
	public void crearTag(Tag tag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Tag");
		List<Integer> listaNumero = new ArrayList<>();
		listaNumero.add(-1);
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", tag.getNombre() != null ? tag.getNombre() : "vacio");
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Tag> encontrarTagPorNombre(String nombre) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tag> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("nombre", FilterOperator.EQUAL, nombre);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Tag '" + nombre + "' no encontrado");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Tag> encontrarTagPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tag> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.ASCENDING);
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
}
