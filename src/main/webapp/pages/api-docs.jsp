<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API Documentation - DataInsight</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #667eea;
        }
        h1 {
            color: #667eea;
            font-size: 2.5em;
        }
        h2 {
            color: #667eea;
            margin-top: 30px;
            margin-bottom: 15px;
            font-size: 1.8em;
        }
        h3 {
            color: #333;
            margin-top: 20px;
            margin-bottom: 10px;
            font-size: 1.3em;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 1em;
            background: #6c757d;
            color: white;
        }
        .endpoint {
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 20px;
            margin: 15px 0;
            border-radius: 5px;
        }
        .method {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 5px;
            font-weight: bold;
            margin-right: 10px;
            font-size: 0.9em;
        }
        .method-get {
            background: #28a745;
            color: white;
        }
        .method-post {
            background: #007bff;
            color: white;
        }
        .method-put {
            background: #ffc107;
            color: #000;
        }
        .method-delete {
            background: #dc3545;
            color: white;
        }
        .url {
            font-family: 'Courier New', monospace;
            background: #e9ecef;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
            overflow-x: auto;
        }
        .description {
            color: #555;
            margin: 10px 0;
        }
        .params {
            margin: 10px 0;
        }
        .param-name {
            font-weight: bold;
            color: #667eea;
        }
        .example {
            background: #263238;
            color: #aed581;
            padding: 15px;
            border-radius: 5px;
            margin: 10px 0;
            overflow-x: auto;
            font-family: 'Courier New', monospace;
            font-size: 0.9em;
        }
        .response {
            background: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .toc {
            background: #e3f2fd;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
        }
        .toc ul {
            list-style: none;
        }
        .toc li {
            margin: 8px 0;
        }
        .toc a {
            color: #667eea;
            text-decoration: none;
            font-weight: bold;
        }
        .toc a:hover {
            text-decoration: underline;
        }
        code {
            background: #e9ecef;
            padding: 2px 6px;
            border-radius: 3px;
            font-family: 'Courier New', monospace;
        }
        .test-section {
            background: #d4edda;
            border-left: 4px solid #28a745;
            padding: 20px;
            margin: 30px 0;
            border-radius: 5px;
        }
        .test-section h3 {
            color: #155724;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📡 API REST Documentation</h1>
            <a href="${pageContext.request.contextPath}/" class="btn">← Accueil</a>
        </div>

        <!-- Table of Contents -->
        <div class="toc">
            <h3>📑 Table des matières</h3>
            <ul>
                <li><a href="#introduction">Introduction</a></li>
                <li><a href="#base-url">Base URL</a></li>
                <li><a href="#authentication">Authentication</a></li>
                <li><a href="#clients-api">Clients API</a></li>
                <li><a href="#transactions-api">Transactions API</a></li>
                <li><a href="#stats-api">Statistics API</a></li>
                <li><a href="#testing">Testing with cURL</a></li>
                <li><a href="#error-handling">Error Handling</a></li>
            </ul>
        </div>

        <!-- Introduction -->
        <h2 id="introduction">Introduction</h2>
        <p>L'API REST DataInsight permet d'accéder programmatiquement aux données clients, transactions et statistiques. Toutes les réponses sont au format JSON.</p>

        <!-- Base URL -->
        <h2 id="base-url">Base URL</h2>
        <div class="url">
            <%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api
        </div>

        <!-- Authentication -->
        <h2 id="authentication">Authentication</h2>
        <p>Actuellement, l'API est ouverte (pas d'authentification requise). En production, vous devriez implémenter OAuth2 ou JWT.</p>

        <!-- Response Format -->
        <h3>Format de réponse standard</h3>
        <div class="example">
{
    "success": true,
    "message": "Success",
    "data": { ... },
    "count": 10,
    "timestamp": "2025-11-03T14:30:00"
}
        </div>

        <!-- Clients API -->
        <h2 id="clients-api">🔵 Clients API</h2>

        <!-- GET All Clients -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Récupérer tous les clients</h3>
            <div class="url">/api/clients</div>
            <div class="description">Retourne la liste de tous les clients.</div>
            
            <div class="params">
                <strong>Query Parameters (optionnels):</strong>
                <ul>
                    <li><span class="param-name">pays</span> - Filtrer par pays</li>
                    <li><span class="param-name">profession</span> - Filtrer par profession</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients"
            </div>

            <strong>Exemple avec filtre:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients?pays=Maroc"
            </div>

            <div class="response">
                <strong>Réponse (200 OK):</strong>
                <div class="example">
{
    "success": true,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "nom": "Alami",
            "prenom": "Mohammed",
            "pays": "Maroc",
            "age": 35,
            "profession": "Ingénieur",
            "email": "mohammed.alami@datainsight.com",
            "dateInscription": "2025-11-01T10:30:00",
            "statut": "actif",
            "nombreTransactions": 15
        }
    ],
    "count": 1,
    "timestamp": "2025-11-03T14:30:00"
}
                </div>
            </div>
        </div>

        <!-- GET Single Client -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Récupérer un client par ID</h3>
            <div class="url">/api/clients/{id}</div>
            <div class="description">Retourne les détails d'un client spécifique.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients/1"
            </div>
        </div>

        <!-- POST Create Client -->
        <div class="endpoint">
            <h3><span class="method method-post">POST</span> Créer un nouveau client</h3>
            <div class="url">/api/clients</div>
            <div class="description">Crée un nouveau client.</div>

            <div class="params">
                <strong>Body (JSON):</strong>
                <ul>
                    <li><span class="param-name">nom</span> - Nom (requis)</li>
                    <li><span class="param-name">prenom</span> - Prénom (optionnel)</li>
                    <li><span class="param-name">pays</span> - Pays (requis)</li>
                    <li><span class="param-name">age</span> - Âge (requis)</li>
                    <li><span class="param-name">profession</span> - Profession (optionnel)</li>
                    <li><span class="param-name">statut</span> - Statut (optionnel: actif, inactif, premium)</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X POST "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Benali",
    "prenom": "Fatima",
    "pays": "Maroc",
    "age": 28,
    "profession": "Médecin",
    "statut": "actif"
  }'
            </div>

            <div class="response">
                <strong>Réponse (201 Created):</strong>
                <div class="example">
{
    "success": true,
    "message": "Client created",
    "data": {
        "id": 1001,
        "nom": "Benali",
        "prenom": "Fatima",
        ...
    },
    "timestamp": "2025-11-03T14:30:00"
}
                </div>
            </div>
        </div>

        <!-- PUT Update Client -->
        <div class="endpoint">
            <h3><span class="method method-put">PUT</span> Mettre à jour un client</h3>
            <div class="url">/api/clients/{id}</div>
            <div class="description">Met à jour les informations d'un client existant.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X PUT "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients/1" \
  -H "Content-Type: application/json" \
  -d '{
    "statut": "premium",
    "age": 36
  }'
            </div>
        </div>

        <!-- DELETE Client -->
        <div class="endpoint">
            <h3><span class="method method-delete">DELETE</span> Supprimer un client</h3>
            <div class="url">/api/clients/{id}</div>
            <div class="description">Supprime un client et toutes ses transactions.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X DELETE "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients/1"
            </div>
        </div>

        <!-- Transactions API -->
        <h2 id="transactions-api">💳 Transactions API</h2>

        <!-- GET All Transactions -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Récupérer les transactions</h3>
            <div class="url">/api/transactions</div>
            <div class="description">Retourne la liste des transactions récentes.</div>

            <div class="params">
                <strong>Query Parameters (optionnels):</strong>
                <ul>
                    <li><span class="param-name">limit</span> - Nombre de résultats (défaut: 100)</li>
                    <li><span class="param-name">categorie</span> - Filtrer par catégorie</li>
                    <li><span class="param-name">clientId</span> - Filtrer par ID client</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/transactions?limit=50"
            </div>

            <div class="response">
                <strong>Réponse (200 OK):</strong>
                <div class="example">
{
    "success": true,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "date": "2025-11-01",
            "montant": 1500.50,
            "categorie": "Informatique",
            "description": "Achat ordinateur",
            "modePaiement": "carte",
            "referenceTransaction": "TXN-1730723000-1234",
            "clientId": 1,
            "clientNom": "Alami",
            "clientPrenom": "Mohammed",
            "clientPays": "Maroc"
        }
    ],
    "count": 1,
    "timestamp": "2025-11-03T14:30:00"
}
                </div>
            </div>
        </div>

        <!-- POST Create Transaction -->
        <div class="endpoint">
            <h3><span class="method method-post">POST</span> Créer une transaction</h3>
            <div class="url">/api/transactions</div>
            <div class="description">Enregistre une nouvelle transaction.</div>

            <div class="params">
                <strong>Body (JSON):</strong>
                <ul>
                    <li><span class="param-name">clientId</span> - ID du client (requis)</li>
                    <li><span class="param-name">date</span> - Date (format: YYYY-MM-DD, requis)</li>
                    <li><span class="param-name">montant</span> - Montant (requis)</li>
                    <li><span class="param-name">categorie</span> - Catégorie (requis)</li>
                    <li><span class="param-name">description</span> - Description (optionnel)</li>
                    <li><span class="param-name">modePaiement</span> - Mode de paiement (optionnel)</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X POST "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/transactions" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "date": "2025-11-03",
    "montant": 250.00,
    "categorie": "Alimentation",
    "description": "Courses hebdomadaires",
    "modePaiement": "carte"
  }'
            </div>
        </div>

        <!-- DELETE Transaction -->
        <div class="endpoint">
            <h3><span class="method method-delete">DELETE</span> Supprimer une transaction</h3>
            <div class="url">/api/transactions/{id}</div>
            
            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X DELETE "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/transactions/1"
            </div>
        </div>

        <!-- Statistics API -->
        <h2 id="stats-api">📊 Statistics API</h2>

        <!-- Overview -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Vue d'ensemble</h3>
            <div class="url">/api/stats/overview</div>
            <div class="description">Statistiques globales (total clients, transactions, revenu).</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/overview"
            </div>

            <div class="response">
                <strong>Réponse (200 OK):</strong>
                <div class="example">
{
    "success": true,
    "message": "Success",
    "data": {
        "totalClients": 1000,
        "totalTransactions": 10000,
        "totalRevenue": 2500000.50,
        "averageTransaction": 250.00
    },
    "timestamp": "2025-11-03T14:30:00"
}
                </div>
            </div>
        </div>

        <!-- Revenue by Country -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Revenu par pays</h3>
            <div class="url">/api/stats/revenue-by-country</div>
            <div class="description">Agrégation du revenu par pays.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/revenue-by-country"
            </div>

            <div class="response">
                <strong>Réponse (200 OK):</strong>
                <div class="example">
{
    "success": true,
    "data": [
        {
            "pays": "Maroc",
            "totalRevenue": 850000.50,
            "averageAmount": 280.50,
            "transactionCount": 3000
        },
        {
            "pays": "France",
            "totalRevenue": 720000.00,
            "averageAmount": 310.20,
            "transactionCount": 2300
        }
    ],
    "count": 2
}
                </div>
            </div>
        </div>

        <!-- Revenue by Category -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Revenu par catégorie</h3>
            <div class="url">/api/stats/revenue-by-category</div>
            <div class="description">Agrégation du revenu par catégorie de transaction.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/revenue-by-category"
            </div>
        </div>

        <!-- Top Clients -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Top clients</h3>
            <div class="url">/api/stats/top-clients</div>
            <div class="description">Clients avec les dépenses les plus élevées.</div>

            <div class="params">
                <strong>Query Parameters (optionnels):</strong>
                <ul>
                    <li><span class="param-name">limit</span> - Nombre de clients (défaut: 10)</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/top-clients?limit=5"
            </div>

            <div class="response">
                <strong>Réponse (200 OK):</strong>
                <div class="example">
{
    "success": true,
    "data": [
        {
            "nom": "Alami",
            "prenom": "Mohammed",
            "pays": "Maroc",
            "totalSpent": 15000.00,
            "transactionCount": 45
        }
    ],
    "count": 5
}
                </div>
            </div>
        </div>

        <!-- Sales by Month -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Ventes par mois</h3>
            <div class="url">/api/stats/sales-by-month</div>
            <div class="description">Agrégation mensuelle des ventes.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/sales-by-month"
            </div>
        </div>

        <!-- Sales by Day -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Ventes par jour</h3>
            <div class="url">/api/stats/sales-by-day</div>
            <div class="description">Agrégation quotidienne des ventes.</div>

            <div class="params">
                <strong>Query Parameters (optionnels):</strong>
                <ul>
                    <li><span class="param-name">days</span> - Nombre de jours (défaut: 30)</li>
                </ul>
            </div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/sales-by-day?days=7"
            </div>
        </div>

        <!-- Clients by Country -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Clients par pays</h3>
            <div class="url">/api/stats/clients-by-country</div>
            <div class="description">Distribution des clients par pays.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/clients-by-country"
            </div>
        </div>

        <!-- Clients by Profession -->
        <div class="endpoint">
            <h3><span class="method method-get">GET</span> Clients par profession</h3>
            <div class="url">/api/stats/clients-by-profession</div>
            <div class="description">Distribution des clients par profession.</div>

            <strong>Exemple cURL:</strong>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/clients-by-profession"
            </div>
        </div>

        <!-- Testing Section -->
        <div class="test-section" id="testing">
            <h2>🧪 Testing with cURL</h2>
            <p>Pour tester rapidement l'API, utilisez les commandes cURL suivantes :</p>

            <h3>1. Récupérer tous les clients</h3>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients" | json_pp
            </div>

            <h3>2. Créer un nouveau client</h3>
            <div class="example">
curl -X POST "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "API",
    "pays": "France",
    "age": 30,
    "profession": "Développeur"
  }' | json_pp
            </div>

            <h3>3. Obtenir les statistiques</h3>
            <div class="example">
curl -X GET "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/stats/overview" | json_pp
            </div>

            <h3>4. Créer une transaction</h3>
            <div class="example">
curl -X POST "<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/transactions" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "date": "2025-11-03",
    "montant": 150.00,
    "categorie": "Informatique",
    "description": "Test transaction",
    "modePaiement": "carte"
  }' | json_pp
            </div>

            <h3>Testing avec Postman</h3>
            <p>Vous pouvez également utiliser <strong>Postman</strong> ou <strong>Insomnia</strong> pour tester l'API de manière interactive :</p>
            <ol>
                <li>Ouvrez Postman</li>
                <li>Créez une nouvelle collection "DataInsight API"</li>
                <li>Ajoutez les endpoints listés ci-dessus</li>
                <li>Configurez les headers : <code>Content-Type: application/json</code></li>
                <li>Testez chaque endpoint</li>
            </ol>
        </div>

        <!-- Error Handling -->
        <h2 id="error-handling">⚠️ Error Handling</h2>
        <p>L'API retourne des codes HTTP standard et des messages d'erreur structurés :</p>

        <h3>Codes de statut HTTP</h3>
        <ul>
            <li><code>200 OK</code> - Requête réussie</li>
            <li><code>201 Created</code> - Ressource créée avec succès</li>
            <li><code>400 Bad Request</code> - Paramètres invalides ou manquants</li>
            <li><code>404 Not Found</code> - Ressource introuvable</li>
            <li><code>500 Internal Server Error</code> - Erreur serveur</li>
        </ul>

        <h3>Format d'erreur</h3>
        <div class="example">
{
    "success": false,
    "message": "Client not found",
    "data": null,
    "timestamp": "2025-11-03T14:30:00"
}
        </div>

        <h3>Exemples d'erreurs courantes</h3>

        <div class="endpoint">
            <strong>400 Bad Request - Champs manquants</strong>
            <div class="example">
{
    "success": false,
    "message": "Missing required fields: nom, pays, age",
    "data": null
}
            </div>
        </div>

        <div class="endpoint">
            <strong>404 Not Found - Ressource introuvable</strong>
            <div class="example">
{
    "success": false,
    "message": "Client not found",
    "data": null
}
            </div>
        </div>

        <div class="endpoint">
            <strong>500 Internal Server Error</strong>
            <div class="example">
{
    "success": false,
    "message": "Server error: Database connection failed",
    "data": null
}
            </div>
        </div>

        <!-- Best Practices -->
        <h2>✅ Best Practices</h2>
        <ul>
            <li><strong>Pagination</strong> - Utilisez le paramètre <code>limit</code> pour contrôler le nombre de résultats</li>
            <li><strong>Filtrage</strong> - Utilisez les query parameters pour filtrer les données</li>
            <li><strong>Validation</strong> - Vérifiez toujours les champs requis avant d'envoyer une requête</li>
            <li><strong>Error Handling</strong> - Gérez correctement les erreurs dans votre code client</li>
            <li><strong>CORS</strong> - L'API supporte CORS, vous pouvez l'appeler depuis n'importe quel domaine</li>
            <li><strong>Performance</strong> - Les endpoints de statistiques peuvent être lents avec de grandes quantités de données</li>
        </ul>

        <!-- Rate Limiting -->
        <h2>⏱️ Rate Limiting</h2>
        <p>Actuellement, il n'y a pas de limite de taux (rate limiting). En production, vous devriez implémenter :</p>
        <ul>
            <li>Limitation à 100 requêtes par minute par IP</li>
            <li>Limitation à 1000 requêtes par heure par utilisateur authentifié</li>
            <li>Headers de réponse : <code>X-RateLimit-Limit</code>, <code>X-RateLimit-Remaining</code></li>
        </ul>

        <!-- Versioning -->
        <h2>🔄 Versioning</h2>
        <p>Version actuelle : <strong>v1.0</strong></p>
        <p>Les futures versions seront accessibles via : <code>/api/v2/clients</code>, etc.</p>

        <!-- Contact -->
        <h2>📧 Support & Contact</h2>
        <p>Pour toute question ou problème avec l'API :</p>
        <ul>
            <li>Email : <a href="mailto:api@datainsight.com">api@datainsight.com</a></li>
            <li>GitHub : <a href="#">github.com/datainsight/api</a></li>
            <li>Documentation : <a href="#">docs.datainsight.com</a></li>
        </ul>

        <div style="margin-top: 50px; text-align: center; color: #999;">
            <p>© 2025 DataInsight Manager - REST API Documentation</p>
            <p>Jakarta EE 10 • Hibernate 6 • MySQL 8 • Jackson</p>
        </div>
    </div>
</body>
</html>