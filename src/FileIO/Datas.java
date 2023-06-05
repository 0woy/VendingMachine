package FileIO;

import GUI.StartMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.time.YearMonth;

public class Datas {

    public Datas() {

    }

    // 매출 입력하기
    public void writeSales(){
        
    }

    // 선택한 월의 매출 읽어오기
    public String[][] readSales(String fileName) {
        // 데이터 파일의 경로
        String filePath = "src/FileIO/" + fileName + ".txt";
        String[][] daySales = new String[0][];

        int year = 2023;    // 현재 년도
        int month = Integer.parseInt(fileName.split("_")[1]);   // 현재 월
        
        // 선택한 월의 날짜 수 구하기
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        File dataFile = new File(filePath);

        // 데이터 파일이 존재하는 경우
        if (dataFile.exists()) {
            int dayIdx =0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                String line;
                daySales = new String[daysInMonth][7];
                // 파일에서 데이터를 읽어와 테이블에 추가합니다.
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");

                    int sum = 0;
                    for (int i = 0; i < data.length; i++) {
                        sum += Integer.parseInt(data[i]) * StartMachine.vm.getBeveragePrice(i);
                    }
                    daySales[dayIdx][0] = String.valueOf(dayIdx+1)+"일";
                    System.arraycopy(data, 0, daySales[dayIdx], 1, data.length);
                    daySales[dayIdx++][data.length+1] = String.valueOf(sum) + " 원";

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else daySales = null;
        return daySales;
    }
}

