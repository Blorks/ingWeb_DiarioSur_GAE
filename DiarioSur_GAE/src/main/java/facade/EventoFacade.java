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

import entity.Evento;

public class EventoFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	private Transaction conexion;
	
	public EventoFacade(){}
	
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
	
	private List<Evento> crearEntidades(List<Entity> listaEntidades) {
		List<Evento> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Evento ev = new Evento();
			
			Object val = e.getProperty("ID");
			ev.setId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("titulo");
			ev.setTitulo(val.toString());
			
			val = e.getProperty("subtitulo");
			ev.setSubtitulo(val.toString());
			
			val = e.getProperty("descripcion");
			ev.setDescripcion(val.toString());
			
			val = e.getProperty("direccionFisica");
			ev.setDireccionfisica(val.toString());
			
			val = e.getProperty("precio");
			ev.setPrecio(Double.parseDouble(val.toString()));
			
			val = e.getProperty("latitud");
			ev.setLatitud(Double.parseDouble(val.toString()));
			
			val = e.getProperty("longitud");
			ev.setLongitud(Double.parseDouble(val.toString()));
			
			val = e.getProperty("estaRevisado");
			ev.setEstarevisado(Integer.parseInt(val.toString()));
			
			val = e.getProperty("dateevID");
			ev.setDateevId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("usuarioID");
			ev.setUsuarioId(Integer.parseInt(val.toString()));

			lista.add(ev);
		}
		
		return lista;
	}

	
	//Métodos Públicos - CRUD
	public void crearEvento(Evento ev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Evento");
		List<Integer> listaNumero = new ArrayList<>();
		listaNumero.add(-1);
		double initDbl = -1.0;
		Integer initInt = -1;
		String initStr = "vacio";
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("titulo", ev.getTitulo() != null ? ev.getTitulo() : initStr);
			entidad.setProperty("subtitulo", ev.getSubtitulo() != null ? ev.getSubtitulo() : initStr);
			entidad.setProperty("descripcion", ev.getDescripcion() != null ? ev.getDescripcion() : initStr);
			entidad.setProperty("direccionFisica", ev.getDireccionfisica() != null ? ev.getDireccionfisica() : initStr);
			entidad.setProperty("precio", ev.getPrecio() != null ? ev.getPrecio() : initDbl);
			entidad.setProperty("latitud", ev.getLatitud() != null ? ev.getLatitud() : initDbl);
			entidad.setProperty("longitud", ev.getLongitud() != null ? ev.getLongitud() : initDbl);
			entidad.setProperty("estaRevisado", ev.getEstarevisado() != null ? ev.getEstarevisado() : initInt);
			entidad.setProperty("dateevID", ev.getDateevId() != null ? ev.getDateevId() : initInt);
			entidad.setProperty("usuarioID", ev.getUsuarioId() != null ? ev.getUsuarioId() : initInt);
			
			conexion = datastore.beginTransaction();
			
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> crearEvento");
		}finally {
			conexion.commit();
		}	
	}
	
	public void editarEvento(Evento ev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Evento");
		List<Entity> listaEntidades = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, ev.getId());
		q.setFilter(filtro);

		try {
			 listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			 entidad = listaEntidades.get(0);
			 
			entidad.setProperty("titulo", ev.getTitulo());
			entidad.setProperty("subtitulo", ev.getSubtitulo());
			entidad.setProperty("descripcion", ev.getDescripcion());
			entidad.setProperty("direccionFisica", ev.getDireccionfisica());
			entidad.setProperty("precio", ev.getPrecio());
			entidad.setProperty("latitud", ev.getLatitud());
			entidad.setProperty("longitud", ev.getLongitud());
			entidad.setProperty("estaRevisado", ev.getEstarevisado());
			entidad.setProperty("dateevID", ev.getDateevId());
			entidad.setProperty("usuarioID", ev.getUsuarioId());
				
			 datastore.put(conexion, entidad);

		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> editarEvento");
		}finally {
			conexion.commit();
		}
	}
	
	public void eliminarEventoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			datastore.delete(conexion, key);
			
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> eliminarEventoPorID");
		}finally {
			conexion.commit();
		}
	}
	
	
	//Métodos Públicos - Find
	public List<Evento> encontrarEventoPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventoPorID");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> encontrarEventosRevisados(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		List<Evento> eventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		
		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getEstarevisado() == 1) {
					eventos.add(lista.get(i));
				}
			}
			
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventosRevisados");
		}finally {
			conexion.commit();
		}

		return eventos;
	}
	
	public List<Evento> encontrarEventoPorUsuario(Integer idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioId", FilterOperator.EQUAL, idUsuario);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventoPorUsuario");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> encontrarEventoPorPrecioMaximo(Double precioMax){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		List<Evento> eventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getPrecio() <= precioMax) {
					eventos.add(lista.get(i));
				}
			}
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventoPorPrecioMaximo");
		}finally {
			conexion.commit();
		}

		return eventos;
	}
	
	public List<Evento> encontrarEventosNoRevisados(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		List<Evento> eventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getEstarevisado() == 0) {
					eventos.add(lista.get(i));
				}
			}
				
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventosNoRevisados");
		}finally {
			conexion.commit();
		}

		return eventos;
	}
	
	public List<Evento> encontrarEventosPorFecha(Integer idFecha){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		List<Evento> eventos = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
			
			for(int i=0; i<lista.size(); i++) {
				if(lista.get(i).getDateevId() == idFecha) {
					eventos.add(lista.get(i));
				}
			}
			
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarEventoPorFecha");
		}finally {
			conexion.commit();
		}

		return eventos;
	}

	public List<Evento> encontrarTodosLosEventos(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.DESCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> encontrarTodosLosEventos");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	
	//Métodos Públicos - Sort
	public List<Evento> ordenarEventosAlfabeticamente(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("titulo", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosAlfabeticamente");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> ordenarEventosAlfabeticamenteDESC(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("titulo", Query.SortDirection.DESCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosAlfabeticamenteDESC");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> ordenarEventosPorPrecio(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("precio", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosPorPrecio");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> ordenarEventosPorPrecioDESC(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("precio", Query.SortDirection.DESCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosPorPrecioDESC");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	
	public List<Evento> ordenarEventosPorFecha(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.DESCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosPorFecha");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
	public List<Evento> ordenarEventosPorFechaDESC(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en EventoFacade -> ordenarEventosPorFechaDESC");
		}finally {
			conexion.commit();
		}

		return lista;
	}
	
}
