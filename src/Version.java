public class Version {

    public Version() {
    }

    private String versionName;
    private String versionCode;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }
}
