//Es un producto tipo snack
package SistemaTaqueria;

public class Bebida extends Producto{
	private String nombreBebida;
	
	public Bebida(String producto, int cant, double precio, String notas, boolean conQueso, String nombreBebida,String estado) {
		super(producto, cant, precio, notas, conQueso,estado);
		this.nombreBebida = nombreBebida;
	}
	@Override 
	public Producto clonarProd() {
		return new Bebida(this.producto,this.cant,this.precio,this.notas,this.conQueso,this.nombreBebida,this.estado);
	}
	@Override 
	
	public String toString() {
		String strRet =nombreBebida;
		return strRet;
	}
}
