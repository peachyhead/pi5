package org.example;

class MyClass {
    private int number;
    private String text;
    private OtherClass[] storage = new OtherClass[5];
    private WowClass wowClass;

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

class OtherClass {
    private boolean flag;
    private OtherUsage wowClass;

    OtherClass(OtherUsage wowClass) {
        this.wowClass = wowClass;
    }

    private int getNumber() {
        return flag ? 1 : 0;
    }
}

class WowClass {
    private String field;

    public WowClass(String field) {
        this.field = field;
    }
}

class OtherUsage {
    private long lint = 5;
}