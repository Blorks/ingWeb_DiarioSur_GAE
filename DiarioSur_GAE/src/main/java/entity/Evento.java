package entity;

import java.io.Serializable;
import java.util.List;

public class Evento implements Serializable{
	private static final long serialVersionUID = 1L;
	private String titulo;
    private String subtitulo;
    private String descripcion;
    private Integer id;
    private String direccionfisica;
    private Double precio;
    private Double latitud;
    private Double longitud;
    private Integer estarevisado;
    private List<Calendario> calendarioList;
    private List<Tagevento> tageventoList;
    private List<Puntuacion> archivosList;
    private int dateevId;
    private int usuarioId;

    public Evento() {
    }

    public Evento(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccionfisica() {
        return direccionfisica;
    }

    public void setDireccionfisica(String direccionfisica) {
        this.direccionfisica = direccionfisica;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Integer getEstarevisado() {
        return estarevisado;
    }

    public void setEstarevisado(Integer estarevisado) {
        this.estarevisado = estarevisado;
    }

    public List<Calendario> getCalendarioList() {
        return calendarioList;
    }

    public void setCalendarioList(List<Calendario> calendarioList) {
        this.calendarioList = calendarioList;
    }

    public List<Tagevento> getTageventoList() {
        return tageventoList;
    }

    public void setTageventoList(List<Tagevento> tageventoList) {
        this.tageventoList = tageventoList;
    }

    public List<Puntuacion> getArchivosList() {
        return archivosList;
    }

    public void setArchivosList(List<Puntuacion> archivosList) {
        this.archivosList = archivosList;
    }

    public int getDateevId() {
        return dateevId;
    }

    public void setDateevId(int dateevId) {
        this.dateevId = dateevId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Evento[ id=" + id + " ]";
    }
}
