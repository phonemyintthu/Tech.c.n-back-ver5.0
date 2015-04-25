package n_back;


//ver17.0
//16.0でスタートボタン音の追加修正
//17.0ではゲームオーバーの後に15秒で初期画面に戻る機能を追加

import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//アプレット本体 Mainクラス
public class Main extends Applet {

	// コンボボックス、スタートボタンの為のインスタンスの準備
	private Choice ch = new Choice();
	private Button bt;

	// 各フォント表示のためのフォントクラスのインスタンスの作成
	private Font fn35 = new Font("MS ゴシック", Font.BOLD, 35);
	private Font fn20 = new Font("MS ゴシック", Font.BOLD, 20);
	private Font fn30 = new Font("MS ゴシック", Font.BOLD, 30);
	private Font fn40 = new Font("MS ゴシック", Font.BOLD, 40);
	private Font fn75 = new Font("MS ゴシック", Font.BOLD, 75);
	private Font fn45 = new Font("MS ゴシック", Font.BOLD, 45);
	private Font fn25 = new Font("MS ゴシック", Font.BOLD, 25);
	private int width; //画面幅
	private int height; //画面高さ
	private Image[] imgs; // 0～18までの数字の画像を取り込むためのImageクラスの配列
	private Image bimg; // イメージバッファー
	private Image logogame;
	private Image logoover;

	private int Nnum; // メインクラスでのN数
	private int score; // 得点
	private int[][] question; // Nbackクラスが生成する問題の一時受け取り配列
	private int[] answer; // Nbackクラスが生成する問題の答えの一時受け取り配列
	private List<Integer> rightQ = null;// 問題の右辺のArrayList
	private List<Integer> leftQ = null; // 問題の左辺のArrayList
	private List<Integer> Ans = null; // 問題の答えのArrayList
	private int current_q_num; // 現在10問中どの問題を処理しているかを示す変数
	private int clickednum; // 選択下答えを受け取る変数
	private int disp_q_num; //画面に表示する
	private int playsecond; //ゲームのプレイ時間
	private int anstimer; //回答猶予時間
	private final static int qmaxtime = 5; //最大回答時間

	private boolean startflag = false; // スタートフラグ
	private boolean continueflag = false; // 継続フラグ
	private boolean finishedflag = false; // 終了フラグ
	private boolean judgedflag = false; //問題回答済みフラグ
	private boolean judgebleflag = false; //問題回答可能フラグ
	private boolean explainflag = false; //一問目に表示する説明文のフラグ
	private Nback nb = new Nback(); // Nbackクラスのインスタンス作成
	private Music msc = new Music(); //Musicクラスのインスタンス作成
	private Results rst = new Results(); //Resultwsクラスのインスタンス作成
	private Thread gameth = null; // ゲームスレッド
	private Thread timerth = null; // タイマースレッド

	// アプレットの初期化メソッド
	public void init() {

		// N数、得点、現在処理中の問題番号、選択した答え、総回答時間、表示用問題数を示す変数の初期化
		Nnum = 1;
		score = 0;
		current_q_num = 0;
		clickednum = 0;
		playsecond = 0;
		disp_q_num = 1;

		//アプレットのサイズ取得１
		Dimension size = getSize();
		width = (int) size.getWidth();
		height = (int) size.getHeight();
		bimg = createImage(width, height);

		setLayout(null); // アプレット画面の固定レイアウトを解除

		// コンボボックスにN数の選択肢を登録、画面に追加、位置の指定、ItemListenerの登録
		for (int i = 1; i <= 9; i++) {
			ch.add("" + i + "");
		}
		add(ch);
		ch.setBounds(675, 23, 50, 20);
		ch.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent arg0) {

					Nnum = ch.getSelectedIndex() + 1;
					nb.setNnum(Nnum);


			}
		});

		// スタートボタンのインスタンスを作成、フォントの作成、位置の指定、画面に追加、ActionListenerの登録
		bt = new Button("START");
		bt.setFont(fn35);
		bt.setBounds(550, 550, 150, 50);
		add(bt);
		bt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				initGame();

				//ゲームスレッド
				gameth = new Thread(new Thread() {

					public void run() {

						//問題数が少なくなったら追加
						if (current_q_num % 17 == 0) {
							for (int j = 0; j < 3; j++) {
								question = nb.initQ(); // 問題の生成10問
								answer = nb.ans(question);
								for (int i = 0; i <= 9; i++) {
									leftQ.add(question[0][i]);
									rightQ.add(question[1][i]);
									Ans.add(answer[i]);
								}
							}
						}
							msc.start();
							playGame();
							GameStop();

							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
							ClearDisplay();
					}
				});

				//タイマースレッド
				timerth = new Thread(new Thread() {

					public void run() {

						TimerUtil playtimer = new TimerUtil();
						playtimer.start();

						while (finishedflag == false) {

							playsecond = playtimer.getSecond();
						}

						playtimer.stop();
						playtimer = null;

					}
				});

				//各スレッドの開始
				gameth.start();
				timerth.start();

			}
		});


		// Imageの配列を作成、
		imgs = new Image[19];

		// Imageインスタンスに数字の画像を読み込む、
		for (int i = 0; i <= 18; i++) {

			//imgs[i] = getImage(getCodeBase(), "../img/" + i + ".jpg");
			imgs[i] = getImage(getDocumentBase(), "../img/" + i + ".jpg");

		}

		//ロゴ画像の取り込み
		//logogame = getImage(getCodeBase(), "../logo/game.png");
		//logoover = getImage(getCodeBase(), "../logo/over.png");
		logogame = getImage(getDocumentBase(), "../logo/game.png");
		logoover = getImage(getDocumentBase(), "../logo/over.png");

		// MouseListenerを登録
		addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				int xu = 30;
				int xl = 60;
				if (y >= 350 && y <= 425) {
					if (x >= xu && x <= 75 + xu) {
						clickednum = 0;
						Judge(clickednum);
					} else if (x >= 80 + xu && x <= 155 + xu) {
						clickednum = 1;
						Judge(clickednum);
					} else if (x >= 160 + xu && x <= 235 + xu) {
						clickednum = 2;
						Judge(clickednum);
					} else if (x >= 240 + xu && x <= 315 + xu) {
						clickednum = 3;
						Judge(clickednum);
					} else if (x >= 320 + xu && x <= 395 + xu) {
						clickednum = 4;
						Judge(clickednum);
					} else if (x >= 400 + xu && x <= 475 + xu) {
						clickednum = 5;
						Judge(clickednum);
					} else if (x >= 480 + xu && x <= 555 + xu) {
						clickednum = 6;
						Judge(clickednum);
					} else if (x >= 560 + xu && x <= 635 + xu) {
						clickednum = 7;
						Judge(clickednum);
					} else if (x >= 640 + xu && x <= 715 + xu) {
						clickednum = 8;
						Judge(clickednum);
					} else if (x >= 720 + xu && x <= 795 + xu) {
						clickednum = 9;
						Judge(clickednum);
					}

				} else if (y > 450 && y <= 575) {

					if (x >= xl && x <= 75 + xl) {
						clickednum = 10;
						Judge(clickednum);
					} else if (x >= 80 + xl && x <= 155 + xl) {
						clickednum = 11;
						Judge(clickednum);
					} else if (x >= 160 + xl && x <= 235 + xl) {
						clickednum = 12;
						Judge(clickednum);
					} else if (x >= 240 + xl && x <= 315 + xl) {
						clickednum = 13;
						Judge(clickednum);
					} else if (x >= 320 + xl && x <= 395 + xl) {
						clickednum = 14;
						Judge(clickednum);
					} else if (x >= 400 + xl && x <= 475 + xl) {
						clickednum = 15;
						Judge(clickednum);
					} else if (x >= 480 + xl && x <= 555 + xl) {
						clickednum = 16;
						Judge(clickednum);
					} else if (x >= 560 + xl && x <= 635 + xl) {
						clickednum = 17;
						Judge(clickednum);
					} else if (x >= 640 + xl && x <= 715 + xl) {
						clickednum = 18;
						Judge(clickednum);
					}
				}
				repaint();
			}
		});


		rightQ = Collections.synchronizedList(new ArrayList<Integer>()); // 問題（右辺）のArrayListのインスタンスの作成
		leftQ = Collections.synchronizedList(new ArrayList<Integer>()); // 問題（左辺）のArrayListのインスタンスの作成
		Ans = Collections.synchronizedList(new ArrayList<Integer>()); // 答えのArrayListのインスタンスの作成



		//問題と答えを30個追加
		for (int j = 0; j < 3; j++) {
			question = nb.initQ(); // 問題の生成10問
			answer = nb.ans(question);// 答えの生成

			// ArrayListへ登録
			for (int i = 0; i <= 9; i++) {
				leftQ.add(question[0][i]);
				rightQ.add(question[1][i]);
				Ans.add(answer[i]);
			}
		}
		// バックグラウンドミュージック
		//msc.bgm();

	}

	// updateメソッド
	public void update(Graphics g) {
		paint(g);
	}

	// 画面描画メソッド
	public void paint(Graphics g) {

		//イメージバッファーを用意
		Graphics buffer = bimg.getGraphics();
		buffer.clearRect(0, 0, width, height);

		// 背景色を指定
		Color bgcolor = new Color(225, 225, 255);
		setBackground(bgcolor);

		// タイトル、N数の選択をを促す文章の表示
		buffer.setFont(fn35);
		buffer.drawString("Nバックゲーム", 35, 40);
		buffer.setFont(fn20);
		buffer.drawString("N数を選択してください。", 425, 40);

		// 画面に選択肢の画像を描画
		for (int i = 0; i <= 18; i++) {
			if (i <= 9) {
				buffer.drawImage(imgs[i], 30 + 80 * i, 350, 75, 75, this);
			} else {
				buffer.drawImage(imgs[i], 60 + (80 * (i % 10)), 450, 75, 75,
						this);
			}
		}

		// ゲーム開始後の問題等の表示
		if (startflag) {

			int num1 = (int) leftQ.get(current_q_num);
			int num2 = (int) rightQ.get(current_q_num);
			buffer.drawImage(imgs[num1], 175, 85, 150, 150, this);
			buffer.setFont(fn40);
			buffer.drawString("+", 405, 175);
			buffer.drawImage(imgs[num2], 500, 85, 150, 150, this);
			// g.drawString(""+playsecond, 700,100);
			buffer.setFont(fn30);
			if (judgebleflag) {
				buffer.drawString("残り：" + (qmaxtime - anstimer) + "秒", 665, 100);
			}
			buffer.drawString(disp_q_num + "問目", 50, 90);
			// buffer.drawString("" + playsecond, 700, 150);

		}

		//最初の一問目のみゲームの説明を表示
		if (explainflag) {
			buffer.drawString("この問題を" + Nnum + "問後に答えて下さい", 150, 315);
		}

		// ゲーム終了後の表示
		if (finishedflag) {
			buffer.setFont(fn75);
			buffer.drawImage(logogame ,220 ,100, 320, 77, this);
			buffer.drawImage(logoover , 320, 170, 320, 80, this);
			buffer.setFont(fn45);
			/*
			int finalscore = Nnum * (score * 10) + (score * qmaxtime) - playsecond + (Nnum * 5);
			if(finalscore < 0){
				finalscore = 0;
			}

			*/
			int finalscore = rst.score(score, Nnum, playsecond);
			buffer.drawString("SCORE:"
					+ finalscore, 400, 280);
			//System.out.println("正解数は"+ score +"");

			String comment = rst.comment(score, Nnum, playsecond);
			buffer.setFont(fn25);
			buffer.drawString(comment, 80, 320);
		}

		// デバッグ用の画面表示
		/*
		 * if(startflag && current_q_num > 1){
		 * buffer.drawString(""+leftQ.get(current_q_num - Nnum) + " + " +
		 * rightQ.get(current_q_num - Nnum), 35, 100); }
		 * buffer.drawString("" + current_q_num, 35, 70);
		 * buffer.drawString(""+Nnum, 35, 95);
		 * buffer.drawString(""+clickednum, 35, 70);
		*/
		g.drawImage(bimg, 0, 0, width, height, this);
	}

	// ゲームの初期化メソッド
	public void initGame() {

		//ゲーム開始前の初期処理
		score = 0;
		playsecond = 0;
		anstimer = 0;
		disp_q_num = 1;
		finishedflag = false; // 終了フラグをfalseに
		continueflag = false; // 継続フラグをfalseに
		judgedflag = false; //回答済みフラグをfalseに
		ch.setEnabled(false); //チョイスの非活性化
		Color btpressed = new Color(150, 150, 150);
		bt.setBackground(btpressed);
		bt.setEnabled(false); //スタートボタンの非活性化

	}

	// ゲーム開始メソッド
	public void playGame() {
		startflag = true; // 開始フラグをtrueに

		if (current_q_num % 17 == 0) {
			for (int j = 0; j < 3; j++) {
				question = nb.initQ(); // 問題の生成10問
				answer = nb.ans(question);
				for (int i = 0; i <= 9; i++) {
					leftQ.add(question[0][i]);
					rightQ.add(question[1][i]);
					Ans.add(answer[i]);
				}
			}
		}

		explainflag = true;
		// 問題をN+１回画面に表示
		repaint();

		for (int i = 0; i <= Nnum - 1; i++) {

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.getStackTrace();
			}
			explainflag = false;
			// paint(getGraphics());
			current_q_num += 1;
			disp_q_num += 1;
			repaint();
		}

		//問題回答
		anstimer = 0;
		judgebleflag = true;
		for (int i = 1; i <= 50; i++) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if ((i % 10) == 0) {
				anstimer = i / 10;
				repaint();
			}
			if (judgedflag == true) {
				break;
			}

		}
		judgebleflag = false;

		//答えを間違えるまで続く
		while (continueflag) {
			judgedflag = false;

			if (current_q_num % 17 == 0) {
				for (int j = 0; j < 3; j++) {
					question = nb.initQ(); // 問題の生成10問
					answer = nb.ans(question);
					for (int i = 0; i <= 9; i++) {
						leftQ.add(question[0][i]);
						rightQ.add(question[1][i]);
						Ans.add(answer[i]);
					}
				}
			}
			// paint(getGraphics());
			repaint();

			anstimer = 0;
			judgebleflag = true;
			for (int i = 1; i <= 50; i++) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.getStackTrace();
				}
				// paint(getGraphics());
				repaint();
				if ((i % 10) == 0) {
					anstimer = i / 10;
				}
				if (judgedflag == true) {
					break;
				}
				if (i == 50 && judgedflag == false) {
					continueflag = false;
					break;
				}
			}
			judgebleflag = false;

		}

	}

	//ゲーム終了メソッド
	public void GameStop() {

		finishedflag = true;
		startflag = false;
		judgedflag = false;
		gameth = null;
		timerth = null;
		ch.setEnabled(true);
		Color defa = new Color(240,240,240);
		bt.setBackground(defa);
		bt.setEnabled(true);
		repaint();
	}

	//正誤判定メソッド
	public void Judge(int q) {
		if (judgebleflag) {
			if (q == (Ans.get(current_q_num - Nnum))) {
				msc.correct();
				continueflag = true;
				judgedflag = true;
				score++;
				current_q_num++;
				disp_q_num++;
			} else {
				msc.incorrect();
				continueflag = false;
				judgedflag = true;
			}

		}
	}

	public void ClearDisplay(){
		startflag = false; // スタートフラグ
		continueflag = false; // 継続フラグ
		finishedflag = false; // 終了フラグ
		judgedflag = false; //問題回答済みフラグ
		judgebleflag = false; //問題回答可能フラグ
		explainflag = false; //一問目に表示する説明文のフラグ
		repaint();


	}
}
