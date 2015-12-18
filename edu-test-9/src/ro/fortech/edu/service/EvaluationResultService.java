package ro.fortech.edu.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.controller.EvaluationRuleController;
import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.model.MarketRule;

@Stateless
public class EvaluationResultService {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	@PersistenceContext
    private EntityManager entityManager;

	public void register(EvaluationResult evaluationResult) throws Exception {	 	
		entityManager.persist(evaluationResult);		
	}
	
	public void update(EvaluationResult evaluationResult) throws Exception {    	
    	entityManager.merge(evaluationResult);        
    }
	
	/**
	 * Remove an EvaluationResult from db
	 * 
	 * @param evaluationResult
	 * @throws Exception
	 */
	public void delete(EvaluationResult evaluationResult) throws Exception {
		entityManager.remove(evaluationResult);
	}
	
	public EvaluationResult findEvaluationResultById(long id) {

		return entityManager.find(EvaluationResult.class, id);
	}
	
	public List<EvaluationResult> findAllEvaluationResults() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EvaluationResult> criteria = cb.createQuery(EvaluationResult.class);
        Root<EvaluationResult> evaluationResult = criteria.from(EvaluationResult.class);           
        criteria.select(evaluationResult);        
        return entityManager.createQuery(criteria).getResultList();
    }

}
