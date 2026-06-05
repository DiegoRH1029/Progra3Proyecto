package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel VentanaPrincipal;
    
    // Declaramos ventanas y layouts
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JPanel panelMenu;
    
    // Tus Paneles del Sistema
    private PanelLogin panelLogin;
    private PanelBienvenida panelBien;
    private PanelMesas panelMesas;
    private PanelOrdenes panelOrden;
    private PanelPedidos panelPedidos;
    private PanelHistorial panelHistorial;

    // Botones globales
    private JButton btnOrdenes;
    private JButton btnPedidos;
    private JButton btnCorteDeCaja;
    private JButton btnHistorialDeVentas;
    private JButton btnGastos;
    private JButton btnTickets;
    private JButton btnSalir;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMain frame = new VentanaMain();
                    frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public VentanaMain() {
        setTitle("Sistema Taquería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 650);
        
        // --- CONFIGURACIÓN DEL PANEL PRINCIPAL (TEMA OSCURO) ---
        VentanaPrincipal = new JPanel();
        VentanaPrincipal.setBackground(new Color(30, 30, 30));
        VentanaPrincipal.setBorder(null);
        setContentPane(VentanaPrincipal);
        VentanaPrincipal.setLayout(new BorderLayout(0, 0));
        
        // --- ENCABEZADO SUPERIOR ---
        JLabel lblTituloArriba = new JLabel("TAQUERIA GON"); 
        lblTituloArriba.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloArriba.setFont(new Font("Impact", Font.PLAIN, 36));
        lblTituloArriba.setOpaque(true); // Para que se vea el fondo
        lblTituloArriba.setBackground(new Color(255, 193, 7)); // Amarillo Taquería
        lblTituloArriba.setForeground(new Color(211, 47, 47)); // Rojo Taquería
        lblTituloArriba.setBorder(new EmptyBorder(15, 0, 15, 0));
        VentanaPrincipal.add(lblTituloArriba, BorderLayout.NORTH);
        
        // --- CONTENEDOR CENTRAL (CARD LAYOUT) ---
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(new Color(30, 30, 30));
        VentanaPrincipal.add(panelContenedor, BorderLayout.CENTER);
        
        // --- MENÚ LATERAL IZQUIERDO ---
        panelMenu = new JPanel();
        panelMenu.setBackground(new Color(40, 40, 40));
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setBorder(new EmptyBorder(20, 10, 20, 10));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        VentanaPrincipal.add(panelMenu, BorderLayout.WEST);
        
        JLabel lblTituloMenu = new JLabel("MENÚ");
        lblTituloMenu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloMenu.setForeground(Color.white);
        lblTituloMenu.setAlignmentX(CENTER_ALIGNMENT);
        lblTituloMenu.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelMenu.add(lblTituloMenu);

        // =========================================================
        // INICIALIZACIÓN Y CONEXIÓN DE PANELES
        // =========================================================
        panelLogin = new PanelLogin();
        panelLogin.setVentanaMain(this);
        panelContenedor.add(panelLogin, "LOGIN");
        
        panelBien = new PanelBienvenida();
        panelBien.setVentanaMain(this);
        panelContenedor.add(panelBien, "BIENVENIDA");
        
        panelMesas = new PanelMesas();
        panelMesas.setVentanaMain(this);
        panelContenedor.add(panelMesas, "MESAS");
        
        panelOrden = new PanelOrdenes();
        panelOrden.setVentanaMain(this);
        panelContenedor.add(panelOrden, "ORDEN");
        
        panelPedidos= new PanelPedidos();
        panelPedidos.setVentanaMain(this);
        panelContenedor.add(panelPedidos, "PEDIDOS");
        
        panelHistorial= new PanelHistorial();
        panelHistorial.setVentanaMain(this);
        panelContenedor.add(panelHistorial, "HISTORIAL");
        
        // Estado inicial: Mostrar Login y ocultar el menú lateral
        cardLayout.show(panelContenedor, "LOGIN");
        panelMenu.setVisible(false);
        
        // =========================================================
        // CREACIÓN Y ESTILIZADO DE BOTONES DEL MENÚ
        // =========================================================
        btnOrdenes = new JButton("Órdenes / Mesas");
        estilizarBoton(btnOrdenes);
        panelMenu.add(btnOrdenes);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnPedidos = new JButton("Pedidos");
        estilizarBoton(btnPedidos);
        panelMenu.add(btnPedidos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnCorteDeCaja = new JButton("Corte de caja");
        estilizarBoton(btnCorteDeCaja);
        panelMenu.add(btnCorteDeCaja);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnHistorialDeVentas = new JButton("Historial");
        estilizarBoton(btnHistorialDeVentas);
        panelMenu.add(btnHistorialDeVentas);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnGastos = new JButton("Gastos");
        estilizarBoton(btnGastos);
        panelMenu.add(btnGastos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnTickets = new JButton("Tickets");
        estilizarBoton(btnTickets);
        panelMenu.add(btnTickets);
        panelMenu.add(Box.createVerticalGlue()); // Empuja el botón salir hacia abajo
        
        btnSalir = new JButton("Salir");
        estilizarBoton(btnSalir);
        panelMenu.add(btnSalir);
        
        // =========================================================
        // ACCIONES DE LOS BOTONES
        // =========================================================
        btnOrdenes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Al darle clic a Órdenes, el mesero debe ver la pantalla de mesas primero
                navegarA("MESAS");
            }
        });
        btnPedidos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		navegarA("PEDIDOS");
        	}
        });
        btnCorteDeCaja.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnHistorialDeVentas.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		navegarA("HISTORIAL");
        	}
        });
        btnGastos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnTickets.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnSalir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		panelMenu.setVisible(false);
        		navegarA("LOGIN");
        	}
        });

        // Aquí puedes agregar la acción para btnSalir (ej. System.exit(0) o regresar al Login)
    }
    	
    // =========================================================
    // MÉTODOS DE NAVEGACIÓN Y CONTROL
    // =========================================================
    
    //Metodo para cerrar programa
    public void closeProgram() {
    	System.exit(0);
    }
    // Método que llama el PanelLogin cuando las credenciales son correctas
	public void loginExitoso(String usuario, String rol) {
		
		panelBien.setTextUsuario(usuario);
		cardLayout.show(panelContenedor, "BIENVENIDA");
		panelMenu.setVisible(true);
		InventarioDB.cargarInventarioDB();
		
		if (rol.equalsIgnoreCase("trabajador")) {
			btnCorteDeCaja.setVisible(false);
			btnHistorialDeVentas.setVisible(false);
			btnGastos.setVisible(false);
			btnTickets.setVisible(false);
		}else if(rol.equalsIgnoreCase("administrador")) {
			btnCorteDeCaja.setVisible(true);
			btnHistorialDeVentas.setVisible(true);
			btnGastos.setVisible(true);
			btnTickets.setVisible(true);
		}
		
	}
	public void mostrarMenu(boolean mostrar) {
		panelMenu.setVisible(mostrar);
	}

    // Método genérico para movernos entre pantallas
    public void navegarA(String panel) {
    	
        cardLayout.show(panelContenedor, panel);
   }
    public void ordenando(int mesaSelect,boolean modoEdicion) {
    	Mesa mesa = ControladorMesa.getMesa(mesaSelect);
    	panelOrden.setMesa(mesa,modoEdicion);
    	navegarA("ORDEN");
    }
    
    public void actualizarPedidos() {
    	panelPedidos.cargarPedidos();
    }

    // =========================================================
    // MÉTODOS DE DISEÑO (UI/UX)
    // =========================================================
    
    private void estilizarBoton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(new Color(211, 47, 47)); // Fondo Rojo
        btn.setForeground(Color.white); // Letra Blanca
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto Hover: Cambiar color al pasar el mouse (Corregido 'mouseEntered')
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 193, 7)); // Cambia a Amarillo
                btn.setForeground(Color.black); // Letra Negra
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(211, 47, 47)); // Regresa a Rojo
                btn.setForeground(Color.white); // Regresa a Blanca
            }
        });
    }
    

}