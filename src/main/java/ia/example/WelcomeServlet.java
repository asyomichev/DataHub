package ia.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class WelcomeServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		String dbUrl = System.getenv("DATABASE_URL");
		if (null == dbUrl)
			dbUrl = "none";
			
        resp.getWriter().print("DATABASE_URL=" + dbUrl + "\n");
    }
	
	public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new WelcomeServlet()),"/*");
        server.start();
        server.join();   
	}

}
