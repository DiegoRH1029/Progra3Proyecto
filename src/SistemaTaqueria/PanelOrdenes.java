package SistemaTaqueria;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class PanelOrdenes extends JPanel {

    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
   
    private static int contPer=0;
    private String nombreActual = "Persona "+(contPer+1);
    // Grupos de botones para que solo se pueda seleccionar una opción por columna
    private ButtonGroup grupoCantidad;
    private ButtonGroup grupoProducto;
    private ButtonGroup grupoCarne;
    private ButtonGroup grupoConTodo;
    private JRadioButton rbTacos;
    private JRadioButton rbCant1;
    private JRadioButton rbPastor;
    private JTextField textExtras;
    private JTextField textFieldCantidad;
    JScrollPane scrollTicket;
    private ArrayList<Producto> listaProductos;
    private Mesa mesa;
    private String numMesa="";
    private boolean esSnack;
    private boolean esBebida;
    private boolean cantText;
    private JComboBox<String> comboRefrescosGrande;
    private JComboBox<String> comboRefrescosChicos;
    private JComboBox<String> comboAguas1L;
    private JComboBox<String> comboAguasMedioL;
    private JRadioButton rbBurger;
    private JRadioButton rbHotDogs;
    private JCheckBox chkConQueso;
    private JCheckBox chkSinAderezo;
    private JCheckBox chkSinVerduras;
    //Lista de productos grafico
    private DefaultListModel<String> modeloTicket;
    private JList<String> listaTicket;
    //haremos un combobox para navegar entre personas
    private JComboBox<String> comboPersonas;
    private JButton btnEliminarProducto; //Por si se quiere eliminar producto seleccionado
    private boolean modoEdicion =false; //Bandera para controlar si estamos generando o modificando

    public PanelOrdenes() {
    	listaProductos = new ArrayList<>();
        // 1. FONDO PRINCIPAL OSCURO
        setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // =========================================================
        // PANEL CENTRAL: LAS 4 COLUMNAS DE OPCIONES
        // =========================================================
        JPanel panelOpciones = new JPanel();
        panelOpciones.setBackground(new Color(30, 30, 30));
        panelOpciones.setLayout(new GridLayout(1, 4, 15, 0)); 
        add(panelOpciones, BorderLayout.CENTER);

        // --- COLUMNA 1: CANTIDAD ---
        JPanel panelCantidad = new JPanel();
        panelCantidad.setBackground(new Color(45, 45, 45));
        panelCantidad.setBorder(crearBordeOscuro("Cantidad"));
        panelCantidad.setLayout(new BoxLayout(panelCantidad, BoxLayout.Y_AXIS));
        grupoCantidad = new ButtonGroup();
        
        rbCant1 = new JRadioButton("1");
        JRadioButton rbCant2 = new JRadioButton("2");
        JRadioButton rbCant3 = new JRadioButton("3");
        JRadioButton rbCant4 = new JRadioButton("4");
        JRadioButton rbCant5 = new JRadioButton("5");
        JRadioButton rbCant6 = new JRadioButton("6");
        JRadioButton rbCant7 = new JRadioButton("7");
        JRadioButton rbCant8 = new JRadioButton("8");
        JRadioButton rbCant9 = new JRadioButton("9");
        JRadioButton rbCant10 = new JRadioButton("10");
        
        rbCant1.setSelected(true); //Por defecto 1

        JRadioButton[] arrCantidades = {rbCant1, rbCant2, rbCant3, rbCant4, rbCant5, rbCant6, rbCant7, rbCant8, rbCant9, rbCant10};
        for (JRadioButton rb : arrCantidades) {
            estilizarBotonOpcion(rb);
            grupoCantidad.add(rb);
            panelCantidad.add(rb);
            
        }
  
        JButton btnIncCant = new JButton("(+) Aumentar");
        btnIncCant.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIncCant.setPreferredSize(new Dimension(200, 21));
        JButton btnDecCant = new JButton("(-) Disminuir");
        btnDecCant.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDecCant.setMaximumSize(new Dimension(700, 21));
        JButton btnLimpiar = new JButton("( ) Limpiar");
        btnLimpiar.setMaximumSize(new Dimension(700, 21));
        btnLimpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpiar.setPreferredSize(new Dimension(89, 21));
        btnLimpiar.setMinimumSize(new Dimension(89, 21));
        estilizarMiniBoton(btnIncCant, new Color(46, 204, 113));//Verde
        estilizarMiniBoton(btnDecCant, new Color(231, 76, 60));//Rojo
        estilizarMiniBoton(btnLimpiar, new Color(8, 51, 162));//Rojo
        
        textFieldCantidad = new JTextField();
        textFieldCantidad.setMinimumSize(new Dimension(700, 19));
        estilizarCampoTexto(textFieldCantidad);
        
        
        panelCantidad.add(Box.createVerticalStrut(10)); // Espacio
        panelCantidad.add(btnIncCant);
        panelCantidad.add(textFieldCantidad);
        panelCantidad.add(btnDecCant);
        panelCantidad.add(btnLimpiar);
   
        
        // --- COLUMNA 2: PRODUCTO ---
        JPanel panelProducto = new JPanel();
        panelProducto.setBackground(new Color(45, 45, 45));
        panelProducto.setBorder(crearBordeOscuro("Producto"));
        panelProducto.setLayout(new BoxLayout(panelProducto, BoxLayout.Y_AXIS));
        grupoProducto = new ButtonGroup();
        
        rbTacos = new JRadioButton("Tacos");
        JRadioButton rbTortas = new JRadioButton("Tortas");
        JRadioButton rbQuesadillas = new JRadioButton("Quesadillas");
        JRadioButton rbBurros = new JRadioButton("Burros");
        JRadioButton rbVolcanes = new JRadioButton("Volcanes");
        rbBurger = new JRadioButton("Hamburguesa");
        rbHotDogs = new JRadioButton("HotDogs");
        
        JRadioButton[] arrProductos = {rbTacos, rbTortas, rbQuesadillas, rbBurros, rbVolcanes, rbBurger, rbHotDogs};
        for (JRadioButton rb : arrProductos) {
            estilizarBotonOpcion(rb);
            grupoProducto.add(rb);
            panelProducto.add(rb);
        }
        
        //En producto añadiremos otras opciones para elegir bebidas (apenas se me ocurrio)
        JRadioButton rbRefrescoGrande= new JRadioButton("Refresco 600ml");
        JRadioButton rbRefrescoChico= new JRadioButton("Refresco 350ml");
        JRadioButton rbAguas1L = new JRadioButton("Aguas 1L");
        JRadioButton rbAguasMedioL = new JRadioButton("Aguas MedioL");
        grupoProducto.add(rbRefrescoGrande);
        grupoProducto.add(rbRefrescoChico);
        grupoProducto.add(rbAguas1L);
        grupoProducto.add(rbAguasMedioL);
        estilizarBotonOpcion(rbRefrescoGrande);
        estilizarBotonOpcion(rbRefrescoChico);
        estilizarBotonOpcion(rbAguas1L);
        estilizarBotonOpcion(rbAguasMedioL);
        //Declaramos sabores de refrescos
        String[] saboresRefrescosGrande = {"Coca","Coca Vidrio","Fanta","Sprite","Mundet"};
        String[] saboresAguas1L = {"Horchata","Jamaica","Mango"};
        String [] saboresRefrescosChico = {"Coca","Coca Vidrio","Fanta","Sprite","Mundet"};
        String[] saboresAguasMedioL = {"Horchata","Jamaica","Mango"};
        //Lista desplegable
        comboRefrescosGrande = new JComboBox<>(saboresRefrescosGrande);
        comboRefrescosGrande.setMaximumSize(new Dimension(700, 20));
        comboRefrescosChicos = new JComboBox<>(saboresRefrescosChico);
        comboRefrescosChicos.setAutoscrolls(true);
        comboRefrescosChicos.setMaximumSize(new Dimension(700, 20));
        comboAguas1L = new JComboBox<>(saboresAguas1L);
        comboAguas1L.setMaximumSize(new Dimension(700, 20));
        comboAguasMedioL = new JComboBox<>(saboresAguasMedioL);
        comboAguasMedioL.setMaximumSize(new Dimension(700, 20));
        //Las agregamos al panel de productos 
        //Las estilizamos
        comboRefrescosGrande.setBackground(new Color(60,60,60));
        comboRefrescosGrande.setForeground(Color.WHITE);
        comboRefrescosChicos.setBackground(new Color(60,60,60));
        comboRefrescosChicos.setForeground(Color.WHITE);
        comboAguas1L.setBackground(new Color(60,60,60));
        comboAguas1L.setForeground(Color.WHITE);
        comboAguasMedioL.setBackground(new Color(60,60,60));
        comboAguasMedioL.setForeground(Color.WHITE);
        JLabel lblBebidas = new JLabel("Bebidas:");
        lblBebidas.setForeground(new Color(255, 193, 7)); // Amarillo
        lblBebidas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBebidas.setBorder(new EmptyBorder(10, 0, 5, 0));
        panelProducto.add(lblBebidas);
        panelProducto.add(rbRefrescoGrande);
        panelProducto.add(comboRefrescosGrande);
        panelProducto.add(rbRefrescoChico);
        panelProducto.add(comboRefrescosChicos);
        panelProducto.add(rbAguas1L);
        panelProducto.add(comboAguas1L);
        panelProducto.add(rbAguasMedioL);
        panelProducto.add(comboAguasMedioL);
       
      
        // --- COLUMNA 3: CARNES ---
        JPanel panelCarne = new JPanel();
        panelCarne.setBackground(new Color(45, 45, 45));
        panelCarne.setBorder(crearBordeOscuro("Carnes"));
        panelCarne.setLayout(new BoxLayout(panelCarne, BoxLayout.Y_AXIS));
        grupoCarne = new ButtonGroup();
        
        rbPastor = new JRadioButton("Pastor");
        JRadioButton rbBisteck = new JRadioButton("Bisteck");
        JRadioButton rbChorizo = new JRadioButton("Chorizo");
        JRadioButton rbBirria = new JRadioButton("Birria");
        JRadioButton rbLechon = new JRadioButton("Lechon");
    
        rbPastor.setSelected(true); //Por defecto pastor 
        JRadioButton[] arrCarnes = {rbPastor, rbBisteck, rbChorizo, rbBirria, rbLechon};
        for (JRadioButton rb : arrCarnes) {
            estilizarBotonOpcion(rb);
            grupoCarne.add(rb);
            panelCarne.add(rb);
        }

        // --- COLUMNA 4: EXTRAS ---
        JPanel panelExtras = new JPanel();
        panelExtras.setBackground(new Color(45, 45, 45));
        panelExtras.setBorder(crearBordeOscuro("Extras"));
        panelExtras.setLayout(new BoxLayout(panelExtras, BoxLayout.Y_AXIS));
        grupoConTodo = new ButtonGroup();
        
        // Independientes (CheckBoxes)
        chkConQueso = new JCheckBox("Con queso");
        chkSinAderezo = new JCheckBox("Sin aderezos");
        chkSinVerduras = new JCheckBox("Sin Verduras");
        
        // Grupo de Verduras (RadioButtons)
        JRadioButton rbConTodo = new JRadioButton("Con todo");
        JRadioButton rbSinVerdura = new JRadioButton("Sin Verdura"); // Considera quitar este si ya usas chkSinVerduras
        JRadioButton rbSinCebolla = new JRadioButton("Sin cebolla");
        JRadioButton rbSinCilantro = new JRadioButton("Sin cilantro");

        AbstractButton[] arrExtras = {rbConTodo, rbSinVerdura, rbSinCebolla, rbSinCilantro, chkConQueso, chkSinAderezo, chkSinVerduras};
        
        for (AbstractButton btn : arrExtras) {
            estilizarBotonOpcion(btn);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (btn instanceof JRadioButton) {
                grupoConTodo.add(btn);
            }
            panelExtras.add(btn);
        }

        JLabel lblNotas = new JLabel("Notas Extras:");
        lblNotas.setForeground(new Color(255, 193, 7)); // Amarillo
        lblNotas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNotas.setBorder(new EmptyBorder(10, 0, 5, 0));
        panelExtras.add(lblNotas);
        
        textExtras = new JTextField();
        estilizarCampoTexto(textExtras);
        panelExtras.add(textExtras);

        // Añadimos las 4 columnas al panel central
        panelOpciones.add(panelCantidad);
        panelOpciones.add(panelProducto);
        panelOpciones.add(panelCarne);
        panelOpciones.add(panelExtras);


        // =========================================================
        // PANEL DERECHO: TICKET Y BOTONES DE ACCIÓN
        // =========================================================
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(new Color(30, 30, 30));
        panelDerecho.setPreferredSize(new Dimension(400, 0));
        panelDerecho.setLayout(new BorderLayout(0, 15));
        add(panelDerecho, BorderLayout.EAST);


               
        // Área de texto para el ticket
        //Reemplazamos al text area por una lista
        comboPersonas = new JComboBox<>();
        comboPersonas.setBackground(new Color(60,60,60));
        comboPersonas.setForeground(Color.WHITE);
        comboPersonas.setFont(new Font("Segoe UI", Font.BOLD,14));
        //añadimos la persona 
        comboPersonas.addItem(nombreActual);
        panelDerecho.add(comboPersonas,BorderLayout.NORTH);
        //ahora la lista
        modeloTicket = new DefaultListModel<>();
        listaTicket = new JList<>(modeloTicket);
        listaTicket.setBackground(new Color(60,60,60));
        listaTicket.setForeground(Color.WHITE);
        listaTicket.setFont(new Font("Monospaced", Font.PLAIN,15));
        listaTicket.setSelectionBackground(new Color(8,51,161));//azul cuando se selecciona
        listaTicket.setSelectionForeground(Color.WHITE);

        scrollTicket = new JScrollPane(listaTicket);
        scrollTicket.setBorder(crearBordeOscuro(nombreActual));
        scrollTicket.getViewport().setBackground(new Color(45, 45, 45)); // Quita bordes blancos del scroll
        panelDerecho.add(scrollTicket, BorderLayout.CENTER);
        
        // Sub-panel inferior para los botones de acción
        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setBackground(new Color(30, 30, 30));
        panelBotonesAccion.setLayout(new GridLayout(4, 2, 10, 10)); 

        btnEliminarProducto = new JButton("Eliminar seleccion");
        JButton btnActualizarProducto = new JButton("Actualizar seleccion");
        JButton btnAnadirProducto = new JButton("Añadir Producto");
        JButton btnTerminarPersona = new JButton("Terminar orden persona +");
        JButton btnRegresar = new JButton("Regresar a Mesas");
        JButton btnFinalizar = new JButton("Finalizar Pedido Total");
        JButton editarNombre = new JButton("Editar nombre");

        // Pintamos los botones con la misma lógica que en mesas
        estilizarBoton(btnAnadirProducto, new Color(46, 204, 113), Color.WHITE); // Verde
        estilizarBoton(btnTerminarPersona, new Color(255, 193, 7), Color.BLACK); // Amarillo
        estilizarBoton(btnRegresar, new Color(100, 100, 100), Color.WHITE);      // Gris
        estilizarBoton(btnFinalizar, new Color(211, 47, 47), Color.WHITE);       // Rojo
        estilizarBoton(editarNombre, new Color(8,51,162), Color.WHITE);       // azul
        estilizarBoton(btnEliminarProducto,new Color(232, 75, 60), Color.WHITE); //Rojo mas oscuro
        estilizarBoton(btnActualizarProducto,new Color(20, 200, 100), Color.WHITE); //verde?
        
        panelBotonesAccion.add(btnAnadirProducto);
        panelBotonesAccion.add(btnActualizarProducto);
        panelBotonesAccion.add(btnEliminarProducto);
        panelBotonesAccion.add(editarNombre);
        panelBotonesAccion.add(btnTerminarPersona);
        panelBotonesAccion.add(btnFinalizar);
        panelBotonesAccion.add(btnRegresar);
 
        
        panelDerecho.add(panelBotonesAccion, BorderLayout.SOUTH);
        
        // =========================================================
        // ACCIONES
        // =========================================================
        btnIncCant.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!textFieldCantidad.getText().equals("")) {
	        		int cant = Integer.parseInt(textFieldCantidad.getText());
	        		if(cant<99) {
	        			cant++;
	        			textFieldCantidad.setText(""+cant);
	        		}
        		}
        	}
        });

        btnDecCant.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!textFieldCantidad.getText().equals("")) {
	        		int cant = Integer.parseInt(textFieldCantidad.getText());
	        		if(cant>1) {
	        			cant--;
	        			textFieldCantidad.setText(""+cant);
	        		}
        		}
        	}
        });
        btnLimpiar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textFieldCantidad.setText("");
        		rbCant1.setEnabled(true);
        		rbCant1.setSelected(true);
        		rbCant1.doClick();
        		
        	}
        });
        //Boton para regresar a panel de mesas falta: validacion de seguro que quiere salir
        //Meteremos valicaciones al boton regresar (en caso de que apriete regresar en medio de un pedido)
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	boolean hayCambios = (mesa!=null&&!mesa.getPersonas().isEmpty())||!listaProductos.isEmpty();
            	if(hayCambios) {
            		int opcion = JOptionPane.showConfirmDialog(null, 
            				"Tienes una orden en prograso\n, Desea guardar los cambios?","Guardar cambios",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            		if(opcion == JOptionPane.YES_OPTION) {
            			//Si hay productos pero no le dio a terminar persona lo hacemos por ellos
            			if(!listaProductos.isEmpty()) {
            				btnTerminarPersona.doClick();
            			}
            			btnFinalizar.doClick();
            		}else if(opcion==JOptionPane.NO_OPTION) {
            			if(ventanaMain!=null) {
            				limpiarTodo();
            				//Si no hacen cambios volvemos a restaurar los valores originales desde la base de datos 
            				ControladorMesa.generarListaMesas();
            				ventanaMain.mostrarMenu(true);
            				ventanaMain.navegarA("MESAS");
            				
            			}
            		}
            		//Si eligen cancelar o cerrar la tacha el oanel no hace nada
            	}else {
        			if(ventanaMain!=null) {
        				limpiarTodo();
        				ventanaMain.mostrarMenu(true);
        				ventanaMain.navegarA("MESAS");
        			}
            	
            	}
            }
        });
        //Finaliza la orden total de la mesa
        //Cuando se finaliza el pedido total es cuando se actualiza la base de datos 
        //Agarraremos a todas las personas de la mesa, y llenaremos todas las tablas de base de datos con la nueva orden y actualizaremos el inventario
        //Añadimos de que en modo edicion se actualicen las tablas (o borre completamente las anteriores y metamos nuevas)
        btnFinalizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Verificamos que la mesa no este vacia o que no tenga personas
        		if(mesa == null|| mesa.getPersonas().isEmpty()) {
        			JOptionPane.showMessageDialog(null, "No existen mesas o no hay personas", "!", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		Connection con=null;
        		int idCuentaActiva = 0; //Aqui se guarda el turno
        		try {
        			con=ConexionBD.obtenerConexion();
        			con.setAutoCommit(false);//Esto es para que solo se actualice al final
        			//Para mantener el orden de llegada mejor usaremos el id original de la mesa
       
        			//Si esta en modo edicion se borra la cuenta anterior de la base de datos antes de añadir la nueva orden
        			//Para que no te vuelvan a aparecer productos ya terminados en caso de que editen la orden haremos lo siguiente
        			HashMap<String,String> estadosViejos= new HashMap<>();
        			if(modoEdicion) {
        				//buscaremos el ide de la cuenta a borrar
        				String sqlGetCuenta="SELECT idCuenta FROM cuentas WHERE idMesa=?";
        				PreparedStatement psGet = con.prepareStatement(sqlGetCuenta);
        				psGet.setInt(1, mesa.getNumMesa());
        				ResultSet rsGet = psGet.executeQuery();
        				if(rsGet.next()) {
        					idCuentaActiva = rsGet.getInt("idCuenta");
        					//Aqui se recupera el stock antes de borrar
        					//borramos los pedidos
        					String sqlRecuperar = "SELECT producto, cantidad, estado FROM pedidos WHERE idPersona IN(SELECT idPersona FROM personas WHERE idCuenta=?)";
        					PreparedStatement psRec = con.prepareStatement(sqlRecuperar);
        					psRec.setInt(1, idCuentaActiva);
        					ResultSet rsRec = psRec.executeQuery();
        					while(rsRec.next()) {
        						String prodDB = rsRec.getString("producto");
        						int cantVieja = rsRec.getInt("cantidad");

        						String estadoDB = rsRec.getString("estado");//Leemos el estado
        						estadosViejos.put(prodDB, estadoDB);//Reespaldamos los estados
        						String nombreBase = InventarioDB.obtenerNombreBase(prodDB);
        						InventarioDB.sumarStockMemoria(nombreBase, cantVieja);
        					}
        					//Borramos los pedidos y personas
        					String sqlDeletePedidos = "DELETE FROM pedidos WHERE idPersona IN(SELECT idPersona FROM personas WHERE idcuenta=?)";
        					PreparedStatement psDelPed = con.prepareStatement(sqlDeletePedidos);
        					psDelPed.setInt(1, idCuentaActiva);
        					psDelPed.executeUpdate();
        					//borramos las personas
        					String sqlDeletePersonas = "DELETE FROM personas WHERE idcuenta=?";
        					PreparedStatement psDelPer= con.prepareStatement(sqlDeletePersonas);
        					psDelPer.setInt(1, idCuentaActiva);
        					psDelPer.executeUpdate();
    
        				}
        			}else {
        				//Aqui se inicia un nuevo guardado de la orden con la tabla ya limpia
        			//Creamos una cuenta
	        			String sqlCuenta = "INSERT INTO cuentas (idMesa) VALUES(?)";
	        			PreparedStatement psCuenta =  con.prepareStatement(sqlCuenta,java.sql.Statement.RETURN_GENERATED_KEYS);
	        			psCuenta.setInt(1, mesa.getNumMesa());
	        			psCuenta.executeUpdate();
	        			
	        			ResultSet rsCuenta= psCuenta.getGeneratedKeys();
	        			if(rsCuenta.next())idCuentaActiva=rsCuenta.getInt(1);
	        			
        			}
        			//Aqui se guarda la orden sea nueva o editada
        			String sqlPersona="INSERT INTO personas (idCuenta,nombre) VALUES(?,?)";
        			PreparedStatement psPersona =  con.prepareStatement(sqlPersona,java.sql.Statement.RETURN_GENERATED_KEYS);
        			
        			String sqlPedido = "INSERT INTO pedidos (idPersona,cantidad, producto,precio,estado) VALUES (?,?,?,?,?)";
        			PreparedStatement psPedido =  con.prepareStatement(sqlPedido);
        			
        			//Personas y productos 
        			for(Persona p: mesa.getPersonas()) {
        				psPersona.setInt(1, idCuentaActiva);
        				psPersona.setString(2, p.getNombre());
        				psPersona.executeUpdate();
        				
        				ResultSet rsPersona = psPersona.getGeneratedKeys();
        				int idPersona=0;
        				if(rsPersona.next()) idPersona=rsPersona.getInt(1);
        				
        				for(Producto prod:p.getListaProductos()) {
        					psPedido.setInt(1, idPersona);
        					psPedido.setInt(2, prod.getCant());
        					psPedido.setString(3, prod.toString());
        					psPedido.setDouble(4, prod.getPrecioTotal());
        					//Aqui restauramos el estado o lo ponemos como pendiente
        					String estadoFinal = "Pendiente";//por defecto
        					if(modoEdicion&&estadosViejos.containsKey(prod.toString())) {
        						estadoFinal = estadosViejos.get(prod.toString());
        					}
        					psPedido.setString(5, estadoFinal);
        					psPedido.executeUpdate();
        					//Aqui restamos el nuevo stock en la memoria y lo guardamos en la base de datos
        					if(estadoFinal=="Pendiente") {
            					String nombreBase=InventarioDB.obtenerNombreBase(prod.toString());
            					InventarioDB.restarStockMemoria(nombreBase, prod.getCant());
            					InventarioDB.actualizarStockBD(nombreBase, con);
        					}
        				}
        			}
        			//Actualizamos el estado de la mesa despues de guardar la orden
        			String sqlEstado = "UPDATE mesas SET estado = 'Esperando' WHERE idMesa = ?";
        			PreparedStatement psEstado =  con.prepareStatement(sqlEstado);
        			psEstado.setInt(1, mesa.getNumMesa());
        			psEstado.executeUpdate();
        			//Confirmamos la actualizacion
        			con.commit();
        			JOptionPane.showMessageDialog(null, "Orden de mesa"+mesa.getNumMesa()+" guardada con exito!","Exito", JOptionPane.INFORMATION_MESSAGE);
        			
        			limpiarTodo();
        			
        			//Nos regresamos a mesas
        			if(ventanaMain!=null) {
        				ventanaMain.mostrarMenu(true);
        				PanelMesas.actualizarColores();
        				ventanaMain.actualizarPedidos();
        				ventanaMain.navegarA("MESAS");
        			}
        		}catch(SQLException ex1) {
        			ex1.printStackTrace();
        			//en caso de que falle cancelamos la conexion y hacemos rollback
        			try {
        				if(con!=null) con.rollback();
        				
        			}catch(SQLException ex2) {
        			}
        			JOptionPane.showMessageDialog(null, "Ocurrio un error, base de datos restaurada", "Error", JOptionPane.INFORMATION_MESSAGE);
        		}finally {
        			try {
        				if(con!=null)con.close();
        			}catch(SQLException ex3) {}
        		}
        	}
        });
        
        //Añade producto a la pila de productos de la persona
        btnAnadirProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String prodStr="";
        		String canStr ="";
        		if(textFieldCantidad.getText().equals("")) {
        			canStr = obtenerSeleccion(grupoCantidad);
        		}else canStr=textFieldCantidad.getText();
   
				if(esBebida) {
        			
        			String bebida="";
        			if(rbRefrescoGrande.isSelected())bebida=(String)comboRefrescosGrande.getSelectedItem()+ " 600ml";
        			else if(rbRefrescoChico.isSelected())bebida=(String)comboRefrescosChicos.getSelectedItem()+ " 350ml";
        			else if(rbAguas1L.isSelected())bebida = (String)comboAguas1L.getSelectedItem() +" 1L";
        			else if(rbAguasMedioL.isSelected())bebida=(String)comboAguasMedioL.getSelectedItem()+" medioL";
        			prodStr = bebida;
        		}
        		else {
        			prodStr = obtenerSeleccion(grupoProducto);
        		}
        		int cant = Integer.parseInt(canStr);
        		double precio = InventarioDB.getPrecio(prodStr);
        		boolean conQueso = chkConQueso.isSelected();
        		if((prodStr.equals("Torta")||prodStr.equals("Tacos"))&&conQueso) precio+=InventarioDB.getPrecio("Queso");
	        	if((InventarioDB.estaDisponible(prodStr)&&InventarioDB.validarStockMemoria(prodStr, cant))) {
	        		if(esBebida) {
	
	        			Bebida b = new Bebida(obtenerSeleccion(grupoProducto),cant,precio,"",false,prodStr,"Pendiente");
	        			listaProductos.add(b);
	        		}
	        		else {
	            		String notas = textExtras.getText(); 
	        
	            		if(!esSnack) {
	        
	            			String carneStr = obtenerSeleccion(grupoCarne); 
	            			if(InventarioDB.estaDisponible(carneStr)) {
	            				String extrasStr = obtenerSeleccion(grupoConTodo);
		            			Antojitos antoj = new Antojitos(prodStr,cant,precio,notas,conQueso,carneStr,extrasStr,"Pendiente");
		            			listaProductos.add(antoj);
	            			}
	            			else {
	            				JOptionPane.showMessageDialog(null, "El "+carneStr+" se acabo", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);
	            			}
	            			
	            		}
	            		else {
	            			String extraSnacks = "" ;
	            			if(chkSinAderezo.isSelected()) extraSnacks+="Sin aderezo";
	            			if(chkSinVerduras.isSelected()) extraSnacks+="Sin Verdura";
	            			Snacks snack = new Snacks(prodStr,cant,precio,notas,conQueso,extraSnacks,"Pendiente");
	            			listaProductos.add(snack);
	            		}
	        		}
	        		Producto ultimoProd = listaProductos.getLast();
	        		String desc = ultimoProd.toString();
	        		if (desc.length() > 20) {
	        		    desc = desc.substring(0, 18) + "..";
	        		}
	        		String lineaTicket = String.format("%-3d %-20s $%6.2f\n", ultimoProd.getCant(), desc, ultimoProd.getPrecioTotal());

	        		modeloTicket.addElement(lineaTicket);
	        		limpiarTodo();
	        	}else {JOptionPane.showMessageDialog(null, "El producto no esta disponible", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);}
        	}
        });
        //Boton que elimina producto seleccionado
        //Agarra el indice del producto seleccionado en la lista y lo elimina
        btnEliminarProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int ind = listaTicket.getSelectedIndex();
        		if(ind!=-1) {
        			if(listaProductos.get(ind).getEstado().equals("Terminado")) {
        				JOptionPane.showMessageDialog(null, "No puedes eliminar un producto ya terminado", "Advertencia", JOptionPane.ERROR_MESSAGE);
        				return;
        			}
        			if(JOptionPane.showConfirmDialog(null,"Desea eliminar este producto?"+listaProductos.get(ind).toString(),"Seguro?",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION) {
        				listaProductos.remove(ind);
        				modeloTicket.remove(ind);
        			}
        			else {
        				JOptionPane.showMessageDialog(null, "Operacion cancelada", "Cancelado", JOptionPane.ERROR_MESSAGE);
        			}

        		}
        		else {
        			JOptionPane.showMessageDialog(null, "Primero selecciona un producto", "Error", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
        btnActualizarProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int ind = listaTicket.getSelectedIndex();
        		if(ind==-1) {
        			JOptionPane.showMessageDialog(null, "Selecciona un producto", "Error", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
    			if(listaProductos.get(ind).getEstado().equals("Terminado")) {
    				JOptionPane.showMessageDialog(null, "No puedes actualizar un producto ya terminado", "Advertencia", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
        		String prodStr="";
        		String canStr ="";
        		if(textFieldCantidad.getText().equals("")) {
        			canStr = obtenerSeleccion(grupoCantidad);
        		}else canStr=textFieldCantidad.getText();
   
				if(esBebida) {
        			
        			String bebida="";
        			if(rbRefrescoGrande.isSelected())bebida=(String)comboRefrescosGrande.getSelectedItem()+ " 600ml";
        			else if(rbRefrescoChico.isSelected())bebida=(String)comboRefrescosChicos.getSelectedItem()+ " 350ml";
        			else if(rbAguas1L.isSelected())bebida = (String)comboAguas1L.getSelectedItem() +" 1L";
        			else if(rbAguasMedioL.isSelected())bebida=(String)comboAguasMedioL.getSelectedItem()+" medioL";
        			prodStr = bebida;
        		}
        		else {
        			prodStr = obtenerSeleccion(grupoProducto);
        		}
        		int cant = Integer.parseInt(canStr);
        		double precio = InventarioDB.getPrecio(prodStr);
        		boolean conQueso = chkConQueso.isSelected();
        		if((prodStr.equals("Torta")||prodStr.equals("Tacos"))&&conQueso) precio+=InventarioDB.getPrecio("Queso");
	        	if((InventarioDB.estaDisponible(prodStr)&&InventarioDB.validarStockMemoria(prodStr, cant))) {
	        		if(esBebida) {
	
	        			Bebida b = new Bebida(obtenerSeleccion(grupoProducto),cant,precio,"",false,prodStr,"Pendiente");
	        			listaProductos.set(ind,b);
	        		}
	        		else {
	            		String notas = textExtras.getText(); 
	        
	            		if(!esSnack) {
	        
	            			String carneStr = obtenerSeleccion(grupoCarne); 
	            			if(InventarioDB.estaDisponible(carneStr)) {
	            				String extrasStr = obtenerSeleccion(grupoConTodo);
		            			Antojitos antoj = new Antojitos(prodStr,cant,precio,notas,conQueso,carneStr,extrasStr,"Pendiente");
		            			listaProductos.set(ind,antoj);
	            			}
	            			else {
	            				JOptionPane.showMessageDialog(null, "El "+carneStr+" se acabo", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);
	            			}
	            			
	            		}
	            		else {
	            			String extraSnacks = "" ;
	            			if(chkSinAderezo.isSelected()) extraSnacks+="Sin aderezo";
	            			if(chkSinVerduras.isSelected()) extraSnacks+="Sin Verdura";
	            			Snacks snack = new Snacks(prodStr,cant,precio,notas,conQueso,extraSnacks,"Pendiente");
	            			listaProductos.set(ind,snack);
	            		}
	        		}
	        		Producto actuProd = listaProductos.get(ind);
	        		String desc = actuProd.toString();
	        		if (desc.length() > 20) {
	        		    desc = desc.substring(0, 18) + "..";
	        		}
	        		String lineaTicket = String.format("%-3d %-20s $%6.2f\n", actuProd.getCant(), desc, actuProd.getPrecioTotal());

	        		modeloTicket.set(ind,lineaTicket);
	        		JOptionPane.showMessageDialog(null, "Producto actualizado", "Succesfull", JOptionPane.INFORMATION_MESSAGE);
	        		limpiarTodo();
	        	}else {JOptionPane.showMessageDialog(null, "El producto no esta disponible", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);}
        	}
        });
        //Termina la orden para esa persona
        btnTerminarPersona.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int indPersona=comboPersonas.getSelectedIndex();
        		if(mesa!=null&&indPersona<mesa.getPersonas().size()) {
        			JOptionPane.showMessageDialog(null, "Intentas añadir una persona vacia", "Advertencia", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		if(listaProductos.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Aun no añades ningun producto", "Orden vacia", JOptionPane.INFORMATION_MESSAGE);
        		}
        		else {
               		ArrayList<Producto> listaNueva = new ArrayList<>();
            		for(Producto prod:listaProductos) {
            			listaNueva.add(prod.clonarProd());
            		}
            		Persona p = new Persona(nombreActual, contPer, listaNueva);
            		mesa.addPersona(p);
            		JOptionPane.showMessageDialog(null, "Persona "+nombreActual+" añadida con exito", "Persona", JOptionPane.INFORMATION_MESSAGE);
              		contPer++;
              		nombreActual="Persona "+(contPer+1);
              		comboPersonas.addItem(nombreActual);
              		comboPersonas.setSelectedItem(nombreActual);
              		scrollTicket.setBorder(crearBordeOscuro(nombreActual));
            		modeloTicket.clear();
            		listaProductos.clear();	
        		}
        	}
        });
        editarNombre.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String nombre = JOptionPane.showInputDialog("Ingresa nuevo nombre: ");

                // 2. Mostrar el nombre ingresado
                if (nombre != null && !nombre.trim().isEmpty()) {
                    scrollTicket.setBorder(crearBordeOscuro(nombre));
                    nombreActual = nombre;
                } else {
                    JOptionPane.showMessageDialog(null, "No ingresaste ningún nombre.");
                }
        		
        	}
        });
        
        // ... (Aquí van el resto de tus ActionListeners) ...
        
        //Action listenes que este pendiente si se preciona snack se desabilite la opcion de carnes
        //ejemplo si se selecciona hamburguesa no tiene sentido poner "burger de pastor"
        ActionListener validarProductos = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				esSnack=rbBurger.isSelected()||rbHotDogs.isSelected();
				esBebida = rbRefrescoGrande.isSelected() || rbRefrescoChico.isSelected()||rbAguas1L.isSelected()||rbAguasMedioL.isSelected();
		        for(Enumeration<AbstractButton> carnes = grupoCarne.getElements(); carnes.hasMoreElements();) {
		            AbstractButton carne = carnes.nextElement(); 
		            carne.setEnabled(!esSnack&&!esBebida);
		            
		        }
		        if(rbQuesadillas.isSelected()||rbBurros.isSelected()||rbVolcanes.isSelected()||rbBurger.isSelected()) {
		        	chkConQueso.setSelected(true);
		        }else {
		        	chkConQueso.setSelected(false);
		        }
		        if(esSnack||esBebida) {
		        	grupoCarne.clearSelection();
		        }
		        
		        //Ocultamos las cosas para los tacos si es snakck o bebida
		        rbConTodo.setEnabled(!esSnack&&!esBebida);
		        rbSinCebolla.setEnabled(!esSnack&&!esBebida);
		        rbSinCilantro.setEnabled(!esSnack&&!esBebida);
		        rbSinVerdura.setEnabled(!esSnack&&!esBebida);
		        chkConQueso.setEnabled(!esBebida);
		        //Mostramos solo los combos bos si esta seleccionado su respectiva
		        comboRefrescosGrande.setEnabled(rbRefrescoGrande.isSelected());
		        comboRefrescosChicos.setEnabled(rbRefrescoChico.isSelected());
		        comboAguas1L.setEnabled(rbAguas1L.isSelected());
		        comboAguasMedioL.setEnabled(rbAguasMedioL.isSelected());
		        //Mostramos las cosas de comida rapida
		        chkSinAderezo.setEnabled(esSnack);
		        chkSinVerduras.setEnabled(esSnack);
		        rbPastor.setSelected(true);
			}
        };
        //Se le añade el action listener anterior a cada elemento del grupo de productos
        for(Enumeration<AbstractButton> productos = grupoProducto.getElements(); productos.hasMoreElements();) {
        	productos.nextElement().addActionListener(validarProductos);
        }
        //Por defecto tacos 
        rbTacos.doClick();

        //Haremos otro accion listener para validar que no se precione una cantidad si el usuario escribie algo
        ActionListener validarCantidad = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldCantidad.getText().equals("")) {
					cantText=true;
				}
				else {
					cantText=false;
				}
		        for(Enumeration<AbstractButton> cantidad = grupoCantidad.getElements(); cantidad.hasMoreElements();) {
		            AbstractButton c = cantidad.nextElement(); 
		            c.setEnabled(cantText);
		            
		        }
			}
        }; 
        for(Enumeration<AbstractButton> cantidad = grupoCantidad.getElements(); cantidad.hasMoreElements();) {
        	cantidad.nextElement().addActionListener(validarCantidad);
        }
        //Añadimos un key listener al text field para que no tenga seleccionado una cantidad en caso de que escriba algo
        textFieldCantidad.addKeyListener(new java.awt.event.KeyAdapter(){
        	public void keyReleased(java.awt.event.KeyEvent evt) {
        		String texto = textFieldCantidad.getText().trim();
        		if(texto.isEmpty()) {
        			rbCant1.setEnabled(true);
        			rbCant1.setSelected(true);
        			rbCant1.doClick();
        			return;
        		}
        		else {
        			try {
        				int cant = Integer.parseInt(texto);
        				if(cant<=0||cant>99) {
        					JOptionPane.showMessageDialog(null,"Cantidad invalida","Alerta",JOptionPane.WARNING_MESSAGE);
        					textFieldCantidad.setText("");
        					rbCant1.doClick();
        					return;
        				}
        				rbCant1.doClick();
        				grupoCantidad.clearSelection();
        			}catch(NumberFormatException ex1) {
        				JOptionPane.showMessageDialog(null,"Cantidad invalida","Alerta",JOptionPane.WARNING_MESSAGE);
        				textFieldCantidad.setText("");
        				rbCant1.doClick();
        			}
        		}
        	}
        });
        //Añadimos un action listenes, para que si el mesero toca un producto pueda ver la orden graficamente
        listaTicket.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
        	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        			if(e.getValueIsAdjusting()) {//Evita que se active mas de una vez
        				int ind = listaTicket.getSelectedIndex();
        				if(ind!=-1&&!listaProductos.isEmpty()) {
        					Producto p = listaProductos.get(ind);
        					//Añadimos bloqueo si el usuario quiere editar un producto ya preparado
        					if(p.getEstado().equals("Terminado")) {
        						JOptionPane.showMessageDialog(null,"Este producto ya se preparo","Producto terminado",JOptionPane.WARNING_MESSAGE);
        						listaTicket.clearSelection();
        						limpiarTodo();
        						return;
        					}
        					cargarDatosInterfaz(p);
        				}
        			}
        	}


        });
        //Ahora vamos añadir que al navegar entre personas podamos ver la lista de productos de cada persona
        comboPersonas.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int ind = comboPersonas.getSelectedIndex();
        		//Verificmos si ya hay mesa o no y si tiene personas)
        		if(ind!=-1&&mesa!=null) {
        			nombreActual=(String) comboPersonas.getSelectedItem();
        			scrollTicket.setBorder(crearBordeOscuro(nombreActual));
        			//Si es una persona existente
        			if(ind<mesa.getPersonas().size()) {
        				Persona pSelect=mesa.getPersonas().get(ind);
        				listaProductos=pSelect.getListaProductos();
        				modeloTicket.clear();
        				for(Producto prod : listaProductos) {
	        				String desc = prod.toString();
	    	        		if (desc.length() > 20) desc = desc.substring(0, 18) + "..";
	    	        		//indicamos que productos ya estan terminados
	    	        		if(prod.getEstado().equals("Terminado"))desc="✓"+desc;
	    
	    	        		String lineaTicket = String.format("%-3d %-20s $%6.2f\n", prod.getCant(), desc, prod.getPrecioTotal());
	    	        		modeloTicket.addElement(lineaTicket);
	        
        				}
        			return;	
        			}else {
            			listaProductos=new ArrayList<>();
            			modeloTicket.clear();
        			}
        			
        		}

        	}
        });
       
    }
	//Getters setters
    public void setVentanaMain(VentanaMain ventanaMain){
        this.ventanaMain = ventanaMain;
    }

    //Actualizamos set mesa para que sea capaz de entrar en modo edicion
	public void setMesa(Mesa mesa,boolean modoEdicion) {
		this.mesa=mesa;
		this.numMesa=""+mesa.getNumMesa();
		this.modoEdicion = modoEdicion;
		//Si entremos en modo edicion se limpia todo por si acaso
		comboPersonas.removeAllItems();
		modeloTicket.clear();
		listaProductos.clear();
		if(modoEdicion && !mesa.getPersonas().isEmpty()) {
			//Si es modo edicion cargamos a las personas que estan en la memoria
			contPer=mesa.getPersonas().size();
			for(Persona p : mesa.getPersonas()) {
				comboPersonas.addItem(p.getNombre());
			}
			//Preparamos nombre para la siguiente persona que añadira
			nombreActual="Persona"+(contPer+1);
			comboPersonas.addItem(nombreActual);
			comboPersonas.setSelectedItem(nombreActual);
		}else {
			contPer=0;
			nombreActual="Persona 1";
			comboPersonas.addItem(nombreActual);
		}
		
		scrollTicket.setBorder(crearBordeOscuro(nombreActual));
	}
	//Para no preguntar bootn por boton hare funcioon auxiluar que hace clik en un boton y lo busca en grupos
	public void hacerClicEn(ButtonGroup grupo,String textoBoton) {
        for(Enumeration<AbstractButton> boton = grupo.getElements(); boton.hasMoreElements();) {
            AbstractButton b = boton.nextElement(); 
            if(b.getText().equalsIgnoreCase(textoBoton)) {
            	b.doClick();
            	return;
            }
            
        }
	}
	//Haremos una funcion que nos ayudara a editar una orden
	//Lo que hara es en base al texto del producto seleccionaeremos las cosas en la interfaz (la recreamos)
	public void cargarDatosInterfaz(Producto p) {
		//Cargamos la cantidad
		textFieldCantidad.setText(String.valueOf(p.getCant()));
		rbCant1.doClick();
		//Pasamos todo el texto a minusculas para buscar facilmente
		String desc = p.toString().toLowerCase();
		//Buscamos el producto
		if(desc.contains("tacos")) hacerClicEn(grupoProducto,"Tacos");
		else if(desc.contains("tortas")) hacerClicEn(grupoProducto,"Tortas");
		else if(desc.contains("ques")) hacerClicEn(grupoProducto,"Quesadillas");
		else if(desc.contains("burros")) hacerClicEn(grupoProducto,"Burros");
		else if(desc.contains("volvanes")) hacerClicEn(grupoProducto,"Volcanes");
		else if(desc.contains("burg")) hacerClicEn(grupoProducto,"Hamburguesa");
		else if(desc.contains("hotdog")) hacerClicEn(grupoProducto,"HotDogs");
		
		// (Bebidas)
	    else if (desc.contains("600ml")) hacerClicEn(grupoProducto, "Refresco 600ml");
	    else if (desc.contains("350ml")) hacerClicEn(grupoProducto, "Refresco 350ml");
	    else if (desc.contains("1l")) hacerClicEn(grupoProducto, "Aguas 1L");
	    else if (desc.contains("mediol")) hacerClicEn(grupoProducto, "Aguas MedioL");

	    // 3. ADIVINAMOS LA CARNE (Revisamos tu abreviatura " p " y damos clic al botón "Pastor")
	    if (desc.contains(" p ") || desc.contains("pastor")) hacerClicEn(grupoCarne, "Pastor");
	    else if (desc.contains(" bis ")) hacerClicEn(grupoCarne, "Bisteck");
	    else if (desc.contains(" cho ")) hacerClicEn(grupoCarne, "Chorizo");
	    else if (desc.contains(" bir ")) hacerClicEn(grupoCarne, "Birria");
	    else if (desc.contains(" lec ")) hacerClicEn(grupoCarne, "Lechon");

	    // 4. ADIVINAMOS EXTRAS EXCLUYENTES
	    if (desc.contains("c/t")) hacerClicEn(grupoConTodo, "Con todo");
	    else if (desc.contains("s/ceb")) hacerClicEn(grupoConTodo, "Sin cebolla");
	    else if (desc.contains("s/cil")) hacerClicEn(grupoConTodo, "Sin cilantro");
	    else if (desc.contains("s/verd") && !desc.contains("s/verdura")) hacerClicEn(grupoConTodo, "Sin Verdura");

	    // 5. CHECKBOXES (Estos SÍ los tienes globales, así que los encendemos directo)
	    chkConQueso.setSelected(desc.contains("c/q"));
	    chkSinAderezo.setSelected(desc.contains("s/aderezo"));
	    chkSinVerduras.setSelected(desc.contains("s/verdura"));
	    
	    // (Opcional) Leer las notas
	    // Las notas son la parte final del string, así que requerirían un poco más de manejo de texto,
	    // pero por ahora textExtras.setText(""); es suficiente para no mezclar notas.
	    textExtras.setText("");
	}
    public String obtenerSeleccion(ButtonGroup grupo) {
        for(Enumeration<AbstractButton> botones = grupo.getElements(); botones.hasMoreElements();) {
            AbstractButton boton = botones.nextElement(); 
            if(boton.isSelected()) {
                return boton.getText(); 
            }

        }
        return "";
    }
    
    public void limpiarTodo() {
    	textFieldCantidad.setText("");
    	rbTacos.doClick();
    	rbCant1.setEnabled(true);
    	rbCant1.doClick();
    	rbPastor.setSelected(true);
    	grupoConTodo.clearSelection();
        chkConQueso.setSelected(false);
        chkSinAderezo.setSelected(false);
        chkSinVerduras.setSelected(false);
    	
    }

    // =========================================================
    // MÉTODOS AUXILIARES DE DISEÑO (MAGIA VISUAL)
    // =========================================================
    
    private void estilizarBotonOpcion(AbstractButton btn) {
        btn.setBackground(new Color(45, 45, 45)); // Mismo fondo que la columna
        btn.setForeground(Color.WHITE); // Letra blanca
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estilizarCampoTexto(JTextField txt) {
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txt.setBackground(new Color(60, 60, 60));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE); // Cursor de escritura blanco
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), 
            new EmptyBorder(5, 5, 5, 5) // Padding interno
        ));
    }

    private void estilizarBoton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estilizarMiniBoton(JButton btn, Color bg) {
        btn.setMaximumSize(new Dimension(700, 25));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private TitledBorder crearBordeOscuro(String titulo) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), // Línea gris sutil
            titulo+" "+"Mesa: "+numMesa, 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(255, 193, 7) // Título en amarillo para resaltar
        );
    }
}