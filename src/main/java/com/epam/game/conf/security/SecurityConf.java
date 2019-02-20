package com.epam.game.conf.security;

import com.epam.game.constants.ViewsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/14/2019
 */
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/img/**", "/js/**", "/downloads/**")
                        .permitAll()
                    .antMatchers("/" + ViewsEnum.SIGN_UP + ViewsEnum.EXTENSION)
                        .permitAll()
                    .antMatchers("/" + ViewsEnum.INFO_PAGE + ViewsEnum.EXTENSION)
                        .permitAll()
                    .antMatchers(ViewsEnum.WEBSOCKET_SERVER_URI)
                        .permitAll()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable() // TODO: for testing purposes only -> extract to separate spring profile

                .formLogin()
                    .loginPage("/" + ViewsEnum.LOGIN + ViewsEnum.EXTENSION)
                        .usernameParameter("userName")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login.html")
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/" + ViewsEnum.DOCUMENTATION + ViewsEnum.EXTENSION))
                    .permitAll()
                .and()
                    .logout()
                        .logoutUrl("/" + ViewsEnum.LOGOUT + ViewsEnum.EXTENSION)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/" + ViewsEnum.LOGIN + ViewsEnum.EXTENSION);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
