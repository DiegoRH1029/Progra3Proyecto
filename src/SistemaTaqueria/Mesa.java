package SistemaTaqueria;

import java.util.ArrayList;

public class Mesa {
	private int numMesa;
	private EstadoMesa estadoMesa;
	private ArrayList<Persona> personas;
	
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
	public ArrayList<Persona> getPersonas() {
		return personas;
	}
	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}
	public Mesa(int numMesa, EstadoMesa estadoMesa) {
		super();
		this.personas=new ArrayList<>();
		this.numMesa = numMesa;
		this.estadoMesa = estadoMesa;
	}
	public Mesa(int numMesa) {
		super();
		this.personas=new ArrayList<>();
		this.numMesa = numMesa;
		
	}
	public void addPersona(Persona persona) {
		personas.add(persona);
	}
		
}
