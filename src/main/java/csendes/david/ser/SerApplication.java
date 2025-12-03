package csendes.david.ser;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SerApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper(){
		ModelMapper m = new ModelMapper();
		return m;
	}

}
