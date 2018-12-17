<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.PostnatalAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.ParsePosition"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.loaders.PostnatalCareBeanLoader" %>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Add Postnatal Care Record";
%>

<%@include file="/header.jsp" %>

<%
	String myerrormsg = " ";
	PostnatalAction action = new PostnatalAction(prodDAO, loggedInMID.longValue());

	/* check if HCP has OBGYN specialty */
	boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
	if (!isOBGYN) {
		response.sendError(403);
	}

	/* Get current date as patient initialization date */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c1 = Calendar.getInstance();
	
    final class IncorrectInputDateException extends Exception { 
        public IncorrectInputDateException() {
            super();
        }
    }

	boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
	if (formIsFilled) {

		PostnatalCareRecordBean bean = null;
		try{
			String recordDateString = request.getParameter("record_date");
            Date recordDate = sdf.parse(recordDateString, new ParsePosition(0));
            if (recordDateString == null || !action.isValidDateInput(recordDateString)) {
            	myerrormsg = "Please enter correct record date in format: yyyy-nn-dd!";
            	throw new IncorrectInputDateException();
            }

            String childbirthDateString = request.getParameter("childbirth_date");
            Date childbirthDate = sdf.parse(childbirthDateString, new ParsePosition(0));
            if (childbirthDate == null || !action.isValidDateInput(childbirthDateString)) {
            	myerrormsg = "Please enter correct childbirth date in format: yyyy-nn-dd!";
            	throw new IncorrectInputDateException();
            }
            
            String releaseDateString = request.getParameter("release_date");
            Date releaseDate = sdf.parse(releaseDateString, new ParsePosition(0));
            if (releaseDate != null || !action.isValidDateInput(releaseDateString)) {
            	if (c1.getTime().before(releaseDate) || releaseDate.before(childbirthDate)){
                    myerrormsg = "Release date cannot be before childbirth date or in the future.";
                    throw new IncorrectInputDateException();
                }
            } else {     	
            	if (releaseDateString != null && releaseDateString.trim() != "") {
            		myerrormsg = "Please enter correct release date in format: yyyy-nn-dd!";
            		throw new IncorrectInputDateException();
            	}
            }
			
        	bean = PostnatalCareBeanLoader.beanBuilder(request.getParameterMap());
			String pidString = (String) session.getAttribute("pid");
			bean.setMid(Long.parseLong(pidString));
			bean.setRecordDate(c1.getTime());
			action.addPostnatalCareRecord(bean);
%>
<div align=center>
	<br />
	<br />
	<span class="iTrustMessage">The patient's postnatal care record has been successfully added!</span>
</div>
<%
		} catch(FormValidationException e){
			myerrormsg = "Please enter correct date format: yyyy-nn-dd!";
            e.printStackTrace();
        } catch(IncorrectInputDateException e){
        	e.printStackTrace();
        }
	}
%>
<div align=center>
	<form action="addPostnatalCareRecord.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true"> <br />
		<br />
		<span class="iTrustError"><%= myerrormsg %></span>
		<table class="fTable">
			<tr>
				<th colspan=2>Postnatal Care Record</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Patient MID:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("pid")) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Patient Name:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + session.getAttribute("patient_name")) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Record Date:</td>
				<td><input type="text" name="record_date"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Childbirth Date:</td>
				<td><input type="text" name="childbirth_date"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td>
					<select name="delivery_type">
						<option value="vaginal_delivery">Vaginal Delivery</option>
						<option value="vaginal_delivery_vacuum_assist">Vaginal Delivery Vacuum Assist</option>
						<option value="vaginal_delivery_forceps_assist">Vaginal Delivery Forceps Assist</option>
						<option value="caesarean_section">Casesarean Section</option>
						<option value="miscarriage">Miscarriage</option>
					</select>
				</td>
			</tr>
						<tr>
				<td class="subHeaderVertical">Comment:</td>
				<td><input type="text" name="comment"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Release Date:</td>
				<td><input type="text" name="release_date"></td>
			</tr>
			
		</table>
		<br />

		<input id="add" type="submit" style="font-size: 16pt; font-weight: bold;" value="Add">
		
		<!--  >a href="/iTrust/auth/hcp-postnatal/addPostnatalCareRecord.jsp">
		 	<button id=addPostnatalCareRecordButton>
		 	Add New Record
		 	</button>
		</a -->
		<input type="button" onclick="window.location.href='/iTrust/auth/hcp-postnatal/initPostnatalCare.jsp'; return false;"
			   value="Cancel" style="font-size: 16pt; font-weight: bold;">

	</form>

	<br />
</div>
<%@include file="/footer.jsp" %>
