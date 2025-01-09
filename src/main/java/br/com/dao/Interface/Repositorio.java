package br.com.dao.Interface;

import java.util.List;

/**
 *
 * @author carol
 * @param <T>
 */
public interface Repositorio<T> {
    public boolean salvar(T obj);
    public boolean deletar(T obj);
    public boolean atualizar(T obj);
    public T getCarro(Integer id);
    public List<T> buscar(T obj);
    public List<T> listar(); 
}
