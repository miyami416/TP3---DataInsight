package com.datainsight.api;

import com.datainsight.dao.ClientDAO;
import com.datainsight.dto.ApiResponse;
import com.datainsight.dto.ClientDTO;
import com.datainsight.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for Client operations
 * Base URL: /api/clients
 */
@WebServlet("/api/clients/*")
public class ApiClientServlet extends HttpServlet {

    private ClientDAO clientDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("✓ ApiClientServlet initialized");
    }

    /**
     * GET: Retrieve clients
     * /api/clients - all clients
     * /api/clients/{id} - single client
     * /api/clients?pays={country} - filter by country
     * /api/clients?profession={profession} - filter by profession
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Enable CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            String pathInfo = request.getPathInfo();
            String pays = request.getParameter("pays");
            String profession = request.getParameter("profession");

            if (pathInfo != null && pathInfo.length() > 1) {
                // GET single client by ID
                Long id = Long.parseLong(pathInfo.substring(1));
                Client client = clientDAO.findById(id);
                
                if (client != null) {
                    ClientDTO dto = ClientDTO.fromEntity(client);
                    ApiResponse<ClientDTO> apiResponse = ApiResponse.success(dto);
                    objectMapper.writeValue(response.getWriter(), apiResponse);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ApiResponse<String> apiResponse = ApiResponse.error("Client not found");
                    objectMapper.writeValue(response.getWriter(), apiResponse);
                }
            } else if (pays != null && !pays.isEmpty()) {
                // Filter by country
                List<Client> clients = clientDAO.findByPays(pays);
                List<ClientDTO> dtos = clients.stream()
                        .map(ClientDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<ClientDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
                
            } else if (profession != null && !profession.isEmpty()) {
                // Filter by profession
                List<Client> clients = clientDAO.findByProfession(profession);
                List<ClientDTO> dtos = clients.stream()
                        .map(ClientDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<ClientDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
                
            } else {
                // GET all clients
                List<Client> clients = clientDAO.findAll();
                List<ClientDTO> dtos = clients.stream()
                        .map(ClientDTO::fromEntity)
                        .collect(Collectors.toList());
                
                ApiResponse<List<ClientDTO>> apiResponse = ApiResponse.success(dtos);
                apiResponse.setCount(dtos.size());
                objectMapper.writeValue(response.getWriter(), apiResponse);
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<String> apiResponse = ApiResponse.error("Invalid ID format");
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Server error: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * POST: Create new client
     * Body: JSON ClientDTO
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
            ClientDTO dto = objectMapper.readValue(reader, ClientDTO.class);

            // Validate
            if (dto.getNom() == null || dto.getNom().trim().isEmpty() ||
                dto.getPays() == null || dto.getPays().trim().isEmpty() ||
                dto.getAge() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<String> apiResponse = ApiResponse.error("Missing required fields: nom, pays, age");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Create entity
            Client client = new Client(
                dto.getNom(), 
                dto.getPrenom() != null ? dto.getPrenom() : "",
                dto.getPays(), 
                dto.getAge(), 
                dto.getProfession()
            );
            
            if (dto.getStatut() != null) {
                client.setStatut(dto.getStatut());
            }

            // Save
            Client saved = clientDAO.create(client);
            ClientDTO resultDto = ClientDTO.fromEntity(saved);

            response.setStatus(HttpServletResponse.SC_CREATED);
            ApiResponse<ClientDTO> apiResponse = ApiResponse.success("Client created", resultDto);
            objectMapper.writeValue(response.getWriter(), apiResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Error creating client: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * PUT: Update existing client
     * URL: /api/clients/{id}
     * Body: JSON ClientDTO
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // CORS
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<String> apiResponse = ApiResponse.error("Client ID required");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            Long id = Long.parseLong(pathInfo.substring(1));
            
            // Read JSON body
            BufferedReader reader = request.getReader();
            ClientDTO dto = objectMapper.readValue(reader, ClientDTO.class);

            // Find existing client
            Client client = clientDAO.findById(id);
            if (client == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ApiResponse<String> apiResponse = ApiResponse.error("Client not found");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Update fields
            if (dto.getNom() != null) client.setNom(dto.getNom());
            if (dto.getPrenom() != null) client.setPrenom(dto.getPrenom());
            if (dto.getPays() != null) client.setPays(dto.getPays());
            if (dto.getAge() != null) client.setAge(dto.getAge());
            if (dto.getProfession() != null) client.setProfession(dto.getProfession());
            if (dto.getStatut() != null) client.setStatut(dto.getStatut());

            // Save
            Client updated = clientDAO.update(client);
            ClientDTO resultDto = ClientDTO.fromEntity(updated);

            ApiResponse<ClientDTO> apiResponse = ApiResponse.success("Client updated", resultDto);
            objectMapper.writeValue(response.getWriter(), apiResponse);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<String> apiResponse = ApiResponse.error("Invalid ID format");
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Error updating client: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), apiResponse);
        }
    }

    /**
     * DELETE: Remove client
     * URL: /api/clients/{id}
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
                ApiResponse<String> apiResponse = ApiResponse.error("Client ID required");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            Long id = Long.parseLong(pathInfo.substring(1));
            
            // Check if exists
            Client client = clientDAO.findById(id);
            if (client == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ApiResponse<String> apiResponse = ApiResponse.error("Client not found");
                objectMapper.writeValue(response.getWriter(), apiResponse);
                return;
            }

            // Delete
            clientDAO.delete(id);

            ApiResponse<String> apiResponse = ApiResponse.success("Client deleted", null);
            objectMapper.writeValue(response.getWriter(), apiResponse);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<String> apiResponse = ApiResponse.error("Invalid ID format");
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<String> apiResponse = ApiResponse.error("Error deleting client: " + e.getMessage());
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