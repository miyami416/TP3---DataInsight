package com.datainsight.web;

import com.datainsight.dao.ClientDAO;
import com.datainsight.model.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Handles CRUD operations for Client entities
 */
@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
        System.out.println("✓ ClientServlet initialized");
    }

    /**
     * GET: List all clients or filter by country/profession
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String pays = request.getParameter("pays");
        String profession = request.getParameter("profession");
        
        try {
            List<Client> clients;
            
            if (pays != null && !pays.isEmpty()) {
                clients = clientDAO.findByPays(pays);
                request.setAttribute("filterType", "Pays: " + pays);
            } else if (profession != null && !profession.isEmpty()) {
                clients = clientDAO.findByProfession(profession);
                request.setAttribute("filterType", "Profession: " + profession);
            } else {
                clients = clientDAO.findAll();
            }
            
            long totalCount = clientDAO.count();

            request.setAttribute("clients", clients);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("displayCount", clients.size());
            request.setAttribute("pageTitle", "Gestion des Clients");

            request.getRequestDispatcher("/pages/clients.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/pages/clients.jsp").forward(request, response);
        }
    }

    /**
     * POST: Create new client (PRG pattern)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createClient(request, response);
        } else if ("delete".equals(action)) {
            deleteClient(request, response);
        }
    }

    /**
     * Create new client (POST-REDIRECT-GET pattern)
     */
    private void createClient(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String pays = request.getParameter("pays");
            String ageStr = request.getParameter("age");
            String profession = request.getParameter("profession");

            // Validation
            if (nom == null || nom.trim().isEmpty() || 
                pays == null || pays.trim().isEmpty() ||
                ageStr == null || ageStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + 
                    "/clients?error=Champs obligatoires manquants");
                return;
            }

            Integer age = Integer.parseInt(ageStr);
            
            Client client = new Client(nom, prenom, pays, age, profession);
            clientDAO.create(client);

            // PRG: Redirect to avoid duplicate submission
            response.sendRedirect(request.getContextPath() + "/clients?success=created");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + 
                "/clients?error=Âge invalide");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + 
                "/clients?error=" + e.getMessage());
        }
    }

    /**
     * Delete client
     */
    private void deleteClient(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            clientDAO.delete(id);

            response.sendRedirect(request.getContextPath() + "/clients?success=deleted");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + 
                "/clients?error=" + e.getMessage());
        }
    }
}