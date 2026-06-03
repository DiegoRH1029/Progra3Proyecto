//Es un producto tipo snack
package SistemaTaqueria;

public class Snacks extends Producto{
	private String extrasSnacks;



	public Snacks(String producto, int cant, double precio, String notas, boolean conQueso, String extrasSnacks) {
		super(producto, cant, precio, notas, conQueso);
		this.extrasSnacks = extrasSnacks;
	}

	public String getExtrasSnacks() {
		return extrasSnacks;
	}

	public void setExtrasSnacks(String extrasSnacks) {
		this.extrasSnacks = extrasSnacks;
	}
	
	@Override 
	
	public String toString() {
		String strRet = super.toString() +" "+ this.extrasSnacks;
		return strRet;
	}
}
