<%--
  Created by IntelliJ IDEA.
  User: nathan
  Date: 10/29/18
  Time: 11:55 AM
  To change this template use File | Settings | File Templates.

  __________________________________________________
  FIXME: Rename this file to "ObstetOVRecords.jsp"
  __________________________________________________
  
  Using the calendar object from here: https://www.ama3.com/anytime/#instructions
  Graphics from: https://icons8.com/icon/3344/calendar and placed into WebRoot/image/icons/icons8-calendar-50.png

--%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Add Obstetrics Office Visit";
%>

<%@include file="/header.jsp" %>

<%
    /* Require a Patient ID first */
    String pidString = (String)session.getAttribute("pid");
    
    if (pidString == null || 1 > pidString.length() || "false".equals(request.getParameter("confirmAction"))) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/addObstetOV.jsp");
        return;
    }	// End of the check for a pid
    Long pidLong = Long.parseLong(pidString);
    String patientsFullName = prodDAO.getPatientDAO().getPatient(pidLong).getFullName();
    if (prodDAO.getPersonnelDAO().getPersonnel(loggedInMID.longValue()).getSpecialty().equals("OB/GYN")){
        boolean isObstetricsPatient = true; //replace "true" with uc 93 functionality
        if (isObstetricsPatient){
%>

            <div align=center>
                <h2><%=patientsFullName%>'s Previous Obstetric Office Visits</h2>
<%
            ViewObstetricOfficeVisitsAction viewAction =  new ViewObstetricOfficeVisitsAction(prodDAO, pidString);
            // TODO: implement OBOVBean.java
            List<ObstetricOVBean> records = viewAction.getOBOVRecords();
            session.setAttribute("records", records);
            if (records.size() > 0) {
%>
                <table class="fTable">
                    <tr>
                        <th>Appointment Date and Time</th>
                        <th>Change</th>
                    </tr>
<%

                int index = 0;
                for(ObstetricOVBean r : records) {

                    Date d = new Date(r.getVisitDate().getTime());

                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

                    String row = "<tr";
%>
                    <%=row+" "+((index%2 == 1)?"class=\"alt\"":"")+">"%>
                    <td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
                    <td><a href="editObstetOV.jsp?obovid=<%=r.getObVisitID() %>">View/Edit</a> <% } //TODO: implement editObstetOV.jsp%></td>
                    </tr>
<%
                    index++;
            	}	// End of for loop going through the records.
%>
            	</table>
            	<%
        	} else {	// else for the if check for record size
        		%>
                <div>
                    <i>No Obstetric Office Visits on Record.</i>
                </div>
                <p>
                <img src="WebRoot/image/icons/icons8-calendar-50.png" alt="Flowers in Chania">
                <form action="action_page.php">
                <table>
                <tr>
                	<th>Item</th>
                	<th>Entry</th>
                </tr>
                <tr>
                	<td>Date</td>
                	<td><input type="text" id="appointmentTimeDate" size="20" value="Needs some recommended" />
						<!--  button id="calendar"><img src="WebRoot/image/icons/icons8-calendar-50.png" alt="cIcon"/></button -->
						<!--  FIXME: Calendar function for button needs to be added -->
                	    </td>
                </tr>
                <tr>
                	<td>Weight</td>
                	<td><input type="text" name="weight" id="weight"> lbs</td>
                </tr>
                <tr>
                	<td>Blood Pressure</td>
                	<td><input type="text" name="bloodPressure" id="bloodPressure"> mm Hg</td>
                </tr>
                <tr>
                	<td>FHR</td>
                	<td><input type="text" name="fhr" id="fhr"> bpm</td>
                </tr>
                <tr>              
					<td>Select Multiple</td>
					<td><input type="checkbox" name="unknown" id="unknown"> 1</td>
				</tr>
				<tr>
					<td>Select Low Lying Placenta</td>
					<td><input type="checkbox" name="lyingLowPlacenta" id="lyingLowPlacenta"></td>
				</tr>
                </table>
                <input type="submit" value="Submit" id="submitButton">
                </form>
                
                <%
			} // End of the else condition for the if check for record size
        } else { 
        	// patient is not an obstetrics patient.
        	%>
            <div align=center>
                <span class="iTrustMessage"><%=patientsFullName%> is not a current obstetrics patient.  Please select a different patient.</span>
            </div>
        	<%
        } // End of the if check for a obstetrics patient
    }  // End of the if check for the speciality for OB GYN
%>
<%@include file="/footer.jsp" %>
