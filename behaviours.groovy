import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.context.IssueContext
import org.apache.log4j.Level


def user = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()
def issueTypeName = getIssueContext().issueType.name
def fieldName = getFieldById("issuetype")
def epicName = getFieldById("Epic Name")
def projectKey = getIssueContext().projectObject.name
String err = "<b>You are not allowed to create an Epic</b>"

if (issueTypeName == "Epic" && projectKey in "AF" && user == user) {
    log.warn(err)
    fieldName.setHidden(true)
    epicName.setHidden(true)
} 
