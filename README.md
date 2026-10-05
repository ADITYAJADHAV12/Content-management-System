# Classroom Content Management System

A web-based Classroom Content Management System (CMS) developed using **Java, Spring Boot, Spring Data JPA, MySQL, HTML, CSS, and JavaScript.

The system allows an administrator to manage teachers and classrooms. Teachers can upload educational content and control which classrooms are allowed to access that content. Classrooms can log in and view only the content that has been granted to them.


## 📌 Project Overview

The Classroom Content Management System provides three different user roles:

- **Admin**
- **Teacher**
- **Classroom**

Each role has its own functionality and dashboard.

### Admin

The administrator can:

- Login to the system
- Add teachers
- Edit teacher details
- Enable or disable teacher accounts
- Add classrooms
- Edit classroom details
- Enable or disable classroom accounts
- View all teachers
- View all classrooms

Teacher

- Login to the system
- View their profile
- Upload educational content
- Upload videos, images, PDFs and other supported files
- Edit content information
- Delete content
- View their uploaded content
- Grant content access to specific classrooms
- Revoke content access
- View which classrooms have access to their content

Classrooms can:

- Login to the system
- View classroom profile
- View permitted educational content
- Watch permitted videos
- View images
- Open PDF documents
- Access other permitted files

---

# 🚀 Main Features

## 🔐 Role-Based Login

The system provides separate dashboards based on the logged-in user's role.

                    Login
                      |
          +-----------+-----------+
          |           |           |
         Admin      Teacher    Classroom
          |           |           |
       Admin       Teacher     Classroom
      Dashboard    Dashboard    Dashboard
