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

            // Busca o carro existente pelo ID
            Carro carroExistente = this.manager.find(Carro.class, obj.getId());

            if (carroExistente != null) {
                // Atualiza apenas os campos que foram enviados (não-nulos)
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

                // Persiste as alterações
                this.manager.merge(carroExistente);

                // Finaliza a transação
                transacao.commit();
                return true;
            } else {
                transacao.rollback(); // Não encontrou o carro, desfaz a transação
                return false;
            }
        } catch (Exception e) {
            // Em caso de erro, desfaz a transação e imprime o erro
            if (transacao.isActive()) {
                transacao.rollback();
            }
            e.printStackTrace();
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
