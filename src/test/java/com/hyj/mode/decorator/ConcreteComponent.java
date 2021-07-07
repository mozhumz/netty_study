package com.hyj.mode.decorator;

public class ConcreteComponent implements Component{
    @Override
    public void doSth() {
        System.out.println("功能A");
    }
}
