package SistemaTaqueria;

// Clase para manejar exclusivamente los líquidos (refrescos, aguas, etc.).
// Hereda de Producto para aprovechar las variables de precio, cantidad y estado.
public class Bebida extends Producto {
	
	// Variable propia de la clase para guardar el sabor y tamaño (ej. "Coca 600ml")
	private String nombreBebida;
	
	// Constructor
	public Bebida(String producto, int cant, double precio, String notas, boolean conQueso, String nombreBebida, String estado) {
		// Llamamos al constructor padre con 'super'.
		// Ojo: aunque una bebida nunca lleva queso ni notas complejas, la clase padre nos pide 
		// esos parámetros por diseño, así que se los pasamos para que no marque error.
		super(producto, cant, precio, notas, conQueso, estado);
		this.nombreBebida = nombreBebida;
	}
	
	// Sobreescribimos el método de clonación por seguridad.
	// Esto nos garantiza que al agregar dos bebidas iguales a diferentes personas, 
	// cada una tenga su propio espacio en memoria y no se buguee la lista.
	@Override 
	public Producto clonarProd() {
		return new Bebida(this.producto, this.cant, this.precio, this.notas, this.conQueso, this.nombreBebida, this.estado);
	}
	
	// Método toString para la impresión del ticket.
	// A diferencia de los antojitos, las bebidas no tienen carnes ni extras que abreviar,
	// por lo que simplemente retornamos su nombre tal cual para que se vea limpio en la pantalla.
	@Override 
	public String toString() {
		String strRet = nombreBebida;
		return strRet;
	}
}