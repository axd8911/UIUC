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
    pageTitle = "iTrust - Edit Prior Pregnancy";
%>

<%@include file="/header.jsp" %>

<%

    ObstetAction action = new ObstetAction(prodDAO, loggedInMID.longValue());

    /* check if HCP has OBGYN specialty */
    boolean isOBGYN = action.isOBGYN(loggedInMID.longValue());
    if (!isOBGYN) {
        response.sendError(403);
    }

    //must have recId set
    if (request.getParameter("recID") == null){
        response.sendRedirect("initObstetPatient.jsp");
    }

    boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
    String recID = request.getParameter("recID");
    String pidString = (String) session.getAttribute("pid");
    ObstetPriorPregBean ob = action.getPriorPreg(Integer.parseInt(recID), Long.parseLong(pidString), formIsFilled);


    if (formIsFilled) {

        try{
            //ObstetPriorPregBean ob = new BeanBuilder<ObstetPriorPregBean>().build(request.getParameterMap(), new ObstetPriorPregBean());

            ob = PriorPregBeanLoader.beanBuilder(request.getParameterMap());
            ob.setPriorPregRecID(Integer.parseInt(recID));
            ob.setMid(Long.parseLong(pidString));
            /*update prior pregnancy record */
            action.updatePriorPreg(ob);

%>
<div align=center>
    <br />
    <br />
    <span class="iTrustMessage">The patient's prior pregnancy record has been successfully updated!</span>
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

    // for handling which delivery method should be selected in the drop down menu.
    String isVD = (ob.getDeliveryType().equals("vaginal_delivery")) ? "selected" : "";
    String isVDVA = (ob.getDeliveryType().equals("vaginal_delivery_vacuum_assist")) ? "selected" : "";
    String isVDFA = (ob.getDeliveryType().equals("vaginal_delivery_forceps_assist")) ? "selected" : "";
    String isCS = (ob.getDeliveryType().equals("caesarean_section")) ? "selected" : "";
    String isM = (ob.getDeliveryType().equals("miscarriage")) ? "selected" : "";
%>

<div align=center>
    <form action="editPriorPreg.jsp?recID=<%= StringEscapeUtils.escapeHtml("" + request.getParameter("recID"))%>" method="post">
        <input type="hidden" name="formIsFilled" value="true"> <br />
        <br />
        <p>Please edit the patient's prior pregnancy information.</p>
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
                <td><input type="text" name="conceptionYear" value="<%=StringEscapeUtils.escapeHtml("" + ob.getConceptionYear())%>"></td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Pregnant Weeks:</td>
                <td><input type="text" name="pregnant_weeks" size="8" value="<%=StringEscapeUtils.escapeHtml("" + ob.getPregWeeks())%>"> weeks,
                    <input type="text" name="pregnant_days" size="8"  value="<%=StringEscapeUtils.escapeHtml("" + ob.getPregDays())%>"> days</td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Hours in Labor:</td>
                <td><input type="text" name="laborHours" value="<%=StringEscapeUtils.escapeHtml("" + ob.getLaborHours())%>"></td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Weight Gained:</td>
                <td><input type="text" name="weightGain" value="<%=StringEscapeUtils.escapeHtml("" + ob.getWeightGain())%>"></td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Delivery Type:</td>
                <td>
                    <select name="deliveryType">
                        <option value="vaginal_delivery" <%=isVD%>>Vaginal Delivery</option>
                        <option value="vaginal_delivery_vacuum_assist" <%=isVDVA%>>Vaginal Delivery Vacuum Assist</option>
                        <option value="vaginal_delivery_forceps_assist" <%=isVDFA%>>Vaginal Delivery Forceps Assist</option>
                        <option value="caesarean_section" <%=isCS%>>Casesarean Section</option>
                        <option value="miscarriage" <%=isM%>>Miscarriage</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Multiplet:</td>
                <td><input type="text" name="multiplet" value="<%=StringEscapeUtils.escapeHtml("" + ob.getMultiplet())%>"></td>
            </tr>
        </table>
        <br />

        <input type="submit" style="font-size: 16pt; font-weight: bold;" value="Change">
        <input type="submit" onclick="window.location.href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp'; return false;"
               value="View Records" style="font-size: 16pt; font-weight: bold;">

    </form>
    <br />
</div>

<%@include file="/footer.jsp" %>
