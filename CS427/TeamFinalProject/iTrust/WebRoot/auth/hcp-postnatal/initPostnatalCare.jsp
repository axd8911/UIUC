
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.PostnatalAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Messages";
%>

<%@include file="/header.jsp" %>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-postnatal/initPostnatalCare.jsp");
		return;
	}
%>

<div align=center>
	<h2>Postnatal care Record</h2>
<%
	/* Get the patient information using the patient's MID */
	PostnatalAction action = new PostnatalAction(prodDAO, loggedInMID.longValue());

	String patientName = action.getName(Long.parseLong(pidString));
	session.setAttribute("patient_name", patientName);
	out.println("<b>Patient's MID:</b> " + pidString + "<br>");
	out.println("<b>Patient's Name:</b> " + patientName);
	
	/* check if HCP has OBGYN specialty */
	boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
	boolean isPostnatalCareEligible = action.isPostnatalCareEligible(Long.parseLong(pidString));

	if (isPostnatalCareEligible){

		/* Retrieve patient's existing obstetric records */
		List<PostnatalCareRecordBean> recordList = action.getPostnatalCareRecords(Long.parseLong(pidString));
		//session.setAttribute("obstetRecs", obstetRecs);
		if (recordList.size() > 0) {
%>	
	<table class="fTable">
		<tr>
			<th>Record #</th>
			<th>Record Date</th>
			<th>Childbirth Date</th>
			<th>Delivery Type</th>
			<th>Comment</th>
			<th>Release Date</th>
		</tr>
<%		 
			for(PostnatalCareRecordBean currBean: recordList) {
%>
			<tr>
				
<%              if (isOBGYN){

%>
					<td><a href="/iTrust/auth/hcp-postnatal/editPostnatalCareRecord.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + (currBean.getPostnatalCareRecordId() ))%>"><%= StringEscapeUtils.escapeHtml("" + (currBean.getPostnatalCareRecordId() )) %></a></td>
<%              } else{
%>
                <td><%= StringEscapeUtils.escapeHtml("" + ( currBean.getPostnatalCareRecordId() )) %></td>
                <%}%>
                
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(currBean.getRecordDate() ))) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(currBean.getChildbirthDate() ))) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( currBean.getDeliveryType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( currBean.getAdditionalComment() )) %></td>
				<%             
					if (currBean.getReleaseDate() != null){
				%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( sdf.format(currBean.getReleaseDate() ))) %></td>
				<% } else { %>
				<td><%= StringEscapeUtils.escapeHtml("") %></td>
				<% } %>
			</tr>
			<% } %>
	</table>
<%		} else { %>
	<div>
		<i>The patient has no postnatal care record.</i>
	</div>
		<% } %>
	<br />

<%		if (isOBGYN) { %>

	<a href="/iTrust/auth/hcp-postnatal/addPostnatalCareRecord.jsp">
		 <button id=addPostnatalCareRecordButton>
		 	Add New Record
		 </button>
	</a>
		
		<% } 
	} %>
</div>

<hr />

<%@include file="/footer.jsp" %>
