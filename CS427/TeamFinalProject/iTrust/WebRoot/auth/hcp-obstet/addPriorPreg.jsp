<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ObstetAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.loaders.PriorPregBeanLoader" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Prior Pregnancy";
%>

<%@include file="/header.jsp" %>

<%

ObstetAction action = new ObstetAction(prodDAO, loggedInMID.longValue());

/* check if HCP has OBGYN specialty */
boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
if (!isOBGYN) {
	response.sendError(403);
}

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
if (formIsFilled) {

	try{
		//ObstetPriorPregBean ob = new BeanBuilder<ObstetPriorPregBean>().build(request.getParameterMap(), new ObstetPriorPregBean());
		
		ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(request.getParameterMap());
		
		String pidString = (String) session.getAttribute("pid");
		ob.setMid(Long.parseLong(pidString));


		
		/* Add a new prior pregnancy record */
		action.addPriorPreg(ob, loggedInMID.longValue());
		
%>
	<div align=center>
		<br />
		<br />
		<span class="iTrustMessage">The patient's prior pregnancy record has been successfully added!</span>
	</div>
<%
	} catch(FormValidationException e){
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
<%
	}
}
%>

<div align=center>
<form action="addPriorPreg.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<p>Please enter the patient's prior pregnancy information.</p>
<table class="fTable">
	<tr>
		<th colspan=2>Prior Pregnancy</th>
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
		<td class="subHeaderVertical">Year of Conception:</td>
		<td><input type="text" name="conceptionYear"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Pregnant Weeks:</td>
		<td><input type="text" name="pregnant_weeks" size="8"> weeks, <input type="text" name="pregnant_days" size="8"> days</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Hours in Labor:</td>
		<td><input type="text" name="laborHours"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Weight Gained:</td>
		<td><input type="text" name="weightGain"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Delivery Type:</td>
		<td>
			<select name="deliveryType">
				<option value="vaginal_delivery">Vaginal Delivery</option>
				<option value="vaginal_delivery_vacuum_assist">Vaginal Delivery Vacuum Assist</option>
				<option value="vaginal_delivery_forceps_assist">Vaginal Delivery Forceps Assist</option>
				<option value="caesarean_section">Casesarean Section</option>
				<option value="miscarriage">Miscarriage</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Multiplet:</td>
		<td><input type="text" name="multiplet"></td>
	</tr>
</table>
<br />

<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add">
<input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp'; return false;" 
		value="View Records" style="font-size: 16pt; font-weight: bold;">
		
</form>
<br />
</div>

<%@include file="/footer.jsp" %>
