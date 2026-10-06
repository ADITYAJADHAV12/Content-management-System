# 📚 Content Management System

A full-stack Content Management System (CMS) developed using **HTML, CSS, JavaScript, Spring Boot, Java, and MySQL**.

The system allows an administrator to manage teachers, students, and classrooms. Teachers can upload educational content such as videos, images, and PDF files and provide access to selected classrooms. Students can access the content available to their classroom.

---

## 🚀 Project Overview

The Content Management System is designed for educational institutions, training centers, and classrooms where teachers need to share learning materials with specific groups of students.

The system provides separate functionality for:

- 👨‍💼 Admin
- 👨‍🏫 Teacher
- 👨‍🎓 Student
- 🏫 Classroom

Teachers can upload educational content and control which classrooms can access that content.

---

## 🎯 Objectives

The main objectives of this project are:

- Manage teachers and students from a central system.
- Create and manage classrooms.
- Assign students to classrooms.
- Allow teachers to upload educational content.
- Store uploaded files on the server.
- Store file information and metadata in MySQL.
- Allow teachers to provide content access to specific classrooms.
- Allow teachers to revoke classroom access.
- Allow students to view content assigned to their classroom.
- Support video, image, and PDF content.
- Provide a simple and user-friendly web interface.

---

# 👥 User Roles

## 👨‍💼 Admin

The administrator manages the complete system.

### Admin can:

- Add teachers
- Update teachers
- Delete teachers
- View teachers
- Add students
- Update students
- Delete students
- View students
- Create classrooms
- Update classrooms
- Delete classrooms
- View classrooms
- Assign students to classrooms
- Remove students from classrooms

---

## 👨‍🏫 Teacher

Teachers are responsible for educational content.

### Teacher can:

- Upload videos
- Upload images
- Upload PDF files
- Add content title
- Add content description
- View uploaded content
- Manage classroom access
- Grant content access to classrooms
- Revoke content access from classrooms

---

## 👨‍🎓 Student

Students can access educational content according to their classroom.

### Student can:

- View assigned classroom
- View available learning content
- Play educational videos
- View images
- View PDF documents

Students should only see content that has been granted to their classroom.

---

## 🏫 Classroom

A classroom represents a group of students.

For example:

Java Full Stack
Python Development
Web Development
Data Science

## 🔄 System Workflow
                    ┌─────────────────┐
                    │      ADMIN      │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              ↓              ↓              ↓
        ┌──────────┐   ┌──────────┐   ┌────────────┐
        │ Teachers │   │ Students │   │ Classrooms │
        └──────────┘   └─────┬────┘   └──────┬─────┘
                             │               │
                             └───────┬───────┘
                                     ↓
                              Assign Student
                              to Classroom
                                     │
                                     ↓
                            ┌─────────────────┐
                            │     TEACHER     │
                            └────────┬────────┘
                                     │
                                     ↓
                              Upload Content
                                     │
                    ┌────────────────┼────────────────┐
                    ↓                ↓                ↓
                 Video             Image             PDF
                    │                │                │
                    └────────────────┼────────────────┘
                                     ↓
                              Store File
                                     │
                                     ↓
                              Store Metadata
                               in MySQL
                                     │
                                     ↓
                           Grant Classroom Access
                                     │
                                     ↓
                            ┌─────────────────┐
                            │     STUDENT     │
                            └────────┬────────┘
                                     │
                                     ↓
                              Check Classroom
                                     │
                                     ↓
                         View Permitted Content
                                     │
                    ┌────────────────┼────────────────┐
                    ↓                ↓                ↓
                 ▶ Video           🖼 Image          📄 PDF

# 🏗️ Project Architecture

The Content Management System follows a layered architecture where the frontend communicates with the Spring Boot backend through REST APIs.

```text
┌─────────────────────────────────────────────────────────────┐
│                         FRONTEND                            │
│                                                             │
│              HTML + CSS + JavaScript                        │
│                                                             │
│     Admin UI  │  Teacher UI  │  Student UI                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTP / REST API
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                         │
│                                                             │
│  AdminController                                            │
│  TeacherController                                          │
│  StudentController                                          │
│  ClassroomController                                        │
│  ContentController                                          │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                           │
│                                                             │
│  AdminService                                               │
│  TeacherService                                             │
│  StudentService                                             │
│  ClassroomService                                           │
│  ContentService                                             │
│  FileStorageService                                         │
└───────────────┬─────────────────────────────┬───────────────┘
                │                             │
                │                             │
                ▼                             ▼
┌─────────────────────────────┐   ┌───────────────────────────┐
│      REPOSITORY LAYER       │   │      FILE STORAGE         │
│                             │   │                           │
│  AdminRepository            │   │  uploads/                 │
│  TeacherRepository          │   │    ├── videos/            │
│  StudentRepository          │   │    ├── images/            │
│  ClassroomRepository        │   │    └── pdfs/              │
│  ContentRepository          │   │                           │
│  ContentPermissionRepository│   │  Videos / Images / PDFs   │
└──────────────┬──────────────┘   └───────────────────────────┘
               │
               │ JPA / Hibernate
               ▼
┌─────────────────────────────────────────────────────────────┐
│                       MYSQL DATABASE                         │
│                                                             │
│  admins                                                     │
│  teachers                                                   │
│  students                                                   │
│  classrooms                                                 │
│  contents                                                   │
│  content_permissions                                        │
└─────────────────────────────────────────────────────────────┘

# 🛠️ Technologies Used

| Technology        | Purpose                |
|-------------------|------------------------|
| Java 21           | Backend programming    |
| Spring Boot 4.1.1 | Backend framework      |
| Spring Web        | REST APIs              |
| Spring Data JPA   | Database operations    |
| Hibernate         | ORM                    |
| MySQL             | Database               |
| HTML5             | Frontend structure     |
| CSS3              | Frontend styling       |
| JavaScript        | Frontend functionality |
| Maven             | Dependency management  |
| Git               | Version control        |
| GitHub            | Source code hosting    |

---

# 📁 Project Structure

```text
content-management-system/
│
├── src/
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cms/
│   │   │           └── cms/
│   │   │               │
│   │   │               ├── config/
│   │   │               │   └── SecurityConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AdminController.java
│   │   │               │   ├── TeacherController.java
│   │   │               │   ├── StudentController.java
│   │   │               │   ├── ClassroomController.java
│   │   │               │   └── ContentController.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── Admin.java
│   │   │               │   ├── Teacher.java
│   │   │               │   ├── Student.java
│   │   │               │   ├── Classroom.java
│   │   │               │   ├── Content.java
│   │   │               │   └── ContentPermission.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── AdminRepository.java
│   │   │               │   ├── TeacherRepository.java
│   │   │               │   ├── StudentRepository.java
│   │   │               │   ├── ClassroomRepository.java
│   │   │               │   ├── ContentRepository.java
│   │   │               │   └── ContentPermissionRepository.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── AdminService.java
│   │   │               │   ├── TeacherService.java
│   │   │               │   ├── StudentService.java
│   │   │               │   ├── ClassroomService.java
│   │   │               │   ├── ContentService.java
│   │   │               │   └── FileStorageService.java
│   │   │               │
│   │   │               └── ContentManagementSystemApplication.java
│   │   │
│   │   └── resources/
│   │       │
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   │
│   │       │   ├── css/
│   │       │   │   ├── style.css
│   │       │   │   ├── login.css
│   │       │   │   └── dashboard.css
│   │       │   │
│   │       │   ├── js/
│   │       │   │   ├── login.js
│   │       │   │   ├── admin.js
│   │       │   │   ├── teacher.js
│   │       │   │   ├── student.js
│   │       │   │   └── content.js
│   │       │   │
│   │       │   └── pages/
│   │       │       ├── admin/
│   │       │       │   ├── dashboard.html
│   │       │       │   ├── teachers.html
│   │       │       │   ├── students.html
│   │       │       │   └── classrooms.html
│   │       │       │
│   │       │       ├── teacher/
│   │       │       │   ├── dashboard.html
│   │       │       │   ├── upload-content.html
│   │       │       │   └── my-content.html
│   │       │       │
│   │       │       └── student/
│   │       │           ├── dashboard.html
│   │       │           └── content-view.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
├── uploads/
│   ├── videos/
│   ├── images/
│   └── pdfs/
│
├── .gitignore
├── pom.xml
└── README.md
```

---

# 🗄️ Database Design

The project uses **MySQL** as the database.

### Database

```text
content_management
```

### Main Tables

```text
admins
teachers
students
classrooms
contents
content_permissions
```

---

# 🔗 Entity Relationships

```text
┌─────────────────┐
│    Classroom    │
└────────┬────────┘
         │
         │ 1
         │
         │ Many
         ▼
┌─────────────────┐
│     Student     │
└─────────────────┘


┌─────────────────┐
│     Teacher     │
└────────┬────────┘
         │
         │ uploads
         ▼
┌─────────────────┐
│     Content     │
└────────┬────────┘
         │
         │ permissions
         ▼
┌──────────────────────┐
│  ContentPermission   │
└──────────┬───────────┘
           │
           │ assigned to
           ▼
┌─────────────────┐
│    Classroom    │
└─────────────────┘
```

---

# 📋 Main Tables

## 👨‍💼 Admin

**Table:** `admins`

```text
id
name
email
password
```

---

## 👨‍🏫 Teacher

**Table:** `teachers`

```text
id
name
email
password
status
```

---

## 👨‍🎓 Student

**Table:** `students`

```text
id
name
email
password
status
classroom_id
```

---

## 🏫 Classroom

**Table:** `classrooms`

```text
id
name
description
status
```

---

## 📚 Content

**Table:** `contents`

```text
id
title
description
content_type
file_name
file_path
uploaded_by
created_at
```

---

## 🔐 Content Permission

**Table:** `content_permissions`

```text
id
content_id
classroom_id
granted_by
status
granted_at
```

---

# 🏗️ Project Architecture

The application follows a layered architecture.

```text
┌─────────────────────────────────────────────────────────────┐
│                         FRONTEND                            │
│                                                             │
│                  HTML + CSS + JavaScript                    │
│                                                             │
│       Admin UI  │  Teacher UI  │  Student UI                │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTP / REST API
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                         │
│                                                             │
│  AdminController                                            │
│  TeacherController                                          │
│  StudentController                                          │
│  ClassroomController                                        │
│  ContentController                                          │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                           │
│                                                             │
│  AdminService                                               │
│  TeacherService                                             │
│  StudentService                                             │
│  ClassroomService                                           │
│  ContentService                                             │
│  FileStorageService                                         │
└───────────────┬─────────────────────────────┬───────────────┘
                │                             │
                ▼                             ▼
┌─────────────────────────────┐   ┌───────────────────────────┐
│      REPOSITORY LAYER       │   │      FILE STORAGE         │
│                             │   │                           │
│  AdminRepository            │   │  uploads/                 │
│  TeacherRepository          │   │    ├── videos/            │
│  StudentRepository          │   │    ├── images/            │
│  ClassroomRepository        │   │    └── pdfs/              │
│  ContentRepository          │   │                           │
│  ContentPermissionRepository│   │  Videos / Images / PDFs   │
└──────────────┬──────────────┘   └───────────────────────────┘
               │
               │ JPA / Hibernate
               ▼
┌─────────────────────────────────────────────────────────────┐
│                       MYSQL DATABASE                         │
│                                                             │
│  admins                                                     │
│  teachers                                                   │
│  students                                                   │
│  classrooms                                                 │
│  contents                                                   │
│  content_permissions                                        │
└─────────────────────────────────────────────────────────────┘
```

---

# 🔄 System Workflow

```text
                         CONTENT MANAGEMENT SYSTEM
                                      │
             ┌────────────────────────┼────────────────────────┐
             │                        │                        │
             ▼                        ▼                        ▼
        👨‍💼 ADMIN                👨‍🏫 TEACHER              👨‍🎓 STUDENT
             │                        │                        │
             ▼                        ▼                        ▼
     Manage Users &             Upload Content          View Content
       Classrooms                     │                        │
             │                        ▼                        │
             │                 Content Service                │
             │                        │                        │
             │              ┌─────────┴─────────┐              │
             │              ▼                   ▼              │
             │        File Storage           MySQL             │
             │              │                   │              │
             │              └─────────┬─────────┘              │
             │                        │                        │
             └────────────────────────┼────────────────────────┘
                                      ▼
                              REST API / Backend
                                      │
                                      ▼
                                 Spring Boot
```

---

# 📂 File Storage

Uploaded files are stored separately from the database.

```text
uploads/
│
├── videos/
│   ├── video-1.mp4
│   └── video-2.mp4
│
├── images/
│   ├── image-1.jpg
│   └── image-2.png
│
└── pdfs/
    ├── notes-1.pdf
    └── notes-2.pdf
```

The **actual file** is stored in the server filesystem.

The **file metadata and path** are stored in MySQL.

---

# 🎥 Video Upload and Viewing Flow

```text
Teacher
   │
   ▼
Teacher Dashboard
   │
   │ Upload Video
   ▼
ContentController
   │
   ▼
ContentService
   │
   ├──────────────► FileStorageService
   │                       │
   │                       ▼
   │                uploads/videos/
   │
   ▼
ContentRepository
   │
   ▼
MySQL
   │
   ▼
Content Metadata Saved
```

When a student watches the video:


Student
   │
   ▼
Student Dashboard
   │
   │ Request Video
   ▼
ContentController
   │
   ▼
ContentService
   │
   ▼
File Storage
   │
   ▼
Video File
   │
   ▼
HTML5 Video Player
   │
   ▼
▶ Video Plays
```

---

# 🔌 REST API Endpoints

## Admin

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admins` | Create admin |
| GET | `/api/admins` | Get all admins |
| GET | `/api/admins/{id}` | Get admin |
| PUT | `/api/admins/{id}` | Update admin |
| DELETE | `/api/admins/{id}` | Delete admin |

---

## Teacher

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/teachers` | Create teacher |
| GET | `/api/teachers` | Get all teachers |
| GET | `/api/teachers/{id}` | Get teacher |
| PUT | `/api/teachers/{id}` | Update teacher |
| DELETE | `/api/teachers/{id}` | Delete teacher |

---

## Student

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/students` | Create student |
| GET | `/api/students` | Get all students |
| GET | `/api/students/{id}` | Get student |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |
| PUT | `/api/students/{studentId}/classroom/{classroomId}` | Assign classroom |
| PUT | `/api/students/{studentId}/remove-classroom` | Remove classroom |
| GET | `/api/students/classroom/{classroomId}` | Get classroom students |

---

## Classroom

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/classrooms` | Create classroom |
| GET | `/api/classrooms` | Get all classrooms |
| GET | `/api/classrooms/{id}` | Get classroom |
| PUT | `/api/classrooms/{id}` | Update classroom |
| DELETE | `/api/classrooms/{id}` | Delete classroom |

---

## Content

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/contents/upload` | Upload content |
| GET | `/api/contents` | Get all content |
| GET | `/api/contents/teacher/{teacherId}` | Get teacher content |
| GET | `/api/contents/{id}/stream` | View/stream content |

---

# ⚙️ Configuration

Update the database configuration in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=content-management-system

spring.datasource.url=jdbc:mysql://localhost:3307/content_management
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

---

## ▶️ How to Run

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/content-management-system.git
```

## 2. Create MySQL Database

```sql
CREATE DATABASE content_management;
```

## 3. Configure MySQL

Check:

```text
src/main/resources/application.properties
```

Make sure your MySQL port, username and password are correct.

## 4. Run the Application

Run:

```text
ContentManagementSystemApplication.java
```

Or:

```bash
mvn spring-boot:run
```

## 5. Open in Browser

```text
http://localhost:8080
```

---

## 🔐 Authentication

Authentication and password security are planned for a later stage.

The current development focus is:

```text
CRUD Operations
        +
Classroom Management
        +
File Upload
        +
File Storage
        +
Content Management
        +
Content Permissions
        +
Content Viewing
```

Future authentication can use:

```text
Spring Security
JWT
BCrypt
Role-Based Authorization
```

---

## 🚀 Future Enhancements

- JWT Authentication
- BCrypt Password Hashing
- Role-Based Authorization
- Admin Dashboard Statistics
- Content Search
- Content Categories
- Content Tags
- Student Assignments
- Online Quizzes
- Student Progress Tracking
- Notifications
- Cloud File Storage
- Responsive Mobile UI
- Advanced Video Player
- Secure Video Streaming

---

## 👨‍💻 Developer

## Aditya Jadhav

Computer Engineering

**GitHub:**  
https://github.com/ADITYAJADHAV12

**LinkedIn:**  
https://www.linkedin.com/in/ADITYA-JADHAV-778781321/

**Portfolio:**  
https://adityajadhav12.github.io/Portfolio/resume/

---

# ⭐ Project Highlights

```text
✔ Java 21
✔ Spring Boot
✔ Spring Data JPA
✔ Hibernate
✔ MySQL
✔ REST APIs
✔ HTML5
✔ CSS3
✔ JavaScript
✔ Admin Management
✔ Teacher Management
✔ Student Management
✔ Classroom Management
✔ Content Management
✔ Video Upload
✔ Image Upload
✔ PDF Upload
✔ File System Storage
✔ Classroom-Based Content Access
✔ Video Streaming
✔ Layered Architecture
```


## 📄 License

This project is developed for educational and portfolio purposes.
