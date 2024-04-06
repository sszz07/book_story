package org.techtown.reiview_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ThreadEx extends Thread{

    private Handler handler; //쓰레드의 값등을 제어하는 클래스...

    private boolean check = true;

    //생성자
    public ThreadEx(Handler handler){
        this.handler = handler;
    }


    public void setCheck(boolean check){
        this.check = check;

    }

    //스레드가 해야할일들을 정의한 메소드..
    @Override
    public void run() {
        int value = 0;

        while(check){
            try{
                Thread.sleep(15); // 내가 지정한 시간 만큼 멈추어 주는 메소드...
            }catch (InterruptedException e){
                e.printStackTrace();
            }


            Message msg = handler.obtainMessage();


            Bundle bundle = new Bundle();

            bundle.putInt("value",value);

            msg.setData(bundle);


            //핸들러 호출
            handler.sendMessage(msg);
            value++;
        }
        super.run();
    }
}
