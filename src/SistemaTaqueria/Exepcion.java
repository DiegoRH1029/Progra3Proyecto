// Hacemos nuestra propia clase de Excepcion personalizada para manejar los errores 
// específicos de nuestro sistema (por ejemplo, cuando intentan meter una cantidad negativa de tacos).
// Hereda de Exception para que Java la trate como un error oficial que podemos atrapar con un try-catch.
package SistemaTaqueria;

public class Exepcion extends Exception{

	// Eclipse nos pide poner esto por defecto siempre que heredamos de Exception 
	// (sirve para la "serialización" de objetos, o sea, por si alguna vez mandamos este error por una red o a un archivo). 
	// Le dejamos el 1L que genera por default.
	private static final long serialVersionUID = 1L;

	// Constructor que recibe el mensaje de error personalizado que queremos mostrar
	Exepcion(String mensaje){
		// Usamos super() para pasarle el texto del error a la clase padre (Exception) 
		// y que ella se encargue de guardarlo para cuando llamemos a e.getMessage() en los JOptionPane
		super(mensaje);
	}

}