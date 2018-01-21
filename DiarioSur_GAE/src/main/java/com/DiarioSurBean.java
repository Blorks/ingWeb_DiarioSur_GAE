package com;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;
import com.google.apphosting.api.ApiProxy.LogRecord.Level;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import facade.*;
import entity.*;

@ManagedBean
@SessionScoped
public class DiarioSurBean implements Serializable {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 1L;

	TagusuarioFacade tuf = new TagusuarioFacade();
	TageventoFacade tef = new TageventoFacade();

	private Usuario usuario = new Usuario();
	private double usuarioLatitud = 0.0;
	private double usuarioLongitud = 0.0;

	private Evento evento = new Evento();
	private int edit = 0;

	private Dateev fecha = new Dateev();
	private String listaDias = "";
	private String[] arFecha;

	private String tagsEvento = "";
	private String tagsUsuario = "";

	private String diaBusqueda;
	private int distMaxima;
	private Double precioMax;

	private String usuarioFoto;

	private String puntuacion_evId = "";
	private String puntuacion = "";

	private List<Evento> eventosConFiltros = new ArrayList<>();

	// @PostConstruct
	// public void init() {
	// DatastoreService datastore;
	// Entity entidad;
	// Transaction conexion;
	// datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized
	// // Datastore
	// // service
	//
	// for (Integer ultimoID = 1; ultimoID < 10; ultimoID++) {
	// entidad = new Entity("Evento");
	// entidad.setProperty("ID", ultimoID);
	// entidad.setProperty("titulo", "tit" + ultimoID);
	// entidad.setProperty("subtitulo", "subTit" + ultimoID);
	// entidad.setProperty("descripcion", "des" + ultimoID);
	// entidad.setProperty("direccionFisica", "dirf" + ultimoID);
	// entidad.setProperty("precio", 123);
	// entidad.setProperty("latitud", (36.71589398816918 + (ultimoID+0.2)));
	// entidad.setProperty("longitud", (-4.477620013640262 + (ultimoID+0.2)));
	// entidad.setProperty("estaRevisado", 1);
	// entidad.setProperty("tagEventoList", "");
	// entidad.setProperty("puntuacionList", 5);
	// entidad.setProperty("dateevID", 1);
	// entidad.setProperty("usuarioID", 1);
	//
	// conexion = datastore.beginTransaction();
	//
	// datastore.put(conexion, entidad);
	// conexion.commit();
	// }
	//
	// }

	/*
	 * Go to
	 */
	public String volver() {
		return "index";
	}

	public String irEditarEvento(Evento e) {
		evento = e;
		edit = 1;
		mostrarTagsDeEvento();
		return "subirevento.xhtml";
	}

	public String irPerfil() {
		mostrarTagsDeUsuario();

		return "perfil.xhtml";
	}

	public String irMisEvento() {
		return "perfil.xhtml";
	}

	public String verEvento(Evento e) {
		evento = e;
		return "evento";
	}

	public String irTodosLosEventos() {
		return "todosloseventos.xhtml";
	}

	public String irValidarEvento() {
		return "validarEvento.xhtml";
	}

	public String irCrearEvento() {
		return "subirevento.xhtml";
	}

	public String irEventosFiltradosDireccion() {
		return "eventosFiltradosDireccion";
	}
	/*
	 * Construct
	 */

	public DiarioSurBean() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Set & Get
	 */
	public String getUsuarioFoto() {
		return usuarioFoto;
	}

	public void setUsuarioFoto(String usuarioFoto) {
		this.usuarioFoto = usuarioFoto;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Double getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(Double precioMax) {
		this.precioMax = precioMax;
	}

	public int getDistMaxima() {
		return distMaxima;
	}

	public void setDistMaxima(int distMaxima) {
		this.distMaxima = distMaxima;
	}

	public String getDiaBusqueda() {
		return diaBusqueda;
	}

	public void setDiaBusqueda(String diaBusqueda) {
		this.diaBusqueda = diaBusqueda;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Dateev getFecha() {
		return fecha;
	}

	public void setFecha(Dateev fecha) {
		this.fecha = fecha;
	}

	public String getListaDias() {
		return listaDias;
	}

	public void setListaDias(String listaDias) {
		this.listaDias = listaDias;
	}

	public String getTagsEvento() {
		return tagsEvento;
	}

	public void setTagsEvento(String tagsEvento) {
		this.tagsEvento = tagsEvento;
	}

	public String getTagsUsuario() {
		return tagsUsuario;
	}

	public void setTagsUsuario(String tagsUsuario) {
		this.tagsUsuario = tagsUsuario;
	}

	public double getUsuarioLatitud() {
		return usuarioLatitud;
	}

	public void setUsuarioLatitud(double usuarioLatitud) {
		this.usuarioLatitud = usuarioLatitud;
	}

	public double getUsuarioLongitud() {
		return usuarioLongitud;
	}

	public void setUsuarioLongitud(double usuarioLongitud) {
		this.usuarioLongitud = usuarioLongitud;
	}

	public List<Evento> getEventosConFiltros() {
		return eventosConFiltros;
	}

	public void setEventosConFiltros(List<Evento> eventosConFiltros) {
		this.eventosConFiltros = eventosConFiltros;
	}

	// probablemente falten get&set escondidos por el managebean..
	/*
	 * Methods
	 */
	public List<Evento> mostrarTodosLosEventos() {
		EventoFacade ef = new EventoFacade();
		List<Evento> eventos = ef.encontrarTodosLosEventos();
		return eventos;
	}

	/*
	 * Evento
	 */

	public List<Evento> mostrarTodosLosEventosRevisados() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.encontrarEventosRevisados();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> filtrarEventosDeUsuario() {
		EventoFacade ef = new EventoFacade();
		return ef.encontrarEventoPorUsuario(usuario.getId());
	}

	public List<Evento> mostrarEventosFiltradosPorPrecio() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.encontrarEventoPorPrecioMaximo(precioMax);
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarTodosLosEventosNoRevisados() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.encontrarEventosNoRevisados();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarEventosFiltradosPorFecha() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.encontrarEventosPorFecha(fecha.getId());
		eventosConFiltros = le;
		return le;
	}

	public String nuevoEvento() {
		if (edit == 0) {
			EventoFacade eventoFacade = new EventoFacade();
			DateevFacade dateevFacade = new DateevFacade();
			UsuarioFacade usuarioFacade = new UsuarioFacade();
			NotificacionFacade notificacionFacade = new NotificacionFacade();

			// Adjunto el usuario creador

			List<Usuario> listaUsuario = usuarioFacade.encontrarUsuarioPorEmail(usuario.getEmail());
			evento.setUsuarioId(listaUsuario.get(0).getId());

			// Adjunto la fecha del evento
			adjuntarFecha();

			// Adjunto si est� revisado o no
			if (esPeriodista()) {
				evento.setEstarevisado(1);
			} else {
				evento.setEstarevisado(0);
			}

			eventoFacade.crearEvento(evento);
			// evento.setId(eventoFacade.ultimoIdInsertado());

			// Adjunto tags al evento
			adjuntarTagsEvento();

			// fecha.setEventoId(eventoFacade.ultimoIdInsertado());
			// clienteFecha.edit_XML(fecha, fecha.getId().toString());

			// Creo notificacion para usuario
			if (esPeriodista()) {
				// crearNotificacion("Has creado el evento con exito!",
				// usuario);
			} else {
				// crearNotificacion("Tu evento est� a la espera de ser
				// validado", usuario);
			}

			// reset variables
			evento = null;
			evento = new Evento();
			tagsEvento = "";

			return "index";
		} else {
			// editarEvento();
			edit = 0;

			return "todoloseventos.xhtml";
		}
	}

	/*
	 * DateevFacade
	 */

	// public List<Dateev> encontrarFechaPorID(String id) {
	// DateevFacade def = new DateevFacade();
	// return def.encontrarFechaPorID(id);
	// }

	public List<Dateev> mostrarTodasLasFechasUnicas() {
		DateevFacade def = new DateevFacade();
		return def.encontrarFechaPorUnica();
	}

	public List<Dateev> mostrarTodasLasFechasRango() {
		DateevFacade def = new DateevFacade();
		return def.encontrarFechaPorRango();
	}

	// private int actualizarIDFecha() {
	// DateevFacade dateevFacade = new DateevFacade();
	//
	// return dateevFacade.ultimoIdInsertado();
	// }

	private void crearFechaUnica() {
		boolean encontrado = false;
		Date fechaTemp;
		Date fechaTemp2 = fecha.getDia();
		int test; // comentario

		DateevFacade dateevFacade = new DateevFacade();

		List<Dateev> lista = dateevFacade.encontrarTodasLasFechas();

		for (int i = 0; i < lista.size(); i++) {
			fechaTemp = lista.get(i).getDia();

			if (fechaTemp != null) {
				test = fechaTemp.compareTo(fechaTemp2);

				if (test == 0) {
					encontrado = true;
					fecha.setId(lista.get(i).getId());
				}
			}

		}

		if (encontrado == false) {
			dateevFacade.crearFecha(fecha);
			// fecha.setId(actualizarIDFecha());
		}
	}

	private void crearFechaRango() {
		boolean encontrado = false;
		Date fechaTemp;
		Date fechaTemp2 = fecha.getDesde();
		Date fechaTemp21 = fecha.getHasta();
		int test, test2;

		DateevFacade dateevFacade = new DateevFacade();

		List<Dateev> lista = dateevFacade.encontrarTodasLasFechas();

		for (int i = 0; i < lista.size(); i++) {
			fechaTemp = lista.get(i).getDesde();

			if (fechaTemp != null) {
				test = fechaTemp.compareTo(fechaTemp2);

				fechaTemp = lista.get(i).getHasta();
				test2 = fechaTemp.compareTo(fechaTemp21);

				if (test == 0 && test2 == 0) {
					encontrado = true;
					fecha.setId(lista.get(i).getId());
				}
			}
		}

		if (encontrado == false) {
			dateevFacade.crearFecha(fecha);
			// fecha.setId(actualizarIDFecha());
		}

	}

	private void crearFechaListaDias() {
		boolean encontrado = false;
		String fechas;

		DateevFacade dateevFacade = new DateevFacade();

		List<Dateev> lista = dateevFacade.encontrarTodasLasFechas();

		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getListadias() != null) {
				fechas = lista.get(i).getListadias();

				encontrado = fechas.equals(fecha.getListadias());
				if (encontrado == true) {
					fecha.setId(lista.get(i).getId());
				}
			}

		}

		if (encontrado == false) {
			dateevFacade.crearFecha(fecha);
			// fecha.setId(actualizarIDFecha());
		}

	}

	public void adjuntarFecha() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		if (fecha.getEsunico() == 1) {

			try {
				fecha.setDia(formato.parse(listaDias));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			fecha.setDesde(null);
			fecha.setHasta(null);
			fecha.setListadias(null);

			crearFechaUnica();

		} else if (fecha.getTodoslosdias() == 1) {
			arFecha = listaDias.trim().split(",");

			try {
				fecha.setDesde(formato.parse(arFecha[0]));
				fecha.setHasta(formato.parse(arFecha[1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			fecha.setDia(null);
			fecha.setListadias(null);

			crearFechaRango();

		} else {
			fecha.setDia(null);
			fecha.setDesde(null);
			fecha.setHasta(null);
			fecha.setListadias(listaDias);

			crearFechaListaDias();
		}

		// evento.setDateevId(actualizarIDFecha());
	}

	/*
	 * UsuarioFacade
	 */
	public void rrssLogin() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map params = externalContext.getRequestParameterMap();

		if (params.size() > 4) {
			usuarioFoto = params.get("picture").toString();

			if (!logIn()) {
				usuario.setNombre(params.get("first_name").toString());
				usuario.setApellidos(params.get("last_name").toString());
				usuario.setEmail(params.get("email").toString());
				usuario.setRol("Usuario");
				nuevoUsuario(usuario);
			}
		}
	}

	// METODOS REFERENTES A LOS FileEv
	public void adjuntarFotoDePerfil(String url) {
		UsuarioFacade cliente2 = new UsuarioFacade();

		Fileev file = new Fileev();
		file.setUrl(url);
		file.setUsuarioId(usuario.getId());

		usuario.setFileev(file.getId()); // Al no existir el archivo file, la BD
											// lo crea automaticamente

		cliente2.editarUsuario(usuario);
	}

	public void nuevoUsuario(Usuario us) {
		UsuarioFacade uf = new UsuarioFacade();
		List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());
		if (usuarios.isEmpty()) {
			uf.crearUsuario(us);

			logIn();
			if (!usuarioFoto.isEmpty()) {
				adjuntarFotoDePerfil(usuarioFoto);
			}
		}
	}

	public boolean logIn() {
		UsuarioFacade uf = new UsuarioFacade();
		FileevFacade ff = new FileevFacade();
		List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());

		if (!usuarios.isEmpty()) {
			usuario = usuarios.get(0);
			if (usuario.getFileev() != -1) {
				List<Fileev> lista = ff.encontrarArchivoPorID(usuario.getFileev());
				usuarioFoto = lista.get(0).getUrl();
			}
			return true;
		} else {
			usuario.setEmail("");
			return false;
		}
	}

	public boolean isLogin() {
		return (usuario.getEmail() == null || usuario.getEmail().equals(""));
	}

	public String isLoginIncl() {
		return isLogin() ? "login.xhtml" : "logout.xhtml";
	}

	public String logout() {
		usuario = new Usuario();
		usuario.setEmail("");
		return "index";
	}

	// public String borrarEvento(Evento ev) {
	// clienteEventos cliente = new clienteEventos();
	// clienteDateev clienteFecha = new clienteDateev();
	// clienteTag cliente3 = new clienteTag();
	// List<Tag> listaTags = encontrarTagsDeEvento();
	//
	// //Elimino tags de evento
	// Response r;
	// List<Tag> listaTemp;
	// GenericType<List<Tag>> genericType;
	// for(int i=0; i<listaTags.size(); i++){
	// r = cliente3.encontrarTagPorNombre_XML(Response.class,
	// listaTags.get(i).getNombre());
	// if(r.getStatus() == 200){
	// genericType = new GenericType<List<Tag>>(){};
	// listaTemp = r.readEntity(genericType);
	//
	// if(!listaTemp.isEmpty()){
	// eliminarTagEvento(listaTemp.get(0));
	// }
	// }
	// }
	//
	// //Elimino Evento
	// r = clienteFecha.encontrarFechaPorID_XML(Response.class,
	// ev.getDateevId().getId().toString());
	// if (r.getStatus() == 200) {
	// GenericType<List<Dateev>> genericType2 = new GenericType<List<Dateev>>()
	// {
	// };
	// List<Dateev> listaFecha = r.readEntity(genericType2);
	//
	// cliente.remove(ev.getId().toString());
	//
	// //Elimino Fecha de Evento
	// r = cliente.encontrarEventosPorFecha_XML(Response.class,
	// listaFecha.get(0).getId().toString());
	// if (r.getStatus() == 200) {
	// GenericType<List<Evento>> genericType3 = new GenericType<List<Evento>>()
	// {
	// };
	// List<Evento> listaEvento = r.readEntity(genericType3);
	//
	// if (listaEvento.isEmpty()) {
	// clienteFecha.remove(ev.getDateevId().getId().toString());
	// }
	// }
	// }
	//
	// crearNotificacion("Has eliminado el evento con exito!", usuario);
	//
	// return "todoloseventos.xhtml";
	// }
	public List<Evento> mostrarEventosOrdenadosAlfabeticamente() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.ordenarEventosAlfabeticamente();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarEventosOrdenadosAlfabeticamenteDESC() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.ordenarEventosAlfabeticamenteDESC();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarEventosOrdenadosPorPrecio() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.ordenarEventosPorPrecio();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarEventosOrdenadosPorPrecioDESC() {
		List<Evento> le = new ArrayList<>();
		EventoFacade ef = new EventoFacade();
		le = ef.ordenarEventosPorPrecioDESC();
		eventosConFiltros = le;
		return le;
	}

	public List<Evento> mostrarEventosFiltradosPorDistancia() {
		EventoFacade ef = new EventoFacade();

		List<Evento> listaTemp = new ArrayList<>();
		List<Evento> eventos = ef.encontrarTodosLosEventos();

		for (int i = 0; i < eventos.size(); i++) {
			double distancia = calcularDistanciaHastaEvento(eventos.get(i).getLatitud(), eventos.get(i).getLongitud(),
					usuarioLatitud, usuarioLongitud);
			if (distancia <= distMaxima) {
				listaTemp.add(eventos.get(i));
			}
		}
		return listaTemp;
	}
	
	private double calcularDistanciaHastaEvento(double latitudEvento, double longitudEvento, double latitudUsuario, double longitudUsuario) {
        double radioTierra = 6371.137;
        double dLat = Math.toRadians(latitudUsuario - latitudEvento) / 2;
        double dLng = Math.toRadians(longitudUsuario - longitudEvento) / 2;

        double sindLat = Math.sin(dLat);
        double sindLng = Math.sin(dLng);

        double val = Math.pow(sindLat, 2) + Math.cos(Math.toRadians(latitudEvento)) * Math.pow(sindLng, 2) * Math.cos(Math.toRadians(latitudUsuario));
        val = Math.sqrt(val);
        double val2 = 2 * radioTierra * Math.asin(val);

        return val2;
    }

	// public void actualizarPuntuacion(String punto, String evId) {
	// PuntuacionFacade pf = new PuntuacionFacade();
	// Evento ev = ef.encontrarEventoPorID(evId).get(0);
	//
	// List<Puntuacion> puntuaciones =
	// pf.encontrarPuntuacionesDeEventoYUsuario(usuario.getId().toString(),
	// evId);
	//
	// Puntuacion pt = new Puntuacion();
	// if (puntuaciones.isEmpty()) {
	// pt.setPuntuacion(Double.parseDouble(punto));
	// pt.setEventoId(ev.getId());
	// pt.setUsuarioId(usuario.getId());
	// pf.crearPuntuacion(pt);
	// } else {
	// pt = puntuaciones.get(0);
	// pt.setPuntuacion(Double.parseDouble(punto));
	// // pf.
	// }
	// }
	public double mostrarPuntuacionMedia(Evento ev) {
		// PuntuacionFacade pf = new PuntuacionFacade();
		// List<Puntuacion> puntuaciones =
		// pf.encontrarPuntuacionesDeEvento(ev.getId().toString());
		// double puntuacionTotal = 0;
		//
		// for (int i = 0; i < puntuaciones.size(); i++) {
		// puntuacionTotal += puntuaciones.get(i).getPuntuacion();
		// }

		return 5;// puntuacionTotal / puntuaciones.size();

	}

	/*
	 * TagFacade
	 */

	private Tag crearTag(String strTag) {
		TagFacade tagFacade = new TagFacade();
		Tag tag = new Tag();
		String tagSinEspacio = strTag.trim();

		List<Tag> lista = tagFacade.encontrarTagPorNombre(tagSinEspacio);

		if (lista.isEmpty()) {
			tag.setNombre(tagSinEspacio);
			tagFacade.crearTag(tag);

			lista = tagFacade.encontrarTagPorNombre(tagSinEspacio);
			tag = lista.get(0);
		} else {
			tag = lista.get(0);
		}
		return tag;
	}

	public void mostrarTagsDeUsuario() {
		List<Tag> listaTags = encontrarTagsDeUsuario();
		String lista = "";

		for (int i = 0; i < listaTags.size(); i++) {
			lista = lista + listaTags.get(i).getNombre() + ", ";
		}

		tagsUsuario = lista;
	}

	public List<Tag> encontrarTagPorNombre(String nombre) { // mejor private?
		TagFacade tf = new TagFacade();
		return tf.encontrarTagPorNombre(nombre);
	}

	public List<Tag> encontrarTagsDeUsuario() {
		TagusuarioFacade tuf = new TagusuarioFacade();
		TagFacade tf = new TagFacade();
		List<Tag> tagsUsuarioTemp = new ArrayList<>();

		Tagusuario tu = new Tagusuario();
		tu.setId(1);

		// List<Tagusuario> lista =
		// tuf.encontrarTagusuarioPorUsuario(usuario.getId().toString());
		List<Tagusuario> lista = new ArrayList<>();
		lista.add(tu);

		for (int i = 0; i < lista.size(); i++) {
			////////////////////////////////////////////////////////////////////////////////////////////// Otro
			////////////////////////////////////////////////////////////////////////////////////////////// problema
			////////////////////////////////////////////////////////////////////////////////////////////// por
			////////////////////////////////////////////////////////////////////////////////////////////// lo
			////////////////////////////////////////////////////////////////////////////////////////////// mismo
			////////////////////////////////////////////////////////////////////////////////////////////// no
			////////////////////////////////////////////////////////////////////////////////////////////// es
			////////////////////////////////////////////////////////////////////////////////////////////// relacional
			////////////////////////////////////////////////////////////////////////////////////////////// y
			////////////////////////////////////////////////////////////////////////////////////////////// estamos
			////////////////////////////////////////////////////////////////////////////////////////////// cogiendo
			////////////////////////////////////////////////////////////////////////////////////////////// el
			////////////////////////////////////////////////////////////////////////////////////////////// getNombre
			////////////////////////////////////////////////////////////////////////////////////////////// a
			////////////////////////////////////////////////////////////////////////////////////////////// una
			////////////////////////////////////////////////////////////////////////////////////////////// id!!!
			////////////////////////////////////////////////////////////////////////////////////////////// (
			////////////////////////////////////////////////////////////////////////////////////////////// getTagId())
			Tag g = new Tag();
			g.setId(1);
			g.setNombre("hardcodeado");

			// List<Tag> lista2 =
			// tf.encontrarTagPorNombre(lista.get(i).getTagId().getNombre());
			List<Tag> lista2 = new ArrayList<>();
			lista2.add(g);
			tagsUsuarioTemp.add(lista2.get(0));

		}

		return tagsUsuarioTemp;
	}

	public List<Tag> encontrarTagsDeEvento() {
		TageventoFacade tef = new TageventoFacade();
		TagFacade tf = new TagFacade();

		List<Tag> tagsEventoTemp = new ArrayList<>();

		List<Tagevento> lista = tef.encontrarTageventoPorEvento(evento.getId());

		for (int i = 0; i < lista.size(); i++) {
			////////////////////////////////////////////////////////////////////////////////////////////// Otro
			////////////////////////////////////////////////////////////////////////////////////////////// problema
			////////////////////////////////////////////////////////////////////////////////////////////// por
			////////////////////////////////////////////////////////////////////////////////////////////// lo
			////////////////////////////////////////////////////////////////////////////////////////////// mismo
			////////////////////////////////////////////////////////////////////////////////////////////// no
			////////////////////////////////////////////////////////////////////////////////////////////// es
			////////////////////////////////////////////////////////////////////////////////////////////// relacional
			////////////////////////////////////////////////////////////////////////////////////////////// y
			////////////////////////////////////////////////////////////////////////////////////////////// estamos
			////////////////////////////////////////////////////////////////////////////////////////////// cogiendo
			////////////////////////////////////////////////////////////////////////////////////////////// el
			////////////////////////////////////////////////////////////////////////////////////////////// getNombre
			////////////////////////////////////////////////////////////////////////////////////////////// a
			////////////////////////////////////////////////////////////////////////////////////////////// una
			////////////////////////////////////////////////////////////////////////////////////////////// id!!!
			////////////////////////////////////////////////////////////////////////////////////////////// (
			////////////////////////////////////////////////////////////////////////////////////////////// getTagId())
			// el comentario esta asi de raro porqe el shift + crt + f de
			////////////////////////////////////////////////////////////////////////////////////////////// eclipse
			////////////////////////////////////////////////////////////////////////////////////////////// lo
			////////////////////////////////////////////////////////////////////////////////////////////// pone
			////////////////////////////////////////////////////////////////////////////////////////////// asi..xD
			List<Tag> lista2 = null;// tf.encontrarTagPorNombre(lista.get(i).getTagId().getNombre());
			tagsEventoTemp.add(lista2.get(0));
		}
		return tagsEventoTemp;
	}

	public void mostrarTagsDeEvento() {
		List<Tag> listaTags = encontrarTagsDeEvento();
		String lista = "";

		for (int i = 0; i < listaTags.size(); i++) {
			lista = lista + listaTags.get(i).getNombre() + ", ";
		}

		tagsEvento = lista;
	}

	/*
	 * TagusuarioFacade
	 */

	/*
	 * TageventoFacade
	 */

	public void adjuntarTagsEvento() {
		TageventoFacade tagEventoFacade = new TageventoFacade();
		String[] partes = tagsEvento.trim().toLowerCase().split(",");
		Tag tagCreado;
		Tagevento tagEv = new Tagevento();

		for (int i = 0; i < partes.length; i++) {
			tagCreado = crearTag(partes[i]);

			tagEv.setEventoId(evento.getId());
			tagEv.setTagId(tagCreado.getId());

			tagEventoFacade.crearTagevento(tagEv);
		}
	}

	// public List<Tag> encontrarTagsDeUsuario() {
	// for(Tagusuario tu :
	// tuf.encontrarTagusuarioPorUsuario(usuario.getId().toString()))
	//
	// }

	public boolean esPeriodista() {
		return false;// usuario.getRol().equals("Periodista");
	}

	public String mostrarFechaDeEvento(Evento ev) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		// if (ev.getDateevId().getEsunico() == 1) {
		// return formato.format(ev.getDateevId().getDia());
		// } else if (ev.getDateevId().getTodoslosdias() == 1) {
		// return formato.format(ev.getDateevId().getDesde()) + " - " +
		// formato.format(ev.getDateevId().getHasta());
		// } else {
		// arFecha = ev.getDateevId().getListadias().trim().split(",");

		return "01/01/2001";// arFecha[0] + " y varias fechas m�s.";

	}

	/*
	 * Notificaciones
	 */
	public List<Notificacion> mostrarNotificacionesNoLeidas() {
		NotificacionFacade nf = new NotificacionFacade();
		// return
		// nf.encontrarNotificacionesNoLeidasDeUsuario(usuario.getId().toString());
		Notificacion n = new Notificacion();
		n.setDescripcion("asd");
		n.setId(1);
		n.setLeida(0);
		List<Notificacion> ln = new ArrayList<>();
		ln.add(n);
		return ln;
	}

	/*
	 * Todo CRUD
	 */
	public String marcarNotificacionComoLeida(Notificacion not) {
		NotificacionFacade nf = new NotificacionFacade();
		// clienteNotificacion cliente = new clienteNotificacion();
		// Notificacion notTemp = not;
		// notTemp.setLeida(1);
		//
		// cliente.edit_XML(notTemp, notTemp.getId().toString());
		// nf.editarblablalba(notTemp);

		return "index.xhtml";
	}

	public void eliminarEventoPorID(Integer id) {
		// TERMINAR; FALTA HACER DELETE EN CASCADA
		EventoFacade ef = new EventoFacade();
		ef.eliminarEventoPorID(id);
	}

	/*
	 * public List<Fileev> encontrarArchivoPorURL(String url) { FileevFacade fef
	 * = new FileevFacade(); return fef.encontrarArchivoPorID(url); }
	 * 
	 * public List<Fileev> encontrarArchivoPorID(String id) { FileevFacade fef =
	 * new FileevFacade(); return fef.encontrarArchivoPorID(id); }
	 */

	public List<Notificacion> mostrarNotificacionesDeUsuario() {
		NotificacionFacade nf = new NotificacionFacade();
		return nf.encontrarTodasLasNotificacionesDeUsuario(usuario.getId().toString());
	}

	public List<Notificacion> encontrarNotificacionPorId(String id) {
		NotificacionFacade nf = new NotificacionFacade();
		return nf.encontrarNotificacionPorId(id);
	}

	public List<Puntuacion> encontrarTodasLasPuntuaciones() {
		PuntuacionFacade pf = new PuntuacionFacade();
		return pf.encontrarTodasLasPuntuaciones();
	}

	public List<Puntuacion> encontrarPuntuacionesDeUsuario(String idUser) {
		PuntuacionFacade pf = new PuntuacionFacade();
		return pf.encontrarPuntuacionesDeUsuario(usuario.getId().toString());
	}

	public String mostrarMapaMarcas() {
		String s = "";
		for (Evento e : eventosConFiltros) {
			s += "evento" + e.getId() + ": { divClass: 'evento" + e.getId() + "', titulo: '" + e.getTitulo()
					+ "', latitud: " + e.getLatitud() + ",  longitud: " + e.getLongitud() + "},";
		}
		return s;
	}

	public void editarEvento() {
		EventoFacade ef = new EventoFacade();
		DateevFacade def = new DateevFacade();
		TagFacade tf = new TagFacade();
		List<Tag> listaTags = encontrarTagsDeEvento();

		// Edicion de la Fecha
		Integer idFechaTemp = evento.getDateevId();

		evento.setDateevId(null);
		ef.editarEvento(evento);

		def.eliminarDateevPorID(idFechaTemp);

		adjuntarFecha();

		// Edicion del Evento
		ef.editarEvento(evento);

		// Edicion de Tags
		List<Tag> listaTemp;
		for (int i = 0; i < listaTags.size(); i++) {
			listaTemp = tf.encontrarTagPorNombre(listaTags.get(i).getNombre());
			// if(!listaTemp.isEmpty())
			// eliminarTagEvento(listaTemp.get(0));
		}

		adjuntarTagsEvento();

		// creo notificacion
		// crearNotificacion("Has editado el evento con exito!", usuario);
	}

	public void actualizarPuntuacion(String punto, String evId) {
		PuntuacionFacade pf = new PuntuacionFacade();

		List<Puntuacion> puntuaciones = pf.encontrarPuntuacionesDeEventoYUsuario(usuario.getId().toString(), evId);
		Puntuacion pt = new Puntuacion();

		if (puntuaciones.isEmpty()) {
			pt.setPuntuacion(Double.parseDouble(punto));
			pt.setEventoId(Integer.parseInt(evId));
			pt.setUsuarioId(usuario.getId());
			pf.crearPuntuacion(pt);

		} else {
			pt = puntuaciones.get(0);
			pt.setPuntuacion(Double.parseDouble(punto));
			pf.editarPuntuacion(pt);

		}
	}

	public double mostrarPuntuacionMedia(Evento ev) {
		PuntuacionFacade pf = new PuntuacionFacade();

		List<Puntuacion> puntuaciones = pf.encontrarPuntuacionesDeEvento(ev.getId().toString());
		double puntuacionTotal = 0;

		for (int i = 0; i < puntuaciones.size(); i++) {
			puntuacionTotal = puntuacionTotal + puntuaciones.get(i).getPuntuacion();
		}

		puntuacionTotal = puntuacionTotal / puntuaciones.size();

		return puntuacionTotal;

	}

}