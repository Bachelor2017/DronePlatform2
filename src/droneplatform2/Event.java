package droneplatform2;

/**
 *
 * @author Jorgen
 */
public class Event {
    private String eventName;
    private int eventNumber;
    private int xValue;
    private int yValue;
    private int zValue;
    private int timeSyclusOfEvent;
    
    
    public Event(int eventNumber,String eventName,int xValue,int zValue,int yValue)
    {
        this.eventNumber = eventNumber;
        this.eventName = eventName;
        this.xValue = xValue;
        this.yValue = yValue;
        this.zValue = zValue;
    }
    
    public Event(int eventNumber, String eventName)
    {
        this.eventNumber = eventNumber;
        this.eventName = eventName;
      
    }

    /**
     * get the eventName
     * @return string eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * setting the eventName
     * @param eventName name of the event return in String
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * get the eventNumber
     * @return the int eventNumber
     */
    public int getEventNumber() {
        return eventNumber;
    }

    /**
     * setting the eventNumber
     * @param eventNumber the int number of the event
     */
    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
    }

    
    
    
    /////////////
    ////KAN TA BORT OM DET IKKE SKAL BRUKES. MULIG DETE BLIR SATT I ARDUINO
    
    /**
     * get the x-value of the event
     * @return the x-value of the event as int 
     */
    public int getxValue() {
        return xValue;
    }

    /**
     * set the x-value of the event
     * @param the x-value of the event as int 
     */
    public void setxValue(int xValue) {
        this.xValue = xValue;
    }
/**
     * get the x-value of the event
     * @return the x-value of the event as int 
     */
    public int getyValue() {
        return yValue;
    }

    /**
     * set the x-value of the event
     * @param the x-value of the event as int 
     */
    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    /**
     * get the x-value of the event
     * @return the x-value of the event as int 
     */
    public int getzValue() {
        return zValue;
    }

    /**
     * set the x-value of the event
     * @param the x-value of the event as int 
     */
    public void setzValue(int zValue) {
        this.zValue = zValue;
    }
    
    public int getTimeSyclusOfEvent()
    {
        return this.timeSyclusOfEvent;
    }
    
    public void settimeSyclusOfEvent(int timeSyclusOfEvent)
    {
        this.timeSyclusOfEvent = timeSyclusOfEvent;
    }
    
}
