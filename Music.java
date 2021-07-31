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


public class Music{
    public static void main(String[] args){
        Clip clip = createClip(newa File("sample.wav"));
    }

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
}