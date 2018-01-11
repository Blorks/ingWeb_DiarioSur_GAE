package com;
 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
 
import java.io.Serializable;
 
@ManagedBean
@SessionScoped
public class DiarioSurBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private String name;
 
	public String getName() {
		System.out.println("Entra en getName()");
		return name;
	}
 
	public void setName(String name) {
		System.out.println("Entra en setName()");
		this.name = name;
	}
 
}