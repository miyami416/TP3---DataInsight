<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DataInsight Manager - Accueil</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            padding: 50px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            max-width: 900px;
            width: 100%;
        }
        h1 {
            color: #667eea;
            font-size: 3.5em;
            margin-bottom: 10px;
            text-align: center;
        }
        .subtitle {
            text-align: center;
            color: #666;
            font-size: 1.3em;
            margin-bottom: 40px;
        }
        .menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .menu-item {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px 20px;
            border-radius: 15px;
            text-decoration: none;
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        .menu-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.3);
        }
        .menu-item h2 {
            font-size: 1.5em;
            margin-bottom: 10px;
        }
        .menu-item p {
            font-size: 0.9em;
            opacity: 0.9;
        }
        .icon {
            font-size: 3em;
            margin-bottom: 15px;
        }
        .footer {
            margin-top: 40px;
            text-align: center;
            color: #999;
            font-size: 0.9em;
        }
        .description {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            border-left: 4px solid #667eea;
        }
        .description h3 {
            color: #667eea;
            margin-bottom: 10px;
        }
        .description ul {
            margin-left: 20px;
            color: #555;
        }
        .description li {
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📊 DataInsight Manager</h1>
        <p class="subtitle">Plateforme d'analyse Big Data pour clients et transactions</p>

        <div class="description">
            <h3>🎯 Fonctionnalités Big Data</h3>
            <ul>
                <li>Gestion de milliers de clients et transactions</li>
                <li>Insertion batch haute performance (HikariCP + Hibernate)</li>
                <li>Requêtes analytiques avancées (JPQL/SQL)</li>
                <li>Tableaux de bord et statistiques en temps réel</li>
                <li>Agrégation par pays, catégorie, période</li>
            </ul>
        </div>

        <div class="menu">
            <a href="<%= request.getContextPath() %>/clients" class="menu-item">
                <div class="icon">👥</div>
                <h2>Clients</h2>
                <p>Gérer la base clients</p>
            </a>

            <a href="<%= request.getContextPath() %>/transactions" class="menu-item">
                <div class="icon">💳</div>
                <h2>Transactions</h2>
                <p>Consulter les achats</p>
            </a>

            <a href="<%= request.getContextPath() %>/stats" class="menu-item">
                <div class="icon">📈</div>
                <h2>Analytics</h2>
                <p>Statistiques avancées</p>
            </a>

            <a href="<%= request.getContextPath() %>/generate" class="menu-item">
                <div class="icon">⚡</div>
                <h2>Générer Data</h2>
                <p>Batch insert (Big Data)</p>
            </a>
            
            <a href="<%= request.getContextPath() %>/pages/api-docs.jsp" class="menu-item">
                <div class="icon">🔌</div>
                <h2>REST API</h2>
                <p>Documentation API</p>
            </a>
        </div>

        <div class="footer">
            <p><strong>Stack Technique:</strong> Jakarta EE 10 • Hibernate 6 • MySQL 8 • HikariCP</p>
            <p>© 2025 DataInsight Project - Big Data Enterprise Application</p>
        </div>
    </div>
</body>
</html>