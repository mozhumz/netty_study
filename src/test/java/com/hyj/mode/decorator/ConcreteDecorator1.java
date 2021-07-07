package com.hyj.mode.decorator;

public class ConcreteDecorator1 extends Decorator{
    public ConcreteDecorator1(Component component) {
        super(component);
    }

    @Override
    public void doSth() {
        super.doSth();
        doSth2();
    }

    private void doSth2(){
        System.out.println("功能B");
    }
}
