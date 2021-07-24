import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static java.awt.Color.*;
import static java.awt.event.KeyEvent.*;

public class Bustra extends JPanel implements MouseMotionListener{
	  private static final long serialVersionUID = 1L;
	  private final static int R = 40, E = 2;
	  private final static int COLS = 6, ROWS = 5;
	  private Color[][] state;
  	private final static Color VIOLET = new Color(0x8a, 0x2b, 0xe2);
	  private Color[] colors = {RED, BLUE, GREEN, YELLOW, VIOLET, MAGENTA, BLACK};
	  private boolean toggle = false;

	  private int x = 0, y = 0;

	  public Bustra() {
		    int i, j;

		    setPreferredSize(new Dimension(240, 320));
		    state = new Color[COLS][ROWS];
		    for (i = 0; i < COLS; i++) {
			      Color[] row = state[i];
			      for (j = 0; j < ROWS; j++) {
				        row[j] = colors[(int)(Math.random() * 6)];
			      }
		    }
		    setFocusable(true);
        addMouseMotionListener(this);
    }

	  @Override
	  public void paint(Graphics g) {
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
				        g.fillOval(i * R, j * R, R, R);
				        Color c = row[j];
				        g.setColor(c);
				        g.fillOval(i * R + E, j * R + E, R - 2 * E, R - 2 * E);
			      }
		    }
		    g.setColor(BLACK);
		    g.drawString("←, ↑, ↓, →: move position", 20, ROWS * R + 25);
		    g.drawString("<SPACE>: toggle exchange",  20, ROWS * R + 40);
    }
    // 任意のタイミングで描画する
    public void myPaint(){
        Graphics g = getGraphics();
        for(int i = 0;i < COLS;i++){
            for(int j = 0;j < ROWS;j++){
                g.setColor(WHITE);
                g.fillOval(i * R,j * R,R,R);
                g.setColor(state[i][j]);
                g.fillOval(i * R + E,j * R + E,R - 2 * E,R - 2 * E);
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
        if(p.x/R < COLS && p.y/R < ROWS){
            if (p.x/R > x){ // 右へ移動
                x = p.x/R;
                Color tmp = state[x - 1][y];
                state[x - 1][y] = state[x][y];
                state[x][y] = tmp;
            }else if(p.x/R < x){ // 左へ移動
                x = p.x/R;
                Color tmp = state[x + 1][y];
                state[x + 1][y] = state[x][y];
                state[x][y] = tmp;
            }else if(p.y/R > y){ // 下へ移動
                y = p.y/R;
                Color tmp = state[x][y - 1];
                state[x][y - 1] = state[x][y];
                state[x][y] = tmp;
            }else if(p.y/R < y){ // 上へ移動
                y = p.y/R;
                Color tmp = state[x][y + 1];
                state[x][y + 1] = state[x][y];
                state[x][y] = tmp;
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
            allSerch();
            puzzledrop();
            refillBlock();
            repaint();
            toggle = false;
        }

    }
    // 同じ色が3つつながっているか(横方向)
    public boolean lineSideThree(int cols,int rows,int cont){
        // 基準となるブロックの色
        Color c = state[cols][rows];
        // 横方向に探索しながら色が違ったら終了
        // contを変更すると4,5つつながった場合に対応できる
        for(int i = 1;i < cont;i++){
            if(c != state[cols+i][rows]){
                return false;
            }
        }
        return true;
    }

    // 同じ色が3つつながっているか(縦方向)
    public boolean lineVerticalThree(int cols,int rows,int cont){
        // 基準となるブロックの色
        Color c = state[cols][rows];
        // 横方向に探索しながら色が違ったら終了
        // contを変更すると4,5つつながった場合に対応できる
        for(int i = 1;i < cont;i++){
            if(c != state[cols][rows+i]){
                return false;
            }
        }
        return true;
    }

    // 全体の探索
    public void allSerch(){
        // 横方向の探索
        for(int i = 0;i < COLS-2;i++){
            for(int j = 0;j < ROWS;j++){
                if(lineSideThree(i,j,3)){
                    puzzleDelete(i,j,3,0);
                    myPaint();
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){}
                }
            }
        }
        // 縦方向の探索
        for(int i = 0;i < COLS;i++){
            for(int j = 0;j < ROWS-2;j++){
                if(lineVerticalThree(i,j,3)){
                    puzzleDelete(i,j,3,1);
                    myPaint();
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){}
                }
            }
        }
    }
    // 揃ったら消す(0で横方向,1で縦方向)
    public void puzzleDelete(int cols,int rows,int cont,int mode){
        for(int i = 0;i < cont;i++){
            if(mode == 0){
                state[cols+i][rows] = colors[6];
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
                    myPaint();
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){}
                }
            }
        }
    }
    // 上にあるブロックを下に移動する
    public void moveDownBlock(int cols,int rows){
        // 黒だと判定されたブロックから１番上のブロックまでを探索
        for(int i = rows-1;i >= 0;i--){
            // 黒以外が見つかったら黒のブロックと置き換える
            if(state[cols][i] != colors[6]){
                state[cols][rows] = state[cols][i];
                state[cols][i] = colors[6];
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
	  public static void main(String[] args) {
		    SwingUtilities.invokeLater(() -> {
			  /* タイトルバーに表示する文字列を指定できる */
			  JFrame frame = new JFrame("Bustra!");

			  frame.add(new Bustra());
			  frame.pack();
			  frame.setVisible(true);

			  /* ×ボタンを押したときの動作を指定する */
			  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    });
	  }
}
