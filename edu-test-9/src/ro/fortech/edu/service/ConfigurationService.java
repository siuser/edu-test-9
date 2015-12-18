package ro.fortech.edu.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.controller.EvaluationRuleController;

@Stateless
public class ConfigurationService {
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	/**
	 * Save a new configuration (key, value) in an external file
	 * 
	 * @param propertiesFile
	 *            External file
	 * @param key
	 * @param value
	 */
	public void registerProperty(File propertiesFile, String key, String value) {
		System.out.println("enter method registerProperty(" + "File propertiesFile, String key, String value)");
		PropertiesConfiguration propertiesConfiguration = null;
		try {
			propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
			propertiesConfiguration.setProperty(key, value);
			propertiesConfiguration.save();

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param propertiesFile
	 *            External file
	 * @return A List of List, last one having two elements: key and value
	 */
	public List<List<String>> getConfigPropertiesAsList(File propertiesFile) {
		System.out.println("enter method getConfigKeyValueAsList()");

		// Use Apache commons for configuration
		PropertiesConfiguration propertiesConfiguration = null;

		// A List of List of pairs (key, value)
		List<List<String>> propertiesList = new ArrayList<List<String>>();

		// Another option: use a HashMap, then transform it in a Set using
		// Set<Map.Entry<K,V>>
		// Map<String,String> propertiesHashMap = new HashMap<>();
		try {
			propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
			Iterator<String> iteratorPropertiesConfiguration = propertiesConfiguration.getKeys();
			String key = null;
			String value = null;
			while (iteratorPropertiesConfiguration.hasNext()) {
				key = iteratorPropertiesConfiguration.next();
				value = propertiesConfiguration.getProperty(key).toString().replace("[", "").replace("]", "");
				List<String> elementArrayList = new ArrayList<String>();
				elementArrayList.add(key);
				elementArrayList.add(value);
				propertiesList.add(elementArrayList);
			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return propertiesList;

	}

	public Map<String, String> getConfigPropertiesAsMap(File propertiesFile) {
		//System.out.println("enter method getConfigKeyValueAsList()");

		// Use Apache commons for configuration
		PropertiesConfiguration propertiesConfiguration = null;

		// A List of List of pairs (key, value)
		// List<List<String>> propertiesList = new ArrayList<>();
		Map<String, String> propertiesMap = new HashMap<String, String>();

		// Another option: use a HashMap, then transform it in a Set using
		// Set<Map.Entry<K,V>>
		// Map<String,String> propertiesHashMap = new HashMap<>();
		try {
			propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
			Iterator<String> iteratorPropertiesConfiguration = propertiesConfiguration.getKeys();
			String key = null;
			String value = null;
			while (iteratorPropertiesConfiguration.hasNext()) {
				key = iteratorPropertiesConfiguration.next();
				value = propertiesConfiguration.getProperty(key).toString().replace("[", "").replace("]", "");
				propertiesMap.put(key, value);

			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return propertiesMap;

	}

	public List<String> getSelectedRuleIds(File propertiesFile, Map<String, Boolean> checkboxesStatus) {

		System.out.println("checkboxesStatus = " + checkboxesStatus);
		Map<String, String> allConfigurationMap = this.getConfigPropertiesAsMap(propertiesFile);
		Set<String> allKeySet = allConfigurationMap.keySet();
		System.out.println("allKeySet = " + allKeySet);
		Iterator<String> iteratorAllKeySet = allKeySet.iterator();
		
		List<String> ruleIdsList = new ArrayList<String>();
		while (iteratorAllKeySet.hasNext()) {
			String key = iteratorAllKeySet.next();
			// Boolean
			// checkboxValue=checkboxesStatus.get(iteratorAllKeySet.next());
			Boolean checkboxValue = checkboxesStatus.get(key);
			System.out.println("key= " + key + " checkboxValue= " + checkboxValue);
			if (checkboxValue) {
				// SelectOneMenu was checked
				// Might be multiple ids, separated by SEPARATOR (comma)
				String valueMultiple=allConfigurationMap.get(key);
				for(int i=0;i<valueMultiple.split(",").length;i++){
					ruleIdsList.add(valueMultiple.split(",")[i].replaceAll("\\s",""));
				}
				//ruleIdsList.add(allConfigurationMap.get(key));			
			}

		}
		
		System.out.println("ruleIdsList " + ruleIdsList);
		return ruleIdsList;

	}

}
