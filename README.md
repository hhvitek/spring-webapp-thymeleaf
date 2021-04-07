# Spring-webapp-thymeleaf

This application implements 3 JPA entities. 
Application allows three direct operations above those 3 entities:
* creating a new entity
* editing already existing one
* removing existing entity

There is a Vehicle and a Person. Single person can drive one vehicle. Let's call this Person a Driver.
Three entities: Vehicle, Person, Driver. Driver is a Person, and a Driver drives a Vehicle.

The application is performing basic validations. Such as if requested to add a new entity and this new entity,
has its own ID, the application checks if this ID is not already in database...

Front-end htmls are produced dynamically by Spring with Thymeleaf templating system. A little bit of JQuery and Bootstrap
is also used for css like styling purposes....


## Requirements

1. Java JDK 11 is required.
2. Apache Tomcat 8 binaries required. See [tomcat.apache.org](https://tomcat.apache.org/download-80.cgi)
3. set up environment variables CATALINA_HOME and JAVA_HOME
4. Maven to build an app

https://www.decodejava.com/how-to-set-environment-variables-for-tomcat.htm

4. Set up IDE with webserver


## Database

A remote setup can be done using postgreSQL
A local setup using Sqlite is also an option.

Database configuration files are located in a folder /resources/db/<dev|prod>/.

* create-schema.sql used to create a database schema in a PROD environment.
* insert-data.sql loads initial data into a database created by the create-schema.sql script.

Database schema in DEV environment is fully created by Hibernate.

<dev|prod> folders contain db.properties file that includes basic DB configuration.
This configuration is passed into application spring-config-servlet.xml file. This
file describes basic spring XML based configuration.


## Spring configuration

The Spring is configured both in XML and Java Configuration classes.

XML file is located in the /resources/spring/spring-config-servlet.xml folder.
* The DB config is predominantly placed in there.

Additional Java-based configurations are in MainApplication.class, and a java package called configurations.
This is a place to configure things such as:
* Spring and Thymeleaf template system
* MessageSource internalization + URL based parameter ...?lang=cs_CZ to change locale if an application frontend


## Install and Set up database postgres

Some commands that could help on Ubuntu like system using docker:

* sudo apt install docker.io
* docker --version
* sudo groupadd docker  
* sudo usermod -aG docker $USE
  
* docker pull postgres  
* docker run --name postgresdb -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres

* docker container ls
* docker ps (-a)

* docker start/stop/restart <docker id hash> a3b9

* docker exec -it a3b9 bash
    * apt-get update
    * apt-get install vim
    * find / -name "pg_hba.conf" 
    * vim /var/lib/postgresql/data/pg_hba.conf
        * host all all 192.168.0.0/16 md5

* psql -U postgresdb


## DB configuration

/resources/db/dev/
contains configuration files for a sqlite db

/resources/db/prod/
contains configuration files for a postgresql db

To switch between profiles change variable in web.xml file (/webapp/web.xml)
```xml
<context-param>
    <param-name>spring.profiles.active</param-name>
    <param-value>dev</param-value>
</context-param>
```

Based on the spring.profiles.active variable one of the previous folders is chosen.

db.properties is loaded into spring xml configuration file as a property file. The file contains
database related configurations.
```xml
<context:property-placeholder location="classpath:db/${spring.profiles.active}/db.properties"/>
```

Also, db initialization scripts may be executed on application start.

To completely refresh DB at each app start use:
```properties
hibernate.hbm2ddl.auto=create

db.initialize=true
db.create.schema=classpath:db/prod/create-schema.sql
db.insert.data=classpath:db/prod/insert-data.sql
```

To persist data into DB use:
```properties
hibernate.hbm2ddl.auto=none

db.initialize=false
db.create.schema=classpath:db/prod/create-schema.sql
db.insert.data=classpath:db/prod/insert-data.sql
```


## Build/Run application

Use Maven to build the app. Maven will generate web-app.war file located in a /target directory
```
mvn clean
mvn package
```

Move/Copy this .war file into your Tomcat /webapps directory "%CATALINA_HOME%/webapps"
```
On windows:
copy /Y target\web-app.war %CATALINA_HOME%\webapps
```

Start tomcat
```
On windows
%CATALINA_HOME%\bin\startup.bat
```

Follow the link:
```
http://localhost:8080/web-app/
```


## Developer bullet points

### DB + Hikari connection pool
```xml
<bean id="dataSourceHikari"
      class="com.zaxxer.hikari.HikariDataSource">
    <property name="driverClassName" value="${hikari.driverClassName}"/>
    <property name="jdbcUrl" value="${hikari.jdbcUrl}"/>
    <property name="username" value="${hikari.username}"/>
    <property name="password" value="${hikari.password}"/>
    <property name="connectionTestQuery" value="SELECT 1;"/>
    <property name="maximumPoolSize" value="5"/>
    <property name="loginTimeout" value="1"/>
    <property name="connectionTimeout" value="1000"/>
    <property name="initializationFailTimeout" value="-1"/>
</bean>

<jdbc:initialize-database data-source="dataSourceHikari" enabled="${db.initialize}">
    <jdbc:script encoding="utf-8" location="classpath:db/${spring.profiles.active}/create-schema.sql"/>
    <jdbc:script encoding="utf-8" location="classpath:db/${spring.profiles.active}/insert-data.sql"/>
</jdbc:initialize-database>
```

### Internalization MessageSource
```java
@Bean
public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("/WEB-INF/messages/messages");
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
}


@Bean
public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver=new SessionLocaleResolver(); //or CookieLocaleResolver
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
}

@Bean
public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
}
    
```

The following must be added into Configuration file for the above to work properly:
```java
/**
  * Must use this insead of addInterceptors() method
  * https://stackoverflow.com/questions/41327940/internationalization-issues
  */
@Bean
public MappedInterceptor localeInterceptor() {
    return new MappedInterceptor(null, localeChangeInterceptor());
}

/**
 * This method does not work
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
} 
 */
```


### UTF-8 encoding

To ensure that URL parameters are properly translated by server-side:

```xml
web.xml file: 
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

HTML templates should contain
```html
<meta charset="UTF-8">
```

SQL initialization scripts, messageSource property files, thymeleaf templates, ... must ba handled with care
to ensure encoding is always explicitly stated.


### Validation of Form user inputs

The user input validation is performed both "syntactically" and "semantically". 

Syntactic validation is done using Entity annotations. This is something like: Does a new person have a name filled in?

Additional validations I called "semantic". Those are questions like. I'm trying to edit a person B. Is this person B
actually already stored in Database? If it is not, I cannot edit this person, I must create a new one...

If the validation fails, the spring org.springframework.ui.Model object is modified to reflect those errors. The template system Thymeleaf than
based on Model it is processing shows error messages to a User.

The very simple gist of this mechanism is as follows:

Thymeleaf expects actual object in one variable Model - let's called it "vehicle"
And the second variable representing errors in a variable called "org.springframework.validation.BindingResult.entity"
This error variable must implement BindingResult / Errors interface to be properly processed by Thymeleaf

Spring automatically handles basic validation using following method signature.
Using @ModelAttribute is required, because Thymeleaf uses by default objects ClassName not a variable name...

```java
@GetMapping
public String addOne(@Valid @ModelAttribute("entity") VehicleEntity entity, BindingResult bindingResult){
    // syntactic validations from Entity annotations    
    if(bindingResult.hasErrors()){
        //
    }

    // perform custom semantic validations, modify BindingResult if any error encountered
    if(hasSemanticErrors(entity,bindingResult)){
        //
    }

    // validation success do something
}
```

One may want to reuse BindingResult logic in a Thymyleaf. This can be achieved creating own BindingResult object
and mapping it to a existing entity instance in model object.

```java
VehicleEntity entity = new VehicleEntity();
BindingResult bindingResult = new BeanPropertyBindingResult(vehicle, "vehicle");

// modify bindingResult object according to errors such as:
// validator.validate(vehicle, bindingResult);
// bindingResult.rejectValue(...)

model.addAttribute("vehicle", vehicle);
model.addAttribute("org.springframework.validation.BindingResult.vehicle", bindingResult);

```