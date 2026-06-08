//Clase que sera la ventana principal de nuestro programa
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
    
    // El cerebro visual: Declaramos como se van a apilar las pantallas
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JPanel panelMenu;
    
    // Todas las pantallas (paneles) que viven dentro de este motor principal
    private PanelLogin panelLogin;
    private PanelBienvenida panelBien;
    private PanelMesas panelMesas;
    private PanelOrdenes panelOrden;
    private PanelPedidos panelPedidos;
    private PanelHistorial panelHistorial;
    private PanelCorte panelCorte;
    private PanelTicket panelticket;
    private PanelGastos panelGastos;
    private PanelInventario panelInventario;

    // Botones globales del menu lateral
    private JButton btnOrdenes;
    private JButton btnPedidos;
    private JButton btnCorteDeCaja;
    private JButton btnHistorialDeVentas;
    private JButton btnGastos;
    private JButton btnTickets;
    private JButton btnInventario;
    private JButton btnSalir;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMain frame = new VentanaMain();
                    frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla automaticamente
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
        // Este hilo vivira por siempre en el fondo mientras el programa este abierto
        MonitorInventario monitor= new MonitorInventario();
        monitor.start();
        
        setTitle("Sistema Taquería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 650);
        
        // --- Configuracion del chasis del programa (Ventana principal) ---
        VentanaPrincipal = new JPanel();
        VentanaPrincipal.setBackground(new Color(30, 30, 30));
        VentanaPrincipal.setBorder(null);
        setContentPane(VentanaPrincipal);
        VentanaPrincipal.setLayout(new BorderLayout(0, 0));
        
        // --- ENCABEZADO SUPERIOR (El letrero que nunca desaparece) ---
        JLabel lblTituloArriba = new JLabel("TAQUERIA GON"); 
        lblTituloArriba.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloArriba.setFont(new Font("Impact", Font.PLAIN, 36));
        lblTituloArriba.setOpaque(true); // Para que se vea el fondo
        lblTituloArriba.setBackground(new Color(255, 193, 7)); // Amarillo Taqueria
        lblTituloArriba.setForeground(new Color(211, 47, 47)); // Rojo Taqueria
        lblTituloArriba.setBorder(new EmptyBorder(15, 0, 15, 0));
        VentanaPrincipal.add(lblTituloArriba, BorderLayout.NORTH);
        
        // --- CONTENEDOR CENTRAL (EL TRUCO DEL CARD LAYOUT) ---
        // CardLayout funciona como una baraja de cartas. Todas las pantallas existen al mismo tiempo,
        // pero estan empalmadas. Nosotros solo le decimos al layout cual "carta" poner hasta arriba.
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(new Color(30, 30, 30));
        VentanaPrincipal.add(panelContenedor, BorderLayout.CENTER);
        
        // --- MENU LATERAL IZQUIERDO ---
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
        // INICIALIZACION Y CONEXION DE PANELES (Inyeccion de dependencias)
        // =========================================================
        // Aqui instanciamos cada pantalla y le pasamos 'this' (esta misma VentanaMain).
        // Asi, los sub-paneles pueden gritarle a la ventana principal: "¡Oye, cambianos de pantalla!"
        
        panelLogin = new PanelLogin();
        panelLogin.setVentanaMain(this);
        panelContenedor.add(panelLogin, "LOGIN"); // Bautizamos la carta como "LOGIN"
        
        panelBien = new PanelBienvenida();
        panelBien.setVentanaMain(this);
        panelContenedor.add(panelBien, "BIENVENIDA");
        
        panelMesas = new PanelMesas();
        panelMesas.setVentanaMain(this);
        panelContenedor.add(panelMesas, "MESAS");
        
        panelOrden = new PanelOrdenes();
        panelOrden.setVentanaMain(this);
        panelContenedor.add(panelOrden, "ORDEN");
        
        // Nota: Tenias panelPedidos repetido dos veces, deje solo uno limpio
        panelPedidos= new PanelPedidos();
        panelPedidos.setVentanaMain(this);
        panelContenedor.add(panelPedidos, "PEDIDOS");
        
        panelInventario= new PanelInventario();
        panelInventario.setVentanaMain(this);
        panelContenedor.add(panelInventario, "INVENTARIO");
        
        panelCorte =new PanelCorte();
        panelContenedor.add(panelCorte, "CORTE");
        
        panelticket =new PanelTicket();
        panelContenedor.add(panelticket, "TICKET");
        
        panelHistorial= new PanelHistorial();
        panelContenedor.add(panelHistorial, "HISTORIAL");
        
        panelGastos = new PanelGastos();
        panelContenedor.add(panelGastos, "GASTOS");
        
        // Estado inicial: Mostramos la carta de LOGIN y escondemos el menu para que no hagan trampa
        cardLayout.show(panelContenedor, "LOGIN");
        panelMenu.setVisible(false);
        
        // =========================================================
        // CREACION Y ESTILIZADO DE BOTONES DEL MENU
        // =========================================================
        btnOrdenes = new JButton("Órdenes / Mesas");
        estilizarBoton(btnOrdenes);
        panelMenu.add(btnOrdenes);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciador invisible
        
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
        
        btnInventario = new JButton("Inventario");
        estilizarBoton(btnInventario);
        panelMenu.add(btnInventario);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnGastos = new JButton("Gastos");
        estilizarBoton(btnGastos);
        panelMenu.add(btnGastos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnTickets = new JButton("Tickets");
        estilizarBoton(btnTickets);
        panelMenu.add(btnTickets);
        
        panelMenu.add(Box.createVerticalGlue()); // Resorte que empuja el boton salir hasta abajo del todo
        
        btnSalir = new JButton("Salir");
        estilizarBoton(btnSalir);
        panelMenu.add(btnSalir);
        
        // =========================================================
        // ACCIONES DE LOS BOTONES (Los interruptores del CardLayout)
        // =========================================================
        btnOrdenes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Al darle clic a Ordenes, el mesero debe ver la pantalla de mesas primero
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
                navegarA("CORTE");
            }
        });
        
        btnHistorialDeVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Truco: Obligamos a la tabla a recargar datos frescos de MySQL justo antes de mostrarla
                panelHistorial.cargarDatosEnTabla();
                navegarA("HISTORIAL");
            }
        });
        
        btnGastos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navegarA("GASTOS");
            }
        });
        
        btnTickets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Refrescamos la lectura de la carpeta de Windows
                panelticket.cargarArchivosTickets();
                navegarA("TICKET");
            } 
        });
        
        btnInventario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelInventario.cargarInventarioEnTabla();
                mostrarMenu(false); // Atrapamos al usuario ocultando el menu lateral por seguridad
                navegarA("INVENTARIO");
            } 
        });
        
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelMenu.setVisible(false); // Apagamos el menu
                navegarA("LOGIN"); // Lo regresamos al principio
            }
        });

    }
        
    // =========================================================
    // METODOS DE NAVEGACION Y CONTROL (Las herramientas del motor)
    // =========================================================
    
    //Metodo para cerrar programa completamente
    public void closeProgram() {
        System.exit(0);
    }
    
    // EL CADENERO DEL ANTRO: Este metodo decide que botones puedes ver segun tu rol
    // Lo manda llamar el PanelLogin cuando adivinas la contrasena
    public void loginExitoso(String usuario, String rol) {
        
        panelBien.setTextUsuario(usuario);
        cardLayout.show(panelContenedor, "BIENVENIDA");
        panelMenu.setVisible(true); // Encendemos el menu lateral
        InventarioDB.cargarInventarioDB(); // Cargamos todo el stock a la memoria RAM a la velocidad de la luz
        
        // Bloqueo de seguridad: Si eres mesero/trabajador, te ocultamos el dinero
        if (rol.equalsIgnoreCase("trabajador")) {
            btnCorteDeCaja.setVisible(false);
            btnHistorialDeVentas.setVisible(false);
            btnGastos.setVisible(false);
            btnTickets.setVisible(false);
        }else if(rol.equalsIgnoreCase("administrador")) {
            // Si eres el jefe, prendemos todo
            btnCorteDeCaja.setVisible(true);
            btnHistorialDeVentas.setVisible(true);
            btnGastos.setVisible(true);
            btnTickets.setVisible(true);
        }
        
    }
    
    public void mostrarMenu(boolean mostrar) {
        panelMenu.setVisible(mostrar);
    }

    // Metodo generico que usan todos para cambiar la carta activa del CardLayout
    public void navegarA(String panel) {
        cardLayout.show(panelContenedor, panel);
   }
   
   // Puente de comunicacion: Le pasa los datos de la mesa elegida al panel de ordenes y luego cambia la pantalla
    public void ordenando(int mesaSelect,boolean modoEdicion) {
        Mesa mesa = ControladorMesa.getMesa(mesaSelect);
        panelOrden.setMesa(mesa,modoEdicion);
        navegarA("ORDEN");
    }
    
    public void actualizarPedidos() {
        panelPedidos.cargarPedidos();
    }

    // =========================================================
    // METODOS DE DISENO (UI/UX)
    // =========================================================
    
    private void estilizarBoton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Se estira a lo ancho del menu lateral
        btn.setBackground(new Color(211, 47, 47)); // Fondo Rojo
        btn.setForeground(Color.white); // Letra Blanca
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto Hover: Cambiar color al pasar el mouse
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 193, 7)); // Cambia a Amarillo brillante
                btn.setForeground(Color.black); // Letra Negra
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(211, 47, 47)); // Regresa a Rojo
                btn.setForeground(Color.white); // Regresa a Blanca
            }
        });
    }
    
}