package com.datainsight.util;

import com.datainsight.dao.ClientDAO;
import com.datainsight.model.Client;
import com.datainsight.model.Transaction;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates batch test data for clients and transactions
 * Demonstrates high-volume insert with batching - BIG DATA simulation
 */
public class DataGenerator {

    private static final Random random = new Random();

    // Sample data arrays
    private static final String[] FIRST_NAMES = {
        "Mohammed", "Fatima", "Ahmed", "Aisha", "Hassan", "Khadija", "Youssef", "Zainab",
        "Omar", "Mariam", "Ali", "Nour", "Karim", "Salma", "Amine", "Layla",
        "Jean", "Marie", "Pierre", "Sophie", "Luc", "Emma", "Marc", "Julie",
        "John", "Sarah", "Michael", "Emily", "David", "Anna", "James", "Lisa"
    };

    private static final String[] LAST_NAMES = {
        "Alami", "Benali", "Idrissi", "El Amrani", "Tazi", "Kabbaj", "Benjelloun", "Fassi",
        "Martin", "Bernard", "Dubois", "Thomas", "Robert", "Richard", "Petit", "Durand",
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Lopez", "Rodriguez", "Martinez", "Hernandez", "Gonzalez", "Wilson", "Anderson", "Taylor"
    };

    private static final String[] COUNTRIES = {
        "Maroc", "France", "Canada", "Espagne", "Belgique", "Suisse", 
        "USA", "Allemagne", "Italie", "Portugal"
    };

    private static final String[] PROFESSIONS = {
        "Ingénieur", "Médecin", "Professeur", "Entrepreneur", "Étudiant",
        "Développeur", "Manager", "Consultant", "Architecte", "Designer",
        "Comptable", "Avocat", "Pharmacien", "Infirmier", "Commercial"
    };

    private static final String[] CATEGORIES = {
        "Informatique", "Santé", "Éducation", "Voyage", "Alimentation",
        "Vêtements", "Électronique", "Sport", "Culture", "Automobile",
        "Immobilier", "Services", "Loisirs", "Beauté", "Mobilier"
    };

    private static final String[] PAYMENT_MODES = {
        "carte", "especes", "virement", "paypal", "crypto"
    };

    /**
     * Generate sample clients if database is empty
     */
    public static List<Client> generateClients(int count) {
        ClientDAO clientDAO = new ClientDAO();
        
        System.out.println("👥 Generating " + count + " clients...");
        List<Client> clients = new ArrayList<>();

        EntityManager em = JpaUtil.getEntityManager();
        int batchSize = 50;

        try {
            em.getTransaction().begin();

            for (int i = 0; i < count; i++) {
                String prenom = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String nom = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                String pays = COUNTRIES[random.nextInt(COUNTRIES.length)];
                int age = 18 + random.nextInt(65); // 18-82 years old
                String profession = PROFESSIONS[random.nextInt(PROFESSIONS.length)];

                Client client = new Client(nom, prenom, pays, age, profession);
                
                // Set random status
                double rand = random.nextDouble();
                if (rand > 0.9) {
                    client.setStatut("premium");
                } else if (rand > 0.8) {
                    client.setStatut("inactif");
                } else {
                    client.setStatut("actif");
                }

                em.persist(client);
                clients.add(client);

                // Flush and clear every batch_size
                if (i > 0 && i % batchSize == 0) {
                    em.flush();
                    em.clear();
                    System.out.print(".");
                    if (i % (batchSize * 20) == 0) {
                        System.out.println(" " + i);
                    }
                }
            }

            em.flush();
            em.clear();
            em.getTransaction().commit();

            System.out.println("\n✓ Created " + count + " clients");
            return clients;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✗ Failed to generate clients: " + e.getMessage());
            e.printStackTrace();
            return clients;
        } finally {
            em.close();
        }
    }

    /**
     * BATCH INSERT: Generate large number of transactions
     * Uses Hibernate batch processing for performance
     */
    public static int generateTransactions(int transactionsPerClient) {
        long startTime = System.currentTimeMillis();
        
        // Get all clients
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();
        
        if (clients.isEmpty()) {
            System.err.println("✗ No clients found! Generate clients first.");
            return 0;
        }

        int totalTransactions = clients.size() * transactionsPerClient;
        System.out.println("🚀 Starting batch insert of " + totalTransactions + " transactions...");
        System.out.println("   (" + transactionsPerClient + " transactions per client)");

        EntityManager em = JpaUtil.getEntityManager();
        int inserted = 0;
        int batchSize = 50; // Must match persistence.xml hibernate.jdbc.batch_size

        try {
            em.getTransaction().begin();

            for (Client client : clients) {
                // Refresh client in this context
                Client managedClient = em.find(Client.class, client.getId());
                
                for (int i = 0; i < transactionsPerClient; i++) {
                    Transaction transaction = createRealisticTransaction(managedClient);
                    
                    em.persist(transaction);
                    inserted++;

                    // Flush and clear every batch_size to avoid memory issues
                    if (inserted > 0 && inserted % batchSize == 0) {
                        em.flush();
                        em.clear();
                        System.out.print(".");
                        if (inserted % (batchSize * 20) == 0) {
                            System.out.println(" " + inserted);
                        }
                    }
                }
            }

            // Final flush
            em.flush();
            em.clear();
            em.getTransaction().commit();

            long duration = System.currentTimeMillis() - startTime;
            double rate = (inserted * 1000.0) / duration;

            System.out.println("\n✓ Batch insert completed!");
            System.out.println("  - Inserted: " + inserted + " transactions");
            System.out.println("  - Duration: " + duration + " ms");
            System.out.println("  - Rate: " + String.format("%.0f", rate) + " records/sec");

            return inserted;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✗ Batch insert failed: " + e.getMessage());
            e.printStackTrace();
            return inserted;
        } finally {
            em.close();
        }
    }

    /**
     * Create realistic transaction
     */
    private static Transaction createRealisticTransaction(Client client) {
        // Random date within last 365 days
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        
        // Random amount based on category
        String categorie = CATEGORIES[random.nextInt(CATEGORIES.length)];
        double montant = generateRealisticAmount(categorie);
        
        // Random payment mode
        String modePaiement = PAYMENT_MODES[random.nextInt(PAYMENT_MODES.length)];
        
        Transaction transaction = new Transaction(date, montant, categorie, client);
        transaction.setModePaiement(modePaiement);
        transaction.setDescription("Transaction " + categorie + " - " + date);
        
        return transaction;
    }

    /**
     * Generate realistic amount based on category
     */
    private static double generateRealisticAmount(String categorie) {
        switch (categorie) {
            case "Informatique":
            case "Électronique":
                return 200 + random.nextDouble() * 1800; // 200-2000€
            case "Immobilier":
                return 50000 + random.nextDouble() * 450000; // 50k-500k€
            case "Automobile":
                return 5000 + random.nextDouble() * 45000; // 5k-50k€
            case "Voyage":
                return 300 + random.nextDouble() * 2700; // 300-3000€
            case "Alimentation":
                return 10 + random.nextDouble() * 190; // 10-200€
            case "Vêtements":
            case "Beauté":
                return 20 + random.nextDouble() * 480; // 20-500€
            case "Santé":
            case "Éducation":
                return 50 + random.nextDouble() * 950; // 50-1000€
            default:
                return 50 + random.nextDouble() * 450; // 50-500€
        }
    }

    /**
     * Standalone test - run as Java application
     */
    public static void main(String[] args) {
        System.out.println("=== DataInsight Generator ===\n");

        try {
            int numClients = 1000; // Default 1000 clients
            int transactionsPerClient = 10; // 10 transactions each = 10,000 total

            // Parse command line arguments
            if (args.length > 0) {
                numClients = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                transactionsPerClient = Integer.parseInt(args[1]);
            }

            // Step 1: Generate clients
            List<Client> clients = generateClients(numClients);
            System.out.println("\nClients in database: " + clients.size());

            // Step 2: Generate transactions
            int inserted = generateTransactions(transactionsPerClient);
            
            System.out.println("\n=== SUMMARY ===");
            System.out.println("Total clients: " + clients.size());
            System.out.println("Total transactions: " + inserted);
            System.out.println("Expected rate: 1,000-5,000 records/sec");

        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JpaUtil.close();
        }
    }
}