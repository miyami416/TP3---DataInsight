package com.datainsight.dto;

import com.datainsight.model.Transaction;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction entity
 */
public class TransactionDTO {
    
    private Long id;
    private LocalDate date;
    private Double montant;
    private String categorie;
    private String description;
    private String modePaiement;
    private String referenceTransaction;
    private LocalDateTime createdAt;
    
    // Client info (nested DTO)
    private Long clientId;
    private String clientNom;
    private String clientPrenom;
    private String clientPays;
    
    // Constructors
    public TransactionDTO() {}
    
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.montant = transaction.getMontant();
        this.categorie = transaction.getCategorie();
        this.description = transaction.getDescription();
        this.modePaiement = transaction.getModePaiement();
        this.referenceTransaction = transaction.getReferenceTransaction();
        this.createdAt = transaction.getCreatedAt();
        
        if (transaction.getClient() != null) {
            this.clientId = transaction.getClient().getId();
            this.clientNom = transaction.getClient().getNom();
            this.clientPrenom = transaction.getClient().getPrenom();
            this.clientPays = transaction.getClient().getPays();
        }
    }
    
    // Static factory method
    public static TransactionDTO fromEntity(Transaction transaction) {
        return new TransactionDTO(transaction);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getClientPrenom() {
        return clientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public String getClientPays() {
        return clientPays;
    }

    public void setClientPays(String clientPays) {
        this.clientPays = clientPays;
    }
}