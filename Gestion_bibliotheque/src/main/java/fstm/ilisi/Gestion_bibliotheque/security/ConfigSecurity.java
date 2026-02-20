package fstm.ilisi.Gestion_bibliotheque.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
            
            User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
            User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build(),
            User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
        
        return httpSecurity
               .formLogin(Customizer.withDefaults() )
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .build();
    }
    
}
