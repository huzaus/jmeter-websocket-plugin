package com.jmeter.websocket.plugin.elements.helpers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static java.lang.System.currentTimeMillis;

public class ExpectationResult {

    public static final String SUCCESS = "success";
    public static final String TIMEOUT = "timeout: ";
    private final long endTime;
    private final boolean success;
    private final String reason;

    public ExpectationResult(long endTime, boolean success, String reason) {
        this.endTime = endTime;
        this.success = success;
        this.reason = reason;
    }

    public static ExpectationResult success() {
        return new ExpectationResult(currentTimeMillis(), true, SUCCESS);
    }

    public static ExpectationResult failure(String reason) {
        return new ExpectationResult(currentTimeMillis(), false, reason);
    }

    public static ExpectationResult timeoutFailure(long timeout) {
        return new ExpectationResult(currentTimeMillis(), false, TIMEOUT + timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEndTime(), isSuccess(), getReason());
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
        return getEndTime() == that.getEndTime() &&
                isSuccess() == that.isSuccess() &&
                Objects.equal(getReason(), that.getReason());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("endTime", endTime)
                .add("success", success)
                .add("reason", reason)
                .toString();
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}
