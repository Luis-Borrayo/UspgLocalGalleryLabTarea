<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Galería (Local)</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .thumb {
            object-fit: cover;
            height: 180px
        }
    </style>
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
                <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/upload.jsp"><i
                        class="bi bi-upload"></i> Subir</a></li>
                <li class="nav-item"><a class="nav-link active" href="#"><i class="bi bi-image"></i> Imágenes</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">

    <%
        // Atributos esperados desde ListServlet
        java.util.List<String> images = (java.util.List<String>) request.getAttribute("images");
        String source = (String) request.getAttribute("source");
        Integer pageNum = (Integer) request.getAttribute("page");   // <-- usar pageNum para no chocar con 'page' implícito
        Integer pageSize = (Integer) request.getAttribute("size");
        Integer total = (Integer) request.getAttribute("total");

        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 12;
        if (total == null) total = (images == null ? 0 : images.size());

        int totalPages = (int) Math.ceil(total / (double) pageSize);
        if (totalPages == 0) totalPages = 1;

        int prev = Math.max(1, pageNum - 1);
        int next = Math.min(totalPages, pageNum + 1);
    %>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="m-0">
            Imágenes locales <small class="text-muted fs-6">(página <%=pageNum%>/<%=totalPages%>)</small>
        </h3>
        <div class="btn-group">
            <a class="btn btn-sm btn-outline-secondary"
               href="<%=request.getContextPath()%>/list?target=<%=source%>&page=<%=prev%>&size=<%=pageSize%>">« Anterior</a>
            <a class="btn btn-sm btn-outline-secondary"
               href="<%=request.getContextPath()%>/list?target=<%=source%>&page=<%=next%>&size=<%=pageSize%>">Siguiente »</a>
        </div>
    </div>

    <% if (images == null) { %>
    <div class="alert alert-info">
        Abre esta página desde <code><%=request.getContextPath()%>/list</code> para cargar el contenido.
    </div>
    <% } else if (images.isEmpty()) { %>
    <div class="alert alert-warning">No hay imágenes aún. Sube algunas desde la pestaña <strong>Subir</strong>.</div>
    <% } else { %>
    <div class="row g-3">
        <% for (String path : images) { %>
        <div class="col-6 col-md-3">
            <div class="card shadow-sm">
                <% if ("local".equalsIgnoreCase(source)) { %>
                <img class="thumb card-img-top"
                     src="<%=request.getContextPath()%>/view?path=<%=java.net.URLEncoder.encode(path, "UTF-8")%>"
                     alt="Imagen">
                <% } else { %>
                <img class="thumb card-img-top" src="<%=path%>" alt="Imagen">
                <% } %>
                <div class="card-body small text-truncate"><%=path%>
                </div>

                <div class="card-footer bg-white">
                    <% if ("local".equalsIgnoreCase(source)) { %>
                    <form method="post" action="<%=request.getContextPath()%>/delete" class="d-inline">
                        <input type="hidden" name="path" value="<%=path%>">
                        <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i> Eliminar</button>
                    </form>
                    <% } else { %>
                    <a class="btn btn-sm btn-outline-secondary" href="<%=path%>" target="_blank">
                        <i class="bi bi-box-arrow-up-right"></i> Abrir en S3
                    </a>
                    <% } %>

                </div>

            </div>
        </div>
        <% } %>
    </div>
    <% } %>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>