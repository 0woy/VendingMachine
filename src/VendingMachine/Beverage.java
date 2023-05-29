package VendingMachine;

public class Beverage {
    private String name;
    private int price;
    private int stocks;
    private int sellCount;

    public Beverage(){}
    public Beverage(String name, int price){
        this.name = name;
        this.price = price;
        this.stocks =3;
        this.sellCount =0;
    }

    //getter
    public int getPrice(){return price;}    // 음료의 가격 반환
    public String getName(){return name;}   // 음료의 이름 반환
    public int getStock(){return stocks; }  // 음료의 재고 반환
    public int getsellCounts(){return this.sellCount;}
    public int getSales(){return sellCount*price;}

    public void setPrice(int price){ this.price =price;}    // 음료의 가격을 price로 변환
    public void setStocks(int plus){this.stocks+=plus;}     // 음료의 재고를 plus 만큼 추가
    public void setName(String name){this.name=name;}       // 음료의 이름을 name으로 변환
    public void setSellCount(){this.sellCount++;}

}


