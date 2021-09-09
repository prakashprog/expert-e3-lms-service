package com.expertworks.lms.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableResourceServer
public class ProjectConfig extends ResourceServerConfigurerAdapter {
	
    @Value("${jwt.client.id}")
    private String clientId;
    
    @Value("${jwt.client.secret}")  
    private String secret;
    
    @Value("${jwt.checktoken.endpoint}")
    private String checkTokenEndPoint;
    
    @Value("${jwt.secret}")
    private String signingKey;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
    	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    	converter.setSigningKey(signingKey);
    	// converter.setSigningKey("javainuse");
       // converter.setAccessTokenConverter( new JwtConverter() );
        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	 http.csrf().disable().authorizeRequests().antMatchers(
    			 "/public/signup/*/*",
    			 "/public/signup",
    			 "/pay/success",
    			 "/pay/cancel",
    			 "/geterror",
    			 "/user/resetpwd",
    			 "/actuator/prometheus",
    			 "/actuator/*",
    			 "/courses/",
    			 "/public/courses",
    			 "/public/courses/*",
    			 "/public/contactus")
         .permitAll().anyRequest().authenticated()
         .and().exceptionHandling().and().sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	
    	 
    	  http.cors().configurationSource(new CorsConfigurationSource() {
              @Override
              public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                  return new CorsConfiguration().applyPermitDefaultValues();
              }
          });
    }
    
    @Bean
    public ResourceServerTokenServices tokenService() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(secret);
        tokenServices.setCheckTokenEndpointUrl(checkTokenEndPoint);
        return tokenServices;
    }
    

    
    
    
    
    
    
    
    
    
    
    
    
}
