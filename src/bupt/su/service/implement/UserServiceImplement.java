package bupt.su.service.implement;

import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import bupt.su.constant.Constant;
import bupt.su.dao.UserDao;
import bupt.su.dao.implement.UserDaoImplement;
import bupt.su.domain.User;
import bupt.su.service.UserService;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.MailUtils;

public class UserServiceImplement implements UserService {
	/**
	 * 用户注册
	 */
	@Override
	public void regist(User user) throws SQLException, AddressException, MessagingException {
		// TODO Auto-generated method stub
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");;
		dao.add(user);
		//发送邮件
		String emailMsg="欢迎您注册成为我们的会员，<a href='http://localhost:8080/store/user?method=active&code="+user.getCode()+"'>点此激活</a>";
		MailUtils.sendMail(user.getEmail(), emailMsg);
	}
	/**
	 * 用户激活
	 * @throws SQLException 
	 */
	@Override
	public User active(String code) throws SQLException {
		// TODO Auto-generated method stub
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");;
		
		//通过code获取一个用户
		User user=dao.getUserByCode(code);
		//用户为空则返回null
		if(user==null){
			return null;
		}else{
			//用户不为空修改用户状态
			user.setState(Constant.USER_IS_ACTIVE);
			dao.updateUser(user);
			return user;
		}
	}
	@Override
	public User login(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");;
		
		//通过code获取一个用户
		return dao.getUserByUserNameAndPassword(username,password);
	}


}
