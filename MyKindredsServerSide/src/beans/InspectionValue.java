/*******************
 * 学籍番号:40313
 * 作成者　:k.koki
 * 作成日　:2015/12/14
 * 内容　　:Servlet,Class,Methodからjspなど各ページに出力や文字列に関して便利なメソッド
 * 			入力チェックなどに関しての正規表現を登録したメソッド
 * *****************
 * 更新者:k.koki
 * 更新日:2016/02/09
 * 内容  :文字列から登録されたフォーマットへの変換に関するメソッド。
 * *****************
 * 更新者:k,koki
 * 更新日:2016/02/09
 * 内容:Carendarクラスのメソッド追加
 * *****************
 * 更新者:k,koki
 * 更新日:2016/02/09
 * 内容:現在の日時を取得する。
 * *****************
 * 更新者:k,koki
 * 更新日:2016/02/09
 * 内容:hew2016のd_delivery_listページ用にフォーマットするメソッド。
 * *****************
 * 更新者:k,koki
 * 更新日:2016/02/25
 * 内容:現在時刻を取得するメソッド。
 * *****************
 * 更新者:k,koki
 * 更新日:2016/02/29
 * 内容:改行文字コードを削除するメソッド
 * *****************
 * 更新者:k,koki
 * 更新日:2016/03/01
 * 内容:SQLインジェクションを開始するメソッド
 * *****************
 * 更新者:k,koki
 * 更新日:2016/11/01
 * 内容:SQLファイルを読み込みString文字列で返すメソッド
 *******************/
package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;

/**
 * @author 浩生
 * @param
 * @retunr OutputValue
 */
public class InspectionValue {

	/**
	 * 作成日:2015/12/14 23:45:22 作成者:k.kooki
	 *
	 * @param 引数がnullの時に空白で返します
	 *            。
	 * @param val
	 * @return 空白または引数
	 */
	public static String cheackNull(String val) {
		if (null == val) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * 作成日:2016/01/07 15:35:53 作成者:k.kooki
	 *
	 * @param valの中がnullの時nullval
	 *            、nullでない時notnullvalを返します。
	 * @param val
	 * @param nullval
	 * @param notnullval
	 * @return
	 */
	public static String cheackNull(String val, String nullval,
			String notnullval) {
		if (val == null)
			return nullval;
		return notnullval;
	}

	/**
	 * int型かをチェックする。
	 */
	public static boolean inspectionInteger(String str) {
		if (!Pattern.compile("\\d+").matcher(str).matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * double型かをチェックする。
	 */
	public static boolean inspectionDouble(String str) {
		if (!Pattern.compile("\\d+\\.\\d+").matcher(str).matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 作成日:2016/01/28 20:13:53 作成者:k.kooki メールチェックの一般的なパターン
	 */
	private static final String _mailPattern = "[0-9a-zA-Z.!#$%&’*+-/=?^_`{|}~]+@([0-9a-zA-Z]+\\.)+[0-9a-zA-Z]+";
	/**
	 * 作成日:2016/01/28 20:28:16 作成者:k.kooki 全角カナ文字の正規表現
	 */
	private static final String _kanaPatten = "^[\\u30A0-\\u30FF]+$";

	/**
	 * 作成日:2016/01/28 20:13:09 作成者:k.kooki
	 *
	 * @param 正規表現にマッチするかのメソッド
	 * @param str
	 * @param pat
	 * @return match=true、matchしない=false
	 */
	public static boolean inspectionPattern(String str, String pat) {
		if (Pattern.compile(pat).matcher(str).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 作成日:2016/01/28 19:14:40 作成者:k.kooki
	 *
	 * @param String型引数がMail
	 *            ([0-9a-zA-Z.!#$%&’*+-/=?^_`{|}~]+@([0-9a-zA-Z]+\\.)+[0-9a-zA-Z
	 *            ]+)に合うかをチェックを行います。 　一般的なメールアドレスです。
	 * @param str
	 * @return
	 */
	public static boolean inspectionMail(String str) {
		return inspectionPattern(str, _mailPattern);
	}

	/**
	 * 作成日:2016/01/28 19:19:58 作成者:k.kooki
	 *
	 * @param String型引数が全角カナ文字かを判断します
	 *            。
	 * @param str
	 * @return 全角カナ=true、それ以外=false
	 */
	/*
	 * public static boolean inspectionKana(String str){ return
	 * inspectionPattern(str, _kanaPatten) }
	 */
	/**
	 * 作成日:2016/01/28 19:27:03 作成者:k.kooki
	 *
	 * @param String型引数が半角英数字かを判断します
	 *            。
	 * @param str
	 * @return 半角英数字=true、それ以外=false
	 */
	public static boolean inspectionAlphanumeric(String str) {
		if (Pattern.compile("[0-9a-zA-Z]{7,20}").matcher(str).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 作成日:2016/01/26 20:04:01 作成者:k.kooki
	 *
	 * @param 引数の文字列がend以内かつbigen以上かをチェックします
	 *            。
	 * @param str
	 * @param bigen
	 * @param end
	 * @return end>str.length>bigen=true それ以外=false
	 */
	public static boolean inspectionRange(String tar, int min, int max) {
		if (tar == null) {
			return false;
		} else if (max > tar.length() && min < tar.length()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 作成日:2016/01/28 18:45:38 作成者:k.kooki
	 *
	 * @param int型の引数がmin
	 *            <tar<maxかを判断する。
	 * @param tar
	 * @param min
	 * @param max
	 * @return min<tar<max=true それ以外=false
	 */
	public static boolean inspectionRange(int tar, int min, int max) {
		if (tar > min && tar < max) {
			return true;
		}
		return false;
	}

	/**
	 * 作成日:2016/01/28 18:54:45 作成者:k.kooki
	 *
	 * @param int型の引数がmin
	 *            <tar<maxかを判断する。
	 * @param tar
	 * @param String
	 *            min
	 * @param String
	 *            max
	 * @return min<tar<max=true それ以外=false min,maxがInt型に変換できない時=false
	 */
	public static boolean inspectionRange(int tar, String min, String max) {
		try {
			return inspectionRange(tar, Integer.parseInt(min),
					Integer.parseInt(max));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 作成日:2016/01/29 15:00:51 作成者:k.kooki
	 *
	 * @param strの中が空白じゃない時にaddを足します
	 *            。
	 * @param str
	 * @param add
	 * @return
	 */
	public static String ifAddString(String str, String add) {
		if (!"".equals(str)) {
			return str + add;
		}
		return str;
	}

	/**
	 * 作成日:2016/01/30 20:05:31 作成者:k.koki
	 *
	 * @param 四捨五入メソッド
	 *            少数第一まで
	 * @param var
	 * @return
	 */
	public static double doDecimal(double var) {
		return doDecimal(var, 0);
	}

	/**
	 * 作成日:2016/01/30 20:06:35 作成者:k.koki
	 *
	 * @param 四捨五入を指定のcount桁まで表示します
	 *            。
	 * @param var
	 * @param count
	 * @return
	 */
	public static double doDecimal(double var, int count) {
		BigDecimal big = new BigDecimal(var);
		BigDecimal decimal = big.setScale(count, BigDecimal.ROUND_HALF_UP);
		return Double.parseDouble(String.valueOf(decimal));
	}

	/**
	 * 作成日:2016/01/30 20:05:44 作成者:k.koki
	 *
	 * @param 日本の汎用数値に変換し返すメソッド
	 * @param var
	 * @return
	 */
	public static String doLocaleJP(double var) {
		Locale jp = new Locale("ja", "JP");
		NumberFormat japan = NumberFormat.getInstance(jp);
		return japan.format(var);
	}

	// update k.koki 2016/02/09 begin
	/**
	 * 作成日:2016/02/09 15:33:32 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成の文字列をCalenderクラスにセットして返します。
	 *            引数が時間帯のみの時、引数に異常がある場合はnullを返します。
	 * @param datetime
	 */
	public static Calendar splitCalenderFormat(String date) {
		if ("".equals(date)) {
			return null;
		}
		String Patyear = "(\\d{4})";
		String PatMMdd = "(\\d{2})";
		String strYear = "";
		String strMonth = "";
		String strDay = "";
		String strTime = "";
		String strMinute = "";
		String strSecond = "";
		try {
			// 年の抽出
			strYear = extranctionString(date, Patyear).get(0);
			// 年をdateから削除する
			date = date.replaceFirst(Patyear, "");
			ArrayList<String> other = extranctionString(date, PatMMdd);
			// 月の抽出
			strMonth = cheackNull(other.get(0), "0", other.get(0));
			// 日の抽出
			strDay = cheackNull(other.get(1), "0", other.get(1));
			// 時の抽出
			strTime = cheackNull(other.get(2), "0", other.get(2));
			// 分の抽出
			strMinute = cheackNull(other.get(3), "0", other.get(3));
			// 秒の抽出
			strSecond = cheackNull(other.get(4), "0", other.get(4));
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay), Integer.parseInt(strTime),
				Integer.parseInt(strMinute)/* , Integer.parseInt(strSecond) */);
		return calendar;
	}

	/**
	 * 作成日:2016/02/09 15:46:55 作成者:k.koki
	 *
	 * @param 正規表現に合う文字列を抽象する
	 *            。
	 * @param target
	 * @param pattern
	 * @return 文字列型可変長配列
	 */
	public static ArrayList<String> extranctionString(String target,
			String pattern) {
		Matcher matcher = Pattern.compile(pattern).matcher(target);
		ArrayList<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	// update k.koki 2016/02/09 end
	// update k.koki 2016/02/11 begin
	/**
	 * 作成日:2016/02/09 13:54:39 作成者:k.koki
	 *
	 * @param 引数の文字列をyyyy
	 *            /MM/ddの形にフォーマットします。
	 * @param time
	 * @return String型　時間
	 * @throws ParseException
	 */
	public static String toFormatDateTime(String datetime)
			throws ParseException {
		String DATE_PATTEN = "yyyy/MM/dd";
		return toFromat(datetime, DATE_PATTEN).toString();
	}

	/**
	 * 作成日:2016/02/09 14:19:39 作成者:k.koki
	 *
	 * @param 文字列型日時を指定のフォーマットに変換する機能です
	 *            。
	 * @param datetime
	 * @param DATE_PATTEN
	 * @return
	 * @throws ParseException
	 */
	private static Date toFromat(String datetime, String DATE_PATTEN)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTEN);
		Date date = dateFormat.parse(datetime);
		return date;
	}

	/**
	 * 作成日:2016/02/09 14:16:56 作成者:k.koki
	 *
	 * @param 引数のdatetimeにtimeを足します
	 *            。
	 * @param datetime
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String plusTime(String datetime, int time)
			throws ParseException {
		Calendar calendar = InspectionValue.splitCalenderFormat(datetime);
		calendar.add(Calendar.HOUR, time);
		String format = "HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String addMinute(String datetime, int min)
			throws ParseException {
		Calendar calendar = InspectionValue.splitCalenderFormat(datetime);
		calendar.add(Calendar.MINUTE, min);
		String format = "HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 作成日:2016/02/11 13:11:01 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成したString型が時間的比較を取得します。
	 * @param leftDateTime
	 * @param rightDateTime
	 * @return 1=未来 -1=過去 0=等しい
	 */
	public static int isComparison(String leftDateTime, String rightDateTime) {
		Calendar leftCalendar = InspectionValue
				.splitCalenderFormat(leftDateTime);
		Calendar rightCalendar = InspectionValue
				.splitCalenderFormat(rightDateTime);
		return rightCalendar.compareTo(leftCalendar);
	}

	public static int isDiffrenceYear(String leftDateTime, String rightDateTime) {
		return isDifferenceDay(leftDateTime, rightDateTime) / 12;
	}

	/**
	 * 作成日:2016/02/11 13:37:05 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成したString型が時間的差分を取得します。
	 * @param leftDateTime
	 * @param rightDateTime
	 * @return 日数バージョン
	 */
	public static int isDifferenceDay(String leftDateTime, String rightDateTime) {
		return isDifferenceHour(leftDateTime, rightDateTime) / 24;
	}

	/**
	 * 作成日:2016/02/11 13:35:12 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成したString型が時間的比較を取得します。
	 * @param leftDateTime
	 * @param rightDateTime
	 * @return 時間バージョン
	 */
	public static int isDifferenceHour(String leftDateTime, String rightDateTime) {
		return isDifferenceMinute(leftDateTime, rightDateTime) / 60;
	}

	/**
	 * 作成日:2016/02/11 13:37:56 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成したString型が時間的比較を取得します。
	 * @param leftDateTime
	 * @param rightDateTime
	 * @return 分バージョン
	 */
	public static int isDifferenceMinute(String leftDateTime,
			String rightDateTime) {
		return (int) isDifferenceSecond(leftDateTime, rightDateTime) / 60;
	}

	/**
	 * 作成日:2016/02/11 13:39:28 作成者:k.koki
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ssの構成したString型が時間的比較を取得します。
	 * @param leftDateTime
	 * @param rightDateTime
	 * @return 秒バージョン
	 */
	public static long isDifferenceSecond(String leftDateTime,
			String rightDateTime) {
		Calendar leftCalendar = splitCalenderFormat(leftDateTime);
		Calendar rightCalendar = splitCalenderFormat(rightDateTime);
		long leftLong = leftCalendar.getTimeInMillis();
		long rightLong = rightCalendar.getTimeInMillis();
		long difference = rightLong - leftLong;
		return difference / 1000;
	}

	/**
	 * 作成日:2016/02/11 14:03:06 作成者:k.koki
	 *
	 * @param doble型時間をHH
	 *            :mm:ssの形にフォーマットします。
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String isFromatDouble(double time) throws ParseException {
		long second = (long) (time * 3600);
		//System.out.println("secound:" + second);
		int minute = (int) (second / 60);
		//System.out.println("minute:" + minute);
		int hour = minute / 60;
		//System.out.println("hour:" + hour);
		String str = "1111-11-11 " + hour + ":" + minute + ":" + second;
		//System.out.println(str);
		return plusTime(str, 0);
	}

	// update k.koki 2016/02/11 end
	// update k.koki 2016/02/16 begin
	/**
	 * 作成日:2016/02/16 17:11:12 作成者:k.koki
	 *
	 * @param 現在の年月日をyyyy
	 *            -MM-dd形式で取得します。
	 * @return String
	 * @throws ParseException
	 */
	public static String getNow() throws ParseException {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	// update k,koki 2016/02/16 end
	// update k.koki 2016/02/17 begin
	/**
	 * 作成日:2016/02/17 15:11:58 作成者:k.koki
	 *
	 * @param yyyy
	 *            /MM/dd-HH:mm;ssのCalenderクラスをyy/MM/dd-HH-(HH+1)の形で取得します。
	 * @param calendar
	 * @return
	 */
	public static String toCalenderFormat(Calendar calendar) {
		if (null == calendar) {
			return "";
		}
		// 年の抽出
		int year = calendar.get(Calendar.YEAR);
		String strYear = String.valueOf(year);
		// 月の抽出
		int month = calendar.get(Calendar.MONTH);
		// 日の抽出
		int day = calendar.get(Calendar.DATE);
		// 時の抽出
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		//System.out.println(hour + "ahoaho");
		int plusHour = hour + 1;
		return strYear.substring(2, 4) + "/" + month + "/" + day + "/"
				+ toIntrgerString(hour) + "-" + toIntrgerString(plusHour);
	}

	/**
	 * 作成日:2016/02/17 15:23:39 作成者:k.koki
	 *
	 * @param 1→01のString型に変換します。
	 * @param i
	 * @return
	 */
	public static String toIntrgerString(int i) {
		if (i >= 10) {
			return String.valueOf(i);
		}
		return "0" + String.valueOf(i);
	}

	// update k.koki 2016/02/17 end
	// update k.koki 2016/02/25 begin
	/**
	 * 作成日:2016/02/25 19:05:09 作成者:k.koki
	 *
	 * @param 現在時刻をHH
	 *            :mm:ssのフォーマットで返す
	 * @return String
	 */
	public static String getNowTime() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	// update k.koki 2016/02/25 end
	// update k.koki 2016/02/29 begin
	public static String replaceLineDecode(String val) {
		if (null != val) {
			return val.replace("\r\n", "");
		}
		return val;
	}

	// update k.koki 2016/02/29 end
	// update k.koki 2016/03/01 begin
	public static String replaceSQL(String val) {
		val = val.replaceAll("<", "&lt;");
		val = val.replaceAll(">", "&gt;");
		val = val.replaceAll("\"", "&quot;");
		val = val.replaceAll("&", "&amp;");
		val = val.replaceAll("'", "&rsquo;");
		return val;
	}
	// update k.koki 2016/03/01 end
	//update k.koki 2016/08/22 begin
	/**
	 * 作成日:2016/08/22 23:28:31
	 * 作成者:k.koki
	 * @param ハイフンなしの電話番号をチェックします。
	 * 		  半角英数時かつ10桁か11桁かを判断します。
	 * @param tel
	 * @return
	 */
	public static boolean inspectionTel(String tel){
		if(!inspectionAlphanumeric(tel)) return false;
		if(tel.length()==10 || tel.length()==11){
			return true;
		}
		return false;
	}
	//update k.koki 2016/08/22 end
	//update k.koki 2016/11/01 start
	public static String readSql(HttpServlet servlet,String path){
		return readSql(servlet, path,"");
	}
	/**
	 * WEB-INF/sql配下にあるsqlファイルを読み込む
	 * @auther 浩生
	 * 2016/11/5
	 * @param servlet
	 * @param path
	 * @param line
	 * @return
	 */
	public static String readSql(HttpServlet servlet,String path,String line){
		path=servlet.getServletContext().getRealPath("/WEB-INF/sql/"+path);
		StringBuilder builder=new StringBuilder();

		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String string=reader.readLine();
			while(string!=null){
				builder.append(string);
				builder.append(line);
				string=reader.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return builder.toString();
	}
	//update k.koki 2016/11/01 end
	/**
	 * WEB-INF配下のディレクトリを読み込み、ファイル配列を返す。
	 * @auther 浩生
	 * 2016/11/16
	 * @param servlet
	 * @param directoryName
	 * @return
	 */
	public static File[] readDirectory(HttpServlet servlet,String directoryName){
		String path=servlet.getServletContext().getRealPath("/WEB-INF/"+directoryName);
		File directory=new File(path);
		return directory.listFiles();
	}
	/**
	 * 指定ファイルを全て読み込み、文字列で返す。
	 * @auther 浩生
	 * 2016/11/16
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String readFile(File file) throws FileNotFoundException{
		return new BufferedReader(new FileReader(file)).toString();
	}

}
