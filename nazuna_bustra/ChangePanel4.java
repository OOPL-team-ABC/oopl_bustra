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
    Boolean doneStart = false;
    Boolean doneEnter = false;
    Boolean doneGuide = false;
    int width, height;
    JFrame frame;

    String name;

    public ChangePanel4(int w, int h){
        width = w;
        height = h;

        frame = new JFrame("Bustra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);

        JPanel startPanel = new JPanel();
        JPanel enterName = new JPanel();
        JPanel guidePanel = new JPanel();

        //startPanel
        
        startPanel.setPreferredSize(new Dimension(width, height));
       
        JButton startButton = new JButton("START!");
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                doneStart = true;
                System.out.println(doneStart);

                startPanel.setVisible(false);
                enterName.setVisible(true);
                frame.remove(startPanel);
                frame.getContentPane().add(enterName);
            }
        });

      
        startPanel.setLayout(new BorderLayout());
        startPanel.add("Center",startButton);
        startPanel.setVisible(true);
        frame.getContentPane().add(startPanel);


        //enterName
        enterName.setPreferredSize(new Dimension(width, height));
        
        enterName.setLayout(null);
        
        JLabel tellMe = new JLabel("Tell me your name!");
        tellMe.setBounds(120,100,200,100);
        enterName.add(tellMe);

        JTextField nameField =   new JTextField();
        nameField.setBounds(30,180,320,50);
        enterName.add(nameField);
        
        JButton okButton = new JButton("OK!");
        okButton.setBounds(160,230,50,50);
        enterName.add(okButton);
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                doneEnter = true;
                System.out.println(doneEnter);

                name = nameField.getText();

                enterName.setVisible(false);
                guidePanel.setVisible(true);
                frame.remove(enterName);
                frame.getContentPane().add(guidePanel);
            }
        });

        // enterName.setLayout(new BoxLayout(enterName, BoxLayout.Y_AXIS));
        nameField.setPreferredSize(new Dimension(200, 35));

        enterName.add(tellMe);
        enterName.add(nameField);
        enterName.add(okButton);
        enterName.setVisible(false);
        // frame.getContentPane().add(enterName);

        //guidePanel
        JLabel rule = new JLabel("ルールを説明します。");

        JLabel l = new JLabel("<html>Hello World!<br/>blahblahblah</html>", SwingConstants.CENTER);
        guidePanel.add(l);
        //l.setFont(new Font("Rockwell", Font.PLAIN, 30));
        
        guidePanel.setPreferredSize(new Dimension(width, height));

        JButton startGame = new JButton("OK!");
        startGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JPanel bustraPanel = new Bustra();
                bustraPanel.setLayout(null);
                bustraPanel.setBounds(0,0,360, 600);


                JLabel yourName = new JLabel(name);
                yourName.setFont(new Font("Rockwell", Font.PLAIN, 30));


                //JLabel mon = new JLabel(Integer.toString(Bustra.monHp));
                //mon.setFont(new Font("Rockwell", Font.PLAIN, 30));

                // //モンスターのイメージを配置
                //Image monster = ImageIO.read(Paths.get("bin/honehone.png").toFile());
                //Image monster = ImageIO.read(new File("bin/honehone.png"));
                //JLabel monsterImg = new JLabel(new ImageIcon(monster));
                //frame.add(monsterImg);

                //Image wPic = ImageIO.read(this.getClass().getResource("honehone.png"));
                // JLabel wIcon = new JLabel(new ImageIcon(wPic));
                // frame.add(wIcon);

                

                

//                 BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
// JLabel picLabel = new JLabel(new ImageIcon(myPicture));
// add(picLabel);

                
                frame.add(bustraPanel);   
                frame.add(yourName);  
                //frame.add(mon);     
                frame.setVisible(true);


                guidePanel.setVisible(false);       
            }
        });



        guidePanel.add(rule);
        guidePanel.add(startGame);
        guidePanel.setVisible(false);
    }


    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ChangePanel4 panel = new ChangePanel4(300, 500);
        });
    }

}
