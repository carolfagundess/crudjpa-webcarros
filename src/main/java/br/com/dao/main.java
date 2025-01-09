package br.com.dao;

import br.com.dao.Dao.CarroDAO;
import br.com.dao.Entity.Carro;
import br.com.dao.Interface.CarroRepositorio;

/**
 *
 * @author carol
 */
public class main {
    public static void main(String[] args) {
//      possivel porque a classe carro dao é implemento da classe carrorepositorio
        CarroRepositorio repo = new CarroDAO();
        
        Carro c = new Carro("Argo", "Fiat", 2021, "branco");
        
        //salvar
        if (repo.salvar(c)) {
            System.out.println("Carro salvo no banco de dados");
            System.out.println(c);
        }else{
            System.out.println("Nao salvo, falha no processo");
        }
        Carro c2 = new Carro("Cheb", "fusca", 2000, "amarelo");
        repo.salvar(c2);
        //listar
        System.out.println(repo.listar());
        //get
        System.out.println(repo.getCarro(2));
    }
}
