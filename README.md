# Meal Tracker Management App (Beerkhathon)
### Introduction
Meal Tracker Management App is a solution that was created to address employee taking more than one meal per day, this application helps the company save money on food by tracking employees meal and this was achieved with by having a QR code that each employee scans before they get their food and this changes the meal status state of employee that they won't be able to get a meal for that day..
### Meal Tracker Management Features
There are two sides to this application:
* Admin
  * An admin can login 
  * An admin can change their password
  * An admin can add employee to create a profile for each employee on the application
  * An admin can fetch all employees
  * An admin can view list of employees that have used their meal ticket for the day. 
* Employee
  * An employee can login 
  * An employee can change their password
  * An employee can scan the companies QR code to get their meal for the day
### Installation Guide
* Clone this repository [here](https://github.com/liomotolani/optimal-route.git).
* The main branch is the most stable branch at any given time, ensure you're working from it.
* Run mvn clean install to install all dependencies
* You can use your locally installed MongoDB for database, You can also use other kind of database do configure to your choice in the application.properties file.
* Check the application.properties file in the resources folder and add your variables.
### Usage
* Run the application by locating the LogisticApplication class and then click on run.
* Connect to the API using Postman on port 8080.
### API Endpoints
| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| POST | /v1/auth/register | To sign up a new user account |
| POST | /v1/auth/login | To login an existing user account |
| POST | /v1/location/add | To add a location |
| PUT | /v1/location/:locationId/update | To edit details of a location |
| GET | /v1/location/:causeId | To retrieve details of a single location |
| GET | /v1/location | To retrieve the details of a all locations |
| DELETE | /v1/location/:locationId | To delete a single location |
| POST | /v1/delivery/generate_details | To generate the optimal route for delivery and delivery cost |
### Technologies Used
* [Springboot](https://www.spring.io/) This is a popular Java-based framework that is used for building stand-alone, production-ready web applications and microservices
* [MongoDB](https://www.mongodb.com/) This is a free open source NOSQL document database with scalability and flexibility. Data are stored in flexible JSON-like documents.
* [HERE API](https://here.com/) This is a free-tier external API to calculate route between two points.
### Swaagger Documentation
* [Optimal Delivery Route API](http://localhost:8080/swagger-ui/index.html)
### Authors
* [Omotolani Ligali](https://github.com/liomotolani)
### License
This project is available for use under the MIT License.
