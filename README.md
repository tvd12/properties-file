[![Build Status](https://travis-ci.org/tvd12/properties-file.svg?branch=master)](https://travis-ci.org/tvd12/properties-file)
[![Coverage Status](https://coveralls.io/repos/github/tvd12/properties-file/badge.svg?branch=master)](https://coveralls.io/github/tvd12/properties-file?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file)
[![Javadocs](https://www.javadoc.io/badge/com.tvd12/properties-file.svg)](https://www.javadoc.io/doc/com.tvd12/properties-file)

# Synopsis

This project support for interact with properties file

# Code Example

**1. Map properties file to object**

Let's say you have a properties file "room-config.properties":

capacity = 1000
id = 1001
maxRoomVariablesAllowed = 30
maxSpectators = 50

and you want to map it to RoomConfig object, you can do:

```java
	RoomConfig room = new PropertiesMapper()
		.file("room-config.properties")
		.map(ExampleRoom.class);
```

**2. Convert an object to a map**

Sometimes you want to convert an object to a map, you can do:

```java
	PropertiesBean map = new PropertiesBean(ClassA.class);
```
or
```java
	PropertiesBean map = new PropertiesBean(new ClassA());
```

**3. Use annotation to map properties file to object or convert an object to a map**

Sometimes you have properties files with list of key and value, but key name is not same with field name, to map them, you can do:

```java
@PropertyWrapper
public class RoomConfig {
    @Setter @Getter
    protected int variablesCount;
    @Setter @Getter
    @Property("public")
    protected boolean isPublic;
    // your code
```

# Motivation

Because sometimes we want to make loose coupling source code, we want to use factory design pattern, we want to map a properties file to object, convert an object to map, so I make this project for that mean

# Installation

```xml
	<dependency>
		<groupId>com.tvd12</groupId>
		<artifactId>properties-file</artifactId>
		<version>1.1.0</version>
	</dependency>
```
# API Reference

http://www.javadoc.io/doc/com.tvd12/properties-file

# Tests

mvn test

# Contributors

None

# License

- Apache License, Version 2.0
	


