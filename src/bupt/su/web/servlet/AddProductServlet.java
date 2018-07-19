package bupt.su.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import bupt.su.domain.Category;
import bupt.su.domain.Product;
import bupt.su.service.ProductService;
import bupt.su.service.implement.ProductServiceImplement;
import bupt.su.utils.UUIDUtils;
import bupt.su.utils.UploadUtils;
import net.sf.ehcache.hibernate.management.impl.BeanUtils;

/**
 * 后台添加商品到服务器 包括将product存入数据库和将图片存入服务器
 */
public class AddProductServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//从form表利用单获取数据若为普通上传组件 添加到map，否则为文件上传组件 图片存储到服务器  然后利用beanutils的populate方法将其封装成product对象 调用service中的add方法完成数据库存储
		//1. 创建map对象
		Map<String,Object> map=new HashMap<String,Object>();
		//2. 根据表单数据向map中添加数据
		//2.1创建磁盘文件项工厂
		DiskFileItemFactory factory=new DiskFileItemFactory();
		//2.2创建文件上传对象
		ServletFileUpload upload=new ServletFileUpload(factory);
		//2.3文件上传对象调用解析请求方法 获取一个List<FileItem>
		try {
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem fileItem : list) {
				if(fileItem.isFormField()){
					String name = fileItem.getFieldName();
					String value = fileItem.getString("utf-8");
					map.put(name, value);
				}else{
					//文件上传组件处理 保存图片到服务器 并且将图片路径添加到map中
					String fileName = fileItem.getName();
					String realName = UploadUtils.getRealName(fileName);
					String uuidName = UploadUtils.getUUIDName(realName);
					//图片路径添加到map
					map.put(fileItem.getFieldName(), "/products/1/"+uuidName);
					//图片存储到服务器
					InputStream is = fileItem.getInputStream();
					String parentPath = getServletContext().getRealPath("/products/1");
					FileOutputStream os=new FileOutputStream(new File(parentPath, uuidName));
					IOUtils.copy(is, os);
					os.close();
					is.close();
					//清除临时文件
					fileItem.delete();
				}
			}
		//3.map中添加表单没有提供的product数据 如pid pdate pflag cid(Product中封装的category对象)
			map.put("pid", UUIDUtils.getId());
			map.put("pdate", new Date());
			map.put("pflag", 0);
			String cid = (String) map.get("cid");
			Category category = new Category();
			category.setCid(cid);
			map.put("category", category);
		//4. 将map中的数据封装成product对象
			Product p=new Product();
			org.apache.commons.beanutils.BeanUtils.populate(p, map);
		//5.向数据库中插入数据
			ProductService ps=new ProductServiceImplement();
			ps.add(p);
			//重定向到展示所有商品页面
			response.sendRedirect(request.getContextPath()+"/adminProduct?method=findAll&currPage=1");	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("msg", "商品添加失败！");
			request.getRequestDispatcher("/jsp/msg.jsp").forward(request, response);
		}
	}

}
