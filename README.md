
# 🍒 E-commerce Order Processing System

A microservices-based e-commerce backend system that handles product catalog management, shopping cart functionality, order processing, and payment simulation. The system is designed with an event-driven architecture using Apache Kafka and includes observability through Kafka UI or Kouncil.

---

## 📌 Features

- **Product Management**: Add, update, and retrieve product details.
- **Cart Management**: Manage user carts including add/remove products.
- **Order Processing**: Place orders and persist order data.
- **Payment Simulation**: Simulate payment processing asynchronously.
- **Event-Driven Architecture**: Kafka-based communication for order flow.
- **Persistence**: MongoDB or PostgreSQL support per microservice.
- **Monitoring**: Kafka events monitored with [Kouncil](https://kafka-ui.provectus.io/) or Kafka UI.

---

## 🧱 Architecture

```
+----------------+     Kafka: order-placed      +----------------+     Kafka: payment-completed    +------------------+
| Cart Service   | ---------------------------> | Order Service  | -----------------------------> | Payment Service  |
+----------------+                              +----------------+                                +------------------+
         |                                              |                                                  |
         | REST API                                     | REST API                                         |
         v                                              v                                                  v
+----------------+                              +----------------+                                +------------------+
| Product Service| <--------------------------- | Inventory Update Topic ------------------------- |     Kafka UI     |
+----------------+                                                                              +------------------+
```

---

## 📦 Microservices

### 🧾 Product Service

- **Purpose**: Manage product catalog.
- **Database**: MongoDB/PostgreSQL

#### REST Endpoints

| Method | Endpoint         | Description         |
|--------|------------------|---------------------|
| GET    | /products        | Get all products    |
| GET    | /products/{id}   | Get product by ID   |
| POST   | /products        | Add new product     |
| PUT    | /products/{id}   | Update product      |
| DELETE | /products/{id}   | Delete product      |

#### DTO: `ProductDTO`

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "price": "double",
  "stock": "int"
}
```

---

### 🛒 Cart Service

- **Purpose**: Manage user shopping cart.

#### REST Endpoints

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | /cart/{userId}          | Get cart for a user        |
| POST   | /cart/{userId}/add      | Add item to cart           |
| DELETE | /cart/{userId}/remove   | Remove item from cart      |
| POST   | /cart/{userId}/checkout | Checkout cart (send event) |

#### DTO: `CartItemDTO`

```json
{
  "productId": "int",
  "quantity": "int"
}
```

#### DTO: `CartDTO`

```json
{
  "userId": "int",
  "items": [
    {
      "productId": "string",
      "quantity": "int"
    }
  ]
}
```

---

### 📦 Order Service

- **Purpose**: Handle orders and inventory.

#### REST Endpoints

| Method | Endpoint      | Description              |
|--------|---------------|--------------------------|
| GET    | /orders       | Get all orders           |
| GET    | /orders/{id}  | Get order by ID          |
| POST   | /orders       | Create new order (internal) |

#### Kafka Consumer
- **Topic**: `order-placed`
- **Action**: Receive cart details and create an order

#### Kafka Producer
- **Topics**:
    - `inventory-updated`
    - `payment-requested`

#### DTO: `OrderDTO`

```json
{
  "orderId": "string",
  "userId": "string",
  "items": [
    {
      "productId": "string",
      "quantity": "int"
    }
  ],
  "totalAmount": "double",
  "status": "string"
}
```

---

### 💳 Payment Service

- **Purpose**: Simulate payment process

#### Kafka Consumer
- **Topic**: `payment-requested`
- **Action**: Process payment and emit confirmation

#### Kafka Producer
- **Topic**: `payment-completed`

#### DTO: `PaymentDTO`

```json
{
  "orderId": "string",
  "paymentStatus": "string",
  "timestamp": "ISO8601 string"
}
```

---

## 📡 Kafka Topics

| Topic Name         | Publisher        | Subscriber        | Event Description                  |
|--------------------|------------------|-------------------|------------------------------------|
| `order-placed`     | Cart Service     | Order Service     | Trigger order creation             |
| `payment-requested`| Order Service    | Payment Service   | Initiate payment                   |
| `payment-completed`| Payment Service  | Order Service     | Confirm order payment              |
| `inventory-updated`| Order Service    | Product Service   | Adjust product stock levels        |

---

## 🔍 Monitoring with Kouncil

- Run **Kouncil** or **Kafka UI** locally or via Docker.
- Connect to the Kafka broker and observe message flow.
- Useful for debugging event triggers and service workflows.

---

## 🛠️ Technologies Used

- Java + Spring Boot
- Apache Kafka
- MongoDB / PostgreSQL
- Docker
- Kouncil or Kafka UI

---

## 🚀 Getting Started

Each microservice can be run independently. Example using Docker:

```bash
docker-compose up --build
```

### Requirements

- Apache Kafka
- Zookeeper
- MongoDB or PostgreSQL (for respective services)
- Kafka UI / Kouncil for monitoring

---

## 📁 Project Structure

```
ecommerce-order-processing/
├── product-service/
├── cart-service/
├── order-service/
├── payment-service/
├── docker-compose.yml
└── README.md
```

---

## 📬 Contributing

PRs and issues are welcome! Please ensure your code follows standard Spring Boot practices and includes tests.

---

## 📃 License

This project is licensed under the MIT License.
