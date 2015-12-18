/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author maja
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)

public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig fc) throws ServletException {}

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) sr1;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With,"
                + " Content-Range, Content-Disposition, Content-Type, Authorization, X-CSRF-TOKEN");
        
        if("OPTIONS".equalsIgnoreCase(((HttpServletRequest) sr).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            fc.doFilter(sr, sr1);
        }
    }

    @Override
    public void destroy() {}
}
