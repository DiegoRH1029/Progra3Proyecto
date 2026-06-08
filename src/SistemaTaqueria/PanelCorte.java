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

// Esta clase es la que ven los administradores al final del día.
// Se encarga de sumar todo el dinero que entró y restarle lo que gastamos,
// para decirnos exactamente cuántos billetes debe haber en la caja registradora.
public class PanelCorte extends JPanel {
    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
    
    // Etiquetas donde vamos a mostrar los números mágicos
    private JLabel lblVentasTotales;
    private JLabel lblGastosTotales;
    private JLabel lblGananciaNeta;
    private JLabel lblEfectivoEsperado;
    
    // Botones de acción
    private JButton btnHacerCorte;
    private JButton btnCalcularDia;
    
    // Variables de memoria RAM para guardar la matemática y luego pasarla al .txt
    private double ventasDia = 0.0;
    private double gastosDia = 0.0;
    private double gananciaNeta = 0.0;

    public PanelCorte() {
    	// Fondo oscuro para que combine con el resto del sistema
        setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 50, 30, 50));

		// Título de la pantalla
        JLabel lblTitulo = new JLabel("CORTE DE CAJA DIARIO", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(255, 193, 7)); // Amarillo taquero
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTitulo, BorderLayout.NORTH);

		// Panel central donde van los números. Usamos GridLayout para que quede acomodado en forma de lista.
        JPanel panelCentro = new JPanel(new GridLayout(5, 1, 10, 10));
        panelCentro.setBackground(new Color(45, 45, 45));
        panelCentro.setBorder(new EmptyBorder(20, 20, 20, 20));

		// Inicializamos las etiquetas con 0 para que no se vea vacío al abrir la pestaña
        lblVentasTotales = crearLabelResumen("Ventas del Día (Ingresos): $ 0.00", new Color(46, 204, 113));
        lblGastosTotales = crearLabelResumen("Gastos Operativos (Retiros): $ 0.00", new Color(231, 76, 60));
        lblGananciaNeta = crearLabelResumen("Ganancia Neta: $ 0.00", Color.WHITE);
        lblEfectivoEsperado = crearLabelResumen("EFECTIVO ESPERADO EN CAJÓN: $ 0.00", new Color(255, 193, 7));

        panelCentro.add(lblVentasTotales);
        panelCentro.add(lblGastosTotales);
        panelCentro.add(new JSeparator()); // Una rayita divisora para separar totales
        panelCentro.add(lblGananciaNeta);
        panelCentro.add(lblEfectivoEsperado);

        add(panelCentro, BorderLayout.CENTER);

		// Panel de abajo para los botones
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
        
        // Lo apagamos al principio porque no tiene sentido imprimir un corte en 0
        btnHacerCorte.setEnabled(false); 
        
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

	// Método auxiliar (Factory Method) para no escribir lo mismo 4 veces al crear etiquetas
    private JLabel crearLabelResumen(String texto, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        return label;
    }

    // ================================================================
    // LOGICA PRINCIPAL: Método que jala los datos de MySQL filtrando por HOY
    // ================================================================
    private void calcularCorte() {
    	// Reiniciamos variables por si le dan clic varias veces al botón
        ventasDia = 0.0;
        gastosDia = 0.0;
        
        try {
            Connection con = ConexionBD.obtenerConexion();
            // ¡Esta consulta SQL es la clave!
            // CURDATE() es una función de MySQL que agarra la fecha de hoy.
            // Así ignoramos lo que se vendió ayer o la semana pasada, solo sacamos el corte del turno actual.
            String sql = "SELECT tipo, monto FROM finanzas WHERE DATE(fecha) = CURDATE()";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                
                // Clasificamos si el dinero entró o salió
                if (tipo.equals("VENTA")) {
                    ventasDia += monto;
                } else if (tipo.equals("GASTO")) {
                    gastosDia += Math.abs(monto); // Math.abs lo pasa a positivo por si se guardó con el signo '-'
                }
            }
            con.close();
            
            // Matemática fina
            gananciaNeta = ventasDia - gastosDia;
            
            // Actualizar Interfaz Gráfica con los resultados
            lblVentasTotales.setText("Ventas del Día (Ingresos): $ " + ventasDia);
            lblGastosTotales.setText("Gastos Operativos (Retiros): -$ " + gastosDia);
            lblGananciaNeta.setText("Ganancia Neta: $ " + gananciaNeta);
            lblEfectivoEsperado.setText("EFECTIVO ESPERADO EN CAJÓN: $ " + gananciaNeta);
            
            // Ya que hay números, prendemos el botón de generar ticket
            btnHacerCorte.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Cálculos del día actualizados correctamente.");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al calcular el corte: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // =========================================================================
    // Método que crea el archivo .txt para tener un comprobante físico o digital
    // =========================================================================
    private void generarArchivoCorte() {
    	// Revisamos si existe la carpeta
        File carpeta = new File("CortesCaja");
        if (!carpeta.exists()) carpeta.mkdirs();

		// Agregamos la fecha al nombre para que no se sobreescriban los cortes de distintos días
        LocalDateTime ahora = LocalDateTime.now();
        String fechaFormat = ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")); // Sin dos puntos por Windows
        String nombreArchivo = "CortesCaja/Corte_" + fechaFormat + ".txt";

		// El try-with-resources asegura que el archivo se cierre correctamente al terminar de escribir
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
            btnHacerCorte.setEnabled(false); // Deshabilitar para que el usuario no imprima 20 veces por accidente
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el archivo .txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método setter para poder interactuar con la ventana principal si fuera necesario
    public void setVentanaMain() {
		this.ventanaMain=ventanaMain;
	}
}