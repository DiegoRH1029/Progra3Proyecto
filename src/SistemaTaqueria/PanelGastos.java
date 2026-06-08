package SistemaTaqueria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// Este panel es el encargado de registrar las salidas de dinero (gastos) de la taqueria
// Usamos un diseno de "Tarjeta" centrada para que se vea moderno y no como un formulario viejo
public class PanelGastos extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtMonto;
	private JTextField txtDetalle;
	private JComboBox<String> comboCategoria;
	private JButton btnGuardarGasto;
	
	public PanelGastos() {
		setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(30, 50, 30, 50)); // Margen exterior para que respire la pantalla

        // TITULO PRINCIPAL
        JLabel lblTitulo = new JLabel("REGISTRO DE GASTOS", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(255, 193, 7)); // Amarillo
        lblTitulo.setFont(new Font("Impact", Font.PLAIN, 32));
        add(lblTitulo, BorderLayout.NORTH);

        // CONTENEDOR CENTRAL (EL TRUCO DEL CENTRADO)
        // Al usar un GridBagLayout vacio en el padre, cualquier cosa que le metamos 
        // se va a centrar automaticamente como un iman, evitando que el formulario se estire deforme.
        JPanel panelContenedorForm = new JPanel(new GridBagLayout());
        panelContenedorForm.setBackground(new Color(30, 30, 30));
        
        // TARJETA DEL FORMULARIO (Diseno oscuro elegante)
        // Este es el cuadrito gris que contiene los inputs
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(45, 45, 45));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(211, 47, 47), 2), // Borde rojo tenue (psicologia de color para "Gasto/Peligro")
            new EmptyBorder(40, 50, 40, 50) // Espaciado interno de la tarjeta para que no se pegue el texto
        ));

        // gbc es nuestro control remoto para acomodar cosas en la cuadricula de la tarjeta
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Que los campos se estiren a lo ancho
        gbc.insets = new Insets(15, 10, 15, 10); // Margen entre cada fila (arriba, izq, abajo, der)
        gbc.anchor = GridBagConstraints.WEST; // Alineado a la izquierda

        // 1. CATEGORIA
        JLabel lblCat = new JLabel("Categoría del Gasto:");
        estilizarLabel(lblCat);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; // Columna 0, Fila 0
        panelFormulario.add(lblCat, gbc);

        comboCategoria = new JComboBox<>(new String[] {"Insumos", "Servicios", "Otros"});
        estilizarComboBox(comboCategoria);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; // Columna 1, Fila 0 (Este ocupa mas espacio)
        panelFormulario.add(comboCategoria, gbc);

        // 2. MONTO
        JLabel lblMonto = new JLabel("Monto: $");
        estilizarLabel(lblMonto);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; // Fila 1
        panelFormulario.add(lblMonto, gbc);

        txtMonto = new JTextField(15);
        estilizarTextField(txtMonto);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panelFormulario.add(txtMonto, gbc);

        // 3. JUSTIFICACION
        JLabel lblDetalle = new JLabel("Justificación:");
        estilizarLabel(lblDetalle);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; // Fila 2
        panelFormulario.add(lblDetalle, gbc);

        txtDetalle = new JTextField(15);
        estilizarTextField(txtDetalle);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        panelFormulario.add(txtDetalle, gbc);

        // 4. BOTON GUARDAR
        btnGuardarGasto = new JButton("Registrar Salida de Dinero");
        btnGuardarGasto.setBackground(new Color(211, 47, 47)); // Rojo
        btnGuardarGasto.setForeground(Color.WHITE);
        btnGuardarGasto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardarGasto.setFocusPainted(false);
        btnGuardarGasto.setBorderPainted(false);
        btnGuardarGasto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarGasto.setPreferredSize(new Dimension(250, 45));

        // Efecto Hover: Magia visual para que el boton reaccione al raton
        btnGuardarGasto.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnGuardarGasto.setBackground(new Color(255, 193, 7)); // Cambia a amarillo
                btnGuardarGasto.setForeground(Color.BLACK);
            }
            public void mouseExited(MouseEvent evt) {
                btnGuardarGasto.setBackground(new Color(211, 47, 47)); // Regresa a rojo
                btnGuardarGasto.setForeground(Color.WHITE);
            }
        });

        // Configuramcion especial para que el boton abarque las 2 columnas y quede centrado abajo
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0); // Mas espacio arriba del boton para separarlo de los inputs
        panelFormulario.add(btnGuardarGasto, gbc);

        panelContenedorForm.add(panelFormulario);
        add(panelContenedorForm, BorderLayout.CENTER);
		
		// Accion del boton (La logica fuerte)
		btnGuardarGasto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 1. Seguro anti-tontos: Evitar que manden campos vacios y rompan la BD
				if(txtMonto.getText().trim().isEmpty()||txtDetalle.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Llena todoslos campos", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					// 2. Extraemos la info
					double monto=Double.parseDouble(txtMonto.getText());
					String categoriaString =comboCategoria.getSelectedItem().toString();
					String detalle=txtDetalle.getText();
					
					// 3. EL TRUCO CONTABLE: aseguramos que el monto sea siempre negativo
					// Usamos Math.abs para quitarle el signo si el usuario lo puso negativo, y luego le clavamos un menos.
					// Asi forzamos que sea un retiro de dinero real en el historial.
					monto=-Math.abs(monto);
					
					// 4. Insercion segura a MySQL
					Connection con=ConexionBD.obtenerConexion();
					String sql="INSERT INTO finanzas (tipo, categoria, monto, detalle, fecha) VALUES ('GASTO',?,?,?,NOW())";
					PreparedStatement pStatement =con.prepareStatement(sql);
					pStatement.setString(1, categoriaString);
					pStatement.setDouble(2, monto);
					pStatement.setString(3, detalle);
					
					pStatement.executeUpdate(); // Ejecutamos la consulta
					con.close(); // Cerramos la puerta
					
					JOptionPane.showMessageDialog(null, "Gasto guardado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
					
					// 5. Dejar todo limpio para el siguiente gasto
					txtMonto.setText("");
					txtDetalle.setText("");
					
				} catch (NumberFormatException ex) {
					// Si el usuario pone letras en lugar de numeros en el monto, atrapamos el error aqui
					JOptionPane.showMessageDialog(null, "El monto debe ser numero valido.","Error", JOptionPane.ERROR_MESSAGE);
				}catch (SQLException e2) {
					// Si MySQL falla (por ejemplo, servidor caido)
					JOptionPane.showMessageDialog(null, "Error en la Base de Datos: "+e2.getMessage(),"Error BD", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

	}
	
	// =========================================================
    // METODOS AUXILIARES DE DISENO (Limpieza visual de codigo)
    // =========================================================
	
	private void estilizarLabel(JLabel lbl) {
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void estilizarTextField(JTextField txt) {
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBackground(new Color(60, 60, 60)); // Fondo gris
        txt.setForeground(Color.WHITE); // Letra blanca
        txt.setCaretColor(Color.WHITE); // El palito que parpadea al escribir
        // Borde compuesto: Un borde gris por fuera + margen transparente por dentro
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            new EmptyBorder(5, 10, 5, 10) 
        ));
    }

    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setPreferredSize(new Dimension(200, 35));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        combo.setBackground(new Color(60, 60, 60));
        combo.setForeground(Color.WHITE);
    }

}