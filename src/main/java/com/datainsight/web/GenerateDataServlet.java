package com.datainsight.web;

import com.datainsight.util.DataGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Triggers batch data generation via web interface
 */
@WebServlet("/generate")
public class GenerateDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Show form
        request.getRequestDispatcher("/pages/generate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Parse parameters
            int numClients = 1000; // Default
            int transactionsPerClient = 10; // Default

            String clientsParam = request.getParameter("numClients");
            String transactionsParam = request.getParameter("transactionsPerClient");

            if (clientsParam != null && !clientsParam.isEmpty()) {
                numClients = Integer.parseInt(clientsParam);
            }
            if (transactionsParam != null && !transactionsParam.isEmpty()) {
                transactionsPerClient = Integer.parseInt(transactionsParam);
            }

            // Validate
            if (numClients < 1 || numClients > 10000) {
                out.println("<h3>❌ Invalid number of clients (1-10,000 allowed)</h3>");
                return;
            }
            if (transactionsPerClient < 1 || transactionsPerClient > 100) {
                out.println("<h3>❌ Invalid transactions per client (1-100 allowed)</h3>");
                return;
            }

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Génération de Données</title>");
            out.println("<style>");
            out.println("body{font-family:monospace; padding:20px; background:#1e1e1e; color:#00ff00;}");
            out.println("pre{background:#000; padding:20px; border-radius:5px;}");
            out.println("a{color:#00aaff; text-decoration:none; font-weight:bold;}");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h2>🚀 Génération de données Big Data en cours...</h2><pre>");
            out.flush();

            // Generate clients
            long start = System.currentTimeMillis();
            out.println("📊 Génération de " + numClients + " clients...");
            out.flush();
            
            DataGenerator.generateClients(numClients);
            
            out.println("\n💳 Génération des transactions...");
            out.flush();
            
            // Generate transactions
            int inserted = DataGenerator.generateTransactions(transactionsPerClient);
            
            long duration = System.currentTimeMillis() - start;
            double rate = (inserted * 1000.0) / duration;

            out.println("\n✅ Génération terminée!");
            out.println("─────────────────────────────────────");
            out.println("Clients générés:         " + numClients);
            out.println("Transactions générées:   " + inserted);
            out.println("Durée totale:            " + duration + " ms");
            out.println("Débit:                   " + String.format("%.0f", rate) + " transactions/sec");
            out.println("─────────────────────────────────────");
            out.println("</pre>");
            
            out.println("<h3>📊 Accès rapide:</h3>");
            out.println("<ul>");
            out.println("<li><a href='" + request.getContextPath() + "/clients'>→ Voir les clients</a></li>");
            out.println("<li><a href='" + request.getContextPath() + "/transactions'>→ Voir les transactions</a></li>");
            out.println("<li><a href='" + request.getContextPath() + "/stats'>→ Voir les statistiques</a></li>");
            out.println("<li><a href='" + request.getContextPath() + "/'>→ Retour à l'accueil</a></li>");
            out.println("</ul>");
            
            out.println("</body></html>");

        } catch (NumberFormatException e) {
            out.println("<h3>❌ Erreur: Paramètres invalides</h3>");
        } catch (Exception e) {
            out.println("<h3>❌ Erreur: " + e.getMessage() + "</h3>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        } finally {
            out.close();
        }
    }
}