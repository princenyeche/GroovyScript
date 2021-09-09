// listener should be issue_updated event

def issue_key = issue.key
def field = issue.fields // current fields

def custom_field = 'customfield_10001' // example, change to your own customfield id

def priority = 'customfield_10002' // same thing as above


// issue result
def pageResult = get("/rest/api/2/issue/${issue_key}")
.header('Content-Type', 'application/json')
.asObject(Map)


// function to update the issue
def updateIssue(custom_id, value, key) {

def updater = put("/rest/api/2/issue/${key}")
.queryString('overrideScreenSecurity', Boolean.TRUE)
.header('Content-Type', 'application/json')
.body([
fields: [
(custom_id): value,
]
])
.asString()
}


if (pageResult.status < 300) {

def customfield_value = field[custom_field] as String
def priority_value = field[priority] as String

if (customfield_value == 'Value_1') {
// add the actual name of the value
priority_value = "P1"
updateIssue(priority, priority_value, issue_key)
}
// add more conditions here
}
