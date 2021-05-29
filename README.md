# SoPra RESTful Service Template FS21

# Introduction
blablalbalba



# Technologies
## Programming language and environment
* Java 15
* Springboot
* Gradle
* AWS

## Database management
* Hibernate
* JPA
* PostgreSQL

# High-level components
We have structured all our classes in controller, service and repository classes. Every API call to the backend is caught by the responding controller class and further processed in the given service class. The service class then references to a repository, where the final entity is saved. The repositories are all JPA-repositories, which mapps our entities via Hibernate to the database. By coppling our entities with the Mapping annotations of the Java persistence library, it makes the application less error-prone and makes deleting dependent entities in the databse easier.

# Launch & Deployment
## start the application
To start the server locally, just run the [server](https://github.com/sopra-fs21-group-12/serverGroup12) and the [client](https://github.com/sopra-fs21-group-12/clientGroup12) the server is reachable under http://localhost:8080/.

To start the deployed application on Heroku follow this [link](http://sopra-fs21-group-12-client.herokuapp.com/)

## dependencies and database
The PostgreSQL database is only used in the deployed build on Heroku. For testing we have used the H2 in-memory database.
Once a change is commited to the master-branch on GitHub, the application is automatically redeployed on Heroku. The deployment follows the steps defined in the properties file.

# Roadmap
* We would like to implement statistics on users and their items. These data would be used to develop a more sophisticated item Proposal, so that the match percentage of a user with a given item ca be increased
* implement a User rating, where you can rate the other user in terms of the swap process. This would increase trustworthyness of users.
* A swap History with all items you haved swapped so far should be displayed for every item.
* Further Google Maps implementations for a match, so that the route and route time is calculated to make the swap.   

# Authors & Acknowledgment
## Developers
* Joel Ruettimann
* Onur Topalak
* Filip Trendafilv
* Dennis Shushack
* Mauro Dörig
    
We would like to extend our thanks to anyone who has supported us through this challenging but experience-filled project. Also, we'd like to especially mention our TA Remi whose advice and guidance was very valuable to us.

# License
Copyright (c) 2021 Finder.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
