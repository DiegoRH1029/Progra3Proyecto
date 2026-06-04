package SistemaTaqueria;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ControladorMesa {
	
	private static List<Mesa> listaMesas = new ArrayList<>();	
	
	//Actualizaremos este objeto para que jale todos los pedidos de la base de datos y los cargue en la lista de mesas
	public static List<Mesa> generarListaMesas(){
		try {
			listaMesas.clear();
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
				//Si no esta libre la mesa cargaremos su pedido desde mysql
				if(estadoEnum!=EstadoMesa.LIBRE) {
					//Traemos la cuenta
					String sqlCuenta = "SELECT idCuenta FROM cuentas WHERE idMesa = ?";
					PreparedStatement pstCuenta = con.prepareStatement(sqlCuenta);
					pstCuenta.setInt(1,idMesa);
					ResultSet rsCuenta = pstCuenta.executeQuery();
					
					if(rsCuenta.next()) {
						//Aqui inicamos obteniendo las personas
						int idCuenta = rsCuenta.getInt("idCuenta");
						String sqlPersonas = "SELECT idPersona,nombre FROM personas WHERE idCuenta = ?";
						PreparedStatement pstPersonas = con.prepareStatement(sqlPersonas);
						pstPersonas.setInt(1,idCuenta);
						ResultSet rsPersonas = pstPersonas.executeQuery();
						
						int contPer = 1;
						while(rsPersonas.next()) {
							int idPersona = rsPersonas.getInt("idPersona");
							String nombrePersona = rsPersonas.getString("nombre");
							ArrayList<Producto> listaProd=new ArrayList<>();
							
							//Traemos los pedidos de esta persona
							String sqlPedidos = "SELECT cantidad,producto,precio FROM pedidos WHERE idPersona = ?";
							PreparedStatement pstPedidos = con.prepareStatement(sqlPedidos);
							pstPedidos.setInt(1,idPersona);
							ResultSet rsPedidos = pstPedidos.executeQuery();
							
							while(rsPedidos.next()) {
								int cant= rsPedidos.getInt("cantidad");
								String descBD = rsPedidos.getString("producto");
								double precioTotal = rsPedidos.getDouble("precio");
								//Se recrea el producto generico 
								ProductoCargado prodRevivido = new ProductoCargado(descBD,cant,(precioTotal/cant));
								listaProd.add(prodRevivido);
								
							}
							Persona personaGuard = new Persona(nombrePersona,contPer,listaProd);
							mesaGuard.addPersona(personaGuard);
							contPer++;
						}
					}

				}
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
		if(!listaMesas.isEmpty()) {
			estadoEnum=listaMesas.get(idMesa-1).getEstadoMesa();
			return estadoEnum;
		}
		return null;
		
	
	}
	//Esta funcion servira para editar mesa mas adelante
	public static Mesa getMesa(int idMesa) {
		return listaMesas.get(idMesa-1);
	}
	
	
}
