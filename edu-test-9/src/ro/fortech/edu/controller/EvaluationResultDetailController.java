package ro.fortech.edu.controller;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@RequestScoped
@ManagedBean(name = "evaluationResultDetailController") 
public class EvaluationResultDetailController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	

}
