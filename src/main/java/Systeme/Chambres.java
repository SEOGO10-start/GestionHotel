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
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;



/**
 *
 * @author MOUSSA SEOGO
 */
public class Chambres extends javax.swing.JFrame {
    private double prixParNuit;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public static double getPrixParType(String type) {
        switch (type) {
            case"Standard": return 10000;
            case "Deluxe": return 20000;
            case "Suite": return 25000;
            default: return 0;
        }
    }
    public Chambres() {
        initComponents();
        // Ajoutez ici vos personnalisations :
        txtPrix.setEditable(false);
        dateArrivee.setText("AAAA-MM-JJ");
        dateDepart.setText("AAAA-MM-JJ");
        dateArrivee.setForeground(Color.GRAY);
        dateDepart.setForeground(Color.GRAY);
    
        // Initialisation des ComboBox (si nécessaire)
        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STANDARD", "DELUXE", "SUITE" }));
        cbStatut.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LIBRE", "OCCUPE", "NETTOYAGE", "MAINTENANCE" }));
    
        // Configuration des placeholders
        dateArrivee.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (dateArrivee.getText().equals("AAAA-MM-JJ")) {
                    dateArrivee.setText("");
                    dateArrivee.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (dateArrivee.getText().isEmpty()) {
                    dateArrivee.setText("AAAA-MM-JJ");
                    dateArrivee.setForeground(Color.GRAY);
                }
            }
        });
        Connect(); // Établir la connexion à la DB
        loadTable(); // Charger les données au démarrage
        setupDateFieldListeners();
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
    public double getPrixParNuit() {
        return this.prixParNuit;
    }
    // Créer la table chambres si elle n'existe pas
    private void createTableIfNotExists() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS chambres (" +
                     "numero TEXT PRIMARY KEY, " +
                     "telephone TEXT, " +
                     "type TEXT, " +
                     "prix REAL, " +
                     "statut TEXT, " +
                     "date_arrivee TEXT, " +
                     "date_depart TEXT)";
            pst = con.prepareStatement(sql);
            pst.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur création table: " + e.getMessage());
        }
    }
  
    // Méthode pour charger les données dans le JTable
    private void loadTable() {
        try {
            String sql = "SELECT * FROM chambres";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("Numéro");
           model.addColumn("Téléphone");
           model.addColumn("Type");
           model.addColumn("Prix");
           model.addColumn("Statut");
           model.addColumn("Arrivée");
           model.addColumn("Départ");
        
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("numero"),
                    rs.getString("telephone"),
                    rs.getString("type"),
                    rs.getDouble("prix"),
                    rs.getString("statut"),
                    rs.getString("date_arrivee"),
                    rs.getString("date_depart")
                });
            }
        
            jTable1.setModel(model);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(this, "Erreur chargement données: " + e.getMessage());
        }
    }
    private void setupDateFieldListeners() {
        // Pour dateArrivee
        dateArrivee.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
               if (dateArrivee.getText().equals("AAAA-MM-JJ")) {
                   dateArrivee.setText("");
                   dateArrivee.setForeground(Color.BLACK);
                }
            }
        
            @Override
            public void focusLost(FocusEvent e) {
                if (dateArrivee.getText().isEmpty()) {
                    dateArrivee.setText("AAAA-MM-JJ");
                    dateArrivee.setForeground(Color.GRAY);
                }
            }
        });
    
        // Même chose pour dateDepart...
    }
    
    private boolean validerDateArrivee() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateArrivee.getText());
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Format date arrivée invalide (AAAA-MM-JJ requis)");
            dateArrivee.requestFocus();
            return false;
        }
    }

    private boolean validerDateDepart() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date depart = sdf.parse(dateDepart.getText());
            Date arrivee = sdf.parse(dateArrivee.getText());
        
            if (depart.before(arrivee)) {
                JOptionPane.showMessageDialog(this, "La date de départ doit être après l'arrivée");
                return false;
            }
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Format date départ invalide (AAAA-MM-JJ requis)");
            dateDepart.requestFocus();
            return false;
        }
    }
    private boolean validerDates() {
        if (cbStatut.getSelectedItem().equals("OCCUPE")) {
            if (!validerDateArrivee() || !validerDateDepart()) {
               return false;
            }
        
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date arrivee = sdf.parse(dateArrivee.getText());
                Date depart = sdf.parse(dateDepart.getText());
            
                if (depart.before(arrivee)) {
                    JOptionPane.showMessageDialog(this, 
                       "La date de départ doit être après la date d'arrivée");
                    return false;
                }
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Format de date invalide");
                return false;
            }
        }
        return true;
    }
    // Méthode pour vider les champs
    private void clearFields() {
        txtNumero.setText("");
        txtTelephone.setText("");
        cbType.setSelectedIndex(0);
        txtPrix.setText("");
        cbStatut.setSelectedIndex(0);
        dateArrivee.setText("AAAA-MM-JJ");
        dateArrivee.setForeground(Color.GRAY);
        dateArrivee.setEnabled(true); // Ajoutez cette ligne
        dateDepart.setText("AAAA-MM-JJ");
        dateDepart.setForeground(Color.GRAY);
        dateDepart.setEnabled(true); // Ajoutez cette ligne
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbStatut = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPrix = new javax.swing.JTextField();
        cbType = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dateDepart = new javax.swing.JTextField();
        dateArrivee = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setBackground(new java.awt.Color(51, 0, 204));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("IFORMATION CHAMBRES");

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("NUMERO");

        txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TELEPHONE");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Statut");

        cbStatut.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cbStatut.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OCCUPE", "LIBRE", "NETTOYAGE", "MAINTENANCE" }));
        cbStatut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatutActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("AJOUTER");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setText("MODIFIER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("MENUE");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Prix");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Type");

        txtPrix.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrixActionPerformed(evt);
            }
        });

        cbType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Deluxe", "Suite" }));
        cbType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTypeActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("SUPPRIMER");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setText("ACTUALISER");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton5.setText("Rechercher");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("ARRIVEE");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DEPART");

        dateDepart.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dateDepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateDepartActionPerformed(evt);
            }
        });

        dateArrivee.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dateArrivee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateArriveeActionPerformed(evt);
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
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(57, 57, 57))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(txtNumero, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dateArrivee))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtTelephone)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(dateDepart))))
                                .addGap(21, 21, 21))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbStatut, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(10, 10, 10)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateDepart, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(dateArrivee))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbType, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(txtPrix))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbStatut, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Action pour le bouton Rechercher
        try {
            String searchNum = jTextField3.getText().trim();
            if (searchNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro de chambre");
                return;
            }

            String sql = "SELECT * FROM chambres WHERE numero = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, searchNum);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtNumero.setText(rs.getString("numero"));
                txtTelephone.setText(rs.getString("telephone"));
                cbType.setSelectedItem(rs.getString("type"));
                txtPrix.setText(String.valueOf(rs.getDouble("prix")));
                cbStatut.setSelectedItem(rs.getString("statut"));
            
                String arrivee = rs.getString("date_arrivee");
                dateArrivee.setText(arrivee != null ? arrivee : "AAAA-MM-JJ");
                dateArrivee.setForeground(arrivee != null ? Color.BLACK : Color.GRAY);
            
                String depart = rs.getString("date_depart");
                dateDepart.setText(depart != null ? depart : "AAAA-MM-JJ");
                dateDepart.setForeground(depart != null ? Color.BLACK : Color.GRAY);
            } else {
                JOptionPane.showMessageDialog(this, "Chambre non trouvée");
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur recherche: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Action pour le bouton ACTUALISER
        loadTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Liste actualisée");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Action pour le bouton SUPPRIMER
        try {
            String numero = txtNumero.getText().trim();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre à supprimer");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer la chambre " + numero + "?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM chambres WHERE numero = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, numero);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Chambre supprimée avec succès");
                    loadTable();
                    clearFields();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur suppression: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // On va faire un retour depuis le menue principale
        Chambres.super.dispose();
        MenuePrincipale men = new MenuePrincipale();
        men.setVisible(true);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Action pour le bouton MODIFIER
        try {
            String numero = txtNumero.getText().trim();
            String telephone = txtTelephone.getText().trim();
            String type = cbType.getSelectedItem().toString();
            String statut = cbStatut.getSelectedItem().toString();
            double prix = Double.parseDouble(txtPrix.getText());
            String arrivee = dateArrivee.getText();
            String depart = dateDepart.getText();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une chambre à modifier");
                return;
            }

            String sql = "UPDATE chambres SET telephone=?, type=?, prix=?, statut=?, " +
                     "date_arrivee=?, date_depart=? WHERE numero=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, telephone);
            pst.setString(2, type);
            pst.setDouble(3, prix);
            pst.setString(4, statut);
            pst.setString(5, arrivee.equals("AAAA-MM-JJ") ? null : arrivee);
            pst.setString(6, depart.equals("AAAA-MM-JJ") ? null : depart);
            pst.setString(7, numero);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Chambre modifiée avec succès");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "Aucune modification effectuée");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix invalide");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur modification: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Action pour le bouton AJOUTER
        try {
            String numero = txtNumero.getText().trim();
            String telephone = txtTelephone.getText().trim();
            String type = cbType.getSelectedItem().toString();
            String statut = cbStatut.getSelectedItem().toString();
            double prix = Double.parseDouble(txtPrix.getText());
            String arrivee = dateArrivee.getText();
            String depart = dateDepart.getText();

           if (numero.isEmpty() || telephone.isEmpty() || 
               (statut.equals("OCCUPE") && (arrivee.isEmpty() || depart.isEmpty()))) {
               JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires");
               return;
            }

            String sql = "INSERT INTO chambres (numero, telephone, type, prix, statut, date_arrivee, date_depart) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, numero);
            pst.setString(2, telephone);
            pst.setString(3, type);
            pst.setDouble(4, prix);
            pst.setString(5, statut);
            pst.setString(6, arrivee.equals("AAAA-MM-JJ") ? null : arrivee);
            pst.setString(7, depart.equals("AAAA-MM-JJ") ? null : depart);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Chambre ajoutée avec succès");
                loadTable();
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix invalide");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur ajout: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrixActionPerformed
        // Validation que le prix est numérique
        try {
            double prix = Double.parseDouble(txtPrix.getText());
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être positif !");
                txtPrix.requestFocus();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un prix valide !");
            txtPrix.setText("");
            txtPrix.requestFocus();
        }
    }//GEN-LAST:event_txtPrixActionPerformed


    private void updateDisponibilites(String type) {
        try {
            String sql = "SELECT COUNT(*) FROM chambres WHERE type=? AND statut='LIBRE'";
            pst = con.prepareStatement(sql);
            pst.setString(1, type);
            rs = pst.executeQuery();
        
            if (rs.next()) {
               int disponibles = rs.getInt(1);
               JOptionPane.showMessageDialog(this,
                   "Il reste " + disponibles + " chambre(s) " + type + " disponible(s)");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur DB: " + ex.getMessage());
        }
    }
    private void cbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTypeActionPerformed
        // Calcul automatique du prix selon le type
        String selectedType = (String) cbType.getSelectedItem();
        double prix = 0;
    
        switch(selectedType) {
            case "STANDARD":
                prix = 10000;
                break;
            case "DELUXE":
                prix = 20000;
                break;
            case "SUITE":
                prix = 25000;
                break;
        }
    
        txtPrix.setText(String.valueOf(prix));
    
        // Mise à jour des disponibilités selon le type
        updateDisponibilites(selectedType);
    }//GEN-LAST:event_cbTypeActionPerformed

    private void cbStatutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatutActionPerformed
        String selectedStatut = (String) cbStatut.getSelectedItem();
    
        // Désactiver les dates si la chambre n'est pas occupée
        boolean isOccupe = "OCCUPE".equals(selectedStatut);
        dateArrivee.setEnabled(isOccupe);
        dateDepart.setEnabled(isOccupe);
    
        // Gestion spéciale pour les chambres en maintenance
        if ("MAINTENANCE".equals(selectedStatut)) {
            int confirm = JOptionPane.showConfirmDialog(this,
               "Marquer cette chambre comme en maintenance ?\n"
               + "Cela la rendra indisponible pour une longue période.",
               "Confirmation Maintenance",
               JOptionPane.YES_NO_OPTION);
        
            if (confirm != JOptionPane.YES_OPTION) {
               cbStatut.setSelectedItem("LIBRE"); // Revenir au statut précédent
            }
        }
    }//GEN-LAST:event_cbStatutActionPerformed

    private void dateArriveeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateArriveeActionPerformed
        if (dateArrivee.getText().equals("AAAA-MM-JJ")) {
            dateArrivee.setText("");
            dateArrivee.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_dateArriveeActionPerformed

    private void dateDepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateDepartActionPerformed
        if (dateDepart.getText().equals("AAAA-MM-JJ")) {
            dateDepart.setText("");
            dateDepart.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_dateDepartActionPerformed

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
            java.util.logging.Logger.getLogger(Chambres.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chambres.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chambres.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chambres.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chambres().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbStatut;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JTextField dateArrivee;
    private javax.swing.JTextField dateDepart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtPrix;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}
