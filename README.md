This projects is using maven, to run the app from commandline
1. Run mvn clean install
2. java -jar target/assignment-1.0-SNAPSHOT.jar

or from Intellij run ProductApplication.java

Assumptions:
    1. Price.now
    This field from backend API can be String or Object, if it is String I have used the value directly if Object then I have used 'to' value
    
Improvements:
    Validation of input parameters CategoryId and LabelType can be improved using javax.validation package and relevant generating bad request response.

    
Have added swagger for some basic API documentation accessible through http://localhost:8080/swagger-ui.html#/ once the app is started    
