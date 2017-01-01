package local.hal.st32.android.mykindreds;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tester on 2017/01/01.
 */

public abstract class Access extends AsyncTask<String,Void,String> {
    private static final String DEBUG_TAG = "Access";
    @Override
    public String doInBackground(String... params){
        String urlStr = params[0];

        HttpURLConnection con = null;
        InputStream is = null;
        String result = "";
        try{
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            is = con.getInputStream();

            result = is2String(is);
        }catch (MalformedURLException ex){
            Log.e(DEBUG_TAG,"URL変換失敗",ex);
        }catch (IOException ex){
            Log.e(DEBUG_TAG,"通信失敗",ex);
        }finally {
            if(con != null) {
                con.disconnect();
            }
            if (is != null) {
                try{
                    is.close();
                }catch (IOException ex){
                    Log.e(DEBUG_TAG,"InputStream解放失敗",ex);
                }
            }
        }
        return result;
    }

    /**
     * InputStreamオブジェクトを文字列に変換するメソッド
     * 変換文字コードはUTF-8
     * @param is 変換された文字列
     * @return 変換された文字列
     * @throws IOException 変換に失敗したときに発生
     */
    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while(0<=(line = reader.read(b))){
            sb.append(b,0,line);
        }
        return  sb.toString();
    }
}
