package ro.fortech.edu.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ro.fortech.edu.model.MarketRule;

@Stateless
public class MarketRuleService {
	public MarketRuleService(){
		
	}

	@PersistenceContext
    private EntityManager entityManager;

    public void registerMarketRule(MarketRule marketRule) throws Exception {
    	entityManager.persist(marketRule);        
    }
    
    public MarketRule findMarketRuleById(long id){    	
    	return entityManager.find(MarketRule.class, id);
    }
    
    public List<MarketRule> findAllMarketRules() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MarketRule> criteria = cb.createQuery(MarketRule.class);
        Root<MarketRule> marketRule = criteria.from(MarketRule.class);           
        criteria.select(marketRule);        
        return entityManager.createQuery(criteria).getResultList();
    }
    
   



}