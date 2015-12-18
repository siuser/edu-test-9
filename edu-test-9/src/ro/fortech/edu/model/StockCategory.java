package ro.fortech.edu.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the STOCK_CATEGORY database table.
 * 
 */
@Entity
@Table(name="STOCK_CATEGORY")
@NamedQuery(name="StockCategory.findAll", query="SELECT s FROM StockCategory s")
public class StockCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_STOCK_CATEGORY")
	private long idStockCategory;

	private String description;

	@Column(name="\"NAME\"")
	private String name;

	public StockCategory() {
	}

	public long getIdStockCategory() {
		return this.idStockCategory;
	}

	public void setIdStockCategory(long idStockCategory) {
		this.idStockCategory = idStockCategory;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}