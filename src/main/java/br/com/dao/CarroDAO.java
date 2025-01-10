package br.com.dao;

import br.com.entity.Carro;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object para a entidade Carro.
 */
public class CarroDAO {

    private static final Logger logger = LoggerFactory.getLogger(CarroDAO.class);
    private EntityManager manager;

    // Passando o tipo da classe para DAO GERAL
    public CarroDAO() {
        var factory = Persistence.createEntityManagerFactory("br.com.crudprojetoweb_crudjpa-webcarros_jar_1.0-SNAPSHOTPU");
        this.manager = factory.createEntityManager();
    }

    /**
     * Método para salvar um Carro.
     */
    public boolean salvar(Carro obj) {
        return executarTransacao(() -> {
            this.manager.persist(obj);
        });
    }

    /**
     * Método para deletar um Carro.
     */
    public boolean deletar(Carro obj) {
        return executarTransacao(() -> {
            this.manager.remove(obj);
        });
    }

    /**
     * Método para atualizar um Carro. Apenas os campos não-nulos são
     * atualizados.
     */
    public boolean atualizar(Carro obj) {
        return executarTransacao(() -> {
            Carro carroExistente = this.manager.find(Carro.class, obj.getId());
            if (carroExistente != null) {
                if (obj.getMarca() != null) {
                    carroExistente.setMarca(obj.getMarca());
                }
                if (obj.getModelo() != null) {
                    carroExistente.setModelo(obj.getModelo());
                }
                if (obj.getAno() != 0) {
                    carroExistente.setAno(obj.getAno());
                }
                if (obj.getCor() != null) {
                    carroExistente.setCor(obj.getCor());
                }
                this.manager.merge(carroExistente);
            }
        });
    }

    /**
     * Método para listar todos os Carros.
     */
    public List<Carro> listar() {
        try {
            String jpql = "SELECT c FROM Carro c";
            TypedQuery<Carro> query = this.manager.createQuery(jpql, Carro.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Erro ao listar carros", e);
            return null;
        }
    }

    /**
     * Método para buscar um Carro por ID utilizando Optional.
     */
    public Carro findId(Integer id) {
        try {
            Carro carro = this.manager.find(Carro.class, id);
            return carro;
        } catch (Exception e) {
            logger.error("Erro ao buscar carro por ID", e);
            return null;
        }
    }

    /**
     * Método para listar carros por marca.
     */
    public List<Carro> listarPorMarca(String marca) {
        try {
            String jpql = "SELECT c FROM Carro c WHERE c.marca = :marca";
            TypedQuery<Carro> query = this.manager.createQuery(jpql, Carro.class);
            query.setParameter("marca", marca);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Erro ao listar carros por marca", e);
            return null;
        }
    }

    /**
     * Método genérico para executar transações.
     */
    private boolean executarTransacao(Runnable transacaoOperacao) {
        EntityTransaction transacao = this.manager.getTransaction();
        try {
            transacao.begin();
            transacaoOperacao.run();
            transacao.commit();
            return true;
        } catch (Exception e) {
            if (transacao.isActive()) {
                transacao.rollback();
            }
            logger.error("Erro ao executar transação", e);
            return false;
        }
    }
}
