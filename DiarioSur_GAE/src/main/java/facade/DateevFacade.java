package facade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	Transaction conexion;
	
	public DateevFacade(){}
	
	public Integer ultimoIdInsertado(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		conexion = datastore.beginTransaction();
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.DESCENDING);
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
	
	private List<Dateev> crearEntidades(List<Entity> listaEntidades) {
		List<Dateev> lista = new ArrayList<>();
		
		for(Entity e: listaEntidades) {
			Dateev fecha = new Dateev();
			
			Object val = e.getProperty("ID");
			fecha.setId(Integer.parseInt(val.toString()));
			
			//fecha única
			val = e.getProperty("esUnico");
			fecha.setEsunico(Integer.parseInt(val.toString()));
			if(fecha.getEsunico() == 1) {
				val = e.getProperty("dia");
				
				fecha.setDia((Date) val);

			}else {
				fecha.setDia(null);
			}
			
			//fecha desde/hasta
			val = e.getProperty("todoslosdias");
			fecha.setTodoslosdias(Integer.parseInt(val.toString()));
			if(fecha.getTodoslosdias() == 1) {
				val = e.getProperty("desde");
				Object val2 = e.getProperty("hasta");
				
				fecha.setDesde((Date) val);
				fecha.setHasta((Date) val2);
				
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
		
	
	//Métodos Públicos - CRUD
	public void crearFecha(Dateev dateev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Dateev");
		Integer initInt = -1;
		String initStr = "vacio";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date initDate = new Date();
		try {
			initDate = sdf.parse("01/01/2015");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Integer ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		

		try {
			entidad.setProperty("ID", ultimoID);
			entidad.setProperty("esUnico", dateev.getEsunico() != null ? dateev.getEsunico() : initInt);
			entidad.setProperty("dia", dateev.getEsunico() == 1 ? dateev.getDia() : initDate);
			entidad.setProperty("todoslosdias", dateev.getTodoslosdias() != null ? dateev.getTodoslosdias() : initInt);
			entidad.setProperty("desde", dateev.getTodoslosdias() == 1 ? dateev.getDesde() : initDate);
			entidad.setProperty("hasta", dateev.getTodoslosdias() == 1 ? dateev.getHasta() : initDate);
			entidad.setProperty("variosDias", dateev.getVariosdias() != null ? dateev.getVariosdias() : initInt);
			entidad.setProperty("listaDias", dateev.getVariosdias() == 1 ? dateev.getListadias() : initStr);
			entidad.setProperty("eventoID", dateev.getEventoId() != null ? dateev.getEventoId() : initInt);
			
			conexion = datastore.beginTransaction();
			
			datastore.put(conexion, entidad);
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> crearFecha");
		}finally {
			conexion.commit();
		}
	}

	public void editarFecha(Dateev dateev) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Dateev");
		List<Entity> listaEntidades = new ArrayList<>();
		
		Integer initInt = -1;
		String initStr = "vacio";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date initDate = new Date();
		try {
			initDate = sdf.parse("01/01/2015");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, dateev.getId());
		q.setFilter(filtro);

		try {
			 listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			 entidad = listaEntidades.get(0);
			 
			entidad.setProperty("esUnico", dateev.getEsunico() != null ? dateev.getEsunico() : initInt);
			entidad.setProperty("dia", dateev.getEsunico() == 1 ? dateev.getDia() : initDate);
			entidad.setProperty("todoslosdias", dateev.getTodoslosdias() != null ? dateev.getTodoslosdias() : initInt);
			entidad.setProperty("desde", dateev.getTodoslosdias() == 1 ? dateev.getDesde() : initDate);
			entidad.setProperty("hasta", dateev.getTodoslosdias() == 1 ? dateev.getHasta() : initDate);
			entidad.setProperty("variosDias", dateev.getVariosdias() != null ? dateev.getVariosdias() : initInt);
			entidad.setProperty("listaDias", dateev.getVariosdias() == 1 ? dateev.getListadias() : initStr);
			entidad.setProperty("eventoID", dateev.getEventoId() != null ? dateev.getEventoId() : initInt);
				
			 datastore.put(conexion, entidad);

		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> editarFecha");
		}finally {
			conexion.commit();
		}
	}
	
	public void eliminarDateevPorID(Integer id) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			Key key = listaEntidades.get(0).getKey();
			datastore.delete(conexion, key);
			
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> eliminarDateevPorID");
		}finally {
			conexion.commit();
		}
	}

	
	//Métodos Públicos - Find
	public List<Dateev> encontrarFechaPorID(Integer id){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Dateev> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.ASCENDING);
		FilterPredicate filtro = new FilterPredicate("ID", FilterOperator.EQUAL, id);
		q.setFilter(filtro);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> encontrarFechaPorID");
		}finally {
			conexion.commit();
		}

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

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> encontrarFechaPorUnica");
		}finally {
			conexion.commit();
		}

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

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(20));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> encontrarFechaPorRango");
		}finally {
			conexion.commit();
		}

		return lista;
	}

	public List<Dateev> encontrarTodasLasFechas(){
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		List<Dateev> lista = new ArrayList<>();
		
		conexion = datastore.beginTransaction();
		
		Query q = new Query("Dateev").addSort("ID", Query.SortDirection.DESCENDING);

		try {
			List<Entity> listaEntidades = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
			lista = crearEntidades(listaEntidades);
		}catch (Exception e) {
			System.out.println("Error en DateevFacade -> encontrarTodasLasFechas");
		}finally {
			conexion.commit();
		}

		return lista;
	}
}
