package FileIO;

import GUI.StartMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Datas {

    public Datas() {

    }

    // 매출 입력하기
    public static void writeSales(int beverageIdx){
        LocalDate currentDate = LocalDate.now();
        int monthValue = currentDate.getMonthValue();
        String fileName = "Month_" + monthValue;

        // 선택한 월의 날짜 수 구하기
        YearMonth yearMonth = YearMonth.of(2023, monthValue);
        int daysInMonth = yearMonth.lengthOfMonth();

        // 파일이 존재하지 않는 경우 생성 및 초기화
        File file = new File("C:\\Java_workspace\\VendingMachine\\src\\FileIO\\"+fileName+".txt");
        if (!file.exists()) {
            try {
                // 파일 생성
                file.createNewFile();
                // 각 행에 대해 초기값 0으로 설정
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < daysInMonth; j++) {
                    writer.write("0,0,0,0,0\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 파일을 읽고 지정된 행과 열에 있는 셀을 업데이트
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] rows = new String[daysInMonth];    // 일자수에 따라 행 배열 초기화
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                rows[row] = line;
                row++;
            }
            reader.close();

            // 지정된 행과 열에 있는 셀을 업데이트합니다.
            int currentDay = LocalDate.now().getDayOfMonth()-1;     // 오늘 날짜 but 파일의 인덱스는 0부터 시작이므로 -1
            int cellIdx = beverageIdx;                              // 변경할 음료의 cell 위치
            String[] rowValues = rows[currentDay].split(",");
            int cellValue = Integer.parseInt(rowValues[cellIdx]);   // 변경할 음료 cell 위치에 있는 값
            cellValue++;                                            // 해당 셀의 값 1증가
            rowValues[cellIdx] = String.valueOf(cellValue);         // 변경된 값 저장

            // 오늘 날짜에 있는 데이터 업데이트
            StringBuilder updatedRow = new StringBuilder();
            for (int j = 0; j < rowValues.length; j++) {
                updatedRow.append(rowValues[j]);
                if (j != rowValues.length - 1) {
                    updatedRow.append(",");
                }
            }
            // 업데이트된 데이터를 row 배열에 저장
            rows[currentDay] = updatedRow.toString();

            // 변경 사항이 반영된 데이터를 파일에 다시 작성
            FileWriter writer = new FileWriter(file);
            for (String rowValue : rows) {
                writer.write(rowValue + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                daySales = new String[daysInMonth+1][7];
                // 파일에서 데이터를 읽어와 테이블에 추가합니다.
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    int sum = 0;

                    // 음료별 월 총 매출액 저장
                    int [] Bsum = new int[StartMachine.vm.getBeverageCount()];
                    for (int i = 0; i < data.length; i++) {
                        data[i]=String.valueOf(Integer.parseInt(data[i]) * StartMachine.vm.getBeveragePrice(i));
                        sum += Integer.parseInt(data[i]);
                    }
                    daySales[dayIdx][0] = String.valueOf(dayIdx+1)+"일";
                    System.arraycopy(data, 0, daySales[dayIdx], 1, data.length);
                    daySales[dayIdx++][data.length+1] = String.valueOf(sum) + " 원";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            // 음료별 총 매출액
//            for(int i=0;i<StartMachine.vm.getBeverageCount();i++){
//                daySales[dayIdx][i+1] =
//
//            }
        }
        else daySales = null;
        return daySales;
    }
}

