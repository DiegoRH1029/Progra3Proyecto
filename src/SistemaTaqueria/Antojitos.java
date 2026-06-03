//Es un producto tipo anto
package SistemaTaqueria;

public class Antojitos extends Producto {
	private String carne;
	private String verduras;
	

	public Antojitos(String producto, int cant, double precio, String notas, boolean conQueso, String carne,
			String verduras) {
		super(producto, cant, precio, notas, conQueso);
		this.carne = carne;
		this.verduras = verduras;
	}

	public String getCarne() {
		return carne;
	}

	public void setCarne(String carne) {
		this.carne = carne;
	}

	public String getVerduras() {
		return verduras;
	}

	public void setVerduras(String verduras) {
		this.verduras = verduras;
	}
	@Override 
	public String toString() {
		String conQueso="";
		if(super.isConQueso()) conQueso = "Con queso";
		String str = super.toString()+" de "+this.carne+" "+conQueso+" "+this.verduras;
		return str;
	}
	
	
}
