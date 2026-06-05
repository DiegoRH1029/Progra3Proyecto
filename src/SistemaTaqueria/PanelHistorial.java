//en este panel imprimiremos en una tabla el historial de ingresos de todos los pedidos y su ticket para ver detalles
package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class PanelHistorial extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentanaMain ventanaMain;

	/**
	 * Create the panel.
	 */
	public PanelHistorial() {
		
		JLabel lblNewLabel = new JLabel("Historial");
		add(lblNewLabel);

	}

	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain = ventanaMain;
	}
	

}
