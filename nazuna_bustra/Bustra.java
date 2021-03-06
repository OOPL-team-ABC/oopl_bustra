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

public class Bustra extends JPanel implements MouseMotionListener{
	  private static final long serialVersionUID = 1L;
	  private final static int R = 60, E = 2;
	  private final static int COLS = 6, ROWS = 5;
	  private Color[][] state;
  	private final static Color VIOLET = new Color(0x8a, 0x2b, 0xe2);
	  private Color[] colors = {RED, BLUE, GREEN, YELLOW, VIOLET, MAGENTA, BLACK};
	  private boolean toggle = false;

    //音声
    static Clip clip_bgm        = createClip(new File("music/bgm.wav"));
    static Clip clip_puzzle_move= createClip(new File("music/puzzle_move.wav"));
    static FloatControl ctrl_bgm        = (FloatControl)clip_bgm.getControl(FloatControl.Type.MASTER_GAIN);

    // 座標
    private int x = 0, y = 0;
    // コンボ数
    private int comb_count = 0;

    // モンスターのHP
    public static int monHp = 1500;
    // 自分のHP
    public static int myHP = 500;
    // モンスターの画像
    Image img = Toolkit.getDefaultToolkit().getImage("bin/honehone1.png");
	  public Bustra() {
		    int i, j;

		    // setPreferredSize(new Dimension(240,320)); //320,520
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
		    int i, j;
        Font fm      = new Font("Serif",Font.PLAIN,50); //You表示用
        Font fm_name = new Font("Serif",Font.PLAIN,30); //名前表示用
		    for (i = 0; i < COLS; i++) {
			      Color[] row = state[i];
			      for (j = 0; j < ROWS; j++) {
				        if (x == i && y-4 == j) {
					          if (toggle) {
						            g.setColor(BLACK);
					          } else {
						            g.setColor(LIGHT_GRAY);
					          }
				        }  else {
					          g.setColor(WHITE);
				        }
				        g.fillOval(i * R, j * R + 240, R, R);
				        //g.fillOval(i * R, j * R, R, R);
				        Color c = row[j];
				        g.setColor(c);
				        g.fillOval(i * R + E, j * R + E + 240, R - 2 * E, R - 2 * E);
				        //g.fillOval(i * R + E, j * R + E, R - 2 * E, R - 2 * E);
			      }
		    }
        // プレイヤーネーム表示
        // g.setColor(BLACK);                       // 文字の色
        // g.setFont(fm);                           // フォント
        // g.drawString("You", 20, 420);            // 文字,x座標,y座標の順に指定
        g.drawImage(img,0,0,200,200,this);
        g.setColor(LIGHT_GRAY);
        g.fillRect(250,30,100,100);
        g.setColor(BLACK);
        g.setFont(fm_name);
        g.drawString("自分のHP",250,60);
        g.drawString(String.valueOf(monHp),250,30);
    }
    // 任意のタイミングで描画する
    public void myPaint(){
        Graphics g = getGraphics();
        for(int i = 0;i < COLS;i++){
            for(int j = 0;j < ROWS;j++){
                g.setColor(WHITE);
                g.fillOval(i * R,j * R+240,R,R);
                //g.fillOval(i * R,j * R,R,R);
                g.setColor(state[i][j]);
                g.fillOval(i * R + E,j * R + E + 240,R - 2 * E,R - 2 * E);
                //g.fillOval(i * R + E,j * R + E,R - 2 * E,R - 2 * E);
            }
        }
    }
    // マウスでドラッグしたときの操作
    public void mouseDragged(MouseEvent e){
        // マウスカーソルの座標取得
        Point p = e.getPoint();
        toggle = true;
        // 配列の範囲外にカーソルがある時動作しない
        // R(円の直径)で割ることで座標を配列に使いやすい形に整形
        if(p.x/R < COLS && p.y/R < ROWS+4){
           if (p.x/R > x){ // 右へ移動
                soundStart(clip_puzzle_move,30);
                x = p.x/R;
                Color tmp = state[x - 1][y-4];
                state[x - 1][y-4] = state[x][y-4];
                state[x][y-4] = tmp;
            }else if(p.x/R < x){ // 左へ移動
                soundStart(clip_puzzle_move,30);
                x = p.x/R;
                Color tmp = state[x + 1][y-4];
                state[x + 1][y-4] = state[x][y-4];
                state[x][y-4] = tmp;
            }else if(p.y/R > y){ // 下へ移動
                soundStart(clip_puzzle_move,30);
                y = p.y/R;
                Color tmp = state[x][y - 4];
                state[x][y - 4] = state[x][y-4];
                state[x][y-4] = tmp;
            }else if(p.y/R < y){ // 上へ移動
                soundStart(clip_puzzle_move,30);
                y = p.y/R;
                Color tmp = state[x][y - 3];
                state[x][y - 3] = state[x][y-4];
                state[x][y-4] = tmp;
            }
        }
        repaint();
    }
    // マウスカーソルを動かしたときの操作
    public void mouseMoved(MouseEvent e){
        Point p = e.getPoint();
        x = p.x/R;
        y = p.y/R;
        if(toggle){
            System.out.println("操作終了");
            // 処理
            while(true){
                if(!(allSerch(true))){
                    break;
                }
                puzzledrop();
                try{
                    Thread.sleep(300);
                }catch(InterruptedException err){}
                refillBlock();
            }
            repaint();
            toggle = false;
        }
        comb_count = 0;
    }
    // 同じ色が3つつながっているか(０で横方向、１で縦方向)
    public boolean lineConnectThree(int cols,int rows,int cont,int mode){
        // 基準となるブロックの色
        Color c = state[cols][rows];
        // 黒(消えている時)終了
        if(c == colors[6]){
            return false;
        }
        // 横方向に探索しながら色が違ったら終了
        // contを変更すると4,5つつながった場合に対応できる(未実装)
        for(int i = 1;i < cont;i++){
            if(mode == 0){
                if(c != state[cols+i][rows]){
                    return false;
                }
            }else{
                if(c != state[cols][rows+i]){
                    return false;
                }
            }
        }
        return true;
    }
    // 全体の探索
    public boolean allSerch(boolean mode){
        // 始まるタイミングで消せるブロックをなくすため
        boolean flag = false;
        // 横方向の探索
        for(int i = 0;i < COLS-2;i++){
            for(int j = 0;j < ROWS;j++){
                if(lineConnectThree(i,j,3,0)){
                    flag =true;
                    puzzleDelete(i,j,3,0);
                    if(mode){
                        comb_count += 1;
                        if(comb_count > 14){comb_count = 14;}
                        myPaint();
                        Clip clip_puzzle_dis = createClip(new File("music/puzzle_dis_"+String.valueOf(comb_count)+".wav"));
                        soundStart(clip_puzzle_dis,500);
                        try{
                            Thread.sleep(300);
                        }catch(InterruptedException e){}
                    }
                }
            }
        }
        // 縦方向の探索
        for(int i = 0;i < COLS;i++){
            for(int j = 0;j < ROWS-2;j++){
                if(lineConnectThree(i,j,3,1)){
                    flag = true;
                    puzzleDelete(i,j,3,1);
                    if(mode){
                        comb_count += 1;
                        if(comb_count > 14){comb_count = 14;}
                        myPaint();
                        Clip clip_puzzle_dis = createClip(new File("music/puzzle_dis_"+String.valueOf(comb_count)+".wav"));
                        soundStart(clip_puzzle_dis,500);
                        try{
                            Thread.sleep(300);
                        }catch(InterruptedException e){}
                    }
                }
            }
        }
        return flag;
    }

public String monGetter(){
    return Integer.toString(monHp);
}

    // 揃ったら消す(0で横方向,1で縦方向)
    public void puzzleDelete(int cols,int rows,int cont,int mode){
        for(int i = 0;i < cont;i++){
            if(mode == 0){
                state[cols+i][rows] = colors[6];

                // 揃った時にHPを-100にする
                monHp -= 100;
                System.out.println(monHp);

                JLabel monmon = new JLabel(Integer.toString(monHp));
                add(monmon);
                

            }else{
                state[cols][rows+i] = colors[6];
            }
        }
    }
    // ブロックを落とす
    // 下から探索して上にあるブロックを下の枠に移動する
    public void puzzledrop(){
        for(int i = 0;i < COLS;i++){
            for(int j = ROWS-1;j > 0;j--){
                // 黒(消えたパズル)なら置き換える
                if(state[i][j] == colors[6]){
                    moveDownBlock(i,j);
                }
            }
            myPaint();
            try{
                Thread.sleep(300);
            }catch(InterruptedException e){}
        }
    }
    // 上にあるブロックを下に移動する
    public void moveDownBlock(int cols,int rows){
        // 黒だと判定されたブロックから１番上のブロックまでを探索
        for(int i = rows-1;i >= 0;i--){
            // 黒以外が見つかったら黒のブロックと置き換える
            if(state[cols][i] != colors[6]){
                for(int j = i;j < rows;j++){
                    state[cols][j+1] = state[cols][j];
                    state[cols][j] = colors[6];
                }
                break;
            }
        }
    }

    // ブロックを補充する
    public void refillBlock(){
        for(int i = 0;i < COLS;i++){
            for(int j = 0;j < ROWS;j++){
                if(state[i][j] == colors[6]){
                    state[i][j] = colors[(int)(Math.random() * 6)];
                }
            }
        }
    }
    // 音楽ファイルを読み込む
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
    public void soundStart(Clip clip,int time){
        clip.start();
        FloatControl ctrl= (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float)Math.log10((float)0.8 / 20)*20);
        try{
            Thread.sleep(time);
        }catch(InterruptedException e){}
        clip.stop();
        clip.flush();
        clip.setFramePosition(0);
    }

	  public static void main(String[] args) {
		    SwingUtilities.invokeLater(() -> {
                ChangePanel4 panel = new ChangePanel4(360, 650);



			  /* タイトルバーに表示する文字列を指定できる */

              
            // bgmの音量調整
            ctrl_bgm.setValue((float)Math.log10((float)0.5 / 20)*20);

			  
            // bgm再生
            clip_bgm.loop(Clip.LOOP_CONTINUOUSLY);
           		    });
	  }
}
