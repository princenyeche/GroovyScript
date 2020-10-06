// Import required packages
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.event.issue.link.IssueLinkCreatedEvent
import com.atlassian.jira.event.issue.link.IssueLinkDeletedEvent
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder

// Define necessary variables
def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueIndexManager = ComponentAccessor.getComponent(IssueIndexingService)
def user = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser()


// Replace Custom Field IDs accordingly (required)
def initOrderField = customFieldManager.getCustomFieldObject("customfield_10301")

// Identify if the event is Issue Link Created or Deleted
if (event instanceof IssueLinkCreatedEvent)
    event = event as IssueLinkCreatedEvent
else if (event instanceof IssueLinkDeletedEvent)
    event = event as IssueLinkDeletedEvent

// Get the context issue, its Issue Type and Initiative Order
def contextIssue = event.getIssueLink().getDestinationObject()
def issuetype = contextIssue.getIssueType().getName()
def initOrder = contextIssue.getCustomFieldValue(initOrderField)
// get a boolean of the issue
def subContextIssue = contextIssue.isSubTask()

// log infos
log.info(event)
log.info("Context Issue: " + contextIssue.getKey() + ' / ' + issuetype + ' / ' + initOrder)
log.info("Subcontext: " + contextIssue.isSubTask())



// Check if the context issue has a Parent
def parentIssue, initOrderParent


// A Sub-task always has a Parent
if (contextIssue.parentObject) {
    parentIssue = contextIssue.parentObject
}
// If there's a Parent issue, get its Initiative Order
if (parentIssue) {
    initOrderParent = parentIssue.getCustomFieldValue(initOrderField)
    log.info("Parent Issue: " + parentIssue?.getKey() + ' / ' + initOrderParent)
}
// Check that the issue is indeed a subTask
    if (subContextIssue == true) {
        // Update Initiative Order if not exists or mismatch
        if (!initOrder || initOrder != initOrderParent) {
            initOrderField.updateValue(null, contextIssue, new ModifiedValue(initOrder, initOrderParent), new DefaultIssueChangeHolder())

            // Reindex the issue
            issueIndexManager.reIndex(contextIssue)

            log.info("Initiative Order is set to " + initOrderParent + " for " + contextIssue.getKey())
        } else
            log.info("Initiative Order remains unchanged for " + contextIssue.getKey())
    }
    else {
        // If there's no Parent or Parent doesn't have Initiative Order
        if (!parentIssue || !initOrderParent) {
            // Clear Initiative Order if exists
            if (subContextIssue == false) {
                log.info("Initiative Order is cleared as this issue" + contextIssue.getKey() + " is Deleted")
            }
            else if (initOrder) {
                initOrderField.updateValue(null, contextIssue, new ModifiedValue(initOrder, null), new DefaultIssueChangeHolder())

                // Reindex the issue
                issueIndexManager.reIndex(contextIssue)

                log.info("Initiative Order is cleared for " + contextIssue.getKey())
            } else
                log.info("Initiative Order remains empty for " + contextIssue.getKey())
        }

    }
