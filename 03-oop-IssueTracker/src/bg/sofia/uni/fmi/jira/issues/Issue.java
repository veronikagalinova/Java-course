package bg.sofia.uni.fmi.jira.issues;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.interfaces.IIssue;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidComponentException;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidDescriptionException;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidPriorityException;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

public abstract class Issue implements IIssue {
	private String id;
	private IssuePriority priority;
	private IssueResolution resolution;
	private IssueStatus status;
	private Component component;
	private User reporter;
	private IssueType issueType;
	private String description;
	private LocalDateTime creationalTime;
	private LocalDateTime lastModification;
	
	public Issue(IssuePriority priority, Component component, User reporter, String description)
			throws InvalidReporterException {
		
		validate(priority,component,reporter,description);
		
		this.priority = priority;
		this.component = component;
		this.reporter = reporter;
		this.description = description;
		this.resolution = IssueResolution.UNRESOLVED;
		this.status = IssueStatus.OPEN;
		this.creationalTime = LocalDateTime.now();
	}

	@Override
	public void resolve(IssueResolution resolution) {
		this.resolution = resolution;
		this.lastModification = LocalDateTime.now();
	}

	@Override
	public void setStatus(IssueStatus status) {
		this.status = status;
		this.lastModification = LocalDateTime.now();
	}

	@Override
	public String getId() {
		return component.getShortName() + IdGenerator.getId();
	}
	
	public Component getComponent() {
		return component;
	}
	
	public IssueStatus getStatus() {
		return status;
	}

	public IssuePriority getPriority() {
		return priority;
	}

	public void setPriority(IssuePriority priority) {
		this.priority = priority;
	}

	public IssueResolution getResolution() {
		return resolution;
	}

	public void setResolution(IssueResolution resolution) {
		this.resolution = resolution;
	}
	
	public LocalDateTime  getCreatedAt() {
		return creationalTime;
	}
	
	public LocalDateTime getLastModifiedAt() {
		return lastModification;
	}
	
	public abstract IssueType getType();
	
	private void validate(IssuePriority priority, Component component, User reporter, String description) throws InvalidReporterException {
		if (reporter == null) {
			throw new InvalidReporterException("Reposter is null!");
		}
		
		if (priority == null) {
			throw new InvalidPriorityException("Priority cannot be null!");
		}
		
		if (component == null) {
			throw new InvalidComponentException("Component cannot be null!");
		}
		
		if (description == null) {
			throw new InvalidDescriptionException("Description cannot be null!");
		}
	}

}
