import java.util.ArrayList;
import java.util.Random;
import java.util.Random.*;

public class Main {

    public static void main(String[] args) {
        /*
        event list with numbers to their eve3nts
        1=arrival to intersection, also determines which line it enters
        2=enters line
        3=enters service bay 1
        4=leaves service bay 1
        5=balks
        8=shutdown of sim
        */
        double timeEW = 0, timeNS = 0;
        int heartPatientsSquared = 0, bleederPatientSquared = 0, gastroPatientSquard = 0;
        double bigTime = 0.0;
        double endTime = 100.0;
        double eventTime = 0.0;
        double deltaTime;
        double balkTime = 5.0;
        boolean lightEW = false, lightNS = true;//EW lasts for 2 minutes, NS for 3. if true that means that light is on
        int balkID = 0, myBalkCust;
        int numInQueueEW, numInQueueNS, totalThruNS = 0, totalThruEW = 0;
        int numInEvent;
        int totalThruSys = 0;
        double totalTimeInLine = 0.0, totalTimeInLine2 = 0, totalTimeInSys = 0.0, totalTimeInServer = 0.0, totalTimeInServer2 = 0.0, ttil, ttis;
        boolean busy1 = false, busy2 = false;//shows both servers are empty
        GenericManager<Event> eventQueue = new GenericManager<>();
        GenericManager<Customer> ewQueue = new GenericManager<>();
        GenericManager<Customer> nsQueue = new GenericManager<>();
        Customer served1 = new Customer(-9, 2);
        Customer served2 = new Customer(-9, 2);
        double delTimeServe, timeArrive, delTimeArriveEW, delTimeArriveNS;
        Customer newCust = new Customer(-9, 2);
        Customer workCust = new Customer(-9, 2);
        Event workEvent = new Event(8, 1000, 0);
        eventQueue.addInOrder(workEvent);
        delTimeArriveEW = timeToEvent(120);//120 per hour
        delTimeArriveNS = timeToEvent(180);//180 per hour
        eventTime = bigTime + delTimeArriveEW;
        double eventTime2 = bigTime + delTimeArriveNS;
        double i = 0;
        i += .05;
        workEvent = new Event(5, i, 0);//turns NS on
        eventQueue.addInOrder(workEvent);
        i += .03333;
        workEvent = new Event(6, i, 0);//turns EW on
        eventQueue.addInOrder(workEvent);
        workEvent = new Event(1, eventTime, 0);
        eventQueue.addInOrder(workEvent);
        workEvent = new Event(2, eventTime2, 0);
        eventQueue.addInOrder(workEvent);
        workEvent = eventQueue.getVal(0);

        System.out.println(i);
        while (workEvent.eventType != 8) {
            deltaTime = workEvent.time - bigTime;
            ttil = updateCustomer(ewQueue, deltaTime);
            totalTimeInLine += ttil;
            totalTimeInLine2 += Math.pow(ttil, 2);
            ttis = updateServer(served1, served2, busy1, busy2, deltaTime);
            totalTimeInServer += ttis;
            totalTimeInServer2 += Math.pow(ttis, 2);
            bigTime = workEvent.time;
            //above preps the server and updates customer and statistics
            numInQueueEW = ewQueue.count;
            numInQueueNS = nsQueue.count;
            switch (workEvent.eventType) {
                case 1://arrives at intersection from EW
                    //System.out.println("incase1");
                    if (!busy1 && numInQueueEW <= 0 && lightEW) {//if not busy, and light is on
                        newCust.timeArrived = bigTime;
                        busy1 = true;
                        served1 = newCust;
                        setDirection(newCust);
                        if (newCust.heading == 0) {//heading south
                            delTimeServe = timeToEvent(720);//720 cars can be processed per hour
                        } else {
                            delTimeServe = timeToEvent(450);//450 cars can be processed per hour
                        }
                        eventTime = delTimeServe + bigTime;
                        workEvent = new Event(4, eventTime, 0);
                        eventQueue.addInOrder(workEvent);
                    } else {
                        balkID++;
                        newCust = new Customer(balkID, 1);
                        newCust.timeArrived = bigTime;
                        setDirection(newCust);
                        if (ewQueue.myList.size() == 0) {
                            ewQueue.myList.add(newCust);
                        } else {
                            ewQueue.addInOrder(newCust);
                        }
                    }
                    delTimeArriveEW = timeToEvent(120);
                    eventTime = bigTime + delTimeArriveEW;
                    workEvent = new Event(1, eventTime, 0);
                    eventQueue.addInOrder(workEvent);
                    break;
                case 2://arrives at intersection from NS
                    //System.out.println("in case 2");
                    if (!busy1 && numInQueueNS <= 0 && lightNS) {//if not busy, and light is on
                        newCust.timeArrived = bigTime;
                        busy1 = true;
                        served1 = newCust;
                        setDirection(newCust);
                        if (newCust.heading == 0) {//heading west
                            delTimeServe = timeToEvent(450);//450 cars can be processed per hour
                        } else {
                            delTimeServe = timeToEvent(900);//900 cars can be processed per hour
                        }
                        eventTime = delTimeServe + bigTime;
                        workEvent = new Event(4, eventTime, 0);
                        eventQueue.addInOrder(workEvent);
                    } else {
                        balkID++;
                        newCust = new Customer(balkID, 0);
                        newCust.timeArrived = bigTime;
                        setDirection(newCust);
                        if (nsQueue.myList.size() == 0) {
                            nsQueue.myList.add(newCust);
                        } else {
                            nsQueue.addInOrder(newCust);
                        }
                    }
                    delTimeArriveEW = timeToEvent(180);
                    eventTime = bigTime + delTimeArriveEW;
                    workEvent = new Event(2, eventTime, 0);
                    eventQueue.addInOrder(workEvent);
                    break;
                case 3://pushes in server
                    numInQueueEW = ewQueue.count;
                    numInQueueNS = nsQueue.count;
                    //System.out.println("in case 3");
                    if (!busy1 && numInQueueNS > 0 && lightNS) {//checks for NS light
                        workCust = nsQueue.getVal(0);
                        totalThruNS++;
                        nsQueue.removeM(0);
                        busy1 = true;
                        System.out.println("NS queue: "+nsQueue.myList.size());
                        served1 = workCust;
                        if (workCust.heading == 0) {//NS heading west
                            delTimeServe = timeToEvent(450);
                        } else {
                            delTimeServe = timeToEvent(900);
                        }
                        eventTime = delTimeServe + bigTime;
                        workEvent = new Event(4, eventTime, 0);
                        eventQueue.addInOrder(workEvent);
                    } else if (!busy1 && numInQueueEW > 0 && lightEW) {//checks for EW light
                        workCust = ewQueue.getVal(0);
                        totalThruEW++;
                        ewQueue.removeM(0);
                        busy1 = true;
                        System.out.println("EW queue: "+ewQueue.myList.size());
                        served1 = workCust;
                        if (workCust.heading == 0) {//NS heading west
                            delTimeServe = timeToEvent(720);
                        } else {
                            delTimeServe = timeToEvent(450);
                        }
                        eventTime = delTimeServe + bigTime;
                        workEvent = new Event(4, eventTime, 0);
                        eventQueue.addInOrder(workEvent);
                    }
                    if (workCust.direction == 0)
                        timeNS += bigTime - served1.timeArrived;
                    else
                        timeEW += bigTime - served1.timeArrived;
                    break;
                case 4://process from server, out
                    busy1 = false;
                    totalThruSys++;
                    numInQueueEW = ewQueue.count;
                    numInQueueNS = nsQueue.count;
                    if (numInQueueEW > 0 || numInQueueNS > 0) {
                        workEvent = new Event(3, bigTime + .000001, 0);
                        eventQueue.addInOrder(workEvent);
                    }
                    break;
                case 5://turns ns light on
                    lightNS = true;
                    lightEW = false;
                    eventQueue.addInOrder(new Event(6, bigTime + .0500000000001, 0));
                    break;
                case 6:
                    lightNS = false;
                    lightEW = true;
                    eventQueue.addInOrder(new Event(5, bigTime + .03333000000000001, 0));
                    break;
                case 8://shutdown event
                    continue;
                default:
                    System.out.println("bad event type of: " + workEvent.eventType + "at time: " + workEvent.time);
                    break;
            }
            eventQueue.removeM(0);//deletes processed event, and grabs next event.
            eventQueue.sort();
            workEvent = eventQueue.getVal(0);

        }
        System.out.println(totalThruSys);
        System.out.println("Average Cars in line(E/W): " + (double) totalThruEW / 1000);
        System.out.println("Average Cars in line(N/S): " + (double) totalThruNS / 1000);
    }

    public static double timeToEvent(double rate) {
        double delTime;
        double bigX;
        bigX = Math.random();
        while (bigX > .9) {
            bigX = Math.random();
        }
        delTime = -Math.log(1 - bigX) / rate;
        return delTime;
    }

    public static void setDirection(Customer customer) {//sets the direction of the arriving customer
        double temp = 0;
        temp = Math.random();
        if (customer.direction == 0) {//NS cars
            if (temp < .25) {//ns car wants to get west
                customer.heading = 0;
            } else {//ns car wants to go north
                customer.heading = 1;
            }
        } else {//ew cars
            if (temp < .7) {//ew car wants to go south
                customer.heading = 0;
            } else {//ew cars wants to go north
                customer.heading = 1;
            }
        }
    }

    public static void purgeEvent(GenericManager<Event> eventQueue, int balkID) {//removes balking customer event
        int i, numInQueue, eventBalkID;
        Event workEvent = new Event(1, 1.0, 1);
        numInQueue = eventQueue.count;
        workEvent = eventQueue.getVal(0);
        eventBalkID = workEvent.custID;
        i = 0;
        while (eventBalkID != balkID && i <= numInQueue - 1) {
            workEvent = eventQueue.getVal(i);
            eventBalkID = workEvent.custID;
            i++;
        }
        if (eventBalkID == balkID) eventQueue.removeM(i - 1);
        return;
    }

    public static double updateCustomer(GenericManager<Customer> custLine, double delTime) {
        double lineTime = 0;
        int custInLine = custLine.count;
        if (custInLine == 0)
            return lineTime;
        else
            return lineTime = delTime * custInLine;
    }

    public static double updateServer(Customer s1, Customer s2, boolean b1, boolean b2, double delTime) {
        double serveTime = 0;
        if (b1 && b2) return serveTime = 2 * delTime;
        else if (b1 || b2) return delTime;
        return serveTime;
    }
}
