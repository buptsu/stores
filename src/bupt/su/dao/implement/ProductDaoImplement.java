package bupt.su.dao.implement;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import bupt.su.dao.ProductDao;
import bupt.su.domain.Product;
import bupt.su.utils.DataSourceUtils;

public class ProductDaoImplement implements ProductDao {

	public List<Product> findNewProduct() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product order by pdate limit 9";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	public List<Product> findHotProduct() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where is_hot=1  order by pdate limit 9";
		return qr.query(sql, new BeanListHandler<>(Product.class));
	}

	public Product getProductById(String productId) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where pid=? limit 1";
		return qr.query(sql, new BeanHandler<>(Product.class),productId);

	}

	public List<Product> findProductByPage(String cid, int currPage, int pageSize) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where cid=? limit ?,?";
		return qr.query(sql, new BeanListHandler<>(Product.class),cid,(currPage-1)*pageSize,pageSize);
	}

	public int getTotalCount(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from product where cid=?";
		return ((Long) qr.query(sql,new ScalarHandler(),cid)).intValue();
	}
/**
 * 将cid置为空null
 * @throws SQLException 
 */
	@Override
	public void updateCid(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner();
		String sql="update product set cid=null where cid=?";
		qr.update(DataSourceUtils.getConnection(),sql, cid);
	}
/**
 * 查询所有商品，分页显示
 * @throws SQLException 
 */
public List<Product> findProductByPage(int currPage, int pageSize) throws SQLException {
	// TODO Auto-generated method stub
	QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
	String sql="select * from product limit ?,?";
	return qr.query(sql, new BeanListHandler<>(Product.class),(currPage-1)*pageSize,pageSize);
}

@Override
public int getTotalCount() throws SQLException {
	// TODO Auto-generated method stub
	QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
	String sql="select count(*) from product";
	return ((Long)qr.query(sql,new ScalarHandler())).intValue();
}

@Override
public void add(Product p) throws SQLException {
	// TODO Auto-generated method stub
	QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
	String sql="insert into product values(?,?,?,?,?,?,?,?,?,?)";
	qr.update(sql, p.getPid(),p.getPname(),p.getMarket_price(),p.getShop_price(),p.getPimage(),new Date(p.getPdate().getTime()),p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCategory().getCid());
}



}
