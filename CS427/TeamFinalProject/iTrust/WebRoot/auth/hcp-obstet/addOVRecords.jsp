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
    String status = request.getParameter("status");
    String pidString = (String) session.getAttribute("pid");
    boolean selected = false;

    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
        return;
    }

    ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(prodDAO, pidString, loggedInMID);

    Date currentDate = new Date();
    DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat tFormat = new SimpleDateFormat("hhmma");
    String hPart = tFormat.format(currentDate).substring(0,2);
    String mPart = tFormat.format(currentDate).substring(2,4);
    String aPart = tFormat.format(currentDate).substring(4);

    String lastSchedDate=dFormat.format(currentDate);
    String lastTime1=hPart;
    String lastTime2=mPart;
    String lastTime3=aPart;

    try {
        if ("newRecord".equalsIgnoreCase(status)) {

            String rhShotSelected = request.getParameter("rhShot");
            action.checkIfRhShotRequired(pidString,rhShotSelected);

            hideForm = true;


            String patientId = request.getParameter("patientId");
            String loggedInMid = request.getParameter("loggedInMid");

            lastSchedDate = request.getParameter("schedDate");
            lastTime1 = request.getParameter("time1");
            lastTime2 = request.getParameter("time2");
            lastTime3 = request.getParameter("time3");
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);

            String numDaysPreg = request.getParameter("numDaysPreg");
            String bloodPress = request.getParameter("bloodPress");
            String fhr = request.getParameter("fhr");
            String multiple = request.getParameter("multiple");
            String llpSelect = request.getParameter("llpSelect");
            String ultraSoundSelect = request.getParameter("ultraSoundSelect");
            String rhShot = request.getParameter("rhShot");
            String weight = request.getParameter("weight");

            //action.checkForFormValidity(date,numDaysPreg,bloodPress,fhr,multiple); //moved to validator bean.

            ObstetricOVBean ovBean = ObstetricOVBeanLoader.beanBuilder(request.getParameterMap());
            ovBean.setoBhcpMID(loggedInMid!=null?Long.parseLong(loggedInMid):-1L);
            ovBean.setPatientMID(patientId!=null?Long.parseLong(patientId):-1);
            ovBean.setVisitDate(date!=null?new Timestamp(date.getTime()):null);

            //NATHAN: below removed as beanloader  above now takes care of them (along with their error handling)
            ovBean.setBloodPressure(bloodPress);
//            ovBean.setNumberDaysPregnant(numDaysPreg!=null && !numDaysPreg.isEmpty()?Short.parseShort(numDaysPreg):-1);
//            ovBean.setFhr(fhr!=null && !fhr.isEmpty()?Short.parseShort(fhr):-1);
//            ovBean.setMultiplet(multiple!=null && !multiple.isEmpty()?Byte.parseByte(multiple):-1);
//            ovBean.setWeight(weight!=null && !weight.isEmpty()?Short.parseShort(weight):-1);

            byte trueVal = 1;
            byte falVal = 0;

            ovBean.setLlp(llpSelect!=null?trueVal:falVal);
            ovBean.setUltraSound(ultraSoundSelect!=null?trueVal:falVal);
            ovBean.setRhShotTaken(rhShot!=null?trueVal:falVal);

            //patient details update.
            ObstetricOVBean persistedOOVBean = action.addOBOVPatientRecord(ovBean);
            if (persistedOOVBean!=null) {
                Long obVisitId = persistedOOVBean.getObVisitID();
                if (obVisitId != null && obVisitId > 0L) {
                    if (ovBean.getUltraSound() == 1) {
                        response.sendRedirect("/iTrust/auth/hcp-obstet/addUltraSoundDetails.jsp?obVisitId=" + obVisitId);
                    } else {
                        response.sendRedirect("/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp?confirmAdd=success");
                    }
                }
            } else {
                //TODO : throw exception
            }
        }
    } catch (FormValidationException e){
        %>
        <div align=center>
            <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
        </div>
        <%
    }
    %>
<div align=center>
    <form id="mainForm" method="post" action="addOVRecords.jsp?status=newRecord">
        <div>
            <h2>Appointment Details</h2>

            <table width="auto" class="fTable">
                <tr>
                    <td><b>Patient ID :</b></td> <td><input type="text" name="patientId" value="<%=pidString%>" /></td>
                </tr>
                <tr>
                    <td><b>OB/GYN HCP MID :</b> </td> <td><input type="text" name="loggedInMid" value="<%=loggedInMID%>" /></td>
                </tr>
                <tr>
                    <td><b>Date:</b></td> <td>
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
                    <input type="text" name="numDaysPreg" />
                </td>
                </tr>
                <tr>
                    <td><b>Weight :</b></td> <td>
                    <input type="text" name="weight" />
                </td>
                </tr>
                <tr>
                    <td><b>Blood Pressure :</b></td> <td>
                    <input type="text" name="bloodPress" />
                </td>
                </tr>
                <tr>
                    <td><b>Fetal Heart Rate Baseline (FHR) :</b></td> <td>
                    <input type="text" name="fhr" />
                </td>
                </tr>
                <tr>
                    <td><b>Multiple :</b></td> <td>
                    <input type="text" name="multiple" />
                </td>
                </tr>
                <tr>
                    <td><b>Low Lying Placenta :</b></td> <td>
                    <input type="checkbox" id="llpSelect" name="llpSelect"/>
                </td>
                </tr>
                <tr>
                    <td><b>Ultrasound :</b></td> <td>
                    <input type="checkbox" id="ultraSoundSelect" name="ultraSoundSelect" />
                </td>
                <tr>
                    <td><b>RH Shots given :</b></td> <td>
                    <input type="checkbox" id="rhShot" name="rhShot" />
                </td>
                </tr>
            </table>

            <br><br>

            <input type="submit" value="Create Appointment" name="createApptButton" id="createApptButton"/>
        </div>
    </form>
    <br />
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
