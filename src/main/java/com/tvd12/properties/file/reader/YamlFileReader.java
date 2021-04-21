package com.tvd12.properties.file.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.tvd12.properties.file.exception.YamlFileException;
import com.tvd12.properties.file.exception.YamlInvalidSyntaxException;

import lombok.AllArgsConstructor;

public class YamlFileReader implements InputStreamReader {

	private static final char TAB_CHAR = '\t';
	private static final char SPACE_CHAR = ' ';
	private static final Set<Character> SPACE_CHARACTERS;
	private static final String SEPARATE_CHAR = ":";
	private static final String PROPERTY_CHAIN_CHAR = ".";
	private static final String START_COMMENT_CHAR = "#";
	private static final String KEY_PATTERN = "[a-zA-Z0-9_-]+";
	private static final String DASH_CHAR = "-";
	private static final String DOUBLE_QUOTE_CHAR = "\"";
	private static final String SINGLE_QUOTE_CHAR = "\'";
	private static final String EMPTY_STRING = "";
	
	static {
		Set<Character> spaceCharacters = new HashSet<>();
		spaceCharacters.add(new Character(TAB_CHAR));
		spaceCharacters.add(new Character(SPACE_CHAR));
		SPACE_CHARACTERS = Collections.unmodifiableSet(spaceCharacters);
	}
	
	@Override
	public Properties readInputStream(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(
				new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
		try {
			try {
				return read(reader);
			}
			finally {
				reader.close();
				inputStream.close();
			}
		}
		catch (IOException e) {
			throw new YamlFileException("read yaml from input stream: " + inputStream + " error", e);
		}
	}
	
	protected Properties read(BufferedReader reader) throws IOException {
		int lineIndex = 0;
		String line = null;
		String lastParentKey = null;
		String lastPropertyKey = null;
		Properties answer = new Properties();
		TreeMap<Integer, YamlNode> nodes = new TreeMap<>();
		while((line = reader.readLine()) != null) {
			++ lineIndex;
			String lineTrim = line.trim();
			if(lineTrim.isEmpty())
				continue;
			if(lineTrim.startsWith(START_COMMENT_CHAR))
				continue;
			if(!lineTrim.contains(SEPARATE_CHAR)) {
				if(lastPropertyKey == null) {
					throw new YamlInvalidSyntaxException(
							"invalid syntax, line " + lineIndex + ": '" + line + "'");
				}
				else {
					String lastValue = answer.getProperty(lastPropertyKey, EMPTY_STRING);
					if(lineTrim.startsWith(DASH_CHAR)) {
						if(lineTrim.length() > 1) {
							String value = lineTrim.substring(1).trim();
							lastValue = lastValue.isEmpty() ? value : (lastValue + "," + value);
						}
					}
					else {
						lastValue = lastValue.isEmpty() ? lineTrim : (lastValue + " " + lineTrim);
					}
					answer.put(lastPropertyKey, lastValue.trim());
					continue;
				}
			}
			String[] keyValue = splitKeyValue(lineTrim);
			String keyTrim = keyValue[0].trim();
			if(keyTrim.isEmpty()) {
				throw new YamlInvalidSyntaxException(
						"invalid syntax, missing key, line " + lineIndex + ": " + line);
			}
			String clearKey = getClearKey(keyTrim);
			if(clearKey == null) {
				if(lastPropertyKey == null) {
					throw new YamlInvalidSyntaxException(
							"invalid syntax, invalid key: '" + keyTrim + "', line " + lineIndex + ": " + line);
				}
				String lastValue = answer.getProperty(lastPropertyKey, EMPTY_STRING);
				lastValue = lastValue.isEmpty() ? lineTrim : (lastValue + " " + lineTrim);
				answer.put(lastPropertyKey, lastValue.trim());
				continue;
			}
			int spaceCount = countStartedSpace(line);
			Entry<Integer, YamlNode> parentEntry = nodes.lowerEntry(spaceCount);
			YamlNode parentNode = null;
			if(parentEntry != null)
				parentNode = parentEntry.getValue();
			if(keyValue.length == 1) {
				if(parentNode != null)
					lastParentKey = parentNode.propertyName + PROPERTY_CHAIN_CHAR + keyTrim;
				else
					lastParentKey = clearKey;
				nodes.put(spaceCount, new YamlNode(lastParentKey));
				lastPropertyKey = lastParentKey;
			}
			else {
				if(parentNode == null)
					lastParentKey = null;
				String fullKey = clearKey;
				if(lastParentKey != null)
					fullKey = lastParentKey + PROPERTY_CHAIN_CHAR + clearKey;
				answer.put(fullKey, getClearValue(keyValue[1]));
				lastPropertyKey = fullKey;
			}
		}
		return answer;
	}
	
	private String[] splitKeyValue(String line) {
		int indexOfSeparateChar = line.indexOf(SEPARATE_CHAR);
		String key = line.substring(0, indexOfSeparateChar);
		if(indexOfSeparateChar >= line.length() - 1)
			return new String[] {key};
		String value = line.substring(indexOfSeparateChar + 1);
		return new String[] {key, value};
	}
	
	private String getClearKey(String rawKey) {
		String clearKey = rawKey;
		if(clearKey.startsWith(DASH_CHAR))
			clearKey = clearKey.substring(1).trim();
		if(clearKey.isEmpty())
			return null;
		if(!clearKey.matches(KEY_PATTERN))
			return null;
		return clearKey;
	}
	
	private String getClearValue(String rawValue) {
		String clearValue = rawValue.trim();
		int commentIndex = clearValue.indexOf(START_COMMENT_CHAR);
		if(commentIndex >= 0) {
			clearValue = clearValue.substring(0, commentIndex).trim();
		}
		if(clearValue.startsWith(DOUBLE_QUOTE_CHAR) &&
				clearValue.endsWith(DOUBLE_QUOTE_CHAR)) {
			if(clearValue.length() > 2)
				clearValue = clearValue.substring(1, clearValue.length() - 1);
			else
				return EMPTY_STRING;
		}
		else if(clearValue.startsWith(SINGLE_QUOTE_CHAR) &&
				clearValue.endsWith(SINGLE_QUOTE_CHAR)) {
			if(clearValue.length() > 2)
				clearValue = clearValue.substring(1, clearValue.length() - 1);
			else
				return EMPTY_STRING;
		}
		else if(clearValue.startsWith(">-")) {
			if(clearValue.length() > 2)
				clearValue = clearValue.substring(2).trim();
			else
				clearValue = EMPTY_STRING;
		}
		else if(clearValue.startsWith("|")) {
			if(clearValue.length() > 1)
				clearValue = clearValue.substring(1).trim();
			else
				clearValue = EMPTY_STRING;
		}
		else if(clearValue.startsWith(">")) {
			if(clearValue.length() > 1)
				clearValue = clearValue.substring(1).trim();
			else
				clearValue = EMPTY_STRING;
		}
		else {
			clearValue = clearValue.trim();
		}
		return clearValue;
	}
	
	protected int countStartedSpace(String line) {
		int count = 0;
		for(int i = 0 ; ; ++i) {
			char ch = line.charAt(i);
			if(!SPACE_CHARACTERS.contains(ch))
				break;
			if(ch == TAB_CHAR)
				count += 4;
			else
				count += 1;
		}
		return count;
	}
	
	@AllArgsConstructor
	private static class YamlNode {
		private String propertyName;
	}
	
}
