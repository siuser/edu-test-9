package ro.fortech.edu.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ro.fortech.edu.model.EvaluationResultDetail;

@Stateless
public class EvaluationResultDetailService {
	public EvaluationResultDetailService(){
		
	}

	@PersistenceContext
	private EntityManager entityManager;

	public void register(EvaluationResultDetail evaluationResultDetail) throws Exception {
		entityManager.persist(evaluationResultDetail);
	}

	public void update(EvaluationResultDetail evaluationResultDetail) throws Exception {
		entityManager.merge(evaluationResultDetail);
	}

	public EvaluationResultDetail findEvaluationResultDetailById(long id) {

		return entityManager.find(EvaluationResultDetail.class, id);
	}

}
