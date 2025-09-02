# Health Clinic Backend Service

Aplikasi backend service untuk mendigitalisasi operasional klinik kesehatan berskala menengah ke bawah.

## Deskripsi

Aplikasi ini dibuat untuk mendigitalisasi operasional pada klinik kesehatan berskala menengah ke bawah. Aplikasi ini akan mengatasi masalah yang biasanya terjadi pada operasional manual seperti:

- Pencatatan secara manual yang mengakibatkan kertas rawan hilang atau rusak
- Penumpukan dokumen
- Masalah antrian
- Kesulitan dalam pencarian data pasien
- Ketidakefisienan dalam manajemen inventori obat

## Tech Stack

- **Backend Framework**: Spring Boot
- **Database**: MySQL
- **Java Version**: 24
- **Build Tool**: Maven/Gradle

## API Endpoints

### Users Management
**Base URL**: `/api/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new user |
| PUT | `/{id}` | Update user by ID |
| DELETE | `/{id}` | Delete user by ID |
| GET | `/{id}` | Get user by ID |

### Patients Management
**Base URL**: `/api/patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new patient |
| PUT | `/{id}` | Update patient by ID |
| DELETE | `/{id}` | Delete patient by ID |
| GET | `/{id}` | Get patient by ID |

### Doctors Management
**Base URL**: `/api/doctors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new doctor |
| PUT | `/{id}` | Update doctor by ID |
| DELETE | `/{id}` | Delete doctor by ID |
| GET | `/{id}` | Get doctor by ID |

### Medicines Management
**Base URL**: `/api/medicines`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new medicine |
| PUT | `/{id}` | Update medicine by ID |
| DELETE | `/{id}` | Delete medicine by ID |

### Medical Histories
**Base URL**: `/api/medical-histories`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new medical history |
| PUT | `/{id}` | Update medical history by ID |
| DELETE | `/{id}` | Delete medical history by ID |

### Transactions
**Base URL**: `/api/transactions`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new transaction |