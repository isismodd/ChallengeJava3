package br.com.fiap.ClyvoPet.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Liberados para todos
                        .requestMatchers("/login", "/registrar", "/css/**", "/js/**", "/webjars/**", "/images/**").permitAll()

                        // Rotas administrativas - apenas ADMIN
                        .requestMatchers("/web/admin/**").hasRole("ADMIN")

                        // Rotas do sistema - ADMIN ou VETERINARIO
                        .requestMatchers("/web/**").hasAnyRole("ADMIN", "VETERINARIO")

                        // API - ADMIN ou VETERINARIO
                        .requestMatchers("/api/**").hasAnyRole("ADMIN", "VETERINARIO")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}