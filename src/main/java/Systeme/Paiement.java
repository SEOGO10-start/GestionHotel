/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Systeme;
import java.util.Date;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException; 
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
/**
 *
 * @author MOUSSA SEOGO
 */
public class Paiement extends javax.swing.JFrame {
    private double montant;
    private String modePaiement;
    private Date datePaiement;

    private Connection con;
    

    private JLabel lblMontant, lblMode, lblClient;
    private JTextField txtMontant;
    private JComboBox<String> cbModePaiement;
    private JComboBox<ReservationInfo> cbClients;
    private JButton btnValider;
    private JButton btnRetour;
    
    public static class ReservationInfo {
        public int idReservation;
        public String nomClient;
        public String typeChambre;

        public ReservationInfo(int id, String nom, String typeChambre) {
            this.idReservation = id;
            this.nomClient = nom;
            this.typeChambre = typeChambre;
        }

        @Override
        public String toString() {
            return nomClient +"-" + typeChambre + " (Réservation #" + idReservation + ")";
        }
    }
    
     public Paiement(Connection con) {
        this.con = con;
        this.datePaiement = new Date();
        initComponents();
        initCustomComponents();
        chargerReservations(); // Charger les réservations dès le lancement
    }
    
    
    private void chargerReservations() {
        if (con == null) {
            throw new IllegalStateException("La connexion est nulle !");
        }

        List<ReservationInfo> reservations = getReservationsAvecClients();
        cbClients.removeAllItems();
        for (ReservationInfo r : reservations) {
            cbClients.addItem(r);
        }
    }

    public List<ReservationInfo> getReservationsAvecClients() {
        List<ReservationInfo> reservations = new ArrayList<>();
        try {
            String sql = "SELECT id, client, typeChambre FROM reservations";
            Statement stmt = con.createStatement(); // ici, con est un champ
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("client");
                String typeChambre = rs.getString("typeChambre");
                reservations.add(new ReservationInfo(id, nom, typeChambre));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    private void initCustomComponents() {
        lblMontant = new javax.swing.JLabel("Montant:");
        lblMode = new javax.swing.JLabel("Mode de paiement:");
        txtMontant = new javax.swing.JTextField(10);
        txtMontant.setEditable(false); 
        cbModePaiement = new javax.swing.JComboBox<>(new String[] { "Espèce", "Carte", "Mobile Money" });
        btnValider = new javax.swing.JButton("Effectuer le paiement");
        lblClient = new javax.swing.JLabel("Client :");
        cbClients = new javax.swing.JComboBox<>(); // vide au départ, on remplira plus tard
        cbClients.addActionListener(e -> {
            ReservationInfo selected = (ReservationInfo) cbClients.getSelectedItem();
            if (selected != null && selected.typeChambre != null) {
                double prix = Chambres.getPrixParType(selected.typeChambre);
                txtMontant.setText(String.valueOf(prix));
            } else {
                txtMontant.setText("0");
            }
        });

        btnRetour = new JButton("Retour");
        // ActionListener pour le bouton Valider
        btnValider.addActionListener(e -> {
            ReservationInfo selected = (ReservationInfo) cbClients.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation.");
                return;
            }

            int reservationId = selected.idReservation;
            String nomClient = selected.nomClient;

            try {
                // Calcul automatique du prix selon le type de chambre
                double prix = Chambres.getPrixParType(selected.typeChambre);
                montant = prix; // Utilisé pour l'enregistrement en base
                txtMontant.setText(String.valueOf(prix)); // Affiche dans le champ
                txtMontant.setEditable(false); // Empêche la modification

                modePaiement = cbModePaiement.getSelectedItem().toString();

                // Enregistrement et génération de facture
                effectuerPaiement(reservationId, con, nomClient);
                JOptionPane.showMessageDialog(this, "Paiement effectué avec succès !");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue.");
                ex.printStackTrace();
            }
        });


        // ActionListener pour le bouton Retour (à ajouter ici)
        btnRetour.addActionListener(e -> {
            this.dispose(); // Ferme cette fenêtre
        // Tu peux aussi ouvrir une autre fenêtre ici si besoin
        new Reservations().setVisible(true);
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMontant)
                    .addComponent(lblClient)
                    .addComponent(lblMode))
                .addGap(10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMontant)
                    .addComponent(cbClients)
                    .addComponent(cbModePaiement)
                    .addComponent(btnValider)
                    .addComponent(btnRetour)) // bouton retour ajouté proprement ici
                .addGap(20)
        );


        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMontant)
                    .addComponent(txtMontant))
            .addGap(20)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblClient)
                .addComponent(cbClients))
            .addGap(20)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblMode)
                .addComponent(cbModePaiement))
            .addGap(30)
            .addComponent(btnValider)
            .addGap(10) 
            .addComponent(btnRetour)        
            .addGap(30)
        );


        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre
        setPreferredSize(new Dimension(500, 350));
    }
    /**
     * Creates new form Paiement
     */
    
    
    public void effectuerPaiement(int reservationId, Connection con, String nomClient) {
        try {
            // 1. Enregistrement dans la base de données
            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO paiement (reservation_id, montant, mode, date) VALUES (?, ?, ?, ?)"
            );
            pst.setInt(1, reservationId);
            pst.setDouble(2, montant);
            pst.setString(3, modePaiement);
            pst.setString(4, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datePaiement));
            pst.executeUpdate();
            pst.close();

            System.out.println(" Paiement enregistré avec succès dans la base de données.");

            // 2. Génération de la facture PDF
            genererFacturePDF(nomClient);
        } catch (Exception e) {
            System.err.println(" Erreur lors du paiement : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void genererFacturePDF(String nomClient) {
        System.out.println(" Génération de la facture pour " + nomClient);
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            String fileName = "facture_" + nomClient.replaceAll(" ", "_") + "_" + System.currentTimeMillis() + ".pdf";
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            com.itextpdf.text.Font titreFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph titre = new com.itextpdf.text.Paragraph("FACTURE - SMDEV HOTEL", titreFont);
            titre.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(titre);

            document.add(new com.itextpdf.text.Paragraph(" "));
            document.add(new com.itextpdf.text.Paragraph("Nom du client : " + nomClient));
            document.add(new com.itextpdf.text.Paragraph("Date : " + datePaiement.toString()));
            document.add(new com.itextpdf.text.Paragraph("Montant : " + montant + " FCFA"));
            document.add(new com.itextpdf.text.Paragraph("Mode de paiement : " + modePaiement));
            document.add(new com.itextpdf.text.Paragraph("Merci pour votre confiance !"));

           document.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    public double getMontant() {
        return montant;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");
                Paiement paiement = new Paiement(con);
                paiement.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur de connexion.");
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
