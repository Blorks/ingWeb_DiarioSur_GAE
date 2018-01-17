package facade;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import entity.Calendario;
import entity.Evento;
import entity.Notificacion;
import entity.Tagusuario;
import entity.Usuario;

public class UsuarioFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	private int edit = 0;
	Key key;
	Transaction conexion;
	
	public UsuarioFacade(){}
	
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Usuario> crearEntidades(List<Entity> listaEntidades) {
		List<Usuario> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Usuario user = new Usuario();
			
			Object val = e.getProperty("ID");
			user.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("nombre");
			user.setNombre(val.toString());
			
			val = e.getProperty("apellidos");
			user.setApellidos(val.toString());
			
			val = e.getProperty("email");
			user.setEmail(val.toString());
			
			val = e.getProperty("hashpassword");
			user.setHashpassword(val.toString());
			
			val = e.getProperty("rol");
			user.setRol(val.toString());
			
			val = e.getProperty("fotoPerfil");
			user.setFileevId(Integer.parseInt(val.toString()));
			
			
			//No se como recoger los list
		
			val = e.getProperty("tagUsuarioList");
			List<Tagusuario> listaTagusuario = (List<Tagusuario>) val;
			user.setTagusuarioList(listaTagusuario);
			
			val = e.getProperty("notificacionList");
			List<Notificacion> listaNotificacion = (List<Notificacion>) val;
			user.setNotificacionList(listaNotificacion);
			
			val = e.getProperty("eventoList");
			List<Evento> listaEvento = (List<Evento>) val;
			user.setEventoList(listaEvento);			
			
			lista.add(user);
		}
		
		return lista;
	}
	
	
	
	
	
	//M�todos P�blicos
	public void crearUsuario(Usuario user) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Usuario");
		key = entidad.getKey();
		String ultimoID = ultimoIdInsertado();
		
		if(edit == 1) {
			ultimoID = user.getId().toString();
		}else if(user.getId() == null){
			ultimoID = "1";
		}else {
			ultimoID = incrementarID(ultimoID);
		}
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", user.getNombre());
		entidad.setProperty("apellidos", user.getApellidos());
		entidad.setProperty("email", user.getEmail());
		entidad.setProperty("hashPassword", user.getHashpassword());
		entidad.setProperty("rol", user.getRol());
		entidad.setProperty("fotoPerfil", user.getFileevId());
		entidad.setProperty("calendarioList", user.getCalendarioList());
		entidad.setProperty("tagUsuarioList", user.getTagusuarioList());
		entidad.setProperty("notificacionList", user.getNotificacionList());
		entidad.setProperty("eventoListt", user.getEventoList());
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}

	public void editarUsuario(Usuario user) {
		edit = 1;
		crearUsuario(user);
	}
	
	
	public List<Usuario> encontrarUsuarioPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Usuario> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Usuario> encontrarUsuarioPorEmail(String email) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Usuario> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("email", FilterOperator.EQUAL, email);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public void eliminarUsuarioPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Usuario").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		
		Key key = listaEntidades.get(0).getKey();
		datastore.delete(conexion, key);
		conexion.commit();
	}
	
	
	
	
	
	
	
}
