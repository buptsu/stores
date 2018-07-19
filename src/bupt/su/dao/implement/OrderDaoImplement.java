package bupt.su.dao.implement;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import bupt.su.dao.OrderDao;
import bupt.su.domain.Order;
import bupt.su.domain.OrderItem;
import bupt.su.domain.Product;
import bupt.su.utils.DataSourceUtils;
import net.sf.ehcache.hibernate.management.impl.BeanUtils;

public class OrderDaoImplement implements OrderDao{

	/**
	 * 添加一条订单
	 */
	@Override
	public void add(Order order) {
		QueryRunner qr = new QueryRunner();
		
		/*
		 * `oid` varchar(32) NOT NULL,
		  `ordertime` datetime DEFAULT NULL,
		  `total` double DEFAULT NULL,
		  
		  `state` int(11) DEFAULT NULL,
		  `address` varchar(30) DEFAULT NULL,
		  `name` varchar(20) DEFAULT NULL,
		  
		  `telephone` varchar(20) DEFAULT NULL,
		  `uid` varchar(32) DEFAULT NULL,
		 */
		String sql="insert into orders values (?,?,?,?,?,?,?,?)";
		 DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			qr.update(DataSourceUtils.getConnection(),sql, order.getOid(),simpleDateFormat.format(order.getOrdertime()),order.getTotal(),order.getState(),
					order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("插入时出错！");
		}
	}

	/**
	 * 添加一条订单项
	 */
	@Override
	public void addItem(OrderItem oi) throws Exception {
		QueryRunner qr = new QueryRunner();
		 /**
		 * `itemid` varchar(32) NOT NULL,
		  `count` int(11) DEFAULT NULL,
		  `subtotal` double DEFAULT NULL,
		  `pid` varchar(32) DEFAULT NULL,
		  `oid` varchar(32) DEFAULT NULL,
		 */
		String sql="insert into orderitem values (?,?,?,?,?)";
		qr.update(DataSourceUtils.getConnection(),sql, oi.getItemid(),oi.getCount(),oi.getSubtotal(),oi.getProduct().getPid(),oi.getOrder().getOid());
	}
/**
 * 获取订单列表
 * @throws SQLException 
 * @throws InvocationTargetException 
 * @throws IllegalAccessException 
 */
	@Override
	public List<Order> findAllByPage(String uid, int currPage, int pageSize) throws SQLException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where uid=? order by ordertime desc limit ?,?";
		//数据库中查询到的订单列表
		List<Order>list= qr.query(sql, new BeanListHandler<>(Order.class), uid,(currPage-1)*pageSize,pageSize);
		//遍历订单集合封装每个订单的订单项列表（OrderItem是个对象，数据库中不能直接查出，所以需要对list进行进一步封装）
		for (Order order : list) {
			sql="select * from orderitem oi,product p where oi.pid=p.pid and oi.oid=?";
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			for (Map<String, Object> map : mapList) {
				//封装orderItem  
				OrderItem oi=new OrderItem();
				org.apache.commons.beanutils.BeanUtils.populate(oi, map);
				//封装orderItem的product对象
				Product product=new Product();
				org.apache.commons.beanutils.BeanUtils.populate(product, map);
				//完成orderItem的封装
				oi.setProduct(product);
				//添加orderItem项到Order，完成Order的封装
				order.getItems().add(oi);
			}
		}
		return list;
	}
/**
 * 获取订单总数
 * @throws SQLException 
 */
	@Override
	public int getTotalCount(String uid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from orders where uid=?";
		return ((Long) qr.query(sql, new ScalarHandler(), uid)).intValue();
	}

	@Override
	public Order findById(String oid) throws SQLException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where oid=?";
		//数据库中查询到的订单列表
		Order order= qr.query(sql, new BeanHandler<>(Order.class), oid);
		//遍历订单集合封装每个订单的订单项列表（OrderItem是个对象，数据库中不能直接查出，所以需要对list进行进一步封装）
			sql="select * from orderitem oi,product p where oi.pid=p.pid and oi.oid=?";
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			for (Map<String, Object> map : mapList) {
				//封装orderItem  
				OrderItem oi=new OrderItem();
				org.apache.commons.beanutils.BeanUtils.populate(oi, map);
				//封装orderItem的product对象
				Product product=new Product();
				org.apache.commons.beanutils.BeanUtils.populate(product, map);
				//完成orderItem的封装
				oi.setProduct(product);
				//添加orderItem项到Order，完成Order的封装
				order.getItems().add(oi);
			}
			return order;
	}

	@Override
	public void updateOrder(Order order) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update orders set state=?,address=?,name=?,telephone=? where oid=?";
		qr.update(sql, order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
	}

	@Override
	public List<Order> findAllByState(String state, int currPage, int pageSize)
			throws SQLException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		List<Order> list=null;
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where 1=1 ";
		if(state!=null && state.trim().length()>0){
			sql += "and state = ? order by ordertime desc limit ?,?";
			list= qr.query(sql,new BeanListHandler<>(Order.class),state,(currPage-1)*pageSize,pageSize);
		}else{
			sql+=" order by ordertime desc limit ?,?";
			//数据库中查询到的订单列表
			list= qr.query(sql, new BeanListHandler<>(Order.class), (currPage-1)*pageSize,pageSize);
		}
		//遍历订单集合封装每个订单的订单项列表（OrderItem是个对象，数据库中不能直接查出，所以需要对list进行进一步封装）
		for (Order order : list) {
			sql="select * from orderitem oi,product p where oi.pid=p.pid and oi.oid=?";
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			for (Map<String, Object> map : mapList) {
				//封装orderItem  
				OrderItem oi=new OrderItem();
				org.apache.commons.beanutils.BeanUtils.populate(oi, map);
				//封装orderItem的product对象
				Product product=new Product();
				org.apache.commons.beanutils.BeanUtils.populate(product, map);
				//完成orderItem的封装
				oi.setProduct(product);
				//添加orderItem项到Order，完成Order的封装
				order.getItems().add(oi);
			}
		}
		return list;
	}

	@Override
	public int getTotalCountByState(String state) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from orders where 1=1";
		if(state!=null && state.trim().length()>0){
			sql += " and state = ? order by ordertime desc";
			return ((Long) qr.query(sql, new ScalarHandler(), state)).intValue();		
		}else{
			sql+=" order by ordertime desc";
			return ((Long) qr.query(sql, new ScalarHandler())).intValue();
		}
	}
}
