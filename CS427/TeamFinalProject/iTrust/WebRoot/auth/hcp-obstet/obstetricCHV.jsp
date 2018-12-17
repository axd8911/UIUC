
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVApptBean"%>
<%@page import="edu.ncsu.csc.itrust.action.CHVApptAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean"%>
<%@page import="edu.ncsu.csc.itrust.action.CHVDeliveryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetAction"%>

<link rel="stylesheet" href="/iTrust/css/w3.css">
<script>
function viewOBRecords() {
    window.open("/iTrust/auth/hcp-obstet/obstetricCHV_OB.jsp", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,top=100,left=300,width=800,height=400");
}
</script>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Childbirth Hospital Visit";
%>

<%@include file="/header.jsp" %>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricCHV.jsp");
		return;
	}


%>

<div align=center>
<%
	/* Get the patient information using the patient's MID */
	ObstetAction action = new ObstetAction(prodDAO, loggedInMID.longValue());

	String patientName = action.getName(Long.parseLong(pidString));

	session.setAttribute("obPatientName", patientName);
	
	/* check if HCP has OBGYN specialty */
	boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
	boolean isObstetricEligible = action.isObstetricEligible(Long.parseLong(pidString));

	if (isObstetricEligible){
		
%>	

<div align=center>
	<h2>Childbirth Hospital Visit</h2>
<%	
	out.println("<b>Patient's Name:</b> " + patientName + "<br>");
%>
	<button onclick="viewOBRecords()">View Obstetric Initialization Records</button>
	<br><br>

<%	
		/* Retrieve patient's childbirth hospital visit appointment */
		CHVApptAction apptAction = new CHVApptAction(prodDAO, loggedInMID.longValue());
		List<CHVApptBean> appts = apptAction.getAppointments(Long.parseLong(pidString));
		if (appts.size() > 0) {
%>	
	<table class="fTable">
		<tr>
			<th>Appointment Date/Time</th>
			<th>Schedule Type</th>
			<th>Preferred Delivery</th>
			<th>When Scheduled</th>
			<th>Comments</th>
		</tr>
<%		 
			for(CHVApptBean appt : appts) {
%>
			<tr>
<%              if (isOBGYN){

%>
				<td><a href="/iTrust/auth/hcp-obstet/editCHVAppt.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + ( appt.getApptID() ))%>"><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(appt.getDate()) )) %></a></td>
<%              } else{
%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(appt.getDate()) )) %></td>
				<%}%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( appt.getApptType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( appt.getPreferDMethod() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( appt.getWhenScheduled() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( appt.getComment() )) %></td>
				
			</tr>
	<%
			}
	%>
	</table>
<%		} else { %>
	<div>
		<i>The patient has no appointment.</i>
	</div>
<%		} %>
	<br />

<%		if (isOBGYN) { %>
	<a href="/iTrust/auth/hcp-obstet/scheduleCHVAppt.jsp" style="text-decoration: none;" class="w3-button w3-white w3-border"">
	Schedule an Appointment</a>
		
<%		} %>

</div>
<hr />

<div align=center>
	<h2>Childbirth Delivery</h2>
<%	
		/* Retrieve patient's childbirth delivery record */
		CHVDeliveryAction delvAction = new CHVDeliveryAction(prodDAO, loggedInMID.longValue());
		List<CHVDeliveryBean> delvs = delvAction.getDelvs(Long.parseLong(pidString));
		if (delvs.size() > 0) {
%>	
	<table class="fTable">
		<tr>
			<th>Delivery Date/Time</th>
			<th>Delivery Method</th>
			<th>Child Born</th>
		</tr>
<%		 
			for(CHVDeliveryBean p : delvs) {
%>
			<tr>
<%              if (isOBGYN){

%>
				<td><a href="/iTrust/auth/hcp-obstet/editCHVDelivery.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + ( p.getDeliveryID() ))%>"><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(p.getDate()) )) %></a></td>
<%              } else{
%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(p.getDate()) )) %></td>
				<%}%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getDeliveryMethod() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("") %></td>
			</tr>
	<%
			}
	%>
	</table>
<%		} else { %>
	<div>
		<i>The patient has no childbirth delivery record.</i>
	</div>
<%		} %>
	<br />

<%		if (isOBGYN) { %>
	<a href="/iTrust/auth/hcp-obstet/addCHVDelivery.jsp" style="text-decoration: none;" class="w3-button w3-white w3-border"">
	Add a Childbirth Delivery</a>
		
<%		} %>

<%
	} else{
%>
	<div align=center>
		<br />
		<br />
		<span class="iTrustMessage">The selected patient is not eligible for obstetric care.  Please select a different patient.</span>
	</div>
	<%
}
%>

	

</div>

<%@include file="/footer.jsp" %>
