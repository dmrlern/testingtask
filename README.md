## Installation


It is ready to use once pulled. The suggested IDE is IntelliJ Idea. Before pulling the project make sure you have:

- [OpenJDK 15](https://jdk.java.net/15/) installed,
- [Maven](https://maven.apache.org/download.cgi) installed,

Selenium drivers are inside the project source

## Executing the Tests

### From Your Local via IDE
You can use the plugins that IDEs provide to run Cucumber feature files and scenarios via mouse click. To pass command line parameters in this case, you should edit the configurations and add parameters to VM options for that test run, from your IDE. The required configuration parameters are:

- **Main Class**: `io.cucumber.core.cli.Main`
- **Glue**: `stepdefs testprojectcore.stepdefs`
- **Program arguments**(_IntelliJ Idea only_): `--plugin org.jetbrains.plugins.cucumber.java.run.CucumberJvm5SMFormatter`

When Cucumber tags aren't declared explicitly, only the tests annotated with the tag inside @CucumberOptions in src/test/java/testprojectcore/runners/RunCucumberTest.java will be run
### From Anywhere Else
The best and the universal way of executing the tests in the framework is running a **Maven command**. The required Maven command is below:


```bash
mvn test 
-Dcucumber.filter.tags=@<Cucumber Tags>
-Dconfigfilepath=<Path to Test Configuration File>
-Dbrowser=<Browser Name> 
-Denv=<Environment(e.g "Test", "Staging")>
-Dgrid=<Grid IP>
```

#### System Parameters:

- **configfilepath**: &#x1F535;`optional`. Parameter to declare the path of a test configuration file. When not declared, it uses the file under _/src/test/resources/config/config.txt_ by default
- **browser**: &#x1F535;`optional`. When not declared, it will take the value from the test configuration file. When it is not present in config file also, it will use the default browser declared in property file for web test configuration. And eventually if it is not present in property file either, it will use Chrome. Case insensitive but use one of browser names while declaring: _"Chrome", "Firefox", "Safari", "Edge", "Opera"_
- **env**: &#x1F534;`mandatory`. Environment. Case insensitive. Please be sure that the same name is used inside the test code
- **cucumber.filter.tags**: &#x1F535;`optional`. When not declared explicitly, only the tests annotated with the tag inside @CucumberOptions in _src/test/java/testprojectcore/runners/RunCucumberTest.java_ will be run
- **grid**: &#x1F535;`optional`. Selenium Grid IP(if exists)


#### An Example Run Command:
```bash
mvn test -Dcucumber.filter.tags=@wip -Dbrowser=chrome -Denv=test 
```

---


# About the Project

The project consists of two parts, core and tests. Inside core, there are things related to framework. And inside tests, there are tests related to integration testing.

## About the Test Framework
The framework is written in **Java**. It uses **JUnit** and [Cucumber](https://cucumber.io/) as runners and is executed via [Maven Surefire plugin](https://maven.apache.org/surefire/maven-surefire-plugin/). Since Cucumber is in use, you may use feature files to organize the tests, design and write the tests with **BDD** in _Given-When-Then_ style, either with declarative or imperative programming


For API testing, you can use any kind of HTTP client but the one that are already in use is [Rest-assured](https://github.com/rest-assured/rest-assured)

For UI testing, **Selenium** and **Appium** are integrated. The project encourages the usage of **Page Object Model** pattern as test design pattern and [PageFactory.initElements()](https://github.com/SeleniumHQ/selenium/wiki/PageFactory) to initialize the web elements inside page objects, so you may use POM pattern and initElements()



### Sharing the Scenario State
You can share the state. It is achieved by dependency injection of type constructor injection via [PicoContainer](http://picocontainer.com/)

### Reporting
Integrated reporting tool is: [Cucumber Reports](https://reports.cucumber.io/)

### Property Files
The framework uses a configuration file which its path can be changed from command line and several property files:
- **config.txt**: Parameters related to test execution that anyone should be able change externally
- **webconfig.properties**: Property file consists of properties related to web testing that can only be changed through the code internally
- **Other property files**: Same as above, related to the domains that the name suggests

---

## Parallel Execution
Parallel Execution is achieved via Maven Surefire Plugin. Driver instance is thread safe. To achieve thread safety while sharing test context among different classes, the easiest way is to record the value with thread id.

