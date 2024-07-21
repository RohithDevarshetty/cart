# Shopping Cart Application

## Overview

The Shopping Cart Application is a Spring Boot application designed to manage shopping carts for users. 
It provides a basic set of RESTful APIs to perform various cart-related operations including adding items, updating items, removing items, clearing carts, and checking out.

## Features

- Add products to a cart
- Update product quantities in the cart
- Remove products from the cart
- View price of total cart for user
- Clear the cart for a user
- Checkout all products in the cart
- Fetch cart details including total price and user information

## Project Structure

- **`src/main/java`**: Contains the main application code.
    - **`com.app.cart.controller`**: Contains REST controllers handling API requests.
    - **`com.app.cart.dto`**: Contains Data Transfer Objects (DTOs).
    - **`com.app.cart.entity`**: Contains entity classes representing database tables.
    - **`com.app.cart.exception`**: Contains custom exceptions.
    - **`com.app.cart.mapper`**: Contains mappers for converting entities to DTOs and vice versa.
    - **`com.app.cart.repository`**: Contains repository interfaces for database access.
    - **`com.app.cart.service`**: Contains service interfaces and implementations for business logic.
    - **`com.app.cart.utils`**: Contains utility classes.

- **`src/test/java`**: Contains test cases.
    - **`com.app.cart.controller`**: Contains unit tests for controllers.
    - **`com.app.cart.service`**: Contains unit tests for services.
    - **`com.app.cart.utils`**: Contains unit tests for utility classes.


## Installation

   Pre-requisites
   -> maven, Java, MySQL installed
   ->  add relevant sql username, password, db values in application.properties
   `spring.jpa.hibernate.ddl-auto=update should take care of auto creating tables`

1. **Clone the repository**

   ```sh
   git clone https://github.com/RohithDevarshetty/cart.git
   cd cart
   ```
   
2. **Build dependencies**
  
    ```sh
   mvn clean install
   ```

3. **Start application**

   ```sh
   mvn spring-boot:run
   ```
   
4. **Swagger**

    `http://localhost:8080/swagger-ui/index.html#/`

and your application is up and running at 8080
### If there is any issue in creating tables use file
`cart/src/main/resources/create.sql` to create tables

For sample user and products data
use `/user/initial-save` and `/products/initial-save` Endpoints from swagger


<img title="DB Schema" alt="DB Schema" src="/cartDB.png">

## DB Design


```

CREATE TABLE `users` (
`id` integer PRIMARY KEY,
`name` varchar(255),
`email` varchar(255),
`created` timestamp
);

CREATE TABLE `cart` (
`id` integer,
`user_id` integer,
`product_id` integer,
`quantity` integer,
`price` double,
`created` timestamp,
`updated` timestamp
);

CREATE TABLE `products` (
`id` integer PRIMARY KEY,
`name` varchar(255),
`description` text,
`stock_quantity` integer,
`unit_price` double,
`created` timestamp
);

ALTER TABLE `products` ADD FOREIGN KEY (`id`) REFERENCES `cart` (`product_id`);

ALTER TABLE `users` ADD FOREIGN KEY (`id`) REFERENCES `cart` (`user_id`);
```