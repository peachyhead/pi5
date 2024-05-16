import test.MyClass;
import externalTest.OtherClass;
import externalTest.OtherUsage;
import externalTest.WowClass;
import writer.ObjectDocumenter;

public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass(new OtherClass(new OtherUsage()), 
                new WowClass("shii"));
        ObjectDocumenter.document(obj, "output.html");
        System.out.println("HTML document generated successfully.");
    }
}