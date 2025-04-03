package user;

import java.time.LocalDate;

public class Fine {
    private int id, userId;
    private double amount;
    private String violation, status;
    private LocalDate issueDate;

    public Fine(int id, int userId, double amount, String violation, LocalDate issueDate, String status) {
        this.id = id;
        this.setUserId(userId);
        this.amount = amount;
        this.violation = violation;
        this.issueDate = issueDate;
        this.status = status;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getViolation() { return violation; }
    public LocalDate getIssueDate() { return issueDate; }
    public String getStatus() { return status; }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
