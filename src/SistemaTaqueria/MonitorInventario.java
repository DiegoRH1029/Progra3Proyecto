package SistemaTaqueria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Esta clase se Extiende de Thread (Hilo).
// Es un proceso que corre "en segundo plano". 

public class MonitorInventario extends Thread {
	
	// Variables de control
    private boolean ejecutando= true; // Mientras sea true, el hilo sigue vivo dando vueltas
    private boolean alertaActiva=false; // Bandera logica que el menu puede consultar para prender un foquito rojo
    
    // Metodo para apagar el hilo de forma segura cuando cerramos el programa
    public void detener() {
        this.ejecutando=false;
    }
    
    public boolean isAlertaActiva() {
        return alertaActiva;
    }
    
    // Para apagar la campanita de alerta despues de que el gerente ya la vio
    public void limpiarAlerta() {
        this.alertaActiva=false;
    }
    
    // ===========================================================================
    // MÉTODO RUN(): Aqui vive el corazón del Hilo. Todo lo que esté aquí adentro
    // se ejecuta de forma independiente al resto de la aplicación.
    // ===========================================================================
    @Override
    public void run() {
        while (ejecutando) {
            try {
            	// 1. Mandamos a dormir al hilo por 15 segundos (15,000 milisegundos).
            	// Así no saturamos de consultas inútiles a MySQL a cada milisegundo.
                Thread.sleep(15000); 
                
                // 2. Cambio: vamos a imprimir los productos que estan bajos desde la base de datos
                String productosBajos=comprobarEnBaseDeDatos();
                
                // 3. Si hay productos bajos Y la alerta NO estaba activa ya (para no spamear ventanas)...
                if (!productosBajos.isEmpty() && !alertaActiva) {
                    alertaActiva=true;
                    
                    // ¡Ojo profe! Aquí apliqué manejo de concurrencia.
                    // Como los hilos no pueden tocar la interfaz gráfica (Swing) directamente sin causar bugs,
                    // uso invokeLater para decirle a Java: "Oye, cuando tengas tiempo libre en la pantalla principal, avienta este mensaje".
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null,"¡Tiene productos con bajo inventario! \n\n"+productosBajos+
                                                        "\nFavor de resurtir","Alerta stock",JOptionPane.WARNING_MESSAGE);
                            // Aqui informamos
                            // tendra que consultar isAlertaActiva() usando un timer en la ventana principal
                            System.out.println( "LOG INTERNO: Se detectó stock bajo: \n"+productosBajos);
                        }
                    });

                }
                // Si fueron a la tienda a comprar más cocas y ya no hay productos bajos, apagamos la alerta solita
                else if(productosBajos.isEmpty()){
                    alertaActiva=false;
                }
                
            } catch (InterruptedException e) {
            	// Esto se dispara si alguien fuerza la detención del hilo mientras estaba dormido
                System.out.println("Hilo de inventario detenido de forma segura");
            }
        }
    }
    
    // ===========================================================================
    // CONEXION A BD: Va y pregunta qué se está acabando
    // ===========================================================================
    private String comprobarEnBaseDeDatos() {
    	// StringBuilder es más rápido que andar concatenando con el '+'
        StringBuilder bajos =  new StringBuilder();
        
        // Buscaremos los productos que tengan stock menor o igual a 5 y que SÍ lleven control de stock (Refrescos, etc)
        String sql = "SELECT nombre, stock FROM inventario WHERE controla_stock=true AND stock<=5";
        
        try(Connection con=ConexionBD.obtenerConexion();
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()){
                    	
            while(rs.next()) {
                String nombre = rs.getString("nombre");
                int stockActual = rs.getInt("stock");
                // Armamos la lista renglón por renglón
                bajos.append(" - ").append(nombre).append(" (quedan: ").append(stockActual).append(")\n");
            }
            
        }catch(SQLException ex) {
            System.out.println("Error al leer stock en el hilo: "+ex.getMessage());
        }
        
        return bajos.toString();
    }
}