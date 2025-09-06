# USPG Local Gallery Lab - AWS S3 Extension

## Descripción del Proyecto

Extensión de la galería local para almacenar y servir imágenes desde Amazon S3, manteniendo el flujo completo: subir → listar → visualizar → eliminar.

## Configuración de AWS

### Región y Bucket
- **Región**: `us-east-1` (configurar en `AwsConfig.Region`)
- **Bucket**: `uspg-equipo-imagenes` (configurar en `AwsConfig.BUCKET`)
- **Block Public Access**: Habilitado (por defecto)
- **Acceso**: Pre-signed URLs únicamente

### Usuario IAM
Se creó un usuario IAM específico para la aplicación con la siguiente política mínima:

```json
{
  "Version": "2012-10-17",
  "Statement": [{
    "Sid": "AllowAppRW",
    "Effect": "Allow",
    "Action": [
      "s3:PutObject",
      "s3:GetObject", 
      "s3:ListBucket"
    ],
    "Resource": [
      "arn:aws:s3:::uspg-equipo-imagenes",
      "arn:aws:s3:::uspg-equipo-imagenes/*"
    ]
  }]
}
```

### Configuración Local de Credenciales

Las credenciales se configuran en `src/main/java/com/darwinruiz/uspglocalgallerylab/config/AwsConfig.java`:

```java
public class AwsConfig {
    public static final String AccesKey = "YOUR_ACCESS_KEY_HERE";
    public static final String AccesSecretKey = "YOUR_SECRET_KEY_HERE";
    public static final String Region = "us-east-1";
    public static final String BUCKET = "uspg-equipo-imagenes";
    public static final long PRESIGNED_MS = 5*60*1000; // 5 minutos
}
```

**⚠️ IMPORTANTE**: No subir credenciales reales al repositorio. Reemplazar con valores de ejemplo antes del commit.

## Decisiones Técnicas

### SDK de AWS
- **Versión**: AWS SDK for Java v1 (1.12.782)
- **Justificación**: Compatibilidad con Jakarta EE y simplicidad de configuración

### TTL de Pre-signed URLs
- **Duración**: 5 minutos (300,000 ms)
- **Uso**: URLs temporales para visualización segura de imágenes

### Manejo de Prefijos S3
- **Estructura**: `imagenes/yyyy/MM/dd/UUID_nombreArchivo.ext`
- **Ejemplo**: `imagenes/2024/03/15/a1b2c3d4-e5f6-7890-abcd-ef1234567890_foto.jpg`

### Validaciones
- **Extensiones**: .png, .jpg, .jpeg, .gif, .webp
- **MIME Type**: image/*
- **Tamaño máximo**: 3 MB por archivo
- **Múltiples archivos**: Soportado

## Funcionalidades Implementadas

### 1. Subida a S3
- Múltiples archivos simultáneos
- Prefijo automático por fecha
- Validación completa antes de subir
- UUID para evitar colisiones de nombres

### 2. Listado desde S3
- Filtrado por prefijo "imagenes/"
- Filtrado por extensiones válidas
- Paginación implementada
- Pre-signed URLs para visualización

### 3. Visualización
- Miniaturas en galería
- URLs temporales seguras
- Interfaz responsive con Bootstrap

### 4. Validaciones
- Tamaño de archivo
- Tipo MIME
- Extensiones permitidas
- Control de errores

## Instalación y Ejecución

### Prerrequisitos
- Java 21
- Maven 3.8+
- WildFly 31+ o servidor compatible con Jakarta EE 10
- Cuenta AWS con S3 configurado

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd UspgLocalGalleryLab1
   ```

2. **Configurar credenciales AWS**
   - Editar `src/main/java/com/darwinruiz/uspglocalgallerylab/config/AwsConfig.java`
   - Reemplazar valores de ejemplo con credenciales reales

3. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

4. **Desplegar en WildFly**
   ```bash
   mvn wildfly:deploy
   ```

5. **Acceder a la aplicación**
   - URL: `http://localhost:8080/UspgLocalGalleryLabTarea/`

## Estructura del Proyecto

```
src/main/java/com/darwinruiz/uspglocalgallerylab/
├── config/
│   └── AwsConfig.java              # Configuración AWS
├── controllers/
│   ├── UploadServlet.java          # Subida de archivos
│   ├── ListServlet.java            # Listado de imágenes
│   └── ViewServlet.java            # Visualización
├── services/
│   └── S3Storage.java              # Servicio S3
├── util/
│   └── ImageValidator.java         # Validaciones
└── repositories/
    └── LocalFileRepository.java    # Repositorio local (backup)
```

## Uso de la Aplicación

### Subir Imágenes
1. Navegar a `/upload.jsp`
2. Seleccionar "Subir a S3"
3. Elegir archivos (múltiples permitidos)
4. Hacer clic en "Subir a S3"

### Ver Galería
1. Navegar a `/list?target=s3`
2. Las imágenes se muestran con URLs temporales
3. Paginación automática (12 imágenes por página)

## Seguridad

- **Credenciales**: No expuestas en el código fuente
- **Bucket**: Acceso público bloqueado
- **URLs**: Pre-firmadas con expiración de 5 minutos
- **Validaciones**: Múltiples capas de seguridad

## Equipo de Desarrollo

- **[Nombre 1]** - [Carné] - Implementación de S3Storage y configuración AWS
- **[Nombre 2]** - [Carné] - Desarrollo de servlets y validaciones
- **[Nombre 3]** - [Carné] - Interfaz de usuario y testing

## Notas Adicionales

- El proyecto mantiene compatibilidad con almacenamiento local
- Las imágenes se organizan automáticamente por fecha
- Sistema de paginación para manejar grandes volúmenes
- Interfaz responsive compatible con dispositivos móviles
