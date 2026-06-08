//Clase que controla toda la logica de las mesas, guarda su estado los actualiza, hace los cobros y las libera
package SistemaTaqueria;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControladorMesa {
	
	private static List<Mesa> listaMesas = new ArrayList<>(); //Se guarda en su memoria una lista de las mesas en memoria

	//Este metodo genera la lista de las mesas desde la base de datos
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
		
		
		
		return listaMesas; //Retorna la lista
	}
	//Get estado de la mesa
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
	//Este metodo funciona ppara cancelar una mesa, hay varios tipos de cancelacion una es la total (no les sirvieron nada y no se cobra nada)
	//Otra es la parcial (le llevaron unos pedidos y se les cobraran) en estos ultimos casos se suma stock a la base de datos y otra es cuando les sirvieron todos sus pedidos
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
					con.commit();
					con.close();
					cobrarMesa(numMesa);
		
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
			generarCobroMesa(numMesa);//Aqui ira la comunicacion con la tabla historial ---<
			return true;
		}finally {
			try {if(con!=null) con.close();}catch(SQLException ex3){}//Si todo esta correcto se cierra la conexion
		}
		
	}
	//Metodo para borrar ccuenta completamente
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
	
	//Metodo que cambia la mesa de estado a libre a la base de datos
	private static void cambiarEstadoMesaLibre(int numMesa,Connection con)throws SQLException{
		String sqlEstado = "UPDATE mesas SET estado = 'Libre' WHERE idMesa = ?";
		try(PreparedStatement psEstado = con.prepareStatement(sqlEstado)){
			psEstado.setInt(1,numMesa);
			psEstado.executeUpdate();
		}
	}
	//Aqui meteremos la funcion para cobrar una mesa, esta guardara el cobro en la base de datos en la tabla finanzas 
	public static boolean generarCobroMesa(int numMesa) {
		Connection con = null;
		try {
			con=ConexionBD.obtenerConexion();
			con.setAutoCommit(false);
			int idCuentaActiva = -1;
			String sqlGetCuenta = "SELECT idCuenta FROM cuentas WHERE idMesa = ?";
			try(PreparedStatement psGet = con.prepareStatement(sqlGetCuenta)){
				psGet.setInt(1, numMesa);
				ResultSet rsGet = psGet.executeQuery();
				if(rsGet.next()) idCuentaActiva = rsGet.getInt("idCuenta");	
			}
			if(idCuentaActiva!=-1) {
				//Aqui arameremos el ticket de la cuenta 
				//Usaremos setring builder
				StringBuilder ticket = new StringBuilder();


				
				//Aqui vi un problema y es que si agrega el text mesa tambien en el ticket agarra los productos no terminados asi que mejor los 
				//hagarramos desde la base de datos 
				double totalMesa=0.0;
				String sqlTotalMesa = "SELECT per.nombre, ped.cantidad, ped.producto, ped.precio "+
							 "FROM personas per "+
							 "JOIN pedidos ped ON per.idPersona = ped.idPersona " +
							 "WHERE per.idCuenta = ? "+
							 "ORDER BY per.idPersona ASC, ped.idPedido ASC";
				try(PreparedStatement psPed = con.prepareStatement(sqlTotalMesa)){
					psPed.setInt(1, idCuentaActiva);
					ResultSet rsPed=psPed.executeQuery();
					//Vamos a dividir el ticket en subcuentas para que sepa cuanto es para cada persona
					String personaActual="";
					double subtotalPersona=0.0;
					while(rsPed.next()) {
						String nombrePersona = rsPed.getString("nombre");
						int cant = rsPed.getInt("cantidad");
						String prod = rsPed.getString("producto");
						double precioTotal = rsPed.getDouble("precio");
						//Si el nombre actual no coincide con el nuevo entoces ya cambiamos de persona
						if(!nombrePersona.equals(personaActual)) {
							if(!personaActual.equals("")) {//Si no es el primer caso mandamos a imprimir
								ticket.append(String.format("     Subtotal %s: $%6.2f\n\n",personaActual,subtotalPersona));
							}
							ticket.append("--- ").append(nombrePersona).append(" ---\n");
							personaActual=nombrePersona;
							subtotalPersona=0.0;
						}
						
						if(prod.length()>20) {
							prod=prod.substring(0,18)+"..";//Si sobrepasa los 20 caracteres se escribe ..
						}
						ticket.append(String.format("%-3d %-20s $%6.2f\n", cant, prod, precioTotal));
						subtotalPersona+=precioTotal;
						totalMesa+=precioTotal;
					}
					if(!personaActual.equals("")) {//Si no es el primer caso mandamos a imprimir
						ticket.append(String.format("     Subtotal %s: $%6.2f\n\n",personaActual,subtotalPersona));
					}
				}		
				//Ya esta el ticket generado, ahora insertaremos los datos a la tabla finanzas 
				String sqlInsertFinanzas = "INSERT INTO finanzas (tipo,Categoria,monto,detalle) VALUES ('VENTA','COBRO LOCAL',?,?)";
				try(PreparedStatement psFinan = con.prepareStatement(sqlInsertFinanzas)){
					psFinan.setDouble(1, totalMesa);//Aqui iria insertado el total de la mesa, vamos a calcularlo antes desde la base de datos
					psFinan.setString(2, ticket.toString());
					psFinan.executeUpdate();
				}
				borrarCuentaActiva(numMesa,con);
				cambiarEstadoMesaLibre(numMesa,con);
				con.commit();
  
    			//llamamos a la clase nueva para generar su ticket
    			GeneradorTicket.generarTxtPedido(ticket.toString(), totalMesa);
				return true;
			}
			return false;
		}catch(Exception e1) {
			e1.printStackTrace();
			try {
			if(con!=null)con.rollback();//En caso de que hlgo falle se hace rollback
		}catch(SQLException ex2){}
		return false;
		}
		finally {
		try {if(con!=null) con.close();}catch(SQLException ex3){}//Si todo esta correcto se cierra la conexion
		}
	}
	
}
