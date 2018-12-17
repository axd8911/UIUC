
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


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Obstetric Initialization Records";
%>

<%@include file="/header.jsp" %>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/initObstetPatient.jsp");
		return;
	}


%>

<div align=center>
	<h2>Obstetric Record</h2>
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

<%		if (isOBGYN) { %>

	<a href="/iTrust/auth/hcp-obstet/addObstetRec.jsp">
		 <button id=addObstetRecButton>
		 	Add Obstetric Record
		 </button>
	</a>
	<!--  input id="addObstetRecButton" type="button" onclick="window.location.href='/iTrust/auth/hcp-obstet/addObstetRec.jsp'; return false;" 
		value="Add Obstetric Record" style="font-size: 100%; font-weight: bold;" /-->
		
<%		} %>

</div>

<hr />

<div align=center>
	<h2>Prior Pregnancy</h2>
<%	
		/* Retrieve patient's prior pregnancy records */
		List<ObstetPriorPregBean> priorPregs = action.getPriorPregs(Long.parseLong(pidString));
		if (priorPregs.size() > 0) {
%>	
	<table class="fTable">
		<tr>
			<th>Record #</th>
			<th>Conception Year</th>
			<th>Pregnant Weeks</th>
			<th>Labor Hours</th>
			<th>Weight Gained</th>
			<th>Delivery Type</th>
			<th>Multiplet</th>
		</tr>
<%		 
			for(ObstetPriorPregBean p : priorPregs) {
%>
			<tr>
<%              if (isOBGYN){

%>
				<td><a href="/iTrust/auth/hcp-obstet/editPriorPreg.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + ( p.getPriorPregRecID() ))%>"><%= StringEscapeUtils.escapeHtml("" + ( p.getPriorPregRecID() )) %></a></td>
<%              } else{
%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getPriorPregRecID() )) %></td>
				<%}%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getConceptionYear() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getPregWeeks() )) %> Weeks, <%= StringEscapeUtils.escapeHtml("" + ( p.getPregDays() )) %> Days</td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getLaborHours() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getWeightGain() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getDeliveryType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( p.getMultiplet() )) %></td>
			</tr>
	<%
			}
	%>
	</table>
<%		} else { %>
	<div>
		<i>The patient has no prior pregnancy record.</i>
	</div>
<%		} %>
	<br />

<%		if (isOBGYN) { %>

	<a href="/iTrust/auth/hcp-obstet/addPriorPreg.jsp">
		 <button id=addPriorPregButton >
		 	Add Prior Pregnancy
		 </button>
	</a>
		
<%		}
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
