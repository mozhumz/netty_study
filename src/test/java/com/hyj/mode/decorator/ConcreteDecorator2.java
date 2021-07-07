package com.hyj.mode.decorator;

public class ConcreteDecorator2 extends Decorator{
    public ConcreteDecorator2(Component component) {
        super(component);
    }

    @Override
    public void doSth() {
        super.doSth();
        doSth2();
    }

    private void doSth2(){
        System.out.println("功能C");
    }
}
