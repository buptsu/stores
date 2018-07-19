package bupt.su.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import bupt.su.constant.Constant;
import bupt.su.domain.User;
import bupt.su.myConverter.MyConverter;
import bupt.su.service.UserService;
import bupt.su.service.implement.UserServiceImplement;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.MD5Utils;
import bupt.su.utils.UUIDUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("add 方法执行了");
		return null;
	
	}
	/**
	 * 跳转到注册页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		return "/jsp/register.jsp";
	
	}
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException, InvocationTargetException, AddressException, MessagingException {
		//1.封装数据
		User user=new User();
		//注册自定义转换器
		ConvertUtils.register(new MyConverter(), Date.class);
		BeanUtils.populate(user, request.getParameterMap());
		//1.1设置用户id
		user.setUid(UUIDUtils.getId());
		//1.2设置激活码
		user.setCode(UUIDUtils.getCode());
		//1.3加密密码
		user.setPassword(MD5Utils.md5(user.getPassword()));
		//调用service完成注册
		UserService s=(UserService) BeanFactory.getBean("UserService");;
		s.regist(user);
		//页面请求转发
		request.setAttribute("msg", "用户注册完成，请邮箱激活！");
		
		return "/jsp/msg.jsp";
	
	}
	/**
	 * 用户激活
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException, InvocationTargetException, AddressException, MessagingException {
		//获取code码
		String code = request.getParameter("code");
		//调用service完成激活
		UserService s = (UserService) BeanFactory.getBean("UserService");;
		User user=s.active(code);
		if(user==null){
			request.setAttribute("msg", "请重新激活");
		}else{
			request.setAttribute("msg", "用户激活成功，可以登录！");
		}
		//请求转发到msg页面
		return "/jsp/msg.jsp";
	}
	/**
	 * 跳转到登录界面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public String loginUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException, InvocationTargetException, AddressException, MessagingException {
		
		return "/jsp/login.jsp";
	}
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException, InvocationTargetException, AddressException, MessagingException {
		//获取用户名和密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		password=MD5Utils.md5(password);//因为数据库中存储的是加密后的密码
		//调用service中login方法 返回User
		UserService s=(UserService) BeanFactory.getBean("UserService");;
		User user=s.login(username,password);
		//判断用户
		if(user==null){
			request.setAttribute("msg", "用户名和密码不匹配！");
			return "/jsp/login.jsp";
		}else{
			//继续判断用户状态是否激活
			if(user.getState()==Constant.USER_IS_ACTIVE){
				request.getSession().setAttribute("user", user);
				response.sendRedirect("index.jsp");
				return null;
			}else{
				//将user放入session中 重定向
				request.setAttribute("msg", "用户没有激活，请激活！");
				return "/jsp/msg.jsp";
			}
			
		}

	}
	//退出登录
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//去掉session
		request.getSession().invalidate();
		//重定向
		response.sendRedirect(request.getContextPath());
		
		return null;
	}

		
}
