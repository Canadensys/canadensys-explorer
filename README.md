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
* [Spring Framework 3.1](http://www.springsource.org/spring-framework)
* [Canadensys Data Access 1.1](https://github.com/Canadensys/canadensys-data-access)
* [Hibernate 4.0](http://www.hibernate.org/)
* [Freemarker 2.3.18](http://freemarker.sourceforge.net/)

* [H2 Database 1.3.163](http://www.h2database.com) (for unit testing only)

Build
-----
```
mvn clean package
```

Tests
-----
Unit tests

```
mvn test
```