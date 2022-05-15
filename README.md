# Transaction Reconciliator
![Build_Status](https://img.shields.io/badge/build-passing-brightgreen)
![Test_Coverage](https://img.shields.io/badge/coverage-93%25-brightgreen)

A Spring Boot application containing 3 RESTful APIs that is able to generate a match count, unmatch count, and potential matches from two CSV files.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
- [MySQL](https://www.mysql.com/) - When building, it uses an in memory database [H2](https://www.h2database.com/html/main.html). On the other hand, when running the actual application, it now uses a MySQL instance.

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.caponong.transactionreconciliator.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

If mysql instance is an issue for locally running the application, then just copy and replace the 
mysql datasource config into h2 config found in:
```shell
/src/test/resources/application.properties
```

Just take not that this is not recommended as this will most likely consume the memory of the server. 

## Design
   
1. Receiving files:
   1. Asynchronous - Once the files are received, the API generates a `reconciliationToken` to be used for
   matching and unmatching APIs. This is returned to the client instantly as the process of saving to the 
     database is asynchronous. Part of the response is the `tokenExpiry` (configurable), once the token is expired,
     the client has to upload again to generate a new `reconciliationToken`.
     
   2. Thread safe - Once the API received the file in the form of `MultipartFile`, the API will save a copy
   to `java.io.tmpdir` and uses this copy as reference instead of the one from client. This is to prevent a
     race condition between the asynchronous writing to database and java removing the `MultipartFile` reference 
     as we enter the async methods. This way, we are preventing intermittent `FileNotFoundException` error. Lastly,
     once the `reconciliationToken` is expired, the temporary files are deleted together with the database items associated
     with the `reconciliationToken`.
     
   3. Memory efficient - Instead of loading the whole file straight to memory, the file is streamed and read
   one by one. Items will be chunked depending on the `chunkSize` (configurable). Each chunk is saved to the database.
     This way, we are making sure that our application will not crash(`OutOfMemoryError`) when a large file is uploaded.
     
   
2. Matching - The matching mechanism is accomplished by taking advantage of the RDBM's `inner join`. Both transaction set is saved to the same table together with their 
   identifier (`a_` + `reconciliationToken` or `b_` + `reconciliationToken`)
whether they belong to the `firstTransactionSet` or `secondTransactionSet`. Below is an illustration of how the `inner join` works 
   to get the match from file `A` and `B`
   
   <p align="left">
     <img src="https://jcap.pro/files/innerjoin.png">
   </p>
   
3. Potential Matching - Potential match is done in two steps: 
   1. Get the unmatched items in file `A`. This is accomplished by using the RDBM's `left exclusive join`. Where we take the
   items in `A` that is not found in `B`. This goes both ways, for file `B`, we will then use `right exclusive join`. 
      Below is an illustration of how they work.

   ![leftexclusive](https://jcap.pro/files/leftexclusive.png)
   ![leftexclusive](https://jcap.pro/files/rightexclusive.png)
   
   2. Once we have the unmatched items, API will count the difference of each column in a given item from `A_unmatched` and `B` and vice versa. An item 
   from `B` will be considered a potential match to `A_unmatched` item if the difference count per column is less that or equal `potential-match-difference-threshold` (configurable).
      

## Configure 

Below is the whole `application.properties` file of the application.

```shell
# server
server.port=8080

# logging
logging.level.root=info

spring.datasource.url=jdbc:mysql://localhost:3306/transactionReconciliatorDb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=my-secret-pw
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

csv.transaction.headername.profile-name=ProfileName
csv.transaction.headername.transaction-date=TransactionDate
csv.transaction.headername.transaction-amount=TransactionAmount
csv.transaction.headername.transaction-narrative=TransactionNarrative
csv.transaction.headername.transaction-description=TransactionDescription
csv.transaction.headername.transaction-id=TransactionID
csv.transaction.headername.transaction-type=TransactionType
csv.transaction.headername.wallet-reference=WalletReference

# token expiry in seconds
reconciliaton-token-expiry=60

# expired token check interval in milli seconds
expired-reconciliaton-token-check-interval=30000

reconciliation-chunk-size=50

potential-match-difference-threshold=2
```

1. `reconciliaton-token-expiry` - Configure this property to change the validity duration of the `reconciliationToken`
2. `expired-reconciliaton-token-check-interval` - This is the duration where the application checks for expired `reconciliationToken`
3. `reconciliation-chunk-size` - This is the chunkSize in number of items. This applies to saving to DB and retrieving potential matches.
4. `potential-match-difference-threshold` - This is the number if difference allowed for an item to be considered potential match

## Test it out

The application can be tried in two ways: 

1. Using http client - Here's a postman collection for reference: https://jcap.pro/files/TransactionReconciliator.postman_collection.json
2. Using the front end counterpart accessible here: https://jcap.pro

## APIs

### [POST] /api/transactions/upload

Invoking this endpoint allows client to upload two sets transactions in csv format that will be checked for matches. 
###### Sample Request
```shell
curl --location --request POST 'https://jcap.pro/api/transactions/upload' \
--form 'firstTransactionSet=@"/C:/Users/ADMIN/Downloads/WorkRelated/TrialProject/ToCandidate - Paymentology/ClientMarkoffFile20140113.csv"' \
--form 'secondTransactionSet=@"/C:/Users/ADMIN/Downloads/WorkRelated/TrialProject/ToCandidate - Paymentology/PaymentologyMarkoffFile20140113.csv"'
```
###### Sample Success Response
```json
{
    "message": "success",
    "code": "0",
    "body": {
        "reconciliationToken": "6dc00804-452b-4f23-8ddc-462d756d0bed",
        "tokenExpiry": 60
    }
}
```
###### Sample Error Response
```json
{
    "message": "error",
    "code": "999",
    "errorCode": {
        "code": "001",
        "message": "Missing request part"
    }
}
```
### [GET] /api/transactions/{reconciliationToken}/matchSummary

Invoking this endpoint allows client to get the transactions match count and unmatch count from the csv files uploaded based on `reconciliationToken`
###### Sample Request
```shell
curl --location --request GET 'https://jcap.pro/api/transactions/64380c01-682c-434a-9637-253884caa634/matchSummary'
```
###### Sample Success Response
```json
{
  "message": "success",
  "code": "0",
  "body": {
    "firstTransactionSet": {
      "fileName": "ClientMarkoffFile20140113.csv",
      "totalUniqueRecords": 304,
      "matchedTransactions": 288,
      "unmatchedTransactions": 16
    },
    "secondTransactionSet": {
      "fileName": "PaymentologyMarkoffFile20140113.csv",
      "totalUniqueRecords": 304,
      "matchedTransactions": 288,
      "unmatchedTransactions": 16
    }
  }
}
```
###### Sample Error Response
```json
{
  "message": "error",
  "code": "999",
  "errorCode": {
    "code": "004",
    "message": "Requested token not yet ready"
  }
}
```

### [GET] /api/transactions/{reconciliationToken}/unmatchedTransactionsSummary

Invoking this endpoint allows client to get the unmatched transactions and its potential matches based on `reconciliationToken`
###### Sample Request
```shell
curl --location --request GET 'https://jcap.pro/api/transactions/424f79bc-e1fb-4cf0-b142-d145c093dac6/unmatchedTransactionsSummary'
```
###### Sample Success Response
```json
{
  "message": "success",
  "code": "0",
  "body": {
    "firstTransactionSet": {
      "fileName": "testDataCsvOne.csv",
      "unmatchedTransactions": []
    },
    "secondTransactionSet": {
      "fileName": "testDataCsvTwo.csv",
      "unmatchedTransactions": []
    }
  }
}
```
###### Sample Error Response
```json
{
  "message": "error",
  "code": "999",
  "errorCode": {
    "code": "004",
    "message": "Requested token not yet ready"
  }
}
```

## Error Code

|   HTTP Status Code    |   Status  |   Code        |   Description                                                                    
|   ----------------    |   ------  |   --------    |   -----------------------------------------                                      
|   500                 |   error   |   999         |   Internal Server Error                                                                 
|   400                 |   error   |   001         |   Missing request part                                                         
|   200                 |   error   |   002         |   File must be in .csv                                                               
|   400                 |   error   |   003         |   token should only contain numbers, letters, and - with length of 36                                                          
|   503                 |   error   |   004         |   Requested token not yet ready                                             
|   400                 |   error   |   005         |   Requested token encountered an error                                            
|   400                 |   error   |   006         |   Request not found

## Version Control

| Date        | Version | Name             | Description                                                                                                           | 
| ----------  | -----   | ---------------- | --------------------------------------------------------------------------------------------------------------------- |
| 15-May-2022 | 1.0     | Joshua Caponong  | Initial File                                                                                                          |
                                             