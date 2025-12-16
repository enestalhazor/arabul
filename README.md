# ARABUL BACKEND

# Project Tech Stack

## Java Version

### Java 17
- Main programming language of the project.
- Long-Term Support (LTS) release.
- Required by modern Spring Boot versions.
- Provides better performance, security updates, and modern language features.

---

## Frameworks & Core Technologies

### Spring Boot
- Backend application framework.
- Simplifies configuration and dependency management.
- Provides embedded Tomcat server.
- Used for:
  - REST API development
  - Dependency Injection
  - Application lifecycle management

### Spring Web (Spring MVC)
- Builds RESTful web services.
- Provides annotations like:
  - `@RestController`
  - `@RequestMapping`
  - `@GetMapping`, `@PostMapping`, `@DeleteMapping`, `@PutMapping`
- Handles HTTP routing and responses.

### Spring JDBC (`JdbcTemplate`)
- Used for database communication.
- Executes raw SQL queries.
- Maps SQL results to Java objects.
- Chosen for:
  - Full SQL control
  - Predictable performance
  - Easier debugging

### Jakarta Validation (Bean Validation)
- Used to validate HTTP request inputs.
- Common annotations:
  - `@NotNull`
  - `@NotBlank`
  - `@NotEmpty`
  - `@Size`
  - `@Min`, `@Max`
- Prevents invalid data from reaching business logic.

## Database

### PostgreSQL
- Relational database management system.
- Stores application data:
  - Users
  - Products
  - Cart
  - Orders
- Ensures data consistency and transaction safety.

## Build & Dependency Management

### Maven
- Build automation tool.
- Manages dependencies.
- Handles project compilation and packaging into JAR files.

## API & Security

### REST API Architecture
- Stateless communication.
- Uses standard HTTP methods:
  - `GET`
  - `POST`
  - `PUT`
  - `DELETE`
- JSON format for request and response bodies.

### JWT Authentication
- Token-based authentication mechanism.
- Used to secure API endpoints.
- User credentials are verified once and represented by a token.
- User ID is extracted using `RequestContext`.

## Project Architecture

### Controller Layer
- Handles incoming HTTP requests.
- Performs validation checks.
- Returns HTTP responses.

### Repository Layer
- Contains database access logic.
- Executes SQL queries using `JdbcTemplate`.

### RequestContext
- Stores authenticated user data.
- Extracts user ID from JWT token.

---

# Global Exception Handler

## What It Does
- Handles exceptions for **all REST controllers**
- Converts errors to **JSON responses**
- Removes the need for `try-catch` blocks in controllers

## Why It Is Used
- Centralized error handling
- Cleaner and simpler controller code
- Consistent error responses for the frontend

## Handled Exceptions

### MissingServletRequestParameterException
- Occurs when a required request parameter is missing
- Returns **400 Bad Request**
- Response:
  ```json
  { "info": "Missing required fields" }

### ConstraintViolationException
- Occurs when validation fails (@NotNull, @NotBlank, etc.)
- Collects all validation messages into one string
- Returns 400 Bad Request

- Example response:

```json 
{ "info": "Name must not be blank, Age must be greater than 18" }
```

---

# CORS Filter Explanation

This `CorsFilter` class is a **custom Servlet Filter** used in a Spring-based Java application to handle **CORS (Cross-Origin Resource Sharing)** configuration manually.

---

## What Is CORS?

CORS is a browser security mechanism that **restricts web pages from making requests to a different domain** than the one that served the page.  
Servers must explicitly allow such requests by sending specific HTTP headers.

---

## Purpose of This CorsFilter

This filter:
- Intercepts **every incoming HTTP request**
- Adds **CORS-related response headers**
- Handles **preflight (OPTIONS) requests**
- Allows the backend to be accessed from different origins (e.g., frontend apps running on different ports or domains)

---

# Dockerfile Explanation

## 1. Base Image

```dockerfile
FROM ubuntu

Uses a clean Ubuntu Linux environment as the base image.

## 2. Install Required Software

dockerfile
RUN apt update && apt install -y postgresql postgresql-contrib openjdk-17-jdk maven nodejs npm
```
Installs:

- PostgreSQL + contrib tools  
- OpenJDK 17 (required for Spring Boot)  
- Maven (optional but useful)  
- Node.js + npm  

## 3. Install Global Node Package

```dockerfile
RUN npm install -g serve
```

Installs `serve` globally to host static files.

## 4. Copy Project Files Into the Container

```dockerfile
COPY ./productphotos /productphotos
COPY ./dump.sql /home/dump.sql
COPY ./start.sh /home/start.sh
COPY ./target/arabul-0.0.1-SNAPSHOT.jar /home/app.jar
```

- Copies product images  
- Copies SQL dump for DB initialization  
- Copies backend JAR  
- Copies startup script  

## 5. Set Environment Variables

```dockerfile
ENV DB_NAME="arabul"
ENV DB_USER="postgres"
ENV DUMP_FILE="/home/dump.sql"
```

Used by PostgreSQL during database creation.

## 6. Initialize PostgreSQL Database

```dockerfile
USER postgres
RUN service postgresql start && service postgresql status && psql -c "CREATE DATABASE $DB_NAME" && psql -d $DB_NAME -f $DUMP_FILE
```

This:

1. Starts PostgreSQL  
2. Creates a database named `arabul`  
3. Imports schema + data from `dump.sql`  

## 7. Return to Root User & Prepare Startup Script

```dockerfile
USER root
RUN chmod +x /home/start.sh
```

Makes `start.sh` executable.

## 8. Set Entrypoint

```dockerfile
ENTRYPOINT ["/home/start.sh"]
```

When the container starts, it executes `start.sh`.  
This script usually:

- Starts PostgreSQL  
- Runs the Spring Boot application:  
  `java -jar /home/app.jar`

Everything launches automatically.

---

# Product Endpoints Documentation

## 1. POST `/api/products` — Create a Product

### What the endpoint does
- Receives product information and an optional photo.
- Saves the photo file to the server if provided.
- Sends the product data to the database to **create a new product entry**.
- Returns success if the database insert works, otherwise returns an error message.

### Request Content
- `name` (text)
- `description` (text)
- `price` (number)
- `category` (text)
- `photo` (file, optional)

### Response Content
- **200 OK**: `{ "info": "Product inserted." }`
- **500 Error**: `{ "info": "DB Error" }` or `{ "info": "Bad request <message>" }`

## 2. GET `/api/products` — Get All Products

### What the endpoint does
- Requests all product records from the database.
- Returns the complete list of products.
- If the database returns no records, responds that no products were found.

### Request Content
- None

### Response Content
- **200 OK**: List of all products
- **404 Not Found**: `{ "info": "No products found" }`
- **404 Error**: `{ "info": "Error fetching products: <message>" }`

## 3. GET `/api/products/{id}` — Get Product by ID

### What the endpoint does
- Requests a single product from the database using the given ID.
- Returns the product if the database finds a matching record.
- If no product exists with that ID, returns a not-found message.

### Request Content
- Path parameter: `id`

### Response Content
- **200 OK**: Product object
- **404 Not Found**: `{ "info": "No products found for id: {id}" }`
- **404 Error**: `{ "info": "Error fetching product: <message>" }`

## 4. DELETE `/api/products/{id}` — Delete Product by ID

### What the endpoint does
- Instructs the database to delete the product with the provided ID.
- Returns success if the database confirms the deletion.
- If the database reports no product with that ID, responds "not found."

### Request Content
- Path parameter: `id`

### Response Content
- **200 OK**: `"Product deleted"`
- **404 Not Found**: `{ "info": "Not found" }`
- **404 Error**: `{ "info": "DB error" }`

## 5. GET `/api/products/search?term=...` — Search Products

### What the endpoint does
- Sends the search term to the database and requests products whose name or description match the term.
- Returns all matching products.
- If no matches are found, returns a not-found message.

### Request Content
- Query parameter: `term`

### Response Content
- **200 OK**: List of matching products
- **404 Not Found**: `{ "info": "No products found for term: {term}" }`
- **404 Error**: `{ "info": "Error fetching product: <message>" }`

---s

# User Endpoints Documentation

## 1. POST `/api/users` --- Register a User

### What the endpoint does

-   Receives user information and an optional profile picture.
-   Validates required fields, email format, password length, phone
    format, JPEG file type.
-   Saves profile picture to server.
-   Hashes password before storing.
-   Inserts user into database.

### Request Content

-   `name` (text)
-   `email` (text)
-   `phone` (text, optional)
-   `password` (text)
-   `address` (text)
-   `profile_picture` (file, optional --- must be JPEG)

### Response Content

-   **200 OK**: `{ "info": "User inserted" }`
-   **400 Bad Request**: Validation errors
-   **422 Unprocessable Entity**: `{ "info": "Email taken" }`
-   **500 Error**: `{ "info": "Bad request <message>" }`

## 2. POST `/api/users/login` --- Login User

### What the endpoint does

-   Validates required fields.
-   Hashes password and checks database.
-   Returns JWT token in body and Authorization header.

### Request Content

``` json
{
  "email": "example@mail.com",
  "password": "1234"
}
```
### Response Content

-   **200 OK**: `{ "token": "<jwt_token>" }`
-   **400 Bad Request**: Missing fields
-   **404 Not Found**: `"User not found"`
-   **500 Error**: `"Internal server error"`

## 3. GET `/api/users/{id}` --- Get User Info

### What the endpoint does

-   Requires valid Authorization token.
-   Returns user info only if token user matches requested ID.

### Response Content

-   **200 OK**: User object
-   **401 Unauthorized**
-   **404 Not Found**
-   **500 Error**

## 4. PUT `/api/users/{id}` --- Edit User Info

### What the endpoint does

-   Requires valid token.
-   Allows updating name, email, phone, password, address, profile
    picture.
-   Validates all updated fields.
-   Hashes password if changed.

### Response Content

-   **200 OK**: `{ "info": "User infos edited id: <id>" }`
-   **400 Bad Request**
-   **401 Unauthorized**
-   **422 Unprocessable Entity**
-   **404 Not Found**
-   **500 Error**

---

# Cart Endpoints Documentation

## 1. POST `/api/cart` --- Add / Update Cart Item

### What the endpoint does

- Receives product ID.
- Requires valid Authorization token.
- Validates product existence.
- Checks product count limit (maximum 10).
- Inserts product into cart or increases product count.

### Request Content

```json
{
  "product_id": 5
}
```

### Response Content

- **200 OK**: `{ "info": "Cart updated." }`
- **400 Bad Request**: `{ "info": "Bad request" }`
- **401 Unauthorized**: `{ "info": "Unauthorized" }`
- **404 Not Found**: `{ "info": "Product not found" }`
- **422 Unprocessable Entity**: `{ "info": "Product limit exceeded" }`
- **500 Error**: `{ "info": "DB Error" }`

## 2. GET `/api/cart` --- Get User Cart

### What the endpoint does

- Requires valid Authorization token.
- Returns all cart items for authenticated user.
- Joins `cart` and `products` tables.
- Returns empty list if cart is empty.

### Response Content

- **200 OK**:
```json
[
  {
    "product_id": 5,
    "user_id": 2,
    "count": 3,
    "name": "Product name",
    "price": 100
  }
]
```

- **401 Unauthorized**: `{ "info": "Unauthorized" }`

## 3. DELETE `/api/cart/{product_id}` --- Remove Product From Cart

### What the endpoint does

- Requires valid Authorization token.
- Decreases product quantity by 1 if count > 1.
- Deletes product entry if count becomes 0.
- Affects only the authenticated user cart.

### Path Parameters

- `product_id` — Product ID to remove or decrement.

### Response Content

- **200 OK**: `{ "info": "Product deleted" }`
- **401 Unauthorized**: `{ "info": "Unauthorized" }`
- **500 Error**: `{ "info": "DB error" }`

## 4. DELETE `/api/cart` --- Clear Cart

### What the endpoint does

- Requires valid Authorization token.
- Deletes all cart items for authenticated user.

### Response Content

- **200 OK**: `{ "info": "Cleared cart" }`
- **401 Unauthorized**: `{ "info": "Unauthorized" }`
- **500 Error**: `{ "info": "DB error" }`

---

# Order Endpoints Documentation

## 1. POST `/api/order` --- Create Order

### What the endpoint does

- Receives credit card and customer information.
- Requires valid Authorization token.
- Validates credit card number (must start with 4 or 5).
- Validates expiration date format (MM/YY).
- Receives ordered products list.
- Saves order and order products to database.

### Request Content

**Request Parameters**

- `creditCardNumber` (text, 16 characters, must start with 4 or 5)
- `verificationCode` (number, 3 digits)
- `expirationDate` (text, MM/YY)
- `firstName` (text)
- `lastName` (text)

**Request Body**

```json
[
  {
    "product_id": 5,
    "count": 2
  }
]
```

### Response Content

- **200 OK**: `{ "info": "Ordered" }`
- **400 Bad Request**: `{ "info": "Bad request" }`
- **401 Unauthorized**: `{ "info": "Unauthorized" }`
- **500 Error**: `{ "info": "DB error" }`

## 2. GET `/api/order` --- Get User Orders

### What the endpoint does

- Requires valid Authorization token.
- Returns all orders of authenticated user.
- Each order contains purchased products with details.

### Response Content

- **200 OK**:
```json
[
  {
    "order_id": 10,
    "credit_card_number": "**** **** **** 1234",
    "verification_code": 123,
    "expiration_date": "12/25",
    "first_name": "John",
    "last_name": "Doe",
    "order_date": "2025-01-01",
    "products": [
      {
        "product_id": 5,
        "count": 2,
        "name": "Product name",
        "description": "Product description",
        "photo": "photo.jpg",
        "price": 100,
        "category": "Electronics"
      }
    ]
  }
]
```

- **401 Unauthorized**: `{ "info": "Unauthorized" }`
- **500 Error**: `{ "info": "DB error" }`