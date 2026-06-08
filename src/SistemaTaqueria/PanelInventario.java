//Haremos otro panel con una tabla que mostrara la tabla desde la base de datos de nuestro inventario
//Se podran hacer modificaciones en la cantidad del stock o añadirlo con el boton, tambien ya que es una ventana mas sencible bloquearemos
//el menu lateral y nos aseguraremos de guardar los datos nuevos en la base de datos
package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class PanelInventario extends JPanel {

	private static final long serialVersionUID = 1L;
	 private VentanaMain ventanaMain;
		private JTable tablaInventario;//vamos a agregar todo en una tabla
		private DefaultTableModel modeloTabla;
		private JButton btnAplicar,btnRegresar,btnAddStock; //Aqui botones para navegar y liberar
		private JTextField txtSumarStock;
		private boolean hayCambios;
		
		
	/**
	 * Create the panel.
	 */
	public PanelInventario() {
		setBackground(new Color(30,30,30));
		setLayout(new BorderLayout(15,15));
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		//Pondremos un titulo arriba
		JPanel panelCentro = new JPanel(new BorderLayout(0,10));
		panelCentro.setBackground(new Color(30,30,30));
		JLabel lblTitulo = new JLabel("Inventario");
		lblTitulo.setFont(new Font("Segoe UI",Font.PLAIN,16));
		lblTitulo.setForeground(new Color(255,193,7));
		lblTitulo.setBackground(new Color(60,60,60));
		panelCentro.add(lblTitulo,BorderLayout.NORTH);
		
		//Aqui se configura la tabla
		String[] columnas = {"ID","Nombre","Categoria","Precio","Stock","Control","Disponible"};
		modeloTabla=new DefaultTableModel(columnas,0) {
			@Override
			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 5 || columnIndex==6) return Boolean.class; //Esto es para dibujar una palomita para alternar entre disponible y no disponible
				return super.getColumnClass(columnIndex);
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return column ==3 ||  column==6;//solo podremos darle clik a la comna de disponible para alternarlo
			}
		};
		tablaInventario = new JTable(modeloTabla);
					
		tablaInventario.setRowHeight(35);
		tablaInventario.setFont(new Font("Segoe UI",Font.PLAIN,16));
		tablaInventario.setForeground(Color.WHITE);
		tablaInventario.setBackground(new Color(60,60,60));
		tablaInventario.setSelectionBackground(new Color(46,204,113));
		tablaInventario.getTableHeader().setBackground(new Color(45,45,45));
		tablaInventario.getTableHeader().setForeground(Color.white);
		tablaInventario.getTableHeader().setFont(new Font("Segoe UI",Font.PLAIN,15));
		
		JScrollPane scrollTabla= new JScrollPane(tablaInventario);
		scrollTabla.getViewport().setBackground(new Color(45,45,45));
		panelCentro.add(scrollTabla,BorderLayout.CENTER);
		add(panelCentro,BorderLayout.CENTER);
		
		//Ahora haremos los botones los pondremos en el panel derecho
		JPanel panelDerecho = new JPanel();
		panelDerecho.setBackground(new Color(45,45,45));
		panelDerecho.setPreferredSize(new Dimension(200, 40));
		panelDerecho.setLayout(new BoxLayout(panelDerecho,BoxLayout.Y_AXIS));
		panelDerecho.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(100,100,100)), new EmptyBorder(20,15,20,15)
				));
		
		
		
		JLabel lblStock = new JLabel("Sumar Stock");
		lblStock.setFont(new Font("Segoe UI",Font.PLAIN,16));
		lblStock.setForeground(Color.WHITE);
		lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		txtSumarStock = new JTextField();
		txtSumarStock.setMaximumSize(new Dimension(200,35));
		txtSumarStock.setFont(new Font("Segoe UI",Font.PLAIN,16));
		txtSumarStock.setHorizontalAlignment(JTextField.CENTER);
		
		btnAddStock= new JButton("+Add Stock");
		estilizarBoton(btnAddStock,new Color(255, 193, 7),Color.WHITE);
		btnAddStock.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAddStock.setMaximumSize(new Dimension(200,40));
		
		panelDerecho.add(Box.createVerticalStrut(50));
		panelDerecho.add(lblStock);
		panelDerecho.add(Box.createVerticalStrut(15));
		panelDerecho.add(txtSumarStock);
		panelDerecho.add(Box.createVerticalStrut(15));
		panelDerecho.add(btnAddStock);
		
		add(panelDerecho,BorderLayout.EAST);
		
		//Ahora otro panel para el boton regresar y aplicar cambios
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,10));
		panelSur.setBackground(new Color (30,30,30));
		btnRegresar = new JButton("Regresar");

		estilizarBoton(btnRegresar,new Color(100, 100, 100),Color.WHITE);
		btnAplicar = new JButton("Aplicar Cambios");
		estilizarBoton(btnAplicar,new Color(46, 200, 100),Color.WHITE);
		
		panelSur.add(btnRegresar);
		panelSur.add(btnAplicar);
		add(panelSur,BorderLayout.SOUTH);
		
		//------Acciones 
		//Esta accion de la tabla es para detectar si hubo cambios 
		modeloTabla.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				
				if(e.getType() == TableModelEvent.UPDATE) {
					hayCambios = true;
			    	btnAplicar.setBackground(new Color(231,76,60));//Indica que hay cambios para aplicar
			    	btnAplicar.setText("Aplicar Cambios*");
				}
			}
			
		});
		//Boton para añadir stock
		btnAddStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Verificamos en que linea se selcciono el prodcutos
				int fila = tablaInventario.getSelectedRow();
				if(fila==-1) {
					JOptionPane.showMessageDialog(null,"Seleccion un producto primero","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				//Verificamos que si lleve control stock
				boolean llevaControl = (boolean) modeloTabla.getValueAt(fila, 5);
				if(!llevaControl){
					JOptionPane.showMessageDialog(null,"Este producto no lleva control de stock","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					int cantidadASumar = Integer.parseInt(txtSumarStock.getText());
					if(cantidadASumar<=0)throw new NumberFormatException();
					int stockActual=(int)modeloTabla.getValueAt(fila, 4);
					int nuevoStock = stockActual+cantidadASumar;
					modeloTabla.setValueAt(nuevoStock,fila,4);
					
					if(nuevoStock>0) {
						modeloTabla.setValueAt(true,fila,6);
					}
					txtSumarStock.setText(""); //Limpiamos
					JOptionPane.showMessageDialog(null,"Operacion exitosa, pulsa aplicar para guardar","Aviso",JOptionPane.INFORMATION_MESSAGE);
				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(null,"Cantidad invalida","Aviso",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnRegresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hayCambios) {
					int resp = JOptionPane.showConfirmDialog(null,"Tiene cambios por guardar, ¿Dese aplicarlos?","Aviso",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if(resp==JOptionPane.YES_OPTION) {
						btnAplicar.doClick();
						salirPanel();
					}else if(resp==JOptionPane.NO_OPTION) {
						cargarInventarioEnTabla();
						salirPanel();
					}
				}else {
					salirPanel();
				}
			}
		});
		//Este boton actualizada el inventario de la base de datos
		btnAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!hayCambios) {
					JOptionPane.showMessageDialog(null,"No hay cambios que guardar","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try(Connection con = ConexionBD.obtenerConexion()) {
					con.setAutoCommit(false);
					String sqlUpdate ="UPDATE inventario SET precio =? , stock = ?, disponible=? WHERE idProd = ?";
					try(PreparedStatement ps = con.prepareStatement(sqlUpdate)){
						for(int i=0; i<modeloTabla.getRowCount();i++) {
							try {
							int idProd = (int)modeloTabla.getValueAt(i,0);
							String precioStr = modeloTabla.getValueAt(i,3).toString();
							double precio = Double.parseDouble(precioStr);
							int stockFinal = (int) modeloTabla.getValueAt(i,4);
							boolean disponible = (boolean) modeloTabla.getValueAt(i, 6);
							ps.setInt(2,stockFinal);
							ps.setDouble(1, precio);
							ps.setBoolean(3,disponible);
							ps.setInt(4,idProd);
							ps.addBatch();
							}
							catch(NumberFormatException ex2) {
								JOptionPane.showMessageDialog(null,"Error","Aviso",JOptionPane.INFORMATION_MESSAGE);
								con.rollback();
								cargarInventarioEnTabla();
								return;
							}
							

						}
						ps.executeBatch();
					}
					con.commit();
					InventarioDB.cargarInventarioDB(); //ACtualizamos la memoria
					hayCambios=false;
			    	btnAplicar.setBackground(new Color(46,200,100));
			    	btnAplicar.setText("Aplicar Cambios");
			    	JOptionPane.showMessageDialog(null,"Inventario actualizado","Aviso",JOptionPane.INFORMATION_MESSAGE);
				}catch(SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null,"Error","Aviso",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		cargarInventarioEnTabla();
	}

    public void setVentanaMain(VentanaMain ventanaMain) {
        this.ventanaMain = ventanaMain;
    }
    public void salirPanel() {
    	if(ventanaMain!=null) {
    		ventanaMain.mostrarMenu(true);
    		ventanaMain.navegarA("MESAS");
    	}
    }
    //Haremos una funcion para cargar los pedidos a la tabla
    public void  cargarInventarioEnTabla() {
    	modeloTabla.setRowCount(0);
    	hayCambios = false;
    	btnAplicar.setBackground(new Color(46,200,100));
    	btnAplicar.setText("Aplicar Cambios");
    	txtSumarStock.setText("");
    	List<Object[]> datos = InventarioDB.obtenerDatosInventario();
    	for(Object[] fila:datos) {
    		modeloTabla.addRow(fila);
    	}
    }
    private void estilizarBoton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                // Efecto Hover: Cambiar color al pasar el mouse (Corregido 'mouseEntered')
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(8,51,162)); // Cambia a Amarillo
                btn.setForeground(Color.black); // Letra Negra
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg); // Regresa a Rojo
                btn.setForeground(Color.white); // Regresa a Blanca
            }
        });
    }
   
}