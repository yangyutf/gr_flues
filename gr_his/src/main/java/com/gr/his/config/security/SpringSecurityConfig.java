package com.gr.his.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * SpringSecurity
 * 安全框架
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    //静态资源配置
    @Override
    public void configure(WebSecurity web) throws Exception {
        //swagger2所需要用到的静态资源，允许访问
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html",
                "/doc.html");
    }

    //url 配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭csrf验证(防止跨站请求伪造攻击)
        http.csrf().disable();
        http.authorizeRequests()
                //允许根路径url的访问
                .antMatchers("/","/test/**").permitAll()
                //允许swagger-ui.html访问
                .antMatchers("/webjars/swagger-bootstrap-ui","/webjars/swagger-bootstrap-ui/**","/webjars/springfox-swagger-ui","/webjars/springfox-swagger-ui/**").permitAll()
                //默认其他请求都需要认证
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .formLogin().permitAll();


    }


}
