package VendingMachine;

public class VendingMachine {
    private final int Max = 5000;   // 최대 투입 금액
    private Integer input;              // 사용자가 투입한 금액 & 구매 후 남은 금액
    protected int Sales;            // 전체 일 매출액
    protected int totalSales;       // 전체 월 매출액
    private int checkBill;          // 1000원권 최대 투입 개수
    private Change change;          // 자판기 내의 잔돈
    private Beverage [] beverage;

    public VendingMachine(){
        this.input= Integer.valueOf(0); // 동적할당
        this.Sales=0;
        this.totalSales=0;
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

    // 자판기 음료 이름 & 가격 받아오기
    public String getBeverageName(int idx){return beverage[idx].getName();}
    public int getBeveragePrice(int idx){return beverage[idx].getPrice();}

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

    // 음료 구입 기능
    public boolean buyBeverage(String name){
        for(int i =0;i<beverage.length;i++){
            if(beverage[i].getName().equals(name) ){
                // 음료 재고가 없는 경우
                if(beverage[i].getStock()<=0)   return false;

                // 재고가 있는 경우
                else {
                    // 투입한 금액이 음료를 사기에 충분한 경우
                    if(input >= beverage[i].getPrice()) {
                        input -= beverage[i].getPrice();    // 음료 가격만큼 사용 금액 감소
                        Sales += beverage[i].getPrice();    // 음료 가격만큼 판매액 증가
                        beverage[i].setSellCount();         // 해당 음료 팔린 개수 증가
                        beverage[i].setStocks(-1);          // 해당 음료 재고 1개 감소
                        return true;
                    }
                    
                    // 재고는 있으나 음료를 살 돈이 부족한 경우
                    else return false;

                }
            }
        }
        return false;
    }

    // 화폐 단위 반환
    public int[] moneyValues(){
        return new int[]{1000,500,100,50,10};
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

    public void setInput(){this.input = Integer.valueOf(0);}    // 동적 할당
    public void freeInput(){this.input=null;}                     // 동적할당 해제

}
