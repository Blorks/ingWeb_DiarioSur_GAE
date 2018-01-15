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
    	EventoFacade ef = new EventoFacade();
        List<Evento> eventos = null;
        
        return eventos;
    }
    
    public int ultimoIDEventoIncrementado() {
    	EventoFacade ef = new EventoFacade();
        return Integer.parseInt(ef.ultimoIdInsertado()) + 1;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}