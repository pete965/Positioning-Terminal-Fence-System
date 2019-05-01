package sse309.bupt.fence.bean;

public class BreakInfo {
    private int userId;
    private int fenceId;
    private long breakTime;

    public BreakInfo(int userId, int fenceId, long breakTime) {
        this.userId = userId;
        this.fenceId = fenceId;
        this.breakTime = breakTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFenceId() {
        return fenceId;
    }

    public void setFenceId(int fenceId) {
        this.fenceId = fenceId;
    }

    public long getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(long breakTime) {
        this.breakTime = breakTime;
    }
}
