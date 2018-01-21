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

import entity.Puntuacion;

public class PuntuacionFacade implements Serializable {
	private static final long serialVersionUID = 1L;

	private DatastoreService datastore;
	private Entity entidad;
	Transaction conexion;

	public PuntuacionFacade() {
	}

	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.DESCENDING);
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

	private List<Puntuacion> crearEntidades(List<Entity> listaEntidades) {
		List<Puntuacion> lista = new ArrayList<>();

		for (Entity e : listaEntidades) {
			Puntuacion pt = new Puntuacion();

			Object val = e.getProperty("ID");
			pt.setId(Integer.parseInt(val.toString()));

			val = e.getProperty("puntuacion");
			pt.setPuntuacion(Double.parseDouble(val.toString()));

			val = e.getProperty("eventoId");
			pt.setEventoId(Integer.parseInt(val.toString()));

			val = e.getProperty("usuarioId");
			pt.setUsuarioId(Integer.parseInt(val.toString()));

			lista.add(pt);
		}

		return lista;
	}

	
	//Métodos Públicos - CRUD
	public void crearPuntuacion(Puntuacion pt) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Puntuacion");
		double initDbl = -1.0;
		Integer initInt = -1;

		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("puntuacion", pt.getPuntuacion() != null ? pt.getPuntuacion() : initDbl);
			entidad.setProperty("eventoId", pt.getEventoId() != null ? pt.getEventoId() : initInt);
			entidad.setProperty("usuarioId", pt.getUsuarioId() != null ? pt.getUsuarioId() : initInt);

			conexion = datastore.beginTransaction();

			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> crearPuntuacion");
		}finally {
			conexion.commit();
		}
	}
	
	public void editarPuntuacion(Puntuacion pt) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Puntuacion");
		List<Entity> listaEntidades = new ArrayList<>();

		conexion = datastore.beginTransaction();

		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, pt.getId());
		q.setFilter(filtro);

		try {
			listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			entidad = listaEntidades.get(0);

			entidad.setProperty("puntuacion", pt.getPuntuacion());
			entidad.setProperty("eventoId", pt.getEventoId());
			entidad.setProperty("usuarioId", pt.getUsuarioId());

			datastore.put(conexion, entidad);

		} catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> editarPuntuacion");
		}finally {
			conexion.commit();
		}
	}

	
	//Métodos Públicos - Find
	public List<Puntuacion> encontrarTodasLasPuntuaciones() {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> encontrarTodasLasPuntuaciones");
		}finally {
			conexion.commit();
		}
		
		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeUsuario(Integer idUser) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idUser);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> encontrarPuntuacionesDeUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeEvento(Integer idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idEvento);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> encontrarPuntuacionesDeUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeEventoYUsuario(Integer idUser, Integer idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();

		conexion = datastore.beginTransaction();

		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idEvento);
		FilterPredicate filtro2 = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idUser);

		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);

		q.setFilter(filtro3);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(null);
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en PuntuacionFacade -> encontrarPuntuacionesDeUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}


}
