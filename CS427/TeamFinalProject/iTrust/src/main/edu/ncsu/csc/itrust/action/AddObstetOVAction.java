package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.RemoteMonitoringDAO;

@Deprecated
public class AddObstetOVAction {
    private RemoteMonitoringDAO rmDAO;
    private AuthDAO authDAO;
    private long loggedInMID;

    /**
     * Constructor
     *
     * @param factory The DAOFactory used to create the DAOs used in this action.
     * @param loggedInMID The MID of the HCP editing their monitoring list.
     */
    public AddObstetOVAction(DAOFactory factory, long loggedInMID) {
        this.loggedInMID = loggedInMID;
        this.rmDAO = factory.getRemoteMonitoringDAO();
        this.authDAO = factory.getAuthDAO();
    }
}
