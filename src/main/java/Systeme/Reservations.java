/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Systeme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.sql.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.time.LocalDate;
import Systeme.Clients;
import Systeme.Chambres;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.sql.Date;

/**
 *
 * @author MOUSSA SEOGO
 */
public class Reservations extends javax.swing.JFrame {
    private DefaultTableModel tableModel;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Clients clients;
    private Chambres chambres;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Paiement paiement; // ✅ association
    private int id;
    private String modePaiement; 
    
    public Reservations() {
        initComponents();
        Connect(); // Établir la connexion à la DB
        tableModel = (DefaultTableModel) jTableReservations.getModel(); // Initialisation
        initJTable(); // Initialise avec le bon modèle
        loadReservations();
        populateRoomComboBox();
        populateClientComboBox();
        this.clients = new Clients(); // Initialisation correcte
        this.chambres = new Chambres();
        this.dateDebut = LocalDate.now();
        this.dateFin = LocalDate.now().plusDays(1);
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    public long getNombreDeJours() {
        return java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);
    }
    
    public double calculerMontantTotal() {
        return getNombreDeJours() * chambres.getPrixParNuit();
    }

    // ✅ méthode pour effectuer un paiement
    public void payer(Connection con, int reservationId, String nomClient) {
        double montant = calculerMontantTotal();
        String modePaiement = "Carte"; // À définir correctement
    
        Paiement paiement = new Paiement(con);
        paiement.effectuerPaiement(reservationId, con, nomClient); // Avec arguments
    }

    public Paiement getPaiement() {
        return paiement;
    }

    // Getters/setters supplémentaires si besoin...
    
    public Clients getClients() {
        return clients;
    }

    public Chambres getChambres() {
        return chambres;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }
    private void populateRoomComboBox() {
        try {
            System.out.println("Tentative de chargement des chambres..."); // Debug
        
            String sql = "SELECT numero FROM chambres WHERE LOWER(statut) = 'libre'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
            int count = 0;
            cbchambre.removeAllItems();
            while (rs.next()) {
                String numero = rs.getString("numero");
                System.out.println("Chambres trouvée: " + numero); // Debug
                cbchambre.addItem(numero);
                count++;
            }
        
            System.out.println(count + " chambres chargées."); // Debug
            if (count == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Aucune chambre libre trouvée", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("ERREUR SQL: " + e.getMessage()); // Debug
            JOptionPane.showMessageDialog(this, 
                "Erreur technique lors du chargement des chambres:\n" + e.getMessage(), 
                "Erreur", 
            JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }
    private void populateClientComboBox() {
        try {
            String sql = "SELECT nom FROM clients"; // Supposant que vous avez une table 'clients'
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
            cbclient.removeAllItems();
            while (rs.next()) {
                cbclient.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement clients: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }

    private void loadReservations() {
        try {
            String sql = "SELECT r.id, r.client, c.numero as chambre, r.date_entree, r.date_sortie " +
                    "FROM reservations r JOIN chambres c ON r.chambre_id = c.id";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
            tableModel.setRowCount(0);
        
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("client"),
                    rs.getString("chambre"),
                    rs.getString("date_entree"),
                    rs.getString("date_sortie")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
       }
    }

    public void Connect() {
        try {
            String dbPath = "jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db";
            System.out.println("Tentative de connexion à : " + dbPath); // Debug
            con = DriverManager.getConnection(dbPath);
            System.out.println("Connexion réussie !"); // Debug
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur DB: " + e.getMessage());
            e.printStackTrace(); // Affiche la stack trace complète
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableReservations = new javax.swing.JTable();
        lblclient = new javax.swing.JLabel();
        lblchambre = new javax.swing.JLabel();
        LblEntreSortie = new javax.swing.JLabel();
        btnajouter = new javax.swing.JButton();
        btnmodifier = new javax.swing.JButton();
        btnsupprimer = new javax.swing.JButton();
        btnactualiser = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        lblmenue = new javax.swing.JLabel();
        txtdateEntre = new javax.swing.JFormattedTextField();
        txtdateSortie = new javax.swing.JFormattedTextField();
        cbclient = new javax.swing.JComboBox<>();
        cbchambre = new javax.swing.JComboBox<>();
        btnrechercher = new javax.swing.JButton();
        btnPaiement = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setBackground(new java.awt.Color(51, 0, 204));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("IFORMATION RESERVATIONS");

        jTableReservations.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTableReservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableReservations.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTableReservationsAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(jTableReservations);

        lblclient.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblclient.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblclient.setText("CLIENT");

        lblchambre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblchambre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblchambre.setText("CHAMBRES");

        LblEntreSortie.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LblEntreSortie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LblEntreSortie.setText("DATE/ ENTRE & SORTIE");

        btnajouter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnajouter.setText("AJOUTER");
        btnajouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnajouterActionPerformed(evt);
            }
        });

        btnmodifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnmodifier.setText("MODIFIER");
        btnmodifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodifierActionPerformed(evt);
            }
        });

        btnsupprimer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnsupprimer.setText("SUPPRIMER");
        btnsupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsupprimerActionPerformed(evt);
            }
        });

        btnactualiser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnactualiser.setText("ACTUALISER");
        btnactualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualiserActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        lblmenue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblmenue.setForeground(new java.awt.Color(255, 0, 0));
        lblmenue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblmenue.setText("MENUE");
        lblmenue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblmenue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmenueMouseClicked(evt);
            }
        });

        try {
            txtdateEntre.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtdateEntre.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        try {
            txtdateSortie.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtdateSortie.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbclient.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbchambre.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        btnrechercher.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnrechercher.setText("Rechercher");
        btnrechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrechercherActionPerformed(evt);
            }
        });

        btnPaiement.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPaiement.setText("Paiement");
        btnPaiement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaiementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblclient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(425, 425, 425))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnPaiement)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblmenue, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblchambre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(16, 16, 16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnajouter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnsupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtdateEntre, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnrechercher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnactualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField3)
                                        .addComponent(btnmodifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtdateSortie, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbchambre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LblEntreSortie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbclient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(lblmenue, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPaiement))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblclient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbclient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblchambre, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbchambre, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LblEntreSortie, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtdateEntre, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(txtdateSortie))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnajouter)
                            .addComponent(btnmodifier))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnsupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnactualiser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnrechercher)))
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

    private void lblmenueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmenueMouseClicked
        // On va faire un retour depuis le menue principale
        Reservations.super.dispose();
        MenuePrincipale menu = new MenuePrincipale();
        menu.setVisible(true);
    }//GEN-LAST:event_lblmenueMouseClicked

    private void btnajouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnajouterActionPerformed
        String client = cbclient.getSelectedItem().toString();
        String chambre = cbchambre.getSelectedItem().toString();
        String dateEntree = txtdateEntre.getText();
        String dateSortie = txtdateSortie.getText();

        if (client.isEmpty() || chambre.isEmpty() || dateEntree.isEmpty() || dateSortie.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateEntreeLD = LocalDate.parse(dateEntree, formatter);
            LocalDate dateSortieLD = LocalDate.parse(dateSortie, formatter);

            // Validation des dates
            if (dateSortieLD.isBefore(dateEntreeLD)) {
                JOptionPane.showMessageDialog(this, "La date de sortie doit être après la date d'entrée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Conversion explicite en java.sql.Date
            java.sql.Date sqlDateEntree = java.sql.Date.valueOf(dateEntreeLD);
            java.sql.Date sqlDateSortie = java.sql.Date.valueOf(dateSortieLD);
            // Récupérer l'ID de la chambre
            String sql = "SELECT id FROM chambres WHERE numero = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, chambre);
            rs = pst.executeQuery();
    
            if (rs.next()) {
                int chambreId = rs.getInt("id");
            
                // Insérer la réservation
                sql = "INSERT INTO reservations (client, chambre_id, date_entree, date_sortie) VALUES (?, ?, ?, ?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, client);
                pst.setInt(2, chambreId);
                pst.setDate(3, sqlDateEntree);
                pst.setDate(4, sqlDateSortie);
                pst.executeUpdate();
            
                // Mettre à jour le statut de la chambre
                sql = "UPDATE chambres SET statut = 'occupé' WHERE id = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, chambreId);
                pst.executeUpdate();
            
               JOptionPane.showMessageDialog(this, "Réservation ajoutée avec succès!");
               loadReservations();
               populateRoomComboBox();
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez JJ/MM/AAAA", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur d'ajout: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnajouterActionPerformed

    private void btnmodifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodifierActionPerformed
        int selectedRow = jTableReservations.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à modifier", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String client = cbclient.getSelectedItem().toString();
        String chambre = cbchambre.getSelectedItem().toString();
        String dateEntree = txtdateEntre.getText();
        String dateSortie = txtdateSortie.getText();

        try {
            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateEntreeLD = LocalDate.parse(dateEntree, formatter);
            LocalDate dateSortieLD = LocalDate.parse(dateSortie, formatter);

            // Validation des dates
            if (dateSortieLD.isBefore(dateEntreeLD)) {
                JOptionPane.showMessageDialog(this, "La date de sortie doit être après la date d'entrée", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
             // Conversion explicite en java.sql.Date
            java.sql.Date sqlDateEntree = java.sql.Date.valueOf(dateEntreeLD);
            java.sql.Date sqlDateSortie = java.sql.Date.valueOf(dateSortieLD);
            // Récupérer l'ancienne chambre
            String sql = "SELECT chambre_id FROM reservations WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            int oldChambreId = rs.getInt("chambre_id");
    
            // Récupérer la nouvelle chambre
            sql = "SELECT id FROM chambres WHERE numero = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, chambre);
            rs = pst.executeQuery();
            int newChambreId = rs.getInt("id");
    
            // Mettre à jour la réservation
            sql = "UPDATE reservations SET client = ?, chambre_id = ?, date_entree = ?, date_sortie = ? WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, client);
            pst.setInt(2, newChambreId);
            pst.setDate(3, sqlDateEntree);
            pst.setDate(4, sqlDateSortie);
            pst.setInt(5, id);
            pst.executeUpdate();
    
            // Mettre à jour les statuts des chambres
            sql = "UPDATE chambres SET statut = 'libre' WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, oldChambreId);
            pst.executeUpdate();
    
            sql = "UPDATE chambres SET statut = 'occupé' WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, newChambreId);
            pst.executeUpdate();
    
            JOptionPane.showMessageDialog(this, "Réservation modifiée avec succès!");
            loadReservations();
            populateRoomComboBox();   
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez JJ/MM/AAAA", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de modification: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnmodifierActionPerformed

    private void btnsupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsupprimerActionPerformed
        int selectedRow = jTableReservations.getSelectedRow();
        if (selectedRow == -1) {
           JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
           return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cette réservation?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
        
            try {
                // Récupérer la chambre associée
                String sql = "SELECT chambre_id FROM reservations WHERE id = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                rs = pst.executeQuery();
                int chambreId = rs.getInt("chambre_id");
            
                // Supprimer la réservation
                sql = "DELETE FROM reservations WHERE id = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
            
                // Mettre à jour le statut de la chambre
                sql = "UPDATE chambres SET statut = 'libre' WHERE id = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, chambreId);
                pst.executeUpdate();
            
                JOptionPane.showMessageDialog(this, "Réservation supprimée avec succès!");
                loadReservations();
                populateRoomComboBox();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur de suppression: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnsupprimerActionPerformed

    private void btnactualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualiserActionPerformed
        loadReservations();
        populateRoomComboBox();
        JOptionPane.showMessageDialog(this, "Liste actualisée avec succès!");
    }//GEN-LAST:event_btnactualiserActionPerformed

    // Méthode pour afficher les résultats de recherche
    private void afficherResultatsRecherche(ResultSet rs) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) jTableReservations.getModel();
        model.setRowCount(0);
    
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("client"),
                rs.getString("chambre"),
                rs.getString("date_entree"),
                rs.getString("date_sortie"),
            });
        }
    }

    private void actualiserTableReservations() {
        remplirTableReservations();
    }
    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    // Méthode pour remplir la table
    private void remplirTableReservations() {
        DefaultTableModel model = (DefaultTableModel) jTableReservations.getModel();
        model.setRowCount(0);
    
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT r.id, c.nom AS client, ch.numero AS chambre, " +
                "r.date_entree, r.date_sortie, " +
                "FROM reservations r " +
                "JOIN clients c ON r.client_id = c.id " +
                "JOIN chambres ch ON r.chambre_id = ch.id")) {
        
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("client"),
                    rs.getString("chambre"),
                    rs.getString("date_entree"),
                    rs.getString("date_sortie"),
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + e.getMessage());
        }
    }

    private void btnrechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrechercherActionPerformed
        String recherche = jTextField3.getText().trim();
    
        if (recherche.isEmpty()) {
            loadReservations(); // Utilisez votre méthode existante
            return;
        }

        try {
            String sql = "SELECT r.id, r.client, c.numero as chambre, r.date_entree, r.date_sortie " +
                    "FROM reservations r JOIN chambres c ON r.chambre_id = c.id " +
                    "WHERE r.client LIKE ? OR c.numero LIKE ? OR r.date_entree LIKE ?";
        
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + recherche + "%");
            pst.setString(2, "%" + recherche + "%");
            pst.setString(3, "%" + recherche + "%");
        
            rs = pst.executeQuery();
        
            // Afficher les résultats
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("client"),
                    rs.getString("chambre"),
                    rs.getString("date_entree"),
                    rs.getString("date_sortie")
                });
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur recherche: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }//GEN-LAST:event_btnrechercherActionPerformed

    private void jTableReservationsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTableReservationsAncestorAdded
        if (evt.getAncestorParent() == jTableReservations) {
            initJTable();
            remplirTableReservations();
        }
    }//GEN-LAST:event_jTableReservationsAncestorAdded

    private void btnPaiementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaiementActionPerformed
        Reservations.super.dispose();
        double montant = 0.0; // À calculer selon votre logique
        String modePaiement = "Carte"; // Ou récupérer depuis l'interface
        Paiement Paiement = new Paiement(con);
        Paiement.setVisible(true);
    }//GEN-LAST:event_btnPaiementActionPerformed
    private void searchReservations() {
        String searchText = jTextField3.getText().trim();
    
        try {
            String sql = "SELECT r.id, r.client, c.numero as chambre, r.date_entree, r.date_sortie " +
                    "FROM reservations r JOIN chambres c ON r.chambre_id = c.id " +
                    "WHERE r.client LIKE ? OR c.numero LIKE ? OR r.date_entree LIKE ? OR r.date_sortie LIKE ?";
        
            pst = con.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            pst.setString(1, searchPattern);  // Client
            pst.setString(2, searchPattern);  // Chambre
            pst.setString(3, searchPattern);  // Date entrée
            pst.setString(4, searchPattern);  // Date sortie
        
            rs = pst.executeQuery();
        
            // Vider le tableau
            tableModel.setRowCount(0);
        
            // Remplir avec les résultats
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("client"),
                    rs.getString("chambre"),
                    rs.getString("date_entree"),
                    rs.getString("date_sortie")
                });
            }
        
            if (tableModel.getRowCount() == 0) {
               JOptionPane.showMessageDialog(this, "Aucun résultat trouvé", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de recherche: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }
    }
    private void initJTable() {
        // 1. Création du modèle de table
        tableModel = new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"ID", "Client", "Chambre", "Date Entrée", "Date Sortie"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        // 2. Application du modèle
        jTableReservations.setModel(tableModel);
    
        // 3. Configuration de l'apparence
        jTableReservations.setRowHeight(25);
        jTableReservations.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        jTableReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        // 4. Centrage du texte
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < jTableReservations.getColumnCount(); i++) {
            jTableReservations.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    
        // 5. Gestionnaire de sélection (simplifié)
        jTableReservations.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
               // Logique de sélection si nécessaire
            }
        });
    }

    private void createTables() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS reservations (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "client TEXT NOT NULL, " +
                     "chambre_id INTEGER NOT NULL, " +
                     "date_entree TEXT NOT NULL, " +
                     "date_sortie TEXT NOT NULL, " +
                     "FOREIGN KEY (chambre_id) REFERENCES chambres(id))";
        
            try (Statement stmt = con.createStatement()) {
                stmt.execute(sql);
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_res_chambre ON reservations(chambre_id)");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur création table: " + e.getMessage());
        }
    }
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
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reservations().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblEntreSortie;
    private javax.swing.JButton btnPaiement;
    private javax.swing.JButton btnactualiser;
    private javax.swing.JButton btnajouter;
    private javax.swing.JButton btnmodifier;
    private javax.swing.JButton btnrechercher;
    private javax.swing.JButton btnsupprimer;
    private javax.swing.JComboBox<String> cbchambre;
    private javax.swing.JComboBox<String> cbclient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableReservations;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblchambre;
    private javax.swing.JLabel lblclient;
    private javax.swing.JLabel lblmenue;
    private javax.swing.JFormattedTextField txtdateEntre;
    private javax.swing.JFormattedTextField txtdateSortie;
    // End of variables declaration//GEN-END:variables
}
