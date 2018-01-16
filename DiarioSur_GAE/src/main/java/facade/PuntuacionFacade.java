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

import entity.Puntuacion;

public class PuntuacionFacade implements Serializable{
private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public PuntuacionFacade(){}
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Puntuacion> crearEntidades(List<Entity> listaEntidades) {
		List<Puntuacion> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
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

	
	
	
	
	
	//Métodos Públicos
	public void crearPuntuacion(Puntuacion pt) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Puntuacion");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("puntuacion", pt.getPuntuacion());
		entidad.setProperty("eventoId", pt.getEventoId());
		entidad.setProperty("usuarioId", pt.getUsuarioId());
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}

	public List<Puntuacion> encontrarTodasLasPuntuaciones(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.DESCENDING);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeUsuario(String idUser) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(idUser);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeEvento(String idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(idEvento);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public List<Puntuacion> encontrarPuntuacionesDeEventoYUsuario(String idUser, String idEvento) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Puntuacion> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(idEvento);
		int idTemp2 = Integer.parseInt(idUser);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Puntuacion").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("eventoId", FilterOperator.EQUAL, idTemp);
		FilterPredicate filtro2 = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idTemp2);
		
		Filter filtro3 = CompositeFilterOperator.and(filtro, filtro2);
		
		q.setFilter(filtro3);

		List<Entity> listaEntidades = datastore.prepare(q).asList(null);
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}


}
