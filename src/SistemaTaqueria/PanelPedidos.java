//Este panel se encargara de mostrar los pedidos actuales ordenados para que el cocinero los vea y los pueda liberar
package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.awt.event.ActionEvent;

public class PanelPedidos extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;
	private JTable tablaCocina; //vamos a agregar todo en una tabla
	private DefaultTableModel modeloTabla;
	private JButton btnLiberar, btnArriba, btnAbajo; //Aqui botones para navegar y liberar

	/**
	 * Create the panel.
	 */
	public PanelPedidos() {
		setBackground(new Color(30,30,30));
		setLayout(new BorderLayout(15,15));
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		//Aqui se configura la tabla (el corazon visual del cocinero)
		String[] columnas = {"ID Pedido","Mesa","Persona","Producto"};
		
		//Bloqueamos la edicion para que no le metan mano por error a los textos
		modeloTabla=new DefaultTableModel(columnas,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		//La magia del renderizado: personalizamos como se pinta cada celda
		tablaCocina = new JTable(modeloTabla) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				// 1. Respetar el color verde cuando el usuario selecciona una fila
				if (isRowSelected(row)) {
					return c;
				}

				// 2. Leer el ID oculto de la columna 0 ("M-1", "P-1-Persona", "O-1-32")
				String idOculto = getModel().getValueAt(row, 0).toString();
				String[] partes = idOculto.split("-");
				
				if (partes.length >= 2) {
					int numMesa = Integer.parseInt(partes[1]);
					
					// 3. Alternar el color de fondo usando el numero de la mesa (Efecto cebra por mesa)
					if (numMesa % 2 == 0) {
						c.setBackground(new Color(60, 60, 60)); // Gris original para mesas pares
					} else {
						c.setBackground(new Color(45, 45, 45)); // Gris mas oscuro para mesas impares
					}
					
					// 4. (Opcional pero recomendado) Hacer que el encabezado "MESA X" resalte mas
					if (partes[0].equals("M")) {
						c.setFont(new Font("Segoe UI", Font.BOLD, 17));
						c.setForeground(new Color(255, 193, 7)); // Letra amarilla para el titulo de la mesa
					} else {
						c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
						c.setForeground(Color.WHITE); // Letra blanca normal para el resto
					}
				}
				return c;
			}
		};
		
		//Estilos generales de la tabla
		tablaCocina.setRowHeight(35);
		tablaCocina.setFont(new Font("Segoe UI",Font.PLAIN,16));
		tablaCocina.setForeground(Color.WHITE);
		tablaCocina.setBackground(new Color(60,60,60));
		tablaCocina.setSelectionBackground(new Color(46,204,113));
		tablaCocina.getTableHeader().setBackground(new Color(45,45,45));
		tablaCocina.getTableHeader().setForeground(Color.white);
		tablaCocina.getTableHeader().setFont(new Font("Segoe UI",Font.PLAIN,15));
		
		//EL TRUCO: Ocultamos la columna 0. Tiene datos clave para nuestra logica pero el usuario no necesita verla
		tablaCocina.removeColumn(tablaCocina.getColumnModel().getColumn(0));
		
		//Ajustamos los anchos de las columnas visibles
		tablaCocina.getColumnModel().getColumn(0).setPreferredWidth(80);
		tablaCocina.getColumnModel().getColumn(1).setPreferredWidth(120);
		tablaCocina.getColumnModel().getColumn(2).setPreferredWidth(250);
		
		JScrollPane scrollTabla= new JScrollPane(tablaCocina);
		scrollTabla.getViewport().setBackground(new Color(45,45,45));
		add(scrollTabla,BorderLayout.CENTER);
		
		//hacemos el panel de los botones (menu lateral derecho)
		JPanel panelBotones=new JPanel();
		panelBotones.setLayout(new GridLayout(4,1,0,15));
		panelBotones.setBackground(new Color(30,30,30));
		panelBotones.setPreferredSize(new Dimension(200,0));
		
		btnArriba = new JButton("Subir");
		btnAbajo = new JButton("Bajar");
		btnLiberar= new JButton("Liberar seleccionado");

		estilizarBoton(btnArriba,new Color(255, 193, 7),Color.WHITE);
		estilizarBoton(btnAbajo,new Color(255,0,0),Color.WHITE);
		estilizarBoton(btnLiberar,new Color(8,51,162),Color.BLACK); //Azul
		
		panelBotones.add(btnArriba);
		panelBotones.add(btnAbajo);
		panelBotones.add(btnLiberar);
		add(panelBotones,BorderLayout.EAST);
		
		//------Acciones (Logica de funcionamiento)
		
		//Boton para subir en la lista sin usar el mouse
		btnArriba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = (int)tablaCocina.getSelectedRow();
				filaSeleccionada--;
				if(filaSeleccionada!=-1){
					tablaCocina.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
				}
				else {
					tablaCocina.setRowSelectionInterval(0, 0); //Tope arriba
				}
			}
		});
		
		//Boton para bajar en la lista
		btnAbajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = (int)tablaCocina.getSelectedRow();
				filaSeleccionada++;
				if(filaSeleccionada < tablaCocina.getRowCount()&&filaSeleccionada!=0){
					tablaCocina.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
				}
			}
		});
		
		//Este boton libera el pedido, ya sea seleccionando mesa, persona o producto individual
		btnLiberar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaCocina.getSelectedRow();
				
				//Seguro contra nulos
				if(filaSeleccionada==-1) {
					JOptionPane.showMessageDialog(null,"Selecciona un objeto primero","Unsuccesfull",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				//Vamos a leer los datos de la tabla (usamos la columna 0 que escondimos)
				String id = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
				String[] partes = id.split("-",3);
				String tipo = partes[0];
				int numMesa = Integer.parseInt(partes[1]);
				
				//dependiendo de donde clikeas se libera correspondientemente
				if(tipo.equals("M")) {
					//Liberar toda la mesa de golpe
					ControladorPedidos.liberarMesaCompleta(numMesa);
					
				}else if(tipo.equals("P")) {
					//Liberar todos los tacos de una sola persona
					String nombrePer = partes[2];
					ControladorPedidos.liberarPersonaCompleta(numMesa,nombrePer);
					
				}else if(tipo.equals("O")) {
					//Liberar un solo producto especifico
					int idPedido = Integer.parseInt(partes[2]);
					ControladorPedidos.liberarPedido(idPedido,numMesa);
				}
				
				//Refrescamos visualmente para que desaparezca lo que liberamos
				cargarPedidos();
			}
		});
		
		//Efectos visuales Hover (cambian de color cuando el mouse pasa por encima)
		btnLiberar.addMouseListener(new MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				 btnLiberar.setBackground(new Color(46, 204, 113)); //Verde al tocar
				 btnLiberar.setForeground(Color.black);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				 btnLiberar.setBackground(new Color(8,51,162)); //Regresa a su azul
				 btnLiberar.setForeground(Color.white);
			}
		});
		
		btnAbajo.addMouseListener(new MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				 btnAbajo.setBackground(new Color(46, 204, 113));
				 btnAbajo.setForeground(Color.black);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				 btnAbajo.setBackground(new Color(255,0,0));
				 btnAbajo.setForeground(Color.white);
			}
		});
		
		btnArriba.addMouseListener(new MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				 btnArriba.setBackground(new Color(46, 204, 113));
				 btnArriba.setForeground(Color.black);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				 btnArriba.setBackground(new Color(255, 193, 7));
				 btnArriba.setForeground(Color.white);
			}
		});
		
		//Carga inicial al abrir el panel
		cargarPedidos();
	}

	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain = ventanaMain;
	}
	
	//Metodo auxiliar para que los botones se vean limpios y modernos
	private void estilizarBoton(JButton btn, Color bg, Color fg) {
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	//Haremos una funcion para cargar los pedidos a la tabla armando el arbol visual
	public void cargarPedidos() {
		modeloTabla.setRowCount(0); //La limpiamos para que no se dupliquen datos
		
		//Traemos la info cruda de la base de datos
		List <Object[]> pedidos = ControladorPedidos.obtenerPedidosPendientes();
		
		String mesaActual="";
		String personaActual="";
		
		//Recorremos e inyectamos los datos creando la jerarquia
		for(Object[] fila : pedidos) {
			int idPedido = (int) fila[0];
			String mesaTexto = fila[1].toString();
			String numMesaStr=mesaTexto.replace("Mesa ", "");
			String persona = fila[2].toString();
			int cant = (int)fila[3];
			String producto = fila[4].toString();
			
			//Si detectamos que cambiamos de mesa, imprimimos una fila de titulo para esa mesa
			if(!mesaTexto.equals(mesaActual)) {
				modeloTabla.addRow(new Object[] {"M-"+numMesaStr,mesaTexto.toUpperCase(),"",""});
				mesaActual=mesaTexto;
				personaActual=""; //Reseteamos la persona porque es mesa nueva
			}
			
			//Si detectamos que cambiamos de persona, imprimimos su encabezado
			if(!persona.equals(personaActual)) {
				modeloTabla.addRow(new Object[] {"P-"+numMesaStr+"-"+persona,"",persona,""});
				personaActual=persona;
			}
			
			//Finalmente imprimimos el producto en si
			modeloTabla.addRow(new Object[] {"O-"+numMesaStr+"-"+idPedido,"","",cant+"  "+producto});
		}
	}
}