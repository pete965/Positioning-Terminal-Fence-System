package sse309.bupt.fence.bean;

public class LoginEntity {
    private String password;
    private String username;
    private static String user;

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        LoginEntity.user = user;
    }

    public LoginEntity(String pswd, String unm){
        this.password=pswd;
        this.username=unm;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
