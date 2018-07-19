package bupt.su.service;

import java.sql.SQLException;
import java.util.List;

import bupt.su.domain.Category;

public interface CategoryService {

	List<Category> findAllCategory() throws SQLException;

	void add(Category category) throws SQLException;

	Category getById(String cid) throws SQLException;

	void update(Category c) throws SQLException;

	void delete(String cid) throws SQLException;

}
