package SistemaTaqueria;
import java.util.HashMap;

import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioDB {
	//Creamos unas tablas hash para controlar y guardar el intenvario
	private static HashMap<String, Double> precios = new HashMap<>(); //Para guardar precios
	private static HashMap<String, Boolean> disponible = new HashMap<>(); //Para guardar disponibilidad
	private static HashMap<String, Integer> stock = new HashMap<>(); //Para guardar stock del producto
	private static HashMap<String, Boolean> controlStock = new HashMap<>(); //Para ver si tiene stock (por ej tacos no tiene ya que es complicado contar las tortillas)	
	
	//funcion que carga los valores de la base de datos inventario al programa
	public static void cargarInventarioDB() {
		//Borramos si hubo valores anteriores
		precios.clear();
		disponible.clear();
		stock.clear();
		controlStock.clear();
		
		//Conectamos
		String sql = "SELECT nombre,precio,disponible,stock,controla_stock FROM inventario";
		try(Connection con = ConexionBD.obtenerConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery())
		{
				while(rs.next()) { //Recorremos inventario tabla
					String nombre = rs.getString("nombre");
					double precio = rs.getDouble("precio");
					boolean estaDisponible = rs.getBoolean("disponible");
					boolean llevaStock = rs.getBoolean("controla_stock");
					int stockActual = rs.getInt("stock");
					//Guarda,ps los datos 
					precios.put(nombre,precio);
					controlStock.put(nombre, llevaStock);
					//Si lleva sotck y ya no hay stock lo apagamos 
					if(llevaStock&&stockActual<=0) {
						estaDisponible=false;
					}
					disponible.put(nombre, estaDisponible);
					stock.put(nombre, stockActual);
				}
		}
		catch(NumberFormatException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	//Metodo para consultas y modificaciones del inventario 
	//Vamos a implementar restar stock
	public static boolean validarStockMemoria(String nombre, int cantARestar) {
		//Antes que nada verificamos que este disponible
		if(!disponible.getOrDefault(nombre, true)) {
			return false;
		}
		//Se veridica que este prodcuto se cuente el stock
		if(!controlStock.getOrDefault(nombre,false)) {
			return true; //No lleva control stock
		}
		//Si si lleva stock verificamos que la cantidad que nos pidio se cubra
		int stockMem = stock.getOrDefault(nombre, 0);
		if(cantARestar>stockMem ) {
			return false;
		}
		//Si si cumple la cantidad retornamos true;
		//No lo restamos directamente ya que si no se concreta la orden perderiamos datos, lo aremos al final
	
		return true;
	}
	//Este metodo va a actualizar el stock nuevo a la base de datos (Se llamara despues de cada fin de orden
	public static void actualizarStockBD(String nombre,Connection con) throws SQLException{
		//Solo actualizaremos los que llevan controlStock
		if(controlStock.getOrDefault(nombre, false)) {
			String sql="UPDATE inventario SET stock = ?,disponible=? WHERE nombre =?";
			try(PreparedStatement ps = con.prepareStatement(sql)){
				ps.setInt(1, stock.get(nombre));
				ps.setBoolean(2, disponible.get(nombre));
				ps.setString(3, nombre);
				
				ps.executeUpdate();
			}
		}
	}
	//Ahora el metodo para sumar stock a ram (por si se cancela un pedido o modicia
	public static void sumarStockMemoria(String nombre, int cantASumar) {
		if(controlStock.getOrDefault(nombre, false)) {
			int stockActual= stock.getOrDefault(nombre, 0);
			int nuevoStock=stockActual +cantASumar;
			stock.put(nombre, nuevoStock);
			if(nuevoStock>0) {
				disponible.put(nombre, true);
			}
		}	
		
	}
	public static void restarStockMemoria(String nombre, int cantARestar) {
		if(controlStock.getOrDefault(nombre, false)) {
			int stockActual = stock.getOrDefault(nombre, 0);
			int nuevoStock = stockActual-cantARestar;
			//Actualisamos el mapa
			stock.put(nombre, nuevoStock);
			if(nuevoStock<=0) {
				disponible.put(nombre, false);
			}
		}
	}
	//Ahora un metodo para extraer la llave del inventario desde el texto de la BD
	public static String obtenerNombreBase(String descBD) {
		String desc=descBD.toLowerCase();
		if(desc.contains("tacos")) return "Tacos";
		else if(desc.contains("tortas")) return "Tortas";
		else if(desc.contains("quesadillas")) return "Quesadillas";
		else if(desc.contains("burros")) return "Burros";
		else if(desc.contains("volcanes")) return "Volcanes";
		else if(desc.contains("hamburguesa")) return "Hamburguesa";
		else if(desc.contains("hotdogs")) return "HotDogs";
		else if(desc.contains("600ml")) return "Refresco 600ml";
		else if(desc.contains("350ml")) return "Refresco 350ml";
		else if(desc.contains("1L")) return "Aguas 1L";
		else if(desc.contains("medioL")) return "Aguas MedioL";
		return descBD;
	}
	//Para retornar su precio actual
	public static double getPrecio(String nombre) {
		return precios.getOrDefault(nombre, 0.0);
		
	}
	//Para verificar si esta disponible el producto
	public static boolean estaDisponible(String nombre) {
		return disponible.getOrDefault(nombre, false);
	}
	
}
