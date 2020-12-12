package com.example.demo;

import com.example.demo.filter.CustomSuccessAuthentication;
import com.example.demo.filter.TokenCheckerGenericBeanFilter;
import com.example.demo.provider.CustomDaoAuthenticationProvider;
import com.example.demo.repository.JpaMessageRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.source.CustomWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication(exclude = OAuth2ResourceServerAutoConfiguration.class)
public class DemoSecurityApplication {

	@Autowired
	private JpaMessageRepository jpaMessageRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (){
		return args -> {
			System.out.println("Init application");
//			List<Message> messages = new ArrayList<>();
//			Collections.addAll(messages, new Message("Milos","Maksimovic"),
//					new Message("nEMANJA", "SADASDASDA"));
//			this.jpaMessageRepository.saveAll(messages);
		};
	}

	@Configuration
	public static class OAuth2CustomSecurityConfig extends WebSecurityConfigurerAdapter {

	    @Autowired
	    private CustomUserDetailsService customUserDetailsService;
	    @Autowired
	    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;
		@Autowired
		private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					//Uncomment for access to h2 console
//					.csrf().disable()
//					.headers().frameOptions().disable()
//					.and()
					.authorizeRequests(authorizeRequest-> authorizeRequest
							.antMatchers("/h2-console/**").permitAll()
							.mvcMatchers(HttpMethod.POST, "/api/user").permitAll()
							.anyRequest()
							.authenticated()

					)
                    .formLogin(formLogin-> formLogin
							.authenticationDetailsSource(this.customWebAuthenticationDetailsSource)
							.defaultSuccessUrl("/")
					)
					.addFilterBefore(this.customSuccessAuthentication(), UsernamePasswordAuthenticationFilter.class);


		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(this.customDaoAuthenticationProvider);
			auth.userDetailsService(this.customUserDetailsService);
		}

		@Override
		@Bean
		protected AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}

		@Bean
		public PasswordEncoder getPasswordEncoderFactories(){
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}

		@Bean
		public CustomSuccessAuthentication customSuccessAuthentication() throws Exception {
			CustomSuccessAuthentication customSuccessAuthentication =
					new CustomSuccessAuthentication("/auth", this.authenticationManager());
			return customSuccessAuthentication;
		}
	}

	@Configuration
	public static class FilterBeanConfiguration {

		@Bean
		public FilterRegistrationBean<TokenCheckerGenericBeanFilter> tokenCheckerGenericBeanFilterFilterRegistrationBean (){
			FilterRegistrationBean<TokenCheckerGenericBeanFilter> filterFilterRegistrationBean =
					new FilterRegistrationBean<>();
			filterFilterRegistrationBean.setFilter(tokenCheckerGenericBeanFilter());
			filterFilterRegistrationBean.addUrlPatterns("/api/message/*");
			return filterFilterRegistrationBean;
		}

		@Bean
		public TokenCheckerGenericBeanFilter tokenCheckerGenericBeanFilter () {
			return new TokenCheckerGenericBeanFilter();
		}

	}

}
