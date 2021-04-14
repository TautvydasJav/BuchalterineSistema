package system.hiberante;

import system.model.statements.Expense;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ExpenseHibernateCtrl {
    public ExpenseHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Expense expense) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(expense);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExpense(expense.getId()) != null) {
                throw new Exception("expense: " + expense + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Expense expense) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            expense = em.merge(expense);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = expense.getId();
                if (findExpense(id) == null) {
                    throw new Exception("expense with id " + expense + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expense expense;
            try {
                expense = em.getReference(Expense.class, id);
                expense.getId();
                em.merge(expense);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("expense with id " + id + " no longer exists.", enfe);
            }
            em.remove(expense);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Expense> findExpenseEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Expense.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Expense findExpense(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Expense.class, id);
        } finally {
            em.close();
        }
    }
}
