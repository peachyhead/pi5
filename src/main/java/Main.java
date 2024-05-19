import test.MyClass;
import externalTest.OtherClass;
import externalTest.OtherUsage;
import externalTest.WowClass;
import writer.ClassDocumenter;

public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass(new OtherClass(new OtherUsage()), 
                new WowClass("shii"));
        ClassDocumenter.document(obj.getClass(), "output.html");
        System.out.println("HTML document generated successfully.");
    }
}