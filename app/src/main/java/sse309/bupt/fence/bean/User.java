package sse309.bupt.fence.bean;

import java.util.List;

public class User {
    private static User user = new User();
    private String id;
    private String name;
    private List<Fence> fences;
    private List<BreakInfo> breakInfos;
    private double statisticIndex;

    private User() {
    }

    public static User getInstance(){
        return user;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStatisticIndex() {
        return statisticIndex;
    }

    public void setStatisticIndex(double statisticIndex) {
        this.statisticIndex = statisticIndex;
    }

    public List<BreakInfo> getBreakInfos() {
        return breakInfos;
    }

    public void setBreakInfos(List<BreakInfo> breakInfos) {
        this.breakInfos = breakInfos;
    }
}
