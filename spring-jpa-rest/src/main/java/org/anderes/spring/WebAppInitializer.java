package org.anderes.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Durch diese Klasse werden Spring-Dispatcher und Context-Listener dem
 * Servlet-Context hunzugefügt. Dadurch ist ein web.xml Datei für Spring nicht
 * mehr notwendig. Ein entsprechender WebApplication-Context für Spring
 * wird ebenfalls erzeugt und Konfiguriert.
 * 
 * @author René Anderes
 *
 */
public class WebAppInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("org.anderes.spring.configuration");

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));

        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
