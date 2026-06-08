package SistemaTaqueria;

// Esta clase nos sirve para representar como objeto cada producto que tenemos en la BD.
// Nos ayuda a controlar qué cosas se cuentan (como los refrescos) y qué cosas no (como los tacos).
public class ArticuloInventario {
	
	// Variables que coinciden con las columnas de nuestra tabla de inventario
	private int id;
	private String nombre;
	private int stockActual;
	private boolean controlaStock; // Bandera súper importante para saber si este producto se puede agotar
	private boolean disponible;    // Para saber si lo mostramos o lo ocultamos en la interfaz

	// Constructor para armar el objeto cuando leemos los datos desde MySQL
	public ArticuloInventario(int id, String nombre, int stockActual, boolean controlaStock, boolean disponible) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.stockActual = stockActual;
		this.controlaStock = controlaStock;
		this.disponible = disponible;
	}
	
	// Método que llamamos cuando se hace una venta para ir bajando el inventario en memoria
	public boolean restarStock(int cantidadVendida) {
		// Si es un producto que no contamos (ej. un taco de pastor), siempre hay, así que regresa true directo
		if(!controlaStock) return true;
		
		// Si sí se cuenta (ej. una Coca), primero checamos que nos alcance para la venta
		if (stockActual >= cantidadVendida) {
			stockActual -= cantidadVendida; // Le restamos lo que se vendió
			return true; // Sí se pudo vender
		}
		
		// Si llega hasta acá es porque nos pidieron más de lo que tenemos en el refri
		return false; 
	}
	
	// Método para reabastecer o devolver productos si se cancela una orden
	public void agregarStock(int cantidad) {
		// Solo tiene sentido sumarle si es un producto contable
		if (controlaStock) {
			this.stockActual += cantidad;
		}
	}
	
	// Un switch rápido para activar o desactivar productos desde el menú de administración
	// Básicamente invierte el valor (si era true lo hace false y viceversa)
	public void cambiarDisponibilidad() {
		this.disponible = !this.disponible;	
	}
	
	// ==========================================
	// Getters y Setters de cajón para encapsular
	// ==========================================
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getStockActual() {
		return stockActual;
	}
	public void setStockActual(int stockActual) {
		this.stockActual = stockActual;
	}
	public boolean isControlaStock() {
		return controlaStock;
	}
	public void setControlaStock(boolean controlaStock) {
		this.controlaStock = controlaStock;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
}