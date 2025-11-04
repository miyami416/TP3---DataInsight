<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - DataInsight</title>
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
            max-width: 1400px;
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
            font-size: 2em;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 1em;
            transition: background 0.3s;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
            margin-right: 10px;
        }
        .btn-danger {
            background: #dc3545;
            color: white;
            font-size: 0.85em;
            padding: 5px 12px;
        }
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
        }
        .stat-card h3 {
            font-size: 2.5em;
            margin-bottom: 5px;
        }
        .stat-card p {
            opacity: 0.9;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 0.95em;
        }
        thead {
            background: #667eea;
            color: white;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        tbody tr:hover {
            background: #f8f9fa;
        }
        .badge {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
        }
        .badge-success {
            background: #28a745;
            color: white;
        }
        .badge-warning {
            background: #ffc107;
            color: #000;
        }
        .badge-danger {
            background: #dc3545;
            color: white;
        }
        .badge-premium {
            background: #gold;
            color: #000;
        }
        .form-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-top: 30px;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"],
        input[type="number"],
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        .actions {
            display: flex;
            gap: 5px;
        }
        .filters {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .filters select {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>👥 ${pageTitle}</h1>
            <div>
                <a href="${pageContext.request.contextPath}/stats" class="btn btn-primary">📈 Analytics</a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">← Accueil</a>
            </div>
        </div>

        <!-- Alert Messages -->
        <c:if test="${param.success == 'created'}">
            <div class="alert alert-success">✓ Client créé avec succès!</div>
        </c:if>
        <c:if test="${param.success == 'deleted'}">
            <div class="alert alert-success">✓ Client supprimé avec succès!</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">✗ Erreur: ${param.error}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">✗ ${error}</div>
        </c:if>

        <!-- Statistics -->
                <div class="stats">
            <div class="stat-card">
                <h3>${totalCount}</h3>
                <p>Total Clients</p>
            </div>
            <div class="stat-card">
                <h3>${displayCount}</h3>
                <p>Affichés (${filterType != null ? filterType : 'Tous'})</p>
            </div>
        </div>

        <!-- Filters -->
        <div class="filters">
            <form method="get" action="clients">
                <select name="pays">
                    <option value="">-- Filtrer par pays --</option>
                    <option>France</option>
                    <option>Maroc</option>
                    <option>Canada</option>
                    <option>Espagne</option>
                </select>
                <select name="profession">
                    <option value="">-- Filtrer par profession --</option>
                    <option>Ingénieur</option>
                    <option>Médecin</option>
                    <option>Étudiant</option>
                    <option>Entrepreneur</option>
                </select>
                <button type="submit" class="btn btn-primary">Filtrer</button>
                <a href="clients" class="btn btn-secondary">Réinitialiser</a>
            </form>
        </div>

        <!-- Clients Table -->
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Pays</th>
                    <th>Âge</th>
                    <th>Profession</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${clients}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.nom}</td>
                        <td>${c.prenom}</td>
                        <td>${c.pays}</td>
                        <td>${c.age}</td>
                        <td>${c.profession}</td>
                        <td class="actions">
                            <form method="post" action="clients" style="display:inline;">
                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="id" value="${c.id}"/>
                                <button class="btn btn-danger" type="submit"
                                        onclick="return confirm('Supprimer ce client ?');">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Add Client Form -->
        <div class="form-section">
            <h2>➕ Ajouter un client</h2>
            <form method="post" action="clients">
                <input type="hidden" name="action" value="create">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nom</label>
                        <input type="text" name="nom" required/>
                    </div>
                    <div class="form-group">
                        <label>Prénom</label>
                        <input type="text" name="prenom"/>
                    </div>
                    <div class="form-group">
                        <label>Pays</label>
                        <select name="pays" required>
                            <option>France</option>
                            <option>Maroc</option>
                            <option>Canada</option>
                            <option>Espagne</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Âge</label>
                        <input type="number" name="age" min="18" required/>
                    </div>
                    <div class="form-group">
                        <label>Profession</label>
                        <input type="text" name="profession"/>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Créer</button>
            </form>
        </div>
    </div>
</body>
</html>
