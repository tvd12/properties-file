package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.annotation.PropertyAnnotation;
import com.tvd12.properties.file.io.Dates;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.util.PropertiesUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class PropertiesMapperTest {

    @Test
    public void testMapPropertiesToBean() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(properties)
                .clazz(ClassA.class)
                .map();
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBean2() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .bean(new ClassA())
                .map();
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBeanWithMapClazz() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .map(ClassA.class);
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBeanWithFields() {
    	Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassC.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        properties.put("datasource.username", "hello");
		properties.put("datasource.password", "world");
		properties.put("cors_config-detail.enable", true);
		properties.put("cors_config-detail.host", "*");
		properties.put("corsConfigDetailEnable", true);
        
        ClassC object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .map(ClassC.class);
        assertEquals(object.name, "hello");
        assertEquals(object.age, 24);
        assertEquals(object.money, 10);
        assertEquals(object.clazz, ClassC.class);
        assertEquals(object.datasourceUsername, "hello");
        assertNotNull(object.date);
        
        assertEquals(object.dataSourceConfig.username, "hello");
        assertEquals(object.dataSourceConfig.password, "world");
        Properties dataSourceProperties = PropertiesUtil.getPropertiesByPrefix(properties, "datasource");
        assertEquals(object.dataSourceProperties, dataSourceProperties);
        System.out.println("dataSourceProperties: " + dataSourceProperties);
        
		assert object.corsConfigDetail.enable;
		assert object.corsConfigDetail.host.equals("*");
    }
    
    @Test
    public void testMapPropertiesToBeanWithAnntations() {
    	Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("value", "world");
        properties.put("age", 24);
        properties.put("clazz", ClassC.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        properties.put("datasource.username", "hello");
		properties.put("datasource.password", "world");
		properties.put("datasource.driver", "dahlia");
        
		ClassD object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .mappingLevel(MappingLevel.ANNOTATION)
                .addPropertyAnnotation(new PropertyAnnotation(
                		PropertyForTest.class, 
                		a -> ((PropertyForTest)a).value(), 
                		a -> ((PropertyForTest)a).prefix()))
                .addPropertyAnnotation(new PropertyAnnotation(
                		PropertyForTest.class, 
                		a -> ((PropertyForTest)a).value(), 
                		a -> ((PropertyForTest)a).prefix()))
                .map(ClassD.class);
        assertEquals(object.name, "hello");
        assertEquals(object.value, "value");
        assertEquals(object.age, 24);
        assertEquals(object.money, 10);
        assertEquals(object.clazz, ClassC.class);
        assertEquals(object.datasourceDriver, "dahlia");
        assertNotNull(object.date);
        
        assertEquals(object.dataSourceConfig.username, "hello");
        assertEquals(object.dataSourceConfig.password, "world");
        assertEquals(object.dataSourceConfig.driverClass, "dahlia");
        Properties dataSourceProperties = PropertiesUtil.getPropertiesByPrefix(properties, "datasource");
        assertEquals(object.dataSourceProperties, dataSourceProperties);
        System.out.println("dataSourceProperties: " + dataSourceProperties);
    }
    
    @Test
    public void testWithNoClassCase() {
    	Properties properties = new Properties();
    	Object output = new PropertiesMapper()
    		.data(properties)
    		.map();
    	assert properties == output;
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void newBeanInstanceInvalidCaseTest() {
        new PropertiesMapper()
                .data(new Properties())
                .clazz(ClassB.class)
                .map();
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void getPropertiesInvalidCaseTest() {
        new PropertiesMapper()
            .file("xyz/classes.properties")
            .reader(new BaseFileReader())
            .clazz(ClassA.class)
            .context(getClass())
            .map();
    }
    
    @Test
    public void mapBase64FileToObject() {
        new PropertiesMapper()
                .file("room-config.properties")
                .clazz(ExampleRoom.class)
                .reader(new BaseFileReader())
                .map();
    }
    
    @Test
    public void mapByConstructorTest() {
    	Properties properties = new Properties();
    	properties.put("host", "0.0.0.0");
    	properties.put("port", 3005);
    	properties.put("admin.name", "Admin");
    	ServerConfig output = new PropertiesMapper()
    		.data(properties)
    		.map(ServerConfig.class);
    	assert output.getHost().equals("0.0.0.0");
    	assert output.getPort() == 3005;
    	assert output.getAdminName().equals("Admin");
    	assert output.getAdminPassword() == null;
    }
    
    @Data
    public static class ClassA {
        private String name;
        private int age;
        private long money = 10;
        
        private Date date;
        
        private Class<?> clazz;
    }
    
    public static class ClassB {
        protected ClassB() {}
    }
    
    public static class ClassC {
        public String name;
        public int age;
        protected long money = 10;
        
        protected Date date;
        
        protected Class<?> clazz;
        
        @Property(prefix = "datasource")
        protected DataSourceConfig dataSourceConfig;
        
        @Property(prefix = "datasource")
        protected Properties dataSourceProperties;
        
        protected Cors corsConfigDetail;
        
        @Property(prefix = "cors.config.detail.enable")
        protected boolean corsConfigDetailEnable;
        
        protected String datasourceUsername;
    }
    
    public static class ClassD extends ClassDBase implements IClassD {
    	@Property
        protected long money = 10;
        
    	@Property
        protected Date date;
        
    	@Property
        protected Class<?> clazz;
        
    	@Setter
        protected DataSourceConfig dataSourceConfig;
        
    	@Setter
        protected Properties dataSourceProperties;
    	
    	@Property
    	protected String datasourceDriver;
    }
    
    public static class ClassDBase {
    	@Property
        public String name;
    	@Property
        public int age;
    	@Property
    	public final String value = "value";
    }
    
    public static interface IClassD extends IIClassD {
    	
        @Property(prefix = "datasource")
        public void setDataSourceConfig(DataSourceConfig dataSourceConfig);

        @Property(prefix = "datasource")
        public void setDataSourceProperties(Properties dataSourceProperties);
    }
    
    public static interface IIClassD {
    	
        @Property(prefix = "datasource")
        public void setDataSourceConfig(DataSourceConfig dataSourceConfig);

        @Property(prefix = "datasource")
        public void setDataSourceProperties(Properties dataSourceProperties);
    }
    
	public static class DataSourceConfig {
		protected String username;
		protected String password;
		@PropertyForTest("driver")
		protected String driverClass;
	}
	
	public static class Cors {
		protected boolean enable;
		protected String host;
	}
	
	@Getter
	public static class ServerConfig {
		public static String DEFAULT1 = "1";
		private String host;
		private int port;
		private String adminName;
		private String adminPassword;
		public static String DEFAULT2 = "2";
		
		public ServerConfig(
			String host,
			int port,
			String adminName,
			String adminPassword,
			boolean booleanValue,
			byte byteValue,
			char charValue,
			double doubleValue,
			float floatValue,
			int intValue,
			long longValue,
			short shortValue,
			Boolean wrapperBooleanValue
		) {
			this.host = host;
			this.port = port;
			this.adminName = adminName;
			this.adminPassword = adminPassword;
		}
	}
}
