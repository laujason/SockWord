package com.mingrisoft.sockword;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView timeText,dateText,wordText,englishText;   //显示单词音标
    private ImageView playVoice;                               //播放声音
    private String mMonth,mDay,mWay,mHours,mMinute;
    private SpeechSynthesizer speechSynthesizer;

    //锁屏
    private KeyguardManager km;
    private  KeyguardManager.KeyguardLock kl;
    private RadioGroup radioGroup;
    private RadioButton radioOne,radioTwo,radioThree;
    private SharedPreferences sharedPreferences;                //定义轻量级数据库
    SharedPreferences.Editor editor = null;                    //编辑数据库
    int j=0;                                                    //记录答了几道题
    List<Integer> list;                                         //判断题的数目
    List<CET4Entity> datas;                                     //用于从数据库读取相应的词库
    int k;

    /**
     * 手指按下时位置坐标（x1,y1）
     * 手指离开屏幕时坐标（x2,y2）
     */
    float x1=0,x2=0;
    float y1=0,y2=0;

    private SQLiteDatabase db;
    private DaoMaster mDaoMater,dbMaster;
    private DaoSession mDaoSession,dbSesssion;
    private CET4EntityDao questionDao,dbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
    }

    public void init(){
        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        list = new ArrayList<Integer>();

        Random r = new Random();
        int i ;
        while (list.size()<10){
            i=r.nextInt(20);
            if (!list.contains(i)){
                list.add(i);
            }
        }
        km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl=km.newKeyguardLock("unLock");


        AssetsDatabaseManager.initmanager(this);

        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();

        SQLiteDatabase db1= mg.getDatabase("word.db");

        mDaoMater = new DaoMaster(db1);

        mDaoSession = mDaoMater.newSession();
        questionDao = mDaoSession.getCET4EntityDao();

        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"wrong.db",null);

        db = helper.getWritableDatabase();
        dbMaster = new DaoMaster(db);
        dbSession = dbMaster.newSession();
        dbDao=dbSession.getCET4EntityDao();

        timeText = findViewById(R.id.time_text);
        dateText = findViewById(R.id.date_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        playVoice = findViewById(R.id.play_voice);
        radioGroup = findViewById(R.id.choose_group);
        radioOne = findViewById(R.id.choose_btn_one);
        radioTwo =findViewById(R.id.choose_btn_two);
        radioThree = findViewById(R.id.choose_btn_three);
        radioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) MainActivity.this);



    }
}
