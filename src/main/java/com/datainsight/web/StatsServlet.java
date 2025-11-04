package com.datainsight.web;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dao.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Displays comprehensive analytics dashboard
 */
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    private TransactionDAO transactionDAO;
    private ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAO();
        clientDAO = new ClientDAO();
        System.out.println("✓ StatsServlet initialized");
    }

    /**
     * GET: Show analytics dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Global statistics
            long totalClients = clientDAO.count();
            long totalTransactions = transactionDAO.count();
            Double totalRevenue = transactionDAO.getTotalRevenue();
            Double avgTransaction = transactionDAO.getAverageTransactionAmount();

            // Aggregated analytics
            List<Object[]> revenueByCountry = transactionDAO.getRevenueByCountry();
            List<Object[]> topClients = transactionDAO.getTopClients(10);
            List<Object[]> revenueByCategory = transactionDAO.getRevenueByCategory();
            List<Object[]> salesByMonth = transactionDAO.getSalesByMonth();
            List<Object[]> salesByDay = transactionDAO.getSalesByDay(30);
            
            // Demographics
            List<Object[]> clientsByCountry = clientDAO.countByCountry();
            List<Object[]> clientsByProfession = clientDAO.countByProfession();

            // Set attributes
            request.setAttribute("totalClients", totalClients);
            request.setAttribute("totalTransactions", totalTransactions);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("avgTransaction", avgTransaction);
            
            request.setAttribute("revenueByCountry", revenueByCountry);
            request.setAttribute("topClients", topClients);
            request.setAttribute("revenueByCategory", revenueByCategory);
            request.setAttribute("salesByMonth", salesByMonth);
            request.setAttribute("salesByDay", salesByDay);
            
            request.setAttribute("clientsByCountry", clientsByCountry);
            request.setAttribute("clientsByProfession", clientsByProfession);
            
            request.setAttribute("pageTitle", "Analytics Dashboard");

            request.getRequestDispatcher("/pages/stats.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/pages/stats.jsp").forward(request, response);
        }
    }
}