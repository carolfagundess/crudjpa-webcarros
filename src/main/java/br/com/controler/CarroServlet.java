package br.com.controler;

import br.com.dao.Entity.Carro;
import br.com.dao.dao.CarroDAO;
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
@WebServlet("/api-carros/*") // Usando '*' para capturar qualquer caminho após '/api-carros/'
public class CarroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo(); // Captura o caminho após /api-carros/
        CarroDAO carroDao = new CarroDAO();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Retorna todos os carros se não houver ID na URL
            listarCarros(response, carroDao);
        } else {
            // Retorna carro pelo ID se houver
            obterCarroPorId(request, response, pathInfo, carroDao);
        }
    }

    private void listarCarros(HttpServletResponse response, CarroDAO carroDao) throws IOException {
        List<Carro> carros = carroDao.listar();
        if (carros != null && !carros.isEmpty()) {
            String carrosJson = gson.toJson(carros);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(carrosJson);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"Nenhum carro encontrado\"}");
        }
    }

    private void obterCarroPorId(HttpServletRequest request, HttpServletResponse response, String pathInfo, CarroDAO carroDao) throws IOException {
        String[] pathParts = pathInfo.split("/");

        // Verifica se o ID está presente na URL
        if (pathParts.length < 2 || pathParts[1].isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID do carro é obrigatório\"}");
            return;
        }

        // Converte o ID do carro da URL
        int carroId = parseCarroId(pathParts[1], response);
        if (carroId < 0) {
            return; // Se o ID for inválido, retorna e não faz mais nada
        }

        // Busca o carro pelo ID
        Carro carro = carroDao.findId(carroId);
        if (carro != null) {
            String carroJson = gson.toJson(carro);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(carroJson);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\": \"Carro não encontrado\"}");
        }
    }

    private int parseCarroId(String idString, HttpServletResponse response) throws IOException {
        try {
            int carroId = Integer.parseInt(idString);
            if (carroId <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"message\": \"ID do carro deve ser um número positivo\"}");
                return -1;
            }
            return carroId;
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID inválido\"}");
            return -1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Lê o corpo da requisição
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // Converte o JSON recebido em um objeto Carro
        Carro carro = gson.fromJson(requestBody, Carro.class);

        CarroDAO carroDao = new CarroDAO();

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

        // Verifica se o ID do carro está presente
        int carroId = parseCarroId(pathParts[1], response);
        if (carroId < 0) {
            return;
        }

        // Lê o corpo da requisição e converte o JSON para o objeto Carro
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Carro carroAtualizado = gson.fromJson(requestBody, Carro.class);
        carroAtualizado.setId(carroId); // Define o ID no objeto para garantir a consistência

        CarroDAO carroDao = new CarroDAO();

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

        // Obtém o id do carro a ser deletado da query string
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            CarroDAO carroDao = new CarroDAO();
            Carro carroDeletar = carroDao.findId(Integer.valueOf(id)); // Obtém o objeto Carro pelo ID

            if (carroDeletar != null && carroDao.deletar(carroDeletar)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{\"message\": \"Carro deletado com sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("{\"message\": \"Erro ao deletar carro\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"ID do carro é necessário\"}");
        }
    }

}
