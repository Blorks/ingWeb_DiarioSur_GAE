package com;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	private boolean mostrarMapaEventos = true;

	private String puntuacion_evId = "";
	private String puntuacion = "";

	private List<Evento> eventosConFiltros = new ArrayList<>();

	 @PostConstruct
	 public void init() {
	//	 DatastoreService datastore;
	//	 Entity entidad;
	//	 Transaction conexion;
	//	 datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized
	// // Datastore
	// // service
	// TagFacade tf = new TagFacade();
	//
	// Tag t = new Tag();
	// t.setId(1);
	// t.setNombre("coche");
	// List<Integer> i = new ArrayList<>();
	// tageventoList.add(2);
	// t.setTageventoList(tageventoList);
	// t.setTagusuarioList(tageventoList);
	// tf.crearTag(t);
	//
	// TageventoFacade tef = new TageventoFacade();
	// for (int i = 1; i < 10; i++) {
	//
	// Tagevento te = new Tagevento();
	// te.setId(i);
	// te.setEventoId(i);
	// te.setTagId(1);
	// tef.crearTagevento(te);
	// }
	// System.out.println("asd");
	// EventoFacade ef = new EventoFacade();
	// Evento e = new Evento();
	// e.setDateevId(1);
	// e.setDescripcion("descccccASD");
	// e.setDireccionfisica("asdDir");
	// e.setEstarevisado(1);
	// e.setId(69);
	// e.setLatitud(36.4);
	// e.setLongitud(-4.5);
	// e.setPrecio(69.99);
	// e.setPuntuacionList(i);
	// e.setSubtitulo("subASDASD");
	// e.setTageventoList(i);
	// e.setTitulo("titASASDASD");
	// e.setUsuarioId(2);
	//
	// ef.crearEvento(e);
	//
	// TageventoFacade tef = new TageventoFacade();
	// Tagevento te = new Tagevento();
	// te.setId(69);
	// te.setEventoId(10);
	// te.setTagId(2);
	// tef.crearTagevento(te);
	// tef.crearTagevento(te);
//	 System.out.println("postconstruct");
//	
//	 UsuarioFacade uf = new UsuarioFacade();
//	 Usuario u = new Usuario();
//	 List<Integer> i = new ArrayList<>();
//	 i.add(1);
//	
//	 u.setApellidos("ape");
//	 u.setEmail("asd@gmail.com");
//	 u.setFileev(2);
//	 u.setHashpassword("vacio");
//	 u.setNombre("asd");
//	 u.setRol("Periodista");
//	 uf.crearUsuario(u);
//	 
//	 FileevFacade ff = new FileevFacade();
//	 Fileev f = new Fileev();
//	 f.setId(2);
//	 f.setNombre("nombre");
//	 f.setTipo("asd");
//	 f.setUrl("http://images3.wikia.nocookie.net/__cb20121216194440/dragoncity/es/images/thumb/4/47/Caca_1.png/120px-Caca_1.png");
//	 f.setUsuarioId(1);
//	 ff.crearFileev(f);
	 }

	/*
	 * Go to
	 */
	public String volver() {
		mostrarMapaEventos = true;
		return "index";
	}

	public String irEditarEvento(Evento e) {
		evento = e;
		edit = 1;
		mostrarTagsDeEvento();
		return "subirevento.xhtml";
	}

	public String irPerfil() {
		//agsDeUsuario();

		return "perfil.xhtml";
	}

	public String irMisEvento() {
		return "perfil.xhtml";
	}

	public String verEvento(Evento e) {
		evento = e;
		mostrarMapaEventos = false;
		return "evento";
	}

	public String irTodosLosEventos() {
		return "todoloseventos.xhtml";
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
	
    public String irIntroducirFechaBusqueda() {
        return "introducirFechaBusqueda.xhtml";
    }

    public String irIntroducirDistanciaMaxima() {
        return "introducirDistanciaMaxima.xhtml";
    }

    public String irEventosFiltradosPrecio() {
        return "eventosFiltradosPrecio";
    }

    public String irIntroducirPrecioMaximo() {
        return "introducirPrecioMaximo.xhtml";
    }
    
    public String irElegirSentidoAlfabetico() {
        return "elegirSentidoAlfabetico.xhtml";
    }
	
    public String irElegirSentidoFecha() {
        return "elegirSentidoFecha.xhtml";
    }
    
    public String irElegirSentidoPrecio() {
        return "elegirSentidoPrecio.xhtml";
    }
    
	public String getPuntuacion_evId() {
		return puntuacion_evId;
	}

	public void setPuntuacion_evId(String puntuacion_evId) {
		this.puntuacion_evId = puntuacion_evId;
	}

	public String getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
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
	public boolean isMostrarMapaEventos() {
		return mostrarMapaEventos;
	}

	public void setMostrarMapaEventos(boolean mostrarMapaEventos) {
		this.mostrarMapaEventos = mostrarMapaEventos;
	}

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
			evento.setId(eventoFacade.ultimoIdInsertado());

			// Adjunto tags al evento
			adjuntarTagsEvento();

			fecha.setEventoId(eventoFacade.ultimoIdInsertado());
			dateevFacade.editarFecha(fecha);

			// Creo notificacion para usuario
			if (esPeriodista()) {
				notificacionFacade.crearNotificacion("Has creado el evento con exito!", listaUsuario.get(0).getId());
			} else {
				notificacionFacade.crearNotificacion("Tu evento est� a la espera de ser validado", listaUsuario.get(0).getId());
			}

			// reset variables
			evento = null;
			evento = new Evento();
			tagsEvento = "";

			return "index";
		} else {
			editarEvento();
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
			fecha.setId(dateevFacade.ultimoIdInsertado());
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
			fecha.setId(dateevFacade.ultimoIdInsertado());
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
			fecha.setId(dateevFacade.ultimoIdInsertado());
		}

	}

	public void adjuntarFecha() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		DateevFacade dateevFacade = new DateevFacade();

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

		evento.setDateevId(dateevFacade.ultimoIdInsertado());
	}

	/*
	 * UsuarioFacade
	 */
	public void rrssLogin() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map params = externalContext.getRequestParameterMap();
		if (params.get("first_name") != null) {
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

		usuario.setFileev(-1); // Al no existir el archivo file, la BD
											// lo crea automaticamente

		cliente2.editarUsuario(usuario);
	}

	public void nuevoUsuario(Usuario us) {
		UsuarioFacade uf = new UsuarioFacade();
		List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());
		usuario = usuarios.get(0);
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

	public String borrarEvento(Evento ev) {
		EventoFacade ef = new EventoFacade();
		DateevFacade def = new DateevFacade();
		TagFacade tf = new TagFacade();
		evento = ev;
		List<Tag> listaTags = encontrarTagsDeEvento();

		// Elimino tags de evento
		List<Tag> listaTemp;
		for (int i = 0; i < listaTags.size(); i++) {
			listaTemp = tf.encontrarTagPorNombre(listaTags.get(i).getNombre());
			if (!listaTemp.isEmpty())
				eliminarTagEvento(listaTemp.get(0));
		}

		// Elimino Evento
		List<Dateev> listaFecha = def.encontrarFechaPorID(ev.getDateevId());
		ef.eliminarEventoPorID(ev.getId());
		// Elimino Fecha de Evento
		List<Evento> listaEvento = ef.encontrarEventosPorFecha(listaFecha.get(0).getId());
		if(listaEvento.isEmpty()) {
			def.eliminarDateevPorID(ev.getDateevId());
		}
		
		crearNotificacion("Has eliminado el evento con exito!", usuario);

		return "todoloseventos.xhtml";
	}
	
	public String eliminarTagEvento(Tag tagEvento){
		TageventoFacade tef = new TageventoFacade();
		TagFacade tf = new TagFacade();
		List<Tagevento> lista = new ArrayList<>();

		Tagevento tagEv = tef.encontrarTagEventoPorTagYEvento(tagEvento.getId(), evento.getId()).get(0);
        tef.eliminarTagEventoPorID(tagEv.getId());
        
        lista = tef.encontrarTageventoPorEvento(tagEvento.getId());
		if(lista.isEmpty()){
			List<Tagusuario> lista2 = tuf.encontrarTagusuarioPorUsuario(tagEvento.getId());
			if (lista2.isEmpty()) {
				tf.eliminarTagPorID(tagEvento.getId());
			}
		}

		crearNotificacion("Has eliminado el tag con exito!", usuario);
        
        return "evento";
    }

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

	private double calcularDistanciaHastaEvento(double latitudEvento, double longitudEvento, double latitudUsuario,
			double longitudUsuario) {
		double radioTierra = 6371.137;
		double dLat = Math.toRadians(latitudUsuario - latitudEvento) / 2;
		double dLng = Math.toRadians(longitudUsuario - longitudEvento) / 2;

		double sindLat = Math.sin(dLat);
		double sindLng = Math.sin(dLng);

		double val = Math.pow(sindLat, 2) + Math.cos(Math.toRadians(latitudEvento)) * Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(latitudUsuario));
		val = Math.sqrt(val);
		double val2 = 2 * radioTierra * Math.asin(val);

		return val2;
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

		List<Tagusuario> lista = tuf.encontrarTagusuarioPorUsuario(usuario.getId());	
System.out.println(lista.size() + " lista de user");
		for (int i = 0; i < lista.size(); i++) {

			List<Tag> lista2 = tf.encontrarTagPorID(lista.get(i).getTagId());
			
			if(!lista2.isEmpty()){
				tagsUsuarioTemp.add(lista2.get(0));
			}
		}
System.out.println(tagsUsuarioTemp.size() + " <--------------");
		return tagsUsuarioTemp;
	}

	public List<Tag> encontrarTagsDeEvento() {
		TageventoFacade tef = new TageventoFacade();
		TagFacade tf = new TagFacade();

		List<Tag> tagsEventoTemp = new ArrayList<>();
		List<Tagevento> lista = tef.encontrarTageventoPorEvento(evento.getId());

		for (int i = 0; i < lista.size(); i++) {
			tagsEventoTemp.add(tf.encontrarTagPorID(lista.get(i).getTagId()).get(0));
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
			tagCreado = crearTag(partes[i].trim());

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
		return usuario.getRol() != null && usuario.getRol().equals("Periodista");
	}

	public String mostrarFechaDeEvento(Evento ev) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		DateevFacade dateevFacade = new DateevFacade();
		Dateev fecha = new Dateev();
		
		fecha = dateevFacade.encontrarFechaPorID(ev.getDateevId()).get(0);

		if (fecha.getEsunico() == 1) {
			return formato.format(fecha.getDia());
		} else if (fecha.getTodoslosdias() == 1) {
			return formato.format(fecha.getDesde()) + " - " + formato.format(fecha.getHasta());
		} else {
			arFecha = fecha.getListadias().trim().split(",");
			return arFecha[0] + " y varias fechas mas.";
		}
	}

	/*
	 * Notificaciones
	 */
	public List<Notificacion> mostrarNotificacionesNoLeidas() {
		NotificacionFacade nf = new NotificacionFacade();
		return nf.encontrarNotificacionesNoLeidasDeUsuario(usuario.getId());
	}

	/*
	 * Todo CRUD
	 */
	public String marcarNotificacionComoLeida(Notificacion not) {
		NotificacionFacade nf = new NotificacionFacade();
		Notificacion notTemp = not;
		notTemp.setLeida(1);
		
		nf.editarNotificacion(notTemp);

		return "index.xhtml";
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
		return nf.encontrarTodasLasNotificacionesDeUsuario(usuario.getId());
	}

	public List<Notificacion> encontrarNotificacionPorId(String id) {
		NotificacionFacade nf = new NotificacionFacade();
		return nf.encontrarNotificacionPorId(Integer.parseInt(id));
	}

	public List<Puntuacion> encontrarTodasLasPuntuaciones() {
		PuntuacionFacade pf = new PuntuacionFacade();
		return pf.encontrarTodasLasPuntuaciones();
	}

	public List<Puntuacion> encontrarPuntuacionesDeUsuario(String idUser) {
		PuntuacionFacade pf = new PuntuacionFacade();
		return pf.encontrarPuntuacionesDeUsuario(usuario.getId());
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

		evento.setDateevId(-1);
		ef.editarEvento(evento);

		def.eliminarDateevPorID(idFechaTemp);

		adjuntarFecha();

		// Edicion del Evento
		ef.editarEvento(evento);

		// Edicion de Tags
		List<Tag> listaTemp;
		for (int i = 0; i < listaTags.size(); i++) {
			listaTemp = tf.encontrarTagPorNombre(listaTags.get(i).getNombre());
			if(!listaTemp.isEmpty())
				eliminarTagEvento(listaTemp.get(0));
		}

		adjuntarTagsEvento();

		// creo notificacion
		crearNotificacion("Has editado el evento con exito!", usuario);
	}

	public void actualizarPuntuacion(String punto, String evId) {
		PuntuacionFacade pf = new PuntuacionFacade();

		List<Puntuacion> puntuaciones = pf.encontrarPuntuacionesDeEventoYUsuario(usuario.getId(), Integer.parseInt(evId));
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
		
		List<Puntuacion> puntuaciones = pf.encontrarPuntuacionesDeEvento(ev.getId());
		double puntuacionTotal = 0;
		
		for (int i = 0; i < puntuaciones.size(); i++) {
			puntuacionTotal = puntuacionTotal + puntuaciones.get(i).getPuntuacion();
		}
		
		puntuacionTotal = puntuacionTotal / puntuaciones.size();

		return puntuacionTotal;

	}

	public String validarEvento(Evento ev) {
		EventoFacade ef = new EventoFacade();
		Evento eventoTemporal = ev;

		eventoTemporal.setEstarevisado(1);

		ef.editarEvento(eventoTemporal);

		crearNotificacion("El evento se ha validad con exito!", usuario);

		return "validarEvento.xhtml";
	}

	public void adjuntarTagsUsuario() {
		TagusuarioFacade tuf = new TagusuarioFacade();
		String[] partes = tagsUsuario.trim().toLowerCase().split(",");
		Tag tagCreado;
		Tagusuario tagUs = new Tagusuario();
		for (int i = 0; i < partes.length; i++) {
			tagCreado = crearTag(partes[i]);
			List<Tagusuario> lista = tuf.encontrarTagUsuarioPorTagYUsuario(tagCreado.getId(), usuario.getId());
			if(lista.isEmpty()) {
				 tagUs.setUsuarioId(usuario.getId());
				 tagUs.setTagId(tagCreado.getId());

				 tuf.crearTagusuario(tagUs);
			}
		}
		crearNotificacion("Tus tags se han a�adido con exito!", usuario);

		irPerfil();
	}

	public void eliminarTagDeUsuario(Tag tagUsuario) {
		TagFacade tf = new TagFacade();
		TagusuarioFacade tuf = new TagusuarioFacade();
		TageventoFacade tef = new TageventoFacade();

		List<Tagusuario> lista = tuf.encontrarTagUsuarioPorTagYUsuario(tagUsuario.getId(), usuario.getId());
		tuf.eliminarTagUsuarioPorID(lista.get(0).getId());

		lista = tuf.encontrarTagusuarioPorUsuario(tagUsuario.getId());
		if(lista.isEmpty()){
			List<Tagevento> lista2 = tef.encontrarTageventoPorEvento(tagUsuario.getId());
			if (lista2.isEmpty()) {
				tf.eliminarTagPorID(tagUsuario.getId());
			}
		}

		crearNotificacion("Has eliminado el tag con exito!", usuario);

		irPerfil();
	}

	public void crearNotificacion(String contenido, Usuario user) {
		NotificacionFacade nf = new NotificacionFacade();

		Notificacion not = new Notificacion();
		not.setDescripcion(contenido);
		not.setLeida(0);
		not.setUsuarioId(user.getId());

		nf.crearNotificacion(contenido, user.getId());
	}

	public String mostrarFotoUsuario(Integer idUs) {
		UsuarioFacade uf = new UsuarioFacade();
		FileevFacade ff = new FileevFacade();

		Integer fileevId = uf.encontrarUsuarioPorID(idUs).get(0).getFileev();
		System.out.println(uf.encontrarUsuarioPorID(idUs).get(0).getNombre() + " - " + uf.encontrarUsuarioPorID(idUs).get(0).getFileev());
		return fileevId == -1 ? "/resources/images/user.png" : ff.encontrarArchivoPorID(fileevId).get(0).getUrl();

	}

	public String mostrarAutorEv(Integer idUs) {
		UsuarioFacade uf = new UsuarioFacade();
		Usuario us = uf.encontrarUsuarioPorID(idUs).get(0);

		return us != null ? us.getNombre() + " " + us.getApellidos() : "(autor no encontrado)";
	}



}