package n_back;

import java.util.Random;

public class Nback {
	
	private int nNum = 0;//NbackクラスでのN数
	
	Nback(){
		this.nNum = 0;
	}

	//選択したＮ数のゲッター
	public int getNnum(){
		return nNum;
	}
	
	//選択したＮ数のセッター
	void setNnum(int n){
		this.nNum = n;
	}
	
	//問題の生成メソッド
	public int[][] initQ(){
		int mondai[][] = new int[2][10];//2次元配列を用意
		Random m = new Random();
		
		for(int i=0; i<10; i++){
		int m1 = m.nextInt(10);//左辺を10個生成
		int m2 = m.nextInt(10);//右辺を10個生成
		
		mondai[0][i] = m1;
		mondai[1][i] = m2;
				}
	return mondai;
	}
	
	//解答作成メソッド
	public int[] ans(int[][] Q){
		int answer[] = new int[10];//配列を用意
		for(int i=0; i<10; i++){
			answer[i] = Q[0][i] + Q[1][i];//解答の生成
			}
		return answer;
	}

	
}



