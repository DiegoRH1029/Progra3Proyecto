package SistemaTaqueria;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class PanelLogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsuario;
    private JPasswordField txtPass;
    private VentanaMain ventanaMain;
    

    public PanelLogin() {
    	setBackground(new Color(30, 30 ,30));
    	setLayout(new GridBagLayout());
    	
    	// Crear la tarjeta Login
    	JPanel panelTarjeta = new JPanel();
    	panelTarjeta.setBackground(new Color(45, 45, 45));
    	panelTarjeta.setLayout(new GridBagLayout());
    	
    	// Borde en la tarjeta 
    	panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
    			BorderFactory.createLineBorder(new Color(211, 47, 47),2),new EmptyBorder(40, 50, 40, 50)));
    	
        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.fill = GridBagConstraints.HORIZONTAL;
        gbcLogo.insets = new Insets(10, 0 ,15, 0);
        gbcLogo.gridx=0;
        gbcLogo.gridy=0;
        
        JLabel lblLogo=new JLabel(" ", SwingConstants.CENTER);
        lblLogo.setIcon(new ImageIcon("C:\\Users\\Diego Hernandez\\Downloads\\image-removebg-preview (1) (1) (1).png"));
        panelTarjeta.add(lblLogo, gbcLogo);

        // ------------------------------------------------------------------
    	// 1. TÍTULO
        // ------------------------------------------------------------------
        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.fill = GridBagConstraints.HORIZONTAL;
        gbcTitulo.insets = new Insets(0, 0, 30, 0);
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 1;
        
    	JLabel lblVentanaLogin = new JLabel("INICIO DE SESION", SwingConstants.CENTER);
        lblVentanaLogin.setFont(new Font("Impact", Font.PLAIN, 32));
        lblVentanaLogin.setForeground(new Color(255, 193, 7));
        panelTarjeta.add(lblVentanaLogin, gbcTitulo);
    	
        // ------------------------------------------------------------------
        // 2. LABEL USUARIO
        // ------------------------------------------------------------------
        GridBagConstraints gbcLblUser = new GridBagConstraints();
        gbcLblUser.fill = GridBagConstraints.HORIZONTAL;
        gbcLblUser.insets = new Insets(0, 0, 0, 0);
        gbcLblUser.gridx = 0;
        gbcLblUser.gridy = 2;
        
        JLabel lblNewLabel = new JLabel("Usuario:");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNewLabel.setForeground(new Color(200, 200, 200));
        panelTarjeta.add(lblNewLabel, gbcLblUser);
        
        // ------------------------------------------------------------------
        // 3. INPUT USUARIO
        // ------------------------------------------------------------------
        GridBagConstraints gbcTxtUser = new GridBagConstraints();
        gbcTxtUser.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtUser.insets = new Insets(0, 0, 20, 0);
        gbcTxtUser.gridx = 0;
        gbcTxtUser.gridy = 3;
        
        txtUsuario = new JTextField();
        estiloCampo(txtUsuario);
        panelTarjeta.add(txtUsuario, gbcTxtUser);
        
        // ------------------------------------------------------------------
        // 4. LABEL CONTRASEÑA
        // ------------------------------------------------------------------
        GridBagConstraints gbcLblPass = new GridBagConstraints();
        gbcLblPass.fill = GridBagConstraints.HORIZONTAL;
        gbcLblPass.insets = new Insets(0, 0, 0, 0);
        gbcLblPass.gridx = 0;
        gbcLblPass.gridy = 4;
        
        JLabel lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblContrasea.setForeground(new Color(200, 200, 200));
        panelTarjeta.add(lblContrasea, gbcLblPass);
        
        // ------------------------------------------------------------------
        // 5. INPUT CONTRASEÑA
        // ------------------------------------------------------------------
        GridBagConstraints gbcTxtPass = new GridBagConstraints();
        gbcTxtPass.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtPass.insets = new Insets(0, 0, 40, 0);
        gbcTxtPass.gridx = 0;
        gbcTxtPass.gridy = 5;
        
        txtPass = new JPasswordField();
        estiloCampo(txtPass);
        panelTarjeta.add(txtPass, gbcTxtPass);
     
        // ------------------------------------------------------------------
        // 6. BOTÓN INGRESAR
        // ------------------------------------------------------------------
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.fill = GridBagConstraints.HORIZONTAL;
        gbcBtn.insets = new Insets(0, 0, 0, 0);
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 6;
        
        JButton btnIngresar = new JButton("ENTRAR AL SISTEMA");
        btnIngresar.setPreferredSize(new Dimension(300, 50));
        btnIngresar.setBackground(new Color(211, 47, 47));
        btnIngresar.setForeground(Color.white);
        btnIngresar.setFont(new Font("Segoe UI",Font.BOLD, 16));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover del boton
        btnIngresar.addMouseListener(new MouseAdapter() {
        	public void mouseEntered(java.awt.event.MouseEvent evt) {
    			btnIngresar.setBackground(new Color(255, 193, 7));
    			btnIngresar.setForeground(Color.black);
    		}
    		public void mouseExited(java.awt.event.MouseEvent evt) {
    			btnIngresar.setBackground(new Color(211, 47, 47));
    			btnIngresar.setForeground(Color.white);
    		}
		});
        
        panelTarjeta.add(btnIngresar, gbcBtn);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx =0;
        gbcMain.gridy=0;
        gbcMain.weightx=1.0;
        gbcMain.weighty=1.0;
        gbcMain.anchor=GridBagConstraints.CENTER;
        add(panelTarjeta, gbcMain);
        
        JButton btnSalir= new JButton("SALIR");
        btnSalir.setPreferredSize(new Dimension(100, 40));
        btnSalir.setBackground(new Color(60, 60 ,60));
        btnSalir.setForeground(Color.white);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(null);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
         btnSalir.addMouseListener(new MouseAdapter() {
        	 public void mouseEntered(java.awt.event.MouseEvent evt) {
     			btnIngresar.setBackground(new Color(211, 47, 47));
     		}
     		public void mouseExited(java.awt.event.MouseEvent evt) {
     			btnIngresar.setBackground(new Color(60, 60, 60));
     		}
		});
         GridBagConstraints gbcMainSalir= new GridBagConstraints();
         gbcMainSalir.gridx=0;
         gbcMainSalir.gridy=1;
         gbcMainSalir.anchor=GridBagConstraints.SOUTHEAST;
         gbcMainSalir.insets=new Insets(0,0,30,40);
         add(btnSalir, gbcMainSalir);
         btnSalir.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			System.exit(0);
				
			}
		});
        
        add(panelTarjeta);
        
        // ------------------------------------------------------------------
        // ACCIONES
        // ------------------------------------------------------------------
        btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = txtUsuario.getText();
				String pass = new String(txtPass.getPassword());
				// Validamos que los espacios no esten vacios 
				if(user.isEmpty() || pass.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor , llenar todos los campos");
					return;
				}
				try {
					String rol = validarUsuario(user, pass);
					if (rol != null) {
						JOptionPane.showMessageDialog(null, "Bienvenido " + user + " !");
						// Avisaremos a VentanaMAin y le pasaremos el rol
						if(ventanaMain != null) {
							ventanaMain.loginExitoso(user, rol);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Contrasena o usuario incorrectos");
					}
					
				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, "Error al conectarnos con la BD" + e2.getMessage());
				}
			}
		});
	}

	public void setVentanaMain(VentanaMain ventanaMain) {
		this.ventanaMain=ventanaMain;
	}

	public String validarUsuario(String usuario, String contrasena) throws SQLException{
		Connection con=ConexionBD.obtenerConexion();
		String sql ="SELECT rol FROM usuarios WHERE usuario = ? AND password = ?";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, usuario);
		ps.setString(2, contrasena);
		ResultSet rs= ps.executeQuery();
		if (rs.next()) {
			return rs.getString("rol");
		}
		return null;
	}

	private void estiloCampo(JTextField campo) {
		campo.setPreferredSize(new Dimension(300,35));
		campo.setFont(new Font("Segoe UI",Font.BOLD, 16));
		campo.setBackground(new Color(45, 45, 45));
		campo.setForeground(Color.white);
		campo.setCaretColor(Color.white);
		campo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
		// Animacion de color al hacer clip en el campo
		campo.addFocusListener(new  FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				campo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 193, 7)));
			}
			@Override
			public void focusLost(FocusEvent e) {
				campo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
			}
		});
	}
}