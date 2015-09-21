package ebank.core.common;

import java.util.EnumMap;
import java.util.Map;

public class IsmsConfig {
    public static enum TrxSTS{UNPAID,SUCCESS,FAILURE,CLOSED,UNCERTAIN,NOTFOUND}
    private static EnumMap<TrxSTS,String> stateMap=new EnumMap<TrxSTS,String>(TrxSTS.class);
    static {
        stateMap.put(TrxSTS.UNPAID,"0");
        stateMap.put(TrxSTS.SUCCESS,"1");
        stateMap.put(TrxSTS.FAILURE,"2");
        stateMap.put(TrxSTS.CLOSED,"3");
        stateMap.put(TrxSTS.UNCERTAIN,"5");
        stateMap.put(TrxSTS.NOTFOUND,"-1");
    }

    public static String getTrxsts(TrxSTS ts){
        return stateMap.get(ts);
    }
}
