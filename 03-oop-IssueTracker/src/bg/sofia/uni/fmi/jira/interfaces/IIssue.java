package bg.sofia.uni.fmi.jira.interfaces;

import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;

public interface IIssue {
	public void resolve(IssueResolution resolution);

	public void setStatus(IssueStatus status);

	public String getId();
}
