package transport.fsdf;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


/**
 * 程序入口
 */
public class TestDrawLine {
    public static void main(String[] args) {
        new DrawSee();
    }
}

class DrawSee extends JFrame {
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private static final int sx = 22;//小方格宽度
    private static final int sy = 65;//小方格高度
    private static final int w = 8;
    private static final int rw = 416;
    private static JPanel p, p1, p2;
    private int[][] map;
    private Graphics jg;


    private Color rectColor = new Color(0xf5f5f5);

    /**
     * DrawSee构造方法
     */
    public DrawSee() {

        p1 = new JPanel();
        btn1 = new JButton("运行");
        btn2 = new JButton("暂停");
        btn3 = new JButton("345");
        p1.add(btn1);
        p1.add(btn2);
        p1.add(btn3);
        p1.setBackground(Color.white);

        Container p = getContentPane();
        p.add(p1, BorderLayout.CENTER);
        setBounds(150, 150, 382, 486);
        setVisible(true);
        p.setBackground(rectColor);
        p.setBackground(Color.white);
        setLayout(new BorderLayout());
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Thread.sleep(500);
        } catch (Exception e) { e.printStackTrace(); }

        // 获取专门用于在窗口界面上绘图的对象
        jg = this.getGraphics();
        // 绘制游戏区域
        paintComponents(jg);

    }


    public void paintComponents(Graphics g) {
        try {

            // 设置线条颜色为红色
            g.setColor(Color.black);

            // 绘制外层矩形框
            g.drawRect(sx, sy, 312, 416);

            /* 绘制水平10个，垂直10个方格。
             * 即水平方向9条线，垂直方向9条线，
             * 外围四周4条线已经画过了，不需要再画。
             * 同时内部64个方格填写数字。
             */
            g.drawLine(20, 60, 337, 60);

            for (int i = 1; i < 2; i++) {
                // 绘制第i条竖直线
                g.drawLine(sx + (i * w), sy+2*w, sx + (i * w), sy + rw);
                g.setFont(new Font("Arial", 0, 18));
                g.drawString("1",25,80);
            }
            for (int i = 2; i < 39; i++) {
                // 绘制第i条竖直线
                g.drawLine(sx + (i * w), sy, sx + (i * w), sy + rw);

            }
            for (int i = 1; i < 2; i++) {
                // 绘制第i条水平线
                g.drawLine(sx+2*w, sy + (i * w), sx + 312, sy + (i * w));
            }
            for (int i = 2; i < 52; i++) {
                // 绘制第i条水平线
                g.drawLine(sx, sy + (i * w), sx + 312, sy + (i * w));
                // 填写第i行从第1个方格到第8个方格里面的数字（方格序号从0开始）
                for (int j = 0; j < 10; j++) {
                    //drawString(g, j, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void drawString(Graphics g, int x, int y) {
                 // 为0就不显示
                 if(map[x][y] != 0) {
                         g.setColor(Color.RED);// Graphics对象颜色在之前又被修改过，所以每次需要将颜色重新设置为红色
                         g.setFont(new Font("Arial", 0, 40));// 设置Graphics对象字体大小
                         g.drawString(map[x][y] + "", sx + (y  * w) + 5, sy + ((x + 1) * w) - 5);// 绘制字符
                     }
             }
}
