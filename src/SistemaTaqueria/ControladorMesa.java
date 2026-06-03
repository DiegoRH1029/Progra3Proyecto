package SistemaTaqueria;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ControladorMesa {
	
		
	public static List<Mesa> generarListaMesas(){
		List<Mesa> listaMesas = new ArrayList<>();
		try {
			//Preparamos la conexion con base de datos
			Connection con = ConexionBD.obtenerConexion();
			PreparedStatement pst=con.prepareStatement("Select * from mesas");
			
			ResultSet rs = pst.executeQuery();
			//Mientras no tengamos renglones por leer
			while(rs.next()) {
				int idMesa= rs.getInt("idMesa");
				String estadoSTR = rs.getString("estado");
				EstadoMesa estadoEnum = EstadoMesa.LIBRE; //Valor por defecto
				//Ahora copeamos el estadostr a estado enum
				try {
					//Lo convertimos a mayusculas
					estadoEnum = EstadoMesa.valueOf(estadoSTR.toUpperCase());
				}
				catch(IllegalArgumentException ex2) {
					ex2.printStackTrace();
				}
				//Ahora guardamos los datos leidos a la clase mesa
				Mesa mesaGuard = new Mesa(idMesa,estadoEnum);
				listaMesas.add(mesaGuard);
			}
			
		}
		catch(Exception e1) {
			e1.printStackTrace();
		}
		
		
		
		return listaMesas;
	}
	public static EstadoMesa getEstadoMesa(int idMesa) {
		EstadoMesa estadoEnum = null; 
			//Preparamos la conexion con base de datos
		try(Connection con = ConexionBD.obtenerConexion();
			PreparedStatement pst=con.prepareStatement("Select estado from mesas where idMesa=?");){
			pst.setInt(1, idMesa);


			//Ahora copeamos el estadostr a estado enum
			try {
				ResultSet rs = pst.executeQuery();
				//Mientras no tengamos renglones por leer
				if(rs.next()) {
					String estadoSTR = rs.getString("estado");
					try {
						//Lo convertimos a mayusculas
					estadoEnum = EstadoMesa.valueOf(estadoSTR.toUpperCase());
					}
					catch(IllegalArgumentException ex2){
						ex2.printStackTrace();
					}
				}

			}
			catch(IllegalArgumentException ex2) {
				ex2.printStackTrace();
			}
				//Ahora guardamos los datos leidos a la clase mesa
		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return estadoEnum;
		
	
	}
	//Esta funcion servira para editar mesa mas adelante
	public Mesa getMesa(int idMesa) {
		return null;
	}
	
	
}
