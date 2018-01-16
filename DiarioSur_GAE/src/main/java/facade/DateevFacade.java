package facade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import entity.Dateev;

public class DateevFacade implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
	Key key;
	Transaction conexion;
	
	public DateevFacade(){}
	
	private String ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Dateev> crearEntidades(List<Entity> listaEntidades) {
		List<Dateev> lista = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Entity e: listaEntidades) {
			Dateev fecha = new Dateev();
			
			Object val = e.getProperty("ID");
			fecha.setId(Integer.parseInt(val.toString()));
			
			//fecha única
			val = e.getProperty("esUnico");
			fecha.setEsunico(Integer.parseInt(val.toString()));
			if(fecha.getEsunico() == 1) {
				val = e.getProperty("dia");
				
				try {
					fecha.setDia(sdf.parse(val.toString()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else {
				fecha.setDia(null);
			}
			
			//fecha desde/hasta
			val = e.getProperty("todoslosdias");
			fecha.setTodoslosdias(Integer.parseInt(val.toString()));
			if(fecha.getTodoslosdias() == 1) {
				val = e.getProperty("desde");
				Object val2 = e.getProperty("hasta");
				
				try {
					fecha.setDesde(sdf.parse(val.toString()));
					fecha.setHasta(sdf.parse(val2.toString()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}else {
				fecha.setDesde(null);
				fecha.setHasta(null);
			}
			
			//lista de fechas
			val = e.getProperty("variosDias");
			fecha.setVariosdias(Integer.parseInt(val.toString()));
			if(fecha.getVariosdias() == 1) {
				val = e.getProperty("listaDias");
				fecha.setListadias(val.toString());
			}else {
				fecha.setListadias(null);
			}
			
			//evento de la fecha
			val = e.getProperty("eventoID");
			fecha.setEventoId(Integer.parseInt(val.toString()));
			
			lista.add(fecha);
		}
		
		return lista;
	}
		
	
	
	
	
	//Métodos Públicos
	public void crearFecha(Dateev dateev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Dateev");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("esUnico", dateev.getEsunico());
		entidad.setProperty("dia", dateev.getDia());
		entidad.setProperty("todoslosdias", dateev.getTodoslosdias());
		entidad.setProperty("desde", dateev.getDesde());
		entidad.setProperty("hasta", dateev.getHasta());
		entidad.setProperty("variosDias", dateev.getVariosdias());
		entidad.setProperty("listaDias", dateev.getListadias());
		entidad.setProperty("eventoID", dateev.getEventoId());
		
		conexion = datastore.beginTransaction();
		
		datastore.put(conexion, entidad);
		conexion.commit();
	}

	public List<Dateev> encontrarFechaPorID(String id){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Dateev> lista = new ArrayList<>();
		int idTemp = Integer.parseInt(id);
		
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, idTemp);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}

	public List<Dateev> encontrarFechaPorUnica(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Dateev> lista = new ArrayList<>();
		int condicion = 0;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("esUnico", FilterOperator.EQUAL, condicion);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
	
	public List<Dateev> encontrarFechaPorRango(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Dateev> lista = new ArrayList<>();
		int condicion = 0;
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("todoslosdias", FilterOperator.EQUAL, condicion);
		q.setFilter(filtro);

		List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
		lista = crearEntidades(listaEntidades);
		
		return lista;
	}
		
}
