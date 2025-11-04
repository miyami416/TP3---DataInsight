<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>${pageTitle} - DataInsight</title>
<style>
body{font-family:'Segoe UI',sans-serif;background:#eef1f6;padding:20px;}
.container{max-width:1500px;margin:auto;background:#fff;border-radius:10px;padding:30px;box-shadow:0 2px 10px rgba(0,0,0,.1);}
h1{color:#667eea;text-align:center;margin-bottom:30px;}
.section{margin-top:30px;}
table{width:100%;border-collapse:collapse;margin-top:10px;}
thead{background:#667eea;color:#fff;}
th,td{padding:10px;border-bottom:1px solid #ddd;text-align:left;}
.cardbox{display:flex;gap:15px;flex-wrap:wrap;justify-content:center;}
.card{flex:1;min-width:180px;background:linear-gradient(135deg,#667eea,#764ba2);color:#fff;padding:20px;border-radius:10px;text-align:center;}
a.btn{display:inline-block;margin-top:20px;background:#667eea;color:#fff;padding:10px 20px;border-radius:6px;text-decoration:none;}
</style>
</head>
<body>
<div class="container">
<h1>📈 Tableau de Bord Big Data</h1>

<div class="cardbox">
 <div class="card"><h2>${totalClients}</h2><p>Clients</p></div>
 <div class="card"><h2>${totalTransactions}</h2><p>Transactions</p></div>
 <div class="card"><h2><fmt:formatNumber value="${totalRevenue}" type="number"/></h2><p>Total (€)</p></div>
 <div class="card"><h2><fmt:formatNumber value="${avgTransaction}" type="number" maxFractionDigits="2"/></h2><p>Moyenne (€)</p></div>
</div>

<div class="section">
<h2>Revenus par Pays</h2>
<table><thead><tr><th>Pays</th><th>Total (€)</th><th>Moyenne (€)</th></tr></thead>
<tbody><c:forEach var="r" items="${revenueByCountry}">
<tr><td>${r[0]}</td><td><fmt:formatNumber value="${r[1]}" type="number"/></td><td><fmt:formatNumber value="${r[2]}" type="number"/></td></tr>
</c:forEach></tbody></table>
</div>

<div class="section">
<h2>Top Clients</h2>
<table>
  <thead>
    <tr><th>Client</th><th>Pays</th><th>Total (€)</th><th>Transactions</th></tr>
  </thead>
  <tbody>
    <c:forEach var="r" items="${topClients}">
      <tr>
        <td>${r[0]} ${r[1]}</td>
        <td>${r[2]}</td>
        <td><fmt:formatNumber value="${r[3]}" type="number" maxFractionDigits="2"/></td>
        <td>${r[4]}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>
</div>


<div class="section">
<h2>Revenus par Catégorie</h2>
<table><thead><tr><th>Catégorie</th><th>Total (€)</th></tr></thead>
<tbody><c:forEach var="r" items="${revenueByCategory}">
<tr><td>${r[0]}</td><td><fmt:formatNumber value="${r[1]}" type="number"/></td></tr>
</c:forEach></tbody></table>
</div>

<div class="section">
<h2>Ventes par Mois</h2>
<table><thead><tr><th>Mois</th><th>Total (€)</th></tr></thead>
<tbody><c:forEach var="r" items="${salesByMonth}">
<tr><td>${r[0]}</td><td><fmt:formatNumber value="${r[1]}" type="number"/></td></tr>
</c:forEach></tbody></table>
</div>

<div class="section">
<h2>Clients par Pays</h2>
<table><thead><tr><th>Pays</th><th>Nombre</th></tr></thead>
<tbody><c:forEach var="r" items="${clientsByCountry}">
<tr><td>${r[0]}</td><td>${r[1]}</td></tr>
</c:forEach></tbody></table>
</div>

<a href="${pageContext.request.contextPath}/" class="btn">← Retour Accueil</a>
</div>
</body>
</html>
