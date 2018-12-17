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
    pageTitle = "iTrust - Edit Obstetric Record";
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

    //must have recId set
    if (request.getParameter("recID") == null){
        response.sendRedirect("initObstetPatient.jsp");
    }

    String recID = request.getParameter("recID");
    String pidString = (String) session.getAttribute("pid");
    ObstetRecBean ob = action.getObstetRec(Integer.parseInt(recID), Long.parseLong(pidString), formIsFilled);


    if (formIsFilled ) {

        try{

            /* Check that user provided LMP date is not after today */
            String lmpDateString = request.getParameter("lmpDate");

            if (!sdf.format(sdf.parse(lmpDateString)).equals(lmpDateString)){
				throw new NullPointerException();
			}

            Date lmpDate = sdf.parse(lmpDateString, new ParsePosition(0));

            long dateCutoff = 24192000000L; // 280 days

            if (lmpDate == null){
                myerrormsg = "Please enter a valid date for Last Menstrual Period (LMP) in format of: YYYY-MM-DD";
            } else if (lmpDate.after(c1.getTime())){

                myerrormsg = "LMP date cannot be after today.";

            } else {


                    //ObstetRecBean ob = new BeanBuilder<ObstetRecBean>().build(request.getParameterMap(), new ObstetRecBean());

                    ob.setLmpDate(lmpDate);


                    /* Add a new obstetric record */
                    action.updateObstetRec(ob);

%>
<div align=center>
    <br />
    <br />
    <span class="iTrustMessage">The patient's obstetric record has been successfully changed!</span>
</div>
<%

            }
        } catch(Exception n){

            myerrormsg = "Please enter a valid date for Last Menstrual Period (LMP) in format of: YYYY-MM-DD";
        }
    }
%>
<div align=center>
    <form action="editObstetRec.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + request.getParameter("recID"))%>" method="post">
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
                <td><%= StringEscapeUtils.escapeHtml("" + sdf.format(ob.getInitDate())) %></td>
            </tr>
            <tr>
                <td class="subHeaderVertical">LMP:</td>
                <td><input type="text" name="lmpDate" value="<%=StringEscapeUtils.escapeHtml("" + sdf.format(ob.getLmpDate()))%>"></td>
            </tr>
        </table>
        <br />

        <input type="hidden" id="editObstetRec" name="editObstetRec" value=""/>
        <input id="editObstetRecButton" type="submit" style="font-size: 16pt; font-weight: bold;" value="Change" onClick="document.getElementById('editObstetRec').value='Change'">
        <input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp'; return false;"
               value="View Records" style="font-size: 16pt; font-weight: bold;">

    </form>

    <br />
</div>
<%@include file="/footer.jsp" %>
