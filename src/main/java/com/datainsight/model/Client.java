package com.datainsight.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Client entity - represents a customer with their profile and transaction history
 */
@Entity
@Table(name = "clients", indexes = {
    @Index(name = "idx_pays", columnList = "pays"),
    @Index(name = "idx_profession", columnList = "profession")
})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String pays;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 100)
    private String profession;

    @Column(length = 200)
    private String email;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;

    @Column(length = 20)
    private String statut; // actif, inactif, premium

    // One client has many transactions (bidirectional)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public Client() {
        this.dateInscription = LocalDateTime.now();
        this.statut = "actif";
    }

    public Client(String nom, String prenom, String pays, Integer age, String profession) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.profession = profession;
        this.email = generateEmail(nom, prenom);
    }

    public Client(String nom, String pays, Integer age, String profession) {
        this(nom, "", pays, age, profession);
    }

    // Helper method to generate email
    private String generateEmail(String nom, String prenom) {
        String base = (prenom.isEmpty() ? nom : prenom + "." + nom).toLowerCase();
        base = base.replaceAll("[^a-z.]", "");
        return base + "@datainsight.com";
    }

    // Helper method to add transaction
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setClient(this);
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", pays='" + pays + '\'' +
                ", age=" + age +
                ", profession='" + profession + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}