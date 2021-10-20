package com.tamstudio.asm_duytam.utilities;

import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_EMAIL_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@SuppressLint("SimpleDateFormat")
public class Utilities {
    public static SimpleDateFormat formatMonth = new SimpleDateFormat("dd\nMMM");
    public static SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a");
    public static SimpleDateFormat formatTime2 = new SimpleDateFormat("hh:mm a");

    public static int DAY =Calendar.getInstance().get(Calendar.DATE);
    public static int MONTH = Calendar.getInstance().get(Calendar.MONTH);
    public static int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static int HOUR = Calendar.getInstance().get(Calendar.HOUR);
    public static int MINUS = Calendar.getInstance().get(Calendar.MINUTE);
    public static String DateToString(Date date,SimpleDateFormat pattern){
        return pattern.format(date).toString();
    }
    public static Date StringToDate(String sDate,SimpleDateFormat pattern){
        try {
            return  pattern.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    public static boolean checkAdmin(Context context){
        String emailIDUser=  MySharePreferences.getInstance(context).getStringValue(SHARE_PREFERENCE_EMAIL_ID);
        User user = UserDAO.getInstance(context).getById(emailIDUser);
        return  user.isAdmin();
    }

    public static void showToast(Context context, String textS, int resourceImg, int resourceBg) {
        View view = LayoutInflater.from(context).inflate(com.tamstudio.asm_duytam.R.layout.layout_custom_toast, null);
        view.setBackgroundResource(resourceBg);
        TextView text = view.findViewById(com.tamstudio.asm_duytam.R.id.tv_toast);
        ImageView img = view.findViewById(com.tamstudio.asm_duytam.R.id.iv_toast);

        text.setText(textS);
        img.setImageResource(resourceImg);
        Toast toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

}
