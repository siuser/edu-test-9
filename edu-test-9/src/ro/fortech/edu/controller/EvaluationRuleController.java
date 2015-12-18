package ro.fortech.edu.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.model.EvaluationRules;
import ro.fortech.edu.model.RuleActivity;
import ro.fortech.edu.model.RuleCondition;
import ro.fortech.edu.rest.EvaluationRuleRestClient;
import ro.fortech.edu.rest.EvaluationRuleRestServer;
import ro.fortech.edu.service.EvaluationRuleService;

@RequestScoped
@ManagedBean(name = "evaluationRuleController")
public class EvaluationRuleController {
	
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	private static final String TEMPORARY_FILES_ABSOLUTE_PATH = "E:\\Workspaces\\Websphere-5\\edu-test-9\\docs\\tmp";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TEMPORARY_FILE_NAME = "tmpfile";
	private static final String TEMPORARY_FILE_EXTENSION = ".xml";
	private static final String EVALUATION_RULES_XML_GENERATED_FILE = "E:\\Workspaces\\Websphere-5\\edu-test-9\\docs\\bulkEvaluationRules.xml";
	
	
	@EJB
	private EvaluationRuleService evaluationRuleService;
	
	@EJB
	private EvaluationRuleRestServer evaluationRuleRestServer;
	
	@EJB
	private EvaluationRuleRestClient evaluationRuleRestClient;
	

	
	private EvaluationRule newEvaluationRule;
	
	public EvaluationRule getNewEvaluationRule() {
		return newEvaluationRule;
	}

	public void setNewEvaluationRule(EvaluationRule newEvaluationRule) {
		this.newEvaluationRule = newEvaluationRule;
	}
	 
	private List<EvaluationRule> evaluationRuleList;	

	public List<EvaluationRule> getEvaluationRuleList() {
		return evaluationRuleList;
	}

	public void setEvaluationRuleList(List<EvaluationRule> evaluationRuleList) {
		this.evaluationRuleList = evaluationRuleList;
	}

	@PostConstruct
	public void initNewEvaluationRule() {
		newEvaluationRule = new EvaluationRule();
		getAllEvaluationRuleList();
		viewHide = false;
	}
	
	
	private Part uploadedFile;
	public Part getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	private String textFileContent;
	public String getTextFileContent() {
		return textFileContent;
	}
	public void setTextFileContent(String textFileContent) {
		this.textFileContent = textFileContent;
	}	

	private long idMarketRule;

	public long getIdMarketRule() {
		return idMarketRule;
	}

	public void setIdMarketRule(long idMarketRule) {
		this.idMarketRule = idMarketRule;
	}
	
	private long idEvaluationRule;
	
	public long getIdEvaluationRule() {
		return idEvaluationRule;
	}

	public void setIdEvaluationRule(long idEvaluationRule) {
		this.idEvaluationRule = idEvaluationRule;
	}
	
	private String temporaryFilePath;
	
	

	public String getTemporaryFilePath() {
		return temporaryFilePath;
	}

	public void setTemporaryFilePath(String temporaryFilePath) {
		this.temporaryFilePath = temporaryFilePath;
	}
	
	private boolean viewHide;
	
	

	public boolean isViewHide() {
		return viewHide;
	}

	public void setViewHide(boolean viewHide) {
		this.viewHide = viewHide;
	}

	/**
	 * 
	 * @return A List of all EvaluationRule
	 */
	public List<EvaluationRule> getAllEvaluationRuleList() {		
		apacheLog.info("Enter getAllEvaluationRuleList method");		
		//System.out.println("Rules nr= "+evaluationRuleService.findAllEvaluationRules().size());
		return evaluationRuleService.findAllEvaluationRules();
	}

	public void register() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			evaluationRuleService.register(newEvaluationRule);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
			facesContext.addMessage(null, m);
			initNewEvaluationRule();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
			facesContext.addMessage(null, m);
		}
	}
	
	public void getEvaluationRuleByIdRest(){
		System.out.println("Enter getEvaluationRuleByIdRest");
		long evaluationRuleId = 1;		
		evaluationRuleRestClient.invokeGetEvaluationRuleById(evaluationRuleId);
	}
	
	public void getAllEvaluationRuleRest(){
		System.out.println("Enter getAllEvaluationRuleRest");
		evaluationRuleRestClient.invokeGetAllEvaluationRule();
	}
	
	public void addEvaluationRuleRest(){
		System.out.println("Enter addEvaluationRuleRest");
		//Data should come from other sources
		EvaluationRule evaluationRule = new EvaluationRule();	
		evaluationRule.setMarketRuleId(100);
		evaluationRuleRestClient.invokePostEvaluationRule(evaluationRule);;
	}
	
	public void deleteEvaluationRuleRest(EvaluationRule evaluationRule){
		System.out.println("Enter deleteEvaluationRuleRest");
		//Data should come from other sources
		//EvaluationRule evaluationRule = new EvaluationRule();	
		//evaluationRule.setMarketRuleId(100);
		evaluationRuleRestClient.invokeDeleteEvaluationRule(evaluationRule);;
	}
	
	public String marshallingEvaluationRuleToXmlFile() throws JAXBException{
		System.out.println("Enter marshallingEvaluationRuleToXmlFile method");
		javaLog.log(Level.INFO,"Enter marshallingEvaluationRuleToXmlFile method");
		javaLog.log(Level.INFO,"File = "+EVALUATION_RULES_XML_GENERATED_FILE);
		evaluationRuleService.marshallingEvaluationRuleToXmlFile(EVALUATION_RULES_XML_GENERATED_FILE);
		return "evaluationRuleList.xhtml";
	}
	
	public List<EvaluationRule> unmarshallingEvaluationRuleFromXmlFile() throws JAXBException{
		String temporaryFilePath = TEMPORARY_FILES_ABSOLUTE_PATH+"\\"+TEMPORARY_FILE_NAME+TEMPORARY_FILE_EXTENSION;
		System.out.println("temporaryFilePath = "+temporaryFilePath);
		
	    return evaluationRuleService.unmarshallingEvaluationRuleFromXmlFile(temporaryFilePath);
	}
	
	public String  addBulkEvaluationRuleRest() throws JAXBException{		
		String temporaryFilePath = TEMPORARY_FILES_ABSOLUTE_PATH+"\\"+TEMPORARY_FILE_NAME+TEMPORARY_FILE_EXTENSION;
		apacheLog.info("temporaryFilePath = "+temporaryFilePath);		
		//Create the xml file by marshaling some EvaluationRule
		//marshallingEvaluationRuleToXmlFile();		
		evaluationRuleRestClient.invokePostBatchEvaluationRule(evaluationRuleService.unmarshallingEvaluationRuleFromXmlFile(temporaryFilePath));;
		return "evaluationRuleList.xhtml";  
	}
	
	public void upload() {
		System.out.println("Enter upload()");
        if (null != uploadedFile) {
            try {
                InputStream inputStream = uploadedFile.getInputStream();             
                
                textFileContent = new Scanner(inputStream).useDelimiter("\\A").next();
                //At the moment we can use only one Evaluation Rule
                int start = textFileContent.indexOf("<evaluationRule>");
                textFileContent = textFileContent.substring(start);
                System.out.println("textFileContent="+textFileContent); 
                int end = textFileContent.indexOf("</evaluationRule>")+"</evaluationRule>".length();
                textFileContent = textFileContent.substring(0,end);
                System.out.println("textFileContent="+textFileContent); 
                inputStream = uploadedFile.getInputStream();
                File uploads = new File(TEMPORARY_FILES_ABSOLUTE_PATH);
                File file = new File(uploads, TEMPORARY_FILE_NAME+TEMPORARY_FILE_EXTENSION);   
                if(file.exists()){
                	System.out.println("file exists");
                	file.delete();
                	System.out.println("file deleted");
                }else{
                	System.out.println("file NOT exists");
                }
                Files.copy(inputStream, file.toPath());               
                String temporaryFilePath = TEMPORARY_FILES_ABSOLUTE_PATH+"\\"+TEMPORARY_FILE_NAME+TEMPORARY_FILE_EXTENSION;
                //Path filePath = Files.createTempFile(TEMPORARY_FILES_ABSOLUTE_PATH,TEMPORARY_FILE_NAME, TEMPORARY_FILE_EXTENSION);
                //Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                
                //Path folder = Paths.get(TEMPORARY_FILES_ABSOLUTE_PATH);
                //Path filePath = Files.createTempFile(folder,TEMPORARY_FILE_NAME, TEMPORARY_FILE_EXTENSION);
                //Path filePath2 = Files.createFile(filePath);
                
                
                //inputStream.reset(); //does not work
                inputStream = uploadedFile.getInputStream();
                //Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                inputStream = uploadedFile.getInputStream();
                //Files.copy(inputStream, filePath2, StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
                //this.temporaryFilePath = filePath.toAbsolutePath().toString();
                System.out.println("temporaryFilePath = "+temporaryFilePath);
                //System.out.println("filePath abs= "+filePath.toAbsolutePath());
                //System.out.println("filePath name= "+filePath.getFileName());
        		//List<EvaluationRule> evaluationRuleList = null;
                try {
                	System.out.println("Tryy");
                	this.evaluationRuleList=evaluationRuleService.unmarshallingEvaluationRuleFromXmlFile(temporaryFilePath);
                	//this.evaluationRuleList=evaluationRuleService.unmarshallingEvaluationRuleFromXmlFile(file);
                	
                	if(this.evaluationRuleList!=null){
                		System.out.println("this.evaluationRuleList size="+this.evaluationRuleList.size());
                	}else{
                		System.out.println("this.evaluationRuleList size= 0");
                    	
                	}
                	//if(!evaluationRuleList.isEmpty()) 
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } catch (IOException ex) {
            }
        }
        
        //return "viewUploadedFile.xhtml";
    }
	
	public List<EvaluationRule> unmarshallingEvaluationRuleFromXmlFile2() throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(EvaluationRules.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    
	    InputStream inputStream = null;
	    System.out.println("this.uploadedFile.getName() = "+this.uploadedFile.getName());
	    System.out.println("this.uploadedFile.getContentType() = "+this.uploadedFile.getContentType());
	    System.out.println("this.uploadedFile.toString() = "+this.uploadedFile.toString());
	    System.out.println("this.uploadedFile.getSize() = "+this.uploadedFile.getSize());
	    try {
	    	
			inputStream = this.uploadedFile.getInputStream();
			
			int data = inputStream.read();
			while(data != -1) {
			  //do something with data...
				System.out.println("data= "+data);

			  data = inputStream.read();
			}
			inputStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //We had written this file in marshalling example
	    EvaluationRules evaluationRules = (EvaluationRules) jaxbUnmarshaller.unmarshal( inputStream );
	    List<EvaluationRule> evaluationRuleList =evaluationRules.getEvaluationRule();
	    return evaluationRuleList;
	}
	


	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}
	
	

}
