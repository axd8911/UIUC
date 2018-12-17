<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.text.ParseException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean"%>
<%@page import="edu.ncsu.csc.itrust.action.CHVDeliveryAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<link rel="stylesheet" href="/iTrust/css/w3.css">

<script src="/iTrust/js/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    var maxField = 10; //Input fields increment limitation
    var addButton = $('.add_button'); //Add button selector
    var wrapper = $('.field_wrapper'); //Input field wrapper
    var fieldHTML = '<div>First Name: <input type="text" name="first_name[]" value="" size="6"/> Last Name: <input type="text" name="last_name[]" value="" size="6"/> Gender: <select name="gender[]"><option value="Male">M</option><option value="Female">F</option></select> <a href="javascript:void(0);" class="remove_button">Remove</a></div>'; //New input field html 
    var x = 1; //Initial field counter is 1
    
    //Once add button is clicked
    $(addButton).click(function(){
        //Check maximum number of input fields
        if(x < maxField){ 
            x++; //Increment field counter
            $(wrapper).append(fieldHTML); //Add field html
        }
    });
    
    //Once remove button is clicked
    $(wrapper).on('click', '.remove_button', function(e){
        e.preventDefault();
        $(this).parent('div').remove(); //Remove field html
        x--; //Decrement field counter
    });
});
</script>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Childbirth Delivery";

String headerMessage = "Please fill out the form properly - comments are optional.";
String pidString = (String) session.getAttribute("pid");
long patientID = Long.parseLong(pidString);		
String lastDeliveryDate="";
String lastTime1="";
String lastTime2="";
String lastTime3="";
String lastDeliveryMethod="";
int lastDosPitocin=0;
int lastDosNitrous=0;
int lastDosPethidine=0;
int lastDosEpidural=0;
int lastDosMagnesium=0;
int lastDosRHimmune=0;
boolean invalidDate = false;

%>

<%@include file="/header.jsp" %>

<%

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
if (formIsFilled) {

	try {
		
		CHVDeliveryAction action = new CHVDeliveryAction(prodDAO, loggedInMID.longValue());
		CHVDeliveryBean delivery = new CHVDeliveryBean();
		
		lastDeliveryDate = request.getParameter("deliveryDate");
		lastTime1 = request.getParameter("time1");
		lastTime2 = request.getParameter("time2");
		lastTime3 = request.getParameter("time3");
		lastDeliveryMethod = request.getParameter("deliveryMethod");
		lastDosPitocin=Integer.parseInt(request.getParameter("dosPitocin"));
		lastDosNitrous=Integer.parseInt(request.getParameter("dosNitrous"));
		lastDosPethidine=Integer.parseInt(request.getParameter("dosPethidine"));
		lastDosEpidural=Integer.parseInt(request.getParameter("dosEpidural"));
		lastDosMagnesium=Integer.parseInt(request.getParameter("dosMagnesium"));
		lastDosRHimmune=Integer.parseInt(request.getParameter("dosRHimmune"));
		String[] firstname=request.getParameterValues("first_name[]");
		String[] lastname=request.getParameterValues("last_name[]");
		String[] gender=request.getParameterValues("gender[]");
		if (firstname != null && lastname != null && gender != null) {
			for (int i=0; i<firstname.length; i++) {
				CHVBabyBean baby = new CHVBabyBean();
				baby.setFirstName(firstname[i]);
				baby.setLastName(lastname[i]);
				baby.setGender(gender[i]);
				delivery.addBaby(baby);
			}
		}
		
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		format.setLenient(false);
		try{
			Date date = format.parse(lastDeliveryDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);
			delivery.setDate(new Timestamp(date.getTime()));
		}catch(ParseException e){
			invalidDate=true;
		}
		
		if(invalidDate==false){
			delivery.setHcp(loggedInMID);
			delivery.setPatient(patientID);
			delivery.setDeliveryMethod(lastDeliveryMethod);
			delivery.setDosPitocin(lastDosPitocin);
			delivery.setDosNitrous(lastDosNitrous);
			delivery.setDosPethidine(lastDosPethidine);
			delivery.setDosEpidural(lastDosEpidural);
			delivery.setDosMagnesium(lastDosMagnesium);
			delivery.setDosRHimmune(lastDosRHimmune);
			
			headerMessage = action.addDelv(delivery);
			
		} else {
			headerMessage = "Please input a valid date for the appointment.";
		}
		
		
%>
	<div align=center>
		<br />
		<br />
		<span class="iTrustMessage">The childbirth delivery record has been successfully added!</span>
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
<form action="addCHVDelivery.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<h2>Add a Childbirth Delivery</h2>
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
		<td class="subHeaderVertical">Delivery Date:</td>
		<td><input type="text" name="deliveryDate" value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>"/>
		<input type="button" value="Select Date" onclick="displayDatePicker('deliveryDate');" /></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Delivery Time:</td>
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
		<td class="subHeaderVertical">Delivery Method:</td>
		<td>
			<select name="deliveryMethod">
				<option value="vaginal_delivery">Vaginal Delivery</option>
				<option value="vaginal_delivery_vacuum_assist">Vaginal Delivery Vacuum Assist</option>
				<option value="vaginal_delivery_forceps_assist">Vaginal Delivery Forceps Assist</option>
				<option value="caesarean_section">Casesarean Section</option>
				<option value="miscarriage">Miscarriage</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Drug:</td>
		<td>
			<!--  <a href="/iTrust/auth/hcp-obstet/obstetricCHV_AddDrug.jsp" style="text-decoration: none;" class="w3-button w3-white w3-border"">Add Drug</a>-->
			<table class="fTable"> 
				<tr><td>Drug</td><td>Dosage</td></tr>
				<tr><td>Pitocin: </td><td><input name="dosPitocin" type="number" value="<%=lastDosPitocin %>" min="0" max="100" size="5"></td></tr>
				<tr><td>Nitrous oxide: </td><td><input name="dosNitrous" type="number" value="<%=lastDosNitrous %>" min="0" max="100" size="5""></td></tr>
				<tr><td>Pethidine: </td><td><input name="dosPethidine" type="number" value="<%=lastDosPethidine %>" min="0" max="100" size="5"></td></tr>
				<tr><td>Epidural anaesthesia: </td><td><input name="dosEpidural" type="number" value="<%=lastDosEpidural %>" min="0" max="100" size="5"></td></tr>
				<tr><td>Magnesium sulfate: </td><td><input name="dosMagnesium" type="number" value="<%=lastDosMagnesium %>" min="0" max="100" size="5"></td></tr>
				<tr><td>RH immune globulin: </td><td><input name="dosRHimmune" type="number" value="<%=lastDosRHimmune %>" min="0" max="100" size="5"></td></tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Baby:</td>
		<td>
			<!--
			<a href="/iTrust/auth/hcp-obstet/obstetricCHV_NewBaby.jsp" style="text-decoration: none;" class="w3-button w3-white w3-border"">New Baby</a>
			-->
			<div class="field_wrapper">
			    <div>
			        <a href="javascript:void(0);" class="add_button" title="Add field">Add Baby</a>
			    </div>
			</div>
		</td>
	</tr>
</table>
<br />

<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add">
<input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/obstetricCHV.jsp'; return false;" 
		value="View Records" style="font-size: 16pt; font-weight: bold;">
		
</form>
<br />
</div>

<%@include file="/footer.jsp" %>
