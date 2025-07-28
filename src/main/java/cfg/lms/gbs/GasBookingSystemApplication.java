package cfg.lms.gbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cfg.lms.gbs"})
@EntityScan("cfg.lms.gbs")
@EnableJpaRepositories("cfg.lms.gbs")
public class GasBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GasBookingSystemApplication.class, args);
	}

}
