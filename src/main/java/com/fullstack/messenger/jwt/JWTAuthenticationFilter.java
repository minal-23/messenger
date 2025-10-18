package com.fullstack.messenger.jwt;

import com.fullstack.messenger.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken=null;
        Long userId=null;
        String authHeader=request.getHeader("Authorization");//getting the header from the request
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            jwtToken=authHeader.substring(7);

        }
        //handle jwtToken=null,check cookies stored in browser

        if(jwtToken==null){
            Cookie[] cookies=request.getCookies();
            if(cookies!=null){
                for(Cookie cookie:cookies){
                    if("JWT".equals(cookie.getName())){
                        jwtToken=cookie.getValue();break;
                    }
                }
            }
        }
        //even if cokkies are still empty
        if(jwtToken==null){
            filterChain.doFilter(request,response);
            return;
        }
        //if i get a valid jwt then extract userId
        userId=jwtService.extractUserId(jwtToken);
        //if userId is valid
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            var userDetails = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            if(jwtService.isTokenValid(jwtToken, userDetails)){

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request,response);
        return;

    }
}
