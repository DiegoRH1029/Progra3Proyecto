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

public class PanelLogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsuario;
    private JPasswordField txtPass;
    private VentanaMain ventanaMain;

    public PanelLogin() {
        setBackground(new Color(30, 30, 30));
        setLayout(new GridBagLayout());
        
        // crear la tarjeta Login
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setBackground(new Color(45, 45, 45));
        panelTarjeta.setLayout(new GridBagLayout());
        
        // borde en la tarjeta 
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(211, 47, 47), 2), new EmptyBorder(40, 50, 40, 50)));
        
        // TITULO
        JLabel lblVentanaLogin = new JLabel("INICIO DE SESION ", SwingConstants.CENTER);
        lblVentanaLogin.setFont(new Font("Impact", Font.PLAIN, 32));
        lblVentanaLogin.setForeground(new Color(255, 193, 7));
        GridBagConstraints gbc_lblVentanaLogin = new GridBagConstraints();
        gbc_lblVentanaLogin.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblVentanaLogin.insets = new Insets(0, 0, 30, 0);
        gbc_lblVentanaLogin.gridx = 0;
        gbc_lblVentanaLogin.gridy = 0;
        panelTarjeta.add(lblVentanaLogin, gbc_lblVentanaLogin);
        
        // LABEL USUARIO
        JLabel lblNewLabel = new JLabel("Usuario:");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNewLabel.setForeground(new Color(200, 200, 200));
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        panelTarjeta.add(lblNewLabel, gbc_lblNewLabel);
        
        // TEXTFIELD USUARIO
        txtUsuario = new JTextField();
        estiloCampo(txtUsuario);
        GridBagConstraints gbc_txtUsuario = new GridBagConstraints();
        gbc_txtUsuario.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtUsuario.insets = new Insets(0, 0, 20, 0);
        gbc_txtUsuario.gridx = 0;
        gbc_txtUsuario.gridy = 2;
        panelTarjeta.add(txtUsuario, gbc_txtUsuario);
        
        // LABEL CONTRASEÑA
        JLabel lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblContrasea.setForeground(new Color(200, 200, 200));
        GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
        gbc_lblContrasea.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblContrasea.insets = new Insets(0, 0, 0, 0);
        gbc_lblContrasea.gridx = 0;
        gbc_lblContrasea.gridy = 3;
        panelTarjeta.add(lblContrasea, gbc_lblContrasea);
        
        // TEXTFIELD CONTRASEÑA
        txtPass = new JPasswordField();
        estiloCampo(txtPass);
        GridBagConstraints gbc_txtPass = new GridBagConstraints();
        gbc_txtPass.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtPass.insets = new Insets(0, 0, 40, 0);
        gbc_txtPass.gridx = 0;
        gbc_txtPass.gridy = 4;
        panelTarjeta.add(txtPass, gbc_txtPass);
     
        // BOTON INGRESAR
        JButton btnIngresar = new JButton("ENTRAR AL SISTEMA");
        btnIngresar.setPreferredSize(new Dimension(300, 50));
        btnIngresar.setBackground(new Color(8,51,162));
        btnIngresar.setForeground(Color.white);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // BOTON Salir
        JButton btnSalir = new JButton("Cerrar Programa");
        btnSalir.setPreferredSize(new Dimension(300, 50));
        btnSalir.setBackground(new Color(211, 47, 47));
        btnSalir.setForeground(Color.white);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(null);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover del boton
        btnIngresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(255, 193, 7));
                btnIngresar.setForeground(Color.black);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(8,51,162));
                btnIngresar.setForeground(Color.white);
            }
        });
        
        btnSalir.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalir.setBackground(new Color(255, 193, 7));
                btnSalir.setForeground(Color.black);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalir.setBackground(new Color(211, 47, 47));
                btnSalir.setForeground(Color.white);
            }
        });
        
        GridBagConstraints gbc_btnIngresar = new GridBagConstraints();
        gbc_btnIngresar.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnIngresar.insets = new Insets(0, 0, 0, 0);
        gbc_btnIngresar.gridx = 0;
        gbc_btnIngresar.gridy = 5;
        panelTarjeta.add(btnIngresar, gbc_btnIngresar);
        
        GridBagConstraints gbc_btnSalir = new GridBagConstraints();
        gbc_btnSalir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSalir.insets = new Insets(0, 0, 0, 0);
        gbc_btnSalir.gridx = 0;
        gbc_btnSalir.gridy = 10;
        panelTarjeta.add(btnSalir, gbc_btnSalir);
        
        add(panelTarjeta);
        
        // EVENTO DEL BOTÓN
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = txtUsuario.getText();
                String pass = new String(txtPass.getPassword());
                /*
                // validamos que los espacios no esten vacios 
                if(user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor , llenar todos los campos");
                    return;
                }
                
                try {
                    String rol = validarUsuario(user, pass);
                    if (rol != null) {
                        
                        // avisaremos a VentanaMAin y le pasaremos el rol
                        if(ventanaMain != null) {
                        	txtUsuario.setText("");
                        	txtPass.setText("");
                            ventanaMain.loginExitoso(user, rol);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Contrasena o usuario incorrectos");
                    }
                    
                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(null, "Error al conectarnos con la BD: " + e2.getMessage());
                }
                */
                //*Temporal
                String rol = "administrador";
                user="Admin";
                ventanaMain.loginExitoso(user, rol);
                //*/
            }
        });
        
        btnSalir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ventanaMain.closeProgram();
        	}
        });
    }

    public void setVentanaMain(VentanaMain ventanaMain) {
        this.ventanaMain = ventanaMain;
    }

    public String validarUsuario(String usuario, String contrasena) throws SQLException {
        Connection con = ConexionBD.obtenerConexion();
        String sql = "SELECT rol FROM usuarios WHERE usuario = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("rol");
        }
        return null;
    }

    private void estiloCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(300, 35));
        campo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        campo.setBackground(new Color(45, 45, 45));
        campo.setForeground(Color.white);
        campo.setCaretColor(Color.white);
        campo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
        
        // Animacion de color al hacer clip en el campo
        campo.addFocusListener(new FocusAdapter() {
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