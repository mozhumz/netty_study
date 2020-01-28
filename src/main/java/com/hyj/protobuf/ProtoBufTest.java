package com.hyj.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufTest {
	public static void main(String[] args) throws InvalidProtocolBufferException {
		DataInfo.Student student=DataInfo.Student.newBuilder().setAddress("北京").setAge(20).setName("张三").build();
		byte[] bytes=student.toByteArray();
		DataInfo.Student student2=DataInfo.Student.parseFrom(bytes);
		System.out.println(student2);
		System.out.println(student2.getAddress());
		System.out.println(student2.getAge());
		System.out.println(student2.getName());
	}
}
