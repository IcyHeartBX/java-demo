package com.pix.testannotation;

public class TestMain {

	/**
	 * 测试Annotation的Demo
	 * @param 
	 */
	public static void main(String[] args) {
		Object stu = new Student("张三", 18);
		com.pix.testannotation.bean.Student stuTmp = (com.pix.testannotation.bean.Student)stu;
		System.out.println(stuTmp.toString());
		
	}
	
	public @interface Ety{
		String value();
		String name();
	}

}
