package ro.fortech.edu.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.model.MarketRule;
import ro.fortech.edu.service.EvaluationResultService;
import ro.fortech.edu.service.MarketRuleService;

@RequestScoped
@ManagedBean(name = "evaluationResultController") 
public class EvaluationResultController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
		
	@EJB
	private EvaluationResultService evaluationResultService;
	
	private List<EvaluationResult> allEvaluationResultList;
	
	
	@PostConstruct
	public void initEvaluationResult() {			
		allEvaluationResultList = evaluationResultService.findAllEvaluationResults();		
	}


	public List<EvaluationResult> getAllEvaluationResultList() {
		return allEvaluationResultList;
	}


	public void setAllEvaluationResultList(List<EvaluationResult> allEvaluationResultList) {
		this.allEvaluationResultList = allEvaluationResultList;
	}

}
