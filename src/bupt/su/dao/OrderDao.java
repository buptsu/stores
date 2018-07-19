package bupt.su.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.Order;
import bupt.su.domain.OrderItem;

public interface OrderDao {

	void add(Order order) throws Exception;

	void addItem(OrderItem orderItem) throws Exception;

	List<Order> findAllByState(String state, int currPage, int pageSize) throws SQLException, IllegalAccessException, InvocationTargetException;

	int getTotalCount(String uid) throws SQLException;

	Order findById(String oid) throws SQLException, IllegalAccessException, InvocationTargetException;

	void updateOrder(Order order) throws SQLException;

	int getTotalCountByState(String state) throws SQLException;

	List<Order> findAllByPage(String uid, int currPage, int pageSize)
			throws SQLException, IllegalAccessException, InvocationTargetException;

}
