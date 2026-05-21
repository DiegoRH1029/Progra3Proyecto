package SistemaTaqueria;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelLogin extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtUsuario;
	private JTextField txtPass;
	private VentanaMain ventanaMain;
	//Declaramos usuario y contraseña (para prueba)
	private String usuario = "Diego";
	private String pass = "1234";

	/**
	 * Create the panel.
	 */
	public PanelLogin() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(418, 265, 230, 49);
		add(lblNewLabel);
		
		txtUsuario = new JTextField();
		txtUsuario.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUsuario.setBounds(339, 324, 221, 33);
		add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblContrasea.setBounds(418, 356, 230, 49);
		add(lblContrasea);
		
		txtPass = new JTextField();
		txtPass.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPass.setColumns(10);
		txtPass.setBounds(339, 404, 221, 33);
		add(txtPass);
		
		JLabel lblLogo = new JLabel("LOGO");
		lblLogo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblLogo.setBounds(394, 206, 230, 49);
		add(lblLogo);
		
		JLabel lblVentanaLogin = new JLabel("Ventana Login");
		lblVentanaLogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblVentanaLogin.setBounds(339, 45, 230, 49);
		add(lblVentanaLogin);
		
		JButton btnIngresar = new JButton("Ingresar");

		btnIngresar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnIngresar.setBounds(323, 476, 254, 55);
		add(btnIngresar);
		
		//----------Acciones
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtUsuario.getText().equals(usuario)) {
					if(txtPass.getText().equals(pass)) {
						ventanaMain.loginExitoso(usuario);
					}
				}
				else {
					JOptionPane.showMessageDialog(ventanaMain, "Contra o usuario incorrecto",":(",JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});

	}
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}
	
}
