package VendingMachine;
import VendingMachine.VendingMachine;


public class Manager extends VendingMachine {
    private String password;

    public Manager(){
        this.password ="admin@123"; // 초기 비밀번호
    }

    // 음료 일/월매출
    public int getSales(String name, String period)
    {
        return super.Sales;
    }

    // 전체 일/월 매출
    public int getSales(String period) {
     if(period.equals("day")) return super.Sales;
     else return super.totalSales;
    }

    public String getPassword(){return password;}



}
