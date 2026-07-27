# 🏨 Hotel Management System

A full-stack backend Hotel Management System built using **Java, Spring Boot, Spring Data JPA, PostgreSQL, and REST APIs**. The application allows users to search hotels, book rooms, make payments, and manage bookings with secure business logic and proper entity relationships.

---

## 🚀 Features

### 👤 User Module
- User Registration
- Update User Details
- Get User Details
- Delete User
- Role-based users (Customer/Admin)

### 🏨 Hotel Module
- Add Hotel
- Update Hotel
- Delete Hotel
- Get All Hotels
- Search Hotels by City

### 🛏️ Room Module
- Add Rooms to a Hotel
- Get Rooms by Hotel
- Update Room Details
- Delete Room
- Room Categories (Standard, Deluxe, Suite)
- Room Availability Management

### 📅 Booking Module
- Book a Room
- Booking Availability Check
- Prevent Overlapping Bookings
- Get User Bookings
- Cancel Booking
- Automatic Booking Expiration after 5 Minutes if Payment is Not Completed

### 💳 Payment Module
- Make Payment
- Payment Status Tracking
- Refund Support
- Booking Confirmation only after Successful Payment

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- REST APIs
- Postman

---

## 📂 Project Structure

```
src
│
├── Controller
├── Services
├── Repository
├── Entity
├── DTO
├── Scheduler
├── Config
└── Resources
```

---

## 📊 Database Design

### Entities

- User
- Hotel
- Room
- Booking
- Payment

### Relationships

- One User → Many Bookings
- One Hotel → Many Rooms
- One Room → Many Bookings
- One Booking → One Payment

---

## 🔄 Booking Flow

```
User
   │
   ▼
Search Hotels
   │
   ▼
Select Hotel
   │
   ▼
Choose Room
   │
   ▼
Availability Check
   │
   ▼
Booking Created (PENDING)
   │
   ▼
Payment
   │
   ├───────────────► Success
   │                     │
   │                     ▼
   │              Booking CONFIRMED
   │
   └───────────────► Failure / Timeout
                         │
                         ▼
               Booking CANCELLED
```

---

## ⏰ Automatic Booking Cancellation

A Spring Scheduler runs every minute and automatically cancels bookings that remain in **PENDING** status for more than **5 minutes**.

---

## 📌 API Endpoints

### User APIs

```
POST    /user/create-user
GET     /user/get-all-users
PUT     /user/update-user/{id}
DELETE  /user/delete-user/{id}
```

### Hotel APIs

```
POST    /hotel/create
GET     /hotel/get-all
GET     /hotel/get-by-city/{city}
PUT     /hotel/update/{id}
DELETE  /hotel/delete/{id}
```

### Room APIs

```
POST    /room/create/{hotelId}
GET     /room/get/{hotelId}
PUT     /room/update/{roomId}
DELETE  /room/delete/{roomId}
```

### Booking APIs

```
POST    /booking/create
GET     /booking/users/{userId}/bookings
PUT     /booking/cancel/{bookingId}
```

### Payment APIs

```
POST    /payment/pay/{bookingId}
GET     /payment/{bookingId}
PUT     /payment/refund/{paymentId}
```

---

## ⚙️ Installation

Clone the repository

```bash
git clone https://github.com/yourusername/Hotel-Manage-System.git
```

Move into project

```bash
cd Hotel-Manage-System
```

Configure PostgreSQL credentials in

```
application.properties
```

Run the application

```bash
mvn spring-boot:run
```

---

## 👨‍💻 Author

**Anant Gupta**

- Java Backend Developer
- Spring Boot Enthusiast
- MERN Stack Developer

---

## ⭐ If you found this project helpful, don't forget to star the repository!
