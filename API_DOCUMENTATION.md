# Documentazione API - Packovery Backend

## Informazioni Generali

**Versione:** 1.0-SNAPSHOT  
**Framework:** Quarkus 3.15.1  
**Java Version:** 21  
**Base URL:** `http://localhost:8080`

---

## Autenticazione

### 🔐 Login

**Endpoint:** `POST /api/auth/login`

Autentica un utente e restituisce un token JWT per l'accesso alle risorse protette.

#### Richiesta

**Headers:**
```
Content-Type: application/json
Accept: application/json
```

**Body (JSON):**
```json
{
  "email": "string (obbligatorio, formato email)",
  "password": "string (obbligatorio)"
}
```

#### Risposta di Successo

**Status Code:** `200 OK`

**Body (JSON):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login avvenuto con successo",
  "email": "utente@example.com"
}
```

#### Risposte di Errore

**404 Not Found - Utente non trovato:**
```json
{
  "message": "Utente non trovato."
}
```

**401 Unauthorized - Credenziali non valide:**
```json
{
  "message": "Credenziali non valide."
}
```

**Account Bloccato Permanentemente:**
```json
{
  "message": "Account permanente bloccato. Contatta l'amministratore.",
  "email": "utente@example.com",
  "permanent": true,
  "blockedUntil": null,
  "minutesLeft": null
}
```

**Account Bloccato Temporaneamente:**
```json
{
  "message": "Account temporaneamente bloccato.",
  "email": "utente@example.com",
  "permanent": false,
  "blockedUntil": "2026-01-16T15:30:00",
  "minutesLeft": 25
}
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  body: JSON.stringify({
    email: 'utente@example.com',
    password: 'password123'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## Modelli di Dati

### User (Utente)

```json
{
  "id": "long",
  "email": "string",
  "passwordHash": "string",
  "role": "USER | CUSTOMER_CARE | RIDER",
  "accountStatus": "ACTIVE | TEMP_BLOCKED | PERM_BLOCKED",
  "failedAttempts": "integer",
  "blockedUntil": "LocalDateTime | null"
}
```

### LoginRequest

```json
{
  "email": "string (obbligatorio, formato email)",
  "password": "string (obbligatorio)"
}
```

### LoginResponse

```json
{
  "token": "string",
  "message": "string",
  "email": "string"
}
```

### BlockedResponse

```json
{
  "message": "string",
  "email": "string",
  "permanent": "boolean",
  "blockedUntil": "LocalDateTime | null",
  "minutesLeft": "long | null"
}
```

---

## Stati Account

### UserStatus (Enum)

- **ACTIVE:** Account attivo e funzionante
- **TEMP_BLOCKED:** Account bloccato temporaneamente (con scadenza)
- **PERM_BLOCKED:** Account bloccato permanentemente

### UserRole (Enum)

- **USER:** Utente normale
- **CUSTOMER_CARE:** Servizio clienti
- **RIDER:** Corriere/Rider

---

## Sicurezza

### Gestione Tentativi di Login

Il sistema implementa una protezione contro gli attacchi brute force:

1. **Contatore Tentativi:** Ogni fallimento di login incrementa il contatore `failedAttempts`
2. **Blocco Temporaneo:** Dopo un certo numero di tentativi falliti, l'account viene bloccato temporaneamente
3. **Blocco Permanente:** Gli amministratori possono bloccare permanentemente un account
4. **Reset Contatore:** Un login riuscito resetta il contatore a 0

### Token JWT

- I token JWT vengono generati per l'autenticazione
- Include informazioni sull'utente per l'autorizzazione
- Utilizzare l'header `Authorization: Bearer <token>` per le richieste autenticate

---

## Configurazione

### Properties Application

Le configurazioni sono gestite nel file `src/main/resources/application.properties`.

### Chiavi Crittografiche

- **Chiave Privata:** `src/main/resources/privateKey.pem`
- **Chiave Pubblica:** `src/main/resources/publicKey.pem`

---

## Gestione Errori

### Codici di Errore Standard

| Status Code | Descrizione | Utilizzo |
|-------------|-------------|----------|
| 200 | OK | Richiesta completata con successo |
| 401 | Unauthorized | Credenziali non valide o token mancante |
| 404 | Not Found | Risorsa non trovata (es. utente non esistente) |
| 400 | Bad Request | Dati della richiesta non validi |
| 500 | Internal Server Error | Errore interno del server |

### Formato Errori

Gli errori seguono generalmente questo formato:

```json
{
  "message": "Descrizione dell'errore"
}
```

Per account bloccati, viene restituito un oggetto `BlockedResponse` con informazioni dettagliate.

---

## Note di Sviluppo

### Database

Il progetto utilizza Hibernate/JPA con Quarkus per la persistenza dei dati. Il file `import.sql` viene utilizzato per l'inizializzazione del database in ambiente di sviluppo.

### Testing

- Test di integrazione in `src/test/java/com/packovery/`
- Configurazione separata per ambiente di test

### Struttura Progetto

```
src/main/java/com/packovery/
├── auth/                 # Modulo autenticazione
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── AuthRepository.java
│   └── dto/              # Data Transfer Objects
├── common/               # Classi comuni
│   ├── enums/            # Enumerazioni
│   └── exceptions/       # Gestione eccezioni
├── security/             # Servizi di sicurezza
├── user/                 # Gestione utenti
└── [altri moduli]        # Altri moduli dell'applicazione
```

---

**Ultima modifica:** 16 Gennaio 2026  
**Maintainer:** Team Packovery

---

