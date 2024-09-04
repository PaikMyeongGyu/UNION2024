package skkunion.union2024.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.domain.repository.SessionRepository;
import skkunion.union2024.auth.filter.AccessTokenValidatorFilter;
import skkunion.union2024.auth.filter.AuthTokenGenerateFilter;
import skkunion.union2024.auth.filter.RefreshTokenValidatorFilter;
import skkunion.union2024.auth.util.TokenHandler;
import skkunion.union2024.member.domain.repository.MemberRepository;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenContext authTokenContext,
                                            MemberRepository memberRepository, SessionRepository sessionRepository, TokenHandler tokenHandler) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterAfter(new AuthTokenGenerateFilter(authTokenContext, tokenHandler), BasicAuthenticationFilter.class)
                .addFilterBefore(new AccessTokenValidatorFilter(authTokenContext, tokenHandler), BasicAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenValidatorFilter(authTokenContext, tokenHandler, memberRepository, sessionRepository), AccessTokenValidatorFilter.class)
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/user/*", "/test", "/reissue").authenticated()
                        .requestMatchers("/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
