package com.datainsight.dto;

import com.datainsight.model.Client;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Client entity
 * Used to expose client data via REST API without circular references
 */
public class ClientDTO {
    
    private Long id;
    private String nom;
    private String prenom;
    private String pays;
    private Integer age;
    private String profession;
    private String email;
    private LocalDateTime dateInscription;
    private String statut;
    private Integer nombreTransactions;
    
    // Constructors
    public ClientDTO() {}
    
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.nom = client.getNom();
        this.prenom = client.getPrenom();
        this.pays = client.getPays();
        this.age = client.getAge();
        this.profession = client.getProfession();
        this.email = client.getEmail();
        this.dateInscription = client.getDateInscription();
        this.statut = client.getStatut();
        this.nombreTransactions = client.getTransactions() != null ? 
            client.getTransactions().size() : 0;
    }
    
    // Static factory method
    public static ClientDTO fromEntity(Client client) {
        return new ClientDTO(client);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getNombreTransactions() {
        return nombreTransactions;
    }

    public void setNombreTransactions(Integer nombreTransactions) {
        this.nombreTransactions = nombreTransactions;
    }
}