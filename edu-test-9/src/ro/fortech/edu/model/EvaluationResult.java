package ro.fortech.edu.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the EVALUATION_RESULT database table.
 * 
 */
@Entity
@Table(name="EVALUATION_RESULT")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="EvaluationResult.findAll", query="SELECT e FROM EvaluationResult e")
public class EvaluationResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_EVALUATION_RESULT")
	private Long idEvaluationResult;

	@Column(name="DATE_OF_EVALUATION")
	private Timestamp dateOfEvaluation; 

	@Column(name="EVALUATION_RULE_IDS_APPLIED")
	private String evaluationRuleIdsApplied;

	@Column(name="EVALUATION_RULE_IDS_NOT_IN_DATABASE")
	private String evaluationRuleIdsNotInDatabase;

	@Column(name="EVALUATION_RULE_IDS_TO_BE_APPLIED")
	private String evaluationRuleIdsToBeApplied;

	@Column(name="EVALUATION_RULES_APPLIED")
	private String evaluationRulesApplied;

	//bi-directional many-to-one association to Vehicle
	@ManyToOne
	@JoinColumn(name="ID_VEHICLE_IND")
	@XmlElement
	private Vehicle vehicle;	
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		   this.vehicle = (Vehicle) parent;
		}

	//bi-directional many-to-one association to EvaluationResultDetail
	@OneToMany(mappedBy="evaluationResult", cascade=CascadeType.ALL)
	@XmlElement
	private List<EvaluationResultDetail> evaluationResultDetails = new ArrayList<>();

	public EvaluationResult() {
	}

	public Long getIdEvaluationResult() {
		return this.idEvaluationResult;
	}

	public void setIdEvaluationResult(Long idEvaluationResult) {
		this.idEvaluationResult = idEvaluationResult;
	}

	public Timestamp getDateOfEvaluation() {
		return this.dateOfEvaluation;
	}

	public void setDateOfEvaluation(Timestamp dateOfEvaluation) {
		this.dateOfEvaluation = dateOfEvaluation;
	}

	public String getEvaluationRuleIdsApplied() {
		return this.evaluationRuleIdsApplied;
	}

	public void setEvaluationRuleIdsApplied(String evaluationRuleIdsApplied) {
		this.evaluationRuleIdsApplied = evaluationRuleIdsApplied;
	}

	public String getEvaluationRuleIdsNotInDatabase() {
		return this.evaluationRuleIdsNotInDatabase;
	}

	public void setEvaluationRuleIdsNotInDatabase(String evaluationRuleIdsNotInDatabase) {
		this.evaluationRuleIdsNotInDatabase = evaluationRuleIdsNotInDatabase;
	}

	public String getEvaluationRuleIdsToBeApplied() {
		return this.evaluationRuleIdsToBeApplied;
	}

	public void setEvaluationRuleIdsToBeApplied(String evaluationRuleIdsToBeApplied) {
		this.evaluationRuleIdsToBeApplied = evaluationRuleIdsToBeApplied;
	}

	public String getEvaluationRulesApplied() {
		return this.evaluationRulesApplied;
	}

	public void setEvaluationRulesApplied(String evaluationRulesApplied) {
		this.evaluationRulesApplied = evaluationRulesApplied;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public List<EvaluationResultDetail> getEvaluationResultDetails() {
		return this.evaluationResultDetails;
	}

	public void setEvaluationResultDetails(List<EvaluationResultDetail> evaluationResultDetails) {
		this.evaluationResultDetails = evaluationResultDetails;
	}

	public EvaluationResultDetail addEvaluationResultDetail(EvaluationResultDetail evaluationResultDetail) {
		getEvaluationResultDetails().add(evaluationResultDetail);
		evaluationResultDetail.setEvaluationResult(this);

		return evaluationResultDetail;
	}

	public EvaluationResultDetail removeEvaluationResultDetail(EvaluationResultDetail evaluationResultDetail) {
		getEvaluationResultDetails().remove(evaluationResultDetail);
		evaluationResultDetail.setEvaluationResult(null);

		return evaluationResultDetail;
	}

}