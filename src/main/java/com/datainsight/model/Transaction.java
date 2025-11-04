package com.datainsight.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transaction entity - represents a purchase or transaction made by a client
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_date", columnList = "date"),
    @Index(name = "idx_categorie", columnList = "categorie"),
    @Index(name = "idx_client_date", columnList = "client_id, date")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false, length = 100)
    private String categorie;

    @Column(length = 500)
    private String description;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement; // carte, especes, virement, paypal

    @Column(name = "reference_transaction", length = 100, unique = true)
    private String referenceTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Transaction() {
        this.createdAt = LocalDateTime.now();
        this.modePaiement = "carte";
    }

    public Transaction(LocalDate date, Double montant, String categorie, Client client) {
        this();
        this.date = date;
        this.montant = montant;
        this.categorie = categorie;
        this.client = client;
        this.referenceTransaction = generateReference();
    }

    public Transaction(LocalDate date, Double montant, String categorie, String description, Client client) {
        this(date, montant, categorie, client);
        this.description = description;
    }

    // Generate unique transaction reference
    private String generateReference() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getReferenceTransaction() {
        return referenceTransaction;
    }

    public void setReferenceTransaction(String referenceTransaction) {
        this.referenceTransaction = referenceTransaction;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", montant=" + montant +
                ", categorie='" + categorie + '\'' +
                ", referenceTransaction='" + referenceTransaction + '\'' +
                '}';
    }
}