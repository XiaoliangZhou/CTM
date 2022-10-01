package transport.ctm.main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {



    public static void main(String[] args) {
        JFrame jf = new JFrame("测试窗口");
        jf.setLocation(100,50);
        jf.setSize(332,445);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GridBagLayout gridBag = new GridBagLayout();    // 布局管理器
        GridBagConstraints c = null;                    // 约束

        JPanel panel = new JPanel(gridBag);

        JLabel btn01 = new JLabel("1");
        btn01.setBorder(BorderFactory.createLineBorder(Color.black));
        btn01.setPreferredSize(new Dimension(15,15));
        JLabel btn02 = new JLabel("2");
        btn02.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel btn03 = new JLabel("3");
        btn03.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel btn011 = new JLabel("11");
        btn011.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel btn021 = new JLabel("21");
        btn021.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel btn031 = new JLabel("31");
        btn031.setBorder(BorderFactory.createLineBorder(Color.black));
        btn031.setSize(20,20);
        JLabel btn04 = new JLabel("04");
        JLabel btn05 = new JLabel("05");
        JLabel btn06 = new JLabel("06");
        JLabel btn07 = new JLabel("07");
        JLabel btn08 = new JLabel("08");
        JLabel btn09 = new JLabel("09");
        JLabel btn10 = new JLabel("10");

        /* 添加 组件 和 约束 到 布局管理器 */
        // label1
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn01, c); // 内部使用的仅是 c 的副本

        // Button02
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn02, c);

        // Button03
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn03, c);

        // Button03
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn011, c);

        // Button03
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn031, c);
        // Button03
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn021, c);



        // Button04 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn04, c);

        // Button05 显示区域独占一行（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn05, c);

        // Button06 显示区域占到当前尾倒车第二个单元格（下一个组件后需要手动换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn06, c);

        // Button07 放置在当前行最后一个单元格（换行）
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn07, c);

        // Button08 显示区域占两列，组件填充显示区域
        c = new GridBagConstraints();
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn08, c);

        // Button09 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn09, c);

        // Button10 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        gridBag.addLayoutComponent(btn10, c);

        /* 添加 组件 到 内容面板 */
        panel.add(btn01);
        panel.add(btn02);
        panel.add(btn03);
        panel.add(btn011);
        panel.add(btn021);
        panel.add(btn031);

        panel.add(btn04);
        panel.add(btn05);
        panel.add(btn06);
        panel.add(btn07);
        panel.add(btn08);
        panel.add(btn09);
        panel.add(btn10);

        jf.setContentPane(panel);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}