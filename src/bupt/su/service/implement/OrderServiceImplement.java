package bupt.su.service.implement;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.taglibs.standard.tag.common.sql.DataSourceUtil;

import bupt.su.dao.OrderDao;
import bupt.su.dao.implement.OrderDaoImplement;
import bupt.su.domain.Order;
import bupt.su.domain.OrderItem;
import bupt.su.domain.PageBean;
import bupt.su.domain.User;
import bupt.su.service.OrderService;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.DataSourceUtils;

public class OrderServiceImplement implements OrderService {

	@Override
	public void add(Order order) throws Exception {
		// TODO Auto-generated method stub
		//开启事务
		try {
			DataSourceUtils.startTransaction();
			
			OrderDao orderDao=(OrderDao) BeanFactory.getBean("OrderDao");;
			//向order表中添加一个数据
			orderDao.add(order);
			for (OrderItem orderItem : order.getItems()) {
				orderDao.addItem(orderItem);
			}
			//向orderItem添加n条数据
			//事务处理
			DataSourceUtils.commitAndClose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
	}
	/**
	 * 分页查询
	 * @param user
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public PageBean<Order> findAllByPage(User user, int currPage, int pageSize) throws SQLException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		OrderDao dao=(OrderDao) BeanFactory.getBean("OrderDao");;
		/*		//封装成pageBean的形式
		PageBean<Order> orderPageBean=new PageBean<Order>();*/

		//根据用户调用dao方法查询出订单列表 返回值为 List<Order>
		List<Order> list=dao.findAllByPage(user.getUid(),currPage,pageSize);
		//订单页的totalCount
		int totalCount=dao.getTotalCount(user.getUid());
		return new PageBean<Order>(list, currPage, pageSize, totalCount);
	}
	public Order findById(String oid) throws SQLException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		OrderDao dao=(OrderDao) BeanFactory.getBean("OrderDao");
		return dao.findById(oid);
	}
	@Override
	public void updateOrder(Order order) throws SQLException {
		// TODO Auto-generated method stub
		OrderDao dao=(OrderDao) BeanFactory.getBean("OrderDao");
		dao.updateOrder(order);
	}
	/**
	 * 根据订单状态查询出订单列表
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Override
	public PageBean<Order> findAllByState(String state ,int currPage, int pageSize) throws IllegalAccessException, InvocationTargetException, SQLException {
		// TODO Auto-generated method stub
		OrderDao dao=(OrderDao) BeanFactory.getBean("OrderDao");;
		//根据订单状态调用dao方法查询出订单列表 返回值为 List<Order>
		List<Order> list=dao.findAllByState(state,currPage,pageSize);
		//订单页的totalCount
		int totalCount=dao.getTotalCountByState(state);
		return new PageBean<Order>(list, currPage, pageSize, totalCount);
	}



}
