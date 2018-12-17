<%--
  Created by IntelliJ IDEA.
  User: Ajay Menon
  Date: 11/3/2018
  Time: 8:16 AM
--%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitsAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOVBeanLoader" %>

<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Edit Obstetrics Office Visit";
%>

<%@include file="/header.jsp" %>

<html>
<head>
    <title>Edit Obstetric Patient Records</title>
</head>
<body>
<%
    boolean hideForm = false;
    boolean error = false;
    String hidden = "";
    String aptParameter = "";
    String obOvid = request.getParameter("obovid");
    ObstetricOVBean currentRecord = null;
    String llpSelected = "";
    String ultraSoundSelected = "";
    String rhShotSelected = "";
    String pidString = (String) session.getAttribute("pid");
    String removedMsg = "";
    boolean isRemoved = false;
    Integer intCount = 0;

    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
        return;
    }

    String editReq = request.getParameter("editAppt");
    ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(prodDAO, pidString, loggedInMID);
    Map<Long, ObstetricOVBean> obovMap = (Map<Long, ObstetricOVBean>) session.getAttribute("obovMap");

    //##################################################################################
    /* on load */
    //##################################################################################

    if (obOvid != null && !obOvid.isEmpty()) {
        if (obovMap != null && !obovMap.isEmpty()) {
            currentRecord = obovMap.get(Long.parseLong(obOvid));
            if (editReq == null) {
                action.logObstetricOVTransaction("edit-view",obOvid);
            }
        } else {
            out.println("obov map is null");
            response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
        }
    } else {
        out.println("obovid is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
    }

    if (currentRecord ==null) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
    }
    String changeKey="changeCount"+currentRecord.getObVisitID();


    Date oldDate = new Date(currentRecord.getVisitDate().getTime());
    DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat tFormat = new SimpleDateFormat("hhmma");
    String hPart = tFormat.format(oldDate).substring(0,2);
    String mPart = tFormat.format(oldDate).substring(2,4);
    String aPart = tFormat.format(oldDate).substring(4);

    String lastSchedDate=dFormat.format(oldDate);
    String lastTime1=hPart;
    String lastTime2=mPart;
    String lastTime3=aPart;

    //##################################################################################
    /* on update */
    //##################################################################################
    try {
        if (request.getParameter("apt") != null) {
            aptParameter = request.getParameter("apt");
        }

        if (editReq != null) {
            if ("Change".equalsIgnoreCase(editReq)) {

                if (session.getAttribute(changeKey) !=null) {
                    intCount = ((Integer) session.getAttribute(changeKey)) + 1;
                    session.setAttribute(changeKey,intCount);
                } else {
                    intCount = 1;
                    session.setAttribute(changeKey,intCount);
                }
                ObstetricOVBean newFields = ObstetricOVBeanLoader.beanBuilder(request.getParameterMap());

                lastSchedDate = request.getParameter("schedDate");
                lastTime1 = request.getParameter("time1");
                lastTime2 = request.getParameter("time2");
                lastTime3 = request.getParameter("time3");
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);

                currentRecord.setVisitDate(new Timestamp(date.getTime()));

                currentRecord.setNumberDaysPregnant(newFields.getNumberDaysPregnant());
                currentRecord.setBloodPressure(request.getParameter("bloodPress"));
                currentRecord.setFhr(newFields.getFhr());
                currentRecord.setMultiplet(newFields.getMultiplet());
                currentRecord.setWeight(newFields.getWeight());

                String llpVal = request.getParameter("llpSelect");
                String ultraSoundVal = request.getParameter("ultraSoundSelect");
                System.out.println("llpVal :" + llpVal + " & ultra :" + ultraSoundVal);

                currentRecord.setLlp(request.getParameter("llpSelect")!=null?Byte.parseByte("1"):Byte.parseByte("0"));
                currentRecord.setUltraSound(request.getParameter("ultraSoundSelect")!=null?Byte.parseByte("1"):Byte.parseByte("0"));

                System.out.println(pidString);
                ObstetricOVBean persistedOOVBean = action.editOBOVPatientRecord(currentRecord);
                obovMap.put(Long.parseLong(obOvid), currentRecord); //set update after the DB update.

                if (persistedOOVBean!=null) {
                    Long obVisitId = persistedOOVBean.getObVisitID();
                    if (obVisitId != null && obVisitId > 0L) {
                        if (currentRecord.getUltraSound() == 1) {
                            response.sendRedirect("/iTrust/auth/hcp-obstet/addUltraSoundDetails.jsp?obVisitId=" + obVisitId);
                        } else {
                            response.sendRedirect("/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp?confirmEdit=success");
                        }
                    }
                }

            } else if ("Remove".equalsIgnoreCase(editReq)) {
                isRemoved = action.removeOBOVPatientRecord(currentRecord);
                if (isRemoved) {
                    removedMsg = "Obstetric Patient Records has been removed !";
                }
                obovMap.remove(Long.parseLong(obOvid));
                currentRecord = null;
                response.sendRedirect("/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp?confirmEdit=deleted");
            }
        }

    } catch (FormValidationException e){
%>
<div align=center>
    <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
</div>
<%
    }
    if (currentRecord!=null) {
        if (currentRecord.getLlp()>0) {
            llpSelected = "checked";
        }
        if (currentRecord.getUltraSound()>0) {
            ultraSoundSelected = "checked";
        }
        if (currentRecord.getRhShotTaken()>0) {
            rhShotSelected = "checked";
        }
    }
%>

<%
    if (currentRecord != null) {
        boolean selected = false;
%>
<%=removedMsg%>

<form id="mainForm" <%=hidden %> method="post" action="editObstetricOVRecord.jsp?obovid=<%=currentRecord.getObVisitID()%>&apt=<%=aptParameter%>">
    <div align="center">
        <h2>Appointment Info</h2>
        <table width="auto" class="fTable">
            <tr>
                <td><b>Patient ID :</b></td> <td><%= StringEscapeUtils.escapeHtml("" + (currentRecord.getPatientMID())) %></td>
            </tr>
            <tr>
                <td><b>OB/GYN HCP MID :</b> </td> <td><%= StringEscapeUtils.escapeHtml("" + (currentRecord.getoBhcpMID())) %></td>
            </tr>
            <tr>
                <td><b>Date:</b></td>
                <td>
                    <input type="text" name="schedDate" value="<%=lastSchedDate%>" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('schedDate',false,'MM/dd/yyyy hh:mm a');" />
                    <b>Time:</b>
                    <select name="time1">
                        <%
                            String hour = "";
                            for(int i = 1; i <= 12; i++) {
                                if(i < 10) hour = "0"+i;
                                else hour = i+"";
                                selected = hour.equals(lastTime1);
                        %>
                        <option <%=selected?"selected ":"" %>value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
                        <%
                            }
                        %>
                    </select>:
                    <select name="time2">
                        <%
                            String min = "";
                            for(int i = 0; i < 60; i+=5) {
                                if(i < 10) min = "0"+i;
                                else min = i+"";
                                selected = min.equals(lastTime2);
                        %>
                        <option <%=selected?"selected ":"" %>value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
                        <%
                            }
                            selected = "AM".equals(lastTime3);
                        %>
                    </select>
                    <select name="time3">
                        <option <%=selected?"selected ":"" %>value="AM">AM</option>
                        <option <%=selected?"":"selected " %>value="PM">PM</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><b>Number of Days Pregrant :</b></td> <td>
                <input type="text" name="numDaysPreg" value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getNumberDaysPregnant())) %>" />
            </td>
            </tr>
            <tr>
                <td><b>Weight :</b></td> <td>
                <input type="text" name="weight" value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getWeight())) %>" />
            </td>
            </tr>
            <tr>
                <td><b>Blood Pressure :</b></td> <td>
                <input type="text" name="bloodPress" value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getBloodPressure())) %>" />
            </td>
            </tr>
            <tr>
                <td><b>Fetal Heart Rate Baseline (FHR) :</b></td> <td>
                <input type="text" name="fhr" value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getFhr())) %>" />
            </td>
            </tr>
            <tr>
                <td><b>Multiple :</b></td> <td>
                <input type="text" name="multiple" value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getMultiplet())) %>" />
            </td>
            </tr>
            <tr>
                <td><b>Low Lying Placenta ? :</b></td> <td>
                <input type="checkbox" id="llpSelect" <%=llpSelected%> name="llpSelect"
                       value="<%= StringEscapeUtils.escapeHtml("" + (currentRecord.getLlp()))%>"/>
            </td>
            </tr>
            <tr>
                <td><b>Ultrasound Requested ? :</b></td> <td>
                <input type="checkbox" id="ultraSoundSelect" <%=ultraSoundSelected%> name="ultraSoundSelect"
                       value="<%=StringEscapeUtils.escapeHtml("" + (currentRecord.getUltraSound()))%>"/>
            </td>
            </tr>
            <tr>
                <td><b>RH Shots given :</b></td>
                <td>
                    <input type="checkbox" id="rhShot" <%=rhShotSelected%> name="rhShotSelect"
                           value="<%=StringEscapeUtils.escapeHtml("" + (currentRecord.getRhShotTaken()))%>"/>
                </td>
            </tr>
        </table>

        <br><br>

        <input type="hidden" id="editAppt" name="editAppt" value=""/>

        <input type="submit" value="Change" name="editApptButton" id="changeButton"
               onClick="document.getElementById('editAppt').value='Change'"/>

        <input type="submit" value="Remove" name="editApptButton" id="removeButton"
               onClick="document.getElementById('editAppt').value='Remove'"/>
        <input type="hidden" id="override" name="override" value="noignore"/>
    </div>

</form>
<%
    }
%>
<%@include file="/footer.jsp" %>
</body>
</html>
