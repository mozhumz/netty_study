package com.hyj.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.json.JSONObject;

public class MyThriftClient {
    public static void main(String[] args)  {
        TTransport transport=null;
        try {
            transport=new TFramedTransport(new TSocket("localhost",8899),
                    600);
            TProtocol protocol=new TCompactProtocol(transport);
            transport.open();
            MyPersonService.Client client=new MyPersonService.Client(protocol);
            Person person=client.getPersonByName("张三");
            System.out.println(new JSONObject(person));
            Person person2=new Person();
            person2.setName("里斯");
            person2.setAge(19);
            System.out.println(new JSONObject(person2));
            client.savePerson(person2);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(transport!=null){
                transport.close();
            }

        }

    }
}
