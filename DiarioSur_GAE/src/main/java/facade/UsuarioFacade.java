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

import entity.Evento;
import entity.Usuario;

public class UsuarioFacade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DatastoreService datastore;
	private Entity entidad;
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
			
			
			//No se como recoger los list
//			val = e.getProperty("calendarioList");
//			user.setCalendarioList(-.-);
//			
//			val = e.getProperty("tagUsuarioList");
//			user.setTagusuarioList(-.-);
//			
//			val = e.getProperty("notificacionList");
//			user.setNotificacionList(-.-);
//			
//			val = e.getProperty("eventoList");
//			user.setEventoList(-.-);
			
			
			lista.add(user);
		}
		
		return lista;
	}
	
	public void crearUsuario(Usuario user) {
		datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
		entidad = new Entity("Usuario");
		key = entidad.getKey();
		
		String ultimoID = ultimoIdInsertado();
		ultimoID = incrementarID(ultimoID);
		
		entidad.setProperty("ID", ultimoID);
		entidad.setProperty("nombre", user.getNombre());
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
	
	
	
}
