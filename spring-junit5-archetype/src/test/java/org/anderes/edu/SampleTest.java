package org.anderes.edu;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.anderes.edu.config.JunitSpringConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { JunitSpringConfig.class })
public class SampleTest {

    @Inject
    private List<Device> deviceList;
    @Mock
    private Device device;
    
    @Test
    void checkList() {
        assertThat(deviceList, is(not(nullValue())));
        assertThat(device, is(not(nullValue())));
    }

}
