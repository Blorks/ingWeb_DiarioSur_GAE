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
	
	@SuppressWarnings("unchecked")
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
			
			val = e.getProperty("tagUsuarioList");
			List<Integer> listaTagusuario = (List<Integer>) val;
			user.setTagusuarioList(listaTagusuario);
			
			val = e.getProperty("notificacionList");
			List<Integer> listaNotificacion = (List<Integer>) val;
			user.setNotificacionList(listaNotificacion);
			
			val = e.getProperty("eventoList");
			List<Integer> listaEvento = (List<Integer>) val;
			user.setEventoList(listaEvento);
			
			lista.add(user);
		}
		
		return lista;
	}

	
	
	//Métodos Públicos
	public void crearUsuario(Usuario user) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Usuario");
		List<Integer> listaNumero = new ArrayList<>();
		listaNumero.add(-1);
		String hash = "";

		Integer ultimoID;
		
		ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);

		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", user.getNombre());
		entidad.setProperty("apellidos", user.getApellidos());
		entidad.setProperty("email", user.getEmail());
		entidad.setProperty("hashpassword", "vacio");
		entidad.setProperty("rol", user.getRol());
		entidad.setProperty("fileevId", user.getFileev() != null ? user.getFileev() : new Integer(-1));
		entidad.setProperty("tagUsuarioList", user.getTagusuarioList() != null ? user.getTagusuarioList() : listaNumero);
		entidad.setProperty("notificacionList", user.getNotificacionList() != null ? user.getTagusuarioList() : listaNumero);
		entidad.setProperty("eventoListt", user.getEventoList() != null ? user.getTagusuarioList() : listaNumero);
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
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
			 entidad.setProperty("hashPassword", user.getHashpassword());
			 entidad.setProperty("rol", user.getRol());
			 entidad.setProperty("fileevId", user.getFileev());
			 entidad.setProperty("tagUsuarioList", user.getTagusuarioList());
			 entidad.setProperty("notificacionList", user.getNotificacionList());
			 entidad.setProperty("eventoListt", user.getEventoList());
				
			 datastore.put(conexion, entidad);

		}catch (Exception e) {
			System.out.println("Usuario " + user.getNombre() + " " + user.getApellidos() + " no encontrado. UsuarioFacade.editarUsuario");
		}
		
		conexion.commit();
	}
	
	public List<Usuario> encontrarUsuarioPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Usuario> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

			if(!listaEntidades.isEmpty()){
				lista = crearEntidades(listaEntidades.subList(0, 1));				
			}
		}catch (Exception e) {
			System.out.println("Usuario " + id + " no encontrado. UsuarioFacade.encontrarUsuarioPorID");
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
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

			if(!listaEntidades.isEmpty()){
				lista = crearEntidades(listaEntidades.subList(0, 1));				
			}

		}catch (Exception e) {
			System.out.println("Usuario " + email + " no encontrado. (UsuarioFacade.encontrarUsuarioPorEmail())");
		}finally {
			conexion.commit();
		}
		return lista;
	}
	
	public void eliminarUsuarioPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			
			datastore.delete(conexion, key);
		}catch (Exception e) {
			System.out.println("Usuario " + id + " no encontrado. (UsuarioFacade.eliminarUsuarioPorID");
		}
		
		conexion.commit();
	}
	
	
	
	
	
	
	
}
