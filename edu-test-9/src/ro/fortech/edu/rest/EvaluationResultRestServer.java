package ro.fortech.edu.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.controller.EvaluationRuleController;
import ro.fortech.edu.model.EvaluationResult;
import ro.fortech.edu.service.EvaluationResultService;

/**
 * 
 * This class produces a RESTful service to read/write evaluationResult 
 */
@Path("/evaluationResults")
@Stateless
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class EvaluationResultRestServer {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	

	@EJB
	private EvaluationResultService evaluationResultService;

	@GET 	
	public Response getAllEvaluationResults() {
		//return evaluationResultService.findAllEvaluationResults();		
		GenericEntity<List<EvaluationResult>> evaluationResultWrapper = new GenericEntity<List<EvaluationResult>>(
				evaluationResultService.findAllEvaluationResults()) {
		};
		return Response.ok(evaluationResultWrapper).build();
	}
	
	@GET
    @Path("/{id}")    
    public EvaluationResult getEvaluationResultById(@PathParam("id") long id) {
		EvaluationResult evaluationResult = evaluationResultService.findEvaluationResultById(id);
        if (evaluationResult == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return evaluationResult;
    }
	
	@DELETE
    @Path("/{id}")    
    public void DeleteEvaluationResultById(@PathParam("id") long id) {
		EvaluationResult evaluationResult = evaluationResultService.findEvaluationResultById(id);
        if (evaluationResult == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else{
        	try {
				evaluationResultService.delete(evaluationResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }
	
	
	@POST
	@Path("/evaluationResult")	
	public Response postEvaluationResult(EvaluationResult evaluationResult){
		if ( evaluationResult == null){
			throw new WebApplicationException(Response.Status.NOT_FOUND);		
		 	}
		try {
			if(evaluationResult.getIdEvaluationResult()==null){
				// New EvaluationResult 			
				evaluationResultService.register(evaluationResult);
			}else{
				//EvaluationResult id not null
				//Check if there is an EvaluationResult in db having this id
				if(evaluationResultService.findEvaluationResultById(evaluationResult.getIdEvaluationResult())==null){
					//No EvaluationResult in db having this id
					// So it is a new EvaluationResult having id already set
					evaluationResultService.register(evaluationResult);
				}else{
					//There is an EvaluationResult in db having this id
					//So let's update it
					evaluationResultService.update(evaluationResult);
				}
					
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return Response.status(400).entity(
					"EvaluationResult create/update failed!").build();
		}
		
		return Response.ok(evaluationResult).build();
		
	}
	
	
	
	
}
