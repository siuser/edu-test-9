package ro.fortech.edu.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class EvaluationRules {
	
	@XmlElement
    private List<EvaluationRule> evaluationRule= null;
 
    public List<EvaluationRule> getEvaluationRule() {
        return evaluationRule;
    }
 
    public void setEvaluationRule(List<EvaluationRule> evaluationRule) {
        this.evaluationRule = evaluationRule;
    }

}
