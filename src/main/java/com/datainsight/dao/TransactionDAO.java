package com.datainsight.dao;

import com.datainsight.model.Transaction;
import com.datainsight.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO for Transaction entity - handles CRUD and analytics queries
 */
public class TransactionDAO {

    /**
     * Create new transaction
     */
    public Transaction create(Transaction transaction) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(transaction);
            em.getTransaction().commit();
            return transaction;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to create transaction", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find transaction by ID
     */
    public Transaction findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Transaction.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Find recent transactions (with JOIN FETCH to avoid N+1)
     */
    public List<Transaction> findRecent(int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t JOIN FETCH t.client " +
                "ORDER BY t.date DESC, t.id DESC", 
                Transaction.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find transactions by client ID
     */
    public List<Transaction> findByClient(Long clientId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t WHERE t.client.id = :clientId " +
                "ORDER BY t.date DESC", 
                Transaction.class);
            query.setParameter("clientId", clientId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find transactions by category
     */
    public List<Transaction> findByCategorie(String categorie, int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t JOIN FETCH t.client " +
                "WHERE t.categorie = :categorie ORDER BY t.date DESC", 
                Transaction.class);
            query.setParameter("categorie", categorie);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find transactions by date range
     */
    public List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t JOIN FETCH t.client " +
                "WHERE t.date BETWEEN :startDate AND :endDate ORDER BY t.date DESC", 
                Transaction.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Count total transactions
     */
    public long count() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(t) FROM Transaction t", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Revenue by country (total and average)
     */
    public List<Object[]> getRevenueByCountry() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.client.pays, SUM(t.montant), AVG(t.montant), COUNT(t) " +
                "FROM Transaction t " +
                "GROUP BY t.client.pays " +
                "ORDER BY SUM(t.montant) DESC", 
                Object[].class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Top clients by total spending
     */
    public List<Object[]> getTopClients(int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.client.nom, t.client.prenom, t.client.pays, " +
                "SUM(t.montant) as total, COUNT(t) as nbTransactions " +
                "FROM Transaction t " +
                "GROUP BY t.client.id, t.client.nom, t.client.prenom, t.client.pays " +
                "ORDER BY total DESC", 
                Object[].class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Revenue by category
     */
    public List<Object[]> getRevenueByCategory() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.categorie, SUM(t.montant), AVG(t.montant), COUNT(t) " +
                "FROM Transaction t " +
                "GROUP BY t.categorie " +
                "ORDER BY SUM(t.montant) DESC", 
                Object[].class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Sales by month
     */
    public List<Object[]> getSalesByMonth() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT YEAR(t.date), MONTH(t.date), SUM(t.montant), COUNT(t) " +
                "FROM Transaction t " +
                "GROUP BY YEAR(t.date), MONTH(t.date) " +
                "ORDER BY YEAR(t.date) DESC, MONTH(t.date) DESC", 
                Object[].class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Sales by day (last N days)
     */
    public List<Object[]> getSalesByDay(int days) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            LocalDate since = LocalDate.now().minusDays(days);
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.date, SUM(t.montant), COUNT(t) " +
                "FROM Transaction t " +
                "WHERE t.date >= :since " +
                "GROUP BY t.date " +
                "ORDER BY t.date DESC", 
                Object[].class);
            query.setParameter("since", since);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Total revenue
     */
    public Double getTotalRevenue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Double result = em.createQuery(
                "SELECT SUM(t.montant) FROM Transaction t", Double.class)
                .getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    /**
     * ANALYTICS: Average transaction amount
     */
    public Double getAverageTransactionAmount() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Double result = em.createQuery(
                "SELECT AVG(t.montant) FROM Transaction t", Double.class)
                .getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    /**
     * Delete transaction
     */
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Transaction transaction = em.find(Transaction.class, id);
            if (transaction != null) {
                em.remove(transaction);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete transaction", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete old transactions (cleanup)
     */
    public int deleteOlderThan(LocalDate date) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                "DELETE FROM Transaction t WHERE t.date < :date")
                .setParameter("date", date)
                .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete old transactions", e);
        } finally {
            em.close();
        }
    }
}