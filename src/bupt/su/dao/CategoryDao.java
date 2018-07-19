package bupt.su.dao;

import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.Category;

public interface CategoryDao {

	List<Category> getAllCategory() throws SQLException;

	void add(Category category) throws SQLException;

	Category getById(String cid) throws SQLException;

	void update(Category c) throws SQLException;

	void delete(String cid) throws SQLException;

}
