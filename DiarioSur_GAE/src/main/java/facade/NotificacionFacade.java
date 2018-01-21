package facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
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
	Transaction conexion;
	
	public NotificacionFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.DESCENDING);
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

	
	//Métodos Públicos - CRUD
	public void crearNotificacion(String mensaje, Integer usuarioId) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Notificacion");
		Integer noLeida = 0;
		Integer initInt = -1;
		String initStr = "vacio";
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("descripcion", mensaje != null ? mensaje : initStr);
			entidad.setProperty("leida", noLeida);
			entidad.setProperty("usuarioId", usuarioId != null ? usuarioId : initInt);
			
			conexion = datastore.beginTransaction();
			
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en NotificacionFacade -> crearNotificacion");
		}finally {
			conexion.commit();
		}
	}
	
	public void editarNotificacion(Notificacion noti) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Evento");
		List<Entity> listaEntidades = new ArrayList<>();
		String initStr = "vacio";
		Integer initNoLeida = 0;
		Integer initInt = -1;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, noti.getId());
		q.setFilter(filtro);

		try {
			 listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			 entidad = listaEntidades.get(0);
			 
			 entidad.setProperty("descripcion", noti.getDescripcion() != null ? noti.getDescripcion() : initStr);
			 entidad.setProperty("leida", noti.getLeida() != null ? noti.getLeida() : initNoLeida);
			 entidad.setProperty("usuarioId", noti.getUsuarioId() != null ? noti.getUsuarioId() : initInt);
				
			 datastore.put(conexion, entidad);

		}catch (Exception e) {
			System.out.println("Error en NotificacionFacade -> editarNotificacion");
		}finally {
			conexion.commit();
		}
	}
	
	
	//Métodos Públicos - Find
	public List<Notificacion> encontrarNotificacionesNoLeidasDeUsuario(Integer idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		int leida = 0;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idUsuario);
		FilterPredicate filtro2 = new FilterPredicate("leida", FilterOperator.EQUAL, leida);
		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);
		
		q.setFilter(filtro3);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en NotificacionFacade -> encontrarNotificacionesNoLeidasDeUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Notificacion> encontrarTodasLasNotificacionesDeUsuario(Integer idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idUsuario);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en NotificacionFacade -> encontrarTodasLasNotificacionesDeUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}

	public List<Notificacion> encontrarNotificacionPorId(Integer id){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Notificacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Notificacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en NotificacionFacade -> encontrarNotificacionesPorId");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
}
