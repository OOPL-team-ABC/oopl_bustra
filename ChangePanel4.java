import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import javax.imageio.ImageIO;
import java.io.File;

public class ChangePanel4 extends JFrame{
    static JFrame frame;
    // ユーザーネーム用
    String name = "yourname";

    static int width;
    static int height;

    JPanel startPanel  = new JPanel();
    JPanel enterName   = new JPanel();
    JPanel guidePanel  = new JPanel();
    JPanel bustraPanel = new Puzzle();

    public ChangePanel4(int w, int h){
        width  = w;
        height = h;
        // 全体の大きさと名前
        frame = new JFrame("Bustra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        // 各パネルの大きさ
        startPanel.setPreferredSize(new Dimension(width, height));
        enterName.setPreferredSize(new Dimension(width, height));
        guidePanel.setPreferredSize(new Dimension(width, height));
        bustraPanel.setPreferredSize(new Dimension(width, height));

        // スタート画面
        JButton startButton = new JButton("START!");
        startPanel.setLayout(new BorderLayout());
        startPanel.add("Center",startButton);
        startPanel.setVisible(true);
        frame.getContentPane().add(startPanel);
        // スタートボタンを押したときの処理
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // setVisisbleでfalseとtrueで切り替えて画面遷移
                startPanel.setVisible(false);
                enterName.setVisible(true);
                frame.remove(startPanel);
                frame.getContentPane().add(enterName);
            }
        });

        // 名前入力画面
        // 使用するパーツ
        JLabel tellMe = new JLabel("名前を教えて下さい！");
        JTextField nameField =   new JTextField();
        JButton okButton = new JButton("OK!");
        // レイアウトを座標で指定できる
        enterName.setLayout(null);
        // 各パーツのセットする座標と大きさ
        tellMe.setBounds(110,130,200,100);
        nameField.setBounds(30,230,300,50);
        okButton.setBounds(140,320,80,50);
        // 追加
        enterName.add(tellMe);
        enterName.add(nameField);
        enterName.add(okButton);
        // okボタンを押したときの処理
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // 入力された名前を取得
                name = nameField.getText();
                enterName.setVisible(false);
                guidePanel.setVisible(true);
                frame.remove(enterName);
                frame.getContentPane().add(guidePanel);
            }
        });

        // ルール説明画面
        // 使用するパーツ
        JLabel rule = new JLabel("ルールを説明");
        JLabel l = new JLabel("同じ色のボールを３つ以上揃えてください。", SwingConstants.CENTER);
        JLabel l2 = new JLabel("ホネホネくんを倒せばあなたの勝ちです。", SwingConstants.CENTER);
        JLabel l3 = new JLabel("5回以内に倒さないと、負けちゃいます。", SwingConstants.CENTER);
        JButton startGame = new JButton("START GAME!");
        // 文字のフォント
        rule.setFont(new Font("Ariel", Font.PLAIN, 25));
        // レイアウトを座標で指定できる
        guidePanel.setLayout(null);
        // 各パーツのセットする座標と大きさ
        rule.setBounds(110, 100, 300, 200);
        l.setBounds(30, 140, 300, 200);
        l2.setBounds(30, 158, 300, 200);
        l3.setBounds(30, 175, 300, 200);
        startGame.setBounds(120,320,100,50);
        // 追加
        guidePanel.add(rule);
        guidePanel.add(l);
        guidePanel.add(l2);
        guidePanel.add(l3);
        guidePanel.add(startGame);
        // startボタンを押したときの処理
        startGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JLabel yourName = new JLabel(name);

                yourName.setFont(new Font("Arial", Font.PLAIN, 30));

                frame.add("Center",bustraPanel);
                frame.add("South",yourName);

                guidePanel.setVisible(false);
                bustraPanel.setVisible(true);
            }
        });
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ChangePanel4 panel = new ChangePanel4(360, 650);
        });
    }
}
