package bupt.su.service;

import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import bupt.su.domain.User;

public interface UserService {

	void regist(User user) throws SQLException, AddressException, MessagingException;

	User active(String code) throws SQLException;

	User login(String username, String password) throws SQLException;

}
