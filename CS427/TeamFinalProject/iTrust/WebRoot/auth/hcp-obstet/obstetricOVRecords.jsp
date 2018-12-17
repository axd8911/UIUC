<%--
  Created by IntelliJ IDEA.
  User: nathan
  Date: 10/29/18
  Time: 11:55 AM
  To change this template use File | Settings | File Templates.

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
    pageTitle = "iTrust - Add/View Obstetrics Office Visit";
%>

<%@include file="/header.jsp" %>

<%
    /* Require a Patient ID first */
    String pidString = (String)session.getAttribute("pid");
    String isNonOBGynHCP = "";

    if (pidString == null || 1 > pidString.length() || "false".equals(request.getParameter("confirmAction"))) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
        return;
    }

    Long pidLong = Long.parseLong(pidString);
    String patientsFullName = prodDAO.getPatientDAO().getPatient(pidLong).getFullName();

    String confirmEdit = (String)request.getParameter("confirmEdit");
    String confirmAdd = (String)request.getParameter("confirmAdd");
    String confirmMsg = "";
    if("success".equalsIgnoreCase(confirmAdd)) {
        confirmMsg = "Obstetrics Office Visit successfully added.";
    }

    if("success".equalsIgnoreCase(confirmEdit)) {
        confirmMsg = "Obstetrics Office Visit successfully edited.";
    }

    if (prodDAO.getPersonnelDAO().getPersonnel(loggedInMID.longValue()).getSpecialty().equals("OB/GYN")){

        //TODO: integrate uc 93 to check if patient is currently an obstetrics patient
        boolean isObstetricsPatient = true; //replace "true" with uc 93 functionality

        if (isObstetricsPatient){
%>
            <div align=center>
                <span class="iTrustMessage"><%=confirmMsg%> </span>
            </div>
            <div align=center class="table-editable">
                <h2><%=patientsFullName%>'s Previous Obstetric Office Visits</h2>
<%
            ViewObstetricOfficeVisitsAction viewAction =  new ViewObstetricOfficeVisitsAction(prodDAO, pidString, loggedInMID);

            List<ObstetricOVBean> records = viewAction.getOBOVRecords();
            if(records!=null && !records.isEmpty()) {
                records.sort((o1,o2) -> o1.getVisitDate().compareTo(o2.getVisitDate()));
            }

            session.setAttribute("obovListRecords", records);
            session.setAttribute("obovMap", viewAction.getObstetricVisitMap());

            if (records.size() > 0) {
%>
                <span class="table-add float-right mb-3 mr-2"><a href="#!" class="text-success">
                    <i class="fa fa-plus fa-2x" aria-hidden="true"></i></a>
                </span>
                <table class="table table-bordered table-responsive-md table-striped text-center">
                    <tr>
                        <th>Visit ID</th>
                        <th>OB/GYN HCP ID</th>
                        <th>Patient ID</th>
                        <th>Appointment Date/Time</th>
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
                    <td><%= StringEscapeUtils.escapeHtml("" + ( r.getObVisitID() )) %></td>
                    <td><%= StringEscapeUtils.escapeHtml("" + ( r.getoBhcpMID() )) %></td>
                    <td><%= StringEscapeUtils.escapeHtml("" + ( r.getPatientMID() )) %></td>
                    <td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
                    <td><a href="editObstetricOVRecord.jsp?obovid=<%=r.getObVisitID() %>">View
                        /Edit</a> <% } %></td>
                    </tr>
<%
                    index++;
                }
%>
                </table>

        <%	} else { %>
                <div>
                    <i>No Obstetric Office Visits on Record.</i>
                </div>
                <%	} %>
                <br />
            </div>

<%
        } else{ // patient is not an obstetrics patient.
            isNonOBGynHCP = "disabled='true'";
%>

            <div align=center>
                <span class="iTrustMessage"><%=patientsFullName%> is not a current obstetrics patient.  Please select a different patient.</span>
            </div>

<%
        }
%>
<br />
    <div align=center>
        <input id="addObstetricOVButton" <%=isNonOBGynHCP%> type="button" onclick="window.location.href='/iTrust/auth/hcp-obstet/addOVRecords.jsp'; return false;"
           value="Add Office Visit Record" style="font-size: 150%; font-weight: bold;" />
    </div>
<hr><hr>
<br>
<iframe src="https://calendar.google.com/calendar/b/2/embed?height=400&amp;wkst=2&amp;hl=en&amp;bgcolor=%2333ccff&amp;src=itrustcommon%40gmail.com&amp;color=%231B887A&amp;ctz=America%2FChicago" style="border:solid 1px #777" width="1200" height="400" frameborder="0" scrolling="no"></iframe>
<%@include file="/footer.jsp" %>

