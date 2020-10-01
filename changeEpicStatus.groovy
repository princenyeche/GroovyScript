import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Level
import com.atlassian.jira.issue.customfields.manager.OptionsManager

def optionsManager = ComponentAccessor.getComponent(OptionsManager)
def customFieldManager = ComponentAccessor.getCustomFieldManager()

def epicstatus = customFieldManager.getCustomFieldObjectByName("Epic Status")
def epicvalue = issue.getCustomFieldValue(epicstatus)
def issueTypeField = customFieldManager.getCustomFieldObjectByName("Issue Type")
def issueTypeValue = issue.getCustomFieldValue(issueTypeField)
def fieldConfig = epicstatus.getRelevantConfig(issue)

def option = optionsManager.getOptions(fieldConfig).getOptionForValue("In Progress", null)

if (issueTypeValue == "Epic") {
issue.setCustomFieldValue(epicstatus, option)
}
//if (epicvalue != "In Progress") {
//    issue.setCustomFieldValue(epicstatus, [option])
//    log.warn("Epic Status has been set...")
//}
//else {
//    log.warn("Unable to set the Epic Status")
//}   
