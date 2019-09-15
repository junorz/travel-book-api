# Travel Book API
The `TravelBook` is an web application which aimed to provide a bookkeeping solution for group travelers.

This project is the back-end part of `TravelBook`. It provides APIs with RESTful design, and you could build your user interface if you wish.
The usage from cli tools like `curl` is also avaliable.

To build your user interface, it's recommended to make a reference to our offical UI project, and read through the user guide below.

# User Guide

This part introduce how to build TravelBook API into an executable jar file and run it in background.

### 1. Generate RSA key pair
The TravelBook use JWT (JavaScript Web Token) in authentication and authorization.

The public key of RSA key pair used for encrypting JWT, and private key is used for decrypting.

The following commands show how to generate a private key named `privkey.pem` and a public key named `public.key` using openssl.
```
openssl genpkey -algorithm RSA -out privkey.pem -pkeyopt rsa_keygen_bits:4096
openssl rsa -in privkey.pem -pubout -out public.pem
```

### 2. Run for first time
modify `resources/application.yml`. change database information and RSA key pair path to fit your environment, 
and `travelbook.datafixture` flag to `true`.

The `travelbook.datafixture` flag will initialize database if it set to true.

Please note that you `must` set this flag to `false` after initializing database.

If your have already do manual above, run the following command.

```
./gradlew bootRun
```

You will see the following information if your application executed succeed without any exceptions.

```
Started Application in 9.871 seconds (JVM running for 10.751)
```
And you can just press `Ctrl+C` to quit.

### 3. Build
Set `travelbook.datafixture` flag to `false`, and `travelbook.datasource.ddl` flag to `none` in file named `application.yml`.

Run the following command to build application to a jar file.

```
./gradlew build
```

You'll get a jar file located in `/build/libs` named `travel-book-api-1.0.0.jar`.

### 4. Run
```
nohup java -jar travel-book-api-1.0.0.jar 2>&1 &
```