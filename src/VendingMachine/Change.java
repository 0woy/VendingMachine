package VendingMachine;
import java.util.HashMap;

// 잔돈 확인 및 반환 클래스
public class Change {
    private HashMap<Integer,Integer> money ;

    public Change(){
        money = new HashMap<Integer, Integer>() {{
            put(10, 5);
            put(50, 5);
            put(100, 5);
            put(500, 5);
            put(1000, 5);
        }};
    }

    // 현재 자판기에 남아있는 화폐 수 반환
    public HashMap<Integer, Integer> getChanges(){return money;}

    // 화폐 일괄 충전
    public void setChangesAll(){
     for(Integer key: money.keySet()){
         if(money.get(key) >=5) continue;
         else money.put(key,5);
     }
    }

    // 특정 화폐 개별 충전
    public void setChange(int key, int idx){
        if(idx == -1)
            money.put(key,5);
        else
            money.put(key,idx);
    }

    // 사용자가 돈을 투입한 경우 해당 화폐 수 증가
    public void inputMoney(int name){money.put(name,money.get(name)+1);}

    // 잔돈 반환
    public int [] returnChange(int input) throws Exception {
        int isReturn = 0;
        int[] returnChange = new int[5];
        int [] won = new int[]{1000,500,100,50,10};

        for(Integer key: money.keySet()){
            int value = money.get(key);
            isReturn += key*value;
        }

        if(isReturn < input)
            throw new Exception("잔돈이 부족해 반환  불가합니다.");
        else {
            for(int i=0;i<won.length;i++){
                returnChange[i] = Math.min(input/won[i], money.get(won[i]));
                money.put(won[i],money.get(won[i])-returnChange[i]);
                input %=won[i];
            }
        }
        return returnChange;
    }

    // 수금
    public int moneyTomanager(){
        int sum=0;
        for(Integer key: money.keySet()) {
            if (money.get(key) < 5) {
                setChange(key,-1);
            } else {
                switch (key) {
                    case 10:
                        sum += 10 * (money.get(key) - 5);
                        break;
                    case 50:
                        sum += 50 * (money.get(key) - 5);
                        break;
                    case 100:
                        sum += 100 * (money.get(key) - 5);
                        break;
                    case 500:
                        sum += 500 * (money.get(key) - 5);
                        break;
                    case 1000:
                        sum += 1000 * (money.get(key) - 5);
                        break;
                }
                money.put(key,5);
            }
        }
        return sum;
    }
}
