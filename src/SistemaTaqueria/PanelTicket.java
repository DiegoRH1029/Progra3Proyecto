package SistemaTaqueria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PanelTicket extends JPanel {
    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaTickets;
    private JTextArea txtVistaPrevia;
    private JButton btnActualizar;

    public PanelTicket() {
        setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // TÍTULO
        JLabel lblTitulo = new JLabel("VISOR DE TICKETS GUARDADOS", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(255, 193, 7)); // Amarillo
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        // PANEL CENTRAL DIVIDIDO EN DOS (Izquierda: Lista, Derecha: Lectura)
        JPanel panelCentro = new JPanel(new BorderLayout(15, 0));
        panelCentro.setBackground(new Color(30, 30, 30));

        // IZQUIERDA: LISTA DE ARCHIVOS .TXT
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.setBackground(new Color(30, 30, 30));
        panelIzquierdo.setPreferredSize(new Dimension(300, 0));
        
        modeloLista = new DefaultListModel<>();
        listaTickets = new JList<>(modeloLista);
        listaTickets.setBackground(new Color(45, 45, 45));
        listaTickets.setForeground(Color.WHITE);
        listaTickets.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaTickets.setSelectionBackground(new Color(46, 204, 113)); // Verde al seleccionar
        listaTickets.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollLista = new JScrollPane(listaTickets);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(new Color(30,30,30));
        TitledBorder bordeLista = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)), "Historial de Archivos .txt", 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 15), new Color(255, 193, 7));
            scrollLista.setBorder(BorderFactory.createCompoundBorder(bordeLista, new EmptyBorder(5, 5, 5, 5)));
            scrollLista.getViewport().setBackground(new Color(45, 45, 45));
        btnActualizar = new JButton("Actualizar Carpeta");
        btnActualizar.setBackground(new Color(8, 51, 162)); // Azul
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);
        panelIzquierdo.add(btnActualizar, BorderLayout.SOUTH);

        // DERECHA: VISTA PREVIA DEL TICKET
        txtVistaPrevia = new JTextArea();
        txtVistaPrevia.setEditable(false); // Para que no lo borren por accidente
        txtVistaPrevia.setBackground(new Color(255, 255, 240)); // Color papel ticket
        txtVistaPrevia.setForeground(Color.BLACK);
        txtVistaPrevia.setFont(new Font("Monospaced", Font.BOLD, 14)); // Fuente tipo impresora
        txtVistaPrevia.setBorder(new EmptyBorder(20, 20, 20, 20)); // Aire interior
        
        JScrollPane scrollVista = new JScrollPane(txtVistaPrevia);

        scrollVista.setBorder(null);
        TitledBorder bordeVista = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)), "Lectura del Ticket Seleccionado", 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 15), new Color(255, 193, 7));
            scrollVista.setBorder(BorderFactory.createCompoundBorder(bordeVista, new EmptyBorder(5, 5, 5, 5)));
        panelCentro.add(panelIzquierdo, BorderLayout.WEST);
        panelCentro.add(scrollVista, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // ==========================================
        // ACCIONES
        // ==========================================
        
        // 1. Botón para recargar los archivos de la carpeta
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarArchivosTickets();
            }
        });

        // 2. Al darle clic a un ticket en la lista, lo lee y lo muestra
        listaTickets.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && listaTickets.getSelectedValue() != null) {
                    leerTicketFisico(listaTickets.getSelectedValue());
                }
            }
        });

        // Cargar los archivos la primera vez que se abre la pantalla
        cargarArchivosTickets();
    }

    // Método para escanear la carpeta y listar los nombres
    public void cargarArchivosTickets() {
        modeloLista.clear();
        File carpeta = new File("Tickets");
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    if (f.getName().endsWith(".txt")) {
                        modeloLista.addElement(f.getName());
                    }
                }
            }
        }
        txtVistaPrevia.setText("Selecciona un ticket de la lista para leerlo...");
    }

    // Método para abrir el .txt y pegarlo en pantalla
    private void leerTicketFisico(String nombreArchivo) {
        File archivo = new File("Tickets/" + nombreArchivo);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            txtVistaPrevia.setText(""); // Limpiar pantalla
            String linea;
            while ((linea = br.readLine()) != null) {
                txtVistaPrevia.append(linea + "\n");
            }
        } catch (IOException ex) {
            txtVistaPrevia.setText("Error al intentar leer el archivo: " + nombreArchivo);
        }
    }

    public void setVentanaMain(VentanaMain ventanaMain) {
        this.ventanaMain = ventanaMain;
    }
}