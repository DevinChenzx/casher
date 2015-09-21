package ebank.core.common.util;

import java.lang.reflect.Field;

import ebank.core.common.EventCode;
import ebank.core.common.ServiceException;
import ebank.core.common.Validator;

public class Clazz {
	public static boolean Annotation(Class clazz,String fieldname,String value) throws ServiceException{
		return Clazz.Annotation(clazz, fieldname, value,Validator.class);
	}
	
	public static boolean Annotation(Class clazz,String fieldname,String value,Class<Validator> annclazz) throws ServiceException{				
		Field field=null;
		try {
			field=clazz.getDeclaredField(fieldname.toLowerCase());
		} catch (Exception e) {			
			throw new ServiceException(EventCode.WEB_PARAM_LOST,new String[]{fieldname.toUpperCase()});			
		}							
		if(field!=null&&field.isAnnotationPresent(annclazz)){
			if(value==null||"".equals(value)||"null".equals(value)){
				if(!field.getAnnotation(annclazz).nullable()){
					throw new ServiceException(EventCode.WEB_PARAMEMPTY,new String[]{field.getName().toUpperCase()});
				}
				return true; //null return;
			}
			if(!"".equals(field.getAnnotation(annclazz).defaultregx())&&!String.valueOf(value).matches(field.getAnnotation(annclazz).defaultregx())){
				if(field.getAnnotation(annclazz).throwable()==false) return false;
				throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{field.getName().toUpperCase()});
			}
			int j=value.length();
			int maxlength=field.getAnnotation(annclazz).maxsize();
			int length=field.getAnnotation(annclazz).length();
			if(maxlength!=0&&j>maxlength){
				if(field.getAnnotation(annclazz).throwable()==false) return false;
				throw new ServiceException(EventCode.WEB_PARAM_LENGTH_OVERFLOW,new String[]{field.getName().toUpperCase(),String.valueOf(field.getAnnotation(annclazz).maxsize())});
			}
			if(length!=0&&j!=length){
				if(field.getAnnotation(annclazz).throwable()==false) return false;
				throw new ServiceException(EventCode.WEB_PARAM_LENGTH,new String[]{field.getName().toUpperCase(),String.valueOf(field.getAnnotation(annclazz).length())});											
			}					
			if(field.getAnnotation(annclazz).regx()!=null&&!"".equals(field.getAnnotation(annclazz).regx())){
				if(!String.valueOf(value).matches(field.getAnnotation(annclazz).regx())){
					if(field.getAnnotation(annclazz).throwable()==false) return false;
					throw new ServiceException(EventCode.WEB_PARAMFORMAT,new String[]{field.getName().toUpperCase()});
							
				}
			}	
			return field.getAnnotation(annclazz).list();
		}
		return true;
		
	}

}
