package SistemaTaqueria;
//Este objeto sera un producto jalado desde la base de datos (para transformar el texto)
public class ProductoCargado extends Producto{
	private String descripcionBD;
	
	public ProductoCargado(String descripcionBD,int cant, double precio,String estado) {
		super("",cant,precio,"",false,estado);
		this.descripcionBD = descripcionBD;
	}
	@Override 
	public String toString() {
		return descripcionBD;
	}
}
