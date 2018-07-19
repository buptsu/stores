package bupt.su.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.Order;
import bupt.su.domain.PageBean;
import bupt.su.domain.User;

public interface OrderService {

	void add(Order order) throws Exception;
	PageBean<Order> findAllByPage(User user, int currPage, int pageSize) throws SQLException, IllegalAccessException, InvocationTargetException ;
	Order findById(String oid) throws SQLException, IllegalAccessException, InvocationTargetException ;
	void updateOrder(Order order) throws SQLException;
	PageBean<Order> findAllByState(String state, int currPage, int pageSize) throws IllegalAccessException, InvocationTargetException, SQLException;
}
