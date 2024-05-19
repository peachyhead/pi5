package externalTest;

public class OtherClass {
    public boolean flag;
    private OtherUsage otherUsage;

    public OtherClass(OtherUsage otherUsage) {
        this.otherUsage = otherUsage;
    }

    private int getNumber() {
        return flag ? 1 : 0;
    }
}
