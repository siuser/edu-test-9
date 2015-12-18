package ro.fortech.edu.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wink.client.EntityType;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

import ro.fortech.edu.controller.EvaluationRuleController;
import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.service.EvaluationRuleService;

@Stateless
public class EvaluationRuleRestClient {
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	@EJB 
	EvaluationRuleService evaluationRuleService;   

	/**
	 * Using Apache Wink 
	 * 
	 * @param evaluationRuleId
	 * @return EvaluationRule 
	 */
	public EvaluationRule invokeGetEvaluationRuleById(long evaluationRuleId) {
		System.out.println("****Enter invokeGetEvaluationRuleById****");
		// Create the rest client instance
		// The RestClient is the entry point for building the RESTful Web
		// Service client
		RestClient client = new RestClient();
		// Create the resource instance to interact with
		Resource resource = client
				.resource("http://localhost:9080/edu-test-9/rest/evaluationRules/" + evaluationRuleId);
		// Perform a GET on the resource. The resource will be returned as XML
		EvaluationRule response = resource.accept(MediaType.APPLICATION_XML).get(EvaluationRule.class);
		System.out.println("Rule id=" + response.getIdEvaluationRule());
		return response;
	}

	/**
	 * Using Apache Wink 
	 * Invoke a GET method for all EvaluationRule
	 */
	public void invokeGetAllEvaluationRule() {
		System.out.println("****Enter invokeGetAllEvaluationRule****");
		// Create the rest client instance
		RestClient client = new RestClient();
		// Create the resource instance to interact with
		Resource resource = client.resource("http://localhost:9080/edu-test-9/rest/evaluationRules");
		List<EvaluationRule> evaluationRuleList = resource.get(new EntityType<List<EvaluationRule>>() {
		});
		System.out.println("evaluationRuleList size = " + evaluationRuleList.size());
		for (EvaluationRule evaluationRule : evaluationRuleList) {
			System.out.println("evaluationRule ids= " + evaluationRule.getIdEvaluationRule());
		}
	}

	/**
	 * Using Apache Wink 
	 * Invoke a POST method
	 * 
	 * @param evaluationRule
	 */
	public EvaluationRule invokePostEvaluationRule(EvaluationRule evaluationRule) {

		System.out.println("****Invoking the invokePostEvaluationRule ****");
		System.out.println("rule market id=" + evaluationRule.getMarketRuleId());
		// Create the rest client instance
		RestClient client = new RestClient();

		// Create the resource instance to interact with
		Resource resource = client.resource("http://localhost:9080/edu-test-9/rest/evaluationRules/evaluationRule");

		// Perform a POST on the resource
		EvaluationRule response = resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
				.post(EvaluationRule.class, evaluationRule);
		return response;
	}

	/**
	 * Using Apache Wink Multiple invoke a POST method
	 * 
	 * @param evaluationRuleList
	 */
	public void invokePostBatchEvaluationRule(List<EvaluationRule> evaluationRuleList) { 
		System.out.println("****Invoking the invokePostBatchEvaluationRule ****");
		// Add an existing EvaluationRule in list
		//EvaluationRule evaluationRuleExistent = evaluationRuleService.findEvaluationRuleById(1L);
		// Make a change
		//evaluationRuleExistent.setDescription("Rule changed");
		//evaluationRuleList.add(evaluationRuleExistent);
		
		// Perform a POST on every evaluationRule from list
		for (EvaluationRule evaluationRule : evaluationRuleList) {
			this.invokePostEvaluationRule(evaluationRule);
		}
	}

	/**
	 * Using Apache Wink Invoke a DELETE method
	 * 
	 * @param evaluationRule
	 * @return
	 */
	public void invokeDeleteEvaluationRule(EvaluationRule evaluationRule) {

		System.out.println("****Invoking the invokeDeleteEvaluationRule ****");
		long evaluationRuleId = evaluationRule.getIdEvaluationRule();
		// Create the rest client instance
		RestClient client = new RestClient();

		// Create the resource instance to interact with
		Resource resource = client
				.resource("http://localhost:9080/edu-test-9/rest/evaluationRules/" + evaluationRuleId);

		// Perform a DELETE on the resource
		resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();

	}

	/*
	 * //All below work with JAX-RS 2.0 Client
	 * 
	 * public void invokeGetEvaluationRuleById(long evaluationRuleId) {
	 * System.out.println("****Enter invokeGetEvaluationRuleById****");
	 * 
	 * //Obtain the instance of Client which will be entry point for invoking
	 * REST Services. //It instantiates a preinitialized Client that you can use
	 * right away Client client = ClientBuilder.newClient(); //Target the
	 * RESTful Webservice we want to //invoke by capturing it in WebTarget
	 * instance. WebTarget webTarget =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationRules/" +
	 * evaluationRuleId);
	 * 
	 * //Build the request i.e a GET request to the RESTful Webservice defined
	 * //by the URI in the WebTarget instance. Invocation invocation =
	 * webTarget.request().buildGet();
	 * 
	 * //Invok the request to the RESTful API and capturing the Response.
	 * Response response = invocation.invoke();
	 * 
	 * //Return the XML data which can be unmarshalled //into the instance of
	 * Books by using JAXB. EvaluationRule evaluationRule =
	 * response.readEntity(EvaluationRule.class); System.out.println(
	 * "evaluationRule ID= "+evaluationRule.getIdEvaluationRule());
	 * 
	 * }
	 * 
	 * public void invokeGetAllEvaluationRule() { System.out.println(
	 * "****Enter invokeGetAllEvaluationRule****");
	 * 
	 * //Obtain the instance of Client which will be entry point for invoking
	 * REST Services. Client client = ClientBuilder.newClient();
	 * //List<EvaluationRule> evaluationRuleList =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationRules")
	 * 
	 * // .get(new GenericType<List<EvaluationRule>>(){}); //Target the RESTful
	 * Webservice we want to //invoke by capturing it in WebTarget instance.
	 * WebTarget webTarget =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationRules");
	 * 
	 * //Build the request i.e a GET request to the RESTful Webservice defined
	 * //by the URI in the WebTarget instance. Invocation invocation =
	 * webTarget.request().buildGet();
	 * 
	 * //Invok the request to the RESTful API and capturing the Response.
	 * Response response = invocation.invoke(); //System.out.println(
	 * "response all= "+response); //Return the XML data which can be
	 * unmarshalled //into the instance of Books by using JAXB.
	 * List<EvaluationRule> evaluationRuleList = response.readEntity(new
	 * GenericType<List<EvaluationRule>>(){}); //System.out.println(
	 * "evaluationRule all= "+evaluationRule); //System.out.println(
	 * "evaluationRule ID= "+evaluationRule.getIdEvaluationRule());
	 * 
	 * //List<EvaluationRule> evaluationRuleList =
	 * response.readEntity(EvaluationRule.class); System.out.println(
	 * "evaluationRule list size ID= "+evaluationRuleList.size());
	 * for(EvaluationRule evaluationRule:evaluationRuleList){
	 * System.out.println("evaluationRule id="
	 * +evaluationRule.getIdEvaluationRule()); }
	 * 
	 * }
	 * 
	 * public void addEvaluationRuleRest(EvaluationRule evaluationRule) {
	 * 
	 * System.out.println("****Invoking the addEvaluationRuleRest ****");
	 * 
	 * // Obtaining the instance of Client which will be entry point to //
	 * invoking REST Services. Client client = ClientBuilder.newClient(); //
	 * Targeting the RESTful Webserivce we want to // invoke by capturing it in
	 * WebTarget instance. //Bind the target to the REST service’s URL using the
	 * target() method: WebTarget target = client.target(
	 * "http://localhost:9080/edu-test-8/rest/evaluationRules/testOneRule");
	 * Response response =
	 * target.request().buildPost(Entity.entity(evaluationRule,
	 * MediaType.APPLICATION_XML)).invoke(); //System.out.println(
	 * "RESPONSE location="+response.getMetadata().get("Location").get(0)); //
	 * Building the request i.e a GET request to the RESTful Webservice //
	 * defined // by the URI in the WebTarget instance.
	 * 
	 * // As we know that this RESTful Webserivce returns the XML data which //
	 * can be unmarshalled
	 * 
	 * // into the instance of marketRule by using JAXB. //return
	 * response.getMetadata().get("Location").get(0).toString();
	 * 
	 * }
	 * 
	 */

}
