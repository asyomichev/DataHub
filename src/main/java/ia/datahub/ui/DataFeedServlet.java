package ia.datahub.ui;

import ia.datahub.beans.Podracer;
import ia.datahub.beans.Recall;
import ia.datahub.rest.RestGetPodracerServlet;
import ia.datahub.scraper.PodracerScraper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import com.sun.jersey.spi.container.servlet.ServletContainer;


/**
 * A one-button data feed initiation UI
 */
public class DataFeedServlet extends HttpServlet {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();

        pw.println("<form action='DataFeedServlet' method='get'>");
        pw.println("<a href='/podracer'>Inspect currently loaded podracer data</a>");
        pw.println("<p><input type='radio' name='op' value='Update'/>Update all from WookieePedia<br/>");
        pw.println("<input type='radio' name='op' value='AddRecalls' checked='true'/>");
        pw.println("Generate additional recall information<br/>");
        pw.println("<input type='submit' value='Run'/>");
        pw.println("</form>");
        pw.println("<p>");

        String op = req.getParameter("op");
        if (op != null && op.equals("Update")) {
            // Invoked with a parameter - run the update
            List<Map<String, String>> results = PodracerScraper.scrapePodracers();
            for (Map<String, String> podracerAttrs : results) {
                pw.println("<h3>" + podracerAttrs.get("Model") + "</h3>");
                for (Entry<String, String> e : podracerAttrs.entrySet()) {
                    pw.println("<br>" + e.getKey() + ":" + e.getValue());
                }

                Podracer podracerBean = new Podracer(podracerAttrs);
                try {
                    em.getTransaction().begin();
                    for (Recall r : podracerBean.getRecalls())
                    {   
                        em.persist(r);
                    }
                    em.persist(podracerBean);
                    em.getTransaction().commit();
                } catch (javax.persistence.EntityExistsException ex) {
                    pw.println("ERROR: duplicate model name, ignored");
                    em.getTransaction().rollback();
                }
                pw.println("<p>" + podracerBean.getRecalls().size() + " recalls</p>");
            }
            pw.println("</p>");
        } else if (op != null && op.equals("AddRecalls")) {
            // Invoked with a parameter - generate more recalls
            try {
                em.getTransaction().begin();
                for (Podracer p : em.createQuery("SELECT p FROM Podracer p", Podracer.class).getResultList())
                {
                    p.generateRecalls();
                    for (Recall r : p.getRecalls())
                    {
                        em.persist(r);
                    }
                    em.persist(p);
                    pw.println("<h3>" + p.model + "</h3>");
                    pw.println("<p>" + p.getRecalls().size() + " recalls</p>");
                }
                em.getTransaction().commit();
            } catch (javax.persistence.EntityExistsException ex) {
                pw.println("ERROR: " + ex.getMessage());
                em.getTransaction().rollback();
            }
            pw.println("</p>");
        }
    }

    // A container-less entry point - starts a simple jetty handler
    public static void main(String[] args) throws Exception {
        emf = Persistence.createEntityManagerFactory("persistenceUnit");
        em = emf.createEntityManager();

        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // Add REST servlets
        context.addServlet(new ServletHolder(new RestGetPodracerServlet(em)), "/podracer/*");
        context.addServlet(new ServletHolder(new DataFeedServlet()), "/*");
        
        // Add OData servlets
        ServletHolder servletOData = new ServletHolder(new ServletContainer());
        servletOData.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "org.odata4j.producer.resources.ODataResourceConfig");
        servletOData.setInitParameter("odata4j.producerfactory", "org.odata4j.heroku.HerokuPostgresProducerFactory");
        context.addServlet(servletOData, "/DataHub.svc/*");

        
        server.start();
        server.join();
    }

}
