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
	public Producto clonarProd() {
		return new Antojitos(this.producto,this.cant,this.precio,this.notas,this.conQueso,this.carne,this.verduras);
	}
	@Override 
	public String toString() {
		String carneAbr = "";
	    switch (this.carne.toLowerCase()) {
	        case "pastor":  carneAbr = "p"; break;
	        case "bisteck": carneAbr = "bis"; break;
	        case "chorizo": carneAbr = "cho"; break;
	        case "birria":  carneAbr = "bir"; break;
	        case "lechon":  carneAbr = "lec"; break;
	        default:        carneAbr = this.carne;
	    }
	    String quesoStr = "";

	    boolean traeQuesoDeCajon = this.producto.equals("Quesadillas") || this.producto.equals("Burros") || 
	                              this.producto.equals("Volcanes") ;
	    if (traeQuesoDeCajon) {
	        if (!super.isConQueso()) quesoStr = "s/q"; // Solo avisamos si se lo quitan
	    } else {
	        if (super.isConQueso()) quesoStr = "c/q"; // Solo avisamos si es extra
	    }
	    String extrasAbr = this.verduras;
	    if (this.verduras.equalsIgnoreCase("Con todo")) extrasAbr = "c/t";
	    else if (this.verduras.equalsIgnoreCase("Sin cebolla")) extrasAbr = "s/ceb";
	    else if (this.verduras.equalsIgnoreCase("Sin cilantro")) extrasAbr = "s/cil";
	    else if (this.verduras.equalsIgnoreCase("Sin Verdura")) extrasAbr = "s/verd";
	    String descripcion = super.toString() + " " + carneAbr + " " + quesoStr + " " + extrasAbr;
		return descripcion.replaceAll(" +"," ").trim();
	}
	
	
}
