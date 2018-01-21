package entity;

import java.io.Serializable;
import java.util.Date;

public class Dateev implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer esunico;
    private Date dia;
    private Integer todoslosdias;
    private Date desde;
    private Date hasta;
    private Integer variosdias;
    private String listadias;
    private Integer eventoId;

    public Dateev() {
    }

    public Dateev(Integer id) {
        this.id = id;
    }

    public Dateev(Integer id, int eventoId) {
        this.id = id;
        this.eventoId = eventoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEsunico() {
        return esunico;
    }

    public void setEsunico(Integer esunico) {
        this.esunico = esunico;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Integer getTodoslosdias() {
        return todoslosdias;
    }

    public void setTodoslosdias(Integer todoslosdias) {
        this.todoslosdias = todoslosdias;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Integer getVariosdias() {
        return variosdias;
    }

    public void setVariosdias(Integer variosdias) {
        this.variosdias = variosdias;
    }

    public String getListadias() {
        return listadias;
    }

    public void setListadias(String listadias) {
        this.listadias = listadias;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
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
        if (!(object instanceof Dateev)) {
            return false;
        }
        Dateev other = (Dateev) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Dateev[ id=" + id + " ]";
    }
}
