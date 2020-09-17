import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Level

def customFieldManager = ComponentAccessor.customFieldManager

def numberOne = getCustomFieldValue("Number One")
def numberTwo = getCustomFieldValue("Number two")


if (numberOne && numberTwo != null) {
    def sum = numberOne + numberTwo
    sum as int
}
else if(numberOne || numberTwo == null){
    def err = "Number One or Number two field must not be empty"
    log.warn(err)
}
else{
    def err = "SumNumber Field requires the sum of Number one and Number two field"
    log.warn(err)
}
