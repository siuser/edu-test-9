package ro.fortech.edu.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MARKET_RULE database table.
 * 
 */
@Entity
@Table(name="MARKET_RULE")
@NamedQuery(name="MarketRule.findAll", query="SELECT m FROM MarketRule m")
public class MarketRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_MARKET_RULE")
	private long idMarketRule;

	private int branch;

	@Column(name="COUNTRY_NUMBER")
	private String countryNumber;

	@Column(name="IS_ACTIVE")
	private boolean isActive;

	@Column(name="STOCK_CATEGORY")
	private String stockCategory;

	public MarketRule() {
	}

	public long getIdMarketRule() {
		return this.idMarketRule;
	}

	public void setIdMarketRule(long idMarketRule) {
		this.idMarketRule = idMarketRule;
	}

	public int getBranch() {
		return this.branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}

	public String getCountryNumber() {
		return this.countryNumber;
	}

	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getStockCategory() {
		return this.stockCategory;
	}

	public void setStockCategory(String stockCategory) {
		this.stockCategory = stockCategory;
	}

}