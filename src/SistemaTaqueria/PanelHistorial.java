//en este panel imprimiremos en una tabla el historial de ingresos de todos los pedidos y su ticket para ver detalles
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
		setBackground(new Color(30,30,30));
		setLayout(new BorderLayout(15,15));
		setBorder(new EmptyBorder(15,15,15,15));
		
		JPanel pnlnorte= new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlnorte.setBackground(new Color(30,30,30));
		JLabel lblTitulo=new JLabel("Historial de Finznzas | Filtro: ");
		lblTitulo.setForeground(new Color(255,193,7));
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
		
		comboFiltro=new JComboBox<>(new String[]{"Todas", "VENTA", "GASTO"});
		btnBuscar=new JButton("Actualizar Tabla");
		
		btnBuscar.setBackground(new Color(46,204, 113));
		btnBuscar.setForeground(Color.white);
		btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		pnlnorte.add(lblTitulo);
		pnlnorte.add(comboFiltro);
		pnlnorte.add(btnBuscar);
		add(pnlnorte,BorderLayout.NORTH);
		
		String[] columnas= {"ID", "Tipo", "Categoria","Monto","Fecha", "Detalle"};
		modeloTabla = new DefaultTableModel(columnas, 0) {			
			@Override
			public boolean isCellEditable(int row, int column) {
			return false;
		}};
		tablaHistorial= new JTable(modeloTabla);
		tablaHistorial.setBackground(new Color(60,60,60));
		tablaHistorial.setForeground(Color.white);
		tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablaHistorial.getTableHeader().setBackground(new Color(45,45,45));
		tablaHistorial.getTableHeader().setForeground(Color.white);
		JScrollPane scrollPane= new JScrollPane(tablaHistorial);
		scrollPane.getViewport().setBackground(new Color(45,45,45));
		add(scrollPane, BorderLayout.CENTER);
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDatosEnTabla();
			}
		});
		
			
	}
	public void cargarDatosEnTabla() {
		modeloTabla.setRowCount(0);
		String filtro=comboFiltro.getSelectedItem().toString();
		String sql="SELECT * FROM finanzas";
		
		if (!filtro.equals("Todas")) {
			sql+=" WHERE tipo = '"+filtro+"'";
		}
		sql+=" ORDER BY fecha DESC";
		
		try {
			Connection con=ConexionBD.obtenerConexion();
			PreparedStatement ps= con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				modeloTabla.addRow(new Object[] {rs.getInt("idTransaccion"),
						rs.getString("tipo"),
						rs.getString("categoria"),
						"$"+ rs.getDouble("monto"), rs.getString("fecha"),
						rs.getString("detalle")});
			}
			con.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar historial: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			
		}
	}
	public void setVentanaMain() {
		this.ventanaMain=ventanaMain;
	}

	
	}
	


