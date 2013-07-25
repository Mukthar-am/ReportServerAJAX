<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AJAX calls to Servlet using JQuery and JSON</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(document).ready(function() {
		$('#dimension').change(function(event) {
			var $dim = $("select#dimension").val();
			$.get('ActionServlet', {
				dimension : $dim
			}, function(responseJson) {
				var $select = $('#details');
				$select.find('option').remove();
				$.each(responseJson, function(key, value) {
					$('<option>').val(key).text(value).appendTo($select);
				});
			});
		});
	});
	

	$(document).ready(function() {
		
			var $dim = $("select#details").val();
			$.get('adformatresults', {
				groupby : $dim
			}, function(responseJson) {
				var $select = $('#details');
				$select.find('option').remove();
				$.each(responseJson, function(key, value) {
					$('<option>').val(key).text(value).appendTo($select);
				});
			});
		
	});
	
	
</script>
</head>
<body>

	<h1>PSO webapp server: Results Page</h1>
	<img src="/ReportServer/images/index.jpg" align="right" />

	<br /> Group Results By:
	<select id="dimension">
		<option>Group by...</option>
		<option value="Slot">Slot</option>
		<option value="Creative">Creative</option>
		<option value="Sdk">Sdk</option>
		<option value="Platform">Platform</option>
		<option value="Os-Version">Os-Version</option>
	</select>

	<br />
	<br /> Dimension Value:
	<select id="details">
		<option>details</option>
	</select>

	<br />
	<br />
	<input type="button" id="submit" value="Submit Query"
		onclick="buttonclick">
	<!--  <INPUT TYPE="BUTTON" VALUE="Button 1" ONCLICK="buttonAction()"> -->

	<FORM NAME="form1" METHOD="POST">
		<INPUT TYPE="HIDDEN" NAME="buttonName"> <INPUT TYPE="BUTTON"
			VALUE="Button 1" ONCLICK="button1()">
	</FORM>


</body>
</html>