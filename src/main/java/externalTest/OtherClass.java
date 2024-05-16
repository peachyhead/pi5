package externalTest;

public class OtherClass {
    private boolean flag;
    private OtherUsage wowClass;

    public OtherClass(OtherUsage wowClass) {
        this.wowClass = wowClass;
    }

    private int getNumber() {
        return flag ? 1 : 0;
    }
}
