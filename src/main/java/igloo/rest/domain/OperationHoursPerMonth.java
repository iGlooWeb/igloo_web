package igloo.rest.domain;

/**
 * @author Yikai Gong
 */

public class OperationHoursPerMonth {
    private Integer month;
    private Double hours;

    public OperationHoursPerMonth(Integer month, Double hours) {
        this.month = month;
        this.hours = hours;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
