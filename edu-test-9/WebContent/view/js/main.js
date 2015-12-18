// The root URL for the RESTful services
var rootURL = "http://localhost:9080/edu-test-9/rest/evaluationRules/evaluationRule";

function addRule() {
	alert("Add Rule");
	//var ruleId = $('#idEvaluationRule').val();
	var ruleXml = document.getElementById("textFileContentId").value;	
	alert(ruleXml);
	$.ajax({
		type: 'POST',
		contentType: 'application/xml',
		url: rootURL,
		dataType: "xml",
		//data: "<evaluationRule><idEvaluationRule>1</idEvaluationRule><marketRuleId>103</marketRuleId></evaluationRule> ",
		data:ruleXml,
		success: function(data, textStatus, jqXHR){
			alert('Rule created successfully');
			
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addRule error: ' + textStatus);
		}
	});
}

function updateRule() {
	console.log('updateRule');
	$.ajax({
		type: 'PUT',
		contentType: 'application/xml',
		url: rootURL + '/' + $('#ruleId').val(),
		dataType: "xml",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Rule updated successfully');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('updateRule error: ' + textStatus);
		}
	});
}

function checkJQuery(){
	if (typeof jQuery != 'undefined') {
	    alert("jQuery library is loaded!");
	}else{
	    alert("jQuery library is not found!");
	}
}

function makeDivVisible(){
	document.getElementById("hideDiv").style.display = "block"
}

function addRule2() {
	alert("Test addRule2");
	
}






