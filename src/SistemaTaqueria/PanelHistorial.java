// Este panel es la "maquina del tiempo" de las finanzas. 
// Aqui el administrador podra ver todo el flujo de dinero (entradas y salidas) en forma de tabla.
package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelHistorial extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;
	private JTable tablaHistorial;
	private DefaultTableModel modeloTabla;
	private JComboBox<String> comboFiltro;
	private JButton btnBuscar;
	
	public PanelHistorial() {
		// 1. Configuracion del chasis del panel (Fondo oscuro elegante)
		setBackground(new Color(30,30,30));
		setLayout(new BorderLayout(15,15));
		setBorder(new EmptyBorder(15,15,15,15)); // Aire para que no se pegue a las orillas
		
		// =========================================================
		// PANEL NORTE: CONTROLES DE BUSQUEDA Y FILTRADO
		// =========================================================
		JPanel pnlnorte= new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlnorte.setBackground(new Color(30,30,30));
		
		JLabel lblTitulo=new JLabel("Historial de Finanzas | Filtro: ");
		lblTitulo.setForeground(new Color(255,193,7)); // Amarillo para resaltar
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
		
		// El filtro que le dira a MySQL que informacion queremos traer
		comboFiltro=new JComboBox<>(new String[]{"Todas", "VENTA", "GASTO"});
		
		btnBuscar=new JButton("Actualizar Tabla");
		btnBuscar.setBackground(new Color(46,204, 113)); // Verde accion
		btnBuscar.setForeground(Color.white);
		btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		pnlnorte.add(lblTitulo);
		pnlnorte.add(comboFiltro);
		pnlnorte.add(btnBuscar);
		add(pnlnorte,BorderLayout.NORTH);
		
		// =========================================================
		// ZONA CENTRAL: LA TABLA DE DATOS
		// =========================================================
		String[] columnas= {"ID", "Tipo", "Categoria","Monto","Fecha", "Detalle"};
		
		// EL TRUCO DE LA TABLA INTOCABLE:
		// Sobrescribimos el modelo para que 'isCellEditable' devuelva false.
		// Asi evitamos que un usuario altere el historial de ventas por accidente con un doble clic.
		modeloTabla = new DefaultTableModel(columnas, 0) {			
			@Override
			public boolean isCellEditable(int row, int column) {
			return false;
		}};
		
		// Diseno visual de la tabla
		tablaHistorial= new JTable(modeloTabla);
		tablaHistorial.setBackground(new Color(60,60,60));
		tablaHistorial.setForeground(Color.white);
		tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablaHistorial.getTableHeader().setBackground(new Color(45,45,45));
		tablaHistorial.getTableHeader().setForeground(Color.white);
		
		// JScrollPane es necesario para que aparezca la barra espaciadora si hay muchas ventas
		JScrollPane scrollPane= new JScrollPane(tablaHistorial);
		scrollPane.getViewport().setBackground(new Color(45,45,45));
		add(scrollPane, BorderLayout.CENTER);
		
		// =========================================================
		// ACCIONES
		// =========================================================
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDatosEnTabla(); // Ejecuta la logica de extraccion
			}
		});
		
			
	}
	
	// =========================================================
	// METODOS DE LOGICA DE BASE DE DATOS
	// =========================================================
	public void cargarDatosEnTabla() {
		// 1. Limpiamos la tabla visual para que no se empalmen los datos viejos con los nuevos
		modeloTabla.setRowCount(0);
		
		String filtro=comboFiltro.getSelectedItem().toString();
		
		// 2. MAGIA SQL: Armamos la consulta dinamicamente
		String sql="SELECT * FROM finanzas";
		
		// Si el usuario quiere filtrar, le pegamos la condicion al texto de SQL
		if (!filtro.equals("Todas")) {
			sql+=" WHERE tipo = '"+filtro+"'";
		}
		// Siempre ordenamos por los mas recientes primero
		sql+=" ORDER BY fecha DESC";
		
		// 3. Ejecucion y volcado de datos
		try {
			Connection con=ConexionBD.obtenerConexion();
			PreparedStatement ps= con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				// Metemos fila por fila a la tabla visual
				modeloTabla.addRow(new Object[] {
						rs.getInt("idTransaccion"),
						rs.getString("tipo"),
						rs.getString("categoria"),
						// Le concatenamos el signo de pesos directo al String para que se vea mas profesional
						"$"+ rs.getDouble("monto"), 
						rs.getString("fecha"),
						rs.getString("detalle")
				});
			}
			con.close(); // Cerramos la conexion para liberar memoria
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar historial: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// CORRECCION APLICADA AQUI: Se agrego la variable (VentanaMain ventanaMain) dentro de los parentesis
	// para que pueda recibir el motor principal correctamente y no de error.
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}

}