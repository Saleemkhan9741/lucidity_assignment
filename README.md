Automation Framework Overview

    Repository Name: Cart Automation
    Language: Java
    Build Tool: Maven
    Test Framework: TestNG
    Reporting Plugin: Extent Reports


How to Run the Tests

    Start the Services
    Ensure both the Cart Service and Mock Server are up and running before executing the tests.

Clone the Automation Repository

    git clone https://github.com/Saleemkhan9741/lucidity_assignment
    cd lucidity_assigment

Set Up the Environment 

    Compile the project using OpenJDK 17:
    mvn clean compile

Execute the Test Suite

    Run the following command to trigger the cart service regression tests:
    mvn clean test -DsuiteXmlFile="src/test/resources/suitefiles/cart-service.xml"

View the Report

    After execution, the Extent Report can be found at the following location:
    target/ExtentReport.html
