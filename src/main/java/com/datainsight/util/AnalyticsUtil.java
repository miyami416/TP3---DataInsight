package com.datainsight.util;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dao.TransactionDAO;
import java.util.List;

/**
 * Analytics utilities - generates comprehensive reports from business data
 */
public class AnalyticsUtil {

    private final ClientDAO clientDAO;
    private final TransactionDAO transactionDAO;

    public AnalyticsUtil() {
        this.clientDAO = new ClientDAO();
        this.transactionDAO = new TransactionDAO();
    }

    /**
     * Generate comprehensive business intelligence report
     */
    public void printComprehensiveReport() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          DATAINSIGHT - BUSINESS ANALYTICS REPORT           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Global stats
        long totalClients = clientDAO.count();
        long totalTransactions = transactionDAO.count();
        Double totalRevenue = transactionDAO.getTotalRevenue();
        Double avgTransaction = transactionDAO.getAverageTransactionAmount();

        System.out.println("📊 GLOBAL STATISTICS");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.printf("Total Clients:        %,d%n", totalClients);
        System.out.printf("Total Transactions:   %,d%n", totalTransactions);
        System.out.printf("Total Revenue:        €%,.2f%n", totalRevenue);
        System.out.printf("Average Transaction:  €%,.2f%n", avgTransaction);
        System.out.println();

        // Revenue by country
        System.out.println("🌍 REVENUE BY COUNTRY");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Country        │ Total Revenue  │ Avg Amount │ Transactions");
        System.out.println("───────────────┼────────────────┼────────────┼─────────────");
        
        List<Object[]> revenueByCountry = transactionDAO.getRevenueByCountry();
        for (Object[] row : revenueByCountry) {
            String pays = (String) row[0];
            Double total = (Double) row[1];
            Double avg = (Double) row[2];
            Long count = (Long) row[3];
            System.out.printf("%-14s │ €%,12.2f │ €%,8.2f │ %,11d%n", 
                pays, total, avg, count);
        }
        System.out.println();

        // Top 10 clients
        System.out.println("🏆 TOP 10 CLIENTS BY SPENDING");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Rank │ Name                    │ Country  │ Total Spent  │ Txns");
        System.out.println("─────┼─────────────────────────┼──────────┼──────────────┼─────");
        
        List<Object[]> topClients = transactionDAO.getTopClients(10);
        int rank = 1;
        for (Object[] row : topClients) {
            String nom = (String) row[0];
            String prenom = (String) row[1];
            String pays = (String) row[2];
            Double total = (Double) row[3];
            Long nbTxns = (Long) row[4];
            String fullName = prenom + " " + nom;
            System.out.printf("%4d │ %-23s │ %-8s │ €%,10.2f │ %,4d%n", 
                rank++, fullName, pays, total, nbTxns);
        }
        System.out.println();

        // Revenue by category
        System.out.println("📦 REVENUE BY CATEGORY");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Category       │ Total Revenue  │ Avg Amount │ Transactions");
        System.out.println("───────────────┼────────────────┼────────────┼─────────────");
        
        List<Object[]> revenueByCategory = transactionDAO.getRevenueByCategory();
        for (Object[] row : revenueByCategory) {
            String categorie = (String) row[0];
            Double total = (Double) row[1];
            Double avg = (Double) row[2];
            Long count = (Long) row[3];
            System.out.printf("%-14s │ €%,12.2f │ €%,8.2f │ %,11d%n", 
                categorie, total, avg, count);
        }
        System.out.println();

        // Monthly sales
        System.out.println("📅 SALES BY MONTH");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Year-Month │ Revenue        │ Transactions");
        System.out.println("───────────┼────────────────┼─────────────");
        
        List<Object[]> salesByMonth = transactionDAO.getSalesByMonth();
        for (Object[] row : salesByMonth) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            Double total = (Double) row[2];
            Long count = (Long) row[3];
            System.out.printf("%d-%02d    │ €%,12.2f │ %,11d%n", 
                year, month, total, count);
        }
        System.out.println();

        // Client demographics
        System.out.println("👥 CLIENT DEMOGRAPHICS");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        List<Object[]> countByCountry = clientDAO.countByCountry();
        System.out.println("\nClients by Country:");
        for (Object[] row : countByCountry) {
            String pays = (String) row[0];
            Long count = (Long) row[1];
            double percentage = (count * 100.0) / totalClients;
            System.out.printf("  %-14s: %,6d (%.1f%%)%n", pays, count, percentage);
        }

        List<Object[]> countByProfession = clientDAO.countByProfession();
        System.out.println("\nClients by Profession:");
        for (Object[] row : countByProfession) {
            String profession = (String) row[0];
            Long count = (Long) row[1];
            double percentage = (count * 100.0) / totalClients;
            System.out.printf("  %-14s: %,6d (%.1f%%)%n", profession, count, percentage);
        }

        System.out.println("\n╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Standalone test
     */
    public static void main(String[] args) {
        AnalyticsUtil analytics = new AnalyticsUtil();

        try {
            analytics.printComprehensiveReport();
        } finally {
            JpaUtil.close();
        }
    }
}