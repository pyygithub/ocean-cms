package cn.com.thtf.web.filter;

import cn.com.thtf.common.response.Result;
import cn.com.thtf.common.response.ResultCode;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UrlFilter implements  Filter {

	private static final Logger logger = LoggerFactory.getLogger(UrlFilter.class);
	//需要过滤的路径
	private List<String> ignorePatternUrl;

	//返回到前段的登陆路径
	private String loginUrl;


	public UrlFilter(List<String> ignorePatternUrl, String loginUrl){
		this.ignorePatternUrl = ignorePatternUrl;
		this.loginUrl = loginUrl;
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("初始化自定义过滤器");
	}

	//
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = request.getRequestURI();
		logger.info("请求路径 url = " + url);
		if(!url.contains("login") && !ignorePatternUrl.contains(url) && !url.contains("/out")) {
			final HttpSession session = request.getSession(false);
			final Assertion assertion = session != null ? (Assertion) session.getAttribute("_const_cas_assertion_") : null;
			if(assertion != null) {
				chain.doFilter(request, response);
			} else {
				if(url.contains("/logout")) {
					logger.info("推出跳转到登陆页面");
					chain.doFilter(request, response);
					return;
				}
				logger.info("会话失效");
				Result result = new Result(ResultCode.PERMISSION_EXPIRE, loginUrl);
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter print = response.getWriter();
				print.write(JSON.toJSONString(result));
				print.close();
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		logger.info("销毁过滤器");
	}
}