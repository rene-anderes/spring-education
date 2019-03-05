package org.anderes.edu.config;

import java.util.ArrayList;
import java.util.List;

import org.anderes.edu.Device;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JunitSpringConfig {

    @Bean
    public List<Device> getList() {
        return new ArrayList<>();
    }
}
