package transport.ctm.main;

import com.google.gson.Gson;
import org.junit.Test;
import transport.ctm.model.intersection;

import java.util.*;

public class IntTest {

    @Test
    public void Test(){
        //        List<ctm_intersection> l = new ArrayList<>();
//        fileutils.loadJson(l);
        List ol = new ArrayList();

        /*节点1*/
        intersection cint1 = new intersection();
        Map<String, List<String[]>> map1 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist11 = new ArrayList<>();
        List<String[]> olist12= new ArrayList<>();

        olist11.add(new String[]{"5", "1"});
        olist12.add(new String[]{"3", "2"});

        map1.put("0",olist11);
        map1.put("-1",olist12);

        cint1.setNlabel("1");
        cint1.setPhaseMap(map1);
        cint1.setIcells(new int[]{});
        cint1.setIntStyle("2");
        cint1.setPhases(new LinkedHashMap<>());
        cint1.setPhase(0);

        /*节点2*/
        intersection cint2 = new intersection();
        Map<String, List<String[]>> map2 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist21 = new ArrayList<>();
        List<String[]> olist22= new ArrayList<>();
        List<String[]> olist23 = new ArrayList<>();



        olist21.add(new String[]{"3", "84"});

        olist22.add(new String[]{"83", "3"});
        olist22.add(new String[]{"83", "4"});

        olist23.add(new String[]{"14", "3"});
        map2.put("0", Arrays.asList(new String[]{"1", "4"},new String[]{"14","84"}));
        map2.put("1",olist21);
        map2.put("2",olist22);
        map2.put("3",olist23);

        cint2.setNlabel("2");
        cint2.setPhaseMap(map2);
        cint2.setIcells(new int[]{});
        cint2.setIntStyle("3");
        cint2.setPhases(new LinkedHashMap<>());
        cint2.setPhase(0);

        /*节点3*/
        intersection cint3 = new intersection();
        Map<String, List<String[]>> map3 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist3 = new ArrayList<>();
        List<String[]> olist4 = new ArrayList<>();
        List<String[]> olist5 = new ArrayList<>();


        olist3.add(new String[]{"35", "5"});
        olist4.add(new String[]{"2", "7"});
        olist4.add(new String[]{"2", "6"});
        olist5.add(new String[]{"8", "7"});
        map3.put("0", Arrays.asList(new String[]{"35", "6"},new String[]{"8","5"}));
        map3.put("1",olist3);
        map3.put("2",olist4);
        map3.put("3",olist5);

        cint3.setNlabel("3");
        cint3.setPhaseMap(map3);
        cint3.setIcells(new int[]{});
        cint3.setIntStyle("3");
        cint3.setPhases(new LinkedHashMap<>());
        cint3.setPhase(0);


        /*节点4*/
        intersection cint4 = new intersection();
        Map<String, List<String[]>> map4 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist6 = new ArrayList<>();
        List<String[]> olist7 = new ArrayList<>();
        List<String[]> olist8 = new ArrayList<>();

        olist6.add(new String[]{"6", "9"});
        olist7.add(new String[]{"11","8"});
        olist7.add(new String[]{"11","10"});
        olist8.add(new String[]{"31","8"});
        map4.put("0", Arrays.asList(new String[]{"6", "10"},new String[]{"31","9"}));
        map4.put("1", olist6);
        map4.put("2", olist7);
        map4.put("3", olist8);

        cint4.setNlabel("4");
        cint4.setPhaseMap(map4);
        cint4.setIcells(new int[]{});
        cint4.setIntStyle("3");
        cint4.setPhases(new LinkedHashMap<>());
        cint4.setPhase(0);

        /*节点5*/
        intersection cint5 = new intersection();
        Map<String, List<String[]>> map5 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist50 = new ArrayList<>();
        List<String[]> olist51 = new ArrayList<>();
        List<String[]> olist52 = new ArrayList<>();

        olist50.add(new String[]{"9", "12"});
        olist51.add(new String[]{"15","11",});
        olist51.add(new String[]{"15","13"});
        olist52.add(new String[]{"23","11"});
        map5.put("0", Arrays.asList(new String[]{"9", "13"},new String[]{"23","12"}));
        map5.put("1", olist50);
        map5.put("2", olist51);
        map5.put("3", olist52);

        cint5.setNlabel("5");
        cint5.setPhaseMap(map5);
        cint5.setIcells(new int[]{});
        cint5.setIntStyle("3");
        cint5.setPhases(new LinkedHashMap<>());
        cint5.setPhase(0);


        /*节点6*/
        intersection cint6 = new intersection();
        Map<String, List<String[]>> map6 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist60 = new ArrayList<>();
        List<String[]> olist61 = new ArrayList<>();
        List<String[]> olist62 = new ArrayList<>();
        List<String[]> olist63 = new ArrayList<>();

        /*东西直行*/
        olist60.add(new String[]{"12", "80"});
        olist60.add(new String[]{"78", "15"});
        /*东西左转*/
        olist61.add(new String[]{"12", "14"});
        olist61.add(new String[]{"78", "16"});
        /*南北直行*/
        olist62.add(new String[]{"4", "16"});
        olist62.add(new String[]{"19", "14"});
        /*南北左转*/
        olist63.add(new String[]{"4", "80"});
        olist63.add(new String[]{"19", "15"});

        /*右转相位*/
        map6.put("0", Arrays.asList(new String[]{"4", "15"},new String[]{"12","16"},new String[]{"19","80"},new String[]{"78","14"}));
        map6.put("1", olist60);
        map6.put("2", olist61);
        map6.put("3", olist62);
        map6.put("4", olist63);

        cint6.setNlabel("6");
        cint6.setPhaseMap(map6);
        cint6.setIcells(new int[]{});
        cint6.setIntStyle("4");
        cint6.setPhases(new LinkedHashMap<>());
        cint6.setPhase(0);

        /*节点7*/
        intersection cint7 = new intersection();
        Map<String, List<String[]>> map7 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist71 = new ArrayList<>();
        List<String[]> olist72= new ArrayList<>();
        List<String[]> olist73= new ArrayList<>();


        //7
        olist71.add(new String[]{"79", "18"});
        olist72.add(new String[]{"54", "77"});
        olist72.add(new String[]{"54", "17"});
        olist73.add(new String[]{"20", "77"});
        // 右转
        map7.put("0", Arrays.asList(new String[]{"20", "18"},new String[]{"79","17"}));
        map7.put("1",olist71);
        map7.put("2",olist72);
        map7.put("3",olist73);

        cint7.setNlabel("7");
        cint7.setPhaseMap(map7);
        cint7.setIcells(new int[]{});
        cint7.setIntStyle("3");
        cint7.setPhases(new LinkedHashMap<>());
        cint7.setPhase(0);


        /*节点8*/
        intersection cint8 = new intersection();
        Map<String, List<String[]>> map8 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist16 = new ArrayList<>();
        List<String[]> olist17 = new ArrayList<>();
        List<String[]> olist18 = new ArrayList<>();
        List<String[]> olist19 = new ArrayList<>();

        olist16.add(new String[]{"24", "20"});
        olist16.add(new String[]{"17", "21"});

        olist17.add(new String[]{"24", "19"});
        olist17.add(new String[]{"17", "22"});

        olist18.add(new String[]{"16", "22"});
        olist18.add(new String[]{"47", "19",});

        olist19.add(new String[]{"16", "20"});
        olist19.add(new String[]{"47", "21"});

        map8.put("0", Arrays.asList(new String[]{"24", "22"},new String[]{"47","20"},new String[]{"17","19"},new String[]{"16","21"}));
        map8.put("1", olist16);
        map8.put("2", olist17);
        map8.put("3", olist18);
        map8.put("4", olist19);

        cint8.setNlabel("8");
        cint8.setPhaseMap(map8);
        cint8.setIcells(new int[]{});
        cint8.setIntStyle("4");
        cint8.setPhases(new LinkedHashMap<>());
        cint8.setPhase(0);


        /*节点9*/
        intersection cint9 = new intersection();
        Map<String, List<String[]>> map9 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist90 = new ArrayList<>();
        List<String[]> olist91 = new ArrayList<>();
        List<String[]> olist92 = new ArrayList<>();

        olist90.add(new String[]{"26", "23"});
        olist91.add(new String[]{"13", "25"});
        olist91.add(new String[]{"13", "24"});
        olist92.add(new String[]{"21", "25"});

        map9.put("0", Arrays.asList(new String[]{"26", "24"},new String[]{"21","23"}));
        map9.put("1", olist90);
        map9.put("2", olist91);
        map9.put("3", olist92);

        cint9.setNlabel("9");
        cint9.setPhaseMap(map9);
        cint9.setIcells(new int[]{});
        cint9.setIntStyle("3");
        cint9.setPhases(new LinkedHashMap<>());
        cint9.setPhase(0);


        /*节点10*/
        intersection cint10 = new intersection();
        Map<String, List<String[]>> map10 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist100 = new ArrayList<>();
        List<String[]> olist101 = new ArrayList<>();
        List<String[]> olist102= new ArrayList<>();
        List<String[]> olist103= new ArrayList<>();

        olist100.add(new String[]{"32", "29"});
        olist100.add(new String[]{"48", "27"});
        olist101.add(new String[]{"32", "26"});
        olist101.add(new String[]{"48", "28"});
        olist102.add(new String[]{"25", "28"});
        olist102.add(new String[]{"43", "26"});
        olist103.add(new String[]{"25", "29"});
        olist103.add(new String[]{"43", "27"});
        map10.put("0", Arrays.asList(new String[]{"32", "28"},new String[]{"43","29"},new String[]{"48","26"},new String[]{"25","27"}));
        map10.put("1", olist100);
        map10.put("2", olist101);
        map10.put("3", olist102);
        map10.put("4", olist103);

        cint10.setNlabel("10");
        cint10.setPhaseMap(map10);
        cint10.setIcells(new int[]{});
        cint10.setIntStyle("4");
        cint10.setPhases(new LinkedHashMap<>());
        cint10.setPhase(0);


        /*节点11*/
        intersection cint11 = new intersection();
        Map<String, List<String[]>> map11 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist27 = new ArrayList<>();
        List<String[]> olist28 = new ArrayList<>();
        List<String[]> olist29 = new ArrayList<>();
        List<String[]> olist30 = new ArrayList<>();

        olist27.add(new String[]{"36", "32"});
        olist27.add(new String[]{"27", "33"});
        olist28.add(new String[]{"36", "31"});
        olist28.add(new String[]{"27", "34"});
        olist29.add(new String[]{"10", "34"});
        olist29.add(new String[]{"40", "31"});
        olist30.add(new String[]{"10","32"});
        olist30.add(new String[]{"40", "33"});
        map11.put("0", Arrays.asList(new String[]{"36", "34"},new String[]{"40","32"},new String[]{"27","31"},new String[]{"10","33"}));
        map11.put("1", olist27);
        map11.put("2", olist28);
        map11.put("3", olist29);
        map11.put("4", olist30);

        cint11.setNlabel("11");
        cint11.setPhaseMap(map11);
        cint11.setIcells(new int[]{});
        cint11.setIntStyle("4");
        cint11.setPhases(new LinkedHashMap<>());
        cint11.setPhase(0);


        /*节点12*/
        intersection cint12 = new intersection();
        Map<String, List<String[]>> map12 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist31 = new ArrayList<>();
        List<String[]> olist32 = new ArrayList<>();
        List<String[]> olist33 = new ArrayList<>();

        olist31.add(new String[]{"38", "35"});
        olist32.add(new String[]{"7", "37"});
        olist32.add(new String[]{"7", "36"});
        olist33.add(new String[]{"33", "37"});
        map12.put("0", Arrays.asList(new String[]{"38", "26"},new String[]{"33","35"}));
        map12.put("1", olist31);
        map12.put("2", olist32);
        map12.put("3", olist33);

        cint12.setNlabel("12");
        cint12.setPhaseMap(map12);
        cint12.setIcells(new int[]{});
        cint12.setIntStyle("3");
        cint12.setPhases(new LinkedHashMap<>());
        cint12.setPhase(0);

        /*节点13*/
        intersection cint13 = new intersection();
        Map<String, List<String[]>> map13 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist130 = new ArrayList<>();
        List<String[]> olist131= new ArrayList<>();

        olist130.add(new String[]{"74", "38"});
        olist131.add(new String[]{"37", "39"});

        map13.put("0",olist130);
        map13.put("-1",olist131);

        cint13.setNlabel("13");
        cint13.setPhaseMap(map13);
        cint13.setIcells(new int[]{});
        cint13.setIntStyle("2");
        cint13.setPhases(new LinkedHashMap<>());
        cint13.setPhase(0);

        /*节点14*/
        intersection cint14 = new intersection();
        Map<String, List<String[]>> map14 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist34 = new ArrayList<>();
        List<String[]> olist35 = new ArrayList<>();
        List<String[]> olist36 = new ArrayList<>();

        olist34.add(new String[]{"71", "40"});
        olist35.add(new String[]{"34", "42"});
        olist35.add(new String[]{"34", "41"});
        olist36.add(new String[]{"44", "42"});
        map14.put("0", Arrays.asList(new String[]{"71", "41"},new String[]{"44","40"}));
        map14.put("1", olist34);
        map14.put("2", olist35);
        map14.put("3", olist36);

        cint14.setNlabel("14");
        cint14.setPhaseMap(map14);
        cint14.setIcells(new int[]{});
        cint14.setIntStyle("3");
        cint14.setPhases(new LinkedHashMap<>());
        cint14.setPhase(0);



        /*节点15*/
        intersection cint15 = new intersection();
        Map<String, List<String[]>> map15 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist37 = new ArrayList<>();
        List<String[]> olist38 = new ArrayList<>();
        List<String[]> olist39 = new ArrayList<>();
        List<String[]> olist40 = new ArrayList<>();

        olist37.add(new String[]{"41", "30"});
        olist37.add(new String[]{"51", "44"});

        olist38.add(new String[]{"41","43"});
        olist38.add(new String[]{"51", "46"});

        olist39.add(new String[]{"28", "46"});
        olist39.add(new String[]{"67", "43"});

        olist40.add(new String[]{"28", "30"});
        olist40.add(new String[]{ "67", "44"});
        map15.put("0", Arrays.asList(new String[]{"41", "46"},new String[]{"67","30"},new String[]{"51","43"},new String[]{"28","44"}));
        map15.put("1", olist37);
        map15.put("2", olist38);
        map15.put("3", olist39);
        map15.put("4", olist40);

        cint15.setNlabel("15");
        cint15.setPhaseMap(map15);
        cint15.setIcells(new int[]{});
        cint15.setIntStyle("4");
        cint15.setPhases(new LinkedHashMap<>());
        cint15.setPhase(0);


        /*节点16*/
        intersection cint16 = new intersection();
        Map<String, List<String[]>> map16 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist41 = new ArrayList<>();
        List<String[]> olist42 = new ArrayList<>();
        List<String[]> olist43 = new ArrayList<>();
        List<String[]> olist44 = new ArrayList<>();

        olist41.add(new String[]{"29", "50"});
        olist41.add(new String[]{"55", "48"});
        olist42.add(new String[]{"29", "47"});
        olist42.add(new String[]{"55", "49"});
        olist43.add(new String[]{"22", "49",});
        olist43.add(new String[]{"52", "47",});
        olist44.add(new String[]{"22", "50"});
        olist44.add(new String[]{"52", "48"});
        map16.put("0", Arrays.asList(new String[]{"29", "49"},new String[]{"52","50"},new String[]{"55","47"},new String[]{"22","48"}));
        map16.put("1", olist41);
        map16.put("2", olist42);
        map16.put("3", olist43);
        map16.put("4", olist44);

        cint16.setNlabel("16");
        cint16.setPhaseMap(map16);
        cint16.setIcells(new int[]{});
        cint16.setIntStyle("4");
        cint16.setPhases(new LinkedHashMap<>());
        cint16.setPhase(0);


        /*节点17*/
        intersection cint17 = new intersection();
        Map<String, List<String[]>> map17 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist45 = new ArrayList<>();
        List<String[]> olist46 = new ArrayList<>();
        List<String[]> olist47 = new ArrayList<>();

        olist45.add(new String[]{"49", "53"});
        olist46.add(new String[]{"58", "52"});
        olist46.add(new String[]{"58", "51"});
        olist47.add(new String[]{"30", "52"});
        map17.put("0", Arrays.asList(new String[]{"49", "51"},new String[]{"30","53"}));
        map17.put("1", olist45);
        map17.put("2", olist46);
        map17.put("3", olist47);

        cint17.setNlabel("17");
        cint17.setPhaseMap(map17);
        cint17.setIcells(new int[]{});
        cint17.setIntStyle("3");
        cint17.setPhases(new LinkedHashMap<>());
        cint17.setPhase(0);


        /*节点18*/
        intersection cint18 = new intersection();
        Map<String, List<String[]>> map18 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist180 = new ArrayList<>();
        List<String[]> olist181 = new ArrayList<>();
        List<String[]> olist182= new ArrayList<>();

        olist180.add(new String[]{"18", "56"});
        olist181.add(new String[]{"60", "54"});
        olist181.add(new String[]{"60", "55"});
        olist182.add(new String[]{"50", "54"});
        map18.put("0", Arrays.asList(new String[]{"18", "55"},new String[]{"50","56"}));
        map18.put("1", olist180);
        map18.put("2", olist181);
        map18.put("3", olist182);

        cint18.setNlabel("18");
        cint18.setPhaseMap(map18);
        cint18.setIcells(new int[]{});
        cint18.setIntStyle("3");
        cint18.setPhases(new LinkedHashMap<>());
        cint18.setPhase(0);



        /*节点19*/
        intersection cint19 = new intersection();
        Map<String, List<String[]>> map19 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist190 = new ArrayList<>();
        List<String[]> olist191 = new ArrayList<>();
        List<String[]> olist192= new ArrayList<>();

        olist190.add(new String[]{"53", "59"});
        olist191.add(new String[]{"61", "58"});
        olist191.add(new String[]{"61", "57"});
        olist192.add(new String[]{"45", "58"});
        map19.put("0", Arrays.asList(new String[]{"53", "57"},new String[]{"45","59"}));
        map19.put("1", olist190);
        map19.put("2", olist191);
        map19.put("3", olist192);

        cint19.setNlabel("19");
        cint19.setPhaseMap(map19);
        cint19.setIcells(new int[]{});
        cint19.setIntStyle("3");
        cint19.setPhases(new LinkedHashMap<>());
        cint19.setPhase(0);


        /*节点20*/
        intersection cint20 = new intersection();
        Map<String, List<String[]>> map20 = new LinkedHashMap<String, List<String[]>>();


        List<String[]> olist54 = new ArrayList<>();
        List<String[]> olist55 = new ArrayList<>();
        List<String[]> olist57 = new ArrayList<>();

        olist54.add(new String[]{"63", "62"});

        olist55.add(new String[]{"64", "68"});
        olist55.add(new String[]{"64", "61"});

        olist57.add(new String[]{"59", "68"});

        map20.put("0", Arrays.asList(new String[]{"59", "62"},new String[]{"63","61"}));
        map20.put("1", olist54);
        map20.put("2", olist55);
        map20.put("3", olist57);

        cint20.setNlabel("20");
        cint20.setPhaseMap(map20);
        cint20.setIcells(new int[]{});
        cint20.setIntStyle("3");
        cint20.setPhases(new LinkedHashMap<>());
        cint20.setPhase(0);


        /*节点21*/
        intersection cint21 = new intersection();
        Map<String, List<String[]>> map21 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist210 = new ArrayList<>();
        List<String[]> olist211 = new ArrayList<>();
        List<String[]> olist212= new ArrayList<>();

        olist210.add(new String[]{"62", "66"});

        olist211.add(new String[]{"75", "64"});
        olist211.add(new String[]{"75", "65"});

        olist212.add(new String[]{"69", "64"});
        map21.put("0", Arrays.asList(new String[]{"62", "65"},new String[]{"69","66"}));
        map21.put("1", olist210);
        map21.put("2", olist211);
        map21.put("3", olist212);

        cint21.setNlabel("21");
        cint21.setPhaseMap(map21);
        cint21.setIcells(new int[]{});
        cint21.setIntStyle("3");
        cint21.setPhases(new LinkedHashMap<>());
        cint21.setPhase(0);


        /*节点22*/
        intersection cint22 = new intersection();
        Map<String, List<String[]>> map22 = new LinkedHashMap<String, List<String[]>>();


        List<String[]> olist220 = new ArrayList<>();
        List<String[]> olist221 = new ArrayList<>();
        List<String[]> olist222= new ArrayList<>();
        List<String[]> olist223= new ArrayList<>();

        olist220.add(new String[]{"72", "45"});
        olist220.add(new String[]{"57", "70"});

        olist221.add(new String[]{"72", "67"});
        olist221.add(new String[]{"57", "69"});

        olist222.add(new String[]{"46", "69",});
        olist222.add(new String[]{"65", "67"});

        olist223.add(new String[]{"46", "45"});
        olist223.add(new String[]{"65", "70"});
        map22.put("0", Arrays.asList(new String[]{"72", "69"},new String[]{"65","45"},new String[]{"57","67"},new String[]{"46","70"}));
        map22.put("1", olist220);
        map22.put("2", olist221);
        map22.put("3", olist222);
        map22.put("4", olist223);

        cint22.setNlabel("22");
        cint22.setPhaseMap(map22);
        cint22.setIcells(new int[]{});
        cint22.setIntStyle("4");
        cint22.setPhases(new LinkedHashMap<>());
        cint22.setPhase(0);



        /*节点23*/
        intersection cint23 = new intersection();
        Map<String, List<String[]>> map23 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist65 = new ArrayList<>();
        List<String[]> olist661 = new ArrayList<>();
        List<String[]> olist67 = new ArrayList<>();

        olist65.add(new String[]{"76", "71"});
        olist661.add(new String[]{"42", "73"});
        olist661.add(new String[]{"42", "72"});
        olist67.add(new String[]{"70", "73"});
        map23.put("0", Arrays.asList(new String[]{"76", "72"},new String[]{"70","71"}));
        map23.put("1", olist65);
        map23.put("2", olist661);
        map23.put("3", olist67);

        cint23.setNlabel("23");
        cint23.setPhaseMap(map23);
        cint23.setIcells(new int[]{});
        cint23.setIntStyle("3");
        cint23.setPhases(new LinkedHashMap<>());
        cint23.setPhase(0);


        /*节点24*/
        intersection cint24 = new intersection();
        Map<String, List<String[]>> map24 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist68 = new ArrayList<>();
        List<String[]> olist69 = new ArrayList<>();
        List<String[]> olist70 = new ArrayList<>();

        olist68.add(new String[]{"66", "74"});
        olist69.add(new String[]{"39", "75"});
        olist69.add(new String[]{"39", "76"});
        olist70.add(new String[]{"73", "75"});
        map24.put("0", Arrays.asList(new String[]{"66", "76"},new String[]{"73","74"}));
        map24.put("1", olist68);
        map24.put("2", olist69);
        map24.put("3", olist70);

        cint24.setNlabel("24");
        cint24.setPhaseMap(map24);
        cint24.setIcells(new int[]{});
        cint24.setIntStyle("3");
        cint24.setPhases(new LinkedHashMap<>());
        cint24.setPhase(0);

        /*节点25*/
        intersection cint25 = new intersection();
        Map<String, List<String[]>> map25 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist250 = new ArrayList<>();
        List<String[]> olist251= new ArrayList<>();
        List<String[]> olist252= new ArrayList<>();

        olist250.add(new String[]{"56", "63"});
        olist251.add(new String[]{"68", "60"});


        map25.put("0", olist250);
        map25.put("-1", olist251);


        cint25.setNlabel("25");
        cint25.setPhaseMap(map25);
        cint25.setIcells(new int[]{});
        cint25.setIntStyle("2");
        cint25.setPhases(new LinkedHashMap<>());
        cint25.setPhase(0);

        /*节点26*/
        intersection cint26 = new intersection();
        Map<String, List<String[]>> map26 = new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist260 = new ArrayList<>();
        List<String[]> olist261= new ArrayList<>();
        List<String[]> olist262= new ArrayList<>();

        olist260.add(new String[]{"82", "79"});
        olist261.add(new String[]{"77", "81"});
        olist261.add(new String[]{"77", "78"});
        olist262.add(new String[]{"80", "81"});
        map26.put("0", Arrays.asList(new String[]{"82", "78"},new String[]{"80","79"}));
        map26.put("1", olist68);
        map26.put("2", olist69);
        map26.put("3", olist70);

        cint26.setNlabel("26");
        cint26.setPhaseMap(map26);
        cint26.setIcells(new int[]{});
        cint26.setIntStyle("3");
        cint26.setPhases(new LinkedHashMap<>());
        cint26.setPhase(0);

        /*节点27*/
        intersection cint27 = new intersection();
        Map<String, List<String[]>> map27= new LinkedHashMap<String, List<String[]>>();

        List<String[]> olist270 = new ArrayList<>();
        List<String[]> olist271= new ArrayList<>();

        olist270.add(new String[]{"84", "82"});
        olist271.add(new String[]{"81", "83"});


        map27.put("0", olist270);
        map27.put("-1", olist271);


        cint27.setNlabel("27");
        cint27.setPhaseMap(map27);
        cint27.setIcells(new int[]{});
        cint27.setIntStyle("2");
        cint27.setPhases(new LinkedHashMap<>());
        cint27.setPhase(0);

        Gson gson = new Gson();

        ol.add(cint1);
        ol.add(cint2);
        ol.add(cint3);
        ol.add(cint4);
        ol.add(cint5);
        ol.add(cint6);
        ol.add(cint7);
        ol.add(cint8);
        ol.add(cint9);
        ol.add(cint10);
        ol.add(cint11);
        ol.add(cint12);
        ol.add(cint13);
        ol.add(cint14);
        ol.add(cint15);
        ol.add(cint16);
        ol.add(cint17);
        ol.add(cint18);
        ol.add(cint19);
        ol.add(cint20);
        ol.add(cint21);
        ol.add(cint22);
        ol.add(cint23);
        ol.add(cint24);
        ol.add(cint25);
        ol.add(cint26);
        ol.add(cint27);
        System.out.println(gson.toJson(ol));


    }
}
