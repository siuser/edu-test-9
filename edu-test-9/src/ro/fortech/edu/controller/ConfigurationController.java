package ro.fortech.edu.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.model.Vehicle;
import ro.fortech.edu.service.ConfigurationService;
import ro.fortech.edu.service.VehicleService;

@RequestScoped
@ManagedBean(name = "configurationController")
public class ConfigurationController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	private static final String PROPERTIES_FILE_ABSOLUTE_PATH = "E:\\Workspaces\\Websphere-5\\edu-test-9\\docs\\ruleGroupConfig.properties";
	
	FacesContext facesContext = FacesContext.getCurrentInstance();
	
	@EJB
	private VehicleService vehicleService;
	
	private Map<String, Boolean> checkboxesStatus = new HashMap<String, Boolean>(); 
	
	private String evaluationRuleGroupKey;
	private String evaluationRuleGroupValue;
	
	private Vehicle vehicle;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	private EvaluationResult evaluationResult;

	@EJB
	ConfigurationService configurationService;

	public String getEvaluationRuleGroupValue() {
		return evaluationRuleGroupValue;
	}

	public String getEvaluationRuleGroupKey() {
		return evaluationRuleGroupKey;
	}

	public void setEvaluationRuleGroupKey(String evaluationRuleGroupKey) {
		this.evaluationRuleGroupKey = evaluationRuleGroupKey;
	}

	public void setEvaluationRuleGroupValue(String evaluationRuleGroupValue) {
		this.evaluationRuleGroupValue = evaluationRuleGroupValue;
	}

	/**
	 * 
	 * @return a List of List, last one having two elements: key and value
	 * ConfigurationService will do the job
	 * 
	 */
	public List<List<String>> getConfigKeyValueAsList() {
		//System.out.println("enter method getConfigKeyValueAsList()");

		// Use an external properties file
		// Because we want it editable at runtime
		File propertiesFile = new File(PROPERTIES_FILE_ABSOLUTE_PATH);
		return configurationService.getConfigPropertiesAsList(propertiesFile);
	}
	
	/**
	 * 
	 * Save a new property (key, value)
	 * ConfigurationService will do the job
	 * 
	 */
	public void registerProperty() {
		System.out.println("enter method registerProperty()");
		
		// Use an external properties file
		// Because we want it editable at runtime
		File propertiesFile = new File(PROPERTIES_FILE_ABSOLUTE_PATH);
		configurationService.registerProperty(propertiesFile, this.evaluationRuleGroupKey,
				this.evaluationRuleGroupValue);
		this.evaluationRuleGroupKey = "";
		this.evaluationRuleGroupValue = "";
	}
	
	public String evaluate(){
		// A new Evaluation Result item every time an evaluate method call
				// (every evaluation)
		//EvaluationResult evaluationResult = new EvaluationResult();
			//	evaluationResult.setVehicle(vehicle);

		List<String> ruleIds = this.getSelectedRuleIds();
		Map<String,String> params = 
			      facesContext.getExternalContext().getRequestParameterMap();
		long vehicleId  =  new Long(params.get("vehicleId")); 
		System.out.println("vehicleId="+vehicleId);
		Vehicle vehicle=vehicleService.findById(vehicleId);
		//vehicleService.evaluate(vehicle, ruleIds); 
		this.evaluationResult = vehicleService.evaluate(vehicle, ruleIds); 
		if(evaluationResult!=null){
			System.out.println("Idd ev res="+evaluationResult.getIdEvaluationResult());
		}else{
			System.out.println("Idd ev res NULL=");
		}
		
		 
		return "/view/viewEvaluationResult.xhtml";
	}
	
	
	public EvaluationResult getEvaluationResult() {
		return evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

	public List<String> getSelectedRuleIds(){	 
		File propertiesFile = new File(PROPERTIES_FILE_ABSOLUTE_PATH);
        System.out.println("checkboxesStatus = "+checkboxesStatus);
        return configurationService.getSelectedRuleIds(propertiesFile, checkboxesStatus);
        
	}

	public Map<String, Boolean> getCheckboxesStatus() {
		return checkboxesStatus;
	}

	public void setCheckboxesStatus(Map<String, Boolean> checkboxesStatus) {
		this.checkboxesStatus = checkboxesStatus;
	}
	
	public String goToEvaluateVehicle(){
		System.out.println("Enter to goToEvaluateVehicle");
		return "/view/evaluateVehicleConfig.xhtml";
	}

	

}
