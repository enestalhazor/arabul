✨ Arabul — Backend
Spring Boot • PostgreSQL • JWT • Docker • Static Image Server
<div align="center"> <img src="https://img.shields.io/badge/Java-17-red?style=for-the-badge"> <img src="https://img.shields.io/badge/Spring%20Boot-Backend-success?style=for-the-badge"> <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge"> <img src="https://img.shields.io/badge/Docker-Containerization-2496ED?style=for-the-badge"> <img src="https://img.shields.io/badge/JWT-Authentication-yellow?style=for-the-badge"> </div> <br>

It includes integrated user authentication, product catalog, cart operations, order processing, and static product image hosting.

🌟 Key Features
🔐 Authentication & Security

- JWT authentication

- Encrypted passwords

- Unique email & phone validation

🛍 Product Management

- 80+ preloaded real products

- Photos served via Node.js static server

- Categorized product listings

🛒 Shopping Cart

- Add to cart

- Remove from cart

- Update quantity

- Retrieve cart details

📦 Orders

- Place new orders

- Store credit card + shipping info

- Auto timestamps

- Order → Products mapping system

🗄 Database Included

- Full PostgreSQL schema

- Pre-loaded content

- Automatic Docker initialization

🧬 Tech Stack
Layer	Technology
Backend	Java 17 • Spring Boot • JPA/Hibernate
Database	PostgreSQL 14
Auth	JWT
Static Server	Node.js + serve
Container	Docker (Ubuntu base)
🛠 Project Structure
/
├── Dockerfile
├── start.sh
├── dump.sql
├── target/arabul-0.0.1-SNAPSHOT.jar
└── productphotos/

🐳 Run With Docker (Recommended)
1️⃣ Build Image
docker build -t arabul .

2️⃣ Run Container
docker run -p 8080:8080 -p 8090:8090 --name arabul-container arabul

Running Services
Port	Service
8080	Spring Boot API
8090	Static Product Image Server
🗄 Static Image Hosting

Product images are served from:

http://localhost:8090/<filename>


Example:

http://localhost:8090/12.jpg


These match the photo field in the database.

🧱 Database Schema
· Users

id PK
name
email UNIQUE
phone UNIQUE
password
address
profile_picture

· Products

id PK
name
description
photo
price
category

· Cart

id PK
user_id
product_id
count

· Orders

id PK
user_id
credit_card_number
verification_code
expiration_date
first_name
last_name
order_date (auto)

· Order Products

id PK
order_id FK
product_id FK
count

🔧 Environment Variables
Variable	Description
DB_NAME	PostgreSQL database name
DB_USER	PostgreSQL username
DUMP_FILE	SQL dump file location

Dockerfile sets these automatically.

🚀 Run Locally (Without Docker)
1. PostgreSQL
CREATE DATABASE arabul;

Import data:

psql -d arabul -f dump.sql

2. Static Image Server
serve productphotos -p 8090

3. Run Spring Boot
mvn spring-boot:run

📡 API Overview
Authentication
- POST /auth/register
- POST /auth/login

Products
- GET /products
- GET /products/{id}

Cart
- GET /cart/{userId}
- POST /cart/add
- DELETE /cart/{id}

Orders
POST /orders
GET /orders/user/{userId}

👑 Docker Deployment Workflow

Your Dockerfile automatically:

✔ Installs PostgreSQL, Java 17, Maven, Node.js
✔ Loads your dump.sql
✔ Starts DB service
✔ Starts product photo server
✔ Runs Spring Boot JAR