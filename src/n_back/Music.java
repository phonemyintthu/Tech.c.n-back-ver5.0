package n_back;

import java.applet.Applet;
import java.applet.AudioClip;

//音を流すためのクラス
public class Music {

	private AudioClip start;//スタート音
	private AudioClip correct;//正解音
	private AudioClip incorrect;//不正解音

	Music(){
		start = Applet.newAudioClip(getClass().getResource("../../music/start.wav"));//binフォルダのmusicファイルからstart.wavを読み込む
		correct = Applet.newAudioClip(getClass().getResource("../../music/correct.wav"));///binフォルダのmusicファイルからcorrect.wavを読み込む
		incorrect = Applet.newAudioClip(getClass().getResource("../../music/incorrect.wav"));///binフォルダのmusicファイルからincorrect.wavを読み込む
		/*
		URL url = getClass().getResource("../../music/start.wav");
		URL url1 = getClass().getResource("music/start.wav");
		System.out.println(url);
		System.out.println(url1);
		*/
	}

	//スタート音再生メソッド
	public void start(){
		start.play();
	}

	//正解音再生メソッド
	public void correct(){
		correct.play();
	}

	//不正解音再生メソッド
	public void incorrect(){
		incorrect.play();
	}
}


