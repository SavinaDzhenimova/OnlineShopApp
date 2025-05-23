package org.onlineshop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/uploads/**", "/", "/faq", "/general-conditions", "/privacy-policy",
                                "/maintenance-tips", "/about-us", "/delivery-and-payment", "/return-or-replacement", "/brands",
                                "/loyalty-program", "/choose-size", "/opinions", "/opinions/**", "/subscribe",
                                "/contacts", "/contacts/send-inquiry",
                                "/users/login", "/users/register", "/users/favourites/**",
                                "/products/all", "/products/product/**", "/products/category/**", "/products/brand/**",
                                "/products/size/**", "/products/new", "/products/on-sale",
                                "/shopping-cart", "/shopping-cart/add-item/**", "/shopping-cart/remove-item/**",
                                "/orders/create-order", "/orders/confirmation", "/orders/success", "/orders/track/**",
                                "/orders/make-order",
                                "/promo-codes/apply-promo").permitAll()
                        .requestMatchers("/users/forgot-password", "/users/reset-password/**").anonymous()
                        .requestMatchers("/users/profile").authenticated()
                        .requestMatchers("/products/add-product", "/products/add-discount",
                                "/products/update-product/**",
                                "/promo-codes/**",
                                "/orders/all", "/orders/update-status/**",
                                "/return-or-replacement/requests", "/return-or-replacement/delete-request/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/users/profile", true)
                        .failureUrl("/users/login-error")
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                .build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}