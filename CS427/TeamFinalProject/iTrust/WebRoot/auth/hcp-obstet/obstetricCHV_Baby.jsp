<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean"%>
<%@page import="edu.ncsu.csc.itrust.action.CHVDeliveryAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<script>
function goBack() {
    window.history.back();
}
</script>

<%
pageTitle = "iTrust - View Baby";
%>

<%@include file="/header.jsp" %>

<%
long lastBabyID=0;
long lastDeliveryID=0;
long lastMid=0;
String lastFirstName="";
String lastLastName="";
String lastGender="";
String isMale = "";
String isFemale = "";

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
CHVDeliveryAction action = new CHVDeliveryAction(prodDAO, loggedInMID.longValue());

String recID = request.getParameter("recID");

if(recID != null && recID.length() > 0 && !formIsFilled) {
	
	CHVBabyBean baby = action.getBaby(Integer.parseInt(recID));
	lastBabyID=baby.getBabyID();
	lastDeliveryID=baby.getDeliveryID();
	lastMid=baby.getMid();
	lastFirstName=baby.getFirstName();
	lastLastName=baby.getLastName();
	lastGender=baby.getGender();
	
	isMale = (lastGender.equals("Male")) ? "selected" : "";
	isFemale = (lastGender.equals("Female")) ? "selected" : "";
}
%>

<div align=center>
<form action="addPatient.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<h2>New Baby</h2>
<table class="fTable">
	<tr>
		<th colspan=2>New Baby Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">First name:</td>
		<td><input type="text" name="firstName" value="<%=lastFirstName %>"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Last Name:</td>
		<td><input type="text" name="lastName" value="<%=lastLastName %>"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Gender:</td>
		<td><select name="gender"><option value="Male" <%=isMale%>>M</option><option value="Female" <%=isFemale%>>F</option></select></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Parent's MID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("pid")) %></td>
	</tr>
</table>
<br />
</form>
<br />
</div>
<div align=center>
	<button onclick="goBack()">Go Back</button>
</div>
<%@include file="/footer.jsp" %>
