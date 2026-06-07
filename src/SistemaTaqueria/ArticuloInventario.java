package SistemaTaqueria;

public class ArticuloInventario {
	private int id;
	private String nombre;
	private int stockActual;
	private boolean controlaStock;
	private boolean disponible;

	public ArticuloInventario(int id, String nombre, int stockActual, boolean controlaStock, boolean disponible) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.stockActual = stockActual;
		this.controlaStock = controlaStock;
		this.disponible = disponible;
	}
	public boolean restarStock(int cantidadVendida) {
		if(!controlaStock)return true;
		if (stockActual >= cantidadVendida) {
			stockActual-=cantidadVendida;
			return true;
		}
		return false;
	}
	public void agregarStock(int cantidad) {
		if (controlaStock) {
			this.stockActual+=cantidad;
			
		}
	}
	public void cambiarDisponibilidad() {
		this.disponible=!this.disponible;	
		}
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
