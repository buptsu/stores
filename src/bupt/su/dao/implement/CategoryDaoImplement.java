package bupt.su.dao.implement;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import bupt.su.dao.CategoryDao;
import bupt.su.domain.Category;
import bupt.su.utils.DataSourceUtils;

public class CategoryDaoImplement implements CategoryDao {

	@Override
	public List<Category> getAllCategory() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from category";
		return qr.query(sql, new BeanListHandler<>(Category.class));
	}
/**
 * 添加分类
 * @throws SQLException 
 */
	@Override
	public void add(Category category) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into category values(?,?)";
		qr.update(sql, category.getCid(),category.getCname());
	}
	/**
	 * 通过cid获取category
	 */
	@Override
	public Category getById(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from category where cid=? limit 1";
		return qr.query(sql, new BeanHandler<>(Category.class),cid);
	}
	/**
	 * 更新category
	 * @throws SQLException 
	 */
	@Override
	public void update(Category c) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update  category set cname=? where cid=? limit 1";
		qr.update(sql, c.getCname(),c.getCid());
	}
	@Override
	public void delete(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr=new QueryRunner();
		String sql="delete from category  where cid=? limit 1";
		qr.update(DataSourceUtils.getConnection(), sql, cid);
	}

}
