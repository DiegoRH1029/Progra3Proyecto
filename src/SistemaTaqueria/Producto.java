//Clase para los productos, esta hereda a snacks y antojitos
package SistemaTaqueria;

public class Producto {
	protected String producto;
	protected int cant;
	protected double precio;
	protected String notas;
	protected boolean conQueso;
	
	public boolean isConQueso() {
		return conQueso;
	}

	public void setConQueso(boolean conQueso) {
		this.conQueso = conQueso;
	}

	public Producto(String producto, int cant, double precio, String notas,boolean conQueso) {
		super();
		this.producto = producto;
		this.cant = cant;
		this.precio = precio;
		this.notas = notas;
		this.conQueso=conQueso;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public int getCant() {
		return cant;
	}

	public void setCant(int cant) {
		this.cant = cant;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
	public String toString() {
		
		return cant + " " + producto+" " + notas;
	}

}
