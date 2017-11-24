# vbookstore_offlineordering
Application accepting offline orders by customers of Vbookstore

The input for the orders can come in 3 forms: XML, CSV and Excel. The Orders module receives the input and converts it to a cannonical xml.
The Billing module receives the cannonical xml as input over JMS, calculates the invoice for the order and generates an XML response and an email message body.

(The Ordering module has all the input files in the data folder)

#Running the application

Note: Maven is needed to package the application to an executable jar file. Java should also be installed to run the application. 

1. Clone or download the project.
2. Open a command prompt.
3. Go to the module vbookstore-parent. Type mvn install and enter. This will install the 4 modules: Common, Orders, Billing and Ordering. It will create an executable jar vbookstore-offline-ordering-{project version}.jar under vbookstore-ordering folder.
7. Go to the module vbookstore-ordering. Type java -jar vbookstore-offline-ordering-{project version}.jar. The application runs, displaying the 3 inputs received, the cannonical xml and the email body generated.
8. Ctrl+C to stop the application.

#Code coverage with Cobertura

Some tests have been defined in the vbookstore-orders and vbookstore-billing modules. To check for the code coverage for these tests, go to these modules and type the command mvn cobertura. The coverage report gets created inside the target/site folder.
