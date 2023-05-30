package VendingMachine;

public class Beverage {
    private String name;    // 음료 이름
    private int price;      // 음료 가격
    private int stocks;     // 음료 재고
    private int sellCount;  // 판매 개수

    public Beverage(){}
    public Beverage(String name, int price){
        this.name = name;
        this.price = price;
        this.stocks =3;     // 처음 재고는 3개
        this.sellCount =0;  
    }

    //getter
    public int getPrice(){return price;}    // 음료의 가격 반환
    public String getName(){return name;}   // 음료의 이름 반환
    public int getStock(){return stocks; }  // 음료의 재고 반환
    public int getSellCounts(){return this.sellCount;}  // 음료 팔린 개수 반환
    public int getSales(){return sellCount*price;}      // 음료 팔린 금액 반환

    public void setPrice(int price){ this.price =price;}    // 음료의 가격을 price로 변환
    public void setStocks(int plus){this.stocks+=plus;}     // 음료의 재고를 plus 만큼 추가
    public void setName(String name){this.name=name;}       // 음료의 이름을 name으로 변환
    public void setSellCount(){this.sellCount+=1;}          // 음료가 팔릴 때마다 판매 개수 증가

}


