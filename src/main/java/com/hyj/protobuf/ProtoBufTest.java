package com.hyj.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * grpc（Google protobuf）作为rpc的一种，肯定有服务间的网络调用，调用就一定会有数据的传输，
 * 而这个数据的传输就用的是protobuf去序列化和反序列化的，一个请求的内容在调用方序列化通过网络传送到服务方，
 * 服务方就能用protobuf反序列化出来
 */
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
