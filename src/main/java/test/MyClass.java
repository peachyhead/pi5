package test;

import externalTest.OtherClass;
import externalTest.WowClass;

public class MyClass {
    private int number;
    private String text;
    private OtherClass[] storage = new OtherClass[5];
    private WowClass wowClass;

    public MyClass(String input) {
        number = Integer.parseInt(input);
    }
    
    public MyClass(OtherClass other, WowClass wowClass) {
        storage[0] = other;
        this.wowClass = wowClass;
    }

    private void doSmth(int number, String text) {

    }

    public OtherClass getStorage() {
        return storage[0];
    }
}

