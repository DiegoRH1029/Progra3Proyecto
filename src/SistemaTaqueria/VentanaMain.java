package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel VentanaPrincipal;
	//Declaramos ventanas que usaremos
	private CardLayout cardLayout;
	private PanelLogin panelLogin;
	private PanelBienvenida panelBien;
	private PanelOrdenes panelOrdenes;
	
	private JPanel panelMenu;
	private JPanel panelContenedor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMain frame = new VentanaMain();
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 946, 605);
		VentanaPrincipal = new JPanel();
		VentanaPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(VentanaPrincipal);
		VentanaPrincipal.setLayout(new BorderLayout(0, 0));
		
		//Creamos nuestro objeto card layout
		cardLayout = new CardLayout();
		
		panelContenedor = new JPanel(cardLayout);
		VentanaPrincipal.add(panelContenedor, BorderLayout.CENTER);
		
		panelMenu = new JPanel();
		VentanaPrincipal.add(panelMenu, BorderLayout.WEST);
		panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
		
		//Creamos los objetos de nuestras ventanas y le mandamos la ventana principal para que tengan acceso a sus funciones
		panelLogin = new PanelLogin();
		panelLogin.setVentanaMain(this);
		panelContenedor.add(panelLogin,"LOGIN");
		
		
		panelBien = new PanelBienvenida();
		panelBien.setVentanaMain(this);
		panelContenedor.add(panelBien,"BIENVENIDA");
		
		panelOrdenes = new PanelOrdenes();
		panelOrdenes.setVentanaMain(this);
		panelContenedor.add(panelOrdenes,"ORDENES");
		
		
		//Mostrar el pane login en el card layout (contenedor
		
		cardLayout.show(panelContenedor, "LOGIN");
		panelMenu.setVisible(false);
		
		
		

		
		
		JLabel lblNewLabel_1 = new JLabel("Menu");
		lblNewLabel_1.setMaximumSize(new Dimension(150, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenu.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Ordenes");

		btnNewButton.setMaximumSize(new Dimension(150, 100));
		btnNewButton.setMinimumSize(new Dimension(150, 100));
		btnNewButton.setPreferredSize(new Dimension(100, 50));
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnNewButton);
		
		JButton btnCorteDeCaja = new JButton("Corte de caja");
		btnCorteDeCaja.setPreferredSize(new Dimension(100, 50));
		btnCorteDeCaja.setMinimumSize(new Dimension(150, 100));
		btnCorteDeCaja.setMaximumSize(new Dimension(150, 100));
		btnCorteDeCaja.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnCorteDeCaja);
		
		JButton btnHistorialDeVentas = new JButton("Historial");
		btnHistorialDeVentas.setPreferredSize(new Dimension(100, 50));
		btnHistorialDeVentas.setMinimumSize(new Dimension(150, 100));
		btnHistorialDeVentas.setMaximumSize(new Dimension(150, 100));
		btnHistorialDeVentas.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnHistorialDeVentas);
		
		JButton btnGastos = new JButton("Gastos");
		btnGastos.setPreferredSize(new Dimension(100, 50));
		btnGastos.setMinimumSize(new Dimension(150, 100));
		btnGastos.setMaximumSize(new Dimension(150, 100));
		btnGastos.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnGastos);
		
		JButton btnTickets = new JButton("Tickets");
		btnTickets.setPreferredSize(new Dimension(100, 50));
		btnTickets.setMinimumSize(new Dimension(150, 100));
		btnTickets.setMaximumSize(new Dimension(150, 100));
		btnTickets.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnTickets);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.setPreferredSize(new Dimension(100, 50));
		btnSalir.setMinimumSize(new Dimension(150, 100));
		btnSalir.setMaximumSize(new Dimension(150, 100));
		btnSalir.setHorizontalAlignment(SwingConstants.LEFT);
		panelMenu.add(btnSalir);
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		VentanaPrincipal.add(lblNewLabel, BorderLayout.NORTH);
		
		
		//------------acciones
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarA("ORDENES");
			}
		});

	}
	public void loginExitoso(String usuario) {
		panelBien.setTextUsuario(usuario);
		cardLayout.show(panelContenedor, "BIENVENIDA");
		panelMenu.setVisible(true);
	}
	
	public void navegarA(String panel) {
		cardLayout.show(panelContenedor,panel);
	}

}
