package in.con.redhat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootWebApplication.class)
@WebAppConfiguration
public class SpringBootWebApplicationTest {

	@Test
	public void contextLoads() {
	}

}
