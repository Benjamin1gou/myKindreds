/***************************
 * 学籍番号:40313
 * 作成者　:k.koki
 * 作成日　:2016/11/28
 * 内容　　:旧DBManagerをブラッシュアップする。
 * *************************/
package beans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

/*
 * 各フィールドの名名規則
 * 一番大きなグローバルフィールド：_大文字（一文字）
 * 各内部クラスでのグローバルフィールド：_小文字
 * メソッド内フィールド：全て小文字のスネーク型
 * 各ブロックでのpublicフィールド：小文字から始まるラクダ型
 */

/**
 * このクラスではデータベース接続、SQL結果の制御、 などを一元管理します。 このクラスに存在する例外は全てスローされます。 (クローズメソッド以外）
 * このクラス内のメソッドにDBとの接続をクローズする 処理は行われません。他のメソッドとの整合性を合わせるため。
 * クローズは呼び出し元で用意されたクローズメソッドを実行して ください。
 *
 * @auther 浩生 2016/11/28
 */
public class DBManager2 implements DBManager2Annotation {
	private String _ForName = "com.mysql.jdbc.Driver";
	private String _DriverURL = "jdbc:mysql://localhost:3306/";
	private String _User = "root";
	private String _Password = "";
	private Connection _Con;
	private Statement _St;
	private String _DatabaseName;
	private boolean _IsNewinstance = false;
	private DBManager2(){

	}
	public static DBManager2 dbName(String databaseName) {
		DBManager2 dbManager2 = new DBManager2();
		dbManager2._DatabaseName = databaseName;
		dbManager2._IsNewinstance = true;

		return dbManager2;
	}

	public DBManager2 user(String user) {
		this._User = user;
		return this;
	}

	public DBManager2 password(String password) {
		this._Password = password;
		return this;
	}

	public DBManager2 forName(String forName) {
		this._ForName = forName;
		return this;
	}

	public DBManager2 dirverUrl(String driverUrl) {
		this._DriverURL = driverUrl;
		return this;
	}
	public PreparedStatementByKoki getPreparedStatementByKoki(String sql){
		return this.new PreparedStatementByKoki(sql);
	}
	public PreparedStatementByKoki getPreparedStatementByKoki(HttpServlet servlet,String filePath) throws IOException{
		return this.new PreparedStatementByKoki(servlet,filePath);
	}

	public class DBManager2Exception extends Exception {
		@Override
		public String getMessage() {
			return "dbName()メソッドを必ず使用してください。";
		}
	}

	public DBManager2 connection() throws SQLException, DBManager2Exception, ClassNotFoundException {
		if (this._IsNewinstance == false)
			new DBManager2Exception();
		Class.forName(_ForName);
		this._Con = DriverManager.getConnection(_DriverURL + this._DatabaseName, this._User, _Password);
		this._St = this._Con.createStatement();
		return this;
	}

	/**
	 * DBクローズ時用の一時格納変数
	 *
	 * @auther 浩生 2016/11/28
	 * @param _SelectBox
	 *            SelectBox
	 */
	private SelectBox _SelectBox;

	/**
	 * データベースとの接続を遮断します。 このメソッド後は再接続が必要です。
	 *
	 * @auther 浩生 2016/11/28
	 * @throws SQLException
	 */
	public void close() {
		try {
			if (this._SelectBox != null) {
				this._SelectBox.closeSelect();
			}
			if (this._St != null) {
				this._St.close();
			}
			if (this._Con != null) {
				this._Con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * SQLを実行します。 その後は整形したいメソッドを呼び出す事が出来ます。
	 *
	 * @auther 浩生 2016/11/28
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public SelectBox select(String sql) throws SQLException {
		return this._SelectBox = new SelectBox(this._St.executeQuery(sql));
	}

	/**
	 * WEB-INF配下のSQLファイルを読み込み実行します。
	 * @auther 浩生
	 * 2016/12/08
	 * @param servlet
	 * @param filePath
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public SelectBox selectBox(HttpServlet servlet,String filePath) throws SQLException, IOException{
		return this.select(readSqlFile(servlet, filePath));
	}

	/**
	 * 検索結果を格納する箱クラス。
	 *
	 * @auther 浩生 2016/11/28
	 */
	public class SelectBox {
		private ResultSet rs;
		private ResultSetMetaData rsMetaData;

		public SelectBox(ResultSet rs) throws SQLException {
			this.rs = rs;
			this.rsMetaData = rs.getMetaData();
		}

		protected void closeSelect() throws SQLException {
			this.rsMetaData = null;
			this.rs.close();
		}

		/**
		 * 検索結果を二次元配列の文字列型に変換します。
		 *
		 * @auther 浩生 2016/11/28
		 * @return 検索結果データのみのデータ配列
		 * @throws SQLException
		 */
		public ArrayList<ArrayList<String>> toArrayString() throws SQLException {
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
			if (this.rs.first() == false)
				return result;
			int columnCount = this.rsMetaData.getColumnCount();

			ArrayList<String> row=new ArrayList<>();
			for (int count = 1; count <= columnCount; count++) {
				row.add(this.rs.getString(count));
			}
			result.add(row);

			while (this.rs.next()) {
				row = new ArrayList<String>();
				for (int count = 1; count <= columnCount; count++) {
					row.add(this.rs.getString(count));
				}
				result.add(row);
			}
			return result;
		}

		/**
		 * カラムの基本情報を格納する箱クラス。
		 *
		 * @auther 浩生 2016/11/28
		 */
		public class ColumnBase {
			/**
			 * カタログ名
			 *
			 * @auther 浩生 2016/11/28
			 * @param catalog
			 *            String
			 */
			public String catalog;
			/**
			 * カラム名
			 *
			 * @auther 浩生 2016/11/28
			 * @param columnName
			 *            String
			 */
			public String columnName;
			/**
			 * テーブル名
			 *
			 * @auther 浩生 2016/11/28
			 * @param tableName
			 *            String
			 */
			public String tableName;
			{
				this.catalog = new String();
				this.columnName = new String();
				this.tableName = new String();
			}
		}

		/**
		 * カラムの型、サイズなど詳細情報を格納する箱クラス。
		 *
		 * @auther 浩生 2016/11/28
		 */
		public class ColumnData extends ColumnBase {
			/**
			 * Javaで表現されるデータ型
			 *
			 * @auther 浩生 2016/11/28
			 * @param getJavaClass
			 *            String
			 */
			public String javaClass;
			/**
			 * SQLで表現されるデータ型
			 *
			 * @auther 浩生 2016/11/28
			 * @param sqlClass
			 *            String
			 */
			public String sqlClass;
			/**
			 * カラムのサイズ
			 *
			 * @auther 浩生 2016/11/28
			 * @param size
			 *            int
			 */
			public int size;
			/**
			 * カラムの連番フラグ
			 *
			 * @auther 浩生 2016/11/28
			 * @param autoCommit
			 *            boolean
			 */
			public boolean isAutoCommit;

			/**
			 * カラムにnullが入るかどうか。
			 *
			 * @auther 浩生 2016/11/28
			 * @param isNull
			 *            boolean
			 */
			public int isNull;
			{
				this.javaClass = new String();
				this.sqlClass = new String();
				this.size = 0;
				this.isAutoCommit = false;
				this.isNull = 0;
			}
		}

		/**
		 * SQL結果のカラム情報を簡易的に取得します。
		 *
		 * @auther 浩生 2016/11/29
		 * @return
		 * @throws SQLException
		 */
		public ArrayList<ColumnBase> getColumnDataLights() throws SQLException {
			ArrayList<ColumnBase> columnBases = new ArrayList<DBManager2.SelectBox.ColumnBase>();

			if (this.rsMetaData == null)
				return columnBases;

			int columnCount = this.rsMetaData.getColumnCount();
			for (int count = 1; count <= columnCount; count++) {
				ColumnBase base = new ColumnBase();
				base.catalog = this.rsMetaData.getColumnLabel(count);
				base.columnName = this.rsMetaData.getColumnName(count);
				base.tableName = this.rsMetaData.getTableName(count);
				columnBases.add(base);
			}

			return columnBases;
		}

		/**
		 * SQL結果のカラム情報を詳細に取得します。
		 *
		 * @auther 浩生 2016/11/29
		 * @return
		 * @throws SQLException
		 */
		public ArrayList<ColumnData> getColumnDataHeavy() throws SQLException {
			ArrayList<ColumnData> columnDatas = new ArrayList<DBManager2.SelectBox.ColumnData>();
			if (this.rsMetaData == null)
				return columnDatas;

			int columnCount = this.rsMetaData.getColumnCount();
			for (int count = 1; count <= columnCount; count++) {
				ColumnData columnData = new ColumnData();
				columnData.catalog = this.rsMetaData.getColumnLabel(count);
				columnData.columnName = this.rsMetaData.getColumnName(count);
				columnData.tableName = this.rsMetaData.getTableName(count);
				columnData.javaClass = this.rsMetaData.getColumnClassName(columnCount);
				columnData.sqlClass = this.rsMetaData.getColumnTypeName(columnCount);
				columnData.size = this.rsMetaData.getPrecision(columnCount);
				columnData.isAutoCommit = this.rsMetaData.isAutoIncrement(columnCount);
				columnData.isNull = this.rsMetaData.isNullable(columnCount);
				columnDatas.add(columnData);
			}
			return columnDatas;
		}

		/**
		 * SQL結果をマップリストに変換します。 マップのキーはカラム名です。
		 *
		 * @auther 浩生 2016/11/29
		 * @return
		 * @throws SQLException
		 * @see {@link ResultSetMetaData#getColumnName(int)}カラム名の説明
		 */
		public ArrayList<HashMap<String, String>> toArrayHashMap() throws SQLException {
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

			if (this.rs.first() == false)
				return result;

			ArrayList<ColumnBase> columnBases = getColumnDataLights();
			if (columnBases.isEmpty() == true)
				return result;

			HashMap<String, String> row = new HashMap<String, String>();

			int count = 1;
			for (ColumnBase base : columnBases) {
				row.put(base.columnName, this.rs.getString(count++));
			}
			result.add(row);

			while (this.rs.next()) {
				row = new HashMap<String, String>();
				count = 1;
				for (ColumnBase base : columnBases) {
					row.put(base.columnName, this.rs.getString(count++));
				}
				result.add(row);
			}

			return result;
		}

		/**
		 * SQL結果の一行分を格納するクラスの DBManager2_colアノテーションかつString型のフィールドに 自動格納します。
		 * DBManager2_colアノテーションの引数はカラム名を指定してください。 処理が正常に終了しない場合は空リストを返します。
		 *
		 * @auther 浩生 2016/11/29
		 * @param classz
		 * @return
		 * @throws SQLException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @see {@link ResultSetMetaData#getColumnName(int)}カラム名の説明
		 */
		public <T> ArrayList<T> convertToClass(Class<T> classz)
				throws SQLException, InstantiationException, IllegalAccessException {
			if (this.rs.first() == false)
				return new ArrayList<T>();

			java.lang.reflect.Field[] fields = classz.getDeclaredFields();
			if (fields.length == 0)
				return new ArrayList<T>();

			ArrayList<ColumnData> columnDatas = getColumnDataHeavy();
			if (columnDatas.isEmpty() == true)
				return new ArrayList<T>();

			ArrayList<T> result = new ArrayList<T>();

			T object = classz.newInstance();
			field_loop: for (java.lang.reflect.Field field : fields) {
				DBManager2_col manager2_col = field.getAnnotation(DBManager2_col.class);
				if (manager2_col == null)
					continue field_loop;

				int columnIndex = indexColumnName(columnDatas, manager2_col.value());
				if (columnIndex == -1)
					return new ArrayList<T>();

				field.setAccessible(true);
				field.set(object, this.rs.getString(columnIndex));
			}
			result.add(object);

			while (this.rs.next()) {
				object = classz.newInstance();
				field_loop: for (java.lang.reflect.Field field : fields) {
					DBManager2_col manager2_col = field.getAnnotation(DBManager2_col.class);
					if (manager2_col == null)
						continue field_loop;

					int columnIndex = indexColumnName(columnDatas, manager2_col.value());
					if (columnIndex == -1)
						return new ArrayList<T>();

					field.setAccessible(true);
					field.set(object, this.rs.getString(columnIndex));
				}
				result.add(object);
			}

			return result;
		}

		/**
		 * カラムリストから指定カラム名を検索しインデックスを返すメソッド。 未発見で-1を返す。
		 *
		 * @auther 浩生 2016/12/01
		 * @param bases
		 * @param columnName
		 * @return
		 */
		public int indexColumnName(ArrayList<ColumnData> bases, String columnName) {
			if (bases == null)
				return -1;
			if (bases.isEmpty() == true)
				return -1;
			if (columnName == null)
				return -1;
			if (columnName.isEmpty() == true)
				return -1;

			for (ColumnBase base : bases) {
				if (columnName.equals(base.columnName) == true)
					return bases.indexOf(base)+1;
			}
			return -1;
		}

		/**
		 * 検索結果が空かどうかを判定します。
		 *
		 * @auther 浩生 2016/12/02
		 * @return true=空、false=結果あり
		 * @throws SQLException
		 */
		public boolean isEmpty() throws SQLException {
			if(this.rs==null)return true;
			if(this.rsMetaData==null)return true;
			if(toArrayString().isEmpty()==false)return false;
			return true;
		}
	}

	/**
	 * WEB-INF配下のSQLファイルを読み込み文字列で返します。
	 *
	 * @auther 浩生 2016/11/28
	 * @param servlet
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String readSqlFile(HttpServlet servlet, String filePath) throws IOException {
		filePath = servlet.getServletContext().getRealPath("/WEB-INF/" + filePath);
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
		String string = reader.readLine();
		while (string != null) {
			builder.append(string);
			string = reader.readLine();
		}
		reader.close();
		return builder.toString();
	}

	/**
	 * 更新系SQLを実行します。
	 *
	 * @auther 浩生 2016/12/02
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int update(String sql) throws SQLException {
		return this._St.executeUpdate(sql);
	}

	/**
	 * 更新系SQLを複数回実行します。
	 * @auther 浩生
	 * 2016/12/08
	 * @param sqls
	 * @return 更新件数,実行件数
	 */
	public int[] updates(ArrayList<String> sqls){
		int[] count={0,0};
		for(String sql:sqls){
			count[1]++;
			try{
				this.update(sql);
				count[0]++;
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}


	/**
	 * トランザクションを開始します。
	 *
	 * @auther 浩生 2016/12/02
	 * @throws SQLException
	 */
	public void startTransaction() throws SQLException {
		this._Con.setAutoCommit(false);
	}

	/**
	 * トランザクションを終了します。
	 *
	 * @auther 浩生 2016/12/02
	 * @throws SQLException
	 * @throws TransactionByKoki
	 */
	public void endTransaction() throws SQLException, TransactionByKoki {
		if (!this.nowTransaction())
			throw new TransactionByKoki();
		this._Con.setAutoCommit(true);
	}

	/**
	 * 現在のトランザクション状態を取得します。
	 *
	 * @auther 浩生 2016/12/02
	 * @return true=トランザクション終了、false=トランザクション開始
	 * @throws SQLException
	 */
	public boolean nowTransaction() throws SQLException {
		return this._Con.getAutoCommit();
	}

	/**
	 * トランザクション状態を管理するための例外クラス。 トランザクションの開始と終了のセットが、ソース上で 行われるようにするための、制約的例外クラス。
	 *
	 * @auther 浩生 2016/12/02
	 */
	public class TransactionByKoki extends Exception {
		@Override
		public String getMessage() {
			return "トランザクションが開始されていません!!";
		}
	}

	/**
	 * コミットを行うメソッド
	 *
	 * @auther 浩生 2016/12/02
	 * @throws TransactionByKoki
	 * @throws SQLException
	 */
	public void commit() throws TransactionByKoki, SQLException {
		if (!this.nowTransaction())
			throw new TransactionByKoki();
		this._Con.commit();
	}

	/**
	 * トランザクションを開始前までに戻す処理。
	 *
	 * @auther 浩生 2016/12/02
	 * @throws TransactionByKoki
	 * @throws SQLException
	 * @throws NullPointerException
	 */
	public void rollback() throws TransactionByKoki, SQLException, NullPointerException {
		if (!this.nowTransaction())
			throw new TransactionByKoki();
		if (this._Con == null)
			throw new NullPointerException();
		this._Con.rollback();
	}

	public class PreparedStatementByKoki {
		private String sql;
		/**
		 * toNull()、toNullAll()を実行したときのkeyログ
		 *
		 * @auther 浩生 2016/11/08
		 * @param to_null_keys
		 *            ArrayList<String>
		 */
		private ArrayList<String> to_null_keys;

		{
			this.to_null_keys = new ArrayList<>();
		}

		public PreparedStatementByKoki(String sql) {
			this.sql = sql;
		}
		/**
		 * WEB-INF配下のSQLファイルを読み込み生成します。
		 * @throws IOException
		 * @auther 浩生
		 * 2016/12/08
		 */
		public PreparedStatementByKoki(HttpServlet servlet,String filePath) throws IOException {
			this.sql=readSqlFile(servlet, filePath);
		}

		private void replace(String key, String val)
		{
			this.sql = sql.replaceAll(key, val);
		}

		public void setString(String key, String val)
		{
			val = replaceSQL(val);
			this.replace(key, "'" + val + "'");
		}

		public void setInt(String key, int val)
		{
			this.replace(key, String.valueOf(val));
		}

		public String out(){
			return this.sql;
		}

		/**
		 * コメント文を削除します。
		 * \/**\/で始まるもの。
		 * @auther 浩生
		 * 2016/12/02
		 */
		public void deleteComment() {
			String regex = "/\\*/?([^/]|[^*]/)*\\*/";
			this.sql = this.sql.replaceAll(regex, "");
		}

		private String START = "start*/";
		private String END = "end*/";
		private String IF = "/*if";
		private int nowIndex = 0;

		/**
		 * SQL文でif(key)startからif(key)endまでくくった個所を削除します。
		 * 他のメソッドの干渉もあるのでif(key)はコメント化してください。
		 *
		 * @param key
		 */
		public int toNull(String key) {
			// toNullログ
			if (!this.to_null_keys.contains(key)) {
				this.to_null_keys.add(key);
				this.nowIndex = 0;
			}
			String startKey = IF + "(" + key + ")" + START;
			String endKey = IF + "(" + key + ")" + END;
			int startIndex = endLine(startKey);
			int endIndex = startLine(endKey);
			if (startIndex == -1 || endIndex == -1) {
				return -1;
			}
			// 削除処理
			String start = this.sql.substring(0, startIndex);
			String end = this.sql.substring(endIndex, this.sql.length());
			this.sql = start + end;

			this.nowIndex = endIndex;
			return endIndex + endKey.length();
		}

		/**
		 * コメント文の末尾を求める時の最大半角数 コメント末尾で２文字必要なので、if(key)の)後の許容数
		 */
		private int maxLine = 10;

		/**
		 * if(key)の末尾のインデックスを取得します。
		 *
		 * @param ifcode
		 * @return
		 */
		private int endLine(String ifcode) {
			int endIndex = this.sql.indexOf(ifcode, nowIndex);
			if (endIndex == -1) {
				return -1;
			}
			endIndex = endIndex + ifcode.length();
			// 末尾のコメントを求める
			loop: for (int i = 0; i < maxLine; i++) {
				if (this.sql.substring(endIndex, endIndex + i).equals("*/")) {
					// コメント末尾発見
					endIndex = endIndex + i;
					break loop;
				}
			}
			return endIndex;
		}

		/**
		 * if(key)の先頭のインデックスを取得します。
		 *
		 * @param ifcode
		 * @return
		 */
		private int startLine(String ifcode) {
			int startIndex = this.sql.indexOf(ifcode, nowIndex);
			if (startIndex == -1) {
				return -1;
			}
			// 先頭のコメントを検索する。
			loop: for (int i = 0; i < maxLine; i++) {
				if (this.sql.substring(startIndex - i, startIndex).equals("/*")) {
					// 先頭コメント発見
					startIndex = startIndex - i;
					break loop;
				}
			}
			return startIndex;
		}

		/**
		 * 該当するif(key)文を全て削除します。
		 *
		 * @param key
		 */
		public void toNullAll(String key) {
			while (toNull(key) > 0);

		}

		/**
		 * 検索しそのインデックスを返します。
		 *
		 * @auther 浩生 2016/11/07
		 * @param key
		 * @return
		 */
		public int indexOf(String key) {
			return this.sql.indexOf(key);
		}

		/**
		 * 複数の文字列を検索し存在するかをbooleanで返します。
		 *
		 * @auther 浩生 2016/11/07
		 * @param keys
		 * @return
		 */
		public boolean indexOfs(String... keys) {
			int index = 0;
			for (String key : keys) {
				index = index + indexOf(key);
			}
			if (index > 0) {
				return true;
			}
			return false;
		}

		private ArrayList<WhereBox> add_wheres=new ArrayList<>();
		/**
		 * オブジェクトフィールドに、DBManager2_whereアノテーション
		 * とDBManager2_col、がついている
		 * フィールドの値を追加します。
		 * フィールドの型は文字列型か数値型のみに対応可能です。
		 * 文字列型:String
		 * 数値型：String以外
		 * ※注意
		 * String型は条件式の値にシングルクォーテーションが付与されます。
		 * Date系などはStringフィールドに変換し格納した状態にする必要があります。
		 * @auther 浩生 2016/12/02
		 * @param param
		 */
		@Deprecated
		public void addWhere(Object param) {
			field_loop:for(Field field:param.getClass().getDeclaredFields()){
				DBManager2_where where=field.getAnnotation(DBManager2_where.class);
				if(where==null)continue field_loop;
				DBManager2_col col=field.getAnnotation(DBManager2_col.class);
				if(col==null)continue field_loop;
				if(field.getType()==String.class){
					//フィールドの値を取得後wheheboxに格納。
				}
			}
		}
		@Deprecated
		public class WhereBox{
			/**
			 * WhereBoxに名前を付ける事が出来ます。
			 * @auther 浩生
			 * 2016/12/02
			 * @param where_label String
			 */
			protected String where_label;
			protected String front;
			protected String tableName;
			protected String columnName;
			protected String conditon;
			protected String strVal;
			protected int intVal;
			protected String end;
			{
				this.where_label=new String();
				this.front=new String();
				this.tableName=new String();
				this.columnName=new String();
				this.conditon=new String();
				this.strVal=new String();
				this.end=new String();
			}
			protected String out(){
				StringBuffer sb_result=new StringBuffer();
				sb_result.append(front+" ");
				sb_result.append(this.tableName+"."+this.columnName);
				sb_result.append(" "+this.conditon+" ");
				if(this.strVal.isEmpty()==true)sb_result.append("'"+this.strVal+"'");
				sb_result.append(String.valueOf(this.intVal));
				sb_result.append(" "+this.end);
				return sb_result.toString();
			}
		}
		public SelectBox select() throws SQLException{
			return DBManager2.this.select(this.sql);
		}
		public int update() throws SQLException{
			return DBManager2.this.update(this.sql);
		}
	}
	public String replaceSQL(String strSQL)
	{
		strSQL = strSQL.replaceAll("<", "&lt;");
		strSQL = strSQL.replaceAll(">", "&gt;");
		strSQL = strSQL.replaceAll("\"", "&quot;");
		strSQL = strSQL.replaceAll("&", "&amp;");
		strSQL = strSQL.replaceAll("'", "&rsquo;");
		return strSQL;
	}

}
