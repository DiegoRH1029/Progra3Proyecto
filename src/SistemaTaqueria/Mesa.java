package SistemaTaqueria;

import java.util.ArrayList;

// Esta clase representa una mesa física del local.
// Funciona como un "contenedor" que agrupa a los clientes (Personas) y nos dice en que estado esta la mesa.
public class Mesa {
	
	private int numMesa; // El numerito fisico de la mesa (Mesa 1, Mesa 2...)
	private EstadoMesa estadoMesa; // Usamos un Enum para no equivocarnos escribiendo el estado ("Libre", "Atendida", etc.)
	
	// Usamos un ArrayList de Persona porque en una mesa pueden sentarse varias personas,
	// y para cobrar bien (o dividir cuentas), cada persona guarda sus propios tacos.
	private ArrayList<Persona> personas;
	
	// ==========================================
	// Getters y Setters
	// ==========================================
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
	
	// ==========================================
	// Constructores
	// ==========================================
	
	// Constructor principal (cuando sabemos el número y el estado de la mesa)
	public Mesa(int numMesa, EstadoMesa estadoMesa) {
		super();
		// Súper importante inicializar el ArrayList aquí. 
		// Si no lo hacemos, cuando queramos meter a la primera persona nos va a dar NullPointerException.
		this.personas=new ArrayList<>(); 
		this.numMesa = numMesa;
		this.estadoMesa = estadoMesa;
	}
	
	// Constructor secundario (para cuando solo queremos crear la mesa por su número rápido)
	public Mesa(int numMesa) {
		super();
		this.personas=new ArrayList<>();
		this.numMesa = numMesa;
	}
	
	// Método auxiliar para no tener que hacer mesa.getPersonas().add(persona) cada vez.
	// Simplemente llamamos a mesa.addPersona() y queda más limpio el código.
	public void addPersona(Persona persona) {
		personas.add(persona);
	}
		
}