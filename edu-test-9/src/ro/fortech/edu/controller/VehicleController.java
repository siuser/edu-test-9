package ro.fortech.edu.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.model.Vehicle;
import ro.fortech.edu.service.VehicleService;

/**
 * This class is responsible for allowing JSF to interact with the services
 * 
 * @author Silviu
 *
 */
@RequestScoped
@ManagedBean(name = "vehicleController")
public class VehicleController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	

	FacesContext facesContext = FacesContext.getCurrentInstance();

	@EJB
	private VehicleService vehicleService;

	private List<String> allStockCategoryAsStringList;
	
	private Vehicle vehicle;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}



	private Vehicle newVehicle;

	public Vehicle getNewVehicle() {
		return newVehicle;
	}

	public void setNewVehicle(Vehicle newVehicle) {
		this.newVehicle = newVehicle;
	}

	/**
	 * The @PostConstruct annotation causes a new vehicle object to be placed in
	 * the newVehicle field when the bean is instantiated
	 */
	@PostConstruct
	public void initNewVehicle() {
		newVehicle = new Vehicle();
		allStockCategoryAsStringList=vehicleService.getAllStockCategoryAsStringList();
	}

	public List<String> getAllStockCategoryAsStringList() {
		return allStockCategoryAsStringList;
	}

	public void setAllStockCategoryAsStringList(List<String> allStockCategoryAsStringList) {
		this.allStockCategoryAsStringList = allStockCategoryAsStringList;
	}

	/**
	 * Facade pattern method for persisting a new vehicle entity; work is done in
	 * service clas
	 * @throws Exception
	 */
	public String register() throws Exception {

		try {
			// evaluationRuleController.i
			System.out.println("Faces version=" + FacesContext.class.getPackage().getImplementationVersion());
			vehicleService.register(this.newVehicle);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
			facesContext.addMessage(null, m);
			initNewVehicle();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
			facesContext.addMessage(null, m);
		}
		
		return "/view/vehicleList.xhtml";
	}

	/**
	 * Facade pattern method for updating changes of a Vehicle entity Work is
	 * done in service class 
	 * @param vehicle
	 * @throws Exception
	 */
	public void update(Vehicle vehicle) throws Exception {
		try {

			vehicleService.update(vehicle);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!", "Updating successful");
			facesContext.addMessage(null, m);
			initNewVehicle();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Updated unsuccessful");
			facesContext.addMessage(null, m);
		}
	}
	
	public List<Vehicle> getAllVehicle(){
		return vehicleService.findAllVehicles();		
	}
	
	public String goToEvaluateVehicle(){
		System.out.println("Enter to goToEvaluateVehicle");
		return "/view/evaluateVehicleConfig.xhtml";
	}
	
	/*
	public String evaluate(){
		List<String> ruleIds = this.getSelectedRuleIds();
		Map<String,String> params = 
			      facesContext.getExternalContext().getRequestParameterMap();
		long vehicleId  =  new Long(params.get("vehicleId")); 
		System.out.println("vehicleId="+vehicleId);
		Vehicle vehicle=vehicleService.findById(vehicleId);
		vehicleService.evaluate(vehicle, ruleIds); 
		
		
		return null;
	}
	
	*/
	
	
	
	
	
	

	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

}
