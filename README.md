![Build Status](https://travis-ci.org/tavandung12/properties-file.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/tavandung12/properties-file/badge.svg?branch=master)](https://coveralls.io/github/tavandung12/properties-file?branch=master)

#Synopsis

This project support for interact with properties file

#Code Example

**1. Get all classes're mapped to keys in properties file**
```java
	try {
		ClassFetcher instance = ClassFetcher.newInstance(entry, PROPERTIES_FILE_PATH);
		Map<Object, Class<?>> classes = instance.getClasses();
	} catch (PropertiesFileException e) {
		e.printStackTrace();
		throw new RuntimeException("Can not classes from properties file " 
			+ PROPERTIES_FILE_PATH, e);
	}
```

**2. Get class mapped to key in properites file**
```java
	try {
		ClassFetcher instance = ClassFetcher.newInstance(entry, PROPERTIES_FILE_PATH);
		Class<User> clazz = instance.getClass(Object key);
	} catch (PropertiesFileException e) {
		e.printStackTrace();
		throw new RuntimeException("Can not classes from properties file " 
			+ PROPERTIES_FILE_PATH, e);
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
	InstanceFactory.initialize(getClass(), array of properties files);
```
Step 2: Use the factory to create interface's instance from implementation class, example:
```java
	IClassA instance = InstanceFactory.newInstance(IClassA.class);
```
Step 3: (Optional) If you want to create singleton instance of interface, you can do, example:
```java
	IClassA instance = InstanceFactory.getSingleton(IClassA.class);
```
#Motivation

Because sometimes we want to make loose coupling source code, 
so we want to use factory design pattern, and I make this project for that mean

#Installation

```xml
	<dependency>
		<groupId>com.tvd12</groupId>
		<artifactId>properties-file</artifactId>
		<version>1.0.0</version>
	</dependency>
```
#API Reference

None

#Tests

mvn test

#Contributors

None

#License

- Apache License, Version 2.0
	


