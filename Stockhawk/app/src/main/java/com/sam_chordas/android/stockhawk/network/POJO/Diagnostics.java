
package com.sam_chordas.android.stockhawk.network.POJO;

import java.util.ArrayList;
import java.util.List;

public class Diagnostics {

    public List<Url> url = new ArrayList<Url>();
    public String publiclyCallable;
    public List<Cache> cache = new ArrayList<Cache>();
    public List<Query_> query = new ArrayList<Query_>();
    public Javascript javascript;
    public String userTime;
    public String serviceTime;
    public String buildVersion;

}
