package bupt.su.service.implement;

import java.sql.SQLException;
import java.util.List;

import bupt.su.dao.ProductDao;
import bupt.su.dao.implement.ProductDaoImplement;
import bupt.su.domain.PageBean;
import bupt.su.domain.Product;
import bupt.su.service.ProductService;
import bupt.su.utils.BeanFactory;

public class ProductServiceImplement implements ProductService {

	@Override
	public List<Product> findNewProduct() throws SQLException {
		// TODO Auto-generated method stub
		ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");;
		return dao.findNewProduct();
	}

	@Override
	public List<Product> findHotProduct() throws SQLException {
		// TODO Auto-generated method stub
		ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");;
		return dao.findHotProduct();
	}

	@Override
	public Product getProductById(String productId) throws SQLException {
		// TODO Auto-generated method stub
		ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");;
		return dao.getProductById(productId);
	}
/**
 * 按类分页查询商品
 * @throws SQLException 
 */
	@Override
	public PageBean<Product> findProductByPage(String cid, int currPage, int pageSize) throws SQLException {
		// TODO Auto-generated method stub
		//当前页数据
		ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");
		List<Product> list=dao.findProductByPage(cid,currPage,pageSize);
		//总条数
		int totalCount=dao.getTotalCount(cid);
		return new PageBean<Product>(list, currPage, pageSize, totalCount);
	}

@Override
public PageBean<Product> findAll(int currPage, int pageSize) throws SQLException {
	// TODO Auto-generated method stub
	//当前页数据
	ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");;
	List<Product> list=dao.findProductByPage(currPage,pageSize);
	//总条数
	int totalCount=dao.getTotalCount();
	return new PageBean<Product>(list, currPage, pageSize, totalCount);
}

@Override
public void add(Product p) throws SQLException {
	// TODO Auto-generated method stub
	ProductDao dao=(ProductDao) BeanFactory.getBean("ProductDao");;
	dao.add(p);
}



}
