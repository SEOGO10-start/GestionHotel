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
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author MOUSSA SEOGO
 */
public class Clients extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private String nom;
    
    public Clients() {
        initComponents();
        initTableModel(); // Initialiser le modèle de table
        btnActualiserActionPerformed(null); // Charger les données au démarrage
        initTableListener();
    }

    public void Connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver SQLite non trouvé: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage());
        }
    }
    
    public String getNom() {
        return this.nom;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblClient = new javax.swing.JLabel();
        txtclient = new javax.swing.JTextField();
        lblTelephone = new javax.swing.JLabel();
        txttelephone = new javax.swing.JTextField();
        lblAdresse = new javax.swing.JLabel();
        cbadresse = new javax.swing.JComboBox<>();
        btnAjouter = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnSupprimer = new javax.swing.JButton();
        btnActualiser = new javax.swing.JButton();
        txtrechercher = new javax.swing.JTextField();
        lblMenue = new javax.swing.JLabel();
        btnRechercher = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setBackground(new java.awt.Color(51, 0, 204));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("IFORMATIONS CLIENTS");

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lblClient.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblClient.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClient.setText("NOM-CLIENT");
        lblClient.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblClientAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtclientActionPerformed(evt);
            }
        });

        lblTelephone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTelephone.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTelephone.setText("TELEPHONE");
        lblTelephone.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblTelephoneAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txttelephone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttelephoneActionPerformed(evt);
            }
        });

        lblAdresse.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAdresse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAdresse.setText("ADRESSE");
        lblAdresse.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblAdresseAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        cbadresse.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cbadresse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BURKINA FASO", "COTE D'IVOIRE", "MALI", "NIGER", "TOGO", "GABON", "MAROC", "AUTRES..." }));
        cbadresse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbadresseActionPerformed(evt);
            }
        });

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

        btnActualiser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnActualiser.setText("ACTUALISER");
        btnActualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualiserActionPerformed(evt);
            }
        });

        txtrechercher.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtrechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrechercherActionPerformed(evt);
            }
        });

        lblMenue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMenue.setForeground(new java.awt.Color(255, 0, 0));
        lblMenue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenue.setText("MENUE");
        lblMenue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblMenue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenueMouseClicked(evt);
            }
        });

        btnRechercher.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnRechercher.setText("Rechercher");
        btnRechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtclient))
                        .addGap(425, 425, 425))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblMenue, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblTelephone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(16, 16, 16))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblAdresse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbadresse, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnAjouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnRechercher, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnActualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtrechercher)
                                            .addComponent(btnModifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(txttelephone))
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
                    .addComponent(lblMenue, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtclient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txttelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAdresse, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbadresse, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAjouter)
                            .addComponent(btnModifier))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtrechercher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRechercher)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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

    private void initTableListener() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = jTable1.getSelectedRow();
                if (row >= 0) {
                    txtclient.setText(jTable1.getValueAt(row, 1).toString());
                    txttelephone.setText(jTable1.getValueAt(row, 2).toString());
                    cbadresse.setSelectedItem(jTable1.getValueAt(row, 3).toString());
                }
            }
        });
    }
    private void lblMenueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenueMouseClicked
        // On va faire un retour depuis le menue principale
        Clients.super.dispose();
        MenuePrincipale men = new MenuePrincipale();
        men.setVisible(true);
    }//GEN-LAST:event_lblMenueMouseClicked

    private void clearFields() {
        txtclient.setText("");
        txttelephone.setText("");
        cbadresse.setSelectedIndex(0);
        txtrechercher.setText("");
    }
    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed
        try {
            Connect();
            String sql = "INSERT INTO clients(nom, telephone, adresse) VALUES(?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, txtclient.getText());
            pst.setString(2, txttelephone.getText());
            pst.setString(3, cbadresse.getSelectedItem().toString());
    
            int k = pst.executeUpdate();
            if(k == 1){
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès");
                txtclient.setText("");
                txttelephone.setText("");
                cbadresse.setSelectedIndex(0);
                btnActualiserActionPerformed(null); // Actualiser la table
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur SQL: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnAjouterActionPerformed

   private void initTableModel() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"ID", "Nom", "Téléphone", "Adresse"} // En-têtes des colonnes
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre la table non éditable
            }
        };
        jTable1.setModel(model);
    
        // Personnalisation de l'apparence
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        // Centrer le texte dans les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
        try {
            int row = jTable1.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
                return;
            }
        
            String id = jTable1.getModel().getValueAt(row, 0).toString();
            Connect();
            String sql = "UPDATE clients SET nom=?, telephone=?, adresse=? WHERE id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, txtclient.getText());
            pst.setString(2, txttelephone.getText());
            pst.setString(3, cbadresse.getSelectedItem().toString());
            pst.setString(4, id);
        
            int k = pst.executeUpdate();
            if(k == 1){
                JOptionPane.showMessageDialog(this, "Client modifié avec succès");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }//GEN-LAST:event_btnModifierActionPerformed

    private void btnSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerActionPerformed
        try {
            int row = jTable1.getSelectedRow();
            if(row == -1){
               JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
               return;
            }
        
            int dialogResult = JOptionPane.showConfirmDialog(this, 
                "Voulez-vous vraiment supprimer ce client?", "Confirmation", 
                JOptionPane.YES_NO_OPTION);
        
            if(dialogResult == JOptionPane.YES_OPTION){
                String id = jTable1.getModel().getValueAt(row, 0).toString();
                Connect();
                String sql = "DELETE FROM clients WHERE id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, id);
            
                int k = pst.executeUpdate();
                if(k == 1){
                   JOptionPane.showMessageDialog(this, "Client supprimé avec succès");
                } else {
                   JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
                }
                con.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSupprimerActionPerformed

    private void btnActualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualiserActionPerformed
        try {
            Connect();
            String sql = "SELECT id, nom, telephone, adresse FROM clients";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
        
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"), 
                    rs.getString("adresse")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_btnActualiserActionPerformed

    private void btnRechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercherActionPerformed
        String recherche = txtrechercher.getText().trim();
    
            try {
                Connect();
                String sql = "SELECT id, nom, telephone, adresse FROM clients WHERE nom LIKE ? OR telephone LIKE ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, "%" + recherche + "%");
                pst.setString(2, "%" + recherche + "%");
        
                rs = pst.executeQuery();
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
        
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("adresse")
                });
            }
        
             if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Aucun résultat trouvé");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de recherche: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_btnRechercherActionPerformed

    private void txtrechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrechercherActionPerformed
        try {
            Connect();
            String sql = "SELECT id, nom, telephone, adresse FROM clients";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            afficherClientsDansTable(rs);
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }//GEN-LAST:event_txtrechercherActionPerformed

    private void afficherClientsDansTable(ResultSet rs) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Effacer les données existantes

        try {
            while (rs.next()) {
                String id = rs.getString("id_client");
                String nom = rs.getString("nom");
                String telephone = rs.getString("telephone");
                String adresse = rs.getString("adresse");
            
                model.addRow(new Object[]{id, nom, telephone, adresse});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cbadresseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbadresseActionPerformed
        String paysSelectionne = (String) cbadresse.getSelectedItem();
    }//GEN-LAST:event_cbadresseActionPerformed

    private void lblAdresseAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAdresseAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblAdresseAncestorAdded

    private void txttelephoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttelephoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttelephoneActionPerformed

    private void lblTelephoneAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblTelephoneAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblTelephoneAncestorAdded

    private void txtclientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtclientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtclientActionPerformed

    private void lblClientAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblClientAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblClientAncestorAdded

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
            java.util.logging.Logger.getLogger(Clients.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clients.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clients.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clients.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clients().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualiser;
    private javax.swing.JButton btnAjouter;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnRechercher;
    private javax.swing.JButton btnSupprimer;
    private javax.swing.JComboBox<String> cbadresse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAdresse;
    private javax.swing.JLabel lblClient;
    private javax.swing.JLabel lblMenue;
    private javax.swing.JLabel lblTelephone;
    private javax.swing.JTextField txtclient;
    private javax.swing.JTextField txtrechercher;
    private javax.swing.JTextField txttelephone;
    // End of variables declaration//GEN-END:variables
}
