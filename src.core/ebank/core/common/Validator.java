package ebank.core.common;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
	String defaultregx() default "";//"[^$&=~]+";
	String regx() default "";
	int maxsize() default 0;
	int length() default 0;
	boolean nullable() default true;
	boolean list() default true; //��ʾ����
	boolean throwable() default true; //Υ�������Ƿ����쳣	
	String message() default "�Ƿ��ַ�";
}


