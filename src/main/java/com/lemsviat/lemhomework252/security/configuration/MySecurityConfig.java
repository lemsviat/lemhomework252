package com.lemsviat.lemhomework252.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;


@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String RoleUser = "USER";
    public static final String RoleModerator = "MODERATOR";
    public static final String RoleAdmin = "ADMIN";

    @Autowired
    private DataSource dataSource;

    //@Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
       /* User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(userBuilder.username("petro").password("petro").roles(RoleUser))
                .withUser(userBuilder.username("olena").password("olena").roles(RoleModerator))
                .withUser(userBuilder.username("root").password("Dev911Base!@#").roles(RoleAdmin));*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/files", "/events")
                .hasAnyRole(RoleUser, RoleModerator, RoleAdmin)
                .antMatchers("/moderators_info").hasAnyRole(RoleModerator, RoleAdmin)
                .antMatchers("/uploadingS3_info").hasAnyRole(RoleModerator, RoleAdmin)
                .antMatchers("/admins_info").hasRole(RoleAdmin)
                .antMatchers("/customers/**").hasRole(RoleAdmin)
                .and().formLogin().permitAll()
                .and().logout();
    }
}
