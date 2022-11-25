package in.kassapos.a1broilers.api;

public class SSENotificationData {
    public Integer id;
    public String key;
    public String data;
    public  SSENotificationData(){
    }

    public SSENotificationData(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}