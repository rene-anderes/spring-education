package org.anderes.edu.security;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Diese Klasse wird dem Container (z.B. Tomcat oder Jetty) bekannte gemacht.
 * Dies geschieht mittels {@code @WebListener}. Wird der Container gestartet,
 * so wir die Methode {@link #contextInitialized(ServletContextEvent)} aufgerufen.
 * <p>
 * Dabei wird mittels dem {@link SecurityWebApplicationInitializer} 
 * der Spring Security Context geladen und initialisiert.
 * 
 * @author René Anderes
 *
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        sce.getServletContext().setInitParameter("spring.profiles.active", "alternativ");
        SecurityWebApplicationInitializer.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // nothing to do ...
    }

}
