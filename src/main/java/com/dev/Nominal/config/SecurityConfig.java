package com.dev.Nominal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificar contraseñas
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/#/**") // Excluir ciertas rutas del CSRF si es necesario
                )
                //revisar por si esta mal
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**", "/image/**","/uploads/**").permitAll() // Permitir acceso sin autenticación
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/client/listClient").hasAnyRole("ADMIN")
                                .requestMatchers("/client/formClient/**").hasAnyRole("ADMIN")
                                .requestMatchers("/client/deleteClient/**").hasAnyRole("ADMIN")
                                .requestMatchers("/client/searchClient/**").hasAnyRole("ADMIN")
                                .requestMatchers("/orden/listOrden/**").hasAnyRole("ADMIN")
                                .requestMatchers("/orden/formOrden/**").hasAnyRole("ADMIN")
                                .requestMatchers("/client/**").hasRole("CLIENT")
                                .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/login") // Página de inicio de sesión
                                .defaultSuccessUrl("/user/index", true) // Redirige a esta página después del inicio de sesión
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/user/logout") // URL de cierre de sesión
                                .logoutSuccessUrl("/user/login?logout") // Redirige a la página de inicio de sesión con el parámetro de logout
                                .invalidateHttpSession(true) // Asegura que la sesión se invalide
                                .deleteCookies("JSESSIONID") // Elimina las cookies de sesión
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/access-denied") // Página de acceso denegado
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public Set<String> blacklistedTokens() {
        return new HashSet<>();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(blacklistedTokens()); // Asegúrate de que este filtro esté correctamente implementado
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user); // Utiliza un servicio de usuarios en memoria para pruebas
    }
}
