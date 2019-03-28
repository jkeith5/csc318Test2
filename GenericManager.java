import java.util.ArrayList;

public class GenericManager<T extends Comparable> {
    protected ArrayList<T> myList=new ArrayList<T>();
    protected int count;//size of arraylist above +1
    public GenericManager(){}
    public void addAtEnd(T object){
        this.myList.add(count, object);
        count++;
    }
    public void addInOrder(T object){
        int i=0;

        //below is less than or equal to first object in list
        if (this.count==0||object.compareTo(this.myList.get(0))==-1||object.compareTo(this.myList.get(0))==0){
            this.myList.add(0,object);
        } else if (object.compareTo(this.myList.get(this.count-1))==1||object.compareTo(this.myList.get(this.count-1))==0){
            //x is greater than last entry
            myList.add(this.count,object);
        }else {
            //greater than first, less than last
            while ((i<this.count)&& object.compareTo(this.myList.get(i))==1){
                i++;
            }
            this.myList.add(i,object);
        }
        this.count++;
    }
    public T getVal(int i){
        if (i<this.count)
            return this.myList.get(i);
        else
            return this.myList.get(0);
    }
    public void addAtFront(T object){
        this.myList.add(0,object);
        this.count++;
    }
    public void sort(){//sorts the arraylist
        T xSave, ySave, a, b;
        int temp=1;
        int xlast=this.myList.size();
        while (temp==1){
            temp=0;
            for (int i=0;i<=xlast-2;i++){
                a=this.myList.get(i);
                b=this.myList.get(i+1);
                switch (a.compareTo(b)){
                    case -1:
                        break;
                    case 1:
                        xSave=this.myList.get(i);
                        ySave=this.myList.get(i+1);
                        this.myList.remove(i);
                        this.myList.add(i,ySave);
                        this.myList.remove(i+1);
                        this.myList.add(i+1,xSave);
                        temp=1;
                        break;
                    default:
                }

            }
        }
    }
    public void removeM(int i){
        if (i>=0&&i<=this.count-1){
            this.myList.remove(i);
            this.count--;
        }
    }
}
