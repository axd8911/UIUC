<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.text.ParseException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVApptBean"%>
<%@page import="edu.ncsu.csc.itrust.action.CHVApptAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Modify Childbirth Hospital Visit";

String headerMessage = "Please fill out the form properly - comments are optional.";
String pidString = (String) session.getAttribute("pid");
long patientID = Long.parseLong(pidString);		
String lastSchedDate="";
String lastApptType="";
String lastTime1="";
String lastTime2="";
String lastTime3="";
String lastComment="";
String lastPreferDMethod="";
String lastWhenSchedules="";
boolean invalidDate = false;
boolean ignoreConflicts = true;
String recID ="";


String isVD = "";
String isVDVA = "";
String isVDFA = "";
String isCS = "";
String isM = "";
String isOV = "";
String isEV = "";

%>

<%@include file="/header.jsp" %>

<%

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");

CHVApptAction action = new CHVApptAction(prodDAO, loggedInMID.longValue());

recID = request.getParameter("recID");
if(recID != null && recID.length() > 0 && !formIsFilled) {
	CHVApptBean original = action.getAppt(Integer.parseInt(recID));
	
	Date oldDate = new Date(original.getDate().getTime());
	DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat tFormat = new SimpleDateFormat("hhmma");
	String hPart = tFormat.format(oldDate).substring(0,2);
	String mPart = tFormat.format(oldDate).substring(2,4);
	String aPart = tFormat.format(oldDate).substring(4);
	
	lastSchedDate=dFormat.format(oldDate);
	lastApptType="Childbirth";
	lastTime1=hPart;
	lastTime2=mPart;
	lastTime3=aPart;
	lastComment=original.getComment();
	if(lastComment == null) lastComment="";
	lastPreferDMethod=original.getPreferDMethod();
	lastWhenSchedules=original.getWhenScheduled();
	
	// for handling which delivery method should be selected in the drop down menu.
	isVD = (lastPreferDMethod.equals("vaginal_delivery")) ? "selected" : "";
	isVDVA = (lastPreferDMethod.equals("vaginal_delivery_vacuum_assist")) ? "selected" : "";
	isVDFA = (lastPreferDMethod.equals("vaginal_delivery_forceps_assist")) ? "selected" : "";
	isCS = (lastPreferDMethod.equals("caesarean_section")) ? "selected" : "";
	isM = (lastPreferDMethod.equals("miscarriage")) ? "selected" : "";
	
	isOV = (lastWhenSchedules.equals("office_visit")) ? "selected" : "";
	isEV = (lastWhenSchedules.equals("emergency_visit")) ? "selected" : "";

}

if (formIsFilled && recID != null && recID.length() > 0) {
		try {
			
			lastSchedDate = request.getParameter("schedDate");
			lastApptType = "Childbirth";
			lastTime1 = request.getParameter("time1");
			lastTime2 = request.getParameter("time2");
			lastTime3 = request.getParameter("time3");
			lastComment = request.getParameter("comment");
			lastPreferDMethod = request.getParameter("prefCbMethod");
			lastWhenSchedules = request.getParameter("whenScheduled");
			recID = request.getParameter("recID");
			
			CHVApptBean appt = new CHVApptBean();
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			format.setLenient(false);
			try{
				Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);
				appt.setDate(new Timestamp(date.getTime()));
			}catch(ParseException e){
				invalidDate=true;
			}
			
			if(invalidDate==false){
				appt.setHcp(loggedInMID);
				appt.setPatient(patientID);
				appt.setApptType(lastApptType);
				String comment = "";
				
				if(request.getParameter("comment").equals(""))
					comment = null;
				else 
					comment = request.getParameter("comment");
				appt.setComment(comment);
				
				appt.setPreferDMethod(lastPreferDMethod);
				appt.setWhenScheduled(lastWhenSchedules);
				appt.setApptID(Integer.parseInt(recID));
				
				if (request.getParameter("editAppt").equals("Change")) {
					headerMessage = action.editAppt(appt, ignoreConflicts);
	%>
				<div align=center>
					<br />
					<br />
					<span class="iTrustMessage">The childbirth hospital visit appointment has been successfully changed!</span>
				</div>
	<% 
				} else if (request.getParameter("editAppt").equals("Remove")) {
					headerMessage = action.removeAppt(appt);
	%>
				<div align=center>
					<br />
					<br />
					<span class="iTrustMessage">The childbirth hospital visit appointment has been successfully removed!</span>
				</div>
	<% 
				}
			} else {
				headerMessage = "Please input a valid date for the appointment.";
			}
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
<form action="editCHVAppt.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<h2>Modify Childbirth Hospital Visit</h2>
<table class="fTable">
	<tr>
		<td class="subHeaderVertical">Patient MID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("pid")) %></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Patient Name:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("obPatientName")) %></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Appointment Type:</td>
		<td>Childbirth</td>
	</tr>
		<tr>
		<td class="subHeaderVertical">When Scheduled:</td>
		<td>
			<select name="whenScheduled">
				<option value="office_visit" <%=isOV%>>Office Visit</option>
				<option value="emergency_visit" <%=isEV%>>Emergency Room Visit</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Schedule Date:</td>
		<td><input type="text" name="schedDate" value="<%=lastSchedDate %>"/>
		<input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" /></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Schedule Time:</td>
		<td><select name="time1">
			<%
				String hour = "";
				for(int i = 1; i <= 12; i++) {
					if(i < 10) hour = "0"+i;
					else hour = i+"";
					%>
						<option <% if(hour.toString().equals(lastTime1)) out.print("selected='selected'"); %> value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
					<%
				}
			%>
		</select>:<select name="time2">
			<%
				String min = "";
				for(int i = 0; i < 60; i+=5) {
					if(i < 10) min = "0"+i;
					else min = i+"";
					%>
						<option <% if(min.toString().equals(lastTime2)) out.print("selected='selected'"); %> value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
					<%
				}
			%>
		</select>
		<select name="time3"><option <% if("AM".equals(lastTime3)) out.print("selected='selected'"); %> value="AM">AM</option
		><option  <% if("PM".equals(lastTime3)) out.print("selected='selected'"); %> value="PM">PM</option></select></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Preferred Childbirth Method:</td>
		<td>
			<select name="prefCbMethod">
				<option value="vaginal_delivery" <%=isVD%>>Vaginal Delivery</option>
                <option value="vaginal_delivery_vacuum_assist" <%=isVDVA%>>Vaginal Delivery Vacuum Assist</option>
                <option value="vaginal_delivery_forceps_assist" <%=isVDFA%>>Vaginal Delivery Forceps Assist</option>
                <option value="caesarean_section" <%=isCS%>>Casesarean Section</option>
                <option value="miscarriage" <%=isM%>>Miscarriage</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Comment:</td>
		<td>
			<textarea name="comment" cols="60" rows="10"><%= StringEscapeUtils.escapeHtml(lastComment) %></textarea>
		</td>
	</tr>
</table>
<br />

<input type="hidden" name="recID" value="<%=recID %>"/>
<input type="hidden" id="editAppt" name="editAppt" value=""/>
<input type="submit" value="Change" name="editApptButton" id="changeButton" onClick="document.getElementById('editAppt').value='Change'" style="font-size: 16pt; font-weight: bold;" />
<input type="submit" value="Remove" name="editApptButton" id="removeButton" onClick="document.getElementById('editAppt').value='Remove'" style="font-size: 16pt; font-weight: bold;" />
<input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/obstetricCHV.jsp'; return false;" 
		value="View Records" style="font-size: 16pt; font-weight: bold;">
		
</form>
</div>

<%@include file="/footer.jsp" %>
