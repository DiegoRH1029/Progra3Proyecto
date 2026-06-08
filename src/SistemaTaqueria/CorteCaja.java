//Esta clase suma todos loos gastos e ingresos de la base de datos de ese mismo dia y te imprime un resumen
package SistemaTaqueria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importado para arreglar lo de los nombres de archivo en Windows
import java.util.ArrayList;

import javax.swing.JOptionPane;

// Clase para hacer el corte de caja al final del turno.
// Calcula cuánto dinero debe haber en el cajón comparando las entradas (ventas) vs salidas (gastos).
public class CorteCaja {
	private double fondoInicial;
	private double ventasDelDiaString=0; // Guarda el total de ventas (es double aunque su nombre diga String jaja)
	private double gastosOperativos=0;   // Guarda el total de gastos que hicimos hoy
	
	// Constructor que recibe con cuánta feria empezamos en caja y todos los movimientos del día
	public CorteCaja(double fondoInicial, ArrayList<TransaccionFinanciera> transaccionesHoy) {
		this.fondoInicial = fondoInicial;
		procesarTransacciones(transaccionesHoy);
	}
	
	//separamos las ventanas de gastos matematicamente
	// Recorremos la lista para clasificar si fue entrada de dinero (VENTA) o salida (GASTO)
	private void procesarTransacciones(ArrayList<TransaccionFinanciera> transacciones) {
		for(TransaccionFinanciera t : transacciones) {
			if(t.getTipo().equalsIgnoreCase("VENTA")) {
				ventasDelDiaString += t.getMonto();
			}else if (t.getTipo().equalsIgnoreCase("GASTO")){
				gastosOperativos += Math.abs(t.getMonto());//lo pasamos a positivo para sumarlo a total de gastos
			}
		}
	}
	
	// Lo que ganamos libre (Ventas - Gastos)
	public double getGananciaNeta() {
		return ventasDelDiaString-gastosOperativos;
	}
	
	// Lo que debería haber físicamente en el cajón de dinero ahorita mismo sumando todo
	public double getEfectivoEsperado() {
		return fondoInicial+ ventasDelDiaString-gastosOperativos; 
	}
	
	// Función chida para ver si el cajero nos robó, le sobró feria o si cuadró todo perfecto
	public String auditarCaja(double efectivoContadoFisicamente) {
		double diferencia = efectivoContadoFisicamente-getEfectivoEsperado();
		if (diferencia==0)return "Corte Perfecto";
		if (diferencia>0) return "Sobrante de: $"+diferencia;
		return "Faltante de: $"+Math.abs(diferencia);
	}
	
	// Crea un archivo de texto con el resumen del corte para tenerlo de respaldo histórico
	public void generarTxtCorte(double efectivoContadoFisicamente) {
		File carpeta= new File("CortesCaja");
		if(!carpeta.exists()) carpeta.mkdir(); // Si la carpeta no existe, la creamos
		
		// Formateamos la hora para quitar los ":" porque Windows no deja guardar archivos con ese símbolo
		String fechaSegura = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		String nombreArchivo= "CortesCaja/Corte_"+fechaSegura+".txt";
		
		try(PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))){
			pw.println("=== CORTE DE CAJA: "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))+"===");
			pw.println("Fondo Inicial:    $"+fondoInicial);
			pw.println("Ventas del Dia:   $"+ventasDelDiaString);
			pw.println("Gastos Operativos:$"+gastosOperativos);
			pw.println("-------------------------------------");
			pw.println("Ganancia Neta:     $"+getGananciaNeta());
			pw.println("Efectivo Esperado: $"+getEfectivoEsperado());
			pw.println("Efectivo Contado : $"+efectivoContadoFisicamente);
			pw.println(">>>> "+auditarCaja(efectivoContadoFisicamente)+"<<<<<");
			JOptionPane.showMessageDialog(null, "Archivo de corte generado correctamente");	
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}