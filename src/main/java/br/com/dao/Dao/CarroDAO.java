package br.com.dao.Dao;

import br.com.dao.Entity.Carro;
import br.com.dao.Interface.CarroRepositorio;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author carol
 */
public class CarroDAO
        extends DataAccessObject<Carro>
        implements CarroRepositorio {

    //passando o tipo da classe para DAO GERAL
    public CarroDAO() {
        super(Carro.class);
    }

    @Override
    public boolean atualizar(Carro obj) {
        EntityTransaction transacao = this.manager.getTransaction();
        try {
            transacao.begin();
            // Buscando o carro no banco de dados usando o ID
            Carro carroExistente = this.manager.find(Carro.class, obj.getId());

            if (carroExistente != null) {
                // Atualizando os dados do carro com os novos dados
                carroExistente.setMarca(obj.getMarca());
                carroExistente.setModelo(obj.getModelo());
                carroExistente.setAno(obj.getAno());
                // Atualize outros campos conforme necessário

                // O EntityManager irá atualizar automaticamente no banco de dados
                this.manager.merge(carroExistente);
                transacao.commit();
                return true;
            } else {
                transacao.rollback();
                return false;
            }
        } catch (Exception e) {
            transacao.rollback();
            return false;
        }
    }

    @Override
    public List<Carro> buscar(Carro filtro) {
        try {
            // Criando uma consulta baseada no critério de busca (exemplo, buscar por marca)
            String jpql = "SELECT c FROM Carro c WHERE c.marca = :marca";
            return this.manager.createQuery(jpql, Carro.class)
                    .setParameter("marca", filtro.getMarca())
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Carro> listar() {
        try {
            // Consulta que retorna todos os carros
            String jpql = "SELECT c FROM Carro c";
            TypedQuery<Carro> query = this.manager.createQuery(jpql, Carro.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
