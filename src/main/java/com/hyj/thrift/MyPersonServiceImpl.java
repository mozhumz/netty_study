package com.hyj.thrift;

import com.google.gson.Gson;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.json.JSONObject;

public class MyPersonServiceImpl implements MyPersonService.Iface{
    @Override
    public Person getPersonByName(String name) throws TException {
        Person person=new Person();
        person.setName("张三");
        person.setAge(10);
        return person;
    }

    @Override
    public void savePerson(Person person) throws TException {
        System.out.println(new JSONObject(person).toString());
    }
}
