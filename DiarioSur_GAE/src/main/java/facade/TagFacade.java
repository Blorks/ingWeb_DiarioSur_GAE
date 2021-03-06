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
		id = id + 1;
		return id;
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

	
	//M�todos P�blicos - CRUD
	public void crearTag(Tag tag) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Tag");
		List<Integer> listaNumero = new ArrayList<>();
		listaNumero.add(-1);
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("nombre", tag.getNombre() != null ? tag.getNombre() : "vacio");
			
			conexion = datastore.beginTransaction();
			
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en TagFacade -> crearTag");
		}finally {
			conexion.commit();
		}
	}
	
	public void eliminarTagPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			datastore.delete(conexion, key);
			
		}catch (Exception e) {
			System.out.println("Error en TagFacade -> eliminarTagPorID");
		}finally {
			conexion.commit();
		}
	}

	
	//M�todos P�blicos - FIND
	public List<Tag> encontrarTagPorNombre(String nombre) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tag> lista = new ArrayList<>();
		List<Tag> tags = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getNombre().equalsIgnoreCase(nombre)) {
					tags.add(lista.get(i));
				}
			}
			
		}catch (Exception e) {
			System.out.println("Error en TagFacade -> encontrarTagPorNombre");
		}finally {
			conexion.commit();
		}

		return tags;
	}
	
	public List<Tag> encontrarTagPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Tag> lista = new ArrayList<>();
		List<Tag> tags = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Tag").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getId() == id) {
					tags.add(lista.get(i));
				}
			}
		}catch (Exception e) {
			System.out.println("Error en TagFacade -> encontrarTagPorID");
		}finally {
			conexion.commit();
		}
		return tags;
	}
}
