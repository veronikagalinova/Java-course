package bg.sofia.uni.fmi.jira.issues;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

public class NewFeature extends Issue {
	private LocalDateTime dueTime;

	public NewFeature(IssuePriority priority, Component component, User reporter, String description,
			LocalDateTime dueTime) throws InvalidReporterException {
		super(priority, component, reporter, description);
		this.setDueTime(dueTime);
	}

	@Override
	public IssueType getType() {
		// TODO Auto-generated method stub
		return IssueType.NEW_FEATURE;
	}

	public LocalDateTime getDueTime() {
		return dueTime;
	}

	public void setDueTime(LocalDateTime dueTime) {
		this.dueTime = dueTime;
	}

}
