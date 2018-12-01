package bg.sofia.uni.fmi.jira;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.interfaces.IssueTracker;
import bg.sofia.uni.fmi.jira.issues.Issue;

public class Jira implements IssueTracker {
	private Issue[] issues;

	public Jira(Issue[] issues) {
		this.issues = issues;
	}

	@Override
	public Issue[] findAll(Component component, IssueStatus status) {

		// return Stream.of(filter....)
		Issue[] result = new Issue[issues.length];
		if (component == null || status == null)
			return result;
		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getComponent().getName().equals(component.getName())
					&& issue.getStatus() == status) {
				result[i++] = issue;
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssuePriority priority) {
		Issue[] result = new Issue[issues.length];

		if (component == null || priority == null)
			return result;

		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getComponent().getName().equals(component.getName())
					&& issue.getPriority() == priority) {
				result[i++] = issue;
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssueType type) {

		Issue[] result = new Issue[issues.length];
		if (component == null || type == null)
			return result;
		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getComponent().getName().equals(component.getName())
					&& issue.getType() == type) {
				result[i++] = issue;
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssueResolution resolution) {
		Issue[] result = new Issue[issues.length];
		if (component == null || resolution == null)
			return result;
		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getComponent().getName().equals(component.getName())
					&& issue.getResolution() == resolution) {
				result[i++] = issue;
			}
		}
		return result;
	}

	@Override
	public Issue[] findAllIssuesCreatedBetween(LocalDateTime startTime, LocalDateTime endTime) {
		Issue[] result = new Issue[issues.length];
		if (startTime == null || endTime == null)
			return result;
		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getCreatedAt().compareTo(startTime) > 0
					&& issue.getCreatedAt().compareTo(endTime) < 0) {
				result[i++] = issue;
			}
		}
		return result;
	}

	@Override
	public Issue[] findAllBefore(LocalDateTime dueTime) {
		Issue[] result = new Issue[issues.length];
		if (dueTime == null)
			return result;
		int i = 0;

		for (Issue issue : issues) {
			if (issue != null && issue.getCreatedAt().compareTo(dueTime) > 0) {
				result[i++] = issue;
			}
		}
		return result;
	}

}
