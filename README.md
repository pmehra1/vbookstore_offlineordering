# vbookstore_offlineordering
Application accepting offline orders by customers of Vbookstore

The input for the orders can come in 3 forms: XML, CSV and Excel. The Orders module receives the input and converts it to a cannonical xml.
The Billing module receives the cannonical xml as input over JMS, calculates the invoice for the order and generates an XML response and an email message body.

(The Ordering module has all the input files in the data folder)

#Running the application

Note: Maven is needed to package the application to an executable jar file. Java should also be installed to run the application. 

1. Clone or download the project.
2. Go to the module vbookstore-ordering. Open a command prompt.
3. Type mvn package and enter. It will create an executable jar vbookstore-offline-ordering-{project version}.jar under vbookstore-ordering folder.
4. Type java -jar vbookstore-offline-ordering-{project version}.jar. The application runs, displaying the 3 inputs received, the cannonical xml and the email body generated.


