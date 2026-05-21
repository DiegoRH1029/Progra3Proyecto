package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Dimension;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class PanelBienvenida extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;
	private JLabel lblUsuario;
	/**
	 * Create the panel.
	 */
	public PanelBienvenida() {
		setAlignmentY(Component.BOTTOM_ALIGNMENT);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("BIENVENIDO");
		lblNewLabel.setBounds(305, 138, 300, 100);
		lblNewLabel.setPreferredSize(new Dimension(300, 100));
		lblNewLabel.setMaximumSize(new Dimension(300, 200));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		add(lblNewLabel);
		
		lblUsuario = new JLabel("USUARIO");
		lblUsuario.setBounds(305, 289, 300, 100);
		lblUsuario.setPreferredSize(new Dimension(300, 100));
		lblUsuario.setMaximumSize(new Dimension(300, 200));
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 28));
		add(lblUsuario);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel, lblUsuario}));

	}
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}
	public void setTextUsuario(String usuario) {
		lblUsuario.setText(usuario);
	}

}
