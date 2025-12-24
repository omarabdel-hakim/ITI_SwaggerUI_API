# 🚀 SwaggerUI API Automation Project

![Java](https://img.shields.io/badge/Java-17-orange)
![RestAssured](https://img.shields.io/badge/RestAssured-5.x-green)
![TestNG](https://img.shields.io/badge/TestNG-7.x-blue)
![Allure](https://img.shields.io/badge/Allure_Report-Enabled-purple)
![Maven](https://img.shields.io/badge/Maven-Build-red)

## 📖 Overview

This project is an automated API testing framework built using **Java, TestNG, REST Assured, and Allure Reporting**.

It covers API test scenarios for **Pet, Store, and User** modules based on the Swagger Petstore API.

## 🛠️ Tools & Technologies

* **Language:** Java 17
* **Testing Framework:** TestNG
* **API Library:** REST Assured
* **Reporting:** Allure Report
* **Logging:** Log4j2
* **Build Tool:** Maven

## 📂 Project Structure

```text
src
├── main
│   └── java
│       ├── Page/            # API classes for Pet, Store, User
│       ├── Utils/           # Readers for JSON & Properties
│       ├── CustomListeners/ # TestNG Listeners
│       └── resources/
│           ├── config.properties    # Base configuration
│           └── log4j2.properties    # Logging configuration
├── test
│   └── java
│       ├── TestCases/       # Test scripts for Pet, Store, and User
│       └── TestRunner/      # Main runner including TestNG suite
└── test-output/             # Generated TestNG report, logs, and Allure results