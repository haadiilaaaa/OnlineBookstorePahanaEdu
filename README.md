📚 PahanaEdu – Online Bookstore
  
My Project Video https://drive.google.com/file/d/1QVplOXW56SDjZof4v5w0Caztv7SbWv89/view?usp=sharing 

PahanaEdu is a Java Servlet-based online bookstore web application designed for managing books, customers, staff, and administrators in a secure and scalable way.
The system automates registration, authentication, book browsing, cart management, orders, and payments, while strictly following SOLID principles, layered architecture, and design patterns for maintainability and extensibility.

🚀 Features
👤 User Roles

Admin – Manages books, categories, discounts, customers, staff, and system guidelines.

Staff – Manages books, assists with discounts, and supports order management.

Registered Customer – Can browse, purchase, manage cart, checkout, and view invoices.

Unregistered Customer (Guest) – Can browse and add books to cart, but must log in or register to checkout.

🔐 Registration & Authentication

Secure password hashing using SHA-256 with salt.

OTP-based email verification for new accounts (via OtpSenderService).

Session-based login/logout with role-based access control.

Forgot Password & Reset via secure email tokens.

Enforces globally unique usernames & emails across all user types.

🛒 Bookstore Features

Browse books with search & filter (category, author, price).

Add/remove/update items in cart.

Checkout with Cash / Debit / Credit Card options.

Generate invoice & PDF bill after purchase.

Order confirmation & status updates sent via email.

📦 Order & Delivery Management

Customers can place, cancel, and track orders.

Admins assign orders to delivery partners.

Delivery Partners accept/reject orders, update delivery status, and view earnings.

🛡️ Security Features

Role-based access filters (Customer, Admin, Staff).

Session management with secure logout.

OTP & token expiry handling.

Password reset with expiry-based secure tokens.

🏗️ System Architecture

The project follows a 3-Tier Layered Architecture:

Presentation Layer (Servlets, JSP, CSS, JS) – Handles UI and HTTP requests.

Service Layer – Business logic, validation, OTP, and security rules.

Data Access Layer (DAO) – Handles database operations via MySQL.

DTOs & Mappers – Transfer structured data between layers.

Utility Layer – Common helpers like ID generation, hashing, validation, etc.

📌 Design Patterns Used

Singleton – Database connection (DBConnection).

Factory – LoginServiceFactory for dynamic service creation.

Strategy – Registration strategies (Admin, Customer, Staff, Delivery Partner).

Command – Encapsulating actions like category/discount management.

Facade – Simplified registration flow (RegistrationFacadeServiceImpl).

Observer – Email notifications (e.g., order confirmation).

📂 Project Folder Structure
src/
 ├── controller/         # Servlets (LoginServlet, RegisterServlet, DashboardServlet, etc.)
 ├── dao/                # Data Access Objects (AdminDAO, CustomerDAO, StaffDAO, etc.)
 ├── dto/                # Data Transfer Objects
 ├── mappers/            # Mapping between DTOs and Models
 ├── model/              # Entity classes (User, Customer, Admin, Staff, Order, Item, etc.)
 ├── services/
 │    ├── admin/         # Admin services
 │    ├── customer/      # Customer services
 │    ├── staff/         # Staff services
 │    ├── common/        # Shared services (OtpSenderService, PasswordService, etc.)
 ├── strategy/           # Strategy pattern implementations for registration
 ├── utility/            # Common utilities (DBConnection, HashingUtil, IDGenerator, etc.)
 ├── web/                # JSP pages (Admin, Customer, Staff, Shared login/register pages)
 │    ├── admin/
 │    ├── customer/
 │    ├── staff/
 │    ├── common/
 └── resources/          # Config files, SQL scripts, etc.

🛠️ Technologies Used

Java EE (Jakarta Servlets & JSP)

MySQL (Relational Database)

Jakarta Mail API (for OTP & notifications)

CSS & JavaScript (frontend validation & UI design)

JUnit (Test-driven development & automation)

Git & GitHub (Version control)

🧪 Testing & Quality Assurance

Test-Driven Development (TDD) – All features implemented alongside JUnit tests.

Unit Tests – DAO, Services, and Utility classes tested independently.

Integration Tests – Servlet & database interaction tested in isolated environments.

Automation Testing – Selenium/JUnit used for login, registration, and checkout flows.

Test Cases Document – Covers 75+ functional scenarios (registration, login, orders, payments, etc.).

📈 Future Enhancements

AI-based book recommendations.

Wishlist & Review system for customers.

Payment gateway integration with PayPal/Stripe.

RESTful API for mobile apps.

Cloud deployment (AWS/Heroku).

📜 How to Run Locally

git clone https://github.com/your-username/Onile_bookstore_pahanaedu.git
cd Onile_bookstore_pahanaedu



Import the project into IntelliJ IDEA / Eclipse as a Maven project.

Set up a MySQL database and import the provided SQL script.

Configure database credentials in DBConnection.java.

Deploy on Apache Tomcat.

Access the system via:

http://localhost:8080/PahanaEdu

👩‍💻 Author

Awnul Fassy Fathima Hadila
📧 [hadilafassy@gmail.com
]
🎓 Software Engineering Student | International College of Business & Technology (ICBT)