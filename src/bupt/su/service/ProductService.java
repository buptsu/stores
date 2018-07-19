package bupt.su.service;

import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.PageBean;
import bupt.su.domain.Product;

public interface ProductService {

	List<Product> findNewProduct() throws SQLException;

	List<Product> findHotProduct() throws SQLException;

	Product getProductById(String productId) throws SQLException;

	PageBean<Product> findProductByPage(String cid, int currPage, int pageSize) throws SQLException;

	PageBean<Product> findAll(int currPage, int pageSize) throws SQLException;

	void add(Product p) throws SQLException;

}
