package user;

import java.time.LocalDate;

public class Fine {
    private int id;
    private int userId;
    private double amount;
    private String violation;
    private LocalDate issueDate;
    private String status;

    public Fine(int id, int userId, double amount, String violation, LocalDate issueDate, String status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.violation = violation;
        this.issueDate = issueDate;
        this.status = status;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}