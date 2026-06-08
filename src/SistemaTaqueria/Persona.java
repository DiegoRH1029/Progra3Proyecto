//Clase persona, una persona pertenece a una mesa, cada persona tiene productos
package SistemaTaqueria;


import java.util.ArrayList;

public class Persona {
	private String nombre;
	private int ind;
	private ArrayList<Producto> listaProductos;//Para guardar los productos en memoria
	
	public Persona(String nombre, int ind) {
		super();
		this.nombre = nombre;
		this.setInd(ind);
	}
	
	

	public Persona(String nombre, int ind, ArrayList<Producto> listaProductos) {
		super();
		this.nombre = nombre;
		this.setInd(ind);
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



	public int getInd() {
		return ind;
	}



	public void setInd(int ind) {
		this.ind = ind;
	}
	//Metodo parar obtener el total de una persona
	public double getTotalPersona() {
		double totalPersona=0.0;
		if(listaProductos==null||listaProductos.isEmpty())return 0.0;
		for(Producto prod : listaProductos) {
			totalPersona+=prod.getPrecioTotal();
		}
		return totalPersona;
	}
	
	
}
