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
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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

	/**
	 * Create the panel.
	 */
	public PanelMesas() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		//Codigo del panel derecho
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(350, 10));
		add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel labelMesaActual = new JLabel("New label");
		labelMesaActual.setBorder(new MatteBorder(6, 6, 5, 5, (Color) new Color(255, 0, 0)));
		labelMesaActual.setPreferredSize(new Dimension(300, 30));
		labelMesaActual.setMaximumSize(new Dimension(300, 100));
	
		labelMesaActual.setBackground(new Color(255, 0, 0));
		panel_1.add(labelMesaActual);
		
		JTextArea textMesa = new JTextArea();
		panel_1.add(textMesa);
		
		JButton btnNewOrden = new JButton("Generar orden ");

		panel_1.add(btnNewOrden);
		
		JButton btnEditarOrden = new JButton("Editar orden");
	
		panel_1.add(btnEditarOrden);
		
		JButton btnLiberarMesa = new JButton("Liberar Mesa");

		panel_1.add(btnLiberarMesa);
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
			panel.add(botones[i]);//Agregamos al panel
			
		}
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
		btnNewOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ventanaMain.navegarA("ORDEN");
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
				botones[i].setBackground(new Color(136,231,136)); //Verde
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
	//En este video seguiremos con el codigo de panel de mesas, mi objetivo es que traspasemos los datos de la base de datos a nuestro codigo
	//Hare una clase de mesas, le seguiremos en otro video
	
	//En este otro video seguiremos con lo de arriba; avanzamos bien, nos vemos en un rato:)
	
	//Ya terminamos parcialmente el panel de las mesas ahora seguiremos con el panel de generar orden, avanzamos mas o menos, ahora sigue toda la logica-< seguiremos
	//Siguiente video ----->
	

}
