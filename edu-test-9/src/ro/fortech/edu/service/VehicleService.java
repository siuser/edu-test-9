package ro.fortech.edu.service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.controller.EvaluationRuleController;
import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.model.EvaluationResultDetail;
import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.model.MarketRule;
import ro.fortech.edu.model.RuleActivity;
import ro.fortech.edu.model.RuleCondition;
import ro.fortech.edu.model.Vehicle;

/**
 * Service class for Vehicle This bean requires transactions as it needs to
 * write to the database Making this an EJB gives us access to declarative
 * transactions (vs manual transaction control)
 * 
 * @author Silviu
 *
 */
@Stateless
//Remove below comment when testing
//@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class VehicleService {
	
	// Remove below comment when testing
	// @PersistenceContext(unitName = "edu-test-8", type =
	// PersistenceContextType.TRANSACTION)
	@PersistenceContext	
	private EntityManager entityManager;

	@EJB
	private StockCategoryService stockCategoryService;

	@EJB
	private EvaluationRuleService evaluationRuleService;

	@EJB
	private MarketRuleService marketRuleService;

	@EJB
	private EvaluationResultService evaluationResultService;

	@EJB
	private EvaluationResultDetailService evaluationResultDetailService;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	/**
	 * Service method to persist a new vehicle into database
	 * 
	 * @param vehicle
	 * @throws Exception
	 */
	public void register(Vehicle vehicle) throws Exception {
		System.out.println("Registering Vehicle ... " +
		 vehicle.getBranch());
		//try{
		entityManager.persist(vehicle);
		//}catch(RollbackException e){
		//	System.out.println("Rollbackk");
		//}
		
	}
	
	
	/**
	 * Service method to persist a new vehicle into database
	 * using merge iso persist method
	 * @param vehicle
	 * @return 
	 * @throws Exception
	 */
	public Vehicle registerMerge(Vehicle vehicle) throws Exception {
		System.out.println("Registering Vehicle ... " +
		 vehicle.getBranch());
		return entityManager.merge(vehicle);
	}


	/**
	 * Service method to update an existent vehicle
	 * 
	 * @param vehicle
	 * @throws Exception
	 */
	public void update(Vehicle vehicle) throws Exception {
		// System.out.println("Updating Vehicle id= " + vehicle.getIdVehicle());
		entityManager.merge(vehicle);
	}

	/**
	 * 
	 * @return a list of all vehicles from database
	 */
	public List<Vehicle> findAllVehicles() {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Vehicle> criteria = criteriaBuilder.createQuery(Vehicle.class);
		Root<Vehicle> vehicle = criteria.from(Vehicle.class);
		criteria.select(vehicle);
		return entityManager.createQuery(criteria).getResultList();
	}

	/**
	 * 
	 * @param id
	 * @return a vehicle found by unique id
	 */
	public Vehicle findById(long id) {
		return entityManager.find(Vehicle.class, id);
	}

	public List<String> getAllStockCategoryAsStringList() {

		return stockCategoryService.findAllStockCategoryAsString();

	}

	/**
	 * 
	 * @param vehicle
	 * @param ruleIds
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */

	public EvaluationResult evaluate(Vehicle vehicle, List<String> ruleIds) {
		// A new Evaluation Result item every time an evaluate method call
		// (every evaluation)
		EvaluationResult evaluationResult = new EvaluationResult();
		evaluationResult.setVehicle(vehicle);
		Timestamp dateOfEvaluation = new Timestamp((new Date()).getTime());
		//Date dateOfEvaluation = new Date();
		evaluationResult.setDateOfEvaluation(dateOfEvaluation);

		// Append all rules in allRuleIds
		StringBuilder allRuleIds = new StringBuilder();
		// Append rules not found in database in ruleIdsNotInDb
		StringBuilder ruleIdsNotInDb = new StringBuilder();
		for (String ruleIdString : ruleIds) {
			allRuleIds.append(ruleIdString + ",");
			Long ruleIdLong = new Long(ruleIdString); 
			EvaluationRule evaluationRule = evaluationRuleService.findEvaluationRuleById(ruleIdLong);
			if ( evaluationRule== null) {
				// No EvaluationRule in database correspond to this ruleIdLong
				System.out.println("No such Rule in db for id=" + ruleIdLong);
				ruleIdsNotInDb.append(ruleIdString + ",");
			} else {
				// There is an EvaluationRule in db
				System.out.println("There is Rule in db for id=" + ruleIdLong);
				//EvaluationRule evaluationRule = evaluationRuleService.findEvaluationRuleById(ruleIdLong);

				// A new EvaluationResultDetail for every rule applied
				EvaluationResultDetail evaluationResultDetail = new EvaluationResultDetail();
				evaluationResultDetail.setEvaluationResult(evaluationResult);
				evaluationResultDetail.setIdEvaluationRule(ruleIdLong);

				// The message for EvaluationResultDetail
				StringBuilder evaluationMessage = new StringBuilder();
				evaluationMessage
						.append("EvaluationRule rule id= " + evaluationRule.getIdEvaluationRule() + LINE_SEPARATOR);

				
				
				// Get the marketRule corresponding to this rule
				long marketRuleId = evaluationRule.getMarketRuleId();
				MarketRule marketRule = marketRuleService.findMarketRuleById(marketRuleId);
				if (marketRule != null) {
					// Database contains a MarketRule for this EvaluationRule
					// Let's check if marketRule matches with vehicle
					// Assume that vehicle has indeed fields: countryNumber,
					// branch and stockCategory
					if (vehicle.getCountryNumber().equals(marketRule.getCountryNumber())
							&& (vehicle.getBranch() == marketRule.getBranch())
							&& vehicle.getStockCategory().toLowerCase().equals(vehicle.getStockCategory().toLowerCase())
							&& marketRule.getIsActive()) {

						// This marketRule matches vehicle
						// So we can apply evaluationRule
						evaluationMessage.append("*****Market rule id= " + marketRule.getIdMarketRule()
								+ " matches vehicle and is active" + LINE_SEPARATOR);
						// Find conditions list
						List<RuleCondition> conditionsList = evaluationRule.getRuleConditions();
						
							if (isAllRuleConditionAccomplished(vehicle, conditionsList, evaluationMessage)) {
								// All conditions passed
								// So we can Apply Rule Activity
								applyRuleActivity(vehicle, evaluationRule, evaluationResultDetail, evaluationMessage);

							} else {
								// Not all conditions passed
								evaluationMessage
										.append("*****NOT ALL conditions passed, this evaluation rule will NOT be applied"
												+ LINE_SEPARATOR);
								evaluationResultDetail.setRuleStatus("NOK");
								System.out.println("evaluationMessage= " + evaluationMessage);
							}
						
					} else {
						// Market rule does not matches vehicle OR market Rule
						// inactive
						evaluationMessage.append("*****Market rule id= " + marketRule.getIdMarketRule()
								+ " does NOT matches vehicle or is NOT active" + LINE_SEPARATOR);
						evaluationMessage.append("*****Evaluation rule id= " + evaluationRule.getIdEvaluationRule()
								+ " will NOT be applied" + LINE_SEPARATOR);
						evaluationResultDetail.setRuleStatus("NOK");
						System.out.println("evaluationMessage= " + evaluationMessage);
					} // end else Market rule does not matches vehicle OR market
						// Rule inactive
				} else {
					// Database does NOT contain a MarketRule for this
					// EvaluationRule
					evaluationMessage.append(
							"***** NO market rule  into the database for this Evaluation rule" + LINE_SEPARATOR);
					evaluationMessage.append(LINE_SEPARATOR);
					evaluationMessage.append("*****Evaluation rule id= " + evaluationRule.getIdEvaluationRule()
							+ " will NOT be applied" + LINE_SEPARATOR);
					evaluationResultDetail.setRuleStatus("NOK");
				}
				evaluationResultDetail.setMessage(evaluationMessage.toString());
				System.out.println("evaluationResultDetail = " + evaluationResultDetail);
				// Below null unless below change in EvaluationResult entity
				// private List<EvaluationResultDetail> evaluationResultDetails
				// = new ArrayList<>();
				// ISO private List<EvaluationResultDetail>
				// evaluationResultDetails;
				evaluationResult.getEvaluationResultDetails().add(evaluationResultDetail);
				System.out.println("evaluationMessage= " + evaluationMessage);

			} // end else There is an EvaluationRule in db
		} // end for on rules
			// Let's remove the last separator from allRuleIds and
			// ruleIdsNotInDb
		if (allRuleIds.length() > 0) {
			allRuleIds.setLength(allRuleIds.length() - 1);
		}
		if (ruleIdsNotInDb.length() > 0) {
			ruleIdsNotInDb.setLength(ruleIdsNotInDb.length() - 1);
		}
		evaluationResult.setEvaluationRuleIdsToBeApplied(allRuleIds.toString());
		evaluationResult.setEvaluationRuleIdsNotInDatabase(ruleIdsNotInDb.toString());
		// Update db
		try {
			this.update(vehicle);
			evaluationResultService.register(evaluationResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return evaluationResult;
	}

	/*
	 * public EvaluationResult evaluate(Vehicle vehicle, List<String> ruleIds)
	 * throws IllegalArgumentException, IllegalAccessException,
	 * NoSuchFieldException, SecurityException { // A new Evaluation Result item
	 * every time an evaluate method call // (every evaluation) EvaluationResult
	 * evaluationResult = new EvaluationResult();
	 * evaluationResult.setVehicle(vehicle);
	 * 
	 * // Apply every rule to vehicle for (String ruleId : ruleIds) { // Find
	 * EvaluationRule Long ruleIdAsLong = new Long(ruleId); EvaluationRule
	 * evaluationRule = evaluationRuleRepository.findById(ruleIdAsLong);
	 * 
	 * if (evaluationRule.getIsMappingRule()) { // Apply a mapping rule
	 * applyMappingRule(vehicle, evaluationRule, evaluationResult); } else { //
	 * Apply an interpretation rule applyInterpretationRule(vehicle,
	 * evaluationRule, evaluationResult); }
	 * 
	 * }
	 * 
	 * try { this.evaluationResult = evaluationResult;
	 * this.register(evaluationResult); this.update(vehicle); //
	 * this.register(evaluationResultDetail); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return null; }
	 * 
	 */

	/**
	 * 
	 * @param vehicle
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Map<String, String> getVehicleFiels(Vehicle vehicle) {
		System.out.println("Enter method = getVehicleFiels(Vehicle vehicle)");
		// Use java.reflect API to find vehicle's fields
		Class<?> vehicleClass = vehicle.getClass();
		Field[] vehicleFields = vehicleClass.getDeclaredFields();

		// Put vehicle field in a map
		// fieldName, which is unique, will be the key
		// fieldStringValue will be the value
		String fieldName = null;
		String fieldStringValue = null;
		Map<String, String> vehicleFieldsMap = new HashMap<String, String>();
		for (Field field : vehicleFields) {
			// There are some system fields (like _persistence_primaryKey)
			// which start with underscore; no interested
			if (!field.getName().startsWith("_")) {
				field.setAccessible(true);
				Object objValue = null;
				try {
					objValue = field.get(vehicle);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// fieldType = field.getType().toString();
				fieldName = field.getName();
				if (!(objValue == null)) {
					// objValue not null, so can apply toString
					fieldStringValue = objValue.toString();
				} else {
					// objValue is null, set fieldValue to null
					fieldStringValue = null;
				}
				// System.out.println("Field name= " + fieldName + " string
				// value= " + fieldStringValue);
				vehicleFieldsMap.put(fieldName, fieldStringValue);
			}

		}
		System.out.println("Field map=" + vehicleFieldsMap);
		return vehicleFieldsMap;

	}

	/**
	 * 
	 * @param vehicle
	 * @param fieldName
	 * @param fieldValue
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public void setVehicleFieldValue(Vehicle vehicle, String fieldName, String fieldValue) {
		System.out.println("Enter method = setVehicleFieldValue(Vehicle vehicle)");
		// Use java.reflect API to set vehicle's fields
		Class<?> vehicleClass = vehicle.getClass();
		Field field = null;
		try {
			field = vehicleClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(true);
		try {
			field.set(vehicle, fieldValue);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Apply RuleActivity on a vehicle
	 * 
	 * @param vehicle
	 * @param evaluationRule
	 * @param evaluationResultDetail
	 * @param evaluationMessage
	 */
	public void applyRuleActivity(Vehicle vehicle, EvaluationRule evaluationRule,
			EvaluationResultDetail evaluationResultDetail, StringBuilder evaluationMessage) {
		// So we can apply activities

		// The map of vehicle fields (field name as map key,field value
		// as map value)
		Map<String, String> vehicleFieldsMap = null;
		try {
			vehicleFieldsMap = this.getVehicleFiels(vehicle);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<RuleActivity> activitiesList = evaluationRule.getRuleActivities();
		System.out.println("activitiesList size"+activitiesList.size());
		// Let's check if activitiesList contains any activity
		if (!activitiesList.isEmpty()) {
			// There are activities to be performed
			System.out.println("activitiesList NOt empty");
			String ruleActivityKey = null;
			for (RuleActivity ruleActivity : activitiesList) {
				ruleActivityKey = ruleActivity.getVehicleAttributeName();
				if (vehicleFieldsMap.containsKey(ruleActivityKey)) {
					// Activity attribute has a correspondent in
					// vehicle fields
					try {
						this.setVehicleFieldValue(vehicle, ruleActivityKey, ruleActivity.getVehicleAttributeValue());
						evaluationMessage.append(
								"*****Activity id= " + ruleActivity.getIdRuleActivity() + " applied result = OK");
						evaluationMessage.append(LINE_SEPARATOR);
						evaluationResultDetail.setRuleStatus("OK");
						System.out.println("evaluationMessage= " + evaluationMessage);

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					// Activity attribute has NO correspondent
					// in vehicle fields
					evaluationMessage.append("*****Activity id=" + ruleActivity.getIdRuleActivity()
							+ " activity vehicle attribute (" + ruleActivityKey
							+ ") does not have a correspondent in vehicle fields" + LINE_SEPARATOR);
					evaluationMessage.append(
							"*****Activity id=" + ruleActivity.getIdRuleActivity() + " NOT applied" + LINE_SEPARATOR);
					System.out.println("evaluationMessage= " + evaluationMessage);
				}
			} // end for ruleActivities

		} else {
			// activitiesList empty
			System.out.println("activitiesList empty");
			evaluationMessage.append("*****No Activity to be performed " + LINE_SEPARATOR);

		}

	}

	/**
	 * Check on a vehicle if all conditions from a list are passed
	 * 
	 * @param vehicle
	 * @param conditionsList
	 * @param evaluationMessage
	 * @return
	 */
	public boolean isAllRuleConditionAccomplished(Vehicle vehicle, List<RuleCondition> conditionsList,
			StringBuilder evaluationMessage) {
		// The map of vehicle fields (field name as map key, field value
		// as map value)
		Map<String, String> vehicleFieldsMap = null;
		try {
			vehicleFieldsMap = this.getVehicleFiels(vehicle);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//If no conditions, apply activities anyway
		if (conditionsList.isEmpty()) {
			// conditionsList empty
			evaluationMessage.append(
					"*****NO conditions, this  evaluation rule will be applied anyway" + LINE_SEPARATOR);
		} 
		
		
		boolean isAllConditionAccomplished = true;
		for (RuleCondition ruleCondition : conditionsList) {
			Long conditionId = ruleCondition.getIdRuleCondition();
			String ruleConditionKey = ruleCondition.getVehicleAttributeName();
			if (vehicleFieldsMap.containsKey(ruleConditionKey)) {
				// Condition vehicle attribute has a
				// correspondent in vehicle fields
				evaluationMessage.append("*****Condition id=" + conditionId + " condition's vehicle attribute " + "("
						+ ruleConditionKey + ")" + " is in vehicle fields");
				evaluationMessage.append(LINE_SEPARATOR);
				if ((ruleCondition.getVehicleAttributeValue() == null)
						|| (ruleCondition.getVehicleAttributeValue().trim().length() == 0)) {
					// Vehicle attribute is null or empty
					isAllConditionAccomplished = false;
					evaluationMessage.append("*****Condition id=" + conditionId + " condition's vehicle value for "
							+ "(" + ruleConditionKey + ")" + " is NOT set or null");
					evaluationMessage.append(LINE_SEPARATOR);
					evaluationMessage.append(
							"*****Condition id=" + ruleCondition.getIdRuleCondition() + "verified result = NOK");
					evaluationMessage.append(LINE_SEPARATOR);
					// Let's comment below break to see all
					// conditions in action
					// break;
				} else {
					// Vehicle attribute value not null
					// We can check if vehicle attribute value
					// equals
					// condition attribute value
					if (vehicleFieldsMap.get(ruleConditionKey).equals(ruleCondition.getVehicleAttributeValue())) {
						// Rule condition attribute value equals
						// vehicle attribute value
						// So condition is met
						evaluationMessage.append(
								"*****Condition id=" + ruleCondition.getIdRuleCondition() + " verified result = OK");
						evaluationMessage.append(LINE_SEPARATOR);
						// this.setVehicleFieldValue(vehicle,
						// evaluationRule.getVehicleAttribute(),
						// evaluationRule.getVehicleAttributeTarget());
						System.out.println("evaluationMessage= " + evaluationMessage);
					} else {
						// Rule condition attribute value not
						// equal vehicle attribute value
						// Condition not met
						// System.out.println("Rule condition
						// attribute value not equal vehicle
						// attribute value");
						isAllConditionAccomplished = false;
						evaluationMessage.append("*****Condition id=" + ruleCondition.getIdRuleCondition()
								+ " rule condition attribute value (" + ruleCondition.getVehicleAttributeValue()
								+ ") not equal vehicle attribute value (" + vehicleFieldsMap.get(ruleConditionKey)
								+ ")");
						evaluationMessage.append(LINE_SEPARATOR);
						evaluationMessage.append(
								"*****Condition id=" + ruleCondition.getIdRuleCondition() + " verified result = NOK");
						evaluationMessage.append(LINE_SEPARATOR);
						// Let's comment below break to see all
						// conditions in action
						// break;
					}
				} // end else attribute value not null
			} else {
				// Condition vehicle attribute has NO
				// correspondent in
				// vehicle fields
				evaluationMessage.append("*****Condition id=" + conditionId + " condition's vehicle attribute " + "("
						+ ruleConditionKey + ")" + " NOT in vehicle fields");
				evaluationMessage.append(LINE_SEPARATOR);
				evaluationMessage
						.append("*****Condition id=" + ruleCondition.getIdRuleCondition() + " verified result = NOK");
				evaluationMessage.append(LINE_SEPARATOR);
				isAllConditionAccomplished = false;
				// Let's comment below break to see all
				// conditions in action
				// break;
			}
		} // end for conditionsList

		return isAllConditionAccomplished;
	}

}
