package com.javaclimb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanlantu.exception.CodeNotMatchException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String CODE= "code";

    private String code = CODE;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //判断是否是post请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //判断是否是json格式请求类型
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ||request.getContentType().contains(MediaType.APPLICATION_JSON_UTF8_VALUE)){

            try {
                Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);

                String code = userInfo.get(getCode());
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                //获取session中验证吗
                String codeFromSession = (String) request.getSession().getAttribute("code");
                if (!ObjectUtils.isEmpty(code) && ! ObjectUtils.isEmpty(codeFromSession)
                  && code.equalsIgnoreCase(codeFromSession)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request,authenticationToken);
                    //getAuthenticationManager得定义自己的
                    return this.getAuthenticationManager().authenticate(authenticationToken);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       throw new CodeNotMatchException("验证码不匹配");
    }
}
