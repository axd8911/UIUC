<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ObstetAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.ParsePosition"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetBeanLoader" %>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Add Obstetric Record";
%>

<%@include file="/header.jsp" %>

<%
	String myerrormsg = " ";
	ObstetAction action = new ObstetAction(prodDAO, loggedInMID.longValue());

	/* check if HCP has OBGYN specialty */
	boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
	if (!isOBGYN) {
		response.sendError(403);
	}

	/* Get current date as patient initialization date */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c1 = Calendar.getInstance();

	boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
	if (formIsFilled) {

	    /* Check that user provided LMP date is not after today */
		try {
			String lmpDateString = request.getParameter("lmpDate");
			Date lmpDate = sdf.parse(lmpDateString, new ParsePosition(0));


			long dateCutoff = 24192000000L; // 280 days

			if (lmpDate.after(c1.getTime())){
				myerrormsg = "LMP date cannot be after today.";
			} else if (c1.getTime().getTime() - lmpDate.getTime()  > dateCutoff) {
				myerrormsg = "LMP date is too old to be possible, enter a more realistic date.";
			} else {

				try{
					//ObstetRecBean ob = new BeanBuilder<ObstetRecBean>().build(request.getParameterMap(), new ObstetRecBean());

                    if (request.getParameter("lmpDate").length() > 10){
                        throw new FormValidationException();
                    }

					ObstetRecBean ob = ObstetBeanLoader.beanBuilder(request.getParameterMap());

					String pidString = (String) session.getAttribute("pid");
					ob.setMID(Long.parseLong(pidString));

					ob.setInitDate(c1.getTime());

					/* Add a new obstetric record */
					action.addObstetRec(ob, loggedInMID.longValue());
%>
<div align=center>
	<br />
	<br />
	<span class="iTrustMessage">The patient's obstetric record has been successfully added!</span>
</div>
<%
				} catch(FormValidationException e){
					myerrormsg = "Please enter a valid date for Last Menstrual Period (LMP) in format of: DD-MM-YYYY";
				}
			}
		} catch(NullPointerException n){
			myerrormsg = "Bad LMP date. Please enter a valid date for Last Menstrual Period (LMP) in format of: DD-MM-YYYY";
		}
	}
%>
<div align=center>
	<form action="addObstetRec.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true"> <br />
		<br />
		<span class="iTrustError"><%= myerrormsg %></span>
		<table class="fTable">
			<tr>
				<th colspan=2>Obstetric Record</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Patient MID:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("pid")) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Patient Name:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("obPatientName")) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Initialization Date:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + sdf.format(c1.getTime())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">LMP:</td>
				<td><input type="date" name="lmpDate"></td>
			</tr>
		</table>
		<br />

		<input id="addPatient" type="submit" style="font-size: 16pt; font-weight: bold;" value="Add">
		<input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp'; return false;"
			   value="View Records" style="font-size: 16pt; font-weight: bold;">

	</form>

	<br />
</div>
<%@include file="/footer.jsp" %>
