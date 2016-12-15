package com.tvd12.properties.file.reader;

import java.io.File;
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
public class FileClassFetcher extends AbstractClassFetcher {
    
    //array of properties file path to read
    protected List<File> files;
    
    protected FileClassFetcher(AbstractBuilder builder) {
        super(builder);
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#init(com.tvd12.properties.file.reader.AbstractClassFetcher.AbstractBuilder)
     */
    @Override
    protected void init(AbstractBuilder builder) {
        super.init(builder);
        Builder bd = (Builder)builder;
        this.files = bd.files;
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#loadPropertiesList()
     */
    @Override
    protected List<Properties> loadPropertiesList() {
        return reader.read(files);
    }
    
	/**
	 * Support to build ClassFetcher object
	 * 
	 * @author tavandung12
	 *
	 */
	public static class Builder extends AbstractBuilder {
	    
	    // list of properties file paths to read
	    private List<File> files = new ArrayList<>();
	    
	    /**
	     * add a file to read
	     * 
	     * @param file file to read 
	     * @return this pointer
	     */
	    public Builder file(File file) {
	        files.add(file);
	        return this;
	    }
	    
	    /**
	     * add multiple files
	     * 
	     * @param propertiesFiles files to read
	     * @return this pointer
	     */
	    public Builder files(File... propertiesFiles) {
	        files.addAll(Arrays.asList(propertiesFiles));
	        return this;
	    }
	    
	    /**
         * add multiple files
         * 
         * @param propertiesFiles files to read
         * @return this pointer
         */
        public Builder files(Collection<File> propertiesFiles) {
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
            return (Builder) super.reader(reader);
        }
	    
	    /**
	     * build a ClassFetcher object
	     * 
	     * @return ClassFetcher object
	     */
	    public FileClassFetcher build() {
	        return new FileClassFetcher(this);
	    }
	}
}
