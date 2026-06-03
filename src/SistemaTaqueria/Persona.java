//Clase persona, una persona pertenece a una mesa, cada persona tiene productos
package SistemaTaqueria;


import java.util.ArrayList;

public class Persona {
	private String nombre;
	private int ind;
	private ArrayList<Producto> listaProductos;

	public Persona(String nombre, int ind) {
		super();
		this.nombre = nombre;
		this.ind=ind;
	}
	
	

	public Persona(String nombre, int ind, ArrayList<Producto> listaProductos) {
		super();
		this.nombre = nombre;
		this.ind = ind;
		this.listaProductos = listaProductos;
	}



	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public ArrayList<Producto> getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(ArrayList<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}
	//Agregamos producto
	public void addProduct(Producto p) {
		listaProductos.add(p);
	}
	
	
	
}
