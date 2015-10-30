package ro.procont.fc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ro.procont.fc.model.Mother;

import java.util.Iterator;

import ro.procont.fc.model.Child;

/**
 * Backing bean for Mother entities.
 * <p/>
 * This class provides CRUD functionality for all Mother entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MotherBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Mother entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private Mother mother;

   public Mother getMother()
   {
      return this.mother;
   }

   public void setMother(Mother mother)
   {
      this.mother = mother;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "father_child_53-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.mother = this.example;
      }
      else
      {
         this.mother = findById(getId());
      }
   }

   public Mother findById(Integer id)
   {

      return this.entityManager.find(Mother.class, id);
   }

   /*
    * Support updating and deleting Mother entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.mother);
            return "search?faces-redirect=true";
         }
         else
         {
        	 
        	 Set<Child> addedChildSet = this.mother.getAddedChilds();
						
				
				Iterator<Child> iteratorAddedChildSet = addedChildSet
						.iterator();
				while (iteratorAddedChildSet
						.hasNext()) {
					Child nextAddedChil = iteratorAddedChildSet
							.next();
					System.out.println("NName added child in mother set="
							+ nextAddedChil.getName1());
									

				}
        	 
        	 
        	 
        	 
        	 
            this.entityManager.merge(this.mother);
            return "view?faces-redirect=true&id=" + this.mother.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Mother deletableEntity = findById(getId());
         Iterator<Child> iterChilds = deletableEntity.getChilds().iterator();
         for (; iterChilds.hasNext();)
         {
            Child nextInChilds = iterChilds.next();
            nextInChilds.setMother(null);
            iterChilds.remove();
            this.entityManager.merge(nextInChilds);
         }
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Mother entities with pagination
    */

   private int page;
   private long count;
   private List<Mother> pageItems;

   private Mother example = new Mother();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Mother getExample()
   {
      return this.example;
   }

   public void setExample(Mother example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Mother> root = countCriteria.from(Mother.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Mother> criteria = builder.createQuery(Mother.class);
      root = criteria.from(Mother.class);
      TypedQuery<Mother> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Mother> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("name")), '%' + name.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Mother> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Mother entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Mother> getAll()
   {

      CriteriaQuery<Mother> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Mother.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Mother.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final MotherBean ejbProxy = this.sessionContext.getBusinessObject(MotherBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Mother) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Mother add = new Mother();

   public Mother getAdd()
   {
      return this.add;
   }

   public Mother getAdded()
   {
      Mother added = this.add;
      this.add = new Mother();
      return added;
   }
}
