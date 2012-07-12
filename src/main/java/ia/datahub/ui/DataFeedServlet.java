package ia.datahub.ui;

import ia.datahub.scraper.PodracerScraper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

/**
 * A one-button data feed initiation UI
 */
public class DataFeedServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
		PrintWriter pw = resp.getWriter();
		if (req.getParameter("op") == null)
		{
			// Invoked with no parameter - initial page
			pw.println("<h1>Data Feed</h1>");
			pw.println("<form action='DataFeedServlet' method='get'>");
			pw.println("<input type='submit' value='Update'/>");
			pw.println("<input type='hidden' name='op' value='Update'/>");
			pw.println("</form>");
		} else
		{
			// Invoked with a parameter - run the update
			pw.println("<form action='DataFeedServlet' method='get'>");
			pw.println("<input type='submit' value='Update again'/>");
			pw.println("<input type='hidden' name='op' value='Update'/>");
			pw.println("</form>");
			pw.println("<p>");
			List<Map<String, String>> results = PodracerScraper.scrapePodracers();
			for (Map<String, String> podracer : results)
			{
				pw.println("<h3>" + podracer.get("Model") + "</h3>");
				for (Entry<String, String> e : podracer.entrySet())
				{
					pw.println("<br>" + e.getKey() + ":" + e.getValue());
				}
			}
			pw.println("</p>");
		}
    }

	// A container-less entry point - starts a simple jetty handler
	public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new DataFeedServlet()),"/*");
        server.start();
        server.join();
	}

}
