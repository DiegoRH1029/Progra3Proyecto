//Este panel mostrar la interfaz para hacer ya un pedido
//La forma de hacer un pedido consiste en:
//Cada mesa tiene varias ordenes, cada orden puede tener varias personas, y cada persona puede elegir multiples productos
// Y cada producto se divide en una variedad.

//Vamos a hacer un panel central como se ve en el diagrama, me apoyare de gemini para mas rapidez, ya que windows builder tiene a crashearme cuando estoy diseñando


package SistemaTaqueria;

import javax.swing.*;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelOrdenes extends JPanel {

    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
   
    // Grupos de botones para que solo se pueda seleccionar una opción por columna
    private ButtonGroup grupoCantidad;
    private ButtonGroup grupoProducto;
    private ButtonGroup grupoCarne;
    private ButtonGroup grupoConTodo;
    private JTextField textField;

    public PanelOrdenes() {
        // Layout principal de toda la ventana
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =========================================================
        // PANEL CENTRAL: LAS 4 COLUMNAS DE OPCIONES
        // =========================================================
        JPanel panelOpciones = new JPanel();
        // GridLayout(1, 4) significa: 1 fila, 4 columnas de tamaños iguales
        panelOpciones.setLayout(new GridLayout(1, 4, 10, 0)); 
        add(panelOpciones, BorderLayout.CENTER);

        // --- COLUMNA 1: CANTIDAD ---
        JPanel panelCantidad = new JPanel();
        panelCantidad.setBorder(new TitledBorder("Cantidad"));
        panelCantidad.setLayout(new BoxLayout(panelCantidad, BoxLayout.Y_AXIS));
        grupoCantidad = new ButtonGroup();
        
        // Agregamos un par de botones como ejemplo (puedes copiar y pegar más en el modo visual)
        
        JRadioButton rbCant1 = new JRadioButton("1");
        JRadioButton rbCant2 = new JRadioButton("2");
        JRadioButton rbCant3 = new JRadioButton("3");
        JRadioButton rbCant4 = new JRadioButton("4");
        JRadioButton rbCant5 = new JRadioButton("5");
        JRadioButton rbCant6 = new JRadioButton("6");
        JRadioButton rbCant7 = new JRadioButton("7");
        JRadioButton rbCant8 = new JRadioButton("8");
        JRadioButton rbCant9 = new JRadioButton("9");
        JRadioButton rbCant10 = new JRadioButton("10");
 
        grupoCantidad.add(rbCant1); grupoCantidad.add(rbCant2); grupoCantidad.add(rbCant3);
        grupoCantidad.add(rbCant4); grupoCantidad.add(rbCant5); grupoCantidad.add(rbCant6);
        grupoCantidad.add(rbCant7); grupoCantidad.add(rbCant8); grupoCantidad.add(rbCant9);grupoCantidad.add(rbCant10);
        panelCantidad.add(rbCant1); panelCantidad.add(rbCant2); panelCantidad.add(rbCant3);
        panelCantidad.add(rbCant4); panelCantidad.add(rbCant5); panelCantidad.add(rbCant6);
        panelCantidad.add(rbCant7); panelCantidad.add(rbCant8); panelCantidad.add(rbCant9);panelCantidad.add(rbCant10);
        
        JButton btnDecCant = new JButton("(-)");
        JButton btnIncCant = new JButton("(+)");
        
        panelCantidad.add(btnIncCant);
        
        JTextField textField = new JTextField();

        textField.setMaximumSize(new Dimension(100,20));
        textField.setColumns(10);
        panelCantidad.add(textField);
        
        panelCantidad.add(btnDecCant);
        
        // --- COLUMNA 2: PRODUCTO ---
        JPanel panelProducto = new JPanel();
        panelProducto.setBorder(new TitledBorder("Producto"));
        panelProducto.setLayout(new BoxLayout(panelProducto, BoxLayout.Y_AXIS));
        grupoProducto = new ButtonGroup();
        
        JRadioButton rbTacos = new JRadioButton("Tacos");
        JRadioButton rbTortas = new JRadioButton("Tortas");
        JRadioButton rbQuesadillas = new JRadioButton("Quesadillas");
        JRadioButton rbBurros = new JRadioButton("Burros");
        JRadioButton rbVolcanes = new JRadioButton("Volvanes");
        JRadioButton rbBurger = new JRadioButton("Hamburguesa");
        JRadioButton rbHotDogs = new JRadioButton("HotDogs");
        grupoProducto.add(rbTacos); grupoProducto.add(rbTortas); grupoProducto.add(rbQuesadillas);
        grupoProducto.add(rbBurros); grupoProducto.add(rbVolcanes); grupoProducto.add(rbBurger);
        grupoProducto.add(rbHotDogs); 
        panelProducto.add(rbTacos); panelProducto.add(rbTortas); panelProducto.add(rbQuesadillas);
        panelProducto.add(rbBurros); panelProducto.add(rbVolcanes); panelProducto.add(rbBurger);
        panelProducto.add(rbHotDogs); 

        // --- COLUMNA 3: CARNES ---
        JPanel panelCarne = new JPanel();
        panelCarne.setBorder(new TitledBorder("Carnes"));
        panelCarne.setLayout(new BoxLayout(panelCarne, BoxLayout.Y_AXIS));
        grupoCarne = new ButtonGroup();
        
        JRadioButton rbPastor = new JRadioButton("Pastor");
        JRadioButton rbBisteck = new JRadioButton("Bisteck");
        JRadioButton rbChorizo = new JRadioButton("Chorizo");
        JRadioButton rbBirria = new JRadioButton("Birria");
        JRadioButton rbLechon = new JRadioButton("Lechon");
    
        grupoCarne.add(rbPastor); grupoCarne.add(rbBisteck); grupoCarne.add(rbChorizo);
        panelCarne.add(rbPastor); panelCarne.add(rbBisteck); panelCarne.add(rbChorizo);
        grupoCarne.add(rbBirria); grupoCarne.add(rbLechon);
        panelCarne.add(rbBirria); panelCarne.add(rbLechon);

        // --- COLUMNA 4: EXTRAS ---
        //Habrea un grupo para con todo cilantro y cebolla
        //Con queso se toma de forma independiente y con aderezos igual
        JPanel panelExtras = new JPanel();
        panelExtras.setBorder(new TitledBorder("Extras"));
        panelExtras.setLayout(new BoxLayout(panelExtras, BoxLayout.Y_AXIS));
        grupoConTodo = new ButtonGroup();
        
        //Estos los haremos chek button
        //independientes
        JCheckBox chkConQueso = new JCheckBox("Con queso");
        JCheckBox chkSinAderezo = new JCheckBox("Sin aderezos");
        JCheckBox chkSinVerduras = new JCheckBox("Sin Verduras");
        //Grupo 
        JRadioButton rbConTodo = new JRadioButton("Con todo");
        JRadioButton rbSinVerdura = new JRadioButton("Sin Verdura");
        JRadioButton rbSinCebolla = new JRadioButton("Sin cebolla");
        JRadioButton rbSinCilantro = new JRadioButton("Sin cilantro");


        rbConTodo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbSinCebolla.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbSinCilantro.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        chkConQueso.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkSinAderezo.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkSinVerduras.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        grupoConTodo.add(rbConTodo); grupoConTodo.add(rbSinCebolla);
        grupoConTodo.add(rbSinCilantro);  grupoConTodo.add(rbSinVerdura);
        
  
        panelExtras.add(rbConTodo);panelExtras.add(rbSinVerdura);  panelExtras.add(rbSinCebolla);
        panelExtras.add(rbSinCilantro); panelExtras.add(chkConQueso); panelExtras.add(chkSinAderezo);
        panelExtras.add(chkSinVerduras);

        
        JLabel lblNotas = new JLabel("Notas Extras:");
        panelExtras.add(lblNotas);
        JTextField textExtras = new JTextField();

        textExtras.setMaximumSize(new Dimension(230,20));
        textExtras.setColumns(10);
        panelExtras.add(textExtras);
        
        panelCantidad.add(btnDecCant);
        // Añadimos las 4 columnas al panel central
        panelOpciones.add(panelCantidad);
        panelOpciones.add(panelProducto);
        panelOpciones.add(panelCarne);
        panelOpciones.add(panelExtras);


        // =========================================================
        // PANEL DERECHO: TICKET Y BOTONES DE ACCIÓN
        // =========================================================
        JPanel panelDerecho = new JPanel();
        panelDerecho.setPreferredSize(new Dimension(350, 0));
        panelDerecho.setLayout(new BorderLayout(0, 10));
        add(panelDerecho, BorderLayout.EAST);

        // Área de texto para el ticket
        JTextArea areaTicket = new JTextArea();
        areaTicket.setEditable(false); // Para que no escriban directo en él
        areaTicket.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Lo metemos en un JScrollPane por si la orden es muy larga y necesita barra de scroll
        JScrollPane scrollTicket = new JScrollPane(areaTicket);
        scrollTicket.setBorder(new TitledBorder("Aquí se muestra la orden actual"));
        panelDerecho.add(scrollTicket, BorderLayout.CENTER);

        // Sub-panel inferior para los botones de acción
        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setLayout(new GridLayout(4, 1, 0, 10)); // 4 botones apilados

        JButton btnTerminarPersona = new JButton("Terminar orden persona +");
        btnTerminarPersona.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        JButton btnAnadirProducto = new JButton("Añadir producto");
        btnAnadirProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        JButton btnFinalizar = new JButton("Finalizar pedido total");
        btnFinalizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });

        panelBotonesAccion.add(btnAnadirProducto);
        panelBotonesAccion.add(btnTerminarPersona);
        panelBotonesAccion.add(btnRegresar);
        panelBotonesAccion.add(btnFinalizar);

        panelDerecho.add(panelBotonesAccion, BorderLayout.SOUTH);
        //----------Acciones
        btnIncCant.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnDecCant.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        btnTerminarPersona.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnAnadirProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	
        	}
        });
   
        btnRegresar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnFinalizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
  
    }
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}
	public String obtenerSeleccion(ButtonGroup grupo) {
		//Recorremos nuuestro grupo de botones
		for(Enumeration<AbstractButton> botones = grupo.getElements(); botones.hasMoreElements();) {
			AbstractButton boton = botones.nextElement(); //Lo guardamos 
			if(boton.isSelected()) {//Preguntamos si esta seleccionado
				return boton.getText(); //Recibimos su texto 
			}
		}
		return "Nada";
	}

}