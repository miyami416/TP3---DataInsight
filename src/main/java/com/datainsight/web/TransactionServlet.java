package com.datainsight.web;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dao.TransactionDAO;
import com.datainsight.model.Client;
import com.datainsight.model.Transaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles transaction queries and creation
 */
@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {

    private TransactionDAO transactionDAO;
    private ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAO();
        clientDAO = new ClientDAO();
        System.out.println("✓ TransactionServlet initialized");
    }

    /**
     * GET: List transactions with optional filters
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categorie = request.getParameter("categorie");
        String limitParam = request.getParameter("limit");

        try {
            int limit = 100; // Default limit
            if (limitParam != null && !limitParam.isEmpty()) {
                limit = Integer.parseInt(limitParam);
            }

            List<Transaction> transactions;
            
            if (categorie != null && !categorie.isEmpty()) {
                transactions = transactionDAO.findByCategorie(categorie, limit);
                request.setAttribute("filterType", "Catégorie: " + categorie);
            } else {
                transactions = transactionDAO.findRecent(limit);
            }

            long totalCount = transactionDAO.count();
            Double totalRevenue = transactionDAO.getTotalRevenue();
            Double avgAmount = transactionDAO.getAverageTransactionAmount();

            request.setAttribute("transactions", transactions);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("avgAmount", avgAmount);
            request.setAttribute("limit", limit);
            request.setAttribute("pageTitle", "Transactions Récentes");

            request.getRequestDispatcher("/pages/transactions.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/pages/transactions.jsp").forward(request, response);
        }
    }

    /**
     * POST: Create new transaction
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createTransaction(request, response);
        } else if ("delete".equals(action)) {
            deleteTransaction(request, response);
        }
    }

    /**
     * Create new transaction
     */
    private void createTransaction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String clientIdStr = request.getParameter("clientId");
            String dateStr = request.getParameter("date");
            String montantStr = request.getParameter("montant");
            String categorie = request.getParameter("categorie");
            String description = request.getParameter("description");
            String modePaiement = request.getParameter("modePaiement");

            // Validation
            if (clientIdStr == null || dateStr == null || montantStr == null || 
                categorie == null || categorie.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + 
                    "/transactions?error=Champs obligatoires manquants");
                return;
            }

            Long clientId = Long.parseLong(clientIdStr);
            LocalDate date = LocalDate.parse(dateStr);
            Double montant = Double.parseDouble(montantStr);

            Client client = clientDAO.findById(clientId);
            if (client == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/transactions?error=Client introuvable");
                return;
            }

            Transaction transaction = new Transaction(date, montant, categorie, description, client);
            if (modePaiement != null && !modePaiement.isEmpty()) {
                transaction.setModePaiement(modePaiement);
            }

            transactionDAO.create(transaction);

            response.sendRedirect(request.getContextPath() + "/transactions?success=created");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + 
                "/transactions?error=" + e.getMessage());
        }
    }

    /**
     * Delete transaction
     */
    private void deleteTransaction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            transactionDAO.delete(id);

            response.sendRedirect(request.getContextPath() + "/transactions?success=deleted");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + 
                "/transactions?error=" + e.getMessage());
        }
    }
}