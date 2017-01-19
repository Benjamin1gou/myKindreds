/***************************
 * 学籍番号:40313
 * 作成者　:k.koki
 * 作成日　:2016/11/16
 * 内容　　:レコードのフォーマットを制御するクラス。
 * *************************/
package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import beans.Bank.Banks;
import beans.Bank.Store;
import beans.Bank.YokinCode;
import beans.DBManager.PreparedStatementByKoki;

import common.Common.RecallManner;
import common.Database;

public class Recode {
	/**
	 * レコードのフォーマットを列挙します。
	 *
	 * @author 浩生
	 *
	 */
	public enum RecodeFormat {
		/**
		 * エンドレコード -----説明------- ファイルの終わりを表す。
		 *
		 * @auther 浩生 2016/11/11
		 * @param End
		 *            Recode
		 */
		End("9", "end", 0),
		/**
		 * トレーラーレコード -------説明------- レコードの終わりを表す。
		 *
		 * @auther 浩生 2016/11/11
		 * @param Triler
		 *            Recode
		 */
		Triler("8", "trailer", 20),
		/**
		 * データレコード ------説明------ 連絡・通知する情報の １単位を表す。
		 *
		 * @auther 浩生 2016/11/11
		 * @param Deta
		 *            Recode
		 */
		Deta("2", "deta", 20),
		/**
		 * ヘッダーレコード -----説明------ データレコードの 開始を表す。
		 *
		 * @auther 浩生 2016/11/11
		 * @param Header
		 *            Recode
		 */
		Header("1", "header", 80);

		/**
		 * データ区分を示す。
		 *
		 * @auther 浩生 2016/11/16
		 * @param code
		 *            String
		 */
		public String code;
		/**
		 * レコード名
		 *
		 * @auther 浩生 2016/11/16
		 * @param pgName
		 *            String
		 */
		public String pgName;
		/**
		 * レコードの長さ
		 *
		 * @auther 浩生 2016/11/16
		 * @param length
		 *            int
		 */
		public int length;

		private RecodeFormat(String code, String pgName, int length) {
			// TODO 自動生成されたコンストラクター・スタブ
			this.code = code;
			this.pgName = pgName;
			this.length = length;
		}

		/**
		 * codeと同等のRecodeを返す。
		 *
		 * @auther 浩生 2016/11/11
		 * @param code
		 * @return
		 */
		public static RecodeFormat indexOf(String code) {
			for (RecodeFormat recode : RecodeFormat.values()) {
				if (code.equals(recode.code)) {
					return recode;
				}
			}
			return null;
		}
	}

	/**
	 * 現在の文字インデックスを保持します。
	 *
	 * @auther 浩生 2016/11/16
	 * @param superPoint
	 *            int
	 */
	private int superPoint=0;
	/**
	 * 読み込んだ文字を
	 *
	 * @auther 浩生 2016/11/16
	 * @param fileValue
	 *            char[]
	 */
	private char[] fileValue;

	/**
	 * 読み込んだファイルから何文字分を返します。 開始位置はこのクラスのpointに記録され その位置から読み込みます。
	 *
	 * @auther 浩生 2016/11/16
	 * @param length
	 * @return
	 * @see PaymentCheck#superPoint
	 */
	private String readLength(int length) {
		StringBuffer sbRead = new StringBuffer();
		for (int count = 0; count < length; count++) {
			sbRead.append(this.fileValue[count+superPoint]);
		}
		return sbRead.toString();
	}

	/**
	 * WEB-INF/bank配下にあるファイルを読み込む
	 *
	 * @auther 浩生 2016/11/15
	 * @return
	 */
	private String readFile(HttpServlet httpServlet, String fileName) {
		String path = httpServlet.getServletContext().getRealPath(
				"/WEB-INF/bank/" + fileName);
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "UTF-8"));
			String string = reader.readLine();
			while (string != null) {
				builder.append(string);
				string = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return builder.toString();
	}

	/**
	 * ファイル内容を持つRecodeを生成します。
	 *
	 * @auther 浩生 2016/11/16
	 * @param servlet
	 * @param fileName
	 * @throws RecodeFormatException
	 */
	public Recode(HttpServlet servlet, String fileName)
			throws RecodeFormatException {
		// TODO 自動生成されたコンストラクター・スタブ
		this.fileValue = this.readFile(servlet, fileName).toCharArray();
	}

	private RecodeFormat nowRecode;

	/**
	 * 現在のレコードから次のレコードタイプを返します。 次のレコードがない場合はnullを返します。
	 *
	 * @auther 浩生 2016/11/16
	 * @return
	 * @throws RecodeFormatException
	 */
	public RecodeType nextRecode() throws RecodeFormatException {
		if (this.superPoint > this.fileValue.length)
			return null;
		// 次のレコード判別
		String recode = String.valueOf(this.fileValue[this.superPoint]);
		RecodeFormat recodeFormat = RecodeFormat.indexOf(recode);
		RecodeType recodeType = null;
		if(recodeFormat==null)throw new RecodeFormatException("登録されていないレコードが含まれています。");
		try{
		switch (recodeFormat) {
		case Deta:
			recodeType = (DetaRecode) new DetaRecode(readLength(
					recodeFormat.length).toCharArray());
			break;
		case Triler:
			recodeType = (TorailerRecode) new TorailerRecode(readLength(
					recodeFormat.length).toCharArray());
			break;
		case End:
			recodeType = (EndRecode) new EndRecode(readLength(
					recodeFormat.length).toCharArray());
			break;
		default:
			recodeType = null;
			break;
		}
		}catch(Exception e){
			throw new RecodeFormatException("レコード内が正しくありません。");
		}
		if(recodeType==null)throw new RecodeFormatException("レコード解析失敗しました。");
		recodeType.format = recodeFormat;
		// 現在のレコードを設定
		setNowRecode(recodeType);
		//ポインター変更
		this.superPoint=this.superPoint+recodeType.getPoint()+1;
		return recodeType;
	}

	/**
	 * ファイル内文字列からヘッダーレコードを返します。
	 *
	 * @auther 浩生 2016/11/16
	 * @return
	 * @throws RecodeFormatException
	 */
	public RecodeType firstRecode() throws RecodeFormatException {
		if (String.valueOf(this.fileValue[0]).equals(RecodeFormat.Header.code) == false) {
			// 先頭がヘッダーレコードでない場合
			throw new RecodeFormatException("先頭がヘッダーレコードでありません。");
		}
		// 戻り値の設定
		RecodeType recodeType = new HeaderRecode(readLength(
				RecodeFormat.Header.length).toCharArray());
		recodeType.format = RecodeFormat.Header;
		// 現在レコードの設定
		setNowRecode(recodeType);
		// ポインターの再設定
		this.superPoint = recodeType.getPoint();
		return recodeType;
	}

	private void setNowRecode(RecodeType recodeType) {
		this.nowRecode = recodeType.format;
	}

	public class RecodeFormatException extends Exception {
		private String message;

		public RecodeFormatException(String message) {
			// TODO 自動生成されたコンストラクター・スタブ
			this.message = message;
		}

		@Override
		public String getMessage() {
			// TODO 自動生成されたメソッド・スタブ
			return this.message;
		}
	}

	public RecodeFormat nowRecode() {
		return this.nowRecode;
	}

	/**
	 * 現在のレコードの種類とレコード内容を持ち レコードを制御するクラス。
	 *
	 * @author 浩生
	 *
	 */
	public class RecodeType {
		public RecodeFormat format;
		private char[] recodeVal;
		/**
		 * レコード内文字列のインデックス。
		 *
		 * @auther 浩生 2016/11/16
		 * @param superPoint
		 *            int
		 */
		private int point;
		{
			this.point=0;
		}
		public RecodeType(char[] recodeValue) {
			// TODO 自動生成されたコンストラクター・スタブ
			this.recodeVal = recodeValue;
		}

		/**
		 * 現在のポインタから指定文字数分を返します。
		 *
		 * @auther 浩生 2016/11/16
		 * @param length
		 * @return
		 */
		private String valLength(int length) {
			StringBuffer sbRead = new StringBuffer();
			for (int count = 0; count < length; count++) {
				sbRead.append(this.recodeVal[count+this.point]);
			}
			// ポインター変更
			this.point = this.point + length;
			return sbRead.toString();
		}

		/**
		 * 左詰めregxを削除します。
		 *
		 * @auther 浩生 2016/11/16
		 * @param val
		 * @return
		 */
		private String deleteLeft(String val, String regx) {
			int index = 0;
			loop: for (int count = 0; count < val.length(); count++) {
				if (!String.valueOf(val.charAt(count)).equals(regx)) {
					index = count;
					break loop;
				}
			}
			return val.substring(index);
		}

		/**
		 * 現在のポインターを返します。
		 *
		 * @auther 浩生 2016/11/21
		 * @return
		 */
		public int getPoint() {
			return this.point;
		}

		/**
		 * ポイントを設定します。
		 * @auther 浩生
		 * 2016/11/21
		 * @param point
		 */
		public void setPoint(int point){
			this.point=point;
		}
	}

	/**
	 * ヘッダーレコード情報を取得します。
	 *
	 * @author 浩生
	 *
	 */
	public class HeaderRecode extends RecodeType {
		public HeaderRecode(char[] recodeValue) {
			super(recodeValue);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		/**
		 * 顧客ID
		 *
		 * @auther 浩生 2016/11/16
		 * @param customerId
		 *            String
		 */
		public String customerId;
		/**
		 * 会社名
		 *
		 * @auther 浩生 2016/11/16
		 * @param corporationName
		 *            String
		 */
		public String corporationName;
		/**
		 * 入金日
		 *
		 * @auther 浩生 2016/11/16
		 * @param paymentDate
		 *            String
		 */
		public CalendarByKoki paymentDate;
		/**
		 * 仕向け銀行
		 *
		 * @auther 浩生 2016/11/16
		 * @param fromBank
		 *            Banks
		 */
		public Banks fromBank;
		/**
		 * 支店
		 *
		 * @auther 浩生 2016/11/16
		 * @param fromStore
		 *            Store
		 */
		public Store fromStore;
		/**
		 * 預金種別
		 *
		 * @auther 浩生 2016/11/16
		 * @param depositType
		 *            YokinCode
		 */
		public YokinCode depositType;
		/**
		 * 振込先口座番号
		 *
		 * @auther 浩生 2016/11/16
		 * @param toAccount
		 *            String
		 */
		public String toAccount;
		/**
		 * 各データの長さを定義 先頭から 振込顧客ID、会社名、入金日、仕向け銀行、支店、預金種類、振込先口座番号、ダミー の順番
		 *
		 * @auther 浩生 2016/11/16
		 * @param FORMAT
		 *            int[]
		 */
		private final int[] FORMAT = { 6, 40, 10, 4, 3, 1, 7, 8 };
		{
			// データ区分をとばす
			super.point = super.point + 1;
			// 各データを取得
			// 顧客ID
			this.customerId = super.valLength(FORMAT[0]);
			// 会社名
			this.corporationName = super.deleteLeft(super.valLength(FORMAT[1]),
					" ");
			// 入金日
			this.paymentDate = new CalendarByKoki(super.valLength(FORMAT[2]));
			// 仕向け銀行
			this.fromBank = Banks.indexOf(super.valLength(FORMAT[3]));
			// 支店
			this.fromStore = Store.indexOf(super.valLength(FORMAT[4]));
			// 預金種類
			this.depositType = YokinCode.indexOf(super.valLength(FORMAT[5]));
			// 振込先口座番号
			this.toAccount = super.deleteLeft(super.valLength(FORMAT[6]), "0");
			// ダミー
			super.valLength(FORMAT[7]);
		}
	}

	/**
	 * データレコード
	 *
	 * @author 浩生
	 *
	 */
	public class DetaRecode extends RecodeType {
		public DetaRecode(char[] recodeValue) {
			super(recodeValue);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		/**
		 * 被仕向け銀行
		 *
		 * @auther 浩生 2016/11/16
		 * @param toBank
		 *            Banks
		 */
		public Banks toBank;
		/**
		 * 支店
		 *
		 * @auther 浩生 2016/11/16
		 * @param store
		 *            Store
		 */
		public Store store;
		/**
		 * 支払種別
		 *
		 * @auther 浩生 2016/11/16
		 * @param recallManner
		 *            RecallManner
		 */
		public RecallManner recallManner;
		/**
		 * 支払額
		 *
		 * @auther 浩生 2016/11/16
		 * @param price
		 *            String
		 */
		public String price;
		private final int[] FORMAT = { 4, 3, 1, 10, 1 };
		{
			// データ区分をとばす
			super.point = super.point + 1;
			// 被仕向け銀行
			this.toBank = Banks.indexOf(super.valLength(FORMAT[0]));
			// 支店
			this.store = Store.indexOf(super.valLength(FORMAT[1]));
			// 預金種類
			this.recallManner = RecallManner
					.indexOf(super.valLength(FORMAT[2]));
			// 支払金額
			this.price = super.deleteLeft(super.valLength(FORMAT[3]), "0");
		}
	}

	/**
	 * トレーラーレコード
	 *
	 * @author 浩生
	 *
	 */
	public class TorailerRecode extends RecodeType {
		public TorailerRecode(char[] recodeValue) {
			super(recodeValue);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public String totalNumber;
		public String totalPrice;
		private final int[] FORMAT = { 6, 12, 1 };
		{
			// データ区分をとばす
			super.point = super.point + 1;
			this.totalNumber = super
					.deleteLeft(super.valLength(FORMAT[0]), "0");
			this.totalPrice = super.deleteLeft(super.valLength(FORMAT[1]), "0");
		}
	}

	/**
	 * エンドレコード
	 *
	 * @author 浩生
	 *
	 */
	public class EndRecode extends RecodeType {
		public EndRecode(char[] recodeValue) {
			super(recodeValue);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		{
			// データ区分をとばす
			super.point = super.point + 1;
		}
	}

	/**
	 * 一つのファイルを読み込んだレコード群を保持するクラス。
	 *
	 * @author 浩生
	 *
	 */
	private class RecodeFile {
		public HeaderRecode headerRecode;
		public ArrayList<DetaRecode> detaRecodes;
		public TorailerRecode torailerRecode;
		public EndRecode endRecode;

		{
			this.detaRecodes=new ArrayList<Recode.DetaRecode>();
		}
		public RecodeFile() throws RecodeFormatException {
			// TODO 自動生成されたコンストラクター・スタブ
			this.headerRecode = (HeaderRecode) firstRecode();
			// データ区分が終わるまで取得する
			RecodeType recodeType = nextRecode();
			date:while (recodeType.format == RecodeFormat.Deta) {
				this.detaRecodes.add((DetaRecode) recodeType);
				recodeType = nextRecode();
				if(recodeType==null){
					break date;
				}
			}
			// トレーラーレコードを取得
			this.torailerRecode = (TorailerRecode) recodeType;
			// エンドレコード
			this.endRecode = (EndRecode) nextRecode();
		}
	}
	/**
	 * bank配下にあるファイルを読み込み入金テーブルにインサートする。
	 * @auther 浩生
	 * 2016/11/22
	 * @param servlet
	 */
	public static void readRecode(HttpServlet servlet){
		//ディレクトリにあるファイルを一覧で読み込む。
				File[] files=InspectionValue.readDirectory(servlet, "bank");
				//ファイルのレコード化変数
				ArrayList<RecodeFile> recodeFiles=new ArrayList<Recode.RecodeFile>();
				for(File file:files){

					try {
						Recode recode=new Recode(servlet, file.getName());
						recodeFiles.add(recode.new RecodeFile());
					} catch (RecodeFormatException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						System.out.println(file.getName()+"で問題発生しました。");
					}
				}
				//検索したファイルレコードを入金テーブルにインサート
				for(RecodeFile file:recodeFiles){
					DBManager dbManager=null;
					try{
						dbManager=new DBManager(Database.DBName);
						//トランザクション開始
						dbManager.setautoCommit(false);
						//顧客の最終請求情報を取得する。
						PreparedStatementByKoki selectLastSettle=dbManager.getStatementByKoki(InspectionValue.readSql(servlet, "SelectLastSettle.sql"));
						selectLastSettle.setString("CUSTOMER_ID", file.headerRecode.customerId);
						String settleId=new String();
						for(ArrayList<String> row:selectLastSettle.select()){
							settleId=row.get(0);
						}
						if(settleId.isEmpty()==false){
							//取得した請求情報に入金処理を行う。
							PreparedStatementByKoki insertPayment=dbManager.getStatementByKoki(InspectionValue.readSql(servlet, "InsertPayment.sql"));
							insertPayment.setInt("SETTLEMENT_ID", Integer.parseInt(settleId));
							insertPayment.setString("DEAD_LINE", new CalendarByKoki().outSQLDate());
							insertPayment.setString("PAYMENT_DATE", new CalendarByKoki().outSQLDate());
							for(DetaRecode deta:file.detaRecodes){
								insertPayment.setInt("PAYMENT_WAY", Integer.parseInt(deta.recallManner.code));
								insertPayment.setInt("PRICE", Integer.parseInt(deta.price));
								insertPayment.update();
							}
						}
						dbManager.runCommit();
						dbManager.setautoCommit(true);
						//トランザクション終了
					}catch(Exception e){
						e.printStackTrace();
					}
				}
	}
}
