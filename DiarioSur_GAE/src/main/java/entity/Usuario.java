package entity;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String hashpassword;
    private String rol;
    private Integer fileev;
    private List<Integer> tagusuarioList;
    private List<Integer> notificacionList;
    private List<Integer> eventoList;

    public Usuario() {
    }

    public Usuario(Integer id) {
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashpassword() {
        return hashpassword;
    }

    public void setHashpassword(String hashpassword) {
        this.hashpassword = hashpassword;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Integer> getTagusuarioList() {
        return tagusuarioList;
    }

    public void setTagusuarioList(List<Integer> tagusuarioList) {
        this.tagusuarioList = tagusuarioList;
    }

    public List<Integer> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Integer> notificacionList) {
        this.notificacionList = notificacionList;
    }

    public List<Integer> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Integer> eventoList) {
        this.eventoList = eventoList;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Usuario[ id=" + id + " ]";
    }

	public Integer getFileev() {
		return fileev;
	}

	public void setFileev(Integer fileev) {
		this.fileev = fileev;
	}
}
