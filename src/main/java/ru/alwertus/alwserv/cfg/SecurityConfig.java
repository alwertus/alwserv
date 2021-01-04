package ru.alwertus.alwserv.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Ability to give access with annotation @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                    .antMatchers("/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/test/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/test/loginauth").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/v1/test/login/**").authenticated()
//                    .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                    .anyRequest().authenticated()
 /*               .and()
                    .formLogin()    // аутентификация - форма логина
                        .loginPage("/auth/login").permitAll()   // определяем кастомную страницу логина, и разрешаем её всем
                        .defaultSuccessUrl("/auth/success")     // если логин прошёл удачно - перенаправляем
                .and()
                    .logout()//.permitAll()
                        .logoutRequestMatcher(                  // настраиваем логаут
                                new AntPathRequestMatcher(      // requestMatcher должен быть обработан AntPathRequestMatcher
                                        "/auth/logout", // по указаной ссылке
                                        "POST"       // с указанным методом
                                )
                        )
                        .invalidateHttpSession(true)            // инвалидируем сессию
                        .clearAuthentication(true)              // очищаем аутентификацию
                        .deleteCookies("JSESSIONID")            // чистим куки
                        .logoutSuccessUrl("/auth/login")        // перенаправляем в случае удачного логаута
        */
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        System.out.println("PASSWORD");
        return new BCryptPasswordEncoder(); // set strength
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}