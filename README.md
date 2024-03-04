Documentation in Progress
# eConsent Public API
QA Automation Project for eConsent Public API
REST Assured - Maven - Java - TestNG - Xray framework for API Testing

____________________________________________________________

## Install
* Java 13 or higher
* Maven
* TestNG
* Rest Assured API

____________________________________________________________

## Project Links


____________________________________________________________
## Run Test

### Regression
```
mvn clean test -Dsecret_key=*** -Dsurefire.suiteXmlFiles=src/testNG/Regression_Suite.xml -Denv=QA -Dserver=us -DtestRun=regression -Drc=true -Dxray=true -DrunID=new
```

### Partial regression
```
mvn clean test -Dsecret_key=*** -Dsurefire.suiteXmlFiles=src/testNG/testSet/GeneratePresignedUrl.xml -Denv=QA -Dserver=us -DtestRun=regression -Drc=true -Dxray=true -DrunID=new
```

### Smoke
```
mvn clean test -Dsecret_key=*** -Dsurefire.suiteXmlFiles=src/testNG/Smoke_Suite.xml -Denv=QA -Dserver=us -DtestRun=smoke -Drc=true -Dxray=true -DrunID=new
```
