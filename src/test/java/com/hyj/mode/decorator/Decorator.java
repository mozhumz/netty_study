package com.hyj.mode.decorator;

public class Decorator implements Component{
    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void doSth() {
        component.doSth();
    }
}
