package bupt.su.dao;

import java.sql.SQLException;

import bupt.su.domain.User;

public interface UserDao {

	void add(User user) throws SQLException;

	User getUserByCode(String code) throws SQLException;

	void updateUser(User user) throws SQLException;

	User getUserByUserNameAndPassword(String username, String password) throws SQLException;

}
