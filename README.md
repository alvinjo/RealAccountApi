## List Of Symbols
| Acronym/Abbreviation   | Meaning |
| ---------------------- | ---------------------- |
| API | [Application Programming Interface](https://www.howtogeek.com/343877/what-is-an-api/) |
| DB | Database |
| MS | [Micro-Service](https://www.edureka.co/blog/what-is-microservices/) |
| JMS | [Java Messaging Service](https://www.javatpoint.com/jms-tutorial) |
| REST | [Representational state transfer](https://medium.com/extend/what-is-rest-a-simple-explanation-for-beginners-part-1-introduction-b4a072f8740f) |

## Index
0. [Project Definition](#0-Project-Definition)
1. [Architecture](#1-architecture)
   * [High Level Architecture Diagram](#High-level-architecture-diagram)
   * [The stack](#The-Stack)
   * [Springboot API Structure](#SpringBoot-API-Structure)
     
2. [The Micro-Services](#2-The-Micro-Services)
   * [React Front End](#React-Front-End)
   * [AccountApi](#AccountApi)
   * [AccountNumberGeneratorApi](#AccountNumberGeneratorApi)
   * [AccountPrizeApi](#AccountPrizeApi)
   * [ActiveMQ](#ActiveMQ)
   * [AccountConsumer](#AccountConsumer)
   * [MongoDB](#MongoDB)
     
3. [Testing](#3-Testing)
   * [Unit Testing](#Unit-Testing)
     
4. [I Don't Understand (FAQ)](#4-I-Dont-Understand-FAQ)

5. [How To Run](#5-How-To-Run)
   * [Prerequisites](#Prerequisites)
   * [Steps](#Steps)
   
   
   
# 0. Project Definition
As a user you register for an account online for a bank. Once registered you will be assigned an account number and will be given the opportunity to claim a cash prize. Whether or not a user wins is dependent upon the account number. The size of the cash prize is also dependent upon the account number of that user.


# 1. Architecture
## High level architecture diagram

![HLD1](/HLD1.JPG)

From the diagram we can see four major components of the entire application.

**Browser**: 
* The user of the application will only see the front end, depicted in the diagram as `Browser`. More specifically they will be exposed to  `Presentation`. This of course is the HTML rendered by the browser, styled up with CSS. 
* The user will perform operations in the browser that will make REST calls to endpoints in `AccountAPI`.

**Backend Application**: 
* This is comprised of three APIs. 
* As described in the [project definition](#0-Project-Definition), the user should be assigned an account number. The `AccountNumGenAPI` will take care of generating this number. 
* The user must also know how much cash they are entitled to; this service will be taken care of by the `AccountPrizeAPI`.
* We do not want the Browser to make calls to multiple APIs. Instead we want it to make calls to a main API which will then make the appropriate calls to AccountNumGenAPI and AccountPrizeAPI. This main API will be `AccountAPI`.

The process:

The user will click register account and a call will be made to AccountAPI. AccountAPI must return an account number and so makes a call to AccountNumGenAPI which generates the number and sends it back. 

The AccountAPI must now check if the user is entitled to a cash prize and so makes a call to the AccountPrizeAPI which runs a check and returns a cash value. 

The AccountAPI now has all the information it needs and returns the data (account number and prize) to the browser while also saving this information to the local database.

But we also want to store the data to a longterm database. So we send off the data to the `Queue`.

```
For Dummies:
Consider each API as a different person with different specialties. 
One API may know about prices and another may know about phones.
As a customer, if you wanted advice on phones you would ask the phone person.
But the phone person doesn't know about prices! So they would ask the prices person for help.
The phone person can then tell the customer what they know about phones.

In this case, the customer acts as the browser, making a request to an external API for phone advice. 
```


**Queue**:
* The queue is used for communication between two applications. In this case it, the AccountAPI wants to talk to the `JMS Consumer`, since it wants to store data in the longterm database. The queue picks up data from the AccountAPI and waits for the `JMS Consumer` to take the data.


```
For Dummies: 
Imagine this as a factory conveyor belt. 
A worker places a chocolate bar on the conveyor belt. 
Another worker is waiting at the conveyor belt for chocolate bars because it is their job to store chocolate bars away. 
The worker is constantly checking the conveyor belt for the chocolate until they see one. 
Once they do, they grab it, store it away and go back to the conveyor belt.

In this sense, the AccountAPI is the worker placing chocolate on the belt, 
the Queue is the conveyor belt, and the JMS Consumer is the worker taking chocolate off the belt.
```

**JMS Consumer**:
* This API constantly checks if the `Queue` has any data that it is allowed to receive. If it does, it will take take the data off of the queue. 
* Once off the queue, this API will persist the data to the database.


## The Stack
* **React**: This is a javascript library for making user interfaces. The front end of this project is a single paged application built with React. More information on React can be found [here](https://stories.jotform.com/7-reasons-why-you-should-use-react-ad420c634247)

* **Springboot**: Built on top of the Spring framework, which uses dependency injection to build decoupled systems. Much quicker and easier to deploy compared to standard web based applications. SpringBoot will be used for each of our APIs in `Backend Application`.
More information on SpringBoot can be found [here](https://www.zoltanraffai.com/blog/what-is-spring-boot/) and [here](https://stackoverflow.com/questions/1061717/what-exactly-is-spring-framework-for)

* **ActiveMQ**: An open sourced implementation of JMS. Used for sending messages between applications. ActiveMQ is what we will use for the `Queue` in the diagram above. More information on ActiveMQ and JMS can be found [here](http://blog.christianposta.com/activemq/what-is-activemq/) and [here](https://www.javatpoint.com/jms-tutorial)

* **MongoDB**: A NoSQL database. Very flexible and not as stringent as SQL databases. Described by some as a 'sexy dustbin'. More information on Mongo can be found [here](https://www.tutorialspoint.com/mongodb/mongodb_overview.htm)


## Springboot API Structure
![HLD2](/HLD2.JPG)

Each API we have will follow this structure with slight variations depending on the purpose of the API.


* Rest:

This layer exposes the applications RESTful API. The endpoints contained in this layer are responsibile for accepting requests for a resource and delegating the processing of that request to the Service layer. 

* Service:

Any business rules required will be set in this layer e.g. Block all requests for accountId:1.

* Persistence:

This layer has two components. 
The Domain part holds the classes of the objects you will work with. 
The repo part is abstracted from us, any details of the persistence mechanism is hidden away. We do not write any code to store/alter data in the database .
The repo uses JpaRepository which persists to a relational H2 database, using the [JPA](https://en.wikibooks.org/wiki/Java_Persistence/What_is_JPA%3F) interface with a [Hibernate](https://en.wikipedia.org/wiki/Hibernate_(framework)) implementation.


# 2. The Micro-Services

## React Front End

* Using react we have created a single page application that dynamically renders the page.

* We have included [routing](https://reacttraining.com/react-router/core/guides/philosophy) using the react-router-dom package so that routes are rendered as the app is rendering

* Most importantly, we use the axios package to make [HTTP requests](https://www.tutorialspoint.com/http/http_requests.htm) to the AccountApi. More information on the axios package can be found [here](https://alligator.io/react/axios-react/)

## AccountApi

* This API follows the Springboot API structure above.
  
* Some notable features:
  - **Rest**: 
  One of our endpoints in this layer creates an account.
  To create an account, we must make calls to our other APIs for the account number and prize! 
  These calls are made in this layer.
  In addition to this, our rest layer performs another special function; when an account is created, it is sent to the Queue.
  - **Persistence**: 
  When an account is created/updated/deleted, changes must be made to our in-memory database, that is our H2 database, all made possible with our fancy JpaRepository interface.

  
## AccountNumberGeneratorApi
   * A very simple API that generates a 6, 8 or 10 digit account number preceeded with a random account type (A, B or C), e.g. A:123456
   * The rest layer of this API has an endpoint that calls the utility class (a class made for performing operations, not to be instantiated) that generates the account number.


## AccountPrizeApi
   - The rest layer of this API has an endpoint that requires input. This means that whatever is calling this API must POST data to the endpoint.
   - The input it requires is an account number. This is because this API must check the account number and return a cash prize amount.
   - Different account types are worth different amounts, and a longer account number means more cash.

## ActiveMQ
As stated before, this is the service that will handle communication between our AccountApi and our AccountConsumer.

This mciro-serivce is not quite visible to us. It sits quietly on localhost:3306.

## AccountConsumer
This API listens to the queue on localhost:3306 and waits until there is data it can take.
It then picks the data and persists it to MongoDB.

## MongoDB
This is a seperate service running on localhost:2701.


# 3. Testing

## Unit Testing
   * Unit testing involves breaking the program down into very small pieces that we can test. More about unit testing [here](http://softwaretestingfundamentals.com/unit-testing/)
   * For our unit tests we used **Mockito**.
   * **Mockito** - Mockito is a library for effective unit testing. More about mockito [here](https://www.tutorialspoint.com/mockito/index.htm)



# 4. I Don't Understand (FAQ)

1. Why is everything seperated? Why not just one application that does it all? Why are you making things difficult?
> 
> We are using a Micro-Service approach as opposed to a Monolithic one because we want things modular. 
> 
> Modularity means it is easier to fix things if they are broken. It means you can swap parts in and out to improve the system.
> This kind of modularity can be seen when building a PC. 
> If you want to swap out the graphics card, you are able to do so. That graphics card sounds like a micro-service!
> 
> But if you have an Apple product then you have yourself a monolithic architecture ~~and a bad one that is~~ 
> 
> More on [Monolithic vs MS](https://articles.microservices.com/monolithic-vs-microservices-architecture-5c4848858f59)


2. Why doesn't AccountAPI just send data directly to JMS Consumer?

> We want loosely coupled components! This goes back to the Monolithic vs MS argument.
> 
> If we wanted to swap out JMS Consumer for a different consumer then the application wouldn't work as it should.
> But with the Queue inbetween handling the communication, JMS Consumer can be swapped around. 
> The queue will simply hold onto the data until a JMS Consumer is back and ready to take data.

3. What is the difference between a JMS Consumer and JMS Listener?
> They both mean the same thing
> The act of listening and consuming both require input. 
> In real life context, someone needs to speak for you to listen, and food must be present for you to consume.
> They both take some sort of input
> In programming context either term can be used to describe the process of dequeueing (taking data from the queue)




4. I still don't understand

> Consult the [all knowing](https://google.com)




# 5. How To Run

## Prerequisites:
* [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://www.mkyong.com/maven/how-to-install-maven-in-windows/)
* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [Node.js](https://nodejs.org/en/download/)
* [ActiveMQ](https://activemq.apache.org/getting-started.html)
* [MongoDB](https://docs.mongodb.com/manual/installation/)

## Steps:
1. Make a new directory to work inside
2. Create a .bat file with the contents:
```
@echo off
git clone https://github.com/alvinjo/AccountFront.git
git clone https://github.com/alvinjo/RealAccountApi.git
git clone https://github.com/alvinjo/RealAccountNumGenApi.git
git clone https://github.com/alvinjo/RealAccountPrizeApi.git
git clone https://github.com/alvinjo/RealAccountConsumer.git
cd AccountFront
npm install
cd ..
```
3. Run the .bat file you created
4. Start the ActiveMQ service

Do this by locating the directory where you have extracted the activemq files. Open command prompt and navigate to the 'bin' folder within the activemq files. Run the command 'activemq start'. The activemq process should start. You can view the queue on localhost:8161/admin/queues.jsp. The default username and password is 'admin'. 

5. Start MongoDB

Do this by first locating the directory where mongodb is installed. The path will look similar to this *'..\MongoDB\Server\4.0\bin\'*. Open command prompt, navigate to this directory and run the mongo.exe file by typing 'mongo'. Once you hit enter, the mongo service will be running.

6. Create another .bat file with the contents:
```
cd RealAccountApi
start mvn spring-boot:run

cd ..
cd RealAccountConsumer
start mvn spring-boot:run

cd ..
cd RealAccountPrizeApi
start mvn spring-boot:run

cd ..
cd RealAccountNumGenApi
start mvn spring-boot:run

cd ..
cd AccountFront
npm start
```
7. Run the .bat file

8. The application should be running. Navigate to localhost:3000


