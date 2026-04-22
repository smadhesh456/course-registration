# Cloud Deployment Guide - Course Registration System

## Overview
This guide walks you through deploying your Java/JSP Course Registration app to Render.com (free tier).

---

## Prerequisites
- GitHub account
- Render.com account (free)
- Database hosting (Render provides free MySQL)

---

## Files Ready for Deployment

### 1. Dockerfile (CourseRegistration/Dockerfile)
```dockerfile
FROM tomcat:10.1.24-jdk17

COPY CourseRegistration.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
ENV PORT=8080

ENV DB_URL=jdbc:mysql://localhost:3306/course_registration
ENV DB_USERNAME=root
ENV DB_PASSWORD=
ENV DB_DRIVER=com.mysql.cj.jdbc.Driver

CMD ["catalina.sh", "run"]
```

### 2. SQL Setup Script (deploy/setup_database.sql)
Contains all tables and sample data.

### 3. WAR File (deploy/CourseRegistration.war)
The compiled application.

---

## Step 1: Push Code to GitHub

### Option A: Using GitHub CLI
```bash
# Initialize git if not done
git init

# Add files (ignoring logs/temp files via .gitignore)
git add CourseRegistration/ deploy/ course_registration_full.sql

# Add .gitignore update
git add .gitignore

# Commit
git commit -m "Prepare for cloud deployment"

# Create GitHub repo and push
gh repo create course-registration --public --source=. --push
```

### Option B: Manual Upload
1. Go to github.com/new
2. Create a new repository (e.g., "course-registration")
3. Upload these files:
   - `CourseRegistration/Dockerfile`
   - `CourseRegistration/CourseRegistration.war` (renamed from deploy/CourseRegistration.war)
   - `deploy/setup_database.sql` (renamed to setup_database.sql)
   - `course_registration_full.sql`

---

## Step 2: Deploy to Render.com

### 1. Create Account
- Go to [render.com](https://render.com)
- Sign up with GitHub

### 2. Create Web Service
1. Click "New +" → "Web Service"
2. Connect your GitHub repository
3. Configure:
   - **Name**: course-registration
   - **Region**: Oregon (or closest to you)
   - **Branch**: main
   - **Build Command**: (leave empty - Dockerfile handles it)
   - **Start Command**: (leave empty - Dockerfile handles it)
   - **Dockerfile Path**: CourseRegistration/Dockerfile

### 3. Set Environment Variables
In Render dashboard, go to "Environment" tab and add:
- `DB_URL` = `jdbc:mysql://<your-mysql-host>:3306/<database>`
- `DB_USERNAME` = `<your-db-username>`
- `DB_PASSWORD` = `<your-db-password>`

### 4. Deploy
Click "Create Web Service"

---

## Step 3: Set Up Database

### Option A: Render MySQL (Recommended)
1. In Render dashboard: "New +" → "MySQL"
2. Choose free tier
3. Note the connection details (host, port, database, username, password)
4. Copy the internal hostname to your web service environment variables

### Option B: External MySQL Host
Use a free MySQL hosting service:
- [db4free.net](https://www.db4free.net/) (free MySQL)
- [freemysqlhosting.net](https://www.freemysqlhosting.net/) (free)

### 2. Run SQL Setup
In Render's MySQL shell or your SQL client:
```sql
-- Paste contents from course_registration_full.sql
```

Or use the setup_database.sql file from deploy folder.

---

## Step 4: Verify Deployment

After deployment completes:
1. Visit your Render URL: `https://course-registration.onrender.com`
2. Test login/signup
3. If errors, check Render logs

---

## Environment Variables Reference

Your app already supports these environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| DB_URL | JDBC connection string | jdbc:mysql://localhost:3306/course_registration |
| DB_USERNAME | Database username | root |
| DB_PASSWORD | Database password | yourpassword |
| DB_DRIVER | JDBC driver class | com.mysql.cj.jdbc.Driver |

---

## Troubleshooting

### Application Won't Start
- Check Render logs for errors
- Ensure environment variables are set correctly
- Verify MySQL is running

### Database Connection Error
- Check DB_URL format: `jdbc:mysql://host:port/database`
- Verify username/password
- Ensure MySQL is accessible from Render's network

### 503 Error
- Application may still be starting
- Check build logs in Render dashboard

---

## Alternative: Fly.io

If Render doesn't work, try [Fly.io](https://fly.io):
1. Install flyctl: `winget install flyctl`
2. Launch: `fly launch`
3. Deploy: `fly deploy`

Fly.io offers persistent volumes and supports Docker.

---

## Security Notes

- Never commit database passwords to GitHub
- Use Render's environment variables for secrets
- The `.gitignore` already excludes logs and temp files