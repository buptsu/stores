package bupt.su.service.implement;

import java.sql.SQLException;
import java.util.List;

import org.apache.catalina.realm.DataSourceRealm;

import bupt.su.dao.CategoryDao;
import bupt.su.dao.ProductDao;
import bupt.su.dao.implement.CategoryDaoImplement;
import bupt.su.dao.implement.ProductDaoImplement;
import bupt.su.domain.Category;
import bupt.su.service.CategoryService;
import bupt.su.service.ProductService;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.DataSourceUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CategoryServiceImplement implements CategoryService {

	@Override
	public List<Category> findAllCategory() throws SQLException {
		// TODO Auto-generated method stub
/*		//方法1：直接从数据库查询数据
		CategoryDao dao = BeanFactory.getBean("CategoryDao");;
		return dao.getAllCategory();
		*/
		
		//方法2：先从缓存获取数据，没有数据再从数据库中查询数据
		//获取缓存管理器
		CacheManager cm = CacheManager.create(CategoryDaoImplement.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		//获取指定的缓存
		Cache cache = cm.getCache("categoryCache");
		//通过缓存获取数据  将cache看成一个map即可
		Element element = cache.get("categoryList");
		List<Category> list=null;
		//判断缓存中数据是否存在
		if(element==null){
			//从数据库中获取数据、
			CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");;
			list=dao.getAllCategory();
			//将获取后的数据存入缓存
			cache.put(new Element("categoryList", list));
			System.out.println("从数据库中读取数据，放入缓存");
		}else{
			list=(List<Category>) element.getObjectValue();
			System.out.println("从缓存中读取数据");
		}
		return list;
		
	}

	@Override
	public void add(Category category) throws SQLException {
		// TODO Auto-generated method stub
		CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");;
		dao.add(category);
		//获取缓存管理器
		CacheManager cm = CacheManager.create(CategoryDaoImplement.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		//获取指定的缓存
		Cache cache = cm.getCache("categoryCache");
		//清空缓存
		cache.remove("categoryList");
	}
/**
 * 通过cid获取一个分类对象
 * @throws SQLException 
 */
	@Override
	public Category getById(String cid) throws SQLException {
		// TODO Auto-generated method stub
		CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");;
		return dao.getById(cid);
	}
/**
 * 更新category
 * @throws SQLException 
 */
@Override
public void update(Category c) throws SQLException {
	// TODO Auto-generated method stub
	//更新
	CategoryDao dao = (CategoryDao) BeanFactory.getBean("CategoryDao");;
	dao.update(c);
	
	//获取缓存管理器
	CacheManager cm = CacheManager.create(CategoryDaoImplement.class.getClassLoader().getResourceAsStream("ehcache.xml"));
	//获取指定的缓存
	Cache cache = cm.getCache("categoryCache");
	//清空缓存
	cache.remove("categoryList");
}

@Override
public void delete(String cid) throws SQLException {
	// TODO Auto-generated method stub
	//开启事务
	try {
		DataSourceUtils.startTransaction();
		//更新该分类下商品的cid为null
		ProductDao pdao=new ProductDaoImplement();
		pdao.updateCid(cid);
		//删除分类
		CategoryDao cdao=(CategoryDao) BeanFactory.getBean("CategoryDao");;
		cdao.delete(cid);
		//清空缓存
			///获取缓存管理器
			CacheManager cm = CacheManager.create(CategoryDaoImplement.class.getClassLoader().getResourceAsStream("ehcache.xml"));
			///获取指定的缓存
			Cache cache = cm.getCache("categoryCache");
			///清空缓存
			cache.remove("categoryList");
		//关闭事物
		DataSourceUtils.commitAndClose();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		DataSourceUtils.rollbackAndClose();
		throw e;
	}

}


}
