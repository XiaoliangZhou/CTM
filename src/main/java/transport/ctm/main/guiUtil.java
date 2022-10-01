package transport.ctm.main;

import transport.ctm.model.ctmCell;
import transport.ctm.model.endCell;
import transport.graph.Edge;

import java.util.ArrayList;
import java.util.List;

public class guiUtil {

    /**
     *
     * @param sec
     * @param edge
     */
    protected static void setupGridRowAndCowIndex(List ctms,Edge edge,int sec) {
        String l_name = edge.getLabel();
        double t=0;
        int todx=sec;
        int sdx=edge.getoCell();

        int[] grs = new int[2];
        int[] gcs = new int[2];

        endCell edc;
        ctmCell ctc;
        List<int[]> indxs = new ArrayList<>();
        if(l_name.equals("1")){
            grs = new int[]{1,1};
            gcs = new int[]{2,27};
        }
        if(l_name.equals("2")){
            grs = new int[]{2,11};
            gcs = new int[]{0,0};
        }
        if(l_name.equals("3")){
            grs = new int[]{0,0};
            gcs = new int[]{27,2};
        }
        if(l_name.equals("4")){
            grs = new int[]{2,11};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("5")){
            grs = new int[]{11,2};
            gcs = new int[]{1,1};
        }
        if(l_name.equals("6")){
            grs = new int[]{13,13};
            gcs = new int[]{2,11};
        }
        if(l_name.equals("7")){
            grs = new int[]{14,28};
            gcs = new int[]{0,0};
        }
        if(l_name.equals("8")){
            grs = new int[]{12,12};
            gcs = new int[]{11,2};
        }
        if(l_name.equals("9")){
            grs = new int[]{13,13};
            gcs = new int[]{14,18};
        }
        if(l_name.equals("10")){
            grs = new int[]{14,28};
            gcs = new int[]{12,12};
        }
        if(l_name.equals("11")){
            grs = new int[]{12,12};
            gcs = new int[]{18,14};
        }
        if(l_name.equals("12")){
            grs = new int[]{13,13};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("13")){
            grs = new int[]{14,21};
            gcs = new int[]{19,19};
        }
        if(l_name.equals("14")){
            grs = new int[]{11,2};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("15")){
            grs = new int[]{12,12};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("16")){
            grs = new int[]{14,21};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("17")){
            grs = new int[]{22,22};
            gcs = new int[]{36,30};
        }
        if(l_name.equals("18")){
            grs = new int[]{24,28};
            gcs = new int[]{37,37};
        }
        if(l_name.equals("19")){
            grs = new int[]{21,14};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("20")){
            grs = new int[]{23,23};
            gcs = new int[]{30,36};
        }
        if(l_name.equals("21")){
            grs = new int[]{22,22};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("22")){
            grs = new int[]{24,28};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("23")){
            grs = new int[]{21,14};
            gcs = new int[]{20,20};
        }
        if(l_name.equals("24")){
            grs = new int[]{23,23};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("25")){
            grs = new int[]{24,28};
            gcs = new int[]{19,19};
        }
        if(l_name.equals("26")){
            grs = new int[]{28,24};
            gcs = new int[]{20,20};
        }
        if(l_name.equals("27")){
            grs = new int[]{29,29};
            gcs = new int[]{18,14};
        }
        if(l_name.equals("28")){
            grs = new int[]{31,35};
            gcs = new int[]{19,19};
        }
        if(l_name.equals("29")){
            grs = new int[]{30,30};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("30")){
            grs = new int[]{37,37};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("31")){
            grs = new int[]{28,14};
            gcs = new int[]{13,13};
        }
        if(l_name.equals("32")){
            grs = new int[]{30,30};
            gcs = new int[]{14,18};
        }
        if(l_name.equals("33")){
            grs = new int[]{29,29};
            gcs = new int[]{11,2};
        }
        if(l_name.equals("34")){
            grs = new int[]{31,35};
            gcs = new int[]{12,12};
        }
        if(l_name.equals("35")){
            grs = new int[]{28,14};
            gcs = new int[]{1,1};
        }
        if(l_name.equals("36")){
            grs = new int[]{30,30};
            gcs = new int[]{2,11};
        }
        if(l_name.equals("37")){
            grs = new int[]{31,49};
            gcs = new int[]{0,0};
        }
        if(l_name.equals("38")){
            grs = new int[]{49,31};
            gcs = new int[]{1,1};
        }
        if(l_name.equals("39")){
            grs = new int[]{51,51};
            gcs = new int[]{2,11};
        }
        if(l_name.equals("40")){
            grs = new int[]{35,31};
            gcs = new int[]{13,13};
        }
        if(l_name.equals("41")){
            grs = new int[]{37,37};
            gcs = new int[]{14,18};
        }
        if(l_name.equals("42")){
            grs = new int[]{38,42};
            gcs = new int[]{12,12};
        }
        if(l_name.equals("43")){
            grs = new int[]{35,31};
            gcs = new int[]{20,20};
        }
        if(l_name.equals("44")){
            grs = new int[]{36,36};
            gcs = new int[]{18,14};
        }
        if(l_name.equals("45")){
            grs = new int[]{44,44};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("46")){
            grs = new int[]{38,42};
            gcs = new int[]{19,19};
        }
        if(l_name.equals("47")){
            grs = new int[]{28,24};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("48")){
            grs = new int[]{29,29};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("49")){
            grs = new int[]{31,35};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("50")){
            grs = new int[]{30,30};
            gcs = new int[]{30,36};
        }
        if(l_name.equals("51")){
            grs = new int[]{36,36};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("52")){
            grs = new int[]{35,31};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("53")){
            grs = new int[]{38,42};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("54")){
            grs = new int[]{28,24};
            gcs = new int[]{38,38};
        }
        if(l_name.equals("55")){
            grs = new int[]{29,29};
            gcs = new int[]{36,30};
        }
        if(l_name.equals("56")){
            grs = new int[]{31,49};
            gcs = new int[]{37,37};
        }
        if(l_name.equals("57")){
            grs = new int[]{43,43};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("58")){
            grs = new int[]{42,38};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("59")){
            grs = new int[]{45,49};
            gcs = new int[]{28,28};
        }
        if(l_name.equals("60")){
            grs = new int[]{49,31};
            gcs = new int[]{38,38};
        }
        if(l_name.equals("61")){
            grs = new int[]{49,45};
            gcs = new int[]{29,29};
        }
        if(l_name.equals("62")){
            grs = new int[]{50,50};
            gcs = new int[]{27,21};
        }
        if(l_name.equals("63")){
            grs = new int[]{50,50};
            gcs = new int[]{36,30};
        }
        if(l_name.equals("64")){
            grs = new int[]{51,51};
            gcs = new int[]{21,27};
        }
        if(l_name.equals("65")){
            grs = new int[]{49,45};
            gcs = new int[]{20,20};
        }
        if(l_name.equals("66")){
            grs = new int[]{50,50};
            gcs = new int[]{18,14};
        }
        if(l_name.equals("67")){
            grs = new int[]{42,38};
            gcs = new int[]{20,20};
        }
        if(l_name.equals("68")){
            grs = new int[]{51,51};
            gcs = new int[]{30,36};
        }
        if(l_name.equals("69")){
            grs = new int[]{45,49};
            gcs = new int[]{19,19};
        }
        if(l_name.equals("70")){
            grs = new int[]{43,43};
            gcs = new int[]{18,14};
        }
        if(l_name.equals("71")){
            grs = new int[]{42,38};
            gcs = new int[]{13,13};
        }
        if(l_name.equals("72")){
            grs = new int[]{44,44};
            gcs = new int[]{14,18};
        }
        if(l_name.equals("73")){
            grs = new int[]{45,49};
            gcs = new int[]{12,12};
        }
        if(l_name.equals("74")){
            grs = new int[]{50,50};
            gcs = new int[]{11,2};
        }
        if(l_name.equals("75")){
            grs = new int[]{51,51};
            gcs = new int[]{14,18};
        }
        if(l_name.equals("76")){
            grs = new int[]{49,45};
            gcs = new int[]{13,13};
        }
        if(l_name.equals("77")){
            grs = new int[]{21,14};
            gcs = new int[]{38,38};
        }
        if(l_name.equals("78")){
            grs = new int[]{12,12};
            gcs = new int[]{36,30};
        }
        if(l_name.equals("79")){
            grs = new int[]{14,21};
            gcs = new int[]{37,37};
        }
        if(l_name.equals("80")){
            grs = new int[]{13,13};
            gcs = new int[]{30,36};
        }
        if(l_name.equals("81")){
            grs = new int[]{11,2};
            gcs = new int[]{38,38};
        }
        if(l_name.equals("82")){
            grs = new int[]{2,11};
            gcs = new int[]{37,37};
        }
        if(l_name.equals("83")){
            grs = new int[]{0,0};
            gcs = new int[]{36,30};
        }
        if(l_name.equals("84")){
            grs = new int[]{1,1};
            gcs = new int[]{30,36};
        }
        if(grs[0]>grs[1]){
            for(int i=grs[0];i>=grs[1];i--) {
                if(gcs[0]>gcs[1]){
                    for (int j=gcs[0];j>=gcs[1]; j--) {
                        int[] drs = new int[]{i,j};
                        indxs.add(drs);
                    }
                }else{
                    for (int j=gcs[0];j<=gcs[1]; j++) {
                        int[] drs = new int[]{i,j};
                        indxs.add(drs);
                    }
                }
            }
        }else{
            for(int i=grs[0];i<=grs[1];i++) {
                if(gcs[0]>gcs[1]){
                    for (int j=gcs[0];j>=gcs[1]; j--) {
                        int[] drs = new int[]{i,j};
                        indxs.add(drs);
                    }
                }else{
                    for (int j=gcs[0];j<=gcs[1]; j++) {
                        int[] drs = new int[]{i,j};
                        indxs.add(drs);
                    }
                }
            }
        }

        int h=0;
        while (sdx <= todx) {
            ctc = (ctmCell)ctms.get(sdx);
            if(h<indxs.size()){
                int[] ss = indxs.get(h);
                ctc.setGrindex(ss[0]);
                ctc.setGcindex(ss[1]);
                ++h;

            }else{

            }
            ++sdx;
        }
        for(Integer cid : edge.getDcMap().values()){
            edc = initCtm.getEndCell(ctms,cid);
            int[] ss = indxs.get(indxs.size()-1);
            edc.setGrindex(ss[0]);
            edc.setGcindex(ss[1]);
        }
    }

}
