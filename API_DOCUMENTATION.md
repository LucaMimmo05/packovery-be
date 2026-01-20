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

### 🔑 Richiesta Reset Password

**Endpoint:** `POST /api/auth/request-reset-password`

Avvia il processo di reset password inviando un codice OTP via email all'utente.

#### Richiesta

**Headers:**
```
Content-Type: application/json
Accept: application/json
```

**Body (JSON):**
```json
{
  "email": "string (obbligatorio, email dell'utente)"
}
```

#### Risposta di Successo

**Status Code:** `200 OK`

**Comportamento:**
- Se l'email esiste: Genera OTP di 6 cifre valido per 5 minuti e lo invia via email
- Se l'email non esiste: Risposta 404 per sicurezza

#### Risposte di Errore

**404 Not Found - Utente non trovato:**
```json
{
  "message": "Utente non trovato"
}
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/auth/request-reset-password', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  body: JSON.stringify({
    email: 'utente@example.com'
  })
})
.then(response => {
  if (response.ok) {
    console.log('OTP inviato via email');
  } else {
    console.log('Email non trovata');
  }
});
```

---

### 🔄 Reset Password

**Endpoint:** `POST /api/auth/reset-password`

Completa il processo di reset password utilizzando l'OTP ricevuto via email.

#### Richiesta

**Headers:**
```
Content-Type: application/json
Accept: application/json
```

**Body (JSON):**
```json
{
  "email": "string (obbligatorio, email dell'utente)",
  "otp": "string (obbligatorio, codice OTP di 6 cifre)",
  "newPassword": "string (obbligatorio, nuova password)"
}
```

#### Risposta di Successo

**Status Code:** `200 OK`

**Body (Testo):**
```
"Password reimpostata con successo."
```

#### Risposte di Errore

**400 Bad Request - Nessuna richiesta attiva:**
```
"Non è stata effettuata alcuna richiesta di reset password per questa email o la richiesta è scaduta."
```

**400 Bad Request - OTP non valido:**
```
"OTP non valido."
```

**404 Not Found - Utente non trovato:**
```
"Utente non trovato."
```

#### Controlli di Sicurezza

1. **Richiesta obbligatoria:** L'OTP può esistere solo se è stata fatta una richiesta tramite `/request-reset-password`
2. **Scadenza temporale:** L'OTP è valido solo per 5 minuti dalla generazione
3. **Uso singolo:** L'OTP viene invalidato dopo il primo utilizzo (successo o fallimento)
4. **Tipizzazione:** L'OTP è specifico per il reset password (non riutilizzabile per altri scopi)

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/auth/reset-password', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  body: JSON.stringify({
    email: 'utente@example.com',
    otp: '123456',
    newPassword: 'nuovaPasswordSicura123'
  })
})
.then(response => response.text())
.then(message => {
  console.log(message); // "Password reimpostata con successo."
});
```

**Flusso Completo Reset Password:**
```javascript
// 1. Richiesta OTP
async function requestPasswordReset(email) {
  const response = await fetch('/api/auth/request-reset-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email })
  });
  
  if (response.ok) {
    console.log('Controlla la tua email per il codice OTP');
    return true;
  } else {
    console.log('Email non trovata');
    return false;
  }
}

// 2. Reset password con OTP
async function resetPassword(email, otp, newPassword) {
  const response = await fetch('/api/auth/reset-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, otp, newPassword })
  });
  
  const message = await response.text();
  
  if (response.ok) {
    console.log('Password aggiornata con successo');
    return true;
  } else {
    console.log('Errore:', message);
    return false;
  }
}

// Utilizzo
requestPasswordReset('user@example.com')
  .then(() => {
    // L'utente inserisce l'OTP ricevuto via email
    const otpFromUser = prompt('Inserisci il codice ricevuto via email');
    return resetPassword('user@example.com', otpFromUser, 'nuovaPassword123');
  });
```

---

## Gestione Utenti

### 👤 Crea Utente

**Endpoint:** `POST /api/users`

Crea un nuovo utente nel sistema.

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
  "password": "string (obbligatorio, minimo 8 caratteri)",
  "role": "USER | CUSTOMER_CARE | RIDER (opzionale, default: USER)"
}
```

#### Risposta di Successo

**Status Code:** `201 Created`

**Body (JSON):**
```json
{
  "id": 1,
  "email": "user@example.com",
  "role": "USER",
  "accountStatus": "ACTIVE",
  "failedAttempts": 0,
  "blockedUntil": null
}
```

#### Risposte di Errore

**409 Conflict - Email già esistente:**
```json
{
  "message": "Un utente con questa email esiste già"
}
```

**400 Bad Request - Dati non validi:**
```json
{
  "message": "L'email è obbligatoria / La password deve essere di almeno 8 caratteri"
}
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/users', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'newuser@example.com',
    password: 'password123',
    role: 'USER'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

---

### 👥 Ottieni Tutti gli Utenti

**Endpoint:** `GET /api/users`

Recupera la lista di tutti gli utenti registrati.

#### Risposta di Successo

**Status Code:** `200 OK`

**Body (JSON):**
```json
[
  {
    "id": 1,
    "email": "user1@example.com",
    "role": "USER",
    "accountStatus": "ACTIVE",
    "failedAttempts": 0,
    "blockedUntil": null
  },
  {
    "id": 2,
    "email": "rider@example.com",
    "role": "RIDER",
    "accountStatus": "ACTIVE",
    "failedAttempts": 1,
    "blockedUntil": null
  }
]
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/users')
.then(response => response.json())
.then(users => console.log(users));
```

---

### 👤 Ottieni Utente per ID

**Endpoint:** `GET /api/users/{id}`

Recupera un utente specifico tramite il suo ID.

#### Parametri Path

- `id` (long): ID dell'utente

#### Risposta di Successo

**Status Code:** `200 OK`

**Body (JSON):**
```json
{
  "id": 1,
  "email": "user@example.com",
  "role": "USER",
  "accountStatus": "ACTIVE",
  "failedAttempts": 0,
  "blockedUntil": null
}
```

#### Risposte di Errore

**404 Not Found - Utente non trovato:**
```json
{
  "message": "Utente non trovato"
}
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/users/1')
.then(response => response.json())
.then(user => console.log(user));
```

---

### 🗑️ Elimina Utente

**Endpoint:** `DELETE /api/users/{id}`

Elimina un utente dal sistema.

#### Parametri Path

- `id` (long): ID dell'utente da eliminare

#### Risposta di Successo

**Status Code:** `204 No Content`

#### Risposte di Errore

**404 Not Found - Utente non trovato:**
```json
{
  "message": "Utente non trovato"
}
```

#### Esempio di Utilizzo

**JavaScript (fetch):**
```javascript
fetch('http://localhost:8080/api/users/1', {
  method: 'DELETE'
})
.then(response => {
  if (response.ok) {
    console.log('Utente eliminato con successo');
  }
});
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
- **TEMP_BLOCKED:** Account bloccato temporaneamente con scadenza automatica
  - Si sblocca automaticamente alla scadenza del periodo specificato
  - Il controllo avviene ad ogni tentativo di login o refresh token
- **PERM_BLOCKED:** Account bloccato permanentemente (richiede intervento amministratore)

### UserRole (Enum)

- **USER:** Utente normale
- **CUSTOMER_CARE:** Servizio clienti
- **RIDER:** Corriere/Rider

---

## Sicurezza

### Gestione Tentativi di Login

Il sistema implementa una protezione contro gli attacchi brute force con sblocco automatico:

1. **Contatore Tentativi:** Ogni fallimento di login incrementa il contatore `failedAttempts`
2. **Blocco Temporaneo Progressivo:** 
   - 3-4 tentativi falliti: blocco di 30 minuti
   - 5 tentativi falliti: blocco di 1 ora
3. **Blocco Permanente:** 6+ tentativi falliti richiedono intervento amministratore
4. **Reset Contatore:** Un login riuscito resetta il contatore a 0
5. **Sblocco Automatico:** Gli account temporaneamente bloccati si sbloccano automaticamente alla scadenza del periodo di blocco al prossimo tentativo di login o refresh token

**Comportamento di Sblocco Automatico:**
- Il sistema controlla la scadenza del blocco temporaneo ad ogni tentativo di login o refresh token
- Se il periodo di blocco è scaduto, l'account viene automaticamente riattivato
- Lo status cambia da `TEMP_BLOCKED` a `ACTIVE`
- Il contatore `failedAttempts` viene azzerato

### Token JWT

- I token JWT vengono generati per l'autenticazione
- Include informazioni sull'utente per l'autorizzazione
- Utilizzare l'header `Authorization: Bearer <token>` per le richieste autenticate

### Sicurezza Sistema OTP

Il sistema di reset password utilizza un meccanismo OTP (One-Time Password) sicuro:

#### Caratteristiche di Sicurezza

1. **Generazione Casuale**: OTP di 6 cifre generato con `SecureRandom`
2. **Hashing**: L'OTP viene hashato con BCrypt prima del salvataggio (mai salvato in chiaro)
3. **Tipizzazione**: Ogni OTP è associato a un tipo specifico (`PASSWORD_RESET`)
4. **Scadenza Temporale**: Validità limitata a 5 minuti dalla generazione
5. **Uso Singolo**: L'OTP viene rimosso dopo il primo utilizzo (successo o fallimento)

#### Controlli di Integrità

- **Richiesta Obbligatoria**: L'OTP esiste solo se generato tramite richiesta legittima
- **Email Verificata**: Solo utenti esistenti nel sistema possono richiedere reset
- **Validazione Doppia**: Controllo esistenza richiesta + verifica OTP specifico
- **Chiave Composita**: Utilizzo di `email:tipo` per evitare collisioni

#### Limitazioni Attuali

⚠️ **Nota**: Il sistema attuale utilizza memoria in-process (`ConcurrentHashMap`) per gli OTP. Con molti utenti simultanei questo può causare:
- Consumo crescente di memoria
- Perdita dati al riavvio dell'applicazione  
- Problemi con load balancing/multiple istanze

Per ambienti di produzione si raccomanda l'utilizzo di un database dedicato per la persistenza degli OTP.

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

**Ultima modifica:** 17 Gennaio 2026  
**Maintainer:** Team Packovery

**Modifiche recenti:**
- Implementazione sistema refresh token con doppia autenticazione
- Aggiornamento endpoint `/api/auth/login` per restituire refresh token
- Nuovo endpoint `/api/auth/refresh` per rinnovo token
- **Nuovi endpoint reset password**: `/api/auth/request-reset-password` e `/api/auth/reset-password`
- Sistema OTP sicuro per reset password con validazione a doppio livello
- Sblocco automatico account temporaneamente bloccati alla scadenza
- Esempi JavaScript completi per gestione autenticazione e reset password

---

