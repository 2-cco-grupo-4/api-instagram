package sptech.picme.instagramapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "PICME API - Instagram",
				description = "API REST que realiza a conexão com o Instagram do Projeto PICME - Grupo 4 de 2CCO de 2023 - SPTech School",
				contact = @Contact(
						name = "Grupo 4",
						url = "https://github.com/2-cco-grupo-4"
				),
				license = @License(name = "UNLICENSED"),
				version = "0.4"
		)
)

//@SecurityScheme(
//		name = "Bearer", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"
//)

@SpringBootApplication
public class InstagramapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramapiApplication.class, args);
	}

}
