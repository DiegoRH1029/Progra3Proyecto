//Esta clase controlara los pedidos usando FIFO, se cargaran desde la base de datos los pedidos y tendra contorol para actualizar
//El chef tiene opcion de liberar pedido ya sea por mesa completa, por persona o por prpoducto individual (tambien en cascada)
package SistemaTaqueria;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class ControladorPedidos {
	//Metodo que devuelve una lista con todos los pedidos de la base de datos pendientes
	public static List<Object[]> obtenerPedidosPendientes(){
		List<Object[]> lista = new ArrayList <>();
		//Se extrae toda la informacion del pedido desde la base de datos
		String sql = "SELECT p.idPedido, m.idMesa, per.nombre, p.cantidad, p.producto,p.estado "+
					"From pedidos p "+
					"JOIN personas per ON p.idPersona = per.idPersona " +
					"JOIN cuentas c ON per.idCuenta = c.idCuenta "+
					"JOIN mesas m ON c.idMesa = m.idMesa "+
					"WHERE p.estado = 'Pendiente' "+
					"ORDER BY c.idCuenta ASC, p.idPedido ASC";
					try(Connection con = ConexionBD.obtenerConexion();
							PreparedStatement ps = con.prepareStatement(sql);
							ResultSet rs= ps.executeQuery()){
							while(rs.next()) {
								lista.add(new Object[]{
									rs.getInt("idPedido"),
									"Mesa "+rs.getInt("idMesa"),
									rs.getString("nombre"),
									rs.getInt("cantidad"),
									rs.getString("producto"),
									rs.getString("estado")
								});
							}
					}catch(SQLException ex) {
						ex.printStackTrace();
					}
					return lista;
	}
	//Metodo que libera todos los pedidos de una persona 
	//Haremos otros metodos, ya que es impractico estar liberando pedido por pedido
	//Haremos que se puedan liberar pedidos de una mesa o persona completa
	//Vamos a hacer una funcion para marcar el pedido como liberado
	public static void liberarPersonaCompleta(int numMesa, String nombrePersona) {
		try(Connection con = ConexionBD.obtenerConexion();){
			String sqlUpdate = "UPDATE pedidos p "+
					 "JOIN personas per ON p.idPersona = per.idPersona "+
					 "JOIN cuentas c ON per.idCuenta = c.idCuenta "+
					 "SET p.estado = 'Terminado' "+
					 "WHERE c.idMesa = ? AND per.nombre=? AND p.estado='Pendiente' ";
			try(PreparedStatement ps = con.prepareStatement(sqlUpdate)){
				ps.setInt(1, numMesa);
				ps.setString(2, nombrePersona);
				ps.executeUpdate();
			}
			//Checar si la mesa entera ya se acabo
			String sqlCheck = "SELECT COUNT(*) AS restantes FROM pedidos p "+
					 "JOIN personas per ON p.idPersona = per.idPersona "+
					 "JOIN cuentas c ON per.idCuenta = c.idCuenta "+
					 "WHERE c.idMesa = ? AND p.estado='Pendiente' ";
			try(PreparedStatement psCheck = con.prepareStatement(sqlCheck)){
				psCheck.setInt(1,numMesa);
				ResultSet rsCheck = psCheck.executeQuery();
				if(rsCheck.next()) {
					int restantes = rsCheck.getInt("restantes");
					if(restantes==0) {
						//Cambiamos el estado de la mesa si ya bo hay pendientes
						String sqMesa = "UPDATE mesas SET estado ='Atendida' WHERE idMesa = ?";
						try(PreparedStatement psMesa = con.prepareStatement(sqMesa)){
							psMesa.setInt(1, numMesa);
							psMesa.executeUpdate();
						}
						JOptionPane.showMessageDialog(null,"La mesa "+numMesa+" se atendio completamente","Succesfull",JOptionPane.INFORMATION_MESSAGE);
						PanelMesas.actualizarColores();
					}
				}
			}
			ControladorMesa.generarListaMesas();//Se actualiza memoria
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	//Metodo que libera una mesa completa 
	//haremos lo mismo pero para liberar mesa completa
	public static void liberarMesaCompleta(int numMesa) {
		try(Connection con = ConexionBD.obtenerConexion();){
			String sqlUpdate = "UPDATE pedidos p "+
					 "JOIN personas per ON p.idPersona = per.idPersona "+
					 "JOIN cuentas c ON per.idCuenta = c.idCuenta "+
					 "SET p.estado = 'Terminado' "+
					 "WHERE c.idMesa = ? AND p.estado='Pendiente' ";
			try(PreparedStatement ps = con.prepareStatement(sqlUpdate)){
				ps.setInt(1, numMesa);
				ps.executeUpdate();
			}
			//lista para liberar
			String sqMesa = "UPDATE mesas SET estado ='Atendida' WHERE idMesa = ?";
			try(PreparedStatement psMesa = con.prepareStatement(sqMesa)){
				psMesa.setInt(1, numMesa);
				psMesa.executeUpdate();
			}
			JOptionPane.showMessageDialog(null,"La mesa "+numMesa+" se atendio completamente","Succesfull",JOptionPane.INFORMATION_MESSAGE);
			PanelMesas.actualizarColores();
			ControladorMesa.generarListaMesas();//Se actualiza memoria
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	//Metodo que libera un pedido individualmente
	public static void liberarPedido(int idPedido,int numMesa) {
		try(Connection con = ConexionBD.obtenerConexion();){
			String sqlUpdatePed = "UPDATE pedidos SET estado = 'Terminado' WHERE idPedido = ?";
			//Aqui se intenta liberar el prodcuto individual
			try(PreparedStatement ps = con.prepareStatement(sqlUpdatePed)){
				ps.setInt(1, idPedido);
				ps.executeUpdate();
			}
			//Verifica que no quede ninun pedido en la mesa, si sya no hay se cambiara el estado de la mesa a "Atendida"
			String sqlCheck = "SELECT COUNT(*) AS restantes FROM pedidos p "+
							 "JOIN personas per ON p.idPersona = per.idPersona "+
							 "JOIN cuentas c ON per.idCuenta = c.idCuenta "+
							 "WHERE c.idMesa = ? AND p.estado='Pendiente' ";
			try(PreparedStatement psCheck = con.prepareStatement(sqlCheck)){
				psCheck.setInt(1,numMesa);
				ResultSet rsCheck = psCheck.executeQuery();
				if(rsCheck.next()) {
					int restantes = rsCheck.getInt("restantes");
					if(restantes==0) {
						//Cambiamos el estado de la mesa si ya bo hay pendientes
						String sqMesa = "UPDATE mesas SET estado ='Atendida' WHERE idMesa = ?";
						try(PreparedStatement psMesa = con.prepareStatement(sqMesa)){
							psMesa.setInt(1, numMesa);
							psMesa.executeUpdate();
						}
						JOptionPane.showMessageDialog(null,"La mesa "+numMesa+" se atendio completamente","Succesfull",JOptionPane.INFORMATION_MESSAGE);
						PanelMesas.actualizarColores();
					}
				}
			}
			ControladorMesa.generarListaMesas();//Despues decada pedido actualizamos las mesas 
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	//Metodo para reembolsar al inventario los pedidos que se liberaron y estaban pendeientes
	//ahora lo que falta es el caso de que se libere una mesa que aun no se atiende, entonces los pedidos se eliminan pero no se restauraria el stock
	//Por eso vamos a hacer un metodo que reembolse los pedidos que no se atendieron al stock
	public static void reembolsarPedPendientes(int numMesa,Connection con)throws SQLException {
		String sqlReem = "SELECT p.producto, p.cantidad FROM pedidos p "+
							  "JOIN personas per ON p.idPersona = per.idPersona "+
							  "JOIN cuentas c ON per.idCuenta = c.idCuenta "+
							  "WHERE c.idMesa = ? AND p.estado = 'Pendiente'";
		try(PreparedStatement psReem = con.prepareStatement(sqlReem)){
			psReem.setInt(1, numMesa);
			ResultSet rsReem = psReem.executeQuery();
			while(rsReem.next()) {
				String prodBD = rsReem.getString("producto");
				int cant = rsReem.getInt("cantidad");
				String nombreBase = InventarioDB.obtenerNombreBase(prodBD);
				InventarioDB.sumarStockMemoria(nombreBase, cant);
				InventarioDB.actualizarStockBD(nombreBase, con);
			}
			
		}
				
	}
	
}
