package com.example.yoonsung.ruinofkisa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
자신의 보험목록을 출력해주어 허리관련 보험이 있는지 확인해야됨
로그인 창에서 입력한 아이디와 패스워드는 intent로 넘어옴
*/
public class AttitudeActivity extends BaseActivity {
    private String temp = null;
    private ScrollView scroll;
    private ExpandableListView list;
    private ArrayList<list_item> list_data;
    private ArrayList<ArrayList<child_list_item>> list_data_child;
    private CustomAdapter list_adapter;
    private TextView discount_text;
    private TextView home_text;
    private TextView money_text;
    private Intent intent;
    private String name = null,date=null;
    private String id_number = null;
    private Thread get;
    private int money;
    private int set_color[];
    DBHelper dbManager;

    JsonParser json_parser;
    JsonObject json_object;
    JsonObject json_data;
    JsonObject[] json_data_list;
    JsonArray json_array;
    JsonArray json_contract_array;
    String[] number_array;

    String[] find_tag;
    String[] push_tag;

    public AttitudeActivity() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attitude);
        dbManager = new DBHelper(getApplicationContext(), "User.db", null, 3);
        money = 0;
        scroll = (ScrollView) findViewById(R.id.first_page_ScrollView);
        list = (ExpandableListView) findViewById(R.id.first_page_ListView);
        home_text = (TextView) findViewById(R.id.home_txt);
        money_text = (TextView) findViewById(R.id.money_txt);
        discount_text = (TextView) findViewById(R.id.discount_txt);
        intent = getIntent();
        name = intent.getExtras().getString("name"); // id
        id_number = intent.getExtras().getString("id_number"); // pw
        date = intent.getExtras().getString("DATE"); // date
        home_text.setText(name + "님 안녕하세요!!!!");
        find_tag = new String[]{"PB020UM_O_PRTY_NAME", "PB020UM_O_PAIG_AGMT_AGE", "PB020UM_O_PAIG_KNOW_AGE", "PB020UM_O_ELAG_PNSN_ST_AGE",
                "PB020UM_O_PROD_NAME", "PB020UM_O_INAG_ST_DT", "PB020UM_O_ELAG_PAY", "PB020UM_O_INAG_STATE", "PB020UM_O_PREM_STATE",
                "PB020UM_O_PREM_BASE_PREM", "PB020UM_O_INAG_INS_PERIOD", "PB020UM_O_PREM_CLCT_MTHD", "PB020UM_O_AUTR_CLIM_ACCT_NO",
                "PB020UM_O_AUTR_IND_DT", "PB020UM_O_AUTR_RQST_BANK_NM", "PB020UM_O_INPT_PRTY_MNAME", "PB020UM_O_INPT_PRTY_SNAME"};
        push_tag = new String[]{"계약자명", "계약연령", "현재연령", "연금개시연령", "상품명", "계약시기", "가입금액", "계약상태",
                "납입상태", "원보험료", "보험기간", "보험료수납방법", "계좌번호", "이체일", "은행명", "모집설계사", "현재 담당 설계사"};


        temp = id_number;

        final Handler mHandler = new Handler();
        get = new Thread(new Runnable() {
            @Override
            public void run() { // TODO Auto-generated method stub
                temp = apiResult("https://openapi.kyobo.co.kr:1443/v1.0/INFO/user/husermapinfo", "1", "PRTY_REG_NO", temp, "p95AT1LAeQZ6tqCE5aTV7Q7bmSdzzCkR");
            }
        });

        get.start();
        try {
            get.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        json_parser = new JsonParser();
        json_object = (JsonObject) json_parser.parse(temp);
        json_data = (JsonObject) json_object.get("dataBody");

        json_array = json_data.getAsJsonArray("USER_INFO");

        number_array = new String[json_array.size()];

        temp = "";
        for(int i=0 ; i<json_array.size() ; i++){
            number_array[i] = ((JsonObject)json_array.get(i)).get("INAG_NO").toString();
            number_array[i] = number_array[i].replace("\"", "");
        }



        temp = "";
        json_contract_array = new JsonArray();

        get = new Thread(new Runnable() {
            @Override
            public void run() { // TODO Auto-generated method stub
                for(int i=0 ; i<json_array.size() ; i++) {
                    temp = number_array[i];
                    temp = apiResult("https://openapi.kyobo.co.kr:1443/v1.0/PAY/contract/terms", "1", "PB020UM_I_INAG_INAG_NO", temp, "p95AT1LAeQZ6tqCE5aTV7Q7bmSdzzCkR");
                    json_contract_array.add(temp);
                }
            }
        });
        get.start();

        try {
            get.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        json_data_list = new JsonObject[json_contract_array.size()];
        list_data = new ArrayList<list_item>();
        list_data_child = new ArrayList<ArrayList<child_list_item>>();
        set_color = new int[json_contract_array.size()];
        for(int i=0 ; i<json_contract_array.size() ; i++){
            set_color[i] = 0;
        }
        for(int i=0 ; i<json_contract_array.size() ; i++){
            JsonObject jo;
            JsonArray ja;
            temp = json_contract_array.get(i).toString();
            temp = temp.replace("\\n", "");
            temp = temp.replace("\"", "");
            temp = temp.replace("\\", "\"");

            json_parser.parse(temp);
            jo =  json_parser.parse(temp).getAsJsonObject();
            jo = (JsonObject) ((JsonObject) json_parser.parse(temp)).get("dataBody");
            ja = jo.getAsJsonArray("PB020UM_O_ITMES");
            jo = (JsonObject) ja.get(0);
            json_data_list[i] = jo;
            temp = jo.get("PB020UM_O_PROD_NAME").toString();
            list_data.add(new list_item(String.valueOf(i), temp));
            ArrayList<child_list_item> temp_list = new ArrayList<>();
            if(temp.contains("사랑")) {
                set_color[i] = 2;
                list.setSelection(i);
                String aaaa = jo.get("PB020UM_O_ELAG_PAY").toString().replace("\"", "");
                money += Integer.parseInt(jo.get("PB020UM_O_ELAG_PAY").toString().replace("\"", ""));
            }
            for(int j=0 ; j<17 ; j++){
                temp_list.add(new child_list_item(push_tag[j], jo.get(find_tag[j]).toString()));
            }
            list_data_child.add(temp_list);
            Log.v("보험금", String.valueOf(money));
            money_text.setText("현재 가입한 건강보험은 " + String.valueOf(Math.round(money/12/1000)*1000) + "원 입니다");
            int right_time = dbManager.getRight(id_number,date);
            if(right_time > 2000) {
                int discount = dbManager.getDiscount(id_number,date);
                dbManager.update("update USER_LIST set control1 = " + Math.round((Math.round(money / 12 / 100000) * 100000) * 0.001/30) + " where pw = '" + id_number + "' AND create_at = '" + date + "';");
                discount_text.setText("오늘 할인을 받으셔서 총" + String.valueOf(discount)+"원을 할인받을 수 있습니다");
            }
            else
                discount_text.setText("오늘은 할인을 받지 못하였습니다.");


        }

        list_adapter = new CustomAdapter(this, list_data, list_data_child);
        list.setAdapter(list_adapter);

        //list.setSelector(new PaintDrawable(0xd3e5df));

        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //   Toast.makeText(AttitudeActivity.this, "group._Clicked", Toast.LENGTH_LONG).show();
                if(set_color[groupPosition] == 0){
                    set_color[groupPosition] = 1;
                    v.setBackgroundColor(0xb2bab8);
                }
                else if(set_color[groupPosition] == 1){
                    set_color[groupPosition] = 0;
                    v.setBackgroundColor(0xffffff);
                }
                return false;
            }
        });


    }
    public String apiResult(String apiUrl, String cnt, String apiReuestId, String apiReuestValue, String kyoboApiKey) {
        String result = null;
        try {


            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            JSONObject req = new JSONObject();
            for (int i = 0; i < Integer.valueOf(cnt); i++) {
                if (apiReuestValue.split("/")[i] != null) {
                    req.put(apiReuestId.split("/")[i], apiReuestValue.split("/")[i]);
                }
            }
            String reqS = "";
            reqS = "{\"dataBody\":" + req.toString() + "}";

            System.out.println("request json data===> " + reqS);
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.addRequestProperty("kyoboApiKey", kyoboApiKey);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.connect();

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            out.write(reqS);
            out.flush();
            out.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            result = sb.toString();
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(result, Object.class);
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println("response json data===> " + result);
        } catch (Exception e) {
            result = e.toString();
            Log.v("error", e.toString());
        }
        return result;
    }

    public class CustomAdapter extends BaseExpandableListAdapter{
        private ArrayList<list_item> group_data;
        private ArrayList<ArrayList<child_list_item>> child_data;
        private LayoutInflater inflater;
        public CustomAdapter(Context context, ArrayList<list_item> group, ArrayList<ArrayList<child_list_item>> child){
            this.group_data = group;
            this.child_data = child;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            return group_data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child_data.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_data.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child_data.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.activity_list, parent,false);
            }
            list_item listviewitem = group_data.get(groupPosition);

            TextView num = (TextView)convertView.findViewById(R.id.item_number);
            TextView name = (TextView)convertView.findViewById(R.id.item_name) ;
            num.setText(listviewitem.getNum() + "번 : ");
            name.setText(listviewitem.getName());
            if(set_color[groupPosition] == 0){
                //투명색, 선택되어지지 않은 경우
                Log.v("그룹", "선택 x");
                convertView.setBackgroundResource(R.color.colorRegular);
            }
            else if(set_color[groupPosition] == 1){
                //선택되어 확장된 경우
                Log.v("그룹", "선택 o");
                convertView.setBackgroundResource(R.color.colorChoose);
            }
            else if(set_color[groupPosition] == 2){
                //허리보험의 경우
                Log.v("그룹", "허리보험");
                convertView.setBackgroundResource(R.color.colorInsure);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.activity_list_child, parent,false);
            }
            child_list_item childlistitem = child_data.get(groupPosition).get(childPosition);
            TextView num = (TextView)convertView.findViewById(R.id.item_tag);
            TextView name = (TextView)convertView.findViewById(R.id.item_desc) ;

            num.setText(childlistitem.get_list_tag_desc());
            name.setText(childlistitem.get_list_desc());
            convertView.setBackgroundResource(R.color.colorChild);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }




    public class list_item{
        private String num;
        private String name;
        private String description;
        private int group_number;

        list_item(String init_num, String init_name){
            num = init_num;
            name = init_name;
            description = "";
            group_number = 17;
        }
        public int getGroup_number(){
            return group_number;
        }
        public String getNum(){
            return num;
        }
        public String getName(){
            return name;
        }
        public String getDescription(){
            return description;
        }
    }

    public class child_list_item{
        private String list_tag_desc;
        private String list_desc;

        public child_list_item(String first, String second){
            list_tag_desc = first;
            list_desc = second;
        }
        public String get_list_tag_desc(){
            return list_tag_desc;
        }
        public String get_list_desc(){
            return list_desc;
        }
    }
}