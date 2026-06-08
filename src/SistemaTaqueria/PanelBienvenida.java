//Clase de jpnael que imprime el panel de bienbendia
package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;

public class PanelBienvenida extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;
	private JLabel lblUsuario;
	/**
	 * Create the panel.
	 */
	public PanelBienvenida() {
		setBackground(new Color(30,30,30));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbcLo= new GridBagConstraints();
		gbcLo.fill= GridBagConstraints.HORIZONTAL;
		gbcLo.gridx=0;
		//Para imprimir una imagen 
		gbcLo.gridy =0;
		gbcLo.insets = new Insets(0,0, 30,0);
		JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon("C:\\Users\\Diego Hernandez\\Downloads\\image-removebg-preview (2) (1) (1) (1).png"));
		add(lblLogo, gbcLo);
		
		//Bienvenida
		GridBagConstraints gbcBien= new GridBagConstraints();
		gbcBien.gridy=1;
		gbcBien.insets= new Insets(0, 0, 10, 0);
		
		JLabel lblBienvenido = new JLabel("BIENVENIDO AL SISTEMA!", SwingConstants.CENTER);
		lblBienvenido.setFont(new Font("Impact", Font.PLAIN, 46));
		lblBienvenido.setForeground(new Color(255,193,7));
		add(lblBienvenido, gbcBien);
		GridBagConstraints gbcUsu= new GridBagConstraints();
		gbcUsu.gridy=2;
		gbcUsu.insets = new Insets(0, 0, 0, 0);
		
		//Imprime el usuario 
		lblUsuario = new JLabel("USUARIO", SwingConstants.CENTER);
		lblUsuario.setFont(new Font("Segoe UI",Font.BOLD, 36));
	   lblLogo.setForeground(new Color(200,200,200));
	   add(lblUsuario,gbcUsu);
	}
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}
	public void setTextUsuario(String usuario) {
		lblUsuario.setText(usuario);
	}

}