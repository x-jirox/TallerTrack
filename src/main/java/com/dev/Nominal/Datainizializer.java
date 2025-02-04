package com.dev.Nominal;

import com.dev.Nominal.models.entity.Rol;
import com.dev.Nominal.models.entity.User;
import com.dev.Nominal.models.service.IUsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Datainizializer {

    @Bean
    public CommandLineRunner dataLoader(IUsuarioService usuarioService, BCryptPasswordEncoder encoder) {
        return args -> {
            if (usuarioService.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                String encodedPassword = encoder.encode("123");
                System.out.println("Encoded Password: " + encodedPassword); // Imprimir la contraseña encriptada
                admin.setPassword(encodedPassword);
                admin.setRol(Rol.ADMIN);
                usuarioService.save(admin);
                //hola
            }
        };
    }
}