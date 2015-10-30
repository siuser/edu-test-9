package ro.procont.fc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ro.procont.fc.model.Father;

import java.lang.Boolean;
import java.util.Iterator;

import ro.procont.fc.model.Child;

/**
 * Backing bean for Father entities.
 * <p/>
 * This class provides CRUD functionality for all Father entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class FatherBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Father entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Father father;

	public Father getFather() {
		return this.father;
	}

	public void setFather(Father father) {
		this.father = father;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "father_child_53-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		System.out.println("rretrieve in father bean");
		
		

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.father = this.example;
		} else {
			this.father = findById(getId());
		}
	}

	public Father findById(Integer id) {

		return this.entityManager.find(Father.class, id);
	}

	/*
	 * Support updating and deleting Father entities
	 */

	public String update() {
		System.out.println("ffather in em before cv end="+this.entityManager.contains(this.father));
		this.conversation.end();
		System.out.println("ffather in em aftyer cv end="+this.entityManager.contains(this.father));
		

		try {
			if (this.id == null) {
				this.entityManager.persist(this.father);
				return "search?faces-redirect=true";
			} else {
				
				List<Child> childSet=this.father.getChilds();
				System.out.println("childSet size="+childSet.size());
				
				// Below could be used if no cascade=cascadeType.MERGE in Father entity
				/*
				 * Iterator<Child> iteratorChilds = this.father.getChilds().iterator();
				 * while(iteratorChilds.hasNext()) { 
				 * 		Child nextInChilds = iteratorChilds.next(); 
				 * 		if(nextInChilds.getId()==null){ 
				 * 			// new child 
				 * 			System.out.println("nnew child");
				 * 			this.entityManager.persist(nextInChilds); 
				 * 		}
				 * 
				 * 	}
				 */
				
				
				
				// Procedure to delete children	
				// In childSetBeforeRefresh we have Father's children collection
				// after deleting a few
				// Only these must exist in db at the end
				
				
				
				
				List<Child> childSetBeforeRefresh=this.father.getChilds();
				System.out.println("childSetBeforeRefresh size="+childSetBeforeRefresh.size());
				
				// In childSetAfterRefresh we have all Father's children from database
				// as they were prior delete any child
				this.entityManager.refresh(this.father);
				List<Child> childSetAfterRefresh=this.father.getChilds();
				System.out.println("childSetAfterRefresh size="+childSetAfterRefresh.size());
				
				// Let's substract childSetBeforeRefresh from childSetAfterRefresh
				// The result are all children to be removed from db
				childSetAfterRefresh.removeAll(childSetBeforeRefresh);
				System.out.println("childSetAfterRefresh after removeAll="+childSetAfterRefresh.size());
				
				// Let's removed children from db
				Iterator<Child> iteratorChildrenToBeDeleted = childSetAfterRefresh.iterator();
				while(iteratorChildrenToBeDeleted.hasNext()) { 
					Child nextInChilds = iteratorChildrenToBeDeleted.next();	
					System.out.println("Nname to be deleted="+nextInChilds.getName2());
						this.entityManager.remove(nextInChilds); 						 
						 }
				// All Father's children are in childSetBeforeRefresh
				// This is valid for both child add and remove from 
				// Father Collection
				this.father.setChilds(childSetBeforeRefresh);
				
				
				
				this.entityManager.merge(this.father);
				
					
				
				//this.entityManager.setFlushMode(FlushModeType.COMMIT);
				//this.entityManager.flush();
				
				
				
				return "view?faces-redirect=true&id=" + this.father.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Father deletableEntity = findById(getId());
			Iterator<Child> iterChilds = deletableEntity.getChilds().iterator();
			for (; iterChilds.hasNext();) {
				Child nextInChilds = iterChilds.next();
				nextInChilds.setFather(null);
				iterChilds.remove();
				this.entityManager.merge(nextInChilds);
			}
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Father entities with pagination
	 */

	private int page;
	private long count;
	private List<Father> pageItems;

	private Father example = new Father();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Father getExample() {
		return this.example;
	}

	public void setExample(Father example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Father> root = countCriteria.from(Father.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Father> criteria = builder.createQuery(Father.class);
		root = criteria.from(Father.class);
		TypedQuery<Father> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Father> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name1 = this.example.getName1();
		if (name1 != null && !"".equals(name1)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name1")),
					'%' + name1.toLowerCase() + '%'));
		}
		String name2 = this.example.getName2();
		if (name2 != null && !"".equals(name2)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name2")),
					'%' + name2.toLowerCase() + '%'));
		}
		Boolean isTrue1 = this.example.getIsTrue1();
		if (isTrue1 != null) {
			predicatesList.add(builder.equal(root.get("isTrue1"), isTrue1));
		}
		String textContent1 = this.example.getTextContent1();
		if (textContent1 != null && !"".equals(textContent1)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("textContent1")),
					'%' + textContent1.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Father> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Father entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Father> getAll() {

		CriteriaQuery<Father> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Father.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Father.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final FatherBean ejbProxy = this.sessionContext
				.getBusinessObject(FatherBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Integer.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Father) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Father add = new Father();

	public Father getAdd() {
		return this.add;
	}

	public Father getAdded() {
		Father added = this.add;
		this.add = new Father();
		return added;
	}
	
	
	public void setValue1(){
		System.out.println("ffather in em before detached="+this.entityManager.contains(this.father));
		
		this.entityManager.detach(this.father);
		System.out.println("ffather in em after detach="+this.entityManager.contains(this.father));
		
		this.father.setValue1(33.33);
	}
	
	@Remove
	  public void remove()
	  {
	    System.out.println("removed");
	  }

}
