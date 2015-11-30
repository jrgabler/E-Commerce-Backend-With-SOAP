package server;

import business.*;
import xml.*;
import java.util.*;
import components.data.*;

/**
 * created to keep the AppointmentService's code minimal
 * @author Julianna Gabler
 */
public class Main{
/**********************************************************************************************************************/
/********** ATTRIBUTE *************************************************************************************************/
    Database database = null;
/**********************************************************************************************************************/
/************ METHODS *************************************************************************************************/
    /**
     * intialize the database
     * @return boolean
     */
    public boolean initialize(){
        this.database = new Database();
        return this.database.load();
    }//end intiialize

    /**
     * get all of the appointments in the database
     * @return xml String
     */
    public String getAllAppointments(){
        //grab the list of appointments from database
        List<Object> obj = this.database.getDB().getData("Appointment","");
        //create objects for appt and xml
        Appointment appt = new Appointment();
        XML xml = new XML();
        String xmlStr = xml.getStartTag() + xml.getTagNameStart();//add starting tags
        //for each appointment grab the XML tags corresponding it
        for(Object objs : obj){
            xml.setAppointment((Appointment)objs);
            xmlStr += xml.appointmentXML();
        }
        //add the </AppointmentList> ending
        xmlStr += xml.getTagNameEnd();
        return xmlStr;
    }//end getAllAppointments

    /**
     * gets the appointment in xml string format
     * @param appointNumber the number of the appt
     * @return xmlStr String
     */
    public String getAppointment(String appointNumber){
        List<Object> obj = this.database.getAppointment(appointNumber);
        Object rtn = null;
        for(Object objs : obj){
            rtn = objs;
        }
        //if null or empty, return the error string
        if(rtn == "" || rtn == null){
            XML xml = new XML();
            return xml.error();
        }
        Appointment appt = (Appointment)rtn;
        XML xml = new XML(appt);
        String xmlStr = xml.getStartTag() + xml.getTagNameStart() + xml.appointmentXML();
        xmlStr += xml.getTagNameEnd();
        //return it in full
        return xmlStr;
    }//end getAppointment

    /**
     * adds the appointment to the database or gets the next available appointment
     * from the database
     * @param xmlStyle
     * @return apptXML
     */
    public String addAppointment(String xmlStyle){
        try {
            //parse the XML document and grab necessary information
            ParseXML parseXML = new ParseXML(xmlStyle);
            //validate data
            DataValidation dv = new DataValidation(parseXML.getApptInfo(), parseXML.getLabTests());
            //add the appointment or get the next appointment
            String apptXML = dv.apptRequirements();
            return apptXML;

        }catch(NullPointerException npe){
            npe.printStackTrace();
        }
        
        return "";
    }//end addAppointment

    public static void main(String[] args){
        String xml = "<?xml version='1.0' encoding='utf-8' standalone='no'?>";
        xml += "<appointment><date>2016-12-30</date><time>10:00</time><patientId>230";
        xml += "</patientId><physicianId>20</physicianId><pscId>520</pscId><phlebotomistId>";
        xml += "110</phlebotomistId><labTests><test id='86900' dxcode='292.9' /><test id='86609'";
        xml += " dxcode='307.3' /></labTests></appointment>";

        Main m = new Main();
        m.addAppointment(xml);
    }
}//end Main class