package in.kassapos.a1broilers.api;

import java.sql.Timestamp;

/**
 * Created by KASSAPOS8 on 9/19/2015.
 */
public class ReceivedData implements Comparable<ReceivedData>{
    public Long id;
    public String data;
    public Timestamp created_date=new Timestamp(System.currentTimeMillis());

    @Override
    public int compareTo(ReceivedData another) {
        if(another==null){
            return 0;
        }else if(this.id>another.id){
            return -1;
        }
        return 1;
    }
}
