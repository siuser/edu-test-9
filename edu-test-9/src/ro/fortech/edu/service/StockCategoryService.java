package ro.fortech.edu.service;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ro.fortech.edu.model.StockCategory;

/**
 * Service class for StockCategory
 * This bean requires transactions as it needs to write to the database
 * Making this an EJB gives us access to declarative transactions
 * (vs manual transaction control)
 * @author Silviu
 *
 */
@Stateless
public class StockCategoryService {
	
	public StockCategoryService(){
		
	}
	
	@PersistenceContext
    private EntityManager entityManager;
	
	/**
	 * Service method to return all StockCategory from database
	 * @return
	 */
	public List<StockCategory> findAllStockCategories() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StockCategory> criteria = criteriaBuilder.createQuery(StockCategory.class);
        Root<StockCategory> stockCategory = criteria.from(StockCategory.class);           
        criteria.select(stockCategory);        
        return entityManager.createQuery(criteria).getResultList();
    }
	
	/**
	 * Service method to return all StockCategory from database as a list of their name 
	 * @return
	 */
	public List<String> findAllStockCategoryAsString() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StockCategory> criteria = criteriaBuilder.createQuery(StockCategory.class);
        Root<StockCategory> stockCategory = criteria.from(StockCategory.class);           
        criteria.select(stockCategory);        
         entityManager.createQuery(criteria).getResultList();
         List<String> allStockCategoryAsStringList = new ArrayList<String>();
         Iterator<StockCategory> iteratorQueryResult =  entityManager.createQuery(criteria).getResultList().iterator();
         while(iteratorQueryResult.hasNext()){
        	 StockCategory stockCategoryItem = (StockCategory) iteratorQueryResult.next();        	 
        	 allStockCategoryAsStringList.add(stockCategoryItem.getName());
         }
        return allStockCategoryAsStringList;
    }
    
    /**
     * Service method to return one single StockCategory based on its id
     * @param id
     * @return
     */
	public StockCategory findById(long id) {
        return entityManager.find(StockCategory.class, id);
    }

}
