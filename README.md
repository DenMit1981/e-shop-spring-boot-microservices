1.Name of project: e-shop

2.Microservices:
authentication-service
goods-service
order-service
payment-service
attachment-service
comment-service
feedback-service
history-service
discovery-service
gateway-service

Authentication Service: Handles user accounts, authentication, and authorization, including kafka service
Goods Service: Manages product information, inventory, and related details
Order Service: Processes carts and orders, including kafka and email services
Payment Service: Manages order payments and receipts, including kafka and email services
Attachment Service: Manages file uploads and storage by buyers.
Comment Service: Handles order reviews and comments.
Feedback Service: Collects order feedbacks by buyers, including kafka service. 
History Service: Tracks user activity and order history
Discovery Service: Used to automatically discover and manage microservice addresses.
                   When a microservice starts, it registers with the Discovery Service, providing information about its 
                   location (such as IP address and port) and other parameters. This is done so that other microservices 
                   can find it for interaction.
Gateway Service: Acts as a single entry point for all client requests to microservices and simplifies interactions 
                 between clients and distributed services.

3.Project launch:
3.1 Using Docker
3.1.1 Create or change Dockerfile for every microservice:
FROM maven:3.8.4-openjdk-17 as BUILD
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean -DskipTests=true package
FROM openjdk:17
COPY --from=BUILD /target/*.jar /app.jar
ENTRYPOINT ["java","-jar","app.jar"]
3.1.2 Build and run docker-compose with logs:
docker compose up --build
Build and run docker-compose without logs in detached mode:
docker compose up -d --build
Run docker-compose with logs:
docker-compose up
3.1.3 Stop and remove services:
docker-compose down
3.1.4 If not enough memory to run docker-compose file, to increase Docker's memory allocation using the .wslconfig 
file when you find that it's not enough, follow these steps:
- Create or Edit the .wslconfig File:
- Open a text editor (e.g., Notepad).
- Navigate to C:\Users\<YourUsername>\ and create (or edit) a file named .wslconfig.
- Configure Memory Settings: In the .wslconfig file, add or modify the memory allocation for WSL 2: 
[wsl2]
memory=5GB  # Allocates 5GB of memory to WSL 2
- Save the .wslconfig File:
- Restart WSL 2: To apply the new memory settings, restart the WSL 2 backend. 
- Open PowerShell or Command Prompt and run:
wsl --shutdown
This will stop all WSL 2 instances. Docker Desktop will automatically restart WSL 2 with the new settings.
- Check if Changes Took Effect: After restarting WSL, you can verify if Docker is using the updated memory settings 
by running the following command:
docker info

3.2 Using Maven and Docker
3.2.1 Create or change Dockerfile for every microservice:
FROM openjdk:17
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
3.2.2 Build maven project:
mvn clean install
3.2.3 Build and run docker-compose with logs:
docker compose up --build
Build and run docker-compose without logs in detached mode:
docker compose up -d --build
Run docker-compose with logs:
docker-compose up
3.2.4 Stop and remove services:
docker-compose down
3.2.5 If not enough memory to run docker-compose file, to increase Docker's memory allocation using the .wslconfig
file when you find that it's not enough, follow these steps:
- Create or Edit the .wslconfig File:
- Open a text editor (e.g., Notepad).
- Navigate to C:\Users\<YourUsername>\ and create (or edit) a file named .wslconfig.
- Configure Memory Settings: In the .wslconfig file, add or modify the memory allocation for WSL 2:
[wsl2]
memory=5GB  # Allocates 5GB of memory to WSL 2
- Save the .wslconfig File:
- Restart WSL 2: To apply the new memory settings, restart the WSL 2 backend.
- Open PowerShell or Command Prompt and run:
wsl --shutdown
This will stop all WSL 2 instances. Docker Desktop will automatically restart WSL 2 with the new settings.
- Check if Changes Took Effect: After restarting WSL, you can verify if Docker is using the updated memory settings
by running the following command:
docker info

3.3 Using Run/Debug Configuration Intellij IDEA
3.3.1 Create Maven Run Configurations for Each Microservice:
- Open IntelliJ IDEA and load your project.
- Navigate to Run → Edit Configurations in the top menu.
- Click the "+" icon in the upper left and choose Maven.
- In the Name field, enter the name of your microservice (e.g., authentication-service).
- In the Working directory field, choose the directory of the respective microservice (e.g., authentication-service).
- In the Command line field, enter the following command to run the Spring Boot application:
clean spring-boot:run
- Click Apply and then OK.
- Repeat this for all microservices, such as order-service.
3.3.2 Create a Compound Run Configuration to run all the microservices together:
- Open Run → Edit Configurations.
- Click the "+" icon in the upper left and choose Compound.
- In the Name field, enter a descriptive name for the compound configuration: Run e-shop.
- In the Run section, click "+" to add the previously created run configurations for each microservice.
- Add the configuration for every microservice.
- Click Apply and then OK.
3.3.3 Run zookeeper, kafka, kafdrop, minio and createbuckets services using docker-compose or any sources with these services
3.3.4 Run e-shop

4.Ports of the project:
backend: http://localhost:8080
eureka: http://localhost:8761
kafdrop: http://localhost:9000
swagger:
    authentication-service: http://localhost:8080/authentication-service/swagger-ui/index.html#/
    goods-service: http://localhost:8080/goods-service/swagger-ui/index.html#/
    order-service: http://localhost:8080/order-service/swagger-ui/index.html#/
    payment-service: http://localhost:8080/payment-service/swagger-ui/index.html#/
    attachment-service: http://localhost:8080/attachment-service/swagger-ui/index.html#/
    comment-service: http://localhost:8080/comment-service/swagger-ui/index.html#/
    feedback-service: http://localhost:8080/feedback-service/swagger-ui/index.html#/
    history-service: http://localhost:8080/history-service/swagger-ui/index.html#/

5.Logins/passwords/emails/roles of users:
Den/1234/admin1_mogilev@yopmail.com/ROLE_ADMIN,
Peter/4321/peter_mogilev@yopmail.com/ROLE_BUYER,
Asya/5678/asya_mogilev@yopmail.com/ROLE_BUYER,
Jimmy/P@ssword1/admin2_mogilev@yopmail.com/ROLE_ADMIN,
Maricel/221182/maricel_mogilev@yopmail.com/ROLE_BUYER

6.Configuration of every microservice: /resources/application.yaml

7.Templates (if exist): /resources/templates

8.Liquibase migration: /resources/db.changelog

9.Minio storage: e-shop-spring-boot-microservices/data

10.Database PostgreSQL connection:
Name: dbase@localhost 
User: denmit 
Password: 1981 
Port: 5432

11.Sender's email: "denmit777@mail.ru"

12.Rest controllers:
    authentication-service:
        AuthenticationController:
            registration (POST): http://localhost:8080/api/v1/auth/signup + body;
            authentication (POST): http://localhost:8080/api/v1/auth/signin + body;
            changePassword (PATCH): http://localhost:8080/api/v1/auth/change-password + body;
        UserController:
            getById (GET): http://localhost:8080/api/v1/users/{userId};
            getAllAdmins (GET): http://localhost:8080/api/v1/users/role-admin;
    goods-service:
        ProductController:
            create (POST): http://localhost:8080/api/v1/goods + body;
            getAllUnitsForAdmin (GET): http://localhost:8080/api/v1/goods/units;
            getAllForAdmin (GET): http://localhost:8080/api/v1/goods;
            getAllForBuyer (GET): http://localhost:8080/api/v1/goods/for-buyer;
            getUnitById (GET): http://localhost:8080/api/v1/goods/units/{productId};
            getById (GET): http://localhost:8080/api/v1/goods/{categoryId};
            getByTitleAndPrice (GET): http://localhost:8080/api/v1/goods/chosen + parameters;
            update (PUT): http://localhost:8080/api/v1/goods/{productId} + body;
            delete (DELETE): http://localhost:8080/api/v1/goods/{productId};
            getTotalAmount (GET): http://localhost:8080/api/v1/goods/total;
            getProductCategoriesFromUnits (POST): http://localhost:8080/api/v1/goods/chosen/in-cart;
            getUnitForHistoryById (GET): http://localhost:8080/api/v1/goods/units/{productId}/history;
            changeProductQuantityAndTotalPrice (PUT): http://localhost:8080/api/v1/goods/for-buyer/{productId}/change;
            removeOrderedProduct (DELETE): http://localhost:8080/api/v1/goods/for-buyer/{productId};
    order-service:
        CartController:
            addProductToCart (PUT): http://localhost:8080/api/v1/carts/add-product + body;
            removeProductFromCart (PUT): http://localhost:8080/api/v1/carts/remove-product + body;
            clearCartForCancelledOrder (PUT): http://localhost:8080/api/v1/carts/clear;
        OrderController:
            create (POST): http://localhost:8080/api/v1/orders + body;
            getAll (GET): http://localhost:8080/api/v1/orders/for-admin;
            getByIdForBuyer (GET): http://localhost:8080/api/v1/orders/{orderId}/for-buyer;
            getByIdForAdmin (GET): http://localhost:8080/api/v1/orders/{orderId};
            getByIdForHistory (GET): http://localhost:8080/api/v1/orders/{orderId}/history;
            getOrderUserById (GET): http://localhost:8080/api/v1/orders/{orderId}/comment-feedback;
            checkOrderExistenceById (GET): http://localhost:8080/api/v1/orders/{orderId}/existence;
            sendFeedbackMessageToOrder (POST): http://localhost:8080/api/v1/orders/{orderId}/feedback-message;
    payment-service:
        PaymentController:
            createCardPayment (POST): http://localhost:8080/api/v1/payments/order/{orderId}/card-payment + body;
            createBankTransfer (POST): http://localhost:8080/api/v1/payments/order/{orderId}/bank-transfer + body;
            getByOrderId (GET): http://localhost:8080/api/v1/payments/order/{orderId};
            getById (GET): http://localhost:8080/api/v1/payments/{paymentId};
            getByPaymentNumber (GET): http://localhost:8080/api/v1/payments + parameter;
        ReceiptController:
            download (GET): http://localhost:8080/api/v1/receipts/download/{orderId};
    attachment-service:
        AttachmentController:
            uploadFile (POST): http://localhost:8080/api/v1/attachments/order/{orderId} + body;
            getAttachmentsByOrderId (GET): http://localhost:8080/api/v1/attachments/order/{orderId};
            download (GET): http://localhost:8080/api/v1/attachments/{attachmentId};
            getInfo (GET): http://localhost:8080/api/v1/attachments/{attachmentId}/info;
            replace (PUT): http://localhost:8080/api/v1/attachments/{attachmentId} + body;
            deleteById (DELETE): http://localhost:8080/api/v1/attachments/{attachmentId};
            deleteByName (DELETE): http://localhost:8080/api/v1/attachments/{fileName}/order/{orderId};
            getByIdForHistory (GET): http://localhost:8080/api/v1/attachments/{attachmentId}/history;
    comment-service:
        CommentController:
            add (POST): http://localhost:8080/api/v1/comments/order/{orderId} + body;
            getAllByOrderId (GET): http://localhost:8080/api/v1/comments/order/{orderId};
            getById (GET): http://localhost:8080/api/v1/comments/{commentId};
            deleteById (DELETE): http://localhost:8080/api/v1/comments/{commentId};
    feedback-service:
        FeedbackController:
            add (POST): http://localhost:8080/api/v1/feedbacks/order/{orderId} + body;
            getAllByOrderId (GET): http://localhost:8080/api/v1/feedbacks/order/{orderId};
            getById (GET): http://localhost:8080/api/v1/feedbacks/{feedbackId};
            deleteById (DELETE): http://localhost:8080/api/v1/feedbacks/{feedbackId};
            getByIdForEmailMessage (GET): http://localhost:8080/api/v1/feedbacks/{feedbackId}/mail;
    history-service:
        HistoryController:
            getAllHistoryByOrderId (GET): http://localhost:8080/api/v1/history/order/{orderId};
            saveHistoryForAddedProduct (POST): http://localhost:8080/api/v1/history/add-product/{productId};
            saveHistoryForRemovedProduct (POST): http://localhost:8080/api/v1/history/remove-product/{productId};
            saveHistoryForCreatedOrder (POST): http://localhost:8080/api/v1/history/add-product/{productId};
            saveHistoryForOrderPayment (POST): http://localhost:8080/api/v1/history/order/{orderId}/payment/{paymentNumber};
            saveHistoryForCanceledOrder (POST): http://localhost:8080/api/v1/history/cancel-order;
            saveHistoryForAttachedFile (POST): http://localhost:8080/api/v1/history/order/{orderId}/attach-file/{fileId};
            saveHistoryForReplacedFile (POST): http://localhost:8080/api/v1/history/order/{orderId}/replace-file/{fileId};
            saveHistoryForRemovedFile (POST): http://localhost:8080/api/v1/history/order/{orderId}/remove-file/{fileId};
            saveHistoryForRemovedFileByName (POST): http://localhost:8080/api/v1/history/order/{orderId}/remove-file-by-name/{fileName};
            saveHistoryForAddedComment (POST): http://localhost:8080/api/v1/history/order/{orderId}/add-comment/{commentId};
            saveHistoryForRemovedComment (POST): http://localhost:8080/api/v1/history/order/{orderId}/remove-comment/{commentId};
            saveHistoryForLeftFeedback (POST): http://localhost:8080/api/v1/history/order/{orderId}/leave-feedback/{feedbackId};
            saveHistoryForRemovedFeedback (POST): http://localhost:8080/api/v1/history/order/{orderId}/remove-feedback/{feedbackId};

13.Examples of queries in Postman and methods in Swagger: e-shop-spring-boot-microservices/screens


