# Search Media

 

Search Media project has apis calling itunes as well as google books to fetch books and albums based on interest

 

## Getting Started

 

 The project consist of 2 parts. API that fetches results and Client that displays results.
 Below instructions will help you in getting the project up and running on your local machine for development and testing purposes. 
 
### Prerequisites

 

* Java 8 or higher
* Maven 3.6.* or higher
* Node JS 10 or higher (with npm)
* Angular 10 or higher
```
java -version
```
```
mvn -v
```
```
node --version
```
```
npm --v
```
 

### Running the API locally

 

After checkingout repo , install using mvn from the project root directory(where the pom file resides)
```
run "mvn clean install"
```
Start the API
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
That's it , GO to your browser and hit 
```
Healthcheck
http://localhost:8080/actuator/health
```
Actual Media Search
```
peace can be replaced with your interest
http://localhost:8080/media?term=peace
```
Metrics
```
http://localhost:8080/actuator/prometheus
```
Change Limit based on environment
```
go to application-{env}.properties and change media.limit
```

### Running the Client locally

 

After checkingout repo , install node modules from the project root directory(where the package json)
```
npm install
```
Start the Client
```
ng serve
```
That's it , GO to your browser and hit 
Type your interest in text area and click on Search Icon
```
http://localhost:4200/
```

#### Choices of Technology and Architecture
* Springboot is chosen as its a microservice architecture and making it production ready would need scalability which will be easy to implement with this approach.
* Async calls are implemented to google and itunes which makes it faster.
* Exceptions are handled within async call to not hamper upstream APIs response with each other
* rest template is built using builder to enable metrics capturing
* Converters are handled manually because itunes response was with content type text/javascript and micrometer responds with content type null 
* angular is used for my own learning purposes
* angular project is separate from springboot to keep client and api separate
* Putting Springcloud with eureka as well as apigateway will make the same microservice scalable
* junit 5 is used

#### Enhancements
* redis or other sort of caching can be implemented for faster responses
* In case of certain number of connection failures from one of the upstream apis , we can stop making calls to the api during main service call. We can rather keep on checking upstream api status in a separate polling method and call it only when available. 
* junits are written for service , dtos and controller but can be enhanced for clients

 
