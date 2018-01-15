package com;
 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.List;
 
@ManagedBean
@SessionScoped
public class DiarioSurBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private facade.DiarioSurFachada dsf = new facade.DiarioSurFachada();
	private String numero = "";
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	

	public void introNum() {
		dsf.introducirNumero(numero);
		irPortada();
	}
	
	public List<String> mostrarNum() {
		return dsf.encontrarNumeros();
	}
	
	public String irMostrarNumeros() {
		return "muestraNumeros.xhtml";
	}
	
	public String irPortada() {
		return "hello.xhtml";
	}
 
}