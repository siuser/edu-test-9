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

import ro.fortech.edu.model.MarketRule;
import ro.fortech.edu.service.MarketRuleService;
import ro.fortech.edu.service.StockCategoryService;

@RequestScoped
@ManagedBean(name = "marketRuleController")
public class MarketRuleController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	
	private List<String> allStockCategoryAsStringList;
		


	public List<String> getAllStockCategoryAsStringList() {
		return allStockCategoryAsStringList;
	}

	public void setAllStockCategoryAsStringList(List<String> allStockCategoryAsStringList) {
		this.allStockCategoryAsStringList = allStockCategoryAsStringList;
	}

	public List<MarketRule> getAllMarketRulesList() {
		return allMarketRulesList;
	}

	public void setAllMarketRulesList(List<MarketRule> allMarketRulesList) { 
		this.allMarketRulesList = allMarketRulesList;
	}

	public MarketRule getNewMarketRule() {
		return newMarketRule;
	}

	public void setNewMarketRule(MarketRule newMarketRule) {
		this.newMarketRule = newMarketRule;
	}

	@EJB
	private MarketRuleService marketRuleService;	
	
	@EJB
	private StockCategoryService stockCategoryService;
	
	private MarketRule newMarketRule;
	private List<MarketRule> allMarketRulesList;

	@PostConstruct
	public void initNewMarketRule() {		
		newMarketRule = new MarketRule();
		allMarketRulesList = marketRuleService.findAllMarketRules();
		allStockCategoryAsStringList = stockCategoryService.findAllStockCategoryAsString();
	}

	public void registerMarketRule() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {			
			System.out.println("Faces version=" + FacesContext.class.getPackage().getImplementationVersion());
			marketRuleService.registerMarketRule(newMarketRule);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
			
			facesContext.addMessage(null, m);
			initNewMarketRule();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
			facesContext.addMessage(null, m);
		}
	}

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
	
	public void getAllMarketRules(){
		allMarketRulesList = marketRuleService.findAllMarketRules();
		
	}
	
	

}
