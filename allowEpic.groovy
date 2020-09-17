import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import org.apache.log4j.Level

import \
static com.atlassian.jira.issue.IssueFieldConstants.ISSUE_TYPE

def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def allIssueTypes = ComponentAccessor.constantsManager.allIssueTypeObjects

def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def issueTypeField = getFieldById(ISSUE_TYPE)
String err = "You are not allowed to create an Epic"

def remoteUsersRoles = projectRoleManager.getProjectRoles(user, issueContext.projectObject)*.name
def availableIssueTypes = []

if ("Developers" in remoteUsersRoles) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative"] })
    log.warn(err)
}
if ("Service Desk Team" in remoteUsersRoles) {
    availableIssueTypes.addAll(allIssueTypes.findAll { it.name in ["Bug", "Task", "Story", "Theme", "Initiative", "Epic"] })
}

issueTypeField.setFieldOptions(availableIssueTypes)
