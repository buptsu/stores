package bupt.su.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 实体工厂类
 * @author suyang
 *
 */
public class BeanFactory {
	public static Object getBean(String id){
		//通过id获取一个指定的实现类
		try {
			//获取document对象
			Document doc = new SAXReader().read(BeanFactory.class.getClassLoader().getResourceAsStream("bean.xml"));
			//获取指定的bean对象
			Element ele = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			//获取bean对象的class属性
			String value = ele.attributeValue("class");
/*			//反射 以前的逻辑是直接返回的实例
			return Class.forName(value).newInstance();*/
			//现在对service中的add方法进行加强 返回值是代理对象
			Object obj = Class.forName(value).newInstance();
			//判断是否时service实现类
			if(id.endsWith("Service")){
				Object proxyObj = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// TODO Auto-generated method stub
						if("add".equals(method.getName())||"regist".equals(method.getName())){
							System.out.println("-------->对add和regist方法进行加强");
							return method.invoke(obj, args);
						}
						return method.invoke(obj, args);
					}
				});
				//若是service方法则返回代理对象
				return proxyObj;
			}
			//若不是service方法则返回实例对象
			return obj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
