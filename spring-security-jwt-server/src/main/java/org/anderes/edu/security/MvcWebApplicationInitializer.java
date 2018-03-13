package org.anderes.edu.security;

import org.anderes.edu.configuration.AppConfig;
import org.anderes.edu.configuration.WebMvcConfig;
import org.anderes.edu.configuration.WebSecurityConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class  };
    }

    @Override
    protected String[] getServletMappings() {
      return new String[] { "/" };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebMvcConfig.class, WebSecurityConfig.class };
    }
    
}
