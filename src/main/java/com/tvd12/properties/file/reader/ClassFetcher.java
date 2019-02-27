package com.tvd12.properties.file.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Support for reading properties file and store the data to properties object. 
 * After reading, get class object associated with the class or interface with the given string name 
 * in values of properties object
 * 
 * @author tavandung12
 *
 */
public class ClassFetcher extends AbstractClassFetcher {
    
    //which class to get resource as stream
    protected ClassLoader classLoader;
    
    //array of properties file path to read
    protected List<String> files;
    
    protected ClassFetcher(AbstractBuilder builder) {
        super(builder);
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#init(com.tvd12.properties.file.reader.AbstractClassFetcher.AbstractBuilder)
     */
    @Override
    protected void init(AbstractBuilder builder) {
        super.init(builder);
        Builder bd = (Builder)builder;
        this.classLoader = bd.classLoader;
        this.files = bd.files;
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#loadPropertiesList()
     */
    @Override
    protected List<Properties> loadPropertiesList() {
        return reader.read(classLoader, files);
    }
    
	/**
	 * Support to build ClassFetcher object
	 * 
	 * @author tavandung12
	 *
	 */
	public static class Builder extends AbstractBuilder {
	    
	    // class to get resource as stream
	    private ClassLoader classLoader;
	    
	    // list of properties file paths to read
	    private List<String> files = new ArrayList<>();
	    
	    /**
	     * set context
	     * 
	     * @param context class to get resource as stream
	     * @return this pointer
	     */
	    public Builder context(Class<?> context) {
	        this.classLoader = context.getClassLoader();
	        return this;
	    }
	    
	    /**
	     * set class loader
	     * 
	     * @param classLoader loader class loader to get resource as stream
	     * @return this pointer
	     */
	    public Builder classLoader(ClassLoader classLoader) {
	        this.classLoader = classLoader;
	        return this;
	    }
	    
	    /**
	     * add a file to read
	     * 
	     * @param file file to read 
	     * @return this pointer
	     */
	    public Builder file(String file) {
	        files.add(file);
	        return this;
	    }
	    
	    /**
	     * add multiple files
	     * 
	     * @param propertiesFiles files to read
	     * @return this pointer
	     */
	    public Builder files(String... propertiesFiles) {
	        files.addAll(Arrays.asList(propertiesFiles));
	        return this;
	    }
	    
	    /**
         * add multiple files
         * 
         * @param propertiesFiles files to read
         * @return this pointer
         */
        public Builder files(Collection<String> propertiesFiles) {
            files.addAll(propertiesFiles);
            return this;
        }
	    
	    /**
	     * set properties file reader
	     * 
	     * @param reader properties file reader
	     * @return this pointer
	     */
	    public Builder reader(FileReader reader) {
	        this.reader = reader;
	        return this;
	    }
	    
	    /**
	     * build a ClassFetcher object
	     * 
	     * @return ClassFetcher object
	     */
	    public ClassFetcher build() {
	        return new ClassFetcher(this);
	    }
	}
}
