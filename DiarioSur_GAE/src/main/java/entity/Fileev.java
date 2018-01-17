package entity;


import java.io.Serializable;
import java.util.List;

public class Fileev implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String nombre;
    private String url;
    private String tipo;
    private int usuarioId;
    private List<Puntuacion> archivosList;

    public Fileev() {
    }

    public Fileev(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Puntuacion> getArchivosList() {
        return archivosList;
    }

    public void setArchivosList(List<Puntuacion> archivosList) {
        this.archivosList = archivosList;
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
        if (!(object instanceof Fileev)) {
            return false;
        }
        Fileev other = (Fileev) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fileev[ id=" + id + " ]";
    }

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}
}
