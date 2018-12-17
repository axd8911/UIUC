<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.ObstetAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf'%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@include file="/global.jsp" %>
<%@include file="/authenticate.jsp"%>

<%
pageTitle = "iTrust - Obstetric Initialization Records";
%>

<!DOCTYPE HTML>
<html>
<head>
<title><%=StringEscapeUtils.escapeHtml("" + (pageTitle))%></title>
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<link href="/iTrust/css.jsp" type="text/css" rel="stylesheet" />
	<link href="/iTrust/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/iTrust/css/dashboard.css" rel="stylesheet" />
	<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
	<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>
	<script src="/iTrust/js/jquery-1.8.3.js" type="text/javascript"></script>
	<script src="/iTrust/js/SwipeableElem.js" type="text/javascript"></script>
	<script src="/iTrust/js/slidyRabbit.js" type="text/javascript"></script>
	
</head>
<body>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricCHV.jsp");
		return;
	}
%>

<div align=center>
	<h3>Obstetric Initialization Records</h3>
<%
	/* Get the patient information using the patient's MID */
	ObstetAction action = new ObstetAction(prodDAO, loggedInMID.longValue());

	String patientName = action.getName(Long.parseLong(pidString));


	session.setAttribute("obPatientName", patientName);
	out.println("<b>Patient's MID:</b> " + pidString + "<br>");
	out.println("<b>Patient's Name:</b> " + patientName);
	
	/* check if HCP has OBGYN specialty */
	boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
	boolean isObstetricEligible = action.isObstetricEligible(Long.parseLong(pidString));

	if (isObstetricEligible){



		/* Retrieve patient's existing obstetric records */
		List<ObstetRecBean> obstetRecs = action.getObstetRecs(Long.parseLong(pidString));
		//session.setAttribute("obstetRecs", obstetRecs);
		if (obstetRecs.size() > 0) {
%>	
	<table class="fTable">
		<tr>
			<th>Record #</th>
			<th>LMP</th>
			<th>EDD</th>
			<th>Pregnant Weeks</th>
			<th>Initialization Date</th>
		</tr>
<%		 
			for(ObstetRecBean a : obstetRecs) {
%>
			<tr>
<%              if (isOBGYN){

%>
				<td><a href="/iTrust/auth/hcp-obstet/editObstetRec.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + ( a.getObstetRecID() ))%>"><%= StringEscapeUtils.escapeHtml("" + ( a.getObstetRecID() )) %></a></td>
<%              } else{
%>
                <td><%= StringEscapeUtils.escapeHtml("" + ( a.getObstetRecID() )) %></td>
                <%}%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(a.getLmpDate() ))) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(a.getEddDate() ))) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( a.getPregWeeks() )) %> Weeks, <%= StringEscapeUtils.escapeHtml("" + ( a.getPregDays() )) %> Days</td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(a.getInitDate() ))) %></td>
			</tr>
	<%
		}
	%>
	</table>
<%		} else { %>
	<div>
		<i>The patient has no obstetric record.</i>
	</div>
<%		} %>
	<br />
	
	<button onclick="self.close()">Close</button>

<br />
<br />
</div>


	<%
}
%>
	</body>
</html>

