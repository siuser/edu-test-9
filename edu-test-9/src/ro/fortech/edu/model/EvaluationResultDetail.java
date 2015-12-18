package ro.fortech.edu.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the EVALUATION_RESULT_DETAIL database table.
 * 
 */
@Entity
@Table(name="EVALUATION_RESULT_DETAIL")
@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="EvaluationResultDetail.findAll", query="SELECT e FROM EvaluationResultDetail e")
public class EvaluationResultDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_EVALUATION_RESULT_DETAIL")
	private long idEvaluationResultDetail;

	@Column(name="ID_EVALUATION_RULE")
	private long idEvaluationRule;

	@Lob
	private String message;

	@Column(name="RULE_STATUS")
	private String ruleStatus;

	//bi-directional many-to-one association to EvaluationResult
	@ManyToOne
	@JoinColumn(name="ID_EVALUATION_RESULT_IND")
	@XmlElement
	private EvaluationResult evaluationResult;
	
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		   this.evaluationResult = (EvaluationResult) parent;
		}

	public EvaluationResultDetail() {
	}

	public long getIdEvaluationResultDetail() {
		return this.idEvaluationResultDetail;
	}

	public void setIdEvaluationResultDetail(long idEvaluationResultDetail) {
		this.idEvaluationResultDetail = idEvaluationResultDetail;
	}

	public long getIdEvaluationRule() {
		return this.idEvaluationRule;
	}

	public void setIdEvaluationRule(long idEvaluationRule) {
		this.idEvaluationRule = idEvaluationRule;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRuleStatus() {
		return this.ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public EvaluationResult getEvaluationResult() {
		return this.evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

}