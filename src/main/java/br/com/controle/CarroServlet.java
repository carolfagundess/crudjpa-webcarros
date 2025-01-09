package br.com.controle;

import br.com.dao.Dao.CarroDAO;
import br.com.dao.Entity.Carro;
import br.com.dao.Interface.CarroRepositorio;
import br.com.dao.Interface.Repositorio;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Lê o corpo da requisição
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // Converte o JSON recebido em um objeto Pessoa
        Carro carro = gson.fromJson(requestBody, Carro.class);
        System.out.println(carro);
        CarroRepositorio carroDao = new CarroDAO();

        if (carroDao.salvar(carro)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("{\"message\": \"true\"}" + "Retorno" + requestBody);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().println("{\"message\": \"false\"}");
        }
    }
    
        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Instancia o DAO para acessar o banco de dados
        CarroRepositorio carroDao = new CarroDAO();

        // Obtém todos os carros do banco
        List<Carro> carros = carroDao.listar();  // Presumindo que você tenha um método 'listar' no seu DAO

        // Converte a lista de carros para JSON e envia como resposta
        String carrosJson = gson.toJson(carros);
        response.getWriter().println(carrosJson);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Extrai o ID do carro da URL
        String pathInfo = request.getPathInfo(); // Obtém a parte extra da URL após '/api/pessoa/'
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID do carro é obrigatório\"}");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        String idString = pathParts[1];  // O ID do carro está na segunda parte da URL
        int carroId = Integer.parseInt(idString);  // Converte o ID para inteiro

        // Lê o corpo da requisição e converte o JSON para o objeto Carro
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Carro carroAtualizado = gson.fromJson(requestBody, Carro.class);

        // Instancia o DAO para acesso ao banco de dados
        CarroRepositorio carroDao = new CarroDAO();

        // Verifica se o carro existe
        Carro carroExistente = (Carro) carroDao.getCarro(carroId);
        if (carroExistente != null) {
            // Atualiza os dados do carro
            carroExistente.setModelo(carroAtualizado.getModelo());
            carroExistente.setAno(carroAtualizado.getAno());
            carroExistente.setCor(carroAtualizado.getCor());
            // ... Atualize os outros campos conforme necessário

            // Salva a atualização no banco de dados
            if (carroDao.atualizar(carroExistente)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{\"message\": \"Carro atualizado com sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("{\"message\": \"Erro ao atualizar o carro\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"Carro não encontrado\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String id = request.getParameter("id");
        Repositorio carroDao = new CarroDAO();
        Carro carrodeletar = (Carro) carroDao.getCarro(Integer.valueOf(id));

        if (id != null && carroDao.deletar(carrodeletar)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"message\": \"true\"}");
            //resp.getWriter().println("true");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"false\"}");
            //resp.getWriter().println("false");
        }
    }
}
