package org.techtown.reiview_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {

    //-> 계정을 안전하게 보호하기 위해 2022년 5월 30일부터 ​​Google은 사용자 이름과 비밀번호만 사용하여 Google 계정에 로그인하도록 요청하는 서드 파티 앱 또는 기기의 사용을 더 이상 지원하지 않습니다.
    //출처: https://support.google.com/accounts/answer/6010255?hl=ko
    //
    //세부적인 해결 방법은 다음과 같습니다.
    //
    //1. 기존 보내는 메일 서버의 Gmail 계정에 현재 2단계 인증이 걸려 있지 않다면 2단계 인증을 설정하세요. 2단계 인증을 설정하면 보안 수준이 낮은 앱의 액세스가 사용 중지됩니다.
    //
    //2. 이제 Gmail 계정이 2단계 인증을 사용하기 때문에 안내에 따라 앱 앱 비밀번호를 생성한 후, 비밀번호를 복사하거나 기억해 둡니다. 앱 비밀번호를 생성하려면 직접 다음 페이지로 이동할 수 있습니다.
    //https://myaccount.google.com/security?gar=1 (Google에 로그인-> 앱 비밀번호->재로그인)
    //
    //
    //
    //보내는 메일 서버: smtp.gmail.com으로 지정하시고, 로그온 정보는 다음과 같이 구성하여 보세요.
    //사용자 이름: username(at)gmail.com
    //비밀번호: 앱 비밀번호 (일반 비밀번호가 아님

   String user = "sg4771089@gmail.com"; // 보내는 계정의 id
    String password = "cypo yoso hasy ubhy"; // 보내는 계정의 pw

    GMailSender gMailSender = new GMailSender(user, password);
    String emailCode = gMailSender.getEmailCode();
    public void sendSecurityCode(Context context, String sendTo) {
            try {
                SharedPreferences preferences = context.getSharedPreferences("이메일인증번호", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("이메일인증번호", emailCode);
                editor.apply();

                gMailSender.sendMail("인증번호", "인증번호입니다   "+emailCode, sendTo);
                Toast.makeText(context, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (SendFailedException e) {
                Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}