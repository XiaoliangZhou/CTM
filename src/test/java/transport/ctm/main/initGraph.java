package transport.ctm.main;

import org.junit.Test;


public class initGraph {

    @Test
    public void Test1(){
        char grade = 's';
        char banji = 'f';

        switch(grade)
        {
            case 'A' :
                switch (banji){
                    case 'h':
                        System.out.println("优秀"+"1");
                        break;
                    case 'j':
                        System.out.println("优秀"+"2");
                        break;
                    case 'k':
                        System.out.println("优秀"+"3");
                        break;
                    default :
                        System.out.println("未知等级");
                }
                break;
            case 'B' :
            case 'C' :
                switch (banji){
                    case 'h':
                        System.out.println("良好"+"1");
                        break;
                    case 'j':
                        System.out.println("良好"+"2");
                        break;
                    case 'k':
                        System.out.println("良好"+"3");
                        break;
                    default :
                        System.out.println("未知等级2");
                }
                break;
            case 'D' :
                switch (banji){
                    case 'h':
                        System.out.println("及格"+"1");
                        break;
                    case 'j':
                        System.out.println("及格"+"2");
                        break;
                    case 'k':
                        System.out.println("及格"+"3");
                        break;
                    default :
                        System.out.println("未知等级3");
                }
                break;
                default :
                System.out.println("未知等级");
        }
        System.out.println("你的等级是 " + grade);

    }

}
