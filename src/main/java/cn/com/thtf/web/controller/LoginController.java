package cn.com.thtf.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jasig.cas.client.util.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@Api(value = "LoginController", description = "CAS登陆控制器")
@RestController
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	//cas登陆路径
	@Value(value = "${cas.server-login-url}")
	private String casLoginPath;
	
	//cas服务路径
	@Value(value = "${cas.server-url-prefix}")
	private String casServerPrefix;
	
	//登陆成功后跳转页面
	@Value(value = "${login.success.path}")
	private String servicePath;
	
	//登出后的跳转页面
	@Value(value = "${login.out.path}")
	private String outPath;
	
	
	
	/**
	 * @description 登陆路径
	 * @param response
	 * @throws IOException
	 */

	@ApiOperation(value = "登陆路径")
	@GetMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("登陆页面 ------------------------------------------ ");
			URIBuilder uri = new URIBuilder(servicePath, true);
			response.sendRedirect(uri.toString());
		} catch (Exception e) {
			logger.error("登陆失败  e = {}", e.getMessage());
		}
	}

	@ApiOperation(value = "登出")
	@GetMapping("/logout")
	public void logout(HttpServletResponse response, HttpSession session) {
		try {
			//session.invalidate();
			String service = "";
			service = URLEncoder.encode(outPath, "UTF-8");
			URIBuilder uri = new URIBuilder(casServerPrefix + "/logout?service=" + service, true);
			response.sendRedirect(uri.toString());
		} catch (Exception e) {
			logger.error("登出失败   e = {}", e.getMessage());
		}
	}

	@PostMapping("/out")
	public void logoutCas(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		try {
			//HANDLER.process(request, response);
			session.invalidate();
			logger.info("退出登陆");
		} catch (Exception e) {
			logger.error("登出失败   e = {}", e);
		}
	}
}
