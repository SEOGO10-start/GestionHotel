 Hotel Réservation System


 Description
Solution complète de gestion hôtelière offrant :
- Gestion centralisée des clients, chambres et réservations
- Système de paiement intégré avec génération de factures
- Tableau de bord analytique en temps réel
- Interface sécurisée avec gestion des permissions

 Technologies
- Langage : Java SE 8+
- Interface : Swing (NetBeans GUI Builder)
- Base de données : SQLite
- Bibliothèques :
  - JDBC pour la connexion DB
  - JFreeChart pour les graphiques
  - iText pour les PDF

 Installation
 Prérequis
- Java Runtime Environment 8+
- 500 Mo d'espace disque

 Étapes
1. Télécharger le dossier d'application
2. Exécuter le fichier JAR :
   ```bash
   java -jar msdev_hotel.jar
Utiliser les identifiants par défaut :

Admin : admin/admin123

Réception : user/user123

 Fonctionnalités Principales
Module	Description
🔐 Authentification	Connexion sécurisée avec rôles
👥 Gestion Clients	CRUD complet avec recherche avancée
🛏 Gestion Chambres	Suivi des statuts (Libre/Occupé/Nettoyage)
📅 Réservations	Système intégré avec vérification de disponibilité
💳 Paiements	Génération automatique de factures PDF
📊 Tableau de bord	Statistiques visuelles et rapports
📋 Base de Données
Structure principale :

sql
CREATE TABLE chambres (
    numero TEXT PRIMARY KEY,
    type TEXT,
    prix REAL,
    statut TEXT
);
Emplacement : /data/hotel.db

🎨 Captures d'écran
Écran	Description
https://docs/login.png	Authentification sécurisée
https://docs/dashboard.png	Vue analytique
⚙️ Configuration
Modifier config.properties pour :

Changer le chemin de la DB

Ajuster les paramètres de connexion

Personnaliser les tarifs des chambres

📜 Licence
Propriétaire - © 2023 SEOGO NANAWENDIN MOUSSA 

✉️ Contact
moussaseogo74@gmail.com
+226 64 68 75 33
