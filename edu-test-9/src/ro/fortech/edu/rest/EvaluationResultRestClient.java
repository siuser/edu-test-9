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
import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.service.EvaluationResultService;

@Stateless
public class EvaluationResultRestClient {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	

	@EJB 
	EvaluationResultService evaluationResultService;    

	/**
	 * Using Apache Wink 
	 * 
	 * @param evaluationResultId
	 * @return EvaluationResult 
	 */
	public EvaluationResult invokeGetEvaluationResultById(long evaluationResultId) {
		System.out.println("****Enter invokeGetEvaluationResultById****");
		// Create the rest client instance
		// The RestClient is the entry point for building the RESTful Web
		// Service client
		RestClient client = new RestClient();
		// Create the resource instance to interact with
		Resource resource = client
				.resource("http://localhost:9080/edu-test-9/rest/evaluationResults/" + evaluationResultId);
		// Perform a GET on the resource. The resource will be returned as XML
		EvaluationResult response = resource.accept(MediaType.APPLICATION_XML).get(EvaluationResult.class);
		System.out.println("Rule id=" + response.getIdEvaluationResult());
		return response;
	}

	/**
	 * Using Apache Wink 
	 * Invoke a GET method for all EvaluationResult
	 */
	public void invokeGetAllEvaluationResult() {
		System.out.println("****Enter invokeGetAllEvaluationResult****");
		// Create the rest client instance
		RestClient client = new RestClient();
		// Create the resource instance to interact with
		Resource resource = client.resource("http://localhost:9080/edu-test-9/rest/evaluationResults");
		List<EvaluationResult> evaluationResultList = resource.get(new EntityType<List<EvaluationResult>>() {
		});
		System.out.println("evaluationResultList size = " + evaluationResultList.size());
		for (EvaluationResult evaluationResult : evaluationResultList) {
			System.out.println("evaluationResult ids= " + evaluationResult.getIdEvaluationResult());
		}
	}

	/**
	 * Using Apache Wink 
	 * Invoke a POST method
	 * 
	 * @param evaluationResult
	 */
	public EvaluationResult invokePostEvaluationResult(EvaluationResult evaluationResult) {

		System.out.println("****Invoking the invokePostEvaluationResult ****");
		System.out.println("vehicle id=" + evaluationResult.getVehicle().getIdVehicle());
		// Create the rest client instance
		RestClient client = new RestClient();

		// Create the resource instance to interact with
		Resource resource = client.resource("http://localhost:9080/edu-test-9/rest/evaluationResults/evaluationResult");

		// Perform a POST on the resource
		EvaluationResult response = resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
				.post(EvaluationResult.class, evaluationResult);
		return response;
	}

	/**
	 * Using Apache Wink Multiple invoke a POST method
	 * 
	 * @param evaluationResultList
	 */
	public void invokePostBatchEvaluationResult(List<EvaluationResult> evaluationResultList) {
		System.out.println("****Invoking the invokePostBatchEvaluationResult ****");
		// Add an existing EvaluationResult in list
		//EvaluationResult evaluationResultExistent = evaluationResultService.findEvaluationResultById(1L);
		// Make a change
		//evaluationResultExistent.setDescription("Rule changed");
		//evaluationResultList.add(evaluationResultExistent);
		
		// Perform a POST on every evaluationResult from list
		for (EvaluationResult evaluationResult : evaluationResultList) {
			this.invokePostEvaluationResult(evaluationResult);
		}
	}

	/**
	 * Using Apache Wink Invoke a DELETE method
	 * 
	 * @param evaluationResult
	 * @return
	 */
	public void invokeDeleteEvaluationResult(EvaluationResult evaluationResult) {

		System.out.println("****Invoking the invokeDeleteEvaluationResult ****");
		long evaluationResultId = evaluationResult.getIdEvaluationResult();
		// Create the rest client instance
		RestClient client = new RestClient();

		// Create the resource instance to interact with
		Resource resource = client
				.resource("http://localhost:9080/edu-test-9/rest/evaluationResults/" + evaluationResultId);

		// Perform a DELETE on the resource
		resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();

	}

	/*
	 * //All below work with JAX-RS 2.0 Client
	 * 
	 * public void invokeGetEvaluationResultById(long evaluationResultId) {
	 * System.out.println("****Enter invokeGetEvaluationResultById****");
	 * 
	 * //Obtain the instance of Client which will be entry point for invoking
	 * REST Services. //It instantiates a preinitialized Client that you can use
	 * right away Client client = ClientBuilder.newClient(); //Target the
	 * RESTful Webservice we want to //invoke by capturing it in WebTarget
	 * instance. WebTarget webTarget =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationResults/" +
	 * evaluationResultId);
	 * 
	 * //Build the request i.e a GET request to the RESTful Webservice defined
	 * //by the URI in the WebTarget instance. Invocation invocation =
	 * webTarget.request().buildGet();
	 * 
	 * //Invok the request to the RESTful API and capturing the Response.
	 * Response response = invocation.invoke();
	 * 
	 * //Return the XML data which can be unmarshalled //into the instance of
	 * Books by using JAXB. EvaluationResult evaluationResult =
	 * response.readEntity(EvaluationResult.class); System.out.println(
	 * "evaluationResult ID= "+evaluationResult.getIdEvaluationResult());
	 * 
	 * }
	 * 
	 * public void invokeGetAllEvaluationResult() { System.out.println(
	 * "****Enter invokeGetAllEvaluationResult****");
	 * 
	 * //Obtain the instance of Client which will be entry point for invoking
	 * REST Services. Client client = ClientBuilder.newClient();
	 * //List<EvaluationResult> evaluationResultList =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationResults")
	 * 
	 * // .get(new GenericType<List<EvaluationResult>>(){}); //Target the RESTful
	 * Webservice we want to //invoke by capturing it in WebTarget instance.
	 * WebTarget webTarget =
	 * client.target("http://localhost:9080/edu-test-8/rest/evaluationResults");
	 * 
	 * //Build the request i.e a GET request to the RESTful Webservice defined
	 * //by the URI in the WebTarget instance. Invocation invocation =
	 * webTarget.request().buildGet();
	 * 
	 * //Invok the request to the RESTful API and capturing the Response.
	 * Response response = invocation.invoke(); //System.out.println(
	 * "response all= "+response); //Return the XML data which can be
	 * unmarshalled //into the instance of Books by using JAXB.
	 * List<EvaluationResult> evaluationResultList = response.readEntity(new
	 * GenericType<List<EvaluationResult>>(){}); //System.out.println(
	 * "evaluationResult all= "+evaluationResult); //System.out.println(
	 * "evaluationResult ID= "+evaluationResult.getIdEvaluationResult());
	 * 
	 * //List<EvaluationResult> evaluationResultList =
	 * response.readEntity(EvaluationResult.class); System.out.println(
	 * "evaluationResult list size ID= "+evaluationResultList.size());
	 * for(EvaluationResult evaluationResult:evaluationResultList){
	 * System.out.println("evaluationResult id="
	 * +evaluationResult.getIdEvaluationResult()); }
	 * 
	 * }
	 * 
	 * public void addEvaluationResultRest(EvaluationResult evaluationResult) {
	 * 
	 * System.out.println("****Invoking the addEvaluationResultRest ****");
	 * 
	 * // Obtaining the instance of Client which will be entry point to //
	 * invoking REST Services. Client client = ClientBuilder.newClient(); //
	 * Targeting the RESTful Webserivce we want to // invoke by capturing it in
	 * WebTarget instance. //Bind the target to the REST service’s URL using the
	 * target() method: WebTarget target = client.target(
	 * "http://localhost:9080/edu-test-8/rest/evaluationResults/testOneRule");
	 * Response response =
	 * target.request().buildPost(Entity.entity(evaluationResult,
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
