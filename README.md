# TaskManagerAPI - README

## Step #01
1. Register your user at `http://localhost:8080/register/`.
2. Enter username, password, role, dob, and gender.

-   Note: the roles variable must be in the following format with no spaces:
    -   "USER,ADMIN"
    -   "ADMIN,USER"
    -   "USER"
    -   "ADMIN"

## Step #02
1. Using Spring Tools Suite (or your preferred IDE), open TaskManagerAPI.

2. Adjust the Application.properties file and insert your designated username and password for MySQL
     - spring.datasource.username= [your_username]
     - spring.datasource.password= [your_password]

4. Run the TaskManagerAPI as a "Spring Boot App".

5. Open Chrome Web Browser and go to `https://www.localhost:8080/login`

## Step #03

1. Login your user at `http://localhost:8080/login`
2. You will be authenticated and you will be authorized to access the endpoints accessible with your specified role.

### Note: For Users without the "ADMIN" role

-   Users without "ADMIN" role can access the following Endpoints
    -   CREATE NEW TASK --> `http://localhost:8080/api/task/create`
    -   UPDATE TASK BY ID --> `http://localhost:8080/api/task/update/{id}`
    -   GET ALL TASKS --> `http://localhost:8080/api/getalltasks?pageNo=0&pageSize=10`
    -   GET TASK BY ID --> `http://localhost:8080/api/task/{id}`
    -   DELETE TASK BY ID --> `http://localhost:8080/api/task/delete/{id}`

### Note: For Users with "ADMIN" role

-   Users with "ADMIN" role can access the following Endpoints
    -   CREATE NEW TASK --> `http://localhost:8080/api/task/create`
    -   UPDATE TASK BY ID --> `http://localhost:8080/api/task/update/{id}`
    -   GET ALL TASKS --> `http://localhost:8080/api/getalltasks?pageNo=0&pageSize=10`
    -   GET TASK BY ID --> `http://localhost:8080/api/task/{id}`
    -   DELETE TASK BY ID --> `http://localhost:8080/api/task/delete/{id}`
    -   WHICH USER IS LOGGED IN --> `http://localhost:8080/admin/activeuser`
    -   GET ALL USERS --> `http://localhost:8080/admin/getallusers`
    -   GET USER BY ID --> `http://localhost:8080/admin/user/{id}`
    -   DELETE USER BY ID --> `http://localhost:8080/admin/user/delete/{id}`
    
    
### Properties for Railway
-   spring_profiles_active=prod
-   PROD_DB_HOST=mysql.railway.internal
-   PROD_DB_PORT=3306
-   PROD_DB_NAME=railway
-   PROD_DB_PASSWORD=oCEeYZNVfqvohramvGwVmsxTSVFKuSZS
-   PROD_DB_USERNAME=root
    
    
