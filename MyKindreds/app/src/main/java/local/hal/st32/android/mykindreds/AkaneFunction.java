/**
 * Created by Tester on 2016/12/16.
 * 音声入力されたデータによって機能を分岐させるクラス
 *
 */
package local.hal.st32.android.mykindreds;

import android.content.Context;


public class AkaneFunction {

    public Context context;

    public AkaneFunction(Context context){
        this.context = context;
    }

    /**
     * 音声で機能を分岐させるメソッド
     * @param voice
     */
    public void methodSwitch(String voice){
        switch (voice){
            case "天気":

                break;

            default:
                break;
        }
    }



}
