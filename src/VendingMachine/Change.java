package VendingMachine;
import java.util.HashMap;

// 잔돈 확인 및 반환 클래스
public class Change {
    private HashMap<Integer,Integer> money ;

    // 해시맵으로 화폐와 개수 저장
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

    // 특정 화폐 개별 충전
    public void setChange(int key, int idx){
        // -1인 경우 5개로 일괄 충전
        if(idx == -1)
            money.put(key,5);
        
        // 그 외인 경우 idx만큼 충전
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

        // 잔돈통에 남은 금액보다 반환해야 할 금액이 많은 경우
        if(isReturn < input)
            throw new Exception("잔돈이 부족해 반환  불가합니다.");
        
        // 잔돈을 반환할 수 있는 경우
        else {
            for(int i=0;i<won.length;i++){
                returnChange[i] = Math.min(input/won[i], money.get(won[i]));
                money.put(won[i],money.get(won[i])-returnChange[i]);
                input %=won[i];
            }
        }
        return returnChange;
    }

    // 수금: 5개 초과인 화폐만 수금
    public int moneyTomanager(){
        int sum=0;
        for(Integer key: money.keySet()) {
            
            // 잔돈에 남은 화폐가 5개 미만인 경우
            if (money.get(key) < 5) {
                setChange(key,-1);  // 5개로 일괄 충전
            } 
            // 5개 초과인 경우, 5개를 남긴 나머지를 수금함
            else {
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
                // 수금 후 화폐별 남은 개수 5개로 초기화
                money.put(key,5);
            }
        }
        return sum; // 수금액 반환
    }
}
