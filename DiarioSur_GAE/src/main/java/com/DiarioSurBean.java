
package com;
 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.List;
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
	DateevFacade def = new DateevFacade();
	
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
     * Construct
     */
    
    public DiarioSurBean() {
		// TODO Auto-generated constructor stub
	}
    
    /*
     * Set & Get
     */
    
    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    /*
     * Methods
     */
    public List<Evento> mostrarTodosLosEventos() {
        List<Evento> eventos = ef.encontrarTodosLosEventos();
        
        return eventos;
    }
    
    public int ultimoIDEventoIncrementado() {
        return Integer.parseInt(ef.ultimoIdInsertado()) + 1;
    }
    
    ////////////////////////////////////////////////////
    
   private Evento encontrarEventoID(String id) {
    	return ef.encontrarEventoPorID(id).get(0);
    }
    
   public List<Evento> mostrarTodosLosEventosRevisados() {
	   return ef.encontrarEventosRevisados();
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
   
   //////////////// Alvaro
   
   public List<Dateev> encontrarFechaPorID(String id){
	   return def.encontrarFechaPorID(id);
   }
   
   public List<Dateev> mostrarTodasLasFechasUnicas(){
	   return def.encontrarFechaPorUnica();
   }
   
   public List<Dateev> mostrarTodasLasFechasRango(){
	   return def.encontrarFechaPorRango();
   }
}