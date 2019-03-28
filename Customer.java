public class Customer implements Comparable {
    protected double timeInLine;
    protected double timeInSys;
    protected double timeInServer;
    protected double timeArrived;
    protected int direction;//0=NS, 1= EW
    protected int heading;//where it wants to turn. for NS, 0=west 1=north.for EW, 0=south, 1=north
    protected int myNum;
    public Customer(int id, int direction){
        this.myNum = id;
        this.direction=direction;
    }

    public double getTimeInLine() {
        return timeInLine;
    }

    public void setTimeInLine(double timeInLine) {
        this.timeInLine = timeInLine;
    }

    public double getTimeInSys() {
        return timeInSys;
    }

    public void setTimeInSys(double timeInSys) {
        this.timeInSys = timeInSys;
    }

    public double getTimeInServer() {
        return timeInServer;
    }

    public void setTimeInServer(double timeInServer) {
        this.timeInServer = timeInServer;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    @Override
    public int compareTo(Object o) {
        if (this.direction< ((Customer) o).direction) return -1;
        else if (this.direction == ((Customer) o).direction) {
            return Double.compare(this.timeInLine, ((Customer) o).timeInLine);
        } else return 1;
    }
}
