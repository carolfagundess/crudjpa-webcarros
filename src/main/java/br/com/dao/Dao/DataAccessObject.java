package br.com.dao.Dao;

import br.com.dao.Interface.Repositorio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author carol
 * @param <T>
 */
public abstract class DataAccessObject<T> implements Repositorio<T> {

    EntityManager manager;
    private Class type;

    //instanciando a classe entidade
    public DataAccessObject(Class type) {
        var factory = Persistence.createEntityManagerFactory("br.com.crudprojetoweb_crudjpa-webcarros_jar_1.0-SNAPSHOTPU");
        this.manager = factory.createEntityManager();
        this.type = type;
    }

    //CRUD GERAL
    
    //METODO PERSIST
    @Override
    public boolean salvar(T obj) {
        EntityTransaction transcao = this.manager.getTransaction();
        try {
            transcao.begin();
            // salvando um novo objeto
            this.manager.persist(obj);
            transcao.commit();

            return true;
        } catch (Exception e) {
            //cancela a transacao 
            transcao.rollback();
            return false;
        }
    }

    //METODO REMOVE
    @Override
    public boolean deletar(T obj) {
        EntityTransaction transcao = this.manager.getTransaction();
        try {
            transcao.begin();
            // removendo um objeto
            this.manager.remove(obj);
            transcao.commit();

            return true;
        } catch (Exception e) {
            //cancela a transacao 
            transcao.rollback();
            return false;
        }
    }

    //METODO FIND
    @Override
    public T findCarro(Integer id) {
        try {
            T obj = (T)this.manager.find(this.type, id);
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public abstract List<T> buscar(T obj);
}
