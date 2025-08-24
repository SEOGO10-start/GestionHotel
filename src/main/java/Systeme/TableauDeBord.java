/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Systeme;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jfree.chart.renderer.category.BarRenderer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import org.jfree.chart.ChartFactory;

/**
 *
 * @author MOUSSA SEOGO
 */
public class TableauDeBord extends javax.swing.JFrame {

    private Connection con;
    private JTable jTableStats;
    private JTable jTableReservations;
    private JLabel lblTotalChambres;
    private JPanel pnlChart;

    public TableauDeBord() {
        initCustomComponents();
        Connect();
        loadStats();
        setupCharts();
        setLocationRelativeTo(null);
    }

    private void Connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:/Users/MOUSSA SEOGO/Documents/NetBeansProjects/GestionHotel/src/main/java/Systeme/hotel.db");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur DB: " + e.getMessage());
        }
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tableau de Bord - Gestion Hôtel");
        setSize(900, 700);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel lblTitle = new JLabel("TABLEAU DE BORD", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Panel des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Tableau des stats
        jTableStats = new JTable();
        jTableStats.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Statut", "Nombre"}
        ));
        JScrollPane scrollStats = new JScrollPane(jTableStats);
        
        // Panel du graphique
        pnlChart = new JPanel(new BorderLayout());
        pnlChart.setBorder(new TitledBorder("Répartition des chambres"));
        
        statsPanel.add(scrollStats);
        statsPanel.add(pnlChart);
        
        // Panel des réservations
        JPanel resaPanel = new JPanel(new BorderLayout());
        resaPanel.setBorder(new TitledBorder("Dernières réservations"));
        
        jTableReservations = new JTable();
        jTableReservations.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Chambre", "Client", "Arrivée", "Départ"}
        ));
        resaPanel.add(new JScrollPane(jTableReservations), BorderLayout.CENTER);
        
        // Panel inférieur
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        lblTotalChambres = new JLabel("Total: 0");
        lblTotalChambres.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Actualiser");
        JButton btnChambres = new JButton("Gestion Chambres");
        JButton btnMenu = new JButton("Menu Principal");
        
        // Gestion des événements
        btnRefresh.addActionListener(e -> {
            loadStats();
            setupCharts();
        });
        
        btnChambres.addActionListener(e -> {
            new Chambres().setVisible(true);
            dispose();
        });
        
        btnMenu.addActionListener(e -> {
            new MenuePrincipale().setVisible(true);
            dispose();
        });
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnChambres);
        buttonPanel.add(btnMenu);
        
        bottomPanel.add(lblTotalChambres, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Assemblage
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(resaPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void loadStats() {
        try {
            // Statistiques des chambres
            String sql = "SELECT statut, COUNT(*) as count FROM chambres GROUP BY statut";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) jTableStats.getModel();
            model.setRowCount(0);
            
            int totalChambres = 0;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("statut"),
                    rs.getInt("count")
                });
                totalChambres += rs.getInt("count");
            }
            lblTotalChambres.setText("Total: " + totalChambres);
            
            // Dernières réservations
            sql = "SELECT c.numero, r.client, r.date_entree, r.date_sortie " +
                  "FROM reservations r JOIN chambres c ON r.chambre_id = c.id " +
                  "ORDER BY r.date_entree DESC LIMIT 5";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            
            model = (DefaultTableModel) jTableReservations.getModel();
            model.setRowCount(0);
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("numero"),
                    rs.getString("client"),
                    rs.getString("date_entree"),
                    rs.getString("date_sortie")
                });
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement stats: " + e.getMessage());
        }
    }

    private void setupCharts() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        try {
            String sql = "SELECT statut, COUNT(*) as count FROM chambres GROUP BY statut";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
        
            // Réinitialisation du dataset
            dataset.clear();
        
            while (rs.next()) {
                String statut = rs.getString("statut");
                int count = rs.getInt("count");
                dataset.addValue(count, "Chambres", statut);
            }
        
            JFreeChart chart = ChartFactory.createBarChart(
                "Répartition des chambres", 
                "Statut", 
                "Nombre", 
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        
            // Personnalisation des couleurs
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
            // Définition des couleurs pour chaque catégorie
            renderer.setSeriesPaint(0, Color.RED);    // Occupé
            renderer.setSeriesPaint(1, Color.GREEN);  // Libre
            renderer.setSeriesPaint(2, Color.BLUE);   // Maintenance
            renderer.setSeriesPaint(3, Color.YELLOW); // Nettoyage
        
            // Optionnel: Supprimer les bordures des barres
            renderer.setDrawBarOutline(false);
        
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
            pnlChart.removeAll();
            pnlChart.add(chartPanel);
            pnlChart.revalidate();
        
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, "Erreur création graphique: " + e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TableauDeBord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableauDeBord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableauDeBord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableauDeBord.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TableauDeBord().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
