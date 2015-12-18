package ro.fortech.edu.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the EVALUATION_RULE database table.
 * 
 */
@Cacheable(true)
@Entity
@Table(name="EVALUATION_RULE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="EvaluationRule.findAll", query="SELECT e FROM EvaluationRule e")
public class EvaluationRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_NR1")
	//@SequenceGenerator(name="SEQ_NR1",sequenceName="SEQ_NR1")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_EVALUATION_RULE")
	private Long idEvaluationRule;

	private String description;

	@Column(name="MARKET_RULE_ID")
	private long marketRuleId;

	//bi-directional many-to-one association to RuleActivity
	@OneToMany(mappedBy="evaluationRule", cascade=CascadeType.ALL)
	@XmlElement
	private List<RuleActivity> ruleActivities = new ArrayList<>();

	//bi-directional many-to-one association to RuleCondition
	@OneToMany(mappedBy="evaluationRule", cascade=CascadeType.ALL)
	@XmlElement
	private List<RuleCondition> ruleConditions = new ArrayList<>();

	public EvaluationRule() {
	}

	public Long getIdEvaluationRule() {
		return this.idEvaluationRule;
	}

	public void setIdEvaluationRule(Long idEvaluationRule) {
		this.idEvaluationRule = idEvaluationRule;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getMarketRuleId() {
		return this.marketRuleId;
	}

	public void setMarketRuleId(long marketRuleId) {
		this.marketRuleId = marketRuleId;
	}

	public List<RuleActivity> getRuleActivities() {
		return this.ruleActivities;
	}

	public void setRuleActivities(List<RuleActivity> ruleActivities) {
		this.ruleActivities = ruleActivities;
	}

	public RuleActivity addRuleActivity(RuleActivity ruleActivity) {
		getRuleActivities().add(ruleActivity);
		ruleActivity.setEvaluationRule(this);

		return ruleActivity;
	}

	public RuleActivity removeRuleActivity(RuleActivity ruleActivity) {
		getRuleActivities().remove(ruleActivity);
		ruleActivity.setEvaluationRule(null);

		return ruleActivity;
	}

	public List<RuleCondition> getRuleConditions() {
		return this.ruleConditions;
	}

	public void setRuleConditions(List<RuleCondition> ruleConditions) {
		this.ruleConditions = ruleConditions;
	}

	public RuleCondition addRuleCondition(RuleCondition ruleCondition) {
		getRuleConditions().add(ruleCondition);
		ruleCondition.setEvaluationRule(this);

		return ruleCondition;
	}

	public RuleCondition removeRuleCondition(RuleCondition ruleCondition) {
		getRuleConditions().remove(ruleCondition);
		ruleCondition.setEvaluationRule(null);

		return ruleCondition;
	}

}