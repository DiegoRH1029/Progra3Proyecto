package SistemaTaqueria;

public class Mesa {
	private int numMesa;
	private EstadoMesa estadoMesa;
	public int getNumMesa() {
		return numMesa;
	}
	public void setNumMesa(int numMesa) {
		this.numMesa = numMesa;
	}
	public EstadoMesa getEstadoMesa() {
		return estadoMesa;
	}
	public void setEstadoMesa(EstadoMesa estadoMesa) {
		this.estadoMesa = estadoMesa;
	}
	public Mesa(int numMesa, EstadoMesa estadoMesa) {
		super();
		this.numMesa = numMesa;
		this.estadoMesa = estadoMesa;
	}
	

}
