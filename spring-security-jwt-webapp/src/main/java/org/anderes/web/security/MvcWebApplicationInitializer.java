package org.anderes.web.security;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
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
