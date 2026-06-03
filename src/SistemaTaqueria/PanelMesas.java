package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

public class PanelMesas extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;
	private JButton[] botones=new JButton[12]; //Mejor uso un vector de botones ya que es fijo
	private int mesaSelect;
	private Color grisDark = new Color(30,30,30);
	private JButton btnNewOrden;
	private JButton btnLiberarMesa;
	private JButton btnEditarOrden;
	

	/**
	 * Create the panel.
	 */
	public PanelMesas() {
		
		setBackground(grisDark);
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(20,20,20,20));
		
		
		//Panel central (mesas)
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(grisDark);
		add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new GridLayout(4, 3, 15, 15));
		
		//Codigo del panel derecho
		//Panel este
		JPanel panelEast = new JPanel();
		panelEast.setBackground(new Color(45,45,45));
		panelEast.setPreferredSize(new Dimension(350, 0));
		panelEast.setBorder(new EmptyBorder(15,15,15,15));
		add(panelEast, BorderLayout.EAST);
		panelEast.setLayout(new BorderLayout(0, 15));
		
		//Titulo de la mesa actial
		JLabel labelMesaActual = new JLabel("");
		labelMesaActual.setForeground(new Color(255,193,7));
		labelMesaActual.setFont(new Font("Segoe UI", Font.BOLD,20));
		panelEast.add(labelMesaActual,BorderLayout.NORTH);
		
		//Codigo del panel central (Mesas)
		//Esto lo vamos a reducir para mayo comodidad;
		//Van a ser 12 botones
		
		for(int i=0; i<12; i++) {
			final int num = i+1; //Ya que ponemos una funcion dentro usamos final
			botones[i] = new JButton(""+num);
			botones[i].setBorderPainted(false);
			botones[i].setBackground(new Color(136,231,136)); //Ponermos color verde por defecto
			botones[i].setFont(new Font("Arial",Font.BOLD,16));
			
			botones[i].addActionListener(new ActionListener() { //Hacemos un action listener para cada boton, al fin y al cabo solo obtenemos su numero
				public void actionPerformed(ActionEvent e) {
					EstadoMesa estado = ControladorMesa.getEstadoMesa(num);
					mesaSelect =num;
					switch (estado) {
					case LIBRE:
						btnNewOrden.setVisible(true);
						btnEditarOrden.setVisible(false);
						btnLiberarMesa.setVisible(false);
						break;
					case ATENDIDA:
						btnNewOrden.setVisible(false);
						btnEditarOrden.setVisible(true);
						btnLiberarMesa.setVisible(true);
					case ESPERANDO:
						btnNewOrden.setVisible(false);
						btnEditarOrden.setVisible(true);
						btnLiberarMesa.setVisible(true);
					}
					labelMesaActual.setText("Mesa "+num);
					
					
				}
			});
			panelCenter.add(botones[i]);//Agregamos al panel
			
		}
		
	
		//Text area (donde se imprimira el ticket
		JTextArea textMesa = new JTextArea();
		textMesa.setEditable(false);
		textMesa.setBackground(new Color(60,60,60));
		textMesa.setForeground(Color.white);
		textMesa.setFont(new Font("Monospaced", Font.PLAIN,14));
		textMesa.setBorder(new EmptyBorder(10,10,10,10));
		panelEast.add(textMesa);
		
		JScrollPane scrollTicket = new JScrollPane(textMesa);
		scrollTicket.setBorder(null);
		panelEast.add(scrollTicket,BorderLayout.CENTER);
		
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(45,45,45));
		panelBotones.setLayout(new GridLayout(3,1,0,10));
		
		//Botones
		btnNewOrden = new JButton("Generar orden ");
		estilizarBotonAccion(btnNewOrden,new Color(46,204,113));
		
		btnEditarOrden = new JButton("Editar orden");
		estilizarBotonAccion(btnEditarOrden,new Color(255,193,7));
		btnEditarOrden.setForeground(Color.BLACK);
	
		
		btnLiberarMesa = new JButton("Liberar Mesa");
		estilizarBotonAccion(btnLiberarMesa,new Color(211,47,47));
		
		btnNewOrden.setVisible(false);
		btnEditarOrden.setVisible(false);
		btnLiberarMesa.setVisible(false);
		
		panelBotones.add(btnNewOrden);
		panelBotones.add(btnEditarOrden);
		panelBotones.add(btnLiberarMesa);
		
		//Añadimos el panel botones
		
		panelEast.add(panelBotones,BorderLayout.SOUTH);
		
	
		//Funcion para actualizar los colores
		
		actualizarColores();

		
		//----------------------ACCIONES 
		//No pude usar microfono, nos vemos en el siguiente video:)

		
		//Botones orden 
		btnEditarOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		btnLiberarMesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		//Boton generar nueva orden
		btnNewOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				ventanaMain.mostrarMenu(false);
				ventanaMain.ordenando(mesaSelect);
			}
		});


	}

	
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}
	
	public void actualizarColores() {
		List <Mesa> listaMesa = ControladorMesa.generarListaMesas(); //Sacamos la lista de las mesas de la base de datos
		int i=0;
		for(Mesa estaMesa : listaMesa) {
	
			switch (estaMesa.getEstadoMesa()) {
			case LIBRE:
				botones[i].setBackground(new Color(8,51,162)); //Azul rey
				break;
			case ESPERANDO: //Ya se ocupo y se tomo su pedido 
				botones[i].setBackground(new Color(255,255,0));//Amarillo
				break;
			case ATENDIDA: //Su pedido ya se despacho
				botones[i].setBackground(new Color(255,0,0)); //Rojo
			
			}
			i++;
		}
		
	
	}
	private void estilizarBotonAccion(JButton btn, Color colorFondo) {
        btn.setPreferredSize(new Dimension(100, 45));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
	//En este video seguiremos con el codigo de panel de mesas, mi objetivo es que traspasemos los datos de la base de datos a nuestro codigo
	//Hare una clase de mesas, le seguiremos en otro video
	
	//En este otro video seguiremos con lo de arriba; avanzamos bien, nos vemos en un rato:)
	
	//Ya terminamos parcialmente el panel de las mesas ahora seguiremos con el panel de generar orden, avanzamos mas o menos, ahora sigue toda la logica-< seguiremos
	//Siguiente video ----->
	

}
