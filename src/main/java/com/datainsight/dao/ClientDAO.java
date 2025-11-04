package com.datainsight.dao;

import com.datainsight.model.Client;
import com.datainsight.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * DAO for Client entity - handles CRUD operations
 */
public class ClientDAO {

    /**
     * Create new client
     */
    public Client create(Client client) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return client;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to create client", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find client by ID
     */
    public Client findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Find all clients
     */
    public List<Client> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c ORDER BY c.id DESC", Client.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find clients by country
     */
    public List<Client> findByPays(String pays) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.pays = :pays ORDER BY c.nom", 
                Client.class);
            query.setParameter("pays", pays);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find clients by profession
     */
    public List<Client> findByProfession(String profession) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.profession = :profession ORDER BY c.nom", 
                Client.class);
            query.setParameter("profession", profession);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Update client
     */
    public Client update(Client client) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Client updated = em.merge(client);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update client", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete client
     */
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client != null) {
                em.remove(client);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete client", e);
        } finally {
            em.close();
        }
    }

    /**
     * Count total clients
     */
    public long count() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Client c", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Count clients by country
     */
    public List<Object[]> countByCountry() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT c.pays, COUNT(c) FROM Client c GROUP BY c.pays ORDER BY COUNT(c) DESC", 
                Object[].class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Count clients by profession
     */
    public List<Object[]> countByProfession() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
        	TypedQuery<Object[]> query = em.createQuery(
                    "SELECT c.profession, COUNT(c) FROM Client c GROUP BY c.profession ORDER BY COUNT(c) DESC", 
                    Object[].class);
                return query.getResultList();
            } finally {
                em.close();
            }
        }

        /**
         * Get average age by country
         */
        public List<Object[]> getAverageAgeByCountry() {
            EntityManager em = JpaUtil.getEntityManager();
            try {
                TypedQuery<Object[]> query = em.createQuery(
                    "SELECT c.pays, AVG(c.age), COUNT(c) FROM Client c " +
                    "GROUP BY c.pays ORDER BY AVG(c.age) DESC", 
                    Object[].class);
                return query.getResultList();
            } finally {
                em.close();
            }
        }
    }