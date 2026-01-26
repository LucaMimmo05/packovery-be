# Packovery Backend API

Sistema backend per la gestione di ordini e spedizioni con autenticazione JWT, sviluppato con Quarkus Framework.

## 🚀 Tecnologie Utilizzate

- **Java 21** - Linguaggio di programmazione
- **Quarkus 3.17.4** - Framework reattivo supersonic
- **Maven** - Gestione dipendenze e build
- **JAX-RS** - API REST
- **PostgreSQL** - Database relazionale
- **Redis** - Cache per OTP e session management
- **JWT** - Autenticazione e autorizzazione
- **Hibernate ORM con Panache** - ORM semplificato
- **Mailer** - Servizio invio email
- **Bean Validation** - Validazione dati

## 📁 Struttura del Progetto

```
src/
├── main/
│   ├── java/com/packovery/
│   │   ├── auth/              # Sistema autenticazione e sicurezza
│   │   ├── user/              # Gestione utenti
│   │   ├── order/             # Gestione ordini
│   │   ├── vehicle/           # Gestione veicoli
│   │   ├── location/          # Gestione posizioni
│   │   ├── communication/     # Sistema comunicazioni
│   │   ├── alert/             # Sistema alert e notifiche
│   │   ├── logging/           # Sistema logging
│   │   ├── security/          # Configurazioni sicurezza
│   │   └── common/            # Classi condivise
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── enums/         # Enumerazioni
│   │       └── exceptions/    # Gestione eccezioni
│   └── resources/
│       ├── application.properties
│       ├── import.sql
│       ├── privateKey.pem
│       ├── publicKey.pem
│       └── templates/
└── test/
```

## 🔧 Prerequisiti

- **Java 21+**
- **Maven 3.8+**
- **PostgreSQL 13+**
- **Redis Server**

## ⚡ Installazione e Avvio

### 1. Clonare il Repository
```bash
git clone <repository-url>
cd packovery-be
```

### 2. Configurare Database PostgreSQL
```sql
-- Creare database
CREATE DATABASE packovery_db;

-- Creare utente
CREATE USER packovery_admin WITH PASSWORD 'packovery_pwd';

-- Assegnare permessi
GRANT ALL PRIVILEGES ON DATABASE packovery_db TO packovery_admin;
```

### 3. Configurare Redis
Assicurarsi che Redis sia in esecuzione sulla porta predefinita 6379.

### 4. Variabili d'Ambiente
Creare un file `.env` o configurare le seguenti variabili:

```properties
# Database PostgreSQL
QUARKUS_DATASOURCE_JDBC_URL_POSTGRE=jdbc:postgresql://localhost:5432/packovery_db
QUARKUS_DATASOURCE_USERNAME_POSTGRE=packovery_admin
QUARKUS_DATASOURCE_PASSWORD_POSTGRE=packovery_pwd

# Redis
QUARKUS_REDIS_HOSTS=redis://localhost:6379

# OTP Secret
OTP_SECRET=YOUR_SECRET_KEY_HERE

# Email Configuration
MAIL_USERNAME=your-email@domain.com
MAIL_PASSWORD=your-app-password
```

### 5. Compilare e Avviare

#### Modalità Sviluppo (con live reload)
```bash
./mvnw quarkus:dev
```

#### Build Produzione
```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

L'applicazione sarà disponibile su: `http://localhost:8080`

## 📚 API Endpoints

### 🔐 Autenticazione (`/api/auth`)
- `POST /login` - Login utente con email e password
- `POST /refresh` - Rinnova access token usando refresh token
- `POST /request-reset-password` - Richiede reset password via email
- `POST /reset-password` - Reimposta password con OTP
- `POST /new-password` - Imposta nuova password

### 👤 Utenti (`/api/users`)
- `GET /` - Lista tutti gli utenti (Admin)
- `POST /` - Crea nuovo utente (Admin)
- `GET /{id}` - Dettagli utente specifico

### 📦 Ordini (`/api/orders`)
- `GET /` - Lista ordini con filtri opzionali
- `POST /` - Crea nuovo ordine
- `GET /{id}` - Dettagli ordine specifico
- `PUT /{id}/status` - Aggiorna stato ordine

## 🔍 Esempi di Utilizzo

### Login
```javascript
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'user@example.com',
    password: 'password123',
    firstName: 'Mario',
    lastName: 'Rossi'
  })
});

const data = await response.json();
console.log('Access Token:', data.data.accessToken);
console.log('Refresh Token:', data.data.refreshToken);
```

### Refresh Token
```javascript
const response = await fetch('http://localhost:8080/api/auth/refresh', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    refreshToken: 'your-refresh-token-here'
  })
});
```

### Reset Password
```javascript
// 1. Richiesta reset
const resetRequest = await fetch('http://localhost:8080/api/auth/request-reset-password', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'user@example.com'
  })
});

// 2. Conferma con OTP
const resetConfirm = await fetch('http://localhost:8080/api/auth/reset-password', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'user@example.com',
    otp: '123456'
  })
});
```

### Ottenere Ordini con Filtri
```javascript
const response = await fetch('http://localhost:8080/api/orders?status=IN_TRANSIT&riderId=5&limit=10', {
  headers: {
    'Authorization': 'Bearer your-access-token'
  }
});
```

## 🗄️ Modello Dati

### Utente (User)
```java
{
  "id": "UUID",
  "email": "string",
  "firstName": "string", 
  "lastName": "string",
  "role": "ADMIN|RIDER|CUSTOMER",
  "isActive": "boolean",
  "isBlocked": "boolean",
  "blockedUntil": "LocalDateTime"
}
```

### Ordine (Order)
```java
{
  "id": "UUID",
  "customerEmail": "string",
  "riderEmail": "string",
  "status": "PENDING|CONFIRMED|IN_TRANSIT|DELIVERED|CANCELLED",
  "packageWeight": "S|M|L|XL",
  "packageSize": "S|M|L|XL", 
  "transportType": "BIKE|SCOOTER|CAR|VAN",
  "overweight": "boolean",
  "estimatedArrival": "LocalDateTime",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

## ⚠️ Gestione Errori

L'API restituisce errori standardizzati nel formato:

```json
{
  "success": false,
  "message": "Messaggio errore in italiano",
  "error": "CODICE_ERRORE",
  "timestamp": "2026-01-26T10:30:00"
}
```

### Codici di Stato HTTP
- `200` - Successo
- `400` - Richiesta non valida
- `401` - Non autorizzato
- `403` - Accesso negato
- `404` - Risorsa non trovata
- `422` - Errore di validazione
- `429` - Troppe richieste
- `500` - Errore interno del server

## 🧪 Testing

### Eseguire tutti i test
```bash
./mvnw test
```

### Test di integrazione
```bash
./mvnw verify
```

### Coverage report
```bash
./mvnw jacoco:report
```

## 🔒 Sicurezza

- **JWT Tokens**: Access token (15 min) + Refresh token (7 giorni)
- **Rate Limiting**: Protezione contro attacchi brute force
- **Password Hashing**: BCrypt con salt
- **OTP**: Codici temporanei per reset password (5 minuti)
- **Account Blocking**: Blocco temporaneo dopo tentativi falliti
- **Input Validation**: Validazione rigorosa di tutti gli input
- **HTTPS**: Obbligatorio in produzione

## 🚀 Deploy

### Build Nativa (GraalVM)
```bash
./mvnw package -Dnative
```

### Container Docker
```bash
docker build -f src/main/docker/Dockerfile.jvm -t packovery-backend .
docker run -p 8080:8080 packovery-backend
```

## 🔧 Configurazione Avanzata

### Database Connection Pool
```properties
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20
quarkus.datasource.jdbc.acquisition-timeout=10
```

### Redis Configuration
```properties
quarkus.redis.timeout=2s
quarkus.redis.max-pool-size=20
quarkus.redis.max-pool-waiting=30
```

### JWT Configuration
```properties
mp.jwt.verify.publickey.location=publicKey.pem
smallrye.jwt.sign.key.location=privateKey.pem
mp.jwt.verify.issuer=packovery
```

## 🐛 Debug e Troubleshooting

### Logs
```bash
# Visualizza logs in tempo reale
tail -f target/quarkus.log

# Debug SQL queries
quarkus.hibernate-orm.log.sql=true
```

### Health Check
```bash
curl http://localhost:8080/q/health
```

### Metrics
```bash
curl http://localhost:8080/q/metrics
```

## 📝 Licenza

Progetto sviluppato per scopi accademici - Gruppo 4

## 👥 Team

Sviluppato dal Team Frontend/Backend per il progetto Packovery

---

Per maggiori informazioni, consultare la documentazione API completa in `API_DOCUMENTATION.md`
