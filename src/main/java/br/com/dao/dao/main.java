package br.com.dao.dao;

import br.com.dao.Entity.Carro;

/**
 *
 * @author carol
 */
public class main {
    public static void main(String[] args) {
//      possivel porque a classe carro dao é implemento da classe carrorepositorio
        CarroDAO carroDao = new CarroDAO();
        
        Carro c = new Carro("Argo", "Fiat", 2021, "branco");
        
        //salvar
        if (carroDao.salvar(c)) {
            System.out.println("Carro salvo no banco de dados");
            System.out.println(c);
        }else{
            System.out.println("Nao salvo, falha no processo");
        }
        Carro c2 = new Carro("Cheb", "fusca", 2000, "amarelo");
        carroDao.salvar(c2);
        //listar
        System.out.println(carroDao.listar());
        carroDao.deletar(c2);
        System.out.println(carroDao.listar());
        carroDao.salvar(new Carro("parati", "chev", 2003, "branco"));
        System.out.println(carroDao.findId(3));
    }
}
