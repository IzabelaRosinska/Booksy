package mb.booksy.security;

import mb.booksy.jwt.JwtConfig;
import mb.booksy.jwt.JwtTokenVerifier;
import mb.booksy.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public ApplicationSecurityConfig(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable()
                    .addFilterBefore(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new JwtTokenVerifier(secretKey), BasicAuthenticationFilter.class)

                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .antMatchers("/fragments/**").permitAll()
                    .antMatchers("/home").permitAll()
                    .antMatchers("/signup").permitAll()

                    .and().formLogin().loginPage("/login").permitAll().successHandler(appAuthenticationSuccessHandler())
                    .and().httpBasic()
                    .and().logout(logout -> logout
                            .logoutUrl("/logout")
                            .addLogoutHandler((request, response, auth) -> {
                                for (Cookie cookie : request.getCookies()) {
                                    String cookieName = cookie.getName();
                                    if(cookieName.equals("token")) {
                                        Cookie cookieToDelete = new Cookie(cookieName, null);
                                        cookieToDelete.setMaxAge(0);
                                        response.addCookie(cookieToDelete);
                                    }
                                }
                            })
                          );
    }

    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new ApplicationAuthenticationSuccessHandler();
    }
}
