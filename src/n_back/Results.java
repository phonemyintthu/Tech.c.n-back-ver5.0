package n_back;

public class Results {
	
	//s→正解数, n→N数, t→プレイ時間
	public int score(int s,int n, int t){
		int score = 0;
		//スコアの計算
		score = n*s*10+(s*5-t)+n*5;
		if(score<0){
			score = 0;
		}
	return score;
	}
	
	public String comment(int s, int n, int t){
		String time = "";
		String Nnum = "";
		String comment = "";
		
		Results r = new Results();
		
		//正解数が0問の場合割り算でエラーを起こさないように
		int S = s;
		if(s==0){
			S=1;
		}
		//1問あたりの回答時間
		int AT = (t-n*5)/S;
		//スコアを求める
		int score = r.score(s,n,t);
		
		//解答時間に関係するコメント
		if(AT<2){
			time = "せっかちな";
		}else if(AT<4){
			time = "堅実な";
		}else{
			time = "のんびり屋で";
		}
		
		//選択したN数に関係するコメント
		switch(n){
		case 1:	Nnum = "小心者";	break;
		case 2:	Nnum = "レベル2";	break;
		case 3:	Nnum = "ちょっと冒険";	break;
		case 4:	Nnum = "微妙なN数";	break;
		case 5:	Nnum = "なぜか5バック";	break;
		case 6:	Nnum = "ネタ切れ";	break;
		case 7:	Nnum = "思いつかん";	break;
		case 8:	Nnum = "なぜか9バックにしない";	break;
		case 9:	Nnum = "ドマゾ";	break;
		default: break;
		}
		
		//スコアに関係するコメント
		if(score<100){
			comment = "ルール見直して？";
		}else if(score<200){
			comment = "ビギナー";
		}else if(score<300){
			comment = "駆け出しNバッカー";
		}else if(score<400){
			comment = "Nバックの友達";
		}else if(score<500){
			comment = "慣れてきたNバッカー";
		}else if(score<600){
			comment = "中堅";
		}else if(score<700){
			comment = "Nバック鉄人";
		}else if(score<800){
			comment = "Nバック魔人";
		}else if(score<900){
			comment = "準廃人";
		}else if(score<1000){
			comment = "Nバック廃人";
		}else if(score>=1000){
			comment = "N BACK GOD";
		}
		
		//コメント
		return time + Nnum + comment;
	}
}



