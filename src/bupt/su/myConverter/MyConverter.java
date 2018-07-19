package bupt.su.myConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

public class MyConverter implements Converter {

	@Override
	public Object convert(Class clazz, Object value) {
		// TODO Auto-generated method stub
		//class 要封装的类型
		//value 页面上传入的值
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse((String)value);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
