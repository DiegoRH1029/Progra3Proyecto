package SistemaTaqueria;

// Esta clase es para la comida fuerte. 
// Le pusimos "extends Producto" para aplicar herencia y no repetir variables como el precio o cantidad.
public class Antojitos extends Producto {
	
	// Variables exclusivas de los antojitos (las bebidas no ocupan carne ni verduras)
	private String carne;
	private String verduras;
	
	// Constructor
	public Antojitos(String producto, int cant, double precio, String notas, boolean conQueso, String carne,
			String verduras, String estado) {
		// Usamos super() para mandarle los datos generales a la clase padre (Producto)
		super(producto, cant, precio, notas, conQueso, estado);
		// Y ya nomás guardamos lo que es propio de esta clase
		this.carne = carne;
		this.verduras = verduras;
	}

	// Getters y Setters
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
	
	// Sobreescribimos este método para hacer una copia exacta del producto.
	// Esto nos ayuda a que no se nos crucen o modifiquen los datos por error en las listas de memoria.
	@Override 
	public Producto clonarProd() {
		return new Antojitos(this.producto, this.cant, this.precio, this.notas, this.conQueso, this.carne, this.verduras, this.estado);
	}
	
	// Sobreescribimos el toString para armar el texto que va a salir impreso en el ticket
	@Override 
	public String toString() {
		
		// Hacemos un switch para cambiar los nombres largos por letras chiquitas (ahorrar espacio)
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

	    // Checamos si el antojito ya trae queso por default
	    boolean traeQuesoDeCajon = this.producto.equals("Quesadillas") || 
	    						   this.producto.equals("Burros") || 
	                               this.producto.equals("Volcanes") ;
	                               
	    if (traeQuesoDeCajon) {
	    	// Si ya trae, solo avisamos en el ticket si el cliente pide que se lo quiten
	        if (!super.isConQueso()) quesoStr = "s/q"; 
	    } else {
	    	// Si es un taco (no trae queso), avisamos cuando el cliente sí quiere extra
	        if (super.isConQueso()) quesoStr = "c/q"; 
	    }
	    
	    // Abreviamos también las verduras
	    String extrasAbr = this.verduras;
	    if (this.verduras.equalsIgnoreCase("Con todo")) extrasAbr = "c/t";
	    else if (this.verduras.equalsIgnoreCase("Sin cebolla")) extrasAbr = "s/ceb";
	    else if (this.verduras.equalsIgnoreCase("Sin cilantro")) extrasAbr = "s/cil";
	    else if (this.verduras.equalsIgnoreCase("Sin Verdura")) extrasAbr = "s/verd";
	    
	    // Juntamos todo el texto final llamando primero al toString del padre y luego agregando lo de aquí
	    String descripcion = super.toString() + " " + carneAbr + " " + quesoStr + " " + extrasAbr;
	    
	    // Le damos una limpiada con replaceAll para borrar dobles espacios si una abreviación quedó vacía
		return descripcion.replaceAll(" +"," ").trim();
	}
}