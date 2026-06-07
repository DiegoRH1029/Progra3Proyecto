package SistemaTaqueria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class GeneradorTicket {
	public static void generarTxtPedido(String detallePedido, double total) {
		File carpeta = new File("Tickets");
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}
		LocalDateTime ahora= LocalDateTime.now();//sacamos la hora exacta de la compu
		
		DateTimeFormatter formatoArchivo= DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		
		String nombreArchivo= "Tickets/Ticket_"+ahora.format(formatoArchivo)+".txt";
		
		try(PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))){
			pw.println("===============================");
			pw.println("          TAQUERIA GON         ");
			pw.println("          Desde 1985           ");
			pw.println("        Jesus Maria Ags.       ");
			pw.println("      Agustin Iturbide 108     ");
			pw.println("===============================");
			pw.println("Fecha y Hora: "+ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));
			pw.println("------------------------");
			pw.println("DETALLE DEL CONSUMO:  ");
			pw.println(detallePedido);
			pw.println("------------------------");
			pw.println("TOTAL A PAGAR: $"+total);
			pw.println("=========================");
			pw.println(" Gracias por su compra!  ");
			System.out.println("El archivo se guardo en: "+nombreArchivo);
		}catch (IOException e) {
			System.out.println("Hubo un error al crear el txt"+e.getMessage());
		}
		
		
	}

}
