package bupt.su.dao;

import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.Product;

public interface ProductDao {
	List<Product> findNewProduct() throws SQLException;
	List<Product> findHotProduct() throws SQLException;
	 Product getProductById(String productId) throws SQLException;
	void updateCid(String cid) throws SQLException;
	List<Product> findProductByPage(int currPage, int pageSize) throws SQLException;
	int getTotalCount() throws SQLException;
	void add(Product p) throws SQLException;
	List<Product> findProductByPage(String cid, int currPage, int pageSize) throws SQLException;
	int getTotalCount(String cid) throws SQLException;
}
