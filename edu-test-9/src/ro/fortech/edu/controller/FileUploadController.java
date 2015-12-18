package ro.fortech.edu.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.fortech.edu.model.EvaluationRule;
import ro.fortech.edu.model.EvaluationRules;
import ro.fortech.edu.rest.EvaluationRuleRestClient;

@RequestScoped
@ManagedBean(name = "fileUploadController")
public class FileUploadController {
	
	private static final String TEMPORARY_FILE_ABSOLUTE_PATH = "E:\\Workspaces\\Websphere-5\\edu-test-9\\docs\\tmp";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TEMPORARY_FILE_NAME = "tmpfile.xml";
	private static final Log apacheLog = LogFactory.getLog(EvaluationRuleController.class);
	private static final Logger javaLog = Logger.getLogger(EvaluationRuleController.class.getName());
	
	
	@EJB
	private EvaluationRuleRestClient evaluationRuleRestClient; 
	
	
	private String textFileContent;
	
	private Part uploadedFile;
	public Part getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public String getTextFileContent() {
		return textFileContent;
	}
	public void setTextFileContent(String textFileContent) {
		this.textFileContent = textFileContent;
	}
	
	public void upload() {
		System.out.println("Enter upload()");
        if (null != uploadedFile) {
            try {
                InputStream inputStream = uploadedFile.getInputStream();
                /*
                StringBuilder fileContentStringBuilder = new StringBuilder("");
                BufferedReader  reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                while(line != null){
                    System.out.println(line);
                    fileContentStringBuilder.append(line);
                    fileContentStringBuilder.append(LINE_SEPARATOR);
                    line = reader.readLine();
                }          

    
                
                System.out.println("fileContentStringBuilder="+fileContentStringBuilder);
                
                */
                
                textFileContent = new Scanner(inputStream).useDelimiter("\\A").next();
                //textFileContent = fileContentStringBuilder.toString();
                System.out.println("textFileContent="+textFileContent);
                
                File uploads = new File(TEMPORARY_FILE_ABSOLUTE_PATH);
                File file = new File(uploads, TEMPORARY_FILE_NAME);                
                Files.copy(inputStream, file.toPath());               
                
                //inputStream = uploadedFile.getInputStream();
                
            } catch (IOException ex) {
            }
        }
        
        //return "viewUploadedFile.xhtml";
    }
	
	public List<EvaluationRule> unmarshallingEvaluationRuleFromXmlFile() throws JAXBException{
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
	
	public void addBulkEvaluationRuleRest() throws JAXBException{		
		System.out.println("Enter addBatchEvaluationRuleRest");
		//Create the xml file by marshaling some EvaluationRule
		//marshallingEvaluationRuleToXmlFile();		
		evaluationRuleRestClient.invokePostBatchEvaluationRule(unmarshallingEvaluationRuleFromXmlFile());;
	}

}
