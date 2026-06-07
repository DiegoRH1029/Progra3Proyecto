package SistemaTaqueria;
//para tener la  fecha y hora actual 
import java.time.LocalDateTime;
public class TransaccionFinanciera {
	private String tipo;//Venta o Gasto
	private String categoria;
	private double monto;
	private String detalle;
	private LocalDateTime fecha;
	public TransaccionFinanciera(String tipo, String categoria, double monto, String detalle, LocalDateTime fecha) {
		super();
		this.tipo = tipo;
		this.categoria = categoria;
		//si es gasto, nos aseguramos por logica que el monto sea negativo
		this.monto = tipo.equalsIgnoreCase("GASTO") ? -Math.abs(monto) :Math.abs(monto);
		this.detalle = detalle;
		this.fecha = LocalDateTime.now();
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	

}
