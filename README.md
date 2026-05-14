# 🚀 Bita2ti — Unified Digital Identity Platform

> A modern Spring Boot platform designed to simplify identity management through secure digital identification.

---

## 🌟 Overview

**Bita2ti** is a web-based digital identity platform that provides users and organizations with a centralized and secure identity management system.

The platform allows users to register, manage their digital identity, join organizations, and interact with organization administrators through a secure and scalable environment.

Bita2ti aims to modernize traditional identity systems by replacing fragmented identification processes with a unified digital solution.

---

# ✨ Features

## 🔐 Authentication & Security

* Secure user registration and login
* Password authentication
* Role-based access control
* Protected system operations
* Automated email notification when account is created

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/signup.png"
       width="800">
</p>

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/digital_id_created_email.png"
       width="750">
</p>

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/login.png"
       width="750">
</p>



## 🪪 Digital Identity Management

* Unique digital identity for each user
* Centralized user records
* Organization-based identity management

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/user_dashboard.png"
       width="1000">
</p>

## 🏢 Organization System

* Create and manage organizations
* Organization administrator approval system
* User-to-organization relationships
* Membership status tracking
* Operator (Organization admin) request form
* Validation forms for operators with access control options

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/admin_dashboard.png"
       width="975">
</p>


<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/add_org.png"
       width="975">
</p>

Users can request to become operators (organization admins) of one or more organizations using this form :
<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/operator_request.png"
       width="975">
</p>

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/operator_request_pending.png"
       width="975">
</p>

Main admin can view the operator requests submitted by users:
<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/org_admin_request.png"
       width="975">
</p>

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/operator_approval_email.png"
       width="975">
</p>

## 📋 Subscription & Verification

* Subscription management
* Approval workflows
* Registration verification system

This can only be accessed by the main admin and the approved operators of the following organization only ! :
<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/subscription_validation_form.png"
       width="750">
</p>

Access denied message for unauthorized users :

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/subscription_validation_form_denied.png"
       width="750">
</p>

Checking if user is subscribed to a certain organization using his digital id:
<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/user_is_subscribed.png"
       width="650">
</p>

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/user_not_subscribed.png"
       width="550">
</p>

## 📊 Activity Tracking

* Transaction and activity logging
* Timestamped operations
* User activity monitoring

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/latest_transactions.png"
       width="1050">
</p>

---

# 👥 User Roles

| Role                   | Responsibilities                                     |
| ---------------------- | ---------------------------------------------------- |
| **User**               | Register, login, manage identity, join organizations |
| **Organization Admin / Operator** | Manage organization users and validate user subscriptions              |
| **System Admin**       | Full platform management and monitoring              |

---

# 🗄️ Database Design

Here is an image of the Database Schema (MySQL Workbench) :

<p align="center">
  <img src="https://github.com/mh6168/Bita2ti/blob/main/screenshots/DB_Schema-v001.png"
       width="1050">
</p>


---

# 🛠️ Technologies Used

<div align="center">

| Backend         | Frontend  | Database  | Tools         |
| --------------- | --------- | --------- | ------------- |
| Java            | HTML/CSS  | MySQL     | Maven         |
| Spring Boot     | Thymeleaf | JPA       | Git           |

</div>

---

# ⚡ Installation & Setup

## 1️⃣ Clone the Repository

```bash
git clone <repository-link>
cd Bita2ti
```

---

## 2️⃣ Configure Database

Update your `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bita2ti
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

Application will run on:

```txt
http://localhost:8082
```

---

# 📌 Future Improvements

* QR code verification
* Multi-factor authentication
* Mobile application support
* Cloud deployment
* API integrations
* Enhanced analytics dashboard

---

# 🎯 Project Goal

Bita2ti aims to provide a modern, scalable, and secure digital identity ecosystem for users and organizations while simplifying identity verification and management processes.

---

<div align="center">

### Built by Mohamed Hazem Ahmed

# Bita2ti

### One Identity. One Platform.

</div>
