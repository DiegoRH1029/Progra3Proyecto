//Es un producto tipo snack
package SistemaTaqueria;

public class Bebida extends Producto{
	private String nombreBebida;
	
	public Bebida(String producto, int cant, double precio, String notas, boolean conQueso, String nombreBebida) {
		super(producto, cant, precio, notas, conQueso);
		this.nombreBebida = nombreBebida;
	}

	@Override 
	
	public String toString() {
		String strRet =super.getCant()+" "+ nombreBebida;
		return strRet;
	}
}
