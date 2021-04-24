package com.tvd12.properties.file.reader;

import static com.tvd12.properties.file.util.FileUtil.getFileClasspathByProfile;
import static com.tvd12.properties.file.util.FileUtil.getFileSytemByProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.exception.PropertiesFileException;

public class MultiFileReader extends BaseFileReader {

	private final List<String> includeProfiles = new ArrayList<>();
	
	public MultiFileReader(String includeProfiles) {
		this(getIncludeProfiles(includeProfiles));
	}
	
	public MultiFileReader(List<String> includeProfiles) {
		this.includeProfiles.addAll(includeProfiles);
	}
	
	@Override
	public Properties read(ClassLoader classLoader, String filePath) {
		Properties answer = read(classLoader, filePath, new HashSet<>());
		answer.remove(Constants.PROFILES_KEY);
		return answer;
	}
	
	private Properties read(
			ClassLoader classLoader,
			String filePath, Set<String> passedFilePaths) {
		List<String> filePaths = new ArrayList<>();
		filePaths.add(filePath);
		for(String profile : includeProfiles) {
			filePaths.add(getFileClasspathByProfile(filePath, profile));
		}
		filePaths.removeAll(passedFilePaths);
		Properties properties = new Properties();
		for(String fp : filePaths) {
			if(passedFilePaths.contains(fp))
				continue;
			Properties prop;
			try {
				prop = super.read(classLoader, fp);
			}
			catch (PropertiesFileException e) {
				continue;
			}
			passedFilePaths.add(fp);
			properties.putAll(prop);
			String profilesString = prop.getProperty(Constants.PROFILES_KEY);
			List<String> profiles = getIncludeProfiles(profilesString);
			if(profiles.size() > 0) {
				properties.putAll(
					new MultiFileReader(profiles).read(
						classLoader, filePath, passedFilePaths
					)
				);
			}
		}
		return properties;
	}
	
	@Override
	public Properties read(File file) {
		Properties answer = read(file, new HashSet<>());
		answer.remove(Constants.PROFILES_KEY);
		return answer;
	}
	
	private Properties read(File file, Set<File> passedFiles) {
		List<File> files = new ArrayList<>();
		files.add(file);
		for(String profile : includeProfiles) {
			files.add(getFileSytemByProfile(file, profile));
		}
		files.removeAll(passedFiles);
		Properties properties = new Properties();
		for(File f : files) {
			if(passedFiles.contains(f))
				continue;
			Properties prop = super.read(f);
			passedFiles.add(f);
			properties.putAll(prop);
			String profilesString = prop.getProperty(Constants.PROFILES_KEY);
			List<String> profiles = getIncludeProfiles(profilesString);
			if(profiles.size() > 0) {
				properties.putAll(
					new MultiFileReader(profiles).read(file, passedFiles)
				);
			}
		}
		return properties;
	}
	
	private static List<String> getIncludeProfiles(String profilesString) {
		List<String> answer = new ArrayList<>();
		if(profilesString == null)
			return answer;
		String[] profiles = profilesString.split(",");
		for(String profile : profiles) {
			if(profile.trim().length() > 0)
				answer.add(profile);
		}
		return answer;
	}
	
}
