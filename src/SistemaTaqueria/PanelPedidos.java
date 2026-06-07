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
		private JTable tablaCocina;//vamos a agregar todo en una tabla
		private DefaultTableModel modeloTabla;
		private JButton btnLiberar,btnArriba,btnAbajo; //Aqui botones para navegar y liberar
	/**
	 * Create the panel.
	 */
	public PanelPedidos() {
		setBackground(new Color(30,30,30));
		setLayout(new BorderLayout(15,15));
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		//Aqui se configura la tabla
		String[] columnas = {"ID Pedido","Mesa","Persona","Producto"};
		modeloTabla=new DefaultTableModel(columnas,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
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
							
							// 3. Alternar el color de fondo usando el número de la mesa
							if (numMesa % 2 == 0) {
								c.setBackground(new Color(60, 60, 60)); // Gris original para mesas pares
							} else {
								c.setBackground(new Color(45, 45, 45)); // Gris más oscuro para mesas impares
							}
							
							// 4. (Opcional pero recomendado) Hacer que el encabezado "MESA X" resalte más
							if (partes[0].equals("M")) {
								c.setFont(new Font("Segoe UI", Font.BOLD, 17));
								c.setForeground(new Color(255, 193, 7)); // Letra amarilla para el título
							} else {
								c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
								c.setForeground(Color.WHITE); // Letra blanca normal para el resto
							}
						}
						return c;
					}
				};
		tablaCocina.setRowHeight(35);
		tablaCocina.setFont(new Font("Segoe UI",Font.PLAIN,16));
		tablaCocina.setForeground(Color.WHITE);
		tablaCocina.setBackground(new Color(60,60,60));
		tablaCocina.setSelectionBackground(new Color(46,204,113));
		tablaCocina.getTableHeader().setBackground(new Color(45,45,45));
		tablaCocina.getTableHeader().setForeground(Color.white);
		tablaCocina.getTableHeader().setFont(new Font("Segoe UI",Font.PLAIN,15));
		
		tablaCocina.removeColumn(tablaCocina.getColumnModel().getColumn(0));
		//Ajustamos los anchos
		tablaCocina.getColumnModel().getColumn(0).setPreferredWidth(80);
		tablaCocina.getColumnModel().getColumn(1).setPreferredWidth(120);
		tablaCocina.getColumnModel().getColumn(2).setPreferredWidth(250);
		
		JScrollPane scrollTabla= new JScrollPane(tablaCocina);
		scrollTabla.getViewport().setBackground(new Color(45,45,45));
		add(scrollTabla,BorderLayout.CENTER);
		//hacemos el panel de los botones
		JPanel panelBotones=new JPanel();
		panelBotones.setLayout(new GridLayout(4,1,0,15));
		panelBotones.setBackground(new Color(30,30,30));
		panelBotones.setPreferredSize(new Dimension(200,0));
		
		btnArriba = new JButton("Subir");
		btnAbajo = new JButton("Bajar");
		btnLiberar= new JButton("Liberar seleccionado");

		estilizarBoton(btnArriba,new Color(255, 193, 7),Color.WHITE);
		estilizarBoton(btnAbajo,new Color(255,0,0),Color.WHITE);
		estilizarBoton(btnLiberar,new Color(8,51,162),Color.BLACK);
		panelBotones.add(btnArriba);
		panelBotones.add(btnAbajo);
		panelBotones.add(btnLiberar);
		add(panelBotones,BorderLayout.EAST);
		
		//------Acciones 
		btnArriba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = (int)tablaCocina.getSelectedRow();
				filaSeleccionada--;
				if(filaSeleccionada!=-1){
					tablaCocina.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
				}
				else {
					tablaCocina.setRowSelectionInterval(0, 0);
				}
			}
		});
		btnAbajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = (int)tablaCocina.getSelectedRow();
				filaSeleccionada++;
				if(filaSeleccionada < tablaCocina.getRowCount()&&filaSeleccionada!=0){
					tablaCocina.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
				}
			}
		});
		//Este boton liberar el pedido, ya sea seleccionando mesa, persona o producto individual
		btnLiberar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaCocina.getSelectedRow();
				if(filaSeleccionada==-1) {
					JOptionPane.showMessageDialog(null,"Selecciona un objeto primero","Unsuccesfull",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				//Vamos a leer los datos de la tabla
				String id = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
				String[] partes = id.split("-",3);
				String tipo = partes[0];
				int numMesa = Integer.parseInt(partes[1]);
				//dependindo de donde clikeas se libera correspondientemente
				if(tipo.equals("M")) {
					ControladorPedidos.liberarMesaCompleta(numMesa);
					
				}else if(tipo.equals("P")) {
					String nombrePer = partes[2];
					ControladorPedidos.liberarPersonaCompleta(numMesa,nombrePer);
				}else if(tipo.equals("O")) {
					int idPedido = Integer.parseInt(partes[2]);
					ControladorPedidos.liberarPedido(idPedido,numMesa);
					
				}
				cargarPedidos();
			}
		});
	    btnLiberar.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(java.awt.event.MouseEvent evt) {
	        	 btnLiberar.setBackground(new Color(46, 204, 113));
	        	 btnLiberar.setForeground(Color.black);
	        }
	        public void mouseExited(java.awt.event.MouseEvent evt) {
	        	 btnLiberar.setBackground(new Color(8,51,162));
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
		cargarPedidos();
	}

    public void setVentanaMain(VentanaMain ventanaMain) {
        this.ventanaMain = ventanaMain;
    }
    private void estilizarBoton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    //Haremos una funcion para cargar los pedidos a la tabla
    public void  cargarPedidos() {
    	modeloTabla.setRowCount(0); //La limpiamos
    	List <Object[]> pedidos = ControladorPedidos.obtenerPedidosPendientes();
    	String mesaActual="";
    	String personaActual="";
    	for(Object[] fila : pedidos) {
    		int idPedido = (int) fila[0];
    		String mesaTexto = fila[1].toString();
    		String numMesaStr=mesaTexto.replace("Mesa ", "");
    		String persona = fila[2].toString();
    		int cant = (int)fila[3];
    		String producto = fila[4].toString();
    		if(!mesaTexto.equals(mesaActual)) {
    			modeloTabla.addRow(new Object[] {"M-"+numMesaStr,mesaTexto.toUpperCase(),"",""});
    			mesaActual=mesaTexto;
    			personaActual="";
    		}
    		if(!persona.equals(personaActual)) {
    			modeloTabla.addRow(new Object[] {"P-"+numMesaStr+"-"+persona,"",persona,""});
    			personaActual=persona;
    		}
    		modeloTabla.addRow(new Object[] {"O-"+numMesaStr+"-"+idPedido,"","",cant+"  "+producto});
    	}
    	
    }
    

}
