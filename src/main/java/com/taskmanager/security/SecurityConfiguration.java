
package com.taskmanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.taskmanager.auth.AuthenticationAccessHandler;
import com.taskmanager.controllers.LastWord;

// Spring will know this is a security configuration class
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class.getName());

	// MyUserDetailService contains the
	@Autowired
	private MyUserDetailService userDetailService;

	// DaoAuthenticationProvider - uses a UserDetailsService implementation to load
	// user details from the data store
	// The service will fetch the user's username, password, authorities (roles),
	// and other relevant information
	// Spring Security provides a "UserDetails" interface that you can implement to
	// represent your user data
	@Bean
	AuthenticationProvider authenticationProvider() {

		// DaoAuthenticationProvider is a class from spring security
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * Note: the "/login" end point is located in the securityFilterChain because we
	 * want it separate from the RestController. Spring Security will create a
	 * "session
	 */
	// Provides a default configuration for me.
	// "Everything behind the /login screen"
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED........................SecurityFilterChain()");
		logger.trace("ENTERED……………………………………securityFilterChain()");

		// by customizing the "authorizeHttpRequest" we got rid of the default login we
		// had
		// Note: "CSRF" Cross Site Request Forgery
		// If "CSRF" is enabled all postrequests will be block

		return httpSecurity.csrf(AbstractHttpConfigurer::disable)

				.authorizeHttpRequests(registry -> {
					registry.requestMatchers("/home", "/register/**", "/logout/**", "/login/**").permitAll();
					registry.requestMatchers("/admin/**").hasRole("ADMIN");
					registry.requestMatchers("/user/**").hasRole("USER");

					registry.anyRequest().authenticated();

					// when we add httpSecurity we need to add the default formLogin page
				}).formLogin(httpSecurityFormLoginConfigurer -> {
					httpSecurityFormLoginConfigurer.loginPage("/login").successHandler(new AuthenticationAccessHandler())
							.permitAll();
				}).build(); // build the HTTP Security

	}

	/**
	 * Creates an AuthenticationManager bean.
	 *
	 * @return the authentication manager
	 */
	@Bean
	public AuthenticationManager authenticationManager() {
		// Create AuthenticationManager using the configured authentication provider
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public UserDetailsService userDetailsService() {

		return userDetailService;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver()); // Crucial!
		// ... other Thymeleaf configuration if needed
		return templateEngine;
	}

	@Bean
	public ITemplateResolver templateResolver() {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/"); // <-- Important: Prefix for Thymeleaf templates
		resolver.setSuffix(".html"); // <-- Important: Suffix for Thymeleaf templates
		resolver.setTemplateMode(TemplateMode.HTML); // Or TemplateMode.XML, etc.
		resolver.setCacheable(false); // For development, set to false to avoid caching issues
		return resolver;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8"); // Important for character encoding
		return resolver;
	}

}
