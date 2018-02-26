package com.example.yoonsung.ruinofkisa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
자신의 보험목록을 출력해주어 허리관련 보험이 있는지 확인해야됨
로그인 창에서 입력한 아이디와 패스워드는 intent로 넘어옴
*/
public class DBHelper extends SQLiteOpenHelper {
//    private Context context;
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        this.context = context;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 최초에 데이터베이스가 없을경우, 데이터베이스 생성을 위해 호출됨
        // 테이블 생성하는 코드를 작성한다
        Log.d("디비삭제","create");
        db.execSQL("CREATE TABLE USER_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, pw TEXT, create_at TEXT, using_time INTEGER, right_time INTEGER, default1 INTEGER, default2 INTEGER, default3 INTEGER, default4 INTEGER, control1 INTEGER, control2 INTEGER);");
    }




    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists USER_LIST");
        Log.d("디비삭제","ㄴㄴㄴ");
        onCreate(db);
    }
    public void insert(String id,String pw,String date,int using_time,int right_time,int d1,int d2,int d3,int d4,int c1,int c2) {
        SQLiteDatabase db = getWritableDatabase();
        String _query = ("insert into USER_LIST values(null, '" + id + "','" + pw + "','" + date + "', " + using_time + ", " + right_time + ", " + d1 + ", " + d2 + ", " + d3 + ", " + d4 + ", " + c1 + ", " + c2 + ");");
        db.execSQL(_query);
        db.close();
    }
    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }
    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : ID "
                    + cursor.getString(1)
                    + " : PW "
                    + cursor.getString(2)
                    + " : DATE  "
                    + cursor.getString(3)
                    + " : using_time "
                    + cursor.getInt(4)
                    +" : right_time "
                    +cursor.getInt(5)
                    + "\n";
        }

        return str;
    }
    public int[] getStamp(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();
        int dis=0;
        int tmp[] = new int[31];
        Log.d("ㅅㅂㅅㅂ","id : "+date.substring(8,9));
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
            int k = cursor.getInt(10);

            if(check_pw.equals(pw) && (date.substring(0,7).equals(check_date.substring(0,7)))){
                if(k!=0)
                    tmp[Integer.parseInt(check_date.substring(8,9)+check_date.substring(9))] = 1;
            }
        }
        return tmp;
    }
    public int getDiscount(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();
        int dis=0;
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && date.substring(0,7).equals(check_date.substring(0,7))){
                dis+= cursor.getInt(10);
            }
        }
        return dis;
    }
    public int getPW(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();
        int using =-1;
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && check_date.equals(date)){
                using = cursor.getInt(4);
                break;
            }
        }
        return using;
    }
    public int getRight(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();
        int using =-1;
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && check_date.equals(date)){
                using = cursor.getInt(5);
                break;
            }
        }
        return using;
    }
    public int[] todayTime(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();
        int[] time = new int[2];
        int using =-1;
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && check_date.equals(date)){
                time[0] = cursor.getInt(4);
                time[1] = cursor.getInt(5);
                break;
            }
        }
        return time;
    }
    public String[] pastDate(String date){
        SQLiteDatabase db = getReadableDatabase();
        int[] time = new int[12];
        String[] week_day = new String[8];
        int k=1;
        week_day[0] = date;
        while(k<8){
            if(Integer.parseInt(date.substring(9))!=0){
//                Log.d("하하","뷁"+(Integer.parseInt(date.substring(9))-1));
                date = date.substring(0,9)+(Integer.parseInt(date.substring(9))-1);
//                Log.d("하하",date);
                week_day[k] = date;
            }
            else{
                if(Integer.parseInt(date.substring(8))!=0){
                    date = date.substring(0,8)+(Integer.parseInt(date.substring(8,9))-1) +9;
//                    Log.d("하하","뷁"+(Integer.parseInt(date.substring(8,9))-1));
                    week_day[k] = date;
                }
            }
            k++;
        }
        for(int i=0;i<8;i++)
            Log.d("하하",week_day[i]);
        return week_day;
    }
    public int[] pastTime(String pw,String[] date){
        SQLiteDatabase db = getReadableDatabase();
        Log.d("으악","PW : "+pw);
        int[] time = new int[14];
//        String[] week_day = new String[7];
        int cnt=0;
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && check_date.equals(date[cnt])){
                time[cnt*2] = cursor.getInt(4);
                time[cnt*2+1] = cursor.getInt(5);
                cnt++;
                cursor = db.rawQuery("select * from USER_LIST", null);
            }
            if(cnt==7)
                break;
        }
        for(int i=0;i<14;i++){
            Log.d("으악","time : "+time[i]);
        }
        return time;
    }
    public int[] getDefault(String pw,String date){
        SQLiteDatabase db = getReadableDatabase();

        int default_list[] = new int[4];
        Cursor cursor = db.rawQuery("select * from USER_LIST", null);
        while(cursor.moveToNext()){
            String check_pw = cursor.getString(2);
            String check_date = cursor.getString(3);
//            Log.d("으악","id : "+tmp);
            if(check_pw.equals(pw) && check_date.equals(date)){
                default_list[0] = cursor.getInt(6);
                default_list[1] = cursor.getInt(7);
                default_list[2] = cursor.getInt(8);
                default_list[3] = cursor.getInt(9);
                break;
            }
        }
        return default_list;
    }

}




