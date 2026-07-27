# API - Seguimiento de Gastos

REST API para seguimiento de gastos personales con Spring Boot 4.1.0, JWT y PostgreSQL.

## Requisitos

- Java 21
- Maven 3.9+
- PostgreSQL (o Supabase)

## Variables de entorno

| Variable | Requerido | Default | Descripción |
|---|---|---|---|
| `DB_URL` | No | `jdbc:postgresql://...` | URL de conexión a BD |
| `DB_USERNAME` | No | `postgres` | Usuario de BD |
| `DB_PASSWORD` | **Sí** | — | Contraseña de BD |
| `JWT_SECRET_KEY` | **Sí** | — | Clave secreta HMAC-SHA (Base64) |
| `JWT_EXPIRATION` | No | `86400000` | Tiempo de expiración en ms |
| `JWT_ISSUER` | No | `API` | Emisor del token |

### Configurar en IntelliJ

**Run → Edit Configurations → Environment variables:**
```
DB_PASSWORD=Haniel0600@;JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970337336763979244226452948404D635166546A576E5A7234753778214125442A47
```

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Endpoints

### Auth (`/api/auth`)

| Método | Ruta | Cuerpo | Descripción |
|---|---|---|---|
| POST | `/register` | `{ "email", "nombre", "passwordHash" }` | Registrar usuario |
| POST | `/login` | `{ "email", "password" }` | Iniciar sesión |

### Categorías (`/api/categoria`) — requiere token

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Listar todas |
| GET | `/{id}` | Obtener por ID |
| POST | `/` | Crear |
| PUT | `/{id}` | Actualizar |
| DELETE | `/{id}` | Eliminar |

### Movimientos (`/api/movimiento`) — requiere token

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Listar todos |
| GET | `/{id}` | Obtener por ID |
| POST | `/` | Crear |
| PUT | `/{id}` | Actualizar |
| DELETE | `/{id}` | Eliminar |

## Uso

```bash
# Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@mail.com","nombre":"Test","passwordHash":"123456"}'

# Login (devuelve token)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@mail.com","password":"123456"}'

# Endpoint protegido (usar el token del login)
curl http://localhost:8080/api/categoria \
  -H "Authorization: Bearer eyJhbGci..."
```
