<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>USPG | Galería Local (Lab)</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/">USPG Lab</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div id="navMain" class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/upload.jsp"><i
                        class="bi bi-upload"></i> Subir</a></li>
                <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/list"><i
                        class="bi bi-image"></i> Imágenes</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-5">
    <div class="p-4 bg-white rounded shadow-sm">
        <h1 class="h4 mb-3">Laboratorio: Galería local</h1>
        <p class="mb-2">Objetivo: subir múltiples imágenes al servidor, validarlas y mostrarlas en una galería
            paginada.</p>
        <ul class="mb-4">
            <li>Validaciones en el servicio (tamaño, MIME, extensión).</li>
            <li>Subcarpetas por fecha y nombres normalizados (si tu versión del lab lo pide).</li>
            <li>Listar imágenes y renderizarlas desde el servidor.</li>
        </ul>
        <a class="btn btn-primary" href="<%=request.getContextPath()%>/upload.jsp"><i class="bi bi-upload"></i> Ir a
            Subir</a>
        <a class="btn btn-outline-secondary" href="<%=request.getContextPath()%>/list"><i class="bi bi-image"></i> Ver
            galería</a>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>