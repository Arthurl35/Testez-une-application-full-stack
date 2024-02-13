# Projet Yoga-App
Creation of [Front/Back/e2e] tests for a yoga booking application.

## Start the project

Git clone:

> https://github.com/OpenClassrooms-Student-Center/Testez-une-application-full-stack

Database:

Install MySQL on your machine.
Run the SQL script to create the database schema. The script is available at ressources/sql/script.sql.

## Front

The application's front-end is developed in Angular.

Go to front folder:

> cd /front

Install dependencies:

> npm install

Launch Front-end:

> npm run start;

### Test

Do the tests with:

> npm run test;

Do the e2e tests with:

> npm run e2e;

### Coverage

Jest:

> node_modules/.bin/jest --coverage;

E2E:

> npm run e2e:ci;
> npm run e2e:coverage;

## Back

The application's back-end is developed in Java Spring.

Go to back folder:

> cd /back

Do the tests with:

> mvn clean test;

