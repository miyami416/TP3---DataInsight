<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Génération de Données - DataInsight</title>
<style>
body{font-family:'Segoe UI',sans-serif;background:linear-gradient(135deg,#667eea,#764ba2);color:#fff;text-align:center;padding-top:80px;}
.container{background:#fff;color:#333;border-radius:10px;padding:40px;margin:auto;max-width:600px;box-shadow:0 10px 30px rgba(0,0,0,.3);}
h1{color:#667eea;margin-bottom:20px;}
input{width:100%;padding:10px;margin:10px 0;border-radius:5px;border:1px solid #ccc;}
button{background:#667eea;color:#fff;border:none;padding:10px 25px;border-radius:6px;cursor:pointer;font-size:1em;}
button:hover{background:#5568d3;}
a{display:inline-block;margin-top:20px;color:#667eea;text-decoration:none;}
</style>
</head>
<body>
<div class="container">
<h1>⚡ Génération de Données</h1>
<p>Simulez des volumes Big Data pour tester les performances de votre application.</p>
<form method="post" action="generate">
<label>Nombre de clients :</label>
<input type="number" name="numClients" min="10" max="10000" value="1000" required/>
<label>Transactions par client :</label>
<input type="number" name="transactionsPerClient" min="1" max="100" value="10" required/>
<button type="submit">Lancer la génération</button>
</form>
<a href="${pageContext.request.contextPath}/">← Retour à l'accueil</a>
</div>
</body>
</html>
