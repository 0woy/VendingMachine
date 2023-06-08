package VendingMachine;
import VendingMachine.VendingMachine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Manager extends VendingMachine {
    private String password;    // 비밀번호

    public Manager(){
        this.password ="admin@123"; // 초기 비밀번호
    }

    // 현재 비밀번호 반환
    public String getPassword(){return password;}

    // 비밀번호 재설정 함수
    public boolean setPassword(String newPassword) {
        String pattern = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$";

        // 비밀번호와 패턴을 비교
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(newPassword);

        // 비밀번호 조건에 맞는 경우
        if (matcher.matches()){
            this.password = newPassword;
            return true;    // true 반환
        }
        else return false;  // false 반환
    }
}
