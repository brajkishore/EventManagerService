/**
 * 
 */
package com.nroutes.events.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nroutes.events.services.MyAppUserDetailsService;

/**
 * @author brakisho
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyAppUserDetailsService myAppUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/resources").permitAll()
		.antMatchers("/admin/**").hasAnyRole("ADMIN")
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/app-login")
		.usernameParameter("username")
		.passwordParameter("password")
		.defaultSuccessUrl("/admin/welcome")
		.and().logout()
		.logoutUrl("/app-logout")
		.logoutSuccessUrl("/login")
		.deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
		.and().exceptionHandling()
		.accessDeniedPage("/app/error");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
	}
}