package SistemaTaqueria;

import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CorteCaja {
	private double fondoInicial;
	private double ventasDelDiaString=0;
	private double gastosOperativos=0;
	public CorteCaja(double fondoInicial, ArrayList<TransaccionFinanciera> transaccionesHoy) {
		this.fondoInicial = fondoInicial;
		procesarTransacciones(transaccionesHoy);
		
	}
	//separamos las ventanas de gastos matematicamente
	private void procesarTransacciones(ArrayList<TransaccionFinanciera> transacciones) {
		for(TransaccionFinanciera t : transacciones) {
			if(t.getTipo().equalsIgnoreCase("VENTA")) {
				ventasDelDiaString += t.getMonto();
			}else if (t.getTipo().equalsIgnoreCase("GASTO")){
				gastosOperativos += Math.abs(t.getMonto());//lo pasamos a positivo para sumarlo a total de gastos
				
			}
		}
		
	}
	public double getGananciaNeta() {
		return ventasDelDiaString-gastosOperativos;
	}
	public double getEfectivoEsperado() {
		return fondoInicial+ ventasDelDiaString-gastosOperativos; 
	}
	public String auditarCaja(double efectivoContadoFisicamente) {
		double diferencia = efectivoContadoFisicamente-getEfectivoEsperado();
		if (diferencia==0)return "Corte Perfecto";
		if (diferencia>0) return "Sobrante de: $"+diferencia;
		return "Faltante de: $"+Math.abs(diferencia);
		
	}
	public void generarTxtCorte(double efectivoContadoFisicamente) {
		File carpeta= new File("CortesCaja");
		if(!carpeta.exists()) carpeta.mkdir();
		
		String nombreArchivo= "CortesCaja/Corte_"+LocalDateTime.now()+".txt";
		try(PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))){
			pw.println("=== CORTE DE CAJA: "+LocalDateTime.now()+"===");
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
