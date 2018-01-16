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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import entity.Notificacion;

public class NotificacionFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public NotificacionFacade(){}
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Notificacion> crearEntidades(List<Entity> listaEntidades) {
		List<Notificacion> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Notificacion noti = new Notificacion();
			
			Object val = e.getProperty("ID");
			noti.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("descripcion");
			noti.setDescripcion(val.toString());
			
			val = e.getProperty("leida");
			noti.setLeida(Integer.parseInt(val.toString()));
			
			val = e.getProperty("usuarioId");
			noti.setUsuarioId(Integer.parseInt(val.toString()));

			lista.add(noti);
		}
		
		return lista;
	}

	
	
	
	//Métodos Públicos
	public void crearNotificacion(Notificacion noti) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Notificacion");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("descripcion", noti.getDescripcion());
		entidad.setProperty("leida", noti.getLeida());
		entidad.setProperty("usuarioId", noti.getUsuarioId());
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Notificacion> encontrarNotificacionesNoLeidasDeUsuario(String idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		int userID = Integer.parseInt(idUsuario);
		int leida = 0;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, userID);
		FilterPredicate filtro2 = new FilterPredicate("leida", FilterOperator.EQUAL, leida);
		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);
		
		q.setFilter(filtro3);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Notificacion> encontrarTodasLasNotificacionesDeUsuario(String idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		int userID = Integer.parseInt(idUsuario);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, userID);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public List<Notificacion> encontrarNotificacionPorId(String id){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
}
