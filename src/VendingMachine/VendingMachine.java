package VendingMachine;

import java.util.HashMap;
import FileIO.Datas;

public class VendingMachine {
    private final int Max = 5000;   // 최대 투입 금액
    private Integer input;          // 사용자가 투입한 금액 & 구매 후 남은 금액
    private int checkBill;          // 1000원권 최대 투입 개수
    private Change change;          // 자판기 내의 잔돈
    private Beverage [] beverage;
    private Datas writeData;        // 음료 판매 개수 파일 작성
    public VendingMachine(){
        writeData = new Datas();
        this.input= Integer.valueOf(0); // 동적할당
        this.checkBill = 0;
        this.change = new Change();
        this.beverage = new Beverage[]{
                new Beverage("물", 450),
                new Beverage("커피", 500),
                new Beverage("이온음료", 550),
                new Beverage("고급커피", 700),
                new Beverage("탄산음료", 750)
        };
    }

    // 자판기 음료 속성 받아오기
    public String getBeverageName(int idx){return beverage[idx].getName();}
    public int getBeveragePrice(int idx){return beverage[idx].getPrice();}
    public int getBeverageStocks(int idx){return beverage[idx].getStock();}
    public int getBeverageSales(int idx){return beverage[idx].getSales();}

    // 자판기 음료 속성 변경하기
    public void setBeverageName(int idx, String name){beverage[idx].setName(name);}
    public void setBeveragePrice(int idx,int price){beverage[idx].setPrice(price);}
    public void setBeverageStocks(int idx,int stocks){beverage[idx].setStocks(stocks);}

    
    // 자판기 잔돈 받아오기
    public void setChangeStock(int idx, int plus){
        int key =0;
        switch (idx){
            case 0: key = 1000; break;
            case 1: key =500; break;
            case 2: key =100; break;
            case 3: key =50; break;
            case 4: key =10; break;
        }
        change.setChange(key,plus);
    }

    // 자판기 수금하기
    public int MoneyToManager(){return change.moneyTomanager();}

    // 화폐 투입 기능(잔돈 통은 사용자가 넣은 화폐만큼 추가됨)
    public int insertMoney(int won){

        if(input+won > Max){
            System.out.println();
            return 0;
        }

        switch (won){
            case 10:
                change.inputMoney(10);
                input+=won;
                break;
            case 50:
                change.inputMoney(50);
                input+=won;
                break;
            case 100:
                change.inputMoney(100);
                input+=won;
                break;
            case 500:
                change.inputMoney(500);
                input+=won;
                break;
            case 1000:
                if(checkBill >= 3)
                    return 1;
                else {
                    change.inputMoney(1000);
                    checkBill += 1;
                    input+=won;
                }
                break;
        }
        return 2; // 정상 종료
    }

    // 현재 사용자가 투입 & 남은 돈 반환
    public int currentInput(){return input;}
    
    // 현재 음료의 재고
    public int currentStock(int idx){return beverage[idx].getStock();}

    // 음료 구입 기능 & 파일 입력
    public void buyBeverage(int i){
        input -= beverage[i].getPrice();    // 음료 가격만큼 사용 금액 감소
        beverage[i].setSellCount();         // 해당 음료 팔린 개수 증가
        beverage[i].setStocks(-1);          // 해당 음료 재고 1개 감소
        Datas.writeSales(i);                // 파일 입력
    }

    // 화폐 단위 반환
    public int[] moneyValues(){
        return new int[]{1000,500,100,50,10};
    }

    // 남은 화폐 반환
    public HashMap<Integer, Integer> getChangeStock(){
        return change.getChanges();
    }

    // 잔돈 반환 기능
    public int[] getChanges()  {
        int [] changes ={0};
        try{
            changes = change.returnChange(input);
        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
        }
        
        // 잔돈 반환 후 초기화
        input=0;
        checkBill=0;
        return changes;
    }

    // 자판기에 존재하는 음료 개수 반환
    public int getBeverageCount(){
        return beverage.length;
    }

    public void setInput(){this.input = Integer.valueOf(0);}    // 동적 할당
    public void freeInput(){this.input=null;}                     // 동적할당 해제

}
