package SistemaTaqueria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.border.EmptyBorder;

public class PanelCorte extends JPanel {
    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
    private JLabel lblVentasTotales;
    private JLabel lblGastosTotales;
    private JLabel lblGananciaNeta;
    private JLabel lblEfectivoEsperado;
    private JButton btnHacerCorte;
    private JButton btnCalcularDia;
    
    // Variables para guardar la matemática
    private double ventasDia = 0.0;
    private double gastosDia = 0.0;
    private double gananciaNeta = 0.0;

    public PanelCorte() {
        setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitulo = new JLabel("CORTE DE CAJA DIARIO", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(255, 193, 7));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(5, 1, 10, 10));
        panelCentro.setBackground(new Color(45, 45, 45));
        panelCentro.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblVentasTotales = crearLabelResumen("Ventas del Día (Ingresos): $ 0.00", new Color(46, 204, 113));
        lblGastosTotales = crearLabelResumen("Gastos Operativos (Retiros): $ 0.00", new Color(231, 76, 60));
        lblGananciaNeta = crearLabelResumen("Ganancia Neta: $ 0.00", Color.WHITE);
        lblEfectivoEsperado = crearLabelResumen("EFECTIVO ESPERADO EN CAJÓN: $ 0.00", new Color(255, 193, 7));

        panelCentro.add(lblVentasTotales);
        panelCentro.add(lblGastosTotales);
        panelCentro.add(new JSeparator()); 
        panelCentro.add(lblGananciaNeta);
        panelCentro.add(lblEfectivoEsperado);

        add(panelCentro, BorderLayout.CENTER);

        JPanel pnlSur = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlSur.setBackground(new Color(30, 30, 30));
        
        btnCalcularDia = new JButton("Calcular Totales de Hoy");
        btnCalcularDia.setBackground(new Color(46, 204, 113));
        btnCalcularDia.setForeground(Color.WHITE);
        btnCalcularDia.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        btnHacerCorte = new JButton("Imprimir Ticket de Corte (.TXT)");
        btnHacerCorte.setBackground(new Color(8, 51, 162));
        btnHacerCorte.setForeground(Color.WHITE);
        btnHacerCorte.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnHacerCorte.setEnabled(false); // Se activa hasta que se calcule el día
        
        pnlSur.add(btnCalcularDia);
        pnlSur.add(btnHacerCorte);

        add(pnlSur, BorderLayout.SOUTH);

        // ==========================================
        // ACCIÓN 1: CALCULAR LO DEL DÍA DESDE LA BD
        // ==========================================
        btnCalcularDia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularCorte();
            }
        });
        
        // ==========================================
        // ACCIÓN 2: GUARDAR EL TXT FÍSICO
        // ==========================================
        btnHacerCorte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarArchivoCorte();
            }
        });
    }

    private JLabel crearLabelResumen(String texto, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        return label;
    }

    // Método que jala los datos de MySQL filtrando por HOY
    private void calcularCorte() {
        ventasDia = 0.0;
        gastosDia = 0.0;
        
        try {
            Connection con = ConexionBD.obtenerConexion();
            // Traemos todo lo del día actual usando la fecha de la base de datos
            String sql = "SELECT tipo, monto FROM finanzas WHERE DATE(fecha) = CURDATE()";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                
                if (tipo.equals("VENTA")) {
                    ventasDia += monto;
                } else if (tipo.equals("GASTO")) {
                    gastosDia += Math.abs(monto); // Aseguramos que sea positivo para mostrarlo en pantalla
                }
            }
            con.close();
            
            // Matemática
            gananciaNeta = ventasDia - gastosDia;
            
            // Actualizar Interfaz Gráfica
            lblVentasTotales.setText("Ventas del Día (Ingresos): $ " + ventasDia);
            lblGastosTotales.setText("Gastos Operativos (Retiros): -$ " + gastosDia);
            lblGananciaNeta.setText("Ganancia Neta: $ " + gananciaNeta);
            lblEfectivoEsperado.setText("EFECTIVO ESPERADO EN CAJÓN: $ " + gananciaNeta);
            
            // Activar botón de imprimir
            btnHacerCorte.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Cálculos del día actualizados correctamente.");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al calcular el corte: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método que crea el archivo .txt (Obligatorio para la rúbrica)
    private void generarArchivoCorte() {
        File carpeta = new File("CortesCaja");
        if (!carpeta.exists()) carpeta.mkdirs();

        LocalDateTime ahora = LocalDateTime.now();
        String fechaFormat = ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nombreArchivo = "CortesCaja/Corte_" + fechaFormat + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("======================================");
            pw.println("         CORTE DE CAJA DIARIO         ");
            pw.println("======================================");
            pw.println("Fecha de Corte: " + ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            pw.println("--------------------------------------");
            pw.println("  Ventas Totales:    +$" + ventasDia);
            pw.println("  Gastos Operativos: -$" + gastosDia);
            pw.println("--------------------------------------");
            pw.println("  Ganancia Neta:      $" + gananciaNeta);
            pw.println("======================================");
            pw.println("TOTAL EFECTIVO EN CAJON: $" + gananciaNeta);
            pw.println("======================================");
            
            JOptionPane.showMessageDialog(null, "¡Corte generado exitosamente en la carpeta 'CortesCaja'!", "Corte Realizado", JOptionPane.INFORMATION_MESSAGE);
            btnHacerCorte.setEnabled(false); // Deshabilitar para no imprimir el mismo 2 veces
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el archivo .txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setVentanaMain() {
		this.ventanaMain=ventanaMain;
	}
}