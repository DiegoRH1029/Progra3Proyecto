package SistemaTaqueria;

import javax.swing.JOptionPane;

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
				
				boolean stockBajoEnBD=comprobarEnBaseDeDatos();
				
				if (stockBajoEnBD && !alertaActiva) {
					alertaActiva=true;
					//aqui informamos
					//tendra que consultar isAlertaActiva() usando un timer
					System.out.println( "LOG INTERNO: Se detecto stock bajo. Cambiando estado de la alerta a TRUE");
				}
			} catch (InterruptedException e) {
				
				System.out.println("Hilo de inventario detenido de forma segura");
			}
		}
	}
	private boolean comprobarEnBaseDeDatos() {
		//Simulacion logica
		return Math.random()>0.1;
	}
}
