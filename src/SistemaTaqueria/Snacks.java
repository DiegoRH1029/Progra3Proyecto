//Es un producto tipo snack, atributos especiales los extras del snack (Sin aderezos sin verdura etc)
package SistemaTaqueria;

public class Snacks extends Producto{
	private String extrasSnacks;



	public Snacks(String producto, int cant, double precio, String notas, boolean conQueso, String extrasSnacks,String estado) {
		super(producto, cant, precio, notas, conQueso,estado);
		this.extrasSnacks = extrasSnacks;
	}


	public String getExtrasSnacks() {
		return extrasSnacks;
	}

	public void setExtrasSnacks(String extrasSnacks) {
		this.extrasSnacks = extrasSnacks;
	}
	
	@Override 
	public Producto clonarProd() {
		return new Snacks(this.producto,this.cant,this.precio,this.notas,this.conQueso,this.extrasSnacks,this.estado);
	}
	@Override 
	public String toString() {

	    String strRet = super.toString() +" "+ this.extrasSnacks;
		return strRet.replaceAll(" +"," ").trim();
	}
}
