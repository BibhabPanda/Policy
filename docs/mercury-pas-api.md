# Mercury PAS API Documentation (MVP)

## Conventions
- **Base URL**: http://localhost:8080
- **Auth**: JWT header `Authorization: Bearer <token>`
- **Content-Type**: `application/json`
- **Dates**: `YYYY-MM-DD`; DateTime: ISO-8601
- **Roles**: `ADMIN`, `AGENT`, `CUSTOMER`

---

## Auth Endpoints (`/api/auth`)

### POST `/api/auth/register` (Public)
Registers a user and returns a JWT.

Request body:
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "password": "Customer@123",
  "role": "CUSTOMER",
  "dob": "1995-06-15",
  "licenseNumber": "D1234567890"
}
```

Response:
```json
{ "accessToken": "<JWT>", "tokenType": "Bearer" }
```

cURL:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":"Jane",
    "lastName":"Doe",
    "email":"jane.doe@example.com",
    "password":"Customer@123",
    "role":"CUSTOMER",
    "dob":"1995-06-15",
    "licenseNumber":"D1234567890"
  }'
```

### POST `/api/auth/login` (Public)
```json
{ "email": "jane@example.com", "password": "Customer@123" }
```

cURL:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"jane@example.com","password":"Customer@123"}'
```

### POST `/api/auth/reset-password` (Public MVP)
```json
{ "email": "jane@example.com", "newPassword": "NewPass@123" }
```

cURL:
```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"email":"jane@example.com","newPassword":"NewPass@123"}'
```

---

## User Endpoints (`/api/users`)
All require `Authorization: Bearer <JWT>`.

### GET `/api/users/me`
Returns current user profile.

cURL:
```bash
curl http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <JWT>"
```

### PUT `/api/users/update-profile`
Update profile.

Request body:
```json
{
  "firstName":"Jane",
  "lastName":"Doe",
  "email":"jane@example.com",
  "dob":"1995-06-15",
  "licenseNumber":"D123"
}
```

cURL:
```bash
curl -X PUT http://localhost:8080/api/users/update-profile \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com","dob":"1995-06-15","licenseNumber":"D123"}'
```

### GET `/api/users/all` (ADMIN)
```bash
curl http://localhost:8080/api/users/all -H "Authorization: Bearer <JWT>"
```

### DELETE `/api/users/{id}` (ADMIN)
```bash
curl -X DELETE http://localhost:8080/api/users/42 -H "Authorization: Bearer <JWT>"
```

---

## Quote Endpoints (`/api/quotes`)

### POST `/api/quotes/generate`
Generates a quote and creates vehicle if VIN not found.

Request body:
```json
{
  "customerId": 3,
  "make": "Toyota",
  "model": "Camry",
  "year": 2012,
  "vin": "VIN123",
  "driverAge": 24
}
```

Business rules: base premium = 3000; +20% if driverAge < 25; +15% if vehicle age > 10 years.

cURL:
```bash
curl -X POST http://localhost:8080/api/quotes/generate \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"customerId":3,"make":"Toyota","model":"Camry","year":2012,"vin":"VIN123","driverAge":24}'
```

### POST `/api/quotes/save`
Save a quote explicitly.

Request body:
```json
{
  "customerId": 3,
  "vehicleId": 5,
  "coverageDetails": "Standard",
  "premiumAmount": 3450.00
}
```

cURL:
```bash
curl -X POST http://localhost:8080/api/quotes/save \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"customerId":3,"vehicleId":5,"coverageDetails":"Standard","premiumAmount":3450.00}'
```

### GET `/api/quotes/{id}`
```bash
curl http://localhost:8080/api/quotes/10 -H "Authorization: Bearer <JWT>"
```

### GET `/api/quotes/customer/{customerId}`
```bash
curl http://localhost:8080/api/quotes/customer/3 -H "Authorization: Bearer <JWT>"
```

### POST `/api/quotes/convert-to-policy/{quoteId}?agentId={agentId}` (AGENT/ADMIN)
Creates ACTIVE policy from quote and marks quote CONVERTED.

```bash
curl -X POST "http://localhost:8080/api/quotes/convert-to-policy/10?agentId=2" \
  -H "Authorization: Bearer <JWT>"
```

---

## Policy Endpoints (`/api/policies`)

### POST `/api/policies/create` (AGENT/ADMIN)
Create a policy from a quote.

Request body:
```json
{
  "quoteId": 10,
  "agentId": 2,
  "startDate": "2025-01-01",
  "endDate": "2026-01-01"
}
```

cURL:
```bash
curl -X POST http://localhost:8080/api/policies/create \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"quoteId":10,"agentId":2,"startDate":"2025-01-01","endDate":"2026-01-01"}'
```

### GET `/api/policies/{id}`
```bash
curl http://localhost:8080/api/policies/7 -H "Authorization: Bearer <JWT>"
```

### GET `/api/policies/customer/{customerId}`
```bash
curl http://localhost:8080/api/policies/customer/3 -H "Authorization: Bearer <JWT>"
```

### GET `/api/policies/agent/{agentId}` (AGENT/ADMIN)
```bash
curl http://localhost:8080/api/policies/agent/2 -H "Authorization: Bearer <JWT>"
```

### PUT `/api/policies/{id}` (AGENT/ADMIN)
Update policy dates (request body same as create).
```bash
curl -X PUT http://localhost:8080/api/policies/7 \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"quoteId":10,"agentId":2,"startDate":"2025-02-01","endDate":"2026-02-01"}'
```

### DELETE `/api/policies/{id}` (AGENT/ADMIN)
```bash
curl -X DELETE http://localhost:8080/api/policies/7 -H "Authorization: Bearer <JWT>"
```

---

## Claim Endpoints (`/api/claims`)

### POST `/api/claims/file`
Files a new claim. Defaults status `NEW`.

Request body:
```json
{
  "policyId": 7,
  "customerId": 3,
  "description": "Rear bumper damage"
}
```

cURL:
```bash
curl -X POST http://localhost:8080/api/claims/file \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"policyId":7,"customerId":3,"description":"Rear bumper damage"}'
```

### GET `/api/claims/{id}`
```bash
curl http://localhost:8080/api/claims/15 -H "Authorization: Bearer <JWT>"
```

### GET `/api/claims/policy/{policyId}`
```bash
curl http://localhost:8080/api/claims/policy/7 -H "Authorization: Bearer <JWT>"
```

### POST `/api/claims/upload-document/{claimId}` (AGENT/ADMIN)
Stores a document path reference.

Request body:
```json
{ "path": "C:/uploads/claim15/photo1.jpg" }
```

cURL:
```bash
curl -X POST http://localhost:8080/api/claims/upload-document/15 \
  -H "Authorization: Bearer <JWT>" -H "Content-Type: application/json" \
  -d '{"path":"C:/uploads/claim15/photo1.jpg"}'
```

---

## Workflow Diagram

```mermaid
flowchart TD
  A[Customer Registers / Logs in] -->|/api/auth/*| B[JWT Issued]
  B --> C[Customer or Agent Initiates Quote]
  C -->|/api/quotes/generate| D[Quote Generated (premium rules)]
  D -->|/api/quotes/save (optional)| E[Saved Quote]
  D -->|/api/quotes/convert-to-policy/{quoteId}?agentId=| F[Policy Created (ACTIVE)]
  E -->|convert later| F
  F --> G[Customer Policy Active]
  G -->|Incident| H[File Claim /api/claims/file]
  H --> I[Claim NEW]
  I -->|Upload docs /api/claims/upload-document/{id} (AGENT/ADMIN)| J[Docs Stored as Paths]
  I -->|Review Process (future)| K[UNDER_REVIEW/APPROVED/REJECTED]
```

---

## Headers Cheat Sheet
- `Authorization: Bearer <JWT>` (all authenticated requests)
- `Content-Type: application/json`

