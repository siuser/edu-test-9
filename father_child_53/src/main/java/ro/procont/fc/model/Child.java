package ro.procont.fc.model;

// Generated Apr 10, 2015 4:10:43 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.lang.Override;

/**
 * Child generated by hbm2java
 */
@Entity
@Table(name = "child"
      , catalog = "father_child_5")
public class Child implements java.io.Serializable
{

   private Integer id;
   private Father father;
   private Mother mother;
   private String name1;
   private String name2;
   private Double value1;
   private Double value2;
   private Boolean isTrue1;
   private Boolean isTrue2;
   private String textContent1;
   private String textContent2;
   private Date validFrom;
   private Date validTo;

   public Child()
   {
   }

   public Child(String name2)
   {
      this.name2 = name2;
   }

   public Child(Father father, Mother mother, String name1, String name2, Double value1, Double value2, Boolean isTrue1, Boolean isTrue2, String textContent1, String textContent2, Date validFrom, Date validTo)
   {
      this.father = father;
      this.mother = mother;
      this.name1 = name1;
      this.name2 = name2;
      this.value1 = value1;
      this.value2 = value2;
      this.isTrue1 = isTrue1;
      this.isTrue2 = isTrue2;
      this.textContent1 = textContent1;
      this.textContent2 = textContent2;
      this.validFrom = validFrom;
      this.validTo = validTo;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "ID", unique = true, nullable = false)
   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "FATHER_ID")
   public Father getFather()
   {
      return this.father;
   }

   public void setFather(Father father)
   {
      this.father = father;
   }

   @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.MERGE})
   @JoinColumn(name = "MOTHER_ID")
   public Mother getMother()
   {
      return this.mother;
   }

   public void setMother(Mother mother)
   {
      this.mother = mother;
   }

   @Column(name = "NAME1", length = 100)
   public String getName1()
   {
      return this.name1;
   }

   public void setName1(String name1)
   {
      this.name1 = name1;
   }

   @Column(name = "NAME2", nullable = false, length = 100)
   public String getName2()
   {
      return this.name2;
   }

   public void setName2(String name2)
   {
      this.name2 = name2;
   }

   @Column(name = "VALUE1", precision = 22, scale = 0)
   public Double getValue1()
   {
      return this.value1;
   }

   public void setValue1(Double value1)
   {
      this.value1 = value1;
   }

   @Column(name = "VALUE2", precision = 22, scale = 0)
   public Double getValue2()
   {
      return this.value2;
   }

   public void setValue2(Double value2)
   {
      this.value2 = value2;
   }

   @Column(name = "IS_TRUE1")
   public Boolean getIsTrue1()
   {
      return this.isTrue1;
   }

   public void setIsTrue1(Boolean isTrue1)
   {
      this.isTrue1 = isTrue1;
   }

   @Column(name = "IS_TRUE2")
   public Boolean getIsTrue2()
   {
      return this.isTrue2;
   }

   public void setIsTrue2(Boolean isTrue2)
   {
      this.isTrue2 = isTrue2;
   }

   @Column(name = "TEXT_CONTENT1", length = 16777215)
   public String getTextContent1()
   {
      return this.textContent1;
   }

   public void setTextContent1(String textContent1)
   {
      this.textContent1 = textContent1;
   }

   @Column(name = "TEXT_CONTENT2", length = 16777215)
   public String getTextContent2()
   {
      return this.textContent2;
   }

   public void setTextContent2(String textContent2)
   {
      this.textContent2 = textContent2;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "VALID_FROM", length = 19)
   public Date getValidFrom()
   {
      return this.validFrom;
   }

   public void setValidFrom(Date validFrom)
   {
      this.validFrom = validFrom;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "VALID_TO", length = 19)
   public Date getValidTo()
   {
      return this.validTo;
   }

   public void setValidTo(Date validTo)
   {
      this.validTo = validTo;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Child))
      {
         return false;
      }
      Child other = (Child) obj;
      if (id != null)
      {
         if (!id.equals(other.id))
         {
            return false;
         }
      }
      return true;
   }

}