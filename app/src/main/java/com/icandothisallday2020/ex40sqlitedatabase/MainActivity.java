package com.icandothisallday2020.ex40sqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText name,age,email;
    String dbName="text.db";
    String tableName="member";
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.et_name);
        age=findViewById(R.id.et_age);
        email=findViewById(R.id.et_email);

        //dbName 으로 database 파일 열기(파일이없으면 생성)
        //파일을 열면 그 DB를 제어하는 객체(SQLiteDataBase)를 리턴해 줌
        db= openOrCreateDatabase(dbName,MODE_PRIVATE,null);
                //database 관리는 반드시 private mode 로,└커스터마이징 X
        //db 객체를 이용하여  DBMS 시스템(DB를 케어)에 명령(SQL)
        db.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+
        "(num integer primary key autoincrement, name text not null, age integer, email text)");
        //() 안에 no,id 사용불가//자료형 text(문자-2만개까지 저장),integer(숫자)//띄어쓰기 주의
        //primary key:중복된 값이 들어가지 않음/autoincrement:1부터 자동으로 부여됨
        // not null:필수 저장값
    }
    public void clickInsert(View view) {
        String name=this.name.getText().toString();
        int age=Integer.parseInt(this.age.getText().toString());
        String email=this.email.getText().toString();
        //데이터를 삽입하는 SQL 명령실행
        db.execSQL("INSERT INTO "+tableName+
                "(name, age, email) VALUES('"+name+"', '"+age+"', '"+email+"')");
        //넣을칸() VALUES(넣을 값)//문자를 입력할 땐 반드시 ''안에 넣어야 함
        this.name.setText(""); this.age.setText(""); this.email.setText("");
    }

    public void clickSelectAll(View view) {
        Cursor cursor=db.rawQuery("SELECT * FROM "+tableName,null);
        //*:ALL(값이 아닌 그룹명)//WHERE 절을 안쓰면 모든 데이터(record)값을 가져옴
        //(record: 데이터 한줄(관련 데이터 묶음) 단위/field=item :데이터 하나,한칸)
        //리턴된 결과표를 가리키는 Cursor 객체->한줄(row|record)이동하며 읽어들임
        if(cursor==null) return;//ERROR 방지 (표가 만들어지지 않았다면 리턴)
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()){//리턴 타입:boolean/다음 줄에 데이터가 있으면 true
            //처음 커서는 표 항목이름에있기때문에 next 하면 바로 데이터값(0row)
            //현재 가리키는 row 의 각 칸(Column)의 데이터 값을 가져오기
            int num=cursor.getInt(0);
            String name=cursor.getString(1);
            int age=cursor.getInt(cursor.getColumnIndex("age"));
            String email=cursor.getString(cursor.getColumnIndex("email"));
            buffer.append(num+" "+name+" "+age+" "+email+"\n");
        }//while...
        //누적한 문자열 데이터 결과 ->AlertDialog 에 보여주기
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(buffer.toString()).create().show();
        //Method Chaining 메소드 체이닝 리턴 값:본인->리턴된 객체 이용해 한번에 설정 작성
    }
    public void clickName(View view) {
        //이름으로 선택해 선택한 데이터만 보여주기
        String name=this.name.getText().toString();//member 테이블에서 검색할 이름
        Cursor cursor=db.rawQuery("SELECT name,email FROM "+tableName+" WHERE name=?",new String[]{name});
        if(cursor==null) return;
        StringBuffer buffer= new StringBuffer();
        while(cursor.moveToNext()){
            String sName=cursor.getString(0);
            String sEmail=cursor.getString(1);
            buffer.append(sName+" "+sEmail+"\n");
        }
        new AlertDialog.Builder(this).setMessage(buffer.toString()).create().show();
    }

    public void clickUpdate(View view) {
        String name=this.name.getText().toString();
        db.execSQL("UPDATE "+tableName+" SET age=30, email='aa@aa.com' WHERE name=?", new String[] {name});
    }

    public void clickDelete(View view) {
        String name=this.name.getText().toString();
        db.execSQL("DELETE FROM "+tableName+" WHERE name=?",new String[]{name});
    }
}



















