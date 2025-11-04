<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>${pageTitle} - DataInsight</title>
<style>
body{font-family:'Segoe UI',sans-serif;background:#f5f7fa;padding:20px;}
.container{max-width:1400px;margin:auto;background:#fff;border-radius:10px;padding:30px;box-shadow:0 2px 10px rgba(0,0,0,.1);}
.header{display:flex;justify-content:space-between;align-items:center;margin-bottom:30px;border-bottom:2px solid #667eea;padding-bottom:10px;}
h1{color:#667eea;}
table{width:100%;border-collapse:collapse;margin-top:20px;}
thead{background:#667eea;color:#fff;}
th,td{padding:10px;border-bottom:1px solid #ddd;}
tr:hover{background:#f8f9fa;}
.btn{padding:8px 16px;border:none;border-radius:5px;cursor:pointer;text-decoration:none;}
.btn-primary{background:#667eea;color:#fff;}
.btn-secondary{background:#6c757d;color:#fff;}
.btn-danger{background:#dc3545;color:#fff;}
.form-section{background:#f8f9fa;padding:20px;border-radius:10px;margin-top:30px;}
.form-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:15px;}
input,select{width:100%;padding:8px;border:1px solid #ddd;border-radius:5px;}
.stat-box{display:flex;gap:15px;flex-wrap:wrap;margin-bottom:20px;}
.card{flex:1;min-width:180px;background:linear-gradient(135deg,#667eea,#764ba2);color:#fff;padding:20px;border-radius:10px;text-align:center;}
</style>
</head>
<body>
<div class="container">
<div class="header">
  <h1>💳 ${pageTitle}</h1>
  <div>
    <a href="${pageContext.request.contextPath}/stats" class="btn btn-primary">📈 Analytics</a>
    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">← Accueil</a>
  </div>
</div>

<!-- Summary cards -->
<div class="stat-box">
  <div class="card"><h2>${totalCount}</h2><p>Transactions</p></div>
  <div class="card"><h2><fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0"/></h2><p>Total (€)</p></div>
  <div class="card"><h2><fmt:formatNumber value="${avgAmount}" type="number" maxFractionDigits="2"/></h2><p>Moyenne (€)</p></div>
</div>

<!-- Filters -->
<form method="get" action="transactions">
  <label>Catégorie :</label>
  <input type="text" name="categorie" placeholder="ex: Informatique"/>
  <label>Limite :</label>
  <input type="number" name="limit" value="${limit}" min="10" max="1000"/>
  <button type="submit" class="btn btn-primary">Filtrer</button>
  <a href="transactions" class="btn btn-secondary">Réinitialiser</a>
</form>

<!-- Table -->
<table>
<thead>
<tr>
<th>ID</th><th>Date</th><th>Client</th><th>Catégorie</th><th>Montant (€)</th><th>Mode</th><th>Actions</th>
</tr>
</thead>
<tbody>
<c:forEach var="t" items="${transactions}">
<tr>
<td>${t.id}</td>
<td>${t.date}</td>
<td>${t.client.nom}</td>
<td>${t.categorie}</td>
<td><fmt:formatNumber value="${t.montant}" type="number" maxFractionDigits="2"/></td>
<td>${t.modePaiement}</td>
<td>
<form method="post" action="transactions" style="display:inline;">
<input type="hidden" name="action" value="delete"/>
<input type="hidden" name="id" value="${t.id}"/>
<button class="btn btn-danger" onclick="return confirm('Supprimer ?')">Supprimer</button>
</form>
</td>
</tr>
</c:forEach>
</tbody>
</table>

<!-- Add form -->
<div class="form-section">
<h2>➕ Ajouter une Transaction</h2>
<form method="post" action="transactions">
<input type="hidden" name="action" value="create"/>
<div class="form-grid">
  <div><label>Client ID</label><input name="clientId" required></div>
  <div><label>Date</label><input type="date" name="date" required></div>
  <div><label>Montant (€)</label><input type="number" step="0.01" name="montant" required></div>
  <div><label>Catégorie</label><input name="categorie" required></div>
  <div><label>Mode Paiement</label><input name="modePaiement"></div>
  <div><label>Description</label><input name="description"></div>
</div>
<button class="btn btn-primary">Créer</button>
</form>
</div>
</div>
</body>
</html>
