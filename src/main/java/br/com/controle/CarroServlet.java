package br.com.controle;

import br.com.dao.Dao.CarroDAO;
import br.com.dao.Entity.Carro;
import br.com.dao.Interface.CarroRepositorio;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author carol
 */
@WebServlet("/api-carros")
public class CarroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Instancia o DAO
        CarroRepositorio carroDao = new CarroDAO();

        // Recupera a lista de carros do banco de dados usando o método listar() do DAO
        List<Carro> carros = carroDao.listar();

        if (carros != null && !carros.isEmpty()) {
            // Converte a lista de carros para JSON
            String carrosJson = gson.toJson(carros);

            // Retorna a lista de carros como resposta
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(carrosJson);
        } else {
            // Caso não haja carros na base de dados
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"Nenhum carro encontrado\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Lê o corpo da requisição
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // Converte o JSON recebido em um objeto Carro
        Carro carro = gson.fromJson(requestBody, Carro.class);

        CarroRepositorio carroDao = new CarroDAO();

        if (carroDao.salvar(carro)) {
            // Retorna um JSON limpo de sucesso
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("{\"success\": true, \"message\": \"Carro cadastrado com sucesso\"}");
        } else {
            // Retorna um JSON limpo de erro
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().println("{\"success\": false, \"message\": \"Erro ao cadastrar o carro\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Extrai o ID do carro da URL
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID do carro é obrigatório\"}");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        String idString = pathParts[1];
        int carroId;

        try {
            carroId = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID inválido\"}");
            return;
        }

        // Lê o corpo da requisição e converte o JSON para o objeto Carro
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Carro carroAtualizado = gson.fromJson(requestBody, Carro.class);

        // Define o ID no objeto para garantir a consistência
        carroAtualizado.setId(carroId);

        // Instancia o DAO
        CarroRepositorio carroDao = new CarroDAO();

        // Tenta atualizar o carroF
        if (carroDao.atualizar(carroAtualizado)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"message\": \"Carro atualizado com sucesso\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"Carro não encontrado ou erro ao atualizar\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Obtém o id do carro a ser deletado
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            CarroRepositorio carroDao = new CarroDAO();
            Carro carroDeletar = carroDao.findCarro(Integer.valueOf(id)); // Obtém o objeto Carro pelo ID

            if (carroDeletar != null && carroDao.deletar(carroDeletar)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{\"message\": \"true\"}"); // Retorno positivo
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("{\"message\": \"false\"}"); // Erro se não encontrar ou não deletar
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID do carro é necessário\"}");
        }
    }

}
