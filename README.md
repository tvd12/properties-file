[![Build Status](https://travis-ci.org/tvd12/properties-file.svg?branch=master)](https://travis-ci.org/tvd12/properties-file)
[![Coverage Status](https://coveralls.io/repos/github/tvd12/properties-file/badge.svg?branch=master)](https://coveralls.io/github/tvd12/properties-file?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file)
[![Javadocs](https://www.javadoc.io/badge/com.tvd12/properties-file.svg)](https://www.javadoc.io/doc/com.tvd12/properties-file)

# Synopsis

This project support for interact with properties file

# Code Example

**1. Read properties file**

```java
Properties properties = new BaseFileReader()
	.read("application.properties");
```

**2. Read YAML file**


```java
Properties yamlProperties = new BaseFileReader()
	.read("application.yaml");
```

**3. Read properties file with profiles**

```java
Properties propertiesAlpha = new MultiFileReader("alpha")
	.read("application.properties");
```

**4. Read YAML file with profiles**

```java
Properties propertiesAlpha = new MultiFileReader("alpha")
	.read("application.yaml");
```

**5. Map properties or YAML file to POJO**

```java
ApplicationConfig applicationConfigYaml = new PropertiesMapper()
    .reader(new MultiFileReader("alpha"))
    .file("application.yaml")
    .map(ApplicationConfig.class);
```

**6. Use annotation to map**

```java
public class Config {
	@Property("n")
	private String name;
    	
    	@Property("a")
    	private int age;
    	
    	@Property("m")
	private long money = 10;
}
```

**7. Use variable in properties file**

```properties
app.hello=world
app.hi=${app.hello}
```

**8. Use variable in yaml file**

```yaml
app.hello: world
app.hi: ${app.hello}
```

# Motivation

Proprties and YAML are using in a lot of framework and application, so we want to create a library support to read `.propertes` and `YAML` file and map them to `POJO` if you want

# Installation

```xml
<dependency>
	<groupId>com.tvd12</groupId>
	<artifactId>properties-file</artifactId>
	<version>1.1.2</version>
</dependency>
```
# API Reference

http://www.javadoc.io/doc/com.tvd12/properties-file

# Tests

mvn test

# Contact us

- Touch us on [Facebook](https://www.facebook.com/youngmonkeys.org)
- Ask us on [stackask.com](https://stackask.com)
- Email to me [Dzung](mailto:itprono3@gmail.com)

# License

- Apache License, Version 2.0
