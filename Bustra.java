import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static java.awt.Color.*;
import static java.awt.event.KeyEvent.*;

public class Bustra extends JPanel implements KeyListener ,MouseMotionListener{
	  private static final long serialVersionUID = 1L;
	  private final static int R = 40, E = 2;
	  private final static int COLS = 6, ROWS = 5;
	  private Color[][] state;
  	private final static Color VIOLET = new Color(0x8a, 0x2b, 0xe2);
	  private Color[] colors = {RED, BLUE, GREEN, YELLOW, VIOLET, MAGENTA};
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
		    addKeyListener(this);
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
    // マウスでドラッグしたときの操作
    public void mouseDragged(MouseEvent e){
        // マウスカーソルの座標取得
        Point p = e.getPoint();
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
            }else if(p.y/R < x){ // 上へ移動
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
    }
	  public void keyPressed(KeyEvent e) {
		    int key = e.getKeyCode();
		    switch (key) {
		    case VK_SPACE:
			      toggle = !toggle;
			      if (!toggle) {
				            // パズル判定処理
			      }
			      break;
		    case VK_LEFT:
			      if (x > 0) {
				        x--;
				        if (toggle) {
					          Color tmp = state[x + 1][y];
					          state[x + 1][y] = state[x][y];
					          state[x][y] = tmp;
				        }
			      }
			      break;
		    case VK_UP:
		        if (y > 0) {
		            y--;
				        if (toggle) {
					          Color tmp = state[x][y + 1];
				            state[x][y + 1] = state[x][y];
			              state[x][y] = tmp;
		            }
			      }
			      break;
		    case VK_DOWN:
			      if (y < ROWS - 1) {
				        y++;
			          if (toggle) {
					          Color tmp = state[x][y - 1];
					          state[x][y - 1] = state[x][y];
					          state[x][y] = tmp;
				        }
		        }
			      break;
		    case VK_RIGHT:
	          if (x < COLS - 1) {
				        x++;
                if (toggle) {
				            Color tmp = state[x - 1][y];
			              state[x - 1][y] = state[x][y];
			              state[x][y] = tmp;
				        }
		        }
	          break;
		    default:
		        break;
        }
        repaint();
	  }

  	public void keyReleased(KeyEvent e) {}

	  public void keyTyped(KeyEvent e) {}

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
