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
	Key key;
	Transaction conexion;
	
	public EventoFacade(){}
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.DESCENDING);
		String id;
		
		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
				
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
			
			
			//No se como recoger los list
//			val = e.getProperty("calendarioList");
//			ev.setCalendarioList(-.-);
//			
//			val = e.getProperty("tagEventoList");
//			ev.setTageventoList(-.-);
//			
//			val = e.getProperty("archivosList");
//			ev.setArchivosList(-.-);
			
			val = e.getProperty("dateevID");
			ev.setDateevId(Integer.parseInt(val.toString()));
			
			val = e.getProperty("usuarioID");
			ev.setUsuarioId(Integer.parseInt(val.toString()));
			
			
			lista.add(ev);
		}
		
		return lista;
	}

	
	
	//Métodos Públicos
	public void crearEvento(Evento ev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Evento");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("titulo", ev.getTitulo());
		entidad.setProperty("subtitulo", ev.getSubtitulo());
		entidad.setProperty("descripcion", ev.getDescripcion());
		entidad.setProperty("direccionFisica", ev.getDireccionfisica());
		entidad.setProperty("precio", ev.getPrecio());
		entidad.setProperty("latitud", ev.getLatitud());
		entidad.setProperty("longitud", ev.getLongitud());
		entidad.setProperty("estaRevisado", ev.getEstarevisado());
		entidad.setProperty("calendarioList", ev.getCalendarioList());
		entidad.setProperty("tagEventoList", ev.getTageventoList());
		entidad.setProperty("archivosList", ev.getArchivosList());
		entidad.setProperty("dateevID", ev.getDateevId());
		entidad.setProperty("usuarioID", ev.getUsuarioId());
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}
	
	public List<Evento> encontrarEventoPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> encontrarEventosRevisados(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		int condicion = 1;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("estaRevisado", FilterOperator.EQUAL, condicion);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> encontrarEventoPorUsuario(String idUsuario){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		int userID = Integer.parseInt(idUsuario);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("usuarioID", FilterOperator.EQUAL, userID);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> encontrarEventoPorPrecioMaximo(String precioMax){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		double precio = Double.parseDouble(precioMax);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("precio", FilterOperator.LESS_THAN_OR_EQUAL, precio);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> encontrarEventosNoRevisados(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		int condicion = 0;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("estaRevisado", FilterOperator.EQUAL, condicion);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> encontrarEventosPorFecha(String idFecha){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		int fechaID = Integer.parseInt(idFecha);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("DateevID", FilterOperator.EQUAL, fechaID);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public void eliminarEventoPorID(String id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		int idTemp = Integer.parseInt(id);
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		
		Key key = listaEntidades.get(0).getKey();
		datastore.delete(conexion, key);
		conexion.commit();
	}
	
	public List<Evento> ordenarEventosAlfabeticamente(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("titulo", Query.SortDirection.ASCENDING);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> ordenarEventosAlfabeticamenteDESC(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("titulo", Query.SortDirection.DESCENDING);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> ordenarEventosPorPrecio(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("precio", Query.SortDirection.ASCENDING);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Evento> ordenarEventosPorPrecioDESC(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Evento> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Evento").addSort("precio", Query.SortDirection.DESCENDING);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
		
}
