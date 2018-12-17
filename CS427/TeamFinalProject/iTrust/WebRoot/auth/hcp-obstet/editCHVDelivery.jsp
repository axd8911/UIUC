<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.text.ParseException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
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
pageTitle = "iTrust - Edit Childbirth Delivery";

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

String recID ="";

String isVD = "";
String isVDVA = "";
String isVDFA = "";
String isCS = "";
String isM = "";

List<CHVBabyBean> babies = new ArrayList<CHVBabyBean>();

%>

<%@include file="/header.jsp" %>

<%

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");

CHVDeliveryAction action = new CHVDeliveryAction(prodDAO, loggedInMID.longValue());

recID = request.getParameter("recID");
if(recID != null && recID.length() > 0 && !formIsFilled) {
	CHVDeliveryBean original = action.getDelv(Integer.parseInt(recID));
	
	Date oldDate = new Date(original.getDate().getTime());
	DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat tFormat = new SimpleDateFormat("hhmma");
	String hPart = tFormat.format(oldDate).substring(0,2);
	String mPart = tFormat.format(oldDate).substring(2,4);
	String aPart = tFormat.format(oldDate).substring(4);
	
	lastDeliveryDate=dFormat.format(oldDate);
	lastTime1=hPart;
	lastTime2=mPart;
	lastTime3=aPart;
	lastDeliveryMethod=original.getDeliveryMethod();
	lastDosPitocin=original.getDosPitocin();
	lastDosNitrous=original.getDosNitrous();
	lastDosPethidine=original.getDosPethidine();
	lastDosEpidural=original.getDosEpidural();
	lastDosMagnesium=original.getDosMagnesium();
	lastDosRHimmune=original.getDosRHimmune();
	
	// for handling which delivery method should be selected in the drop down menu.
	isVD = (lastDeliveryMethod.equals("vaginal_delivery")) ? "selected" : "";
	isVDVA = (lastDeliveryMethod.equals("vaginal_delivery_vacuum_assist")) ? "selected" : "";
	isVDFA = (lastDeliveryMethod.equals("vaginal_delivery_forceps_assist")) ? "selected" : "";
	isCS = (lastDeliveryMethod.equals("caesarean_section")) ? "selected" : "";
	isM = (lastDeliveryMethod.equals("miscarriage")) ? "selected" : "";

	babies = action.getBabies(Long.parseLong(recID));
}

if (formIsFilled && recID != null && recID.length() > 0) {

	try {
		
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
		
		CHVDeliveryBean delivery = new CHVDeliveryBean();
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
			delivery.setDeliveryID(Long.parseLong(recID));
			
			if (request.getParameter("editDelv").equals("Change")) {
				headerMessage = action.editDelv(delivery);
%>
			<div align=center>
				<br />
				<br />
				<span class="iTrustMessage">The childbirth delivery has been successfully changed!</span>
			</div>
<% 
			} else if (request.getParameter("editDelv").equals("Remove")) {
				headerMessage = action.removeDelv(delivery);
%>
			<div align=center>
				<br />
				<br />
				<span class="iTrustMessage">The childbirth delivery has been successfully removed!</span>
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
<form action="editCHVDelivery.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<h2>Update a Childbirth Delivery</h2>
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
		<td><input type="text" name="deliveryDate" value="<%=lastDeliveryDate %>"/>
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
				<option value="vaginal_delivery" <%=isVD%>>Vaginal Delivery</option>
                <option value="vaginal_delivery_vacuum_assist" <%=isVDVA%>>Vaginal Delivery Vacuum Assist</option>
                <option value="vaginal_delivery_forceps_assist" <%=isVDFA%>>Vaginal Delivery Forceps Assist</option>
                <option value="caesarean_section" <%=isCS%>>Casesarean Section</option>
                <option value="miscarriage" <%=isM%>>Miscarriage</option>
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
			 <%
			 	if (babies != null && babies.size() > 0) {
			 		for (CHVBabyBean baby: babies) {		 			
			 			//out.println("Baby ID: " + baby.getBabyID() + " Patient ID: " + baby.getMid() + "<br>");
			 %>
			 			Baby ID: <a href="/iTrust/auth/hcp-obstet/obstetricCHV_Baby.jsp?recID=<%=baby.getBabyID()%>"><%=baby.getBabyID()%></a>, Patient ID:  <a href="/iTrust/auth/hcp-obstet/viewPatient.jsp?pid=<%=baby.getMid()%>"><%=baby.getMid()%></a><br>
			 <%
			 			
			 		}
			 	}
			 %>
			 <div class="field_wrapper">
			    <div>
			        <a href="javascript:void(0);" class="add_button" title="Add field">Add Baby</a>
			    </div>
			</div>
		</td>
	</tr>
</table>
<br />

<input type="hidden" name="recID" value="<%=recID %>"/>
<input type="hidden" id="editDelv" name="editDelv" value=""/>
<input type="submit" value="Change" name="editDelvtButton" id="changeButton" onClick="document.getElementById('editDelv').value='Change'" style="font-size: 16pt; font-weight: bold;" />
<input type="submit" value="Remove" name="editDelvButton" id="removeButton" onClick="document.getElementById('editDelv').value='Remove'" style="font-size: 16pt; font-weight: bold;" />
<input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/obstetricCHV.jsp'; return false;" 
		value="View Records" style="font-size: 16pt; font-weight: bold;">
		
</form>
<br />
</div>

<%@include file="/footer.jsp" %>
