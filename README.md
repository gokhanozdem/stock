# Brokage Firm Project

This project has been created for case study sample for ING Hubs Turkey. This is a primitive version of buying and selling stocks in the stock market.

Spring Boot and H2 database have been used to create this project.

# Build and run it (2 different ways)

1- Maven install

* Install maven to local machine ([How to install maven](https://maven.apache.org/install.html))
* Open terminal and run

  ```
  mvn clean install
  ```
* Run the application:
  ```
  mvn spring-boot:run
  ```

2- Intellij/Jetbrains install

* Install Intellij IDE ([How to install Intellij](https://www.jetbrains.com/idea/download/?section=windows))
* Open project to select pom.xml
* In the right, Maven logo will appear, then click it and open Maven menu
* First double click to clean then install.

  <img src="assets/image.png" width=220 height=250>

* Run the application: Pick the StockApplication and hit the play button. 
* No need to setup any environment variables, active profiles or VM options. 

# Important Notes

> Note 1: If you don't want to fill the H2 database tables with initial values, please delete/rename data.sql file in path src/main/resources.

> Note 2: Postman collection has been created and include all endpoints which has been listed in case study. You can download it from [here](https://github.com/gokhanozdem/postmanCollections) and import it to your postman.

> Endpoint lists; 
> * POST - /order/create
> * GET - /order/list/{customerId}/{yyyy-MM-dd}
> * DELETE - /order/{orderId}
> * GET - /asset/list/{assetId}
> * PUT - /order/complete/{orderId} - Bonus 2 in case study
> * GET - /asset/list/search?assetName={assetName}&size={size}&usableSize={usableSize}
