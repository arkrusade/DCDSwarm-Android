package com.orctech.dcdswarm.Models;

import org.junit.Test;

/**
 * Created by Justin Lee on 6/3/2017.
 */
public class BlockScheduleTest {
    //region input
    String input = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAALdwQAAAAL\n" +
            "e3NyACBqYXZhLmlvLk5vdFNlcmlhbGl6YWJsZUV4Y2VwdGlvbihWeADnhhY1AgAAeHIAHWphdmEu\n" +
            "aW8uT2JqZWN0U3RyZWFtRXhjZXB0aW9uZMPka405+98CAAB4cgATamF2YS5pby5JT0V4Y2VwdGlv\n" +
            "bmyAc2RlJfCrAgAAeHIAE2phdmEubGFuZy5FeGNlcHRpb27Q/R8+GjscxAIAAHhyABNqYXZhLmxh\n" +
            "bmcuVGhyb3dhYmxl1cY1Jzl3uMsDAARMAAVjYXVzZXQAFUxqYXZhL2xhbmcvVGhyb3dhYmxlO0wA\n" +
            "DWRldGFpbE1lc3NhZ2V0ABJMamF2YS9sYW5nL1N0cmluZztbAApzdGFja1RyYWNldAAeW0xqYXZh\n" +
            "L2xhbmcvU3RhY2tUcmFjZUVsZW1lbnQ7TAAUc3VwcHJlc3NlZEV4Y2VwdGlvbnN0ABBMamF2YS91\n" +
            "dGlsL0xpc3Q7eHBxAH4ACXQAIWNvbS5vcmN0ZWNoLmRjZHN3YXJtLk1vZGVscy5CbG9ja3VyAB5b\n" +
            "TGphdmEubGFuZy5TdGFja1RyYWNlRWxlbWVudDsCRio8PP0iOQIAAHhwAAAAGXNyABtqYXZhLmxh\n" +
            "bmcuU3RhY2tUcmFjZUVsZW1lbnRhCcWaJjbdhQIABEkACmxpbmVOdW1iZXJMAA5kZWNsYXJpbmdD\n" +
            "bGFzc3EAfgAGTAAIZmlsZU5hbWVxAH4ABkwACm1ldGhvZE5hbWVxAH4ABnhwAAAEyHQAGmphdmEu\n" +
            "aW8uT2JqZWN0T3V0cHV0U3RyZWFtdAAXT2JqZWN0T3V0cHV0U3RyZWFtLmphdmF0AAx3cml0ZU9i\n" +
            "amVjdDBzcQB+AA0AAAFadAAaamF2YS5pby5PYmplY3RPdXRwdXRTdHJlYW10ABdPYmplY3RPdXRw\n" +
            "dXRTdHJlYW0uamF2YXQAC3dyaXRlT2JqZWN0c3EAfgANAAAC3nQAE2phdmEudXRpbC5BcnJheUxp\n" +
            "c3R0AA5BcnJheUxpc3QuamF2YXQAC3dyaXRlT2JqZWN0c3EAfgAN/////nQAGGphdmEubGFuZy5y\n" +
            "ZWZsZWN0Lk1ldGhvZHQAC01ldGhvZC5qYXZhdAAGaW52b2tlc3EAfgANAAAD0XQAGWphdmEuaW8u\n" +
            "T2JqZWN0U3RyZWFtQ2xhc3N0ABZPYmplY3RTdHJlYW1DbGFzcy5qYXZhdAARaW52b2tlV3JpdGVP\n" +
            "YmplY3RzcQB+AA0AAAYAdAAaamF2YS5pby5PYmplY3RPdXRwdXRTdHJlYW10ABdPYmplY3RPdXRw\n" +
            "dXRTdHJlYW0uamF2YXQAD3dyaXRlU2VyaWFsRGF0YXNxAH4ADQAABcB0ABpqYXZhLmlvLk9iamVj\n" +
            "dE91dHB1dFN0cmVhbXQAF09iamVjdE91dHB1dFN0cmVhbS5qYXZhdAATd3JpdGVPcmRpbmFyeU9i\n" +
            "amVjdHNxAH4ADQAABMJ0ABpqYXZhLmlvLk9iamVjdE91dHB1dFN0cmVhbXQAF09iamVjdE91dHB1\n" +
            "dFN0cmVhbS5qYXZhdAAMd3JpdGVPYmplY3Qwc3EAfgANAAABWnQAGmphdmEuaW8uT2JqZWN0T3V0\n" +
            "cHV0U3RyZWFtdAAXT2JqZWN0T3V0cHV0U3RyZWFtLmphdmF0AAt3cml0ZU9iamVjdHNxAH4ADQAA\n" +
            "AC50ACljb20ub3JjdGVjaC5kY2Rzd2FybS5Nb2RlbHMuQmxvY2tTY2hlZHVsZXQAEkJsb2NrU2No\n" +
            "ZWR1bGUuamF2YXQADmJsb2Nrc1RvU3RyaW5nc3EAfgANAAAAbHQAKGNvbS5vcmN0ZWNoLmRjZHN3\n" +
            "YXJtLkhlbHBlcnMuQ2FjaGVIZWxwZXJ0ABBDYWNoZUhlbHBlci5qYXZhdAASc3RvcmVCbG9ja1Nj\n" +
            "aGVkdWxlc3EAfgANAAAAOXQAKGNvbS5vcmN0ZWNoLmRjZHN3YXJtLkhlbHBlcnMuQmxvY2tIZWxw\n" +
            "ZXJ0ABBCbG9ja0hlbHBlci5qYXZhdAANcHJvY2Vzc0Jsb2Nrc3NxAH4ADQAAABp0AC1jb20ub3Jj\n" +
            "dGVjaC5kY2Rzd2FybS5BY3Rpdml0aWVzLkJsb2NrQWN0aXZpdHl0ABJCbG9ja0FjdGl2aXR5Lmph\n" +
            "dmF0AAhvbkNyZWF0ZXNxAH4ADQAAGhd0ABRhbmRyb2lkLmFwcC5BY3Rpdml0eXQADUFjdGl2aXR5\n" +
            "LmphdmF0AA1wZXJmb3JtQ3JlYXRlc3EAfgANAAAEXnQAG2FuZHJvaWQuYXBwLkluc3RydW1lbnRh\n" +
            "dGlvbnQAFEluc3RydW1lbnRhdGlvbi5qYXZhdAAUY2FsbEFjdGl2aXR5T25DcmVhdGVzcQB+AA0A\n" +
            "AAo6dAAaYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWR0ABNBY3Rpdml0eVRocmVhZC5qYXZhdAAV\n" +
            "cGVyZm9ybUxhdW5jaEFjdGl2aXR5c3EAfgANAAAKpnQAGmFuZHJvaWQuYXBwLkFjdGl2aXR5VGhy\n" +
            "ZWFkdAATQWN0aXZpdHlUaHJlYWQuamF2YXQAFGhhbmRsZUxhdW5jaEFjdGl2aXR5c3EAfgAN////\n" +
            "/3QAGmFuZHJvaWQuYXBwLkFjdGl2aXR5VGhyZWFkdAATQWN0aXZpdHlUaHJlYWQuamF2YXQABy13\n" +
            "cmFwMTJzcQB+AA0AAAXFdAAcYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQkSHQAE0FjdGl2aXR5\n" +
            "VGhyZWFkLmphdmF0AA1oYW5kbGVNZXNzYWdlc3EAfgANAAAAZnQAEmFuZHJvaWQub3MuSGFuZGxl\n" +
            "cnQADEhhbmRsZXIuamF2YXQAD2Rpc3BhdGNoTWVzc2FnZXNxAH4ADQAAAJp0ABFhbmRyb2lkLm9z\n" +
            "Lkxvb3BlcnQAC0xvb3Blci5qYXZhdAAEbG9vcHNxAH4ADQAAF+d0ABphbmRyb2lkLmFwcC5BY3Rp\n" +
            "dml0eVRocmVhZHQAE0FjdGl2aXR5VGhyZWFkLmphdmF0AARtYWluc3EAfgAN/////nQAGGphdmEu\n" +
            "bGFuZy5yZWZsZWN0Lk1ldGhvZHQAC01ldGhvZC5qYXZhdAAGaW52b2tlc3EAfgANAAADdnQANmNv\n" +
            "bS5hbmRyb2lkLmludGVybmFsLm9zLlp5Z290ZUluaXQkTWV0aG9kQW5kQXJnc0NhbGxlcnQAD1p5\n" +
            "Z290ZUluaXQuamF2YXQAA3J1bnNxAH4ADQAAAwh0ACJjb20uYW5kcm9pZC5pbnRlcm5hbC5vcy5a\n" +
            "eWdvdGVJbml0dAAPWnlnb3RlSW5pdC5qYXZhdAAEbWFpbnNyAB9qYXZhLnV0aWwuQ29sbGVjdGlv\n" +
            "bnMkRW1wdHlMaXN0ergXtDynnt4CAAB4cHg=\n";
    //endregion
    @Test
    public void blocksToString() throws Exception {
        
    }
    
    @Test
    public void setBlocks() throws Exception {
    }
    
}