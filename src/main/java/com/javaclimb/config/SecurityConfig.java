package com.javaclimb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanlantu.config.handle.LoginFilter;
import com.lanlantu.pojo.Admin;
import com.lanlantu.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailService userDetailService;

    @Autowired
    public SecurityConfig(MyUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

 /*   @Bean
     public UserDetailsService userDetailsService(){
         InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
         inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
         return inMemoryUserDetailsManager;
     }*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }


    //得从工厂中暴露出来，不暴露出来只能在工厂内部中使用
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setUsernameParameter("username");
        loginFilter.setPasswordParameter("password");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) ->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("msg","登陆成功");
            map.put("status",200);
            map.put("用户信息",(Admin)(authentication.getPrincipal()));
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            String s = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(s);
        } );

        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("msg","登陆失败:"+exception.getMessage());
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            String s = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(s);
        });
        return loginFilter;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().println("请认证之后再处理");
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("msg","注销成功");
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(map);
                    response.getWriter().println(s);
                })
                .and().csrf().disable()
          ;

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
