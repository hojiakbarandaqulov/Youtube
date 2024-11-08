package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Youtube ",
                        email = "something@gmal.com",
                        url = "https://kitabu.uz/"
                ),
                description = "This API exposes endpoints to manage tutorials.",
                title = "Interview.uz Management API",
                version = "1.0",
                license = @License(
                        name = "Videohub",
                        url = "https://kitabu.uz/"
                ),
                termsOfService = "Savol javob guruhi: https://t.me/code_uz_group"
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server (
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @io.swagger.v3.oas.annotations.servers.Server(
                        description = "PROD ENV",
                        url = "https://api.kitabu.uz/"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)

public class OpenAPIConfig {
    @Value("${server.url}")
    private String url;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(url);
        devServer.setDescription("Youtube test project");

        Contact contact = new Contact();
        contact.setEmail("Youtube");
        contact.setName("BezKoder");
        contact.setUrl("https://www.bezkoder.com");


        Info info = new Info()
                .title("Youtube Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.")
                .termsOfService("https://www.bezkoder.com/terms")
                .license(null);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
