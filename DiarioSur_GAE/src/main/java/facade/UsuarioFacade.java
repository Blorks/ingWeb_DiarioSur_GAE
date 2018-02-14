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

import entity.Usuario;

public class UsuarioFacade implements Serializable{
	private static final long serialVersionUID = 1L;
		
	private DatastoreService datastore;
	private Entity entidad;
	Transaction conexion;
	
	public UsuarioFacade(){}
	
	
	private Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Usuario> crearEntidades(List<Entity> listaEntidades) {
		List<Usuario> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Usuario user = new Usuario();
			
			Object val = e.getProperty("ID");
			Long temp = Long.parseLong(val.toString());
			user.setId(Integer.parseInt(temp.toString()));
			
			val = e.getProperty("nombre");
			user.setNombre(val.toString());
			
			val = e.getProperty("apellidos");
			user.setApellidos(val.toString());
			
			val = e.getProperty("email");
			user.setEmail(val.toString());
			
			val = e.getProperty("hashpassword");
			user.setHashpassword(val.toString());;
			
			val = e.getProperty("rol");
			user.setRol(val.toString());
			
			val = e.getProperty("fileevId");
			temp = Long.parseLong(val.toString());
			user.setFileev(Integer.parseInt(temp.toString()));
			
			
			lista.add(user);
		}
		
		return lista;
	}

	
	
	//Métodos Públicos - CRUD
	public void crearUsuario(Usuario user) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Usuario");
		List<Integer> listaNumero = new ArrayList<>();
		listaNumero.add(-1);
		Integer init = -1;
		String initStr = "vacio";

		Integer ultimoID;
		
		ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("nombre", user.getNombre() != null ? user.getNombre() : initStr);
			entidad.setProperty("apellidos", user.getApellidos() != null ? user.getApellidos() : initStr);
			entidad.setProperty("email", user.getEmail() != null ? user.getEmail() : initStr);
			entidad.setProperty("hashpassword", user.getHashpassword() != null ? user.getHashpassword() : initStr);
			entidad.setProperty("rol", user.getRol() != null ? user.getRol() : initStr);
			entidad.setProperty("fileevId", user.getFileev() != null ? user.getFileev() : init);
			
			conexion = datastore.beginTransaction();
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en UsuarioFacade -> crearUsuario");
		}finally {
			conexion.commit();
		}
	}
	
	public void eliminarUsuarioPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			
			datastore.delete(conexion, key);
		}catch (Exception e) {
			System.out.println("Error en UsuarioFacade -> eliminarUsuarioPorID");
		}
		
		conexion.commit();
	}
	
	public void editarUsuario(Usuario user) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Usuario");
		List<Entity> listaEntidades = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, user.getId());
		q.setFilter(filtro);

		try {
			 listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			 entidad = listaEntidades.get(0);
			 
			 entidad.setProperty("ID", user.getId());
			 entidad.setProperty("nombre", user.getNombre());
			 entidad.setProperty("apellidos", user.getApellidos());
			 entidad.setProperty("email", user.getEmail());
			 entidad.setProperty("hashpassword", user.getHashpassword());
			 entidad.setProperty("rol", user.getRol());
			 entidad.setProperty("fileevId", user.getFileev());
				
			 datastore.put(conexion, entidad);

		}catch (Exception e) {
			System.out.println("Error en UsuarioFacade -> editarUsuario");
		}
		
		conexion.commit();
	}
	
	
	//Métodos Públicos - CRUD
	public List<Usuario> encontrarUsuarioPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Usuario> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));

			if(!listaEntidades.isEmpty()){
				lista = crearEntidades(listaEntidades.subList(0, 1));				
			}
		}catch (Exception e) {
			System.out.println("Error en UsuarioFacade -> encontrarUsuarioPorID");
		}finally {
			conexion.commit();
		}
		
		return lista;
	}
	
	public List<Usuario> encontrarUsuarioPorEmail(String email) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Usuario> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("email", FilterOperator.EQUAL, email);
		q.setFilter(filtro);
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));

			if(!listaEntidades.isEmpty()){
				lista = crearEntidades(listaEntidades);
			}

		}catch (Exception e) {
			System.out.println("Error en UsuarioFacade -> encontrarUsuarioPorEmail");
		}finally {
			conexion.commit();
		}
		return lista;
	}

	
	

	
	
	
	
	
	
	
}
