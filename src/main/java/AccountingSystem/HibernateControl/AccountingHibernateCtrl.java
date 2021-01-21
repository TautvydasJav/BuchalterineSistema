package AccountingSystem.HibernateControl;

import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Categories.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AccountingHibernateCtrl {
    public AccountingHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccountingOS accounting) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(accounting);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccounting(accounting.getId()) != null) {
                throw new Exception("accounting: " + accounting + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void edit(AccountingOS accounting) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            accounting = em.merge(accounting);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = accounting.getId();
                if (findAccounting(id) == null) {
                    throw new Exception("The accounting with id " + accounting + " no longer exists.");
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
            AccountingOS accounting;
            try {
                accounting = em.getReference(AccountingOS.class, id);
                accounting.getId();
                em.merge(accounting);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The accounting with name " + id + " no longer exists.", enfe);
            }
            em.remove(accounting);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccountingOS> findAccountingOSEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccountingOS.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AccountingOS findFirstAccountingTable() {
        EntityManager em = getEntityManager();
        List<AccountingOS> list;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccountingOS.class));
            Query q = em.createQuery(cq);
            list = q.getResultList();
        } finally {
            em.close();
        }
        return list.get(0);
    }

    public AccountingOS findAccounting(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccountingOS.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccountingOSCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccountingOS> rt = cq.from(AccountingOS.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void removeTopCategory(AccountingOS accountingOS, Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                accountingOS.removeTopCategory(category);
                em.merge(accountingOS);
                em.merge(category);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing category from accounting", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
