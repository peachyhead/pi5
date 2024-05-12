package org.example;

import static org.example.ObjectDocumenter.document;

public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass(new OtherClass(new OtherUsage()), 
                new WowClass("shii"));
        document(obj, "output.html");
        System.out.println("HTML document generated successfully.");
    }
}