package entity;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String nombre;
    private List<Integer> tageventoList;
    private List<Integer> tagusuarioList;

    public Tag() {
    }

    public Tag(Integer id) {
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

    public List<Integer> getTageventoList() {
        return tageventoList;
    }

    public void setTageventoList(List<Integer> tageventoList) {
        this.tageventoList = tageventoList;
    }

    public List<Integer> getTagusuarioList() {
        return tagusuarioList;
    }

    public void setTagusuarioList(List<Integer> tagusuarioList) {
        this.tagusuarioList = tagusuarioList;
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
        if (!(object instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Tag[ id=" + id + " ]";
    }
}
