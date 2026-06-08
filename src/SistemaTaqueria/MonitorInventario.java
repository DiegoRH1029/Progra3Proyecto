package SistemaTaqueria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MonitorInventario extends Thread {
	private boolean ejecutando= true;
	private boolean alertaActiva=false;//Bandera logica que el menu puede consultar
	public void detener() {
		this.ejecutando=false;
		
	}
	public boolean isAlertaActiva() {
		return alertaActiva;
	}
	public void limpiarAlerta() {
		this.alertaActiva=false;
		
	}
	@Override
	public void run() {
		while (ejecutando) {
			try {
				Thread.sleep(15000);//revisar cada 15 segundos
				
				//Cambio: vamos a imprimir los productos que estan bajos desde la base de datos
				String productosBajos=comprobarEnBaseDeDatos();
				
				if (!productosBajos.isEmpty() && !alertaActiva) {
					alertaActiva=true;
					SwingUtilities.invokeLater(new Runnable(){
						@Override
						public void run() {
							JOptionPane.showMessageDialog(null,"!Tiene productos con bajo inventario: \n\n"+productosBajos+
														"\nFavor de resurtir","Alerta stock",JOptionPane.WARNING_MESSAGE);
							//aqui informamos
							//tendra que consultar isAlertaActiva() usando un timer
							System.out.println( "LOG INTERNO: Se detecto stock bajo"+productosBajos);
						}
					});

				}
				else if(productosBajos.isEmpty()){
					alertaActiva=false;
				}
			} catch (InterruptedException e) {
				
				System.out.println("Hilo de inventario detenido de forma segura");
			}
		}
	}
	private String comprobarEnBaseDeDatos() {
		StringBuilder bajos =  new StringBuilder();
		//Buscaremos los prodcutos que tengan stock menor a 5 y que tengan control stock = true
		String sql = "SELECT nombre, stock FROM inventario WHERE controla_stock=true AND stock<=5";
		try(Connection con=ConexionBD.obtenerConexion();
					PreparedStatement ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				String nombre = rs.getString("nombre");
				int stockActual = rs.getInt("stock");
				bajos.append(" - ").append(nombre).append(" (quedan: ").append(stockActual).append(")\n");
			}
			
		}catch(SQLException ex) {
			System.out.println("Error al leer stock en el hilo"+ex.getMessage());
		}
		//Simulacion logica
		return bajos.toString();
	}
}
