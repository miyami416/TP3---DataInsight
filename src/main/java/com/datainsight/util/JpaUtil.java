package com.datainsight.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * JPA Utility - manages EntityManagerFactory lifecycle
 * Thread-safe singleton pattern
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "datainsightPU";
    private static EntityManagerFactory emf;

    // Private constructor - singleton
    private JpaUtil() {}

    /**
     * Get EntityManagerFactory (lazy initialization)
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            try {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                System.out.println("✓ EntityManagerFactory created successfully");
            } catch (Exception e) {
                System.err.println("✗ Failed to create EntityManagerFactory");
                e.printStackTrace();
                throw new RuntimeException("Could not initialize JPA", e);
            }
        }
        return emf;
    }

    /**
     * Create new EntityManager
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Close EntityManagerFactory (call on app shutdown)
     */
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✓ EntityManagerFactory closed");
        }
    }
}