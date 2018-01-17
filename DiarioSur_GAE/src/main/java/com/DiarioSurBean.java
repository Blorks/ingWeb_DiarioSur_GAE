
package com;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	EventoFacade ef = new EventoFacade();
	PuntuacionFacade pf = new PuntuacionFacade();
	DateevFacade def = new DateevFacade();
	TagFacade tf = new TagFacade();
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
	private String precioMax;

	private String usuarioFoto;

	private String puntuacion_evId = "";
	private String puntuacion = "";

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

	public String getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(String precioMax) {
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

	// probablemente falten get&set escondidos por el managebean..
	/*
	 * Methods
	 */
	public List<Evento> mostrarTodosLosEventos() {
		List<Evento> eventos = ef.encontrarTodosLosEventos();
		return eventos;
	}

	public int ultimoIDEventoIncrementado() {
		return 1;// Integer.parseInt(ef.ultimoIdInsertado()) + 1;
	}

	/*
	 * EventoFacade
	 */
	private Evento encontrarEventoID(String id) {
		return ef.encontrarEventoPorID(id).get(0);
	}

	public List<Evento> mostrarTodosLosEventosRevisados() {
		List<Evento> le = new ArrayList<>();
		Evento e = new Evento();
		e.setId(1);
		e.setTitulo("asd");
		e.setDescripcion("asd");
		le.add(e);
		return le;// ef.encontrarEventosRevisados();
	}

	public List<Evento> filtrarEventosDeUsuario() {
		return ef.encontrarEventoPorUsuario(usuario.getId().toString());
	}

	public List<Evento> mostrarEventosFiltradosPorPrecio() {
		return ef.encontrarEventoPorPrecioMaximo(precioMax);
	}

	public List<Evento> mostrarTodosLosEventosNoRevisados() {
		return ef.encontrarEventosNoRevisados();
	}

	public List<Evento> mostrarEventosFiltradosPorFecha() {
		return ef.encontrarEventosPorFecha(fecha.getId().toString());
	}

	/*
	 * DateevFacade (alvaro)
	 */

	public List<Dateev> encontrarFechaPorID(String id) {
		return def.encontrarFechaPorID(id);
	}

	public List<Dateev> mostrarTodasLasFechasUnicas() {
		return def.encontrarFechaPorUnica();
	}

	public List<Dateev> mostrarTodasLasFechasRango() {
		return def.encontrarFechaPorRango();
	}

	/*
	 * UsuarioFacade
	 */
	public void rrssLogin() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			Map params = externalContext.getRequestParameterMap();

			if (params.size() > 0) {
				usuario = new Usuario();
				usuario.setRol("");

				usuarioFoto = params.get("picture").toString();

				usuario.setEmail(params.get("email").toString());

				if (!logIn()) {
					usuario.setNombre(params.get("first_name").toString());
					usuario.setApellidos(params.get("last_name").toString());
					usuario.setEmail(params.get("email").toString());
					usuario.setRol("Usuario");
					nuevoUsuario(usuario);
				}
			}
		} catch (Exception e) {
			System.out.println("Error en RRSS: " + e.getMessage());
		}
	}

	public void nuevoUsuario(Usuario us) {
		UsuarioFacade uf = new UsuarioFacade();
		List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());

		if (usuarios.isEmpty()) {
			uf.crearUsuario(us);

			logIn();
			if (!usuarioFoto.isEmpty()) {
				// adjuntarFotoDePerfil(usuarioFoto);
			}
		}
	}

	public boolean logIn() {
		UsuarioFacade uf = new UsuarioFacade();
		List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());

		if (!usuarios.isEmpty()) {
			usuario = usuarios.get(0);
			///////////////////////////////////////////////////////////////////////// problema..
			// if (usuario.getFileevId() != null) {
			// usuarioFoto = usuario.getFileevId().getUrl();
			// }
			return true;
		} else {
			return false;
		}
	}

	public boolean isLogin() {
		return false;// (usuario.getEmail() == null || usuario.getEmail().equals(""));
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
	// GenericType<List<Dateev>> genericType2 = new GenericType<List<Dateev>>() {
	// };
	// List<Dateev> listaFecha = r.readEntity(genericType2);
	//
	// cliente.remove(ev.getId().toString());
	//
	// //Elimino Fecha de Evento
	// r = cliente.encontrarEventosPorFecha_XML(Response.class,
	// listaFecha.get(0).getId().toString());
	// if (r.getStatus() == 200) {
	// GenericType<List<Evento>> genericType3 = new GenericType<List<Evento>>() {
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
		return ef.ordenarEventosAlfabeticamente();
	}

	public List<Evento> mostrarEventosOrdenadosAlfabeticamenteDESC() {
		return ef.ordenarEventosAlfabeticamenteDESC();
	}

	public List<Evento> mostrarEventosOrdenadosPorPrecio() {
		return ef.ordenarEventosPorPrecio();
	}

	public List<Evento> mostrarEventosOrdenadosPorPrecioDESC() {
		return ef.ordenarEventosPorPrecioDESC();
	}

	// public void actualizarPuntuacion(String punto, String evId) {
	// Evento ev = ef.encontrarEventoPorID(evId).get(0);
	//
	// List<Puntuacion> puntuaciones =
	// pf.encontrarPuntuacionesDeEventoYUsuario(usuario.getId().toString(), evId);
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
	 * TagFacade (Alvaro)
	 */
	public void mostrarTagsDeUsuario() {
		List<Tag> listaTags = encontrarTagsDeUsuario();
		String lista = "";

		for (int i = 0; i < listaTags.size(); i++) {
			lista = lista + listaTags.get(i).getNombre() + ", ";
		}

		tagsUsuario = lista;
	}

	public List<Tag> encontrarTagPorNombre(String nombre) { // mejor private?
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

		List<Tagevento> lista = tef.encontrarTageventoPorEvento(evento.getId().toString());

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
			// el comentario esta asi de raro porqe el shift + crt + f de eclipse lo pone
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
	 * TagusuarioFacade (Alvaro)
	 */

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
		//return nf.encontrarNotificacionesNoLeidasDeUsuario(usuario.getId().toString());
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
}