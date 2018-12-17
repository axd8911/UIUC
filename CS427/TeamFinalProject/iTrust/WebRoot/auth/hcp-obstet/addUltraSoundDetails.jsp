<%--
  Created by IntelliJ IDEA.
  User: Ajay Menon
  Date: 11/3/2018
  Time: 8:16 AM
--%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitsAction" %>
<%@page import="edu.ncsu.csc.itrust.action.AddUltraSoundDetails" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.loaders.UltraSoundBeanLoader" %>

<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.*" %>
<%@page import="java.util.regex.*" %>
<%@page import="java.text.*" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import = "javax.servlet.http.*" %>
<%@ page import = "org.apache.commons.fileupload.*" %>
<%@ page import = "org.apache.commons.fileupload.disk.*" %>
<%@ page import = "org.apache.commons.fileupload.servlet.*" %>
<%@ page import = "org.apache.commons.io.output.*" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Add Patient Ultrasound Records";
%>

<%@include file="/header.jsp" %>

<html>
<head>
    <title>Add Patient Ultrasound Records</title>
</head>
<body>
<%

    boolean hideForm = false;
    boolean error = false;
    String status = request.getParameter("status");
    String pidString = (String) session.getAttribute("pid");
    boolean selected = false;
    String imgVal = "";

    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstet/obstetricOVRecords.jsp");
        return;
    }

    AddUltraSoundDetails ultraSoundAction = new AddUltraSoundDetails(prodDAO, pidString, loggedInMID);

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

    String patientId = (String)request.getParameter("patientId");
    String loggedInMid = (String)request.getParameter("loggedInMid");
    String officeVisitId = (String)request.getParameter("obVisitId");

    UltraSoundBean ultraSoundBean = null;
    if (pidString !=null && !pidString.isEmpty() && officeVisitId!=null &&
            !officeVisitId.isEmpty()) {
        ultraSoundBean = ultraSoundAction.getPatientExistingUltraSoundRecord(Long.parseLong(pidString),Long.parseLong(officeVisitId));
    }
    if (ultraSoundBean==null) {
        ultraSoundBean = new UltraSoundBean();
    }

    ultraSoundBean.setPatientMid(pidString!=null?Long.parseLong(pidString):0L);
    ultraSoundBean.setObstetricOfficeVisitId(officeVisitId!=null?Long.parseLong(officeVisitId):0L);

    String confirm = "";

    String crownRumpLength = request.getParameter("crownRumpLength");
    String biparietalDiameter = request.getParameter("biparietalDiameter");
    String headCircumference = request.getParameter("headCircumference");
    String femurLength = request.getParameter("femurLength");
    String occipitofrontalDiameter = request.getParameter("occipitofrontalDiameter");
    String abdominalCircumference = request.getParameter("abdominalCircumference");
    String humerusLength = request.getParameter("humerusLength");
    String estimatedFetalWeight = request.getParameter("estimatedFetalWeight");
    String imageLocation = "";

    try {
        if (request.getContentType() !=null
                && request.getContentType().indexOf("multipart/form-data") >= 0) {

            ultraSoundBean.setPatientMid(pidString!=null?Long.parseLong(pidString):0L);
            ultraSoundBean.setObstetricOfficeVisitId(officeVisitId!=null?Long.parseLong(officeVisitId):0L);

            File file = null;
            String fileLocation = null;
            int maxFileSize = 5000 * 1024;
            int maxMemSize = 5000 * 1024;
            ServletContext context = pageContext.getServletContext();
            String filePath = context.getInitParameter("file-upload");
            String userHomePath = System.getProperty("user.home");
            String tmpPath = userHomePath+filePath;
            try {
        	      File f = new File (tmpPath);
        	      if (f!=null && f.exists() && f.isDirectory()) {
        		        filePath = tmpPath; 
                } else {
                    filePath = userHomePath;
                }
            } catch (Exception io) {
        	     filePath = userHomePath;
            }
            
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(maxMemSize);
            factory.setRepository(new File("c:\\temp"));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax( maxFileSize );

            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () ) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // Write the file
                    if( fileName.lastIndexOf("\\") >= 0 ) {
                        fileLocation = filePath + "/" + fileName.substring(fileName.lastIndexOf("\\"));
                        file = new File(fileLocation) ;
                    } else {
                        fileLocation = filePath + "/" + fileName.substring(fileName.lastIndexOf("\\")+1);
                        file = new File(fileLocation) ;
                    }
                    fi.write( file ) ;
//                    out.println("Uploaded Filename: " + filePath +
//                            fileName + "<br>");
                }
            }
            ultraSoundBean.setImage(file);
            ultraSoundBean.setImageLocation(fileLocation);
            ultraSoundBean = ultraSoundAction.addOrUpdateUltraSoundRecord(ultraSoundBean);
            confirm = "Image added / updated successfully. Path : "+file.getCanonicalFile();
            imgVal = file.getName();

        } else if ("newRecord".equalsIgnoreCase(status)) {

            lastSchedDate = request.getParameter("createDate");
            lastTime1 = request.getParameter("time1");
            lastTime2 = request.getParameter("time2");
            lastTime3 = request.getParameter("time3");
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);

            crownRumpLength = request.getParameter("crownRumpLength");
            biparietalDiameter = request.getParameter("biparietalDiameter");
            headCircumference = request.getParameter("headCircumference");
            femurLength = request.getParameter("femurLength");
            occipitofrontalDiameter = request.getParameter("occipitofrontalDiameter");
            abdominalCircumference = request.getParameter("abdominalCircumference");
            humerusLength = request.getParameter("humerusLength");
            estimatedFetalWeight = request.getParameter("estimatedFetalWeight");

            ultraSoundBean = UltraSoundBeanLoader.beanBuilder(request.getParameterMap());
            ultraSoundBean.setPatientMid(pidString!=null?Long.parseLong(pidString):0L);
            ultraSoundBean.setObstetricOfficeVisitId(officeVisitId!=null?Long.parseLong(officeVisitId):0L);
            ultraSoundBean.setCreateDate(date!=null?new Timestamp(date.getTime()):null);

//            ultraSoundBean.setCrownRumpLength(Double.valueOf(crownRumpLength));
//            ultraSoundBean.setBiparietalDiameter(Double.valueOf(biparietalDiameter));
//            ultraSoundBean.setHeadCircumference(Double.valueOf(headCircumference));
//            ultraSoundBean.setFemurLength(Double.valueOf(femurLength));
//            ultraSoundBean.setOccipitofrontalDiameter(Double.valueOf(occipitofrontalDiameter));
//            ultraSoundBean.setAbdominalCircumference(Double.valueOf(abdominalCircumference));
//            ultraSoundBean.setHumerusLength(Double.valueOf(humerusLength));
//            ultraSoundBean.setEstimatedFetalWeight(Double.valueOf(estimatedFetalWeight));

            ultraSoundBean = ultraSoundAction.addOrUpdateUltraSoundRecord(ultraSoundBean);
            confirm = "Record added / updated successfully";
        }

        if (pidString !=null && !pidString.isEmpty() && officeVisitId!=null &&
                !officeVisitId.isEmpty()) {
            ultraSoundBean = ultraSoundAction.getPatientExistingUltraSoundRecord(Long.parseLong(pidString),Long.parseLong(officeVisitId));
        }

        if (ultraSoundBean !=null) {
            String ultraSoundRecordId = String.valueOf(ultraSoundBean.getUltraSoundRecordId());
            String createDate = String.valueOf(ultraSoundBean.getCreateDate());
            String obstetricOfficeVisitId =
                    String.valueOf(ultraSoundBean.getObstetricOfficeVisitId());

            imageLocation = ultraSoundBean.getImageLocation();
            if (imageLocation!=null && !imageLocation.isEmpty()) {
                String []tokens =  imageLocation.split(Pattern.quote("\\"));
                if (tokens!=null && tokens.length >0) {
                    imgVal = tokens[tokens.length-1];
                }
            }
            crownRumpLength = String.valueOf(ultraSoundBean.getCrownRumpLength());
            biparietalDiameter = String.valueOf(ultraSoundBean.getBiparietalDiameter());
            headCircumference = String.valueOf(ultraSoundBean.getHeadCircumference());
            femurLength = String.valueOf(ultraSoundBean.getFemurLength());
            occipitofrontalDiameter = String.valueOf(ultraSoundBean.getOccipitofrontalDiameter());
            abdominalCircumference = String.valueOf(ultraSoundBean.getAbdominalCircumference());
            humerusLength = String.valueOf(ultraSoundBean.getHumerusLength());
            estimatedFetalWeight = String.valueOf(ultraSoundBean.getEstimatedFetalWeight());
        }
    } catch (FormValidationException e){
        %>
        <div align=center>
            <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
        </div>
        <%
    }
    %>
<div>
    <div class="container">
        <span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span>
        <div class="row">
            <div class="col-sm-5">
                <h2>Upload Ultrasound Image</h2>
                <form name="mainForm" enctype="multipart/form-data" action="addUltraSoundDetails.jsp?status=imageUpload&obVisitId=<%=officeVisitId%>" method="post">
                    <table class="fTable mainTable">
                        <tr>
                            <td><b>Patient ID :</b></td> <td><input type="text" disabled name="patientId" value="<%=pidString%>" /></td>
                        </tr>
                        <tr>
                            <td><b>OB/GYN HCP MID :</b> </td> <td><input type="text" disabled name="loggedInMid" value="<%=loggedInMID%>" /></td>
                        </tr>
                        <tr><th colspan="3">Choose File</th></tr>
                        <tr><td>
                            <input name="ultraSoundImageFile" value="<%=imageLocation%>" type="file"/>
                        </td>
                            <td>
                                <input type="submit" value="Send File" id="sendFile" name="sendFile">
                            </td>
                        </tr>
                    </table>
                </form>
                <%
                    if (imgVal!=null && !imgVal.isEmpty()) {
                %>
                <img class="rounded-bottom" src="http://localhost:8080/iTrust/image/ultrasound/<%=imgVal%>"/>
                <%
                    }
                %>
            </div>
            <div class="col-sm-7">
                <h2>Add Ultrasound Report Details</h2>
                <form id="mainForm" method="post" action="addUltraSoundDetails.jsp?status=newRecord&obVisitId=<%=officeVisitId%>" method="post">
                    <table class="fTable mainTable" width="auto">
                        <tr>
                            <td><b>Patient ID :</b></td> <td><input type="text" disabled name="patientId" value="<%=pidString%>" /></td>
                        </tr>
                        <tr>
                            <td><b>OB/GYN HCP MID :</b> </td> <td><input type="text" disabled name="loggedInMid" value="<%=loggedInMID%>" /></td>
                        </tr>
                        <tr>
                            <td><b>Date:</b></td> <td>
                            <input type="text" name="createDate" value="<%=lastSchedDate%>" />
                            <input type="button" value="Date" onclick="displayDatePicker('createDate',false,'MM/dd/yyyy hh:mm a');" />
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
                            <td><b>Crown Rump Length :</b></td>
                            <td><input type="text" id="crownRumpLength" name="crownRumpLength" value="<%=crownRumpLength!=null && !crownRumpLength.isEmpty()?crownRumpLength:0.0%>"/></td>
                        </tr>
                        <tr>
                            <td><b>Biparietal Diameter :</b></td>
                            <td><input type="text" id="biparietalDiameter" name="biparietalDiameter" value="<%=biparietalDiameter!=null && !biparietalDiameter.isEmpty()?biparietalDiameter:0.0%>"/></td>
                        </tr>
                        <tr>
                            <td><b>Head Circumference :</b></td> <td>
                            <input type="text" id="headCircumference" name="headCircumference" value="<%=headCircumference!=null && !headCircumference.isEmpty()?headCircumference:0.0%>"/>
                        </td>
                        </tr>
                        <tr>
                            <td><b>femur length :</b></td> <td>
                            <input type="text" id="femurLength" name="femurLength" value="<%=femurLength!=null && !femurLength.isEmpty()?femurLength:0.0%>"/>
                        </td>
                        <tr>
                            <td><b>occipitofrontal diameter :</b></td> <td>
                            <input type="text" id="occipitofrontalDiameter" name="occipitofrontalDiameter" value="<%=occipitofrontalDiameter!=null && !occipitofrontalDiameter.isEmpty()?occipitofrontalDiameter:0.0%>"/>
                        </td>
                        </tr>
                        <tr>
                            <td><b>Abdominal Circumference :</b></td> <td>
                            <input type="text" id="abdominalCircumference" name="abdominalCircumference" value="<%=abdominalCircumference!=null && !abdominalCircumference.isEmpty()?abdominalCircumference:0.0%>"/>
                        </td>
                        </tr>
                        <tr>
                            <td><b>humerus length :</b></td> <td>
                            <input type="text" id="humerusLength" name="humerusLength" value="<%=humerusLength!=null && !humerusLength.isEmpty()?humerusLength:0.0%>"/>
                        </td>
                        </tr>
                        <tr>
                            <td><b>estimated fetal weight :</b></td> <td>
                            <input type="text" id="estimatedFetalWeight" name="estimatedFetalWeight" value="<%=estimatedFetalWeight!=null && !estimatedFetalWeight.isEmpty()?estimatedFetalWeight:0.0%>"/>
                        </td>
                        </tr>

                    </table>

                    <br><br>
                    <div align="center">
                        <input type="submit" value="Add Record" name="createUltrasonicRecordButton" id="createUltrasonicRecordButton"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <input id="goBackButton" type="button" onclick="window.location.href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp'; return false;"
           value="Back" style="font-size: 150%; font-weight: bold;" />
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
