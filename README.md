canadensys-explorer
===================

Canadensys data portal.

Documentation
-------------
Visit our [wiki](https://github.com/Canadensys/canadensys-explorer/wiki)

Dependencies
------------
### Softwares
* [Apache Server 2.2](http://httpd.apache.org/)
* [Tomcat 6](http://tomcat.apache.org/)
* [Postgresql 9.1](http://www.postgresql.org/)
* [Windshaft](https://github.com/Vizzuality/Windshaft)

### Libraries
* [Apache Maven 3](http://maven.apache.org/)
* [Spring Framework 3.2.6](http://www.springsource.org/spring-framework)
* [Canadensys Data Access 2.3.0](https://github.com/Canadensys/canadensys-data-access)
* [Hibernate 4.2.8](http://www.hibernate.org/)
* [Freemarker 2.3.20](http://freemarker.sourceforge.net/)
* [SiteMesh 2.5](https://github.com/sitemesh/sitemesh2/)
* [Rewrite 2.0.9](https://github.com/ocpsoft/rewrite)
* [H2 Database 1.3.175](http://www.h2database.com) (for unit testing only)
* [Selenium Client 2.39](http://docs.seleniumhq.org/download/) (for integration testing)

### Projects
* [canadensys-web-theme](https://github.com/Canadensys/canadensys-web-theme)
* [canadensys-web-core](https://github.com/Canadensys/canadensys-web-core)

Build
-----
Please follow instruction on our [wiki](https://github.com/Canadensys/canadensys-explorer/wiki).
```
gradle clean buildProduction
```

Tests
-----
Unit tests

```
gradle clean test
```
