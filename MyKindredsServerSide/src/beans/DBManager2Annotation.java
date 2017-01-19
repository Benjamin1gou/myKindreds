/***************************
 * 学籍番号:40313
 * 作成者　:k.koki
 * 作成日　:2016/12/02
 * 内容　　:
 * *************************/
package beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface DBManager2Annotation {
	/**
	 * 引数にはテーブル名を指定します。
	 * @auther 浩生
	 * 2016/12/02
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DBManager2_where{
		String value();
	}
	/**
	 * 引数にはカラム名を指定します。
	 * @auther 浩生
	 * 2016/12/02
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DBManager2_col {
		String value();
	}
}