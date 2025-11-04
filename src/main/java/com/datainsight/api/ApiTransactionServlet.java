package com.datainsight.api;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dao.TransactionDAO;
import com.datainsight.dto.ApiResponse;
import com.datainsight.dto.TransactionDTO;
import com.datainsight.model.Client;
import com.datainsight.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for Transaction operations
 * Base URL: /api/transactions
 */
@WebServlet("/api/transactions/*")
public class ApiTransactionServlet extends HttpServlet {

    private TransactionDAO transactionDAO;
    private ClientDAO clientDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAO();
        clientDAO = new ClientDAO();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("✓ ApiTransactionServlet initialized");
    }

    /**
     * GET: Retrieve transactions
     * /api/transactions - recent transactions
     * /api/transactions/{id} - single transaction
     * /api/transactions?categorie={cat} - filter by category
     * /api/transactions?clientId={id} - filter by client
     * /api/transactions?limit={n} - limit results
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            String pathInfo = request.getPathInfo();
            String categorie = request.getParameter("categorie");
            String clientIdParam = request.getParameter("clientId");
            String limitParam = request.getParameter("limit");

            int limit = 100; // Default
            if (limitParam != null && !limitParam.isEmpty()) {
                limit = Integer.parseInt(limitParam);
            }

            if (pathInfo != null && pathInfo.length() > 1) {
                // GET single transaction by ID
                Long id = Long.parseLong(pathInfo.substring(1));
                Transaction transaction = transactionDAO.findById(id);
                
                if (transaction != null) {
                    TransactionDTO dto = TransactionDTO.fromEntity(transaction);
                    ApiResponse<TransactionDTO> apiResponse = ApiResponse.success(dto);
                    objectMapper.writeValue(response.getWriter(), apiResponse);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ApiResponse<String> apiResponse = ApiResponse.error("Transaction not found");
                    objectMapper.writeValue(response.getWriter(), apiResponse);
                }
            } else if (clientIdParam != null && !clientIdParam.isEmpty()) {
                // Filter by client
                Long clientId = Long.parseLong(clientIdParam);
                List<Transaction> transactions = transactionDAO.findByClient(clientId);
                List<TransactionDTO> dtos = transactions.stream()
                        .map(TransactionDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<TransactionDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
                
            } else if (categorie != null && !categorie.isEmpty()) {
                // Filter by category
                List<Transaction> transactions = transactionDAO.findByCategorie(categorie, limit);
                List<TransactionDTO> dtos = transactions.stream()
                        .map(TransactionDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<TransactionDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
                
            } else {
                // GET recent transactions
                List<Transaction> transactions = transactionDAO.findRecent(limit);
                List<TransactionDTO> dtos = transactions.stream()
                        .map(TransactionDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<TransactionDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<String> apiResponse = ApiResponse.error("Invalid parameter format");
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Server error: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * POST: Create new transaction
     * Body: JSON TransactionDTO
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // CORS
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            // Read JSON body
            BufferedReader reader = request.getReader();
            TransactionDTO dto = objectMapper.readValue(reader, TransactionDTO.class);

            // Validate
            if (dto.getClientId() == null ||
                dto.getDate() == null ||
                dto.getMontant() == null ||
                dto.getCategorie() == null || dto.getCategorie().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<String> apiResponse = ApiResponse.error(
                    "Missing required fields: clientId, date, montant, categorie");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Find client
            Client client = clientDAO.findById(dto.getClientId());
            if (client == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<String> apiResponse = ApiResponse.error("Client not found");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Create entity
            Transaction transaction = new Transaction(
                dto.getDate(),
                dto.getMontant(),
                dto.getCategorie(),
                dto.getDescription(),
                client
            );
            
            if (dto.getModePaiement() != null) {
                transaction.setModePaiement(dto.getModePaiement());
            }

            // Save
            Transaction saved = transactionDAO.create(transaction);
            TransactionDTO resultDto = TransactionDTO.fromEntity(saved);

            response.setStatus(HttpServletResponse.SC_CREATED);
            ApiResponse<TransactionDTO> apiResponse = ApiResponse.success("Transaction created", resultDto);
            objectMapper.writeValue(response.getWriter(), apiResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Error creating transaction: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * DELETE: Remove transaction
     * URL: /api/transactions/{id}
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // CORS
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<String> apiResponse = ApiResponse.error("Transaction ID required");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            Long id = Long.parseLong(pathInfo.substring(1));
            
            // Check if exists
            Transaction transaction = transactionDAO.findById(id);
            if (transaction == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ApiResponse<String> apiResponse = ApiResponse.error("Transaction not found");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Delete
            transactionDAO.delete(id);

            ApiResponse<String> apiResponse = ApiResponse.success("Transaction deleted", null);
            objectMapper.writeValue(response.getWriter(), apiResponse);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<String> apiResponse = ApiResponse.error("Invalid ID format");
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Error deleting transaction: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * OPTIONS: Handle CORS preflight
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}