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
	//Para retornar su precio actual
	public static double getPrecio(String nombre) {
		return precios.getOrDefault(nombre, 0.0);
		
	}
	//Para verificar si esta disponible el producto
	public static boolean estaDisponible(String nombre) {
		return disponible.getOrDefault(nombre, false);
	}
	
}
