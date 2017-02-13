/**
 * 
 */
package com.anthem.nimbus.platform.core.process.defaults;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.anthem.nimbus.platform.core.process.PlatformProcessEngineConfiguration;
import com.anthem.nimbus.platform.core.process.api.ActivitiProcessGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultProcessGateway;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {PlatformProcessEngineConfiguration.class})
@ActiveProfiles("local")
public class DefaultProcessGatewayTest {

	@Autowired @Qualifier("default.processGateway")
	DefaultProcessGateway processGateway;
	
	@Autowired
	ActivitiProcessGateway activitiProcessGateway;
	
	@Test
	public void t_() {
		assertNotNull(processGateway);
		
		assertNotNull(processGateway.getDelegates());
		
		assertTrue(processGateway.getDelegates().contains(activitiProcessGateway));
	}
}
