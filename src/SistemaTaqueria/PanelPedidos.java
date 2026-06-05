//Este panel se encargara de mostrar los pedidos actuales ordenados para que el cocinero los vea y los pueda liberar
package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
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
		tablaCocina = new JTable(modeloTabla);
		tablaCocina.setRowHeight(35);
		tablaCocina.setFont(new Font("Segoe UI",Font.PLAIN,16));
		tablaCocina.setForeground(Color.WHITE);
		tablaCocina.setBackground(new Color(60,60,60));
		tablaCocina.setSelectionBackground(new Color(46,204,113));
		
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

		estilizarBoton(btnArriba,new Color(100,100,100),Color.WHITE);
		estilizarBoton(btnAbajo,new Color(100,100,100),Color.WHITE);
		estilizarBoton(btnLiberar,new Color(100,100,100),Color.BLACK);
		panelBotones.add(btnAbajo);
		panelBotones.add(btnArriba);
		panelBotones.add(btnLiberar);
		add(panelBotones,BorderLayout.EAST);
		
		//------Acciones 
		btnArriba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAbajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
