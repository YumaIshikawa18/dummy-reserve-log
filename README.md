# dummy-reserve-log

dummy-reserve-log is a reservation management system for camping reservations. It provides RESTful endpoints for managing reservations and related entities such as camps, customers, and rentals.

## Requirements

- **Docker & Docker Compose**

- **Java 21**

- **PostgreSQL, Kafka, and Kafka UI**

## How to Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YumaIshikawa18/dummy-reserve-log.git
   cd dummy-reserve-log
    ```
   
2. **Build the project and start the containers:** 
   ```bash
   ./mvnw clean package && docker compose down --volumes && docker compose up -d --build
   ```
   
3. **API Request Example**

   **Get Reservations**

   Request:
   ```bash
   curl -X GET http://localhost:8080/reservations
   ```

   Sample Response:
   ```json
     {
        "id": "d43c91ec-c03b-4c4d-b5f4-4647aac4c33c",
        "checkInDate": "1999-02-13",
        "checkOutDate": "1999-02-15",
        "status": "RESERVED",
        "numberOfTents": 1,
        "camp": {
        "id": "eb4dfcbb-c243-4091-b2bd-030f1307f92c",
        "price": 4500
        },
        "customer": {
        "id": "2f71f623-f619-420a-bc6b-9cbbd63eb9f9",
        "name": "Duffy",
        "address": "duffy@disney.com"
        },
        "rentals": [
        {
        "id": "758d3dba-6db0-441b-8302-2be9fe723bdf",
        "name": "テント",
        "unitPrice": 1000,
        "quantity": 3,
        "subtotal": 6000
        }
        ],
        "total": 15000,
        "givenPoint": 150
     }
   ```
   