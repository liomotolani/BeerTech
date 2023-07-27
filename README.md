# Meal Tracker Management App (Beerkhathon)
### Introduction
Meal Tracker Management App is a solution that was created to address employee taking more than one meal per day, this application helps the company save money on food by tracking employees meal and this was achieved with by having a QR code that each employee scans before they get their food and this changes the meal status state of employee that they won't be able to get a meal for that day. A botcron resets the users meal status at midnight for reuse.
### Meal Tracker Management Features
There are two sides to this application:
* Admin
	@@ -14,32 +14,33 @@ There are two sides to this application:
  * An employee can change their password
  * An employee can scan the companies QR code to get their meal for the day
### Installation Guide
* Clone this repository [here](https://github.com/liomotolani/BeerTech.git).
* The main branch is the most stable branch at any given time, ensure you're working from it.
* Run mvn clean install to install all dependencies
* You can use your locally installed MongoDB for database, You can also use other kind of database do configure to your choice in the application.properties file.
* Check the application.properties file in the resources folder and add your variables.
### Usage
* Run the application by locating the MealTrackerAPIApplication class and then click on run.
* Connect to the API using Postman on port 8080.
### API Endpoints
| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| POST | /v1/auth/login | To login an existing user account |
| PUT | /v1/auth/change_password | To change password |
| POST | /v1/user/add | To add employee |
| PATCH | /v1/user/read_qrcode?code={code} | To read scanned qr code |
| GET | /v1/user | To retrieve details of a single employee |
| GET | /v1/user/all | To retrieve details of all employees |
| GET | /v1/user/in_active | To fetch all employees that have used up their meal for the day |
### Technologies Used
* [Springboot](https://www.spring.io/) This is a popular Java-based framework that is used for building stand-alone, production-ready web applications and microservices
* [MongoDB](https://www.mongodb.com/) This is a free open source NOSQL document database with scalability and flexibility. Data are stored in flexible JSON-like documents.
* [Elastic mail](https://elasticemail.com/) This is an email marketing tool for sending emails and campaigns
* [Railway ](https://railway.app/) This is a free deployment platform to drag and drop tools for your projects to be production ready.

### Swaagger Documentation
* [Meal Tracker API](https://beertech-production.up.railway.app/swagger-ui/index.html)
### Author
* [Omotolani Ligali](https://github.com/liomotolani)
### License
This project is available for use under the MIT License.
