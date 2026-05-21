package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class PanelOrdenes extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;

	/**
	 * Create the panel.
	 */
	public PanelOrdenes() {
		
		JLabel lblNewLabel = new JLabel("Esto es panel ordenes");
		add(lblNewLabel);

	}
	
	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain = ventanaMain;
	}

}
