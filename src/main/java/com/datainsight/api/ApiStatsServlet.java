package com.datainsight.api;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dao.TransactionDAO;
import com.datainsight.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API for Analytics/Statistics
 * Base URL: /api/stats
 */
@WebServlet("/api/stats/*")
public class ApiStatsServlet extends HttpServlet {

    private TransactionDAO transactionDAO;
    private ClientDAO clientDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAO();
        clientDAO = new ClientDAO();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("✓ ApiStatsServlet initialized");
    }

    /**
     * GET: Retrieve statistics
     * /api/stats/overview - global statistics
     * /api/stats/revenue-by-country - revenue aggregated by country
     * /api/stats/revenue-by-category - revenue aggregated by category
     * /api/stats/top-clients - top spending clients
     * /api/stats/sales-by-month - monthly sales
     * /api/stats/clients-by-country - client distribution by country
     * /api/stats/clients-by-profession - client distribution by profession
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/overview")) {
                getOverview(response);
            } else if (pathInfo.equals("/revenue-by-country")) {
                getRevenueByCountry(response);
            } else if (pathInfo.equals("/revenue-by-category")) {
                getRevenueByCategory(response);
            } else if (pathInfo.equals("/top-clients")) {
                getTopClients(request, response);
            } else if (pathInfo.equals("/sales-by-month")) {
                getSalesByMonth(response);
            } else if (pathInfo.equals("/sales-by-day")) {
                getSalesByDay(request, response);
            } else if (pathInfo.equals("/clients-by-country")) {
                getClientsByCountry(response);
            } else if (pathInfo.equals("/clients-by-profession")) {
                getClientsByProfession(response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ApiResponse<String> apiResponse = ApiResponse.error("Endpoint not found");
                objectMapper.writeValue(response.getWriter(), apiResponse);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Server error: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * Global overview statistics
     */
    private void getOverview(HttpServletResponse response) throws IOException {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalClients", clientDAO.count());
        stats.put("totalTransactions", transactionDAO.count());
        stats.put("totalRevenue", transactionDAO.getTotalRevenue());
        stats.put("averageTransaction", transactionDAO.getAverageTransactionAmount());
        
        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.success(stats);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Revenue aggregated by country
     */
    private void getRevenueByCountry(HttpServletResponse response) throws IOException {
        List<Object[]> data = transactionDAO.getRevenueByCountry();
        
        // Convert to structured format
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("pays", row[0]);
            map.put("totalRevenue", row[1]);
            map.put("averageAmount", row[2]);
            map.put("transactionCount", row[3]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Revenue aggregated by category
     */
    private void getRevenueByCategory(HttpServletResponse response) throws IOException {
        List<Object[]> data = transactionDAO.getRevenueByCategory();
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("categorie", row[0]);
            map.put("totalRevenue", row[1]);
            map.put("averageAmount", row[2]);
            map.put("transactionCount", row[3]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Top spending clients
     */
    private void getTopClients(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String limitParam = request.getParameter("limit");
        int limit = 10; // Default
        if (limitParam != null && !limitParam.isEmpty()) {
            limit = Integer.parseInt(limitParam);
        }
        
        List<Object[]> data = transactionDAO.getTopClients(limit);
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nom", row[0]);
            map.put("prenom", row[1]);
            map.put("pays", row[2]);
            map.put("totalSpent", row[3]);
            map.put("transactionCount", row[4]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Sales aggregated by month
     */
    private void getSalesByMonth(HttpServletResponse response) throws IOException {
        List<Object[]> data = transactionDAO.getSalesByMonth();
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("year", row[0]);
            map.put("month", row[1]);
            map.put("totalRevenue", row[2]);
            map.put("transactionCount", row[3]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Sales aggregated by day
     */
    private void getSalesByDay(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String daysParam = request.getParameter("days");
        int days = 30; // Default
        if (daysParam != null && !daysParam.isEmpty()) {
            days = Integer.parseInt(daysParam);
        }
        
        List<Object[]> data = transactionDAO.getSalesByDay(days);
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("totalRevenue", row[1]);
            map.put("transactionCount", row[2]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Client distribution by country
     */
    private void getClientsByCountry(HttpServletResponse response) throws IOException {
        List<Object[]> data = clientDAO.countByCountry();
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("pays", row[0]);
            map.put("clientCount", row[1]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * Client distribution by profession
     */
    private void getClientsByProfession(HttpServletResponse response) throws IOException {
        List<Object[]> data = clientDAO.countByProfession();
        
        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("profession", row[0]);
            map.put("clientCount", row[1]);
            return map;
        }).toList();
        
        ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(result);
        apiResponse.setCount(result.size());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * OPTIONS: Handle CORS preflight
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}