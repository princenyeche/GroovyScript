import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.groups.GroupManager
import org.apache.log4j.Level

import \
static com.atlassian.jira.issue.IssueFieldConstants.ISSUE_TYPE

def projectGroupManager = ComponentAccessor.getComponent(GroupManager)
def allIssueTypes = ComponentAccessor.constantsManager.allIssueTypeObjects

def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def issueTypeField = getFieldById(ISSUE_TYPE)
String err = "You are not allowed to create an Epic"
String group1 = "jira-administrators"
String group2 = "gym-group"

def remoteUsersGroup = projectGroupManager.getGroupNamesForUser(user)
def availableIssueTypes = []

if (remoteUsersGroup.contains(group1)) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative"] })
    log.warn(err)
}
if (remoteUsersGroup.contains(group2)) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative", "Epic"] })
}

issueTypeField.setFieldOptions(availableIssueTypes)
