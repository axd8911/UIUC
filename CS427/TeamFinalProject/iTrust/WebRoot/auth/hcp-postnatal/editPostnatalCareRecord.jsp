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
    pageTitle = "iTrust - Edit Postnatal Care Record";
%>

<%@include file="/header.jsp" %>

<%
    String myerrormsg = " ";
	PostnatalAction action = new PostnatalAction(prodDAO, loggedInMID.longValue());

	//must have recId set
    if (request.getParameter("recID") == null){
        response.sendRedirect("initPostnatalCareRecord.jsp");
    }
	
    /* check if HCP has OBGYN specialty */
    boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
    if (!isOBGYN) {
        response.sendError(403);
    }

    /* Get current date as patient initialization date */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c1 = Calendar.getInstance();

    boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");

    String recID = request.getParameter("recID");
    PostnatalCareRecordBean bean = action.getPostnatalCareRecord(Integer.parseInt(recID));
    
    final class IncorrectInputDateException extends Exception { 
        public IncorrectInputDateException() {
            super();
        }
    }

    if (formIsFilled ) {
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
            
            /* Add a new obstetric record */
    		bean = PostnatalCareBeanLoader.beanBuilder(request.getParameterMap());
    		bean.setPostnatalCareRecordId(Integer.valueOf(recID));
    		String pidString = (String) session.getAttribute("pid");
    		bean.setMid(Long.parseLong(pidString));
    		bean.setRecordDate(c1.getTime());
			action.updatePostnatalCareRecord(bean);
    		
%>
<div align=center>
    <br />
    <br />
    <span class="iTrustMessage">The patient's Postnatal Care Record has been successfully updated!</span>
</div>
<%
            
        } catch(NullPointerException n){
        	myerrormsg = "Please enter correct date format: yyyy-nn-dd!";
            n.printStackTrace();
        } catch(IncorrectInputDateException e){
        	e.printStackTrace();
        }
    } 
	    
    String isVD = (bean.getDeliveryType().equals("vaginal_delivery")) ? "selected" : "";
    String isVDVA = (bean.getDeliveryType().equals("vaginal_delivery_vacuum_assist")) ? "selected" : "";
    String isVDFA = (bean.getDeliveryType().equals("vaginal_delivery_forceps_assist")) ? "selected" : "";
    String isCS = (bean.getDeliveryType().equals("caesarean_section")) ? "selected" : "";
    String isM = (bean.getDeliveryType().equals("miscarriage")) ? "selected" : "";
%>
<div align=center>
    <form action="editPostnatalCareRecord.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + request.getParameter("recID"))%>" method="post">
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
				<td><input type="text" name="record_date" value="<%= StringEscapeUtils.escapeHtml("" + sdf.format(bean.getRecordDate())) %>"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Childbirth Date:</td>
				<td><input type="text" name="childbirth_date" value="<%= StringEscapeUtils.escapeHtml("" + sdf.format(bean.getChildbirthDate())) %>"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Delivery Type:</td>
				<td>
					<select name="delivery_type">
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
				<td><input type="text" name="comment" value="<%= StringEscapeUtils.escapeHtml("" + bean.getAdditionalComment()) %>"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Release Date:</td>
				
				<%             
					if (bean.getReleaseDate() != null){
				%>
				<td><input type="text" name="release_date" value="<%= StringEscapeUtils.escapeHtml("" + sdf.format(bean.getReleaseDate())) %>"></td>
				<% } else { %>
				<td><input type="text" name="release_date" value="<%= StringEscapeUtils.escapeHtml("") %>"></td>
				<% } %>
			</tr>
        </table>
        <br />

        <input type="hidden" id="editPostnatalCareRecord" name="editPostnatalCareRecord" value=""/>
        <input id="editButton" type="submit" style="font-size: 16pt; font-weight: bold;" value="Update" onClick="document.getElementById('editPostnatalCareRecord').value='Update'">
        <input type="submit" onclick="window.location.href='/iTrust/auth/hcp-postnatal/initPostnatalCare.jsp'; return false;"
               value="View Records" style="font-size: 16pt; font-weight: bold;">

    </form>

    <br />
</div>
<%@include file="/footer.jsp" %>
