package prgrms.marco.be02marbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("prgrms.marco.be02marbox.config")
public class MarBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarBoxApplication.class, args);
	}

}
