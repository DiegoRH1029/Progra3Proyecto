package SistemaTaqueria;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelOrdenes extends JPanel {

    private static final long serialVersionUID = 1L;
    private VentanaMain ventanaMain;
   
    private static int contPer=1;
    private String nombreActual;
    // Grupos de botones para que solo se pueda seleccionar una opción por columna
    private ButtonGroup grupoCantidad;
    private ButtonGroup grupoProducto;
    private ButtonGroup grupoCarne;
    private ButtonGroup grupoConTodo;
    private JTextField textExtras;
    private JTextField textFieldCantidad;
    private ArrayList<Producto> listaProductos;
    private Mesa mesa;
    private boolean esSnack;
    private boolean esBebida;
    private boolean cantText;

    private JRadioButton rbBurger;
    private JRadioButton rbHotDogs;

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
        
        JRadioButton rbTacos = new JRadioButton("Tacos");
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
        JComboBox<String> comboRefrescosGrande = new JComboBox<>(saboresRefrescosGrande);
        comboRefrescosGrande.setMaximumSize(new Dimension(700, 20));
        JComboBox<String> comboRefrescosChicos = new JComboBox<>(saboresRefrescosChico);
        comboRefrescosChicos.setAutoscrolls(true);
        comboRefrescosChicos.setMaximumSize(new Dimension(700, 20));
        JComboBox<String> comboAguas1L = new JComboBox<>(saboresAguas1L);
        comboAguas1L.setMaximumSize(new Dimension(700, 20));
        JComboBox<String> comboAguasMedioL = new JComboBox<>(saboresAguasMedioL);
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
        
        JRadioButton rbPastor = new JRadioButton("Pastor");
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
        JCheckBox chkConQueso = new JCheckBox("Con queso");
        JCheckBox chkSinAderezo = new JCheckBox("Sin aderezos");
        JCheckBox chkSinVerduras = new JCheckBox("Sin Verduras");
        
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
        panelDerecho.setPreferredSize(new Dimension(350, 0));
        panelDerecho.setLayout(new BorderLayout(0, 15));
        add(panelDerecho, BorderLayout.EAST);


               
        // Área de texto para el ticket
        JTextArea areaTicket = new JTextArea();
        areaTicket.setEditable(false); 
        areaTicket.setBackground(new Color(60, 60, 60)); // Fondo oscuro
        areaTicket.setForeground(Color.WHITE); // Texto blanco
        areaTicket.setFont(new Font("Monospaced", Font.PLAIN, 15));
        areaTicket.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollTicket = new JScrollPane(areaTicket);
        scrollTicket.setBorder(crearBordeOscuro("Persona "+contPer));
        scrollTicket.getViewport().setBackground(new Color(45, 45, 45)); // Quita bordes blancos del scroll
        panelDerecho.add(scrollTicket, BorderLayout.CENTER);

        // Sub-panel inferior para los botones de acción
        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setBackground(new Color(30, 30, 30));
        panelBotonesAccion.setLayout(new GridLayout(5, 1, 0, 10)); 

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
      
        
        panelBotonesAccion.add(btnAnadirProducto);
        panelBotonesAccion.add(btnTerminarPersona);
        panelBotonesAccion.add(btnRegresar);
        panelBotonesAccion.add(btnFinalizar);
        panelBotonesAccion.add(editarNombre);
 
        
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
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ventanaMain != null) {
                	ventanaMain.mostrarMenu(true);
                	ventanaMain.navegarA("MESAS");
                }
            }
        });
        //Finaliza la orden total de la mesa
        btnFinalizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
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
	        	if((InventarioDB.estaDisponible(prodStr)&&InventarioDB.validarStockMemoria(prodStr, cant))) {
	        		if(esBebida) {
	
	        			Bebida b = new Bebida(obtenerSeleccion(grupoProducto),cant,precio,"",false,prodStr);
	        			listaProductos.add(b);
	        			areaTicket.append(listaProductos.getLast().toString()+"\n");
	        		}
	        		else {
	            		String notas = textExtras.getText(); 
	            		boolean conQueso = chkConQueso.isSelected();
	            		if(!esSnack) {
	        
	            			String carneStr = obtenerSeleccion(grupoCarne); 
	            			if(InventarioDB.estaDisponible(carneStr)) {
	            				String extrasStr = obtenerSeleccion(grupoConTodo);
		            			Antojitos antoj = new Antojitos(prodStr,cant,precio,notas,conQueso,carneStr,extrasStr);
		            			listaProductos.add(antoj);
		            			areaTicket.append(listaProductos.getLast().toString()+"\n");
	            			}
	            			else {
	            				JOptionPane.showMessageDialog(null, "El "+carneStr+" se acabo", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);
	            			}
	            			
	            		}
	            		else {
	            			String extraSnacks = "" ;
	            			if(chkSinAderezo.isSelected()) extraSnacks+="Sin aderezo";
	            			if(chkSinVerduras.isSelected()) extraSnacks+="Sin Verdura";
	            			Snacks snack = new Snacks(prodStr,cant,precio,notas,conQueso,extraSnacks);
	            			listaProductos.add(snack);
	            			areaTicket.append(listaProductos.getLast().toString()+"\n");
	            		}
	        		}
	        	}else {JOptionPane.showMessageDialog(null, "El producto no esta disponible", "Sin disponibilidad", JOptionPane.ERROR_MESSAGE);}
        	}
        });
        
        //Termina la orden para esa persona
        btnTerminarPersona.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Persona p = new Persona(nombreActual, contPer, listaProductos);
        		mesa.addPersona(p);
        		listaProductos.clear();
        		
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
        		int cant=0;
        		try {
        			if(!textFieldCantidad.getText().equals("")) cant = Integer.parseInt(textFieldCantidad.getText());
        			if(cant>99||cant<=0) throw new Exepcion("Cantidad no permitida");
        	
        		}
        		catch(NumberFormatException ex1) {
        			JOptionPane.showMessageDialog(null, "Cantidad invalida.", "Error", JOptionPane.ERROR_MESSAGE);
        			textFieldCantidad.setText("");
        		}
        		catch(Exepcion ex0) {
           			JOptionPane.showMessageDialog(null, ex0.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        			textFieldCantidad.setText("");
        		}
        		
        		if(textFieldCantidad.getText().equals("")) {
            		rbCant1.setEnabled(true);
            		rbCant1.setSelected(true);
            		rbCant1.doClick();
        		}
        		else { 
        			rbCant1.doClick();
        			if(cant>99||cant<0) {
        				textFieldCantidad.setText("");
        			}
        			grupoCantidad.clearSelection();
        			
        		}
        	}
        });
       
    }

    public void setVentanaMain(VentanaMain ventanaMain){
        this.ventanaMain = ventanaMain;
    }
	//Getters setters
	public void setMesa(Mesa mesa) {
		this.mesa=mesa;
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
            titulo, 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(255, 193, 7) // Título en amarillo para resaltar
        );
    }
}