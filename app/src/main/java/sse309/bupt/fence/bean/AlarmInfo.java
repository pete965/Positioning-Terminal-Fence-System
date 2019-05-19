package sse309.bupt.fence.bean;

public class AlarmInfo {
    private String username;
    private String action;
    private String fencename;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFencename() {
        return fencename;
    }

    public void setFencename(String fencename) {
        this.fencename = fencename;
    }

    public AlarmInfo(String username,String action,String fencename){
        this.action=action;
        this.fencename=fencename;
        this.username=username;
    }
}
