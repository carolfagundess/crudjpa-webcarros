package br.com;

import br.com.controler.CarroServlet;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class WebServer {

    public static void main(String[] args) throws Exception {
        // Configura o servidor na porta 8080
        Server server = new Server(8080);

        // Configura o contexto e mapeia as rotas
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Configura o filtro de CORS
        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*"); // Permitir todas as origens
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,DELETE,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Adiciona o Servlet para gerenciar o CRUD
        context.addServlet(new ServletHolder(new CarroServlet()), "/api-carros/*");

        // Configura o handler para arquivos estáticos (frontend)
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("src/main/java/br/com"); // Ajuste o caminho para onde seus arquivos estão
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        // Configura o handler combinado
        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(context);

        // Configura os handlers no servidor
        server.setHandler(handlers);

        // Inicia o servidor
        try {
            server.start();
            System.out.println("Servidor rodando em: http://localhost:8080");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server.isStarted()) {
                server.destroy();
            }
        }
    }
}
