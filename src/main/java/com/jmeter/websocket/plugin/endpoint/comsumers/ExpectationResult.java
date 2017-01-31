package com.jmeter.websocket.plugin.endpoint.comsumers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static java.lang.System.currentTimeMillis;

public class ExpectationResult {

    private long endTime;
    private boolean success;
    private boolean finished;
    private String reason;

    public ExpectationResult() {
        this(0, false, false);
    }

    public ExpectationResult(long endTime, boolean success, boolean finished) {
        this(endTime, success, finished, null);
    }

    public ExpectationResult(long endTime, boolean success, boolean finished, String reason) {
        this.endTime = endTime;
        this.success = success;
        this.finished = finished;
        this.reason = reason;
    }

    public void success() {
        setFinished(true);
        setSuccess(true);
        setEndTime(currentTimeMillis());
    }

    public void failure(String reason) {
        setFinished(true);
        setSuccess(false);
        setReason(reason);
        setEndTime(currentTimeMillis());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endTime, success, reason);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpectationResult that = (ExpectationResult) o;
        return endTime == that.endTime &&
                success == that.success &&
                Objects.equal(reason, that.reason);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("endTime", endTime)
                .add("success", success)
                .add("finished", success)
                .add("reason", reason)
                .toString();
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
