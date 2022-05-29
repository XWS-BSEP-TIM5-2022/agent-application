package com.xwsbsep.agent.application.config;

import com.xwsbsep.agent.application.security.auth.RestAuthenticationEntryPoint;
import com.xwsbsep.agent.application.security.auth.TokenAuthenticationFilter;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

                .authorizeRequests().antMatchers("**/auth/**").permitAll()
                .antMatchers("**/search/**").permitAll()
                .antMatchers("**/h2-console/**").permitAll()

                .anyRequest().authenticated().and()

                .cors().and()

                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, customUserDetailsService), BasicAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers(HttpMethod.POST, "/**/auth/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/**/auth/**");
        web.ignoring().antMatchers(HttpMethod.GET, "**/h2-console/**");
        web.ignoring().antMatchers(HttpMethod.POST, "/**/company/**");
        web.ignoring().antMatchers(HttpMethod.PUT, "/**/company/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/**/company/**");
        // TODO SD: obrisati
        web.ignoring().antMatchers(HttpMethod.POST, "/**/comments/**");
        web.ignoring().antMatchers(HttpMethod.PUT, "/**/comments/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/**/comments/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/**/users/**");

        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

}
