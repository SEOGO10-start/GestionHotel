
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Systeme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.awt.Color;



/**
 *
 * @author MOUSSA SEOGO
 */
public class Salarie extends javax.swing.JFrame {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel tableModel;
    private javax.swing.JComboBox<String> cbtype;
    private String userRole; // Rôle de l'utilisateur connecté
    private int currentUserId; // ID de l'utilisateur connecté
    private JLabel lblPasswordStrength = new JLabel();
    
    public Salarie(int userId, String role, Connection connection) {
        try {
            this.con = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");
            this.currentUserId = 1; // ID par défaut
            this.userRole = "ADMIN"; // Rôle par défaut
            initializeRolesAndPermissions();
            initComponents();
            Connect();
            initTable();
            loadSalaries();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage());
        }    
    }
    
    public boolean authentifier(String nom, String mdp) {
        try {
            String sql = "SELECT type FROM salaries WHERE nom=? AND mdp=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setString(2, hashPassword(mdp));
    
            rs = pst.executeQuery();
            if (rs.next()) {
                JFrame dashboard = new JFrame("Bienvenue " + nom);
                dashboard.setSize(600, 400);
                dashboard.setVisible(true);
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void Connect() {
        try {
            if (con == null || con.isClosed()) {
               Class.forName("org.sqlite.JDBC");
               con = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver SQLite introuvable: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage());
        }
    }
    
    // Pour la création de compte par l'admin
    public String generateTempPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    private void initTable() {
        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nom", "Mot de passe", "Type"} // Ajout de la colonne Mot de passe
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre toutes les cellules non éditables
            }
        };
        jTable1.setModel(tableModel);
    }
    
    public void initializeRolesAndPermissions() {
        try {
            // Insertion des rôles de base
            String[] roles = {"ADMIN", "MANAGER", "RECEPTIONNISTE"};
            for (String role : roles) {
                pst = con.prepareStatement("INSERT OR IGNORE INTO roles (name) VALUES (?)");
                pst.setString(1, role);
                pst.executeUpdate();
            }
        
            // Insertion des permissions de base
            String[] permissions = {
                "CREATE_SALARIES", "EDIT_SALARIES", "DELETE_SALARIES",
                "VIEW_SALARIES", "CREATE_RESERVATIONS", "EDIT_RESERVATIONS",
                "DELETE_RESERVATIONS", "VIEW_RESERVATIONS", "GENERATE_REPORTS"
            };
        
            for (String perm : permissions) {
                pst = con.prepareStatement("INSERT OR IGNORE INTO permissions (code) VALUES (?)");
                pst.setString(1, perm);
                pst.executeUpdate();
            }
        
            // Attribution des permissions aux rôles
            // ADMIN a toutes les permissions
            pst = con.prepareStatement(
                "INSERT OR IGNORE INTO role_permissions (role_id, permission_id) " +
                "SELECT r.id, p.id FROM roles r, permissions p WHERE r.name = 'ADMIN'");
            pst.executeUpdate();
        
            // MANAGER
            String[] managerPerms = {"VIEW_SALARIES", "CREATE_RESERVATIONS", 
                               "EDIT_RESERVATIONS", "VIEW_RESERVATIONS", "GENERATE_REPORTS"};
            for (String perm : managerPerms) {
                pst = con.prepareStatement(
                    "INSERT OR IGNORE INTO role_permissions (role_id, permission_id) " +
                    "SELECT r.id, p.id FROM roles r, permissions p " +
                    "WHERE r.name = 'MANAGER' AND p.code = ?");
                pst.setString(1, perm);
                pst.executeUpdate();
            }
        
            // RECEPTIONNISTE
            String[] receptionPerms = {"CREATE_RESERVATIONS", "EDIT_RESERVATIONS", "VIEW_RESERVATIONS"};
            for (String perm : receptionPerms) {
                pst = con.prepareStatement(
                    "INSERT OR IGNORE INTO role_permissions (role_id, permission_id) " +
                    "SELECT r.id, p.id FROM roles r, permissions p " +
                    "WHERE r.name = 'RECEPTIONNISTE' AND p.code = ?");
                pst.setString(1, perm);
                pst.executeUpdate();
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur initialisation rôles: " + e.getMessage());
        }
    }   
    // Dans chaque fenêtre sensible
    private void initSessionTimer() {
        Timer timer = new Timer(30*60*1000, e -> {
            JOptionPane.showMessageDialog(this, "Session expirée");
            this.dispose();
            new Login().setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void logAction(String action) {
        try {
            String sql = "INSERT INTO logs (user_id, action, date) VALUES (?, ?, datetime('now'))";
            pst = con.prepareStatement(sql);
            pst.setInt(1, currentUserId);
            pst.setString(2, action);
            pst.executeUpdate();
        } catch(SQLException e) {
            System.err.println("Erreur de journalisation: " + e.getMessage());
        }
    }
    private void loadSalaries() {
        try {
            Connect();
            String sql = "SELECT id, nom, mdp, type FROM salaries"; // Ajout de mdp dans la requête
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
            tableModel.setRowCount(0);
        
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom"),
                    "********", // On masque le mot de passe pour des raisons de sécurité
                    rs.getString("type")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
    // Méthode pour vider les champs
    private void clearFields() {
        // Vérifiez et adaptez ces noms avec vos vrais composants
        txtnom.setText(""); 
        jPasswordField1.setText("");
        txttype.setSelectedIndex(0); // Utilisez txttype (tel que déclaré) au lieu de txtType
        txtrechercher.setText("");
    }
    
    private boolean checkPermission(String permissionCode) {
        // L'admin a toujours tous les droits
        if ("ADMIN".equals(this.userRole)) {
            return true;
        }
    
        try {
            String sql = "SELECT 1 FROM role_permissions rp " +
                "JOIN permissions p ON rp.permission_id = p.id " +
                "JOIN roles r ON rp.role_id = r.id " +
                "JOIN salaries s ON s.role_id = r.id " +
                "WHERE s.id = ? AND p.code = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, currentUserId);
            pst.setString(2, permissionCode);
        
            return pst.executeQuery().next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur vérification permission: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private  boolean isNameAlreadyExists(String nom) {
        try {
            String sql = "SELECT 1 FROM salaries WHERE nom = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, nom);
                try (ResultSet rs = pst.executeQuery()) {
                   return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // ou gérer autrement
        }
    } 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblnom = new javax.swing.JLabel();
        txtnom = new javax.swing.JTextField();
        lblmdp = new javax.swing.JLabel();
        btnAjouter = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnSupprimer = new javax.swing.JButton();
        btnAcualiser = new javax.swing.JButton();
        txtrechercher = new javax.swing.JTextField();
        lblMenu = new javax.swing.JLabel();
        lbltype = new javax.swing.JLabel();
        txttype = new javax.swing.JComboBox<>();
        btnRechercher = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setBackground(new java.awt.Color(51, 0, 204));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("IFORMATIONS SALARIES");

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lblnom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblnom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblnom.setText("NOM UTILISATEUR");

        txtnom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnomActionPerformed(evt);
            }
        });

        lblmdp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblmdp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblmdp.setText("MOT DE PASSE");

        btnAjouter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAjouter.setText("AJOUTER");
        btnAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterActionPerformed(evt);
            }
        });

        btnModifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnModifier.setText("MODIFIER");
        btnModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifierActionPerformed(evt);
            }
        });

        btnSupprimer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSupprimer.setText("SUPPRIMER");
        btnSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimerActionPerformed(evt);
            }
        });

        btnAcualiser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAcualiser.setText("ACTUALISER");
        btnAcualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcualiserActionPerformed(evt);
            }
        });

        txtrechercher.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtrechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrechercherActionPerformed(evt);
            }
        });

        lblMenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMenu.setForeground(new java.awt.Color(255, 0, 0));
        lblMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenu.setText("MENUE");
        lblMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenuMouseClicked(evt);
            }
        });

        lbltype.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltype.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltype.setText("TYPE");
        lbltype.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lbltypeAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txttype.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txttype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "MANAGER", "RECEPTIONNISTE" }));
        txttype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttypeActionPerformed(evt);
            }
        });

        btnRechercher.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnRechercher.setText("Rechercher");
        btnRechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercherActionPerformed(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnAjouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRechercher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAcualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtrechercher)
                                    .addComponent(btnModifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtnom)
                                    .addComponent(lblnom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbltype, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblmdp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txttype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblnom, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnom, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblmdp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbltype, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttype, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAjouter)
                            .addComponent(btnModifier))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAcualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRechercher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtrechercher)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenuMouseClicked
        // On va faire un retour depuis le menue principale
        Salarie.super.dispose();
        MenuePrincipale men = new MenuePrincipale();
        men.setVisible(true);
    }//GEN-LAST:event_lblMenuMouseClicked

    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed
                                         
        // 1. Vérification des permissions
        if (!checkPermission("CREATE_SALARIES")) {
            JOptionPane.showMessageDialog(this, 
                "Action non autorisée - Droits insuffisants",
                "Erreur de permission", 
                JOptionPane.WARNING_MESSAGE);
            logAction("TENTATIVE_AJOUT_SANS_PERMISSION");
            return;
        }

        // 2. Validation des champs
        String nom = txtnom.getText().trim();
        String mdp = jPasswordField1.getText().trim();
        String type = txttype.getSelectedItem().toString();

        // Validation du nom
        if (nom.isEmpty() || !nom.matches("[a-zA-ZÀ-ÿ -]{2,50}")) {
            JOptionPane.showMessageDialog(this,
                "Nom invalide (2-50 lettres, espaces ou traits d'union)",
                "Erreur de saisie",
                JOptionPane.WARNING_MESSAGE);
            txtnom.requestFocus();
            return;
        }

        // Validation du mot de passe
        if (!isPasswordValid(mdp)) {
            showPasswordError();
            return;
        }

        // Validation du type
        if (!isValidRole(type)) {
            JOptionPane.showMessageDialog(this,
               "Type d'utilisateur invalide",
               "Erreur de sélection",
               JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Vérification des doublons
        if (isNameAlreadyExists(nom)) {
            JOptionPane.showMessageDialog(this,
                "Ce nom est déjà utilisé",
                "Erreur de duplication",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. Exécution
        try {
            Connect();
            String hashedPassword = hashPassword(mdp); // Suppression du catch inutile
    
            logAction("AJOUT_SALARIE_TENTATIVE: " + nom + " - " + type);
    
            try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO salaries (nom, mdp, type) VALUES (?, ?, ?)")) {
            
                pst.setString(1, nom);
                pst.setString(2, hashedPassword);
                pst.setString(3, type);
        
                int result = pst.executeUpdate();
        
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Salarié ajouté avec succès\nNom: " + nom + "\nType: " + type,
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            
                    logAction("AJOUT_SALARIE_REUSSI: " + nom);
                    loadSalaries();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            logAction("ERREUR_AJOUT: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erreur technique: " + e.getMessage(),
                "Erreur DB",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // Méthodes de validation supplémentaires
    private boolean isValidRole(String role) {
        // Vérifie que le rôle fait partie des valeurs autorisées
        return Arrays.asList("ADMIN", "MANAGER", "USER").contains(role);
    }

    private boolean isPasswordValid(String password) {
        // Vérifie la complexité du mot de passe
        return password.length() >= 8 &&
           password.matches(".*[A-Z].*") &&
           password.matches(".*[a-z].*") && 
           password.matches(".*\\d.*") &&
           password.matches(".*[!@#$%^&*].*");
    }

    private void showPasswordError() {
        JOptionPane.showMessageDialog(this,
            "Le mot de passe doit contenir:\n"
            + "- 8 caractères minimum\n"
            + "- 1 majuscule\n"
            + "- 1 minuscule\n"
            + "- 1 chiffre\n"
            + "- 1 caractère spécial (!@#$%^&*)",
            "Mot de passe invalide",
            JOptionPane.WARNING_MESSAGE);
        jPasswordField1.requestFocus();
        jPasswordField1.selectAll();
    }//GEN-LAST:event_btnAjouterActionPerformed

    private void txttypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttypeActionPerformed

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un salarié");
            return;
        }
    
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = txtnom.getText().trim();
        String mdp = jPasswordField1.getText().trim(); // Récupération du mot de passe
        String type = txttype.getSelectedItem().toString();
    
        if (nom.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires");
            return;
        }
    
        try {
            Connect();
            // Correction de la requête SQL
            pst = con.prepareStatement("UPDATE salaries SET nom=?, mdp=?, type=? WHERE id=?"); 
            pst.setString(1, nom);
            pst.setString(2, mdp);
            pst.setString(3, type);
            pst.setInt(4, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salarié modifié avec succès!");
            loadSalaries();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_btnModifierActionPerformed

    private void btnSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un salarié");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer ce salarié?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            
            try {
                String sql = "DELETE FROM salaries WHERE id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Salarié supprimé avec succès!");
                loadSalaries();
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur suppression: " + e.getMessage());
            } finally {
                try { if (pst != null) pst.close(); } catch (Exception e) {}
            }
        }
    }//GEN-LAST:event_btnSupprimerActionPerformed
    // Bouton ACTUALISER
    private void btnAcualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcualiserActionPerformed
        loadSalaries();
        clearFields();
        JOptionPane.showMessageDialog(this, "Liste actualisée");
    }//GEN-LAST:event_btnAcualiserActionPerformed

    private void btnRechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercherActionPerformed
        String recherche = txtrechercher.getText().trim();
    
        try {
            Connect();
            String sql = "SELECT id, nom, mdp, type FROM salaries WHERE nom LIKE ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + recherche + "%");
    
            rs = pst.executeQuery();
            tableModel.setRowCount(0);
    
            while(rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getInt("id"),
                    rs.getString("nom"),
                    "********", // Masquage du mot de passe
                    rs.getString("type")
                });
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur recherche: " + ex.getMessage());
        } finally {
            try { if(rs != null) rs.close(); } catch(Exception e) {}
            try { if(pst != null) pst.close(); } catch(Exception e) {}
            try { if(con != null) con.close(); } catch(Exception e) {}
        }
    }//GEN-LAST:event_btnRechercherActionPerformed

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de hachage: " + e.getMessage());
            return null; // important : retourner quelque chose en cas d'erreur
        }
    }
    
    private void lbltypeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lbltypeAncestorAdded
        // Pour récupérer la valeur sélectionnée
        String typeSelectionne = txttype.getSelectedItem().toString();

        // Pour définir une valeur par défaut
        txttype.setSelectedIndex(0); // Sélectionne "ADMIN"

        // Pour ajouter des options dynamiquement
        txttype.addItem("MANAGER");
        txttype.removeItem("USER");
    }//GEN-LAST:event_lbltypeAncestorAdded

    private void txtrechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrechercherActionPerformed
                                            
        String recherche = txtrechercher.getText().trim();
    
        // Validation de l'input
        if (recherche.isEmpty()) {
            loadSalaries(); // Recharge tout si champ vide
            return;
        }
    
        // Protection contre les injections SQL
        if (!recherche.matches("[a-zA-Z0-9 éèàçù-]{2,50}")) {
            JOptionPane.showMessageDialog(this, 
                "Caractères invalides dans la recherche\nUtilisez seulement lettres, chiffres et espaces",
                "Erreur de saisie", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        try {
            Connect();
            String sql = "SELECT id, nom, type FROM salaries WHERE nom LIKE ? OR id LIKE ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + recherche + "%");
            pst.setString(2, recherche + "%"); // Pour recherche par ID
        
            rs = pst.executeQuery();
            tableModel.setRowCount(0); // Efface le tableau
        
            while(rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom"),
                    "********", // Masque le mot de passe
                    rs.getString("type")
                });
            }
        
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Aucun résultat pour : " + recherche,
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur technique lors de la recherche : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_txtrechercherActionPerformed

    private void txtnomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnomActionPerformed
                                       
        String nom = txtnom.getText().trim();
    
        // Validation du format
        if (!nom.matches("[a-zA-ZÀ-ÿ -]{2,50}")) {
            JOptionPane.showMessageDialog(this,
                "Format de nom invalide :\n"
                + "- 2 à 50 caractères\n"
                + "- Lettres, espaces et traits d'union seulement",
                "Erreur de saisie",
                JOptionPane.WARNING_MESSAGE);
        
            txtnom.requestFocus();
            txtnom.selectAll();
            return;
        }
    
        // Vérification de l'unicité
        try {
            Connect();
            String sql = "SELECT 1 FROM salaries WHERE nom = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            rs = pst.executeQuery();
        
            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                    "Ce nom est déjà utilisé par un autre salarié",
                    "Doublon détecté",
                    JOptionPane.WARNING_MESSAGE);
                txtnom.selectAll();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de vérification : " + ex.getMessage(),
                "Erreur technique",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_txtnomActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
                                           
        // Récupération du mot de passe
        char[] passwordChars = jPasswordField1.getPassword();
        String password = new String(passwordChars);
    
        // 1. Validation de la longueur minimum
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this,
               "Le mot de passe doit contenir au moins 8 caractères",
               "Erreur de validation",
               JOptionPane.WARNING_MESSAGE);
            jPasswordField1.requestFocus();
            return;
        }
    
        // 2. Validation de la complexité
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
    
        if (!(hasUpper && hasLower && hasDigit && hasSpecial)) {
            JOptionPane.showMessageDialog(this,
                "Le mot de passe doit contenir :\n" +
                "- Au moins une majuscule\n" +
                "- Au moins une minuscule\n" +
                "- Au moins un chiffre\n" +
                "- Au moins un caractère spécial",
                "Complexité insuffisante",
                JOptionPane.WARNING_MESSAGE);
            jPasswordField1.requestFocus();
            return;
        }
    
        // 3. Vérification des mots de passe courants (optionnel)
        if (isCommonPassword(password)) {
            JOptionPane.showMessageDialog(this,
                "Ce mot de passe est trop courant - choisissez-en un autre",
                "Mot de passe faible",
                JOptionPane.WARNING_MESSAGE);
            jPasswordField1.requestFocus();
            return;
        }
    
        // 4. Feedback visuel
        updatePasswordStrengthIndicator(password);
    
        // Effacement sécurisé du mot de passe en mémoire
        Arrays.fill(passwordChars, '0');
    }

    // Méthodes auxiliaires
    private boolean isCommonPassword(String password) {
        String[] commonPasswords = {"password", "123456", "azerty", "qwerty"};
        return Arrays.asList(commonPasswords).contains(password.toLowerCase());
    }

    private void updatePasswordStrengthIndicator(String password) {
        int strength = 0;
    
        // Longueur
        if (password.length() >= 12) strength += 2;
        else if (password.length() >= 8) strength += 1;
    
        // Complexité
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[^A-Za-z0-9].*")) strength += 2;
    
        // Mise à jour de l'UI
        Color color;
        String message;
    
        if (strength < 4) {
            color = Color.RED;
            message = "Faible";
        } else if (strength < 7) {
            color = Color.ORANGE;
            message = "Moyen";
        } else {
            color = Color.GREEN;
            message = "Fort";
        }
    
        jPasswordField1.setBackground(color);
        jPasswordField1.setToolTipText("Force du mot de passe : " + message);
    
        // Optionnel : Afficher un JLabel avec le feedback
        if (lblPasswordStrength != null) {
            lblPasswordStrength.setText("Force : " + message);
            lblPasswordStrength.setForeground(color);
        }
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Salarie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Salarie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Salarie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Salarie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        try {
        // Créez la connexion dans le try
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");;
            new Salarie(1, "ADMIN", conn).setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Erreur de connexion à la base de données: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcualiser;
    private javax.swing.JButton btnAjouter;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnRechercher;
    private javax.swing.JButton btnSupprimer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblmdp;
    private javax.swing.JLabel lblnom;
    private javax.swing.JLabel lbltype;
    private javax.swing.JTextField txtnom;
    private javax.swing.JTextField txtrechercher;
    private javax.swing.JComboBox<String> txttype;
    // End of variables declaration//GEN-END:variables
}
