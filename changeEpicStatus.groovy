import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Level
import com.atlassian.jira.issue.customfields.manager.OptionsManager

def optionsManager = ComponentAccessor.getComponent(OptionsManager)
def customFieldManager = ComponentAccessor.getCustomFieldManager()

def epicstatus = customFieldManager.getCustomFieldObjectByName("Epic Status")
def epicvalue = issue.getCustomFieldValue(epicstatus)

def fieldConfig = epicstatus.getRelevantConfig(issue)

def option = optionsManager.getOptions(fieldConfig).getOptionForValue("In Progress", null)

if (epicvalue != null) {
    issue.setCustomFieldValue(epicstatus, option)
    log.info("Epic Status has been set...")
}
else {
    log.warn("Unable to set the Epic Status")
}
