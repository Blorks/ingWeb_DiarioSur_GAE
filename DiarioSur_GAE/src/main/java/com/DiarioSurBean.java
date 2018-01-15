
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
        return Integer.parseInt(ef.ultimoIdInsertado()) + 1;
    }
    
    /*
     * EventoFacade
     */
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
	
   /*
    * DateevFacade (alvaro)
    */
   
   public List<Dateev> encontrarFechaPorID(String id){
	   return def.encontrarFechaPorID(id);
   }
   
   public List<Dateev> mostrarTodasLasFechasUnicas(){
	   return def.encontrarFechaPorUnica();
   }
   
   public List<Dateev> mostrarTodasLasFechasRango(){
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
               adjuntarFotoDePerfil(usuarioFoto);
           }
       }
   }
   
   public boolean logIn() {
       UsuarioFacade uf = new UsuarioFacade();
       List<Usuario> usuarios = uf.encontrarUsuarioPorEmail(usuario.getEmail());

       if (!usuarios.isEmpty()) {
           usuario = usuarios.get(0);
			///////////////////////////////////////////////////////////////////////// problema..
           if (usuario.getFileevId() != null) {
               usuarioFoto = usuario.getFileevId().getUrl();
           }
           return true;
       } else {
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
}