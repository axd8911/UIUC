# Setup the Project:

This project is very difficult to setup.  I would not recommend using any editor other than IntelliJ to build and run the project.  Here are the steps to follow to build and run the project locally:

1. Getting IntelliJ ultimate and a student license. You must use the Ultimate version as the community version does not have support for TomCat (and various other useful plugins)
    * https://www.jetbrains.com/idea/
    * asdfehttps://www.jetbrains.com/student/
2. Getting the prerequisite software
    * Install JDK 8: https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html
    * Install Tomcat 9: https://tomcat.apache.org/download-90.cgi If you are a mac user, simply "brew install tomcat@9" and take note of the installed location
    * Install MySQL 5.6:
        * https://dev.mysql.com/downloads/mysql/5.6.html
        * Ubuntu 16.04: https://tecadmin.net/install-mysql-5-on-ubuntu/
        * Mac: https://gist.github.com/benlinton/d24471729ed6c2ace731 Look at the older version part
        * Windows: I am not a windows user: if you succeeded with windows, could you post your method of installing mysql5.6 in the comment?
3. Setting up the project
    * After cloning the repository, use the Import Project dialog in IntelliJ to import it. Select the pom.xml (inside iTrust folder) file in the dialog as iTrust dependencies are managed by maven. Make sure you select "Import Maven projects automatically" and "Eclipse" profile in the set up. Select the JDK you just installed as the project SDK.
    * Edit the WebRoot/META-INF/context.xml file if your MySQL root password is anything other than an empty string.
    * You will now run DBBuilder and edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator under the test folder to populate your iTrust database (Note that it may take long to populate the data). Here, we recommend you add a new MySQL data source in the Data source and drivers dialog to verify if your database is populated. (The database name is itrust)
    * Add a new configuration by going Run → Edit Configurations... Clicking on the + icon, you will add a Tomcat Server (local) run configurations (note that it may hide behind the "not applicable" dropdown ):
        * Change the name to iTrust
        * Under the server tab make sure your "After launch" url is http://localhost:8080/iTrust
        * Under the server tab change the Application to where you installed TomCat
        * Under the deployment tab. Add the iTrust:war artifact. Change the Application context to /iTrust. 
 

You can now run the iTrust Run configuration and test it in a web browser with the  http://localhost:8080/iTrust url.

# What We Accomplished:
Our team was tasked to add 4 new features to the web application.  You can find detailed information about these features and what was specifically added to the project in the FinalCodeSubmission directory.
