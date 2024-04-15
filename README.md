# Enterprise Manager
### By: Brendan Smith

---

## 📝 About
Enterprise Manager is a Java web application that has pages dedicated for an admin, client, accountant, and data entry. The application runs on an Apache Tomcat container using JSPs and HTML for the frontend and Java for the backend

Images of some operations from different roles can be found in the screenshots folder.

---

## 🛠 Installation

Needed Technology:

- Apache Tomcat
- mySQL
- Java Connectors:
    - mySQL


Clone and set up (only recommended for developers):
In your Apache Tomcat directory, navigate to the webapps folder and create a new folder inside it for the project.
Inside of this container, create a structure like this (folder names are important along with case):

- Project-Name
    - WEB-INF
        - classes
        - lib


```
git clone https://github.com/Brendan-Smith529/Java-Web-App.git
or
git clone git@github.com:Brendan-Smith529/Java-Web-App.git

cd Java-Web-App

In Linux or Mac:
Database initialization:
$ mysql -u root -p < project4dbscript.sql
Enter your password when prompted

Container Setup:
$ cp web.xml '/path/to/container/'

Frontend:
$ find ./ -name '*.jsp' -exec cp -prv '{}' '/path/to/container/' ';'
$ cp authentication/authentication.html

Backend:
$ find ./ -name '*.class' -exec cp -prv '{}' '/path/to/container/classes/'

Properties:
$ cp properties/*.properties '/path/to/container/lib/'
Add the mySQL connector to '/path/to/container/lib/'
Add a file with mySQL credentials, called credentials.txt, to '/path/to/container/lib/'
```
