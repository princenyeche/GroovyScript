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

def remoteUsersGroup = projectGroupManager.getGroupsForUser(user.username)
def availableIssueTypes = []

if ("jira-administrator" in remoteUsersGroup) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative"] })
    log.warn(err)
}
if ("gym-group" in remoteUsersGroup) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative", "Epic"] })
}

issueTypeField.setFieldOptions(availableIssueTypes)
