package ro.fortech.edu.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.controller.EvaluationRuleController;
import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.model.EvaluationRules;
import ro.fortech.edu.model.MarketRule;
import ro.fortech.edu.model.RuleActivity;
import ro.fortech.edu.model.RuleCondition;
import ro.fortech.edu.rest.EvaluationRuleRestClient;

@Stateless
// Remove below comment when testing
// @TransactionAttribute(TransactionAttributeType.MANDATORY)
public class EvaluationRuleService {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());

	// Remove below comment when testing
	// @PersistenceContext(unitName = "edu-test-8", type =
	// PersistenceContextType.TRANSACTION)
	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private MarketRuleService marketRuleService;
	
	@EJB
	private EvaluationRuleRestClient evaluationRuleRestClient;

	/**
	 * Persist a new EvaluationRule
	 * 
	 * @param evaluationRule
	 * @throws Exception
	 */
	public void register(EvaluationRule evaluationRule) throws Exception {
		javaLog.log(Level.INFO, "Enter register method");
		entityManager.persist(evaluationRule);
	}

	/**
	 * Persist a new EvaluationRule using merge vs persiste method
	 * 
	 * @param evaluationRule
	 * @throws Exception
	 */
	public EvaluationRule registerMerge(EvaluationRule evaluationRule) throws Exception {
		return entityManager.merge(evaluationRule);
	}

	/**
	 * Persist a list of EvaluationRule
	 * 
	 * @param evaluationRuleList
	 * @throws Exception
	 */
	public void registerList(List<EvaluationRule> evaluationRuleList) throws Exception {
		for (EvaluationRule evaluationRule : evaluationRuleList) {
			entityManager.persist(evaluationRule);
		}
	}

	/**
	 * Remove an EvaluationRule from db
	 * 
	 * @param evaluationRule
	 * @throws Exception
	 */
	public void delete(EvaluationRule evaluationRule) throws Exception {
		entityManager.remove(evaluationRule);
	}

	/**
	 * Update an EvaluationRule
	 * 
	 * @param evaluationRule
	 * @throws Exception
	 */
	public void update(EvaluationRule evaluationRule) throws Exception {
		entityManager.merge(entityManager.merge(evaluationRule));
	}

	/**
	 * Find an EvaluationRule by its id
	 * 
	 * @param id
	 * @return
	 */
	public EvaluationRule findEvaluationRuleById(long id) {
		return entityManager.find(EvaluationRule.class, id);
	}

	/**
	 * Find all EvaluationRule from db
	 * 
	 * @return
	 */
	public List<EvaluationRule> findAllEvaluationRules() {
		long startTime = System.nanoTime();
		// entityManager.setProperty("javax.persistence.cache.storeMode",
		// "USE");
		entityManager.setProperty("javax.persistence.cache.storeMode", "BYPASS");
		Cache cache = entityManager.getEntityManagerFactory().getCache();

		boolean allInCache = false;
		if (cache.contains(EvaluationRule.class, 1)) {
			javaLog.log(Level.INFO,"Rule id=1 in cache");
			allInCache = true;
		} else {
			javaLog.log(Level.INFO,"Rule id=1 NOT in cache");
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EvaluationRule> criteriaQuery = criteriaBuilder.createQuery(EvaluationRule.class);
		Root<EvaluationRule> evaluationRule = criteriaQuery.from(EvaluationRule.class);
		criteriaQuery.select(evaluationRule);
		criteriaQuery.orderBy(criteriaBuilder.asc((evaluationRule.get("idEvaluationRule"))));
		long estimatedTime = System.nanoTime() - startTime;
		javaLog.log(Level.INFO,
				"Estimated Time to get rules from DB= " + estimatedTime + " from cache = " + allInCache);
		
		/*
		javaLog.log(Level.SEVERE, "SEVERE message");
		javaLog.log(Level.WARNING, "WARNING message");
		javaLog.log(Level.INFO, "INFO message");
		javaLog.log(Level.CONFIG, "CONFIG message");
		javaLog.log(Level.FINE, "FINE message");
		javaLog.log(Level.FINER, "FINER message");
		javaLog.log(Level.FINEST, "FINEST message");
		javaLog.log(Level.ALL, "ALL message");
		apacheLog.debug("debug message");
		apacheLog.trace("trace message");
		apacheLog.info("info message");
		apacheLog.warn("warn message");
		apacheLog.error("error message");
		apacheLog.fatal("fatal message");
		*/
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	/**
	 * Find the MarketRule corresponding to an EvaluationRule
	 * 
	 * @param evaluationRule
	 * @return
	 */
	public MarketRule getMarketRule(EvaluationRule evaluationRule) {
		long marketRuleId = evaluationRule.getMarketRuleId();
		return marketRuleService.findMarketRuleById(marketRuleId);
	}
	
	
	public void marshallingEvaluationRuleToXmlFile(String pathToXmlFile) throws JAXBException{
		System.out.println("Enter marshallingToXmlFile");
		System.out.println("pathToXmlFile= "+pathToXmlFile);
		EvaluationRules evaluationRules = new EvaluationRules();
		
		EvaluationRule evaluationRule1 = new EvaluationRule();	
		evaluationRule1.setIdEvaluationRule(1000L);
		RuleCondition ruleCondition1 = new RuleCondition();
		ruleCondition1.setVehicleAttributeName("engineCode");
		ruleCondition1.setVehicleAttributeValue("UTR39");
		evaluationRule1.getRuleConditions().add(ruleCondition1);
		
		RuleCondition ruleCondition2 = new RuleCondition();
		ruleCondition1.setVehicleAttributeName("engineCode");
		ruleCondition1.setVehicleAttributeValue("UTR38");
		evaluationRule1.getRuleConditions().add(ruleCondition2);
		
		RuleActivity ruleActivity1 =  new RuleActivity();
		ruleActivity1.setVehicleAttributeName("version");
		ruleActivity1.setVehicleAttributeValue("gutte");
		evaluationRule1.getRuleActivities().add(ruleActivity1);
		evaluationRule1.setMarketRuleId(101);
		
		EvaluationRule evaluationRule2 = new EvaluationRule();	
		evaluationRule2.setMarketRuleId(101);
		
		List<EvaluationRule> evaluationRuleList = new ArrayList<>();
		evaluationRuleList.add(evaluationRule1);
		evaluationRuleList.add(evaluationRule2);
		
		evaluationRules.setEvaluationRule(evaluationRuleList);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(EvaluationRules.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Marshal the evaluationRules list in console
	    jaxbMarshaller.marshal(evaluationRules, System.out);
	     
	    //Marshal the employees list in xml file 
	    jaxbMarshaller.marshal(evaluationRules, new File(pathToXmlFile));
		
	}
	
	public List<EvaluationRule> unmarshallingEvaluationRuleFromXmlFile(String temporaryFilePath) throws JAXBException{
		System.out.println("Enter unmarshallingEvaluationRuleFromXmlFile");
		System.out.println("EtemporaryFilePath= "+temporaryFilePath);
		JAXBContext jaxbContext = JAXBContext.newInstance(EvaluationRules.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();	     
	    //We had written this file in marshalling example
	    //EvaluationRules evaluationRules = (EvaluationRules) jaxbUnmarshaller.unmarshal( new File("E:\\Workspaces\\gits\\edu-test-8-git\\edu-test-8\\docs\\evaluationRules.xml") );
	    EvaluationRules evaluationRules = (EvaluationRules) jaxbUnmarshaller.unmarshal( new File(temporaryFilePath) );
		   
	    List<EvaluationRule> evaluationRuleList =evaluationRules.getEvaluationRule();
	    return evaluationRuleList;
	}
	
	public List<EvaluationRule> unmarshallingEvaluationRuleFromXmlFile(File file) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(EvaluationRules.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();	     
	    //We had written this file in marshalling example
	    //EvaluationRules evaluationRules = (EvaluationRules) jaxbUnmarshaller.unmarshal( new File("E:\\Workspaces\\gits\\edu-test-8-git\\edu-test-8\\docs\\evaluationRules.xml") );
	    EvaluationRules evaluationRules = (EvaluationRules) jaxbUnmarshaller.unmarshal( file);
		   
	    List<EvaluationRule> evaluationRuleList =evaluationRules.getEvaluationRule();
	    return evaluationRuleList;
	}
	
	public String  addBulkEvaluationRuleRest(String temporaryFilePath) throws JAXBException{		
		System.out.println("Enter addBatchEvaluationRuleRest");
		//Create the xml file by marshaling some EvaluationRule
		//marshallingEvaluationRuleToXmlFile();		
		evaluationRuleRestClient.invokePostBatchEvaluationRule(unmarshallingEvaluationRuleFromXmlFile(temporaryFilePath));;
		return "evaluationRuleList.xhtml";  
	}
	
	
	
}