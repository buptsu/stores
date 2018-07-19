package bupt.su.dao.implement;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import bupt.su.dao.UserDao;
import bupt.su.domain.User;
import bupt.su.utils.DataSourceUtils;

public class UserDaoImplement implements UserDao {

	@Override
	public void add(User user) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into user values(?,?,?,?,?,?,?,?,?,?)";
		qr.update(sql, user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelphone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
	}

	@Override
	public User getUserByCode(String code) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from user where code=? limit 1";
		User user=qr.query(sql, new BeanHandler<>(User.class), code);
		return user;
	}

	@Override
	public void updateUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update user set username=?,password=?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid=?";
		qr.update(sql, user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelphone(),user.getBirthday(),user.getSex(),user.getState(),null,user.getUid());
	}

	@Override
	public User getUserByUserNameAndPassword(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from user where username=? and password=? limit 1";
		return qr.query(sql, new BeanHandler<>(User.class), username,password);
	}

}
