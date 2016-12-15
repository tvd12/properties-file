package com.tvd12.properties.file.reader;

import java.io.InputStream;
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
public class InputStreamClassFetcher extends AbstractClassFetcher {
    
    //array of properties file path to read
    protected List<InputStream> inputStreams;
    
    protected InputStreamClassFetcher(AbstractBuilder builder) {
        super(builder);
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#init(com.tvd12.properties.file.reader.AbstractClassFetcher.AbstractBuilder)
     */
    @Override
    protected void init(AbstractBuilder builder) {
        super.init(builder);
        Builder bd = (Builder)builder;
        this.inputStreams = bd.inputStreams;
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.AbstractClassFetcher#loadPropertiesList()
     */
    @Override
    protected List<Properties> loadPropertiesList() {
        return reader.loadInputStreams(inputStreams);
    }
    
	/**
	 * Support to build ClassFetcher object
	 * 
	 * @author tavandung12
	 *
	 */
	public static class Builder extends AbstractBuilder {
	    
	    // list of properties file paths to read
	    private List<InputStream> inputStreams = new ArrayList<>();
	    
	    /**
	     * add an input stream to read
	     * 
	     * @param inputStream the input stream to read 
	     * @return this pointer
	     */
	    public Builder inputStream(InputStream inputStream) {
	        inputStreams.add(inputStream);
	        return this;
	    }
	    
	    /**
	     * add multiple input streams
	     * 
	     * @param inputStreams the array of input streams to read
	     * @return this pointer
	     */
	    public Builder inputStreams(InputStream... inputStreams) {
	        this.inputStreams.addAll(Arrays.asList(inputStreams));
	        return this;
	    }
	    
	    /**
         * add multiple input streams
         * 
         * @param inputStreams the list of input streams to read
         * @return this pointer
         */
        public Builder inputStreams(Collection<InputStream> inputStreams) {
            this.inputStreams.addAll(inputStreams);
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
	    public InputStreamClassFetcher build() {
	        return new InputStreamClassFetcher(this);
	    }
	}
}
