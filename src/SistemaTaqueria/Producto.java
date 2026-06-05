//Clase para los productos, esta hereda a snacks y antojitos
package SistemaTaqueria;

public class Producto {
	protected String producto;
	protected int cant;
	protected double precio;
	protected String notas;
	protected boolean conQueso;
	protected String estado = "Pendiente";
	
	public boolean isConQueso() {
		return conQueso;
	}

	public void setConQueso(boolean conQueso) {
		this.conQueso = conQueso;
	}
	

	public Producto(String producto, int cant, double precio, String notas,boolean conQueso,String estado) {
		super();
		this.producto = producto;
		this.cant = cant;
		this.precio = precio;
		this.notas = notas;
		this.conQueso=conQueso;
		this.estado=estado;
	}
	
	//Haremos metodo para poder clonar un objeto
	public Producto clonarProd() {
		return new Producto(this.producto,this.cant,this.precio,this.notas,this.conQueso,this.estado);
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
		String prodAbr = this.producto;
	    if (prodAbr.equals("Volcanes")) prodAbr = "Vol";
	    if (prodAbr.equals("Quesadillas")) prodAbr = "Ques";
	    if (prodAbr.equals("Hamburguesa")) prodAbr = "Burg";
	    String strRet=prodAbr+" " + notas;
		return strRet.replaceAll(" +"," ").trim();
	}
	public double getPrecioTotal() {
		return cant*precio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
