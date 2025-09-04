<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Subir imágenes (Local)</title>
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
        <div id="navMain" class="collapse navbar-collapse show">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link active" href="<%=request.getContextPath()%>/upload.jsp"><i
                        class="bi bi-upload"></i> Subir</a></li>
                <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/list"><i
                        class="bi bi-image"></i> Imágenes</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="card shadow-sm">
        <div class="card-header bg-white"><strong>Subir a almacenamiento local</strong></div>
        <div class="card-body">
            <form method="post" action="<%=request.getContextPath()%>/upload" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label">Imágenes</label>
                    <input class="form-control" type="file" name="file" accept="image/*" multiple required>
                    <div class="form-text">
                        Formatos: PNG/JPG/JPEG/GIF/WEBP · Tamaño recomendado ≤ 3&nbsp;MB por archivo.
                    </div>
                </div>
                <button class="btn btn-success" type="submit">
                    <i class="bi bi-cloud-arrow-up"></i> Subir
                </button>
                <a class="btn btn-outline-secondary" href="<%=request.getContextPath()%>/list">
                    <i class="bi bi-image"></i> Ver galería
                </a>
            </form>
        </div>
    </div>
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white"><strong>Subir a S3</strong></div>
        <div class="card-body">
            <form method="post" action="<%=request.getContextPath()%>/upload" enctype="multipart/form-data">
                <input type="hidden" name="target" value="s3">
                <div class="mb-3">
                    <label class="form-label">Imágenes</label>
                    <input class="form-control" type="file" name="file" accept="image/*" multiple required>
                    <div class="form-text">
                        Formatos: PNG/JPG/JPEG/GIF/WEBP · Tamaño recomendado ≤ 5 MB por archivo.
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Prefijo opcional en S3</label>
                    <input type="text" class="form-control" name="prefix" placeholder="ej: mis_imagenes/">
                </div>
                <button class="btn btn-success" type="submit"><i class="bi bi-cloud-arrow-up"></i> Subir a S3</button>
                <a class="btn btn-outline-secondary" href="<%=request.getContextPath()%>/list">
                    <i class="bi bi-image"></i> Ver galería
                </a>
            </form>
        </div>
    </div>
    <%
        String up = request.getParameter("uploaded");
        String bad = request.getParameter("rejected");
        if (up != null || bad != null) {
    %>
    <div class="alert alert-info mt-3">
        Subidos: <strong><%= up == null ? "0" : up %>
    </strong> ·
        Rechazados: <strong><%= bad == null ? "0" : bad %>
    </strong>
    </div>
    <% } %>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>