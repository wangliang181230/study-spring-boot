package cn.wangliang181230.studynativeimage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AFilter extends OncePerRequestFilter implements Ordered {

	private static final Logger LOGGER = LoggerFactory.getLogger(AFilter.class);


	@Value("${filterOrder:0}")
	protected Integer order;

	@Value("${filter-order:0}")
	protected Integer order2;

	@Override
	public int getOrder() {
		return order;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		LOGGER.info("do filter, order: {}, order2: {}", order, order2);
		filterChain.doFilter(request, response);
	}
}
