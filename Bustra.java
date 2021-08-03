import java.awt.Font;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import static java.awt.Color.*;
import static java.awt.event.KeyEvent.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;

import java.lang.String;
import java.util.*;

public class Bustra{
    static Sound sound;
    // bgm
    public static Clip clip = sound.createClip(new File("music/bgm.wav"));
    static FloatControl ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            // ゲーム画面
            ChangePanel4 panel = new ChangePanel4(360,700);
            // bgmの音量調整
            ctrl.setValue((float)Math.log10((float)0.5/20)*20);
            // bgmをループ再生
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        });
    }
}
// パズルの内部処理クラス
class Puzzle extends JPanel implements MouseMotionListener{
    static Sound sound;
    static ChangePanel4 panel;
    private final static int R = 60, E = 2;      // ブロックの大きさ
    private final static int COLS = 6, ROWS = 5;  // 盤面の大きさ
    private static Color[][] state;
    private final static Color VIOLET = new Color(0x8a, 0x2b, 0xe2);
    private Color[] colors = {RED, BLUE, GREEN, YELLOW, VIOLET, MAGENTA, BLACK};
    private boolean toggle = false;

    // 座標
    private int x = 0,y = 0;
    // コンボ数
    private int comb_count = 0;

    // モンスターのHP
    private int monHP = 1500;
    // 自分のHP
    private int myHP  = 500;
    // モンスターの画像
    Image img = Toolkit.getDefaultToolkit().getImage("image/honehone.png");

    // ブロックを動かしたときの音
    Clip clip_puzzle_move = sound.createClip(new File("music/puzzle_move.wav"));

    // 盤面の初期化
    Puzzle(){
        int i,j;
        state = new Color[COLS][ROWS];
        for (i = 0; i < COLS; i++) {
			      Color[] row = state[i];
			      for (j = 0; j < ROWS; j++) {
				        row[j] = colors[(int)(Math.random() * 6)];
			      }
		    }
        // スタートするときに消せるブロックが無いようにする
        while(true){
            if(!(allSerch(false))){
                refillBlock();
                break;
            }
            refillBlock();
        }
        setFocusable(true);
        addMouseMotionListener(this);
    }
    @Override
    public void paint(Graphics g) {
        Font hp   = new Font("Arial",Font.PLAIN,30); // HP表示用
        Font name = new Font("Arial",Font.PLAIN,15); // 名前表示用
        JButton endbutton = new JButton();
        int i, j;
		    for (i = 0; i < COLS; i++) {
			      Color[] row = state[i];
			      for (j = 0; j < ROWS; j++) {
				        if (x == i && y == j) {
					          if (toggle) {
						            g.setColor(BLACK);
					          } else {
						            g.setColor(LIGHT_GRAY);
					          }
				        }  else {
					          g.setColor(WHITE);
				        }
				        g.fillOval(i * R, j * R + 240, R, R);
				        Color c = row[j];
				        g.setColor(c);
				        g.fillOval(i * R + E, j * R + E + 240, R - 2 * E, R - 2 * E);
			      }
		    }
        g.drawImage(img,-50,0,300,300,this);
        g.setColor(LIGHT_GRAY);
        g.fillRect(200,40,150,80);
        g.setColor(WHITE);
        g.fillRect(200,140,150,80);
        g.setColor(BLACK);
        g.setFont(name);
        g.drawString("ホネホネくんHP",215,58);
        g.drawString("YOU",215,158);

        g.setFont(hp);
        g.drawString(String.valueOf(monHP),250,100);
        g.drawString(String.valueOf(myHP),250,200);
        if(monHP <= 0){
            g.drawString("GAME CLEAR",180,600);
        }else if(myHP <= 0){
            g.drawString("GAME OVER",180,600);
        }
    }
    // 任意のタイミングで描画する
    private void myPaint(){
        int i,j;
        Graphics g = getGraphics();
        for (i = 0; i < COLS; i++) {
			      Color[] row = state[i];
			      for (j = 0; j < ROWS; j++) {
                g.setColor(WHITE);
				        g.fillOval(i * R, j * R + 240, R, R);
				        Color c = row[j];
				        g.setColor(c);
				        g.fillOval(i * R + E, j * R + E + 240, R - 2 * E, R - 2 * E);
			      }
		    }
    }
    // 指定した秒数sleepする
    private void sleepFor(double second){
        // 秒数をミリ秒に変換
        int msecond = (int)(second * 100);
        try{
            Thread.sleep(msecond);
        }catch(InterruptedException e){}
    }
    // マウスでドラッグしたときの操作
    public void mouseDragged(MouseEvent e){
        // マウスカーソルの座標取得
        Point p = e.getPoint();
        toggle = true;
        // 配列の範囲外にカーソルがある時動作しない
        // R(円の直径)で割ることで座標を配列に使いやすい形に整形
        if(p.x/R < COLS && p.y/R - 4 < ROWS && p.y/R - 4 >= 0){
            if (p.x/R > x){ // 右へ移動
                x = p.x/R;
                sound.soundStart(clip_puzzle_move,30,0.8);
                exchange(x,x-1,y,y);
            }else if(p.x/R < x){ // 左へ移動
                x = p.x/R;
                sound.soundStart(clip_puzzle_move,30,0.8);
                exchange(x,x+1,y,y);
            }else if(p.y/R-4 > y){ // 下へ移動
                y = p.y/R - 4;
                sound.soundStart(clip_puzzle_move,30,0.8);
                exchange(x,x,y,y-1);
            }else if(p.y/R-4 < y){ // 上へ移動
                y = p.y/R - 4;
                sound.soundStart(clip_puzzle_move,30,0.8);
                exchange(x,x,y,y+1);
            }
       }
       repaint();
    }
    // マウスカーソルを動かしたときの操作
    public void mouseMoved(MouseEvent e){
        Point p = e.getPoint();
        x = p.x/R;
        y = p.y/R - 4;
        if(toggle){
            System.out.println("操作終了");
            // 消せるブロックがなくなるまで処理を行う
            while(true){
                if(!(allSerch(true))){
                    break;
                }
                // ブロックが消えて下に落ちる
                puzzleDrop();
                sleepFor(3);
                // ブロックの補充
                refillBlock();
                myPaint();
                sleepFor(3);
            }
            damege();
            repaint();
            toggle = false;
        }
        comb_count = 0;
    }
    // ブロックを入れ替える
    private void exchange(int b_x,int a_x,int b_y,int a_y){
        Color tmp = state[a_x][a_y];
        state[a_x][a_y] = state[b_x][b_y];
        state[b_x][b_y] = tmp;
    }
    // ブロックを補充
    void refillBlock(){
        int i,j;
        for(i = 0;i < COLS;i++){
            for(j = 0;j < ROWS;j++){
                // 黒いブロック(消えたブロック)の部分だけ
                if(state[i][j] == colors[6]){
                    state[i][j] = colors[(int)(Math.random() * 6)];
                }
            }
        }
    }
    // 盤面全体の探索
    private boolean allSerch(boolean mode){
        int i,j;
        // 始まるタイミングで消せるブロックをなくすため
        boolean flag = false;
        // 横方向の探索
        for(i = 0;i < COLS;i++){
            for(j = 0;j < ROWS;j++){
                // 縦横方向で同じ色でつながっているブロックの個数をカウント
                int size_connect = lineConnect(i,j,COLS-i,0);
                int up_connect   = lineConnect(i,j,ROWS-j,1);
                // 3個以上つながっていたらブロックを消す
                if(size_connect >= 3){
                    flag = true;
                    puzzleDelete(i,j,size_connect,0);
                }else if(up_connect >= 3){
                    flag = true;
                    puzzleDelete(i,j,up_connect,1);
                }
                // はじめの段階で消せるブロックを消すときはここは飛ばす
                if(mode && (size_connect >= 3 || up_connect >= 3)){
                    comb_count += 1;
                    if(comb_count > 14){comb_count = 14;}
                    myPaint();
                    Clip clip_puzzle_dis = sound.createClip(new File(getClass().getResource("music/puzzle_dis_"+String.valueOf(comb_count)+".wav")));
                    sound.soundStart(clip_puzzle_dis,500,0.8);
                    sleepFor(3);
                }
            }
        }
        return flag;
    }
    // 同じ色が3つつながっているか(０で横方向、１で縦方向)
    private int lineConnect(int cols,int rows,int cont,int mode){
        int i,j;
        int connect_count = 1;
        // 基準となるブロックの色
        Color c = state[cols][rows];
        // 黒(消えている時)終了
        if(c == colors[6]){
            return connect_count;
        }
        // 縦か横方向に探索しながら色が違ったら終了
        for(i = 1;i < cont;i++){
            if(mode == 0){
                if(c != state[cols+i][rows]) break;
            }else{
                if(c != state[cols][rows+i]) break;
            }
            connect_count += 1;
        }
        return connect_count;
    }
    // 揃ったら消す(0で横方向,1で縦方向)
    private void puzzleDelete(int cols,int rows,int cont,int mode){
        int i,j;
        for(i = 0;i < cont;i++){
            if(mode == 0){
                state[cols+i][rows] = colors[6];
            }else{
                state[cols][rows+i] = colors[6];
            }
        }
    }
    // ブロックを落とす
    // 下から探索して上にあるブロックを下の枠に移動する
    private void puzzleDrop(){
        int i,j,k;
        List<Integer> block_check = new ArrayList<>();
        for(i = 0;i < COLS;i++){
            // 黒以外のブロックの存在するインデックスを取得
            for(j = ROWS-1;j >= 0;j--){
                if(state[i][j] != colors[6]){
                    block_check.add(j);
                }
            }
            // 色付きブロックを下に落とす
            for(k = 0;k < ROWS;k++){
                if(k < block_check.size()){
                    state[i][ROWS-k-1] = state[i][block_check.get(k)];
                }else{
                    state[i][ROWS-k-1] = colors[6];
                }
            }
            // 次の列に移る前に配列を空に
            block_check.clear();
            myPaint();
            sleepFor(3);
        }
    }
    // ダメージ計算
    private void damege(){
        monHP -= comb_count * 100;
        myHP  -= 100;
    }
}
// サウンドを再生するクラス
class Sound{
    // 音楽ファイルを読みこむ
    public static Clip createClip(File path){
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){
            //ファイル形式取得
            AudioFormat af = ais.getFormat();
            //再生、停止などを行うためデータラインの情報オブジェクトを構築
            DataLine.Info dataLine = new DataLine.Info(Clip.class,af);

            Clip c = (Clip)AudioSystem.getLine(dataLine);
            c.open(ais);

            return c;

            // エラー処理
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
    }
    // 音楽を指定した時間(timeミリ秒)鳴らす
    public static void soundStart(Clip clip,int time,double volume){
        clip.start();
        FloatControl ctrl= (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float)Math.log10((float)volume / 20)*20);
        try{
            Thread.sleep(time);
        }catch(InterruptedException e){}
        clip.stop();
        clip.flush();
        clip.setFramePosition(0);
    }
}
