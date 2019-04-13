# Operations Filter
<img src="http://phronesis-partners.com/sra/wp-content/uploads/2017/07/Investment-Bankin-1.jpg" title="OperationFilter" alt="Operation_Filter">

An application to manage your Banking Operation by: 
* Adding new Operations
* Find them with advanced search bar
* Display and sort them as u like (Sorting and Pagination)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them:

* [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java Environnement
* [Maven](https://maven.apache.org/) - Packages Management
* [Spring](https://spring.io/) - Java Framework
* [Elastic Search](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html) - Database NOSQL Framework
* [Node.js](https://nodejs.org/en/) - JS Server Framework
* [Angular X CLI](https://cli.angular.io/) - JS Framework used by commands
* [Angular Material](https://material.angular.io/components/categories) - CSS & JS Framework

### Installing

A step by step series that tell you how to get the development env running. Once you cloned the repository in your favorite workspace, there is 3 parts (each in a new tab) in order to launch the app. 

Launching Elastic Search: (need to be [installed](https://www.elastic.co/guide/en/elasticsearch/reference/current/install-elasticsearch.html) before)

```
cd ./ElasticSearchFolder/bin
elasticsearch
```

Launching API: 
```
cd ./Operations_Filter/mybank-api/
mvn update
mvn spring-boot:run -f pom.xml
```

Launching Angular Server:
```
cd ./Operations_Filter/mybank-api/
ng build
ng serve
```

#### Once these steps are completed, got to the following [url](http://localhost:4200/) to access homepage. 

---

##### On first launch, the database is empty. In order to add new Operations, you need to follow these steps:
###### *Link for field [types](https://github.com/louiiuol/Operations_Filter/blob/master/mybank-api/src/main/java/com/mybank/app/entities/OperationType.java)*
```
- Open Postman or any software capable of CRUD operation 
- create a POST request with a body request JSON file as follow: 
{
"label": "Your label",  (20 characters max)
"amount": XXX.XX,  (2 digits decimals only)
"isCredential": [true/false], (choose True or False statement)
"account": "XXXXX XXXXX XXXXXX XXX XX", (Account number as a String)
"type": "TRANSFER" (Refer to the link below)
}
```


## Running the tests

Tests are not implemented yet! 

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/louiiuol/f1ca9436c877c85f39f20e683ed64156) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Louis Godlewski**  - [louiiuol](https://github.com/louiiuol)
