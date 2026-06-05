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
							String sqlPedidos = "SELECT cantidad,producto,precio,estado FROM pedidos WHERE idPersona = ?";
							PreparedStatement pstPedidos = con.prepareStatement(sqlPedidos);
							pstPedidos.setInt(1,idPersona);
							ResultSet rsPedidos = pstPedidos.executeQuery();
							
							while(rsPedidos.next()) {
								int cant= rsPedidos.getInt("cantidad");
								String descBD = rsPedidos.getString("producto");
								double precioTotal = rsPedidos.getDouble("precio");
								String estadoDB =rsPedidos.getString("estado");//Leemos su estado desde DB
								//Se recrea el producto generico 
								ProductoCargado prodRevivido = new ProductoCargado(descBD,cant,(precioTotal/cant),estadoDB);
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
	//Implementaremos toda la logica de liberar/cancelar mesa
	//Esta es para el caso de una mesa este en estado esperando
	//Se le cobrara los pedidos "Terminados" pero se borran los demas y forza la mesa en estado libre
	public static int cancelarMesa(int numMesa) {
		//0 = Error 1=Cancelacion total, 2=cancelacion parcial (tenian pedidos terminados)
		Connection con = null;
		try {
			con = ConexionBD.obtenerConexion();
			con.setAutoCommit(false); //por seguridad
			ControladorPedidos.reembolsarPedPendientes(numMesa, con);//Reembolsamos pedidos pendientes
			int idCuentaActiva=-1;
			String sqlGetCuenta = "SELECT idCuenta FROM cuentas WHERE idMesa = ?";
			try(PreparedStatement psGet = con.prepareStatement(sqlGetCuenta)){
				psGet.setInt(1,numMesa);
				ResultSet rsGet =psGet.executeQuery();
				if(rsGet.next())idCuentaActiva=rsGet.getInt("idCuenta");
			}
			if(idCuentaActiva!=-1) {
				//Borramos los pedidos "pendientes"
				String sqlDelPendientes = "DELETE FROM pedidos WHERE estado = 'Pendiente' AND idPersona "+
										  "IN (SELECT idPersona FROM personas WHERE idCuenta=?)";
				try(PreparedStatement psDelPendientes = con.prepareStatement(sqlDelPendientes)){
					psDelPendientes.setInt(1, idCuentaActiva);
					psDelPendientes.executeUpdate();
				}
				//Checamos si quedo algo en terminado 
				boolean hayTerminados = false;
				String sqlCheck = "SELECT COUNT(*) AS restantes FROM pedidos WHERE idPersona IN "+
								  "(SELECT idPersona FROM personas WHERE idCuenta = ?)";
				try(PreparedStatement psCheck = con.prepareStatement(sqlCheck)){
					psCheck.setInt(1, idCuentaActiva);
					ResultSet rsCheck = psCheck.executeQuery();
					if(rsCheck.next()&&rsCheck.getInt("restantes")>0) {
						hayTerminados=true;
					}
					
				}
				if(hayTerminados) {
					//Aqui se interta los pedidos a la tabla de historial pedidos
					borrarCuentaActiva(numMesa,con);
					cambiarEstadoMesaLibre(numMesa,con);
					con.commit();
					return 2;
				}else {
					//Si no quedo nada lo borramos todo alv
					borrarCuentaActiva(numMesa,con);
					cambiarEstadoMesaLibre(numMesa,con);
					con.commit();
					return 1;
				}
			}
		}catch(SQLException ex1) {
			ex1.printStackTrace();
			try {
				if(con!=null)con.rollback();//En caso de que hlgo falle se hace rollback
			}catch(SQLException ex2){}
			return 0;//Se regresa todo
		}finally {
			try {if(con!=null) con.close();}catch(SQLException ex3){}//Si todo esta correcto se cierra la conexion
		}
		return 0;
	}
	//Ahora otro metodo para el caso en que la mesa este en estado atendida, borramos todo y subimos a la base de datos el cobro
	public static boolean cobrarMesa(int numMesa) {
		Connection con=null;
		try {
			con=ConexionBD.obtenerConexion();
			con.setAutoCommit(false);
			//Aqui ira la comunicacion con la tabla historial ---<
			borrarCuentaActiva(numMesa,con);
			cambiarEstadoMesaLibre(numMesa,con);
			con.commit();
			return true;
		}catch(SQLException ex1) {
			ex1.printStackTrace();
			try {
				if(con!=null)con.rollback();//En caso de que hlgo falle se hace rollback
			}catch(SQLException ex2){}
			return false;
		}finally {
			try {if(con!=null) con.close();}catch(SQLException ex3){}//Si todo esta correcto se cierra la conexion
		}
		
	}
	//Haremos otra funcion para borrar ccuenta completamente
	private static void borrarCuentaActiva(int numMesa,Connection con)throws SQLException{
		String sqlGetCuenta = "SELECT idCuenta FROM cuentas WHERE idMesa = ?";//Seleccionamos mesa
		try(PreparedStatement psGet = con.prepareStatement(sqlGetCuenta)){
			psGet.setInt(1,numMesa);
			ResultSet rsGet =psGet.executeQuery();
			if(rsGet.next()) {
				//Borramos todos los campos
				int idCuentaVieja=rsGet.getInt("idCuenta");
				//Borramos pedidos
				String sqlDelPedidos = "DELETE FROM pedidos WHERE idPersona IN (SELECT idPersona FROM personas WHERE idCuenta = ?)";
				try(PreparedStatement psDelPedidos = con.prepareStatement(sqlDelPedidos)){
					psDelPedidos.setInt(1,idCuentaVieja);
					psDelPedidos.executeUpdate();
				}
				//Borramos cuentas
				String sqlDelPer = "DELETE FROM personas WHERE idCuenta = ?";
				try(PreparedStatement psDelPer = con.prepareStatement(sqlDelPer)){
					psDelPer.setInt(1,idCuentaVieja);
					psDelPer.executeUpdate();
				}
				//Borramos personas
				String sqlDelCuenta = "DELETE FROM cuentas WHERE idCuenta = ?";
				try(PreparedStatement psDelCuenta = con.prepareStatement(sqlDelCuenta)){
					psDelCuenta.setInt(1,idCuentaVieja);
					psDelCuenta.executeUpdate();
				}
			}
		}
		
	}
	//Ahora despues de borrar todo la mesa estara libre, usaremos este metodo
	private static void cambiarEstadoMesaLibre(int numMesa,Connection con)throws SQLException{
		String sqlEstado = "UPDATE mesas SET estado = 'Libre' WHERE idMesa = ?";
		try(PreparedStatement psEstado = con.prepareStatement(sqlEstado)){
			psEstado.setInt(1,numMesa);
			psEstado.executeUpdate();
		}
	}
}
