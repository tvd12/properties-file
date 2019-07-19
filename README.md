[![Build Status](https://travis-ci.org/tvd12/properties-file.svg?branch=master)](https://travis-ci.org/tvd12/properties-file)
[![Dependency Status](https://www.versioneye.com/user/projects/5717990efcd19a00415b1f61/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5717990efcd19a00415b1f61)
[![Coverage Status](https://coveralls.io/repos/github/tvd12/properties-file/badge.svg?branch=master)](https://coveralls.io/github/tvd12/properties-file?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tvd12/properties-file)
[![Javadocs](https://www.javadoc.io/badge/com.tvd12/properties-file.svg)](https://www.javadoc.io/doc/com.tvd12/properties-file)

# Synopsis

This project support for interact with properties file

# Code Example

**1. Get all classes're mapped to keys in properties file**
```java
	try {
		Map<Object, Class<?>> classes = new ClassFetcher.Builder()
			.context(getClass())
			.file(PROPERTIES_FILE_PATH)
			.reader(new BaseFileReader()) //or Base64FileReader
			.build()
			.fetch();
	} catch (PropertiesFileException e) {
		e.printStackTrace();
	}
```

**2. Get class mapped to key in properites file**
```java
	try {
		Class<User> = new ClassFetcher.Builder()
			.context(getClass())
			.file(PROPERTIES_FILE_PATH)
			.reader(new BaseFileReader()) //or Base64FileReader
			.build()
			.fetch("key name");
	} catch (PropertiesFileException e) {
		e.printStackTrace();
	}
```

**3. Create a factory and use this factory for creating a new instance**

Let's say you have a properties file that save pairs of interface and implementation class, example:
```java
	com.tvd12.interface.IClassA=com.tvd12.implementation.ClassAImpl
	com.tvd12.interface.IClassB=com.tvd12.implementation.ClassBImpl
```
and you want to create an instance of interface, you can do:
Step 1: Init a factory, example:
```java
	InstanceFactory.defaultInstance().initialize("array of properties files");
```
Step 2: Use the factory to create interface's instance from implementation class, example:
```java
	IClassA instance = InstanceFactory.defaultInstance().getPrototype(IClassA.class);
```
Step 3: (Optional) If you want to create singleton instance of interface, you can do, example:
```java
	IClassA instance = InstanceFactory.defaultInstance().getSingleton(IClassA.class);
```

**4. Map properties file to object**

Let's say you have a properties file "room-config.properties":

capacity = 1000
id = 1001
maxRoomVariablesAllowed = 30
maxSpectators = 50

and you want to map it to RoomConfig object, you can do:

```java
	RoomConfig room = new PropertiesMapper()
		.file("room-config.properties")
		.clazz(ExampleRoom.class)
		.reader(new BaseFileReader())
		.map();
```

and if you encode properties file with base64 you can do:

```java
	RoomConfig room = new PropertiesMapper()
		.file("room-config.properties")
		.clazz(ExampleRoom.class)
		.reader(new Base64FileReader())
		.map();
```

**4. Convert an object to a map**

Sometimes you want to convert an object to a map, you can do:

```java
	PropertiesBean map = new PropertiesBean(ClassA.class);
```
or
```java
	PropertiesBean map = new PropertiesBean(new ClassA());
```

**5. Use annotation to map properties file to object or convert an object to a map**

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
		<version>1.0.8</version>
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
	


