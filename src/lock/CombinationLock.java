package lock;

public class CombinationLock {

    private int combination;
    private boolean isOpen;
    private static int DEFAULT_COMBINATION = 100;

    public CombinationLock(int combination){
        if(isCombinationValid(combination)){
            this.combination = combination;
        }else{
            this.combination = DEFAULT_COMBINATION;
        }
        this.isOpen = false;
    }
    private boolean isCombinationValid(int combination){
        return combination >= 100 && combination <= 999;
    }

    public boolean open(int combination){
        this.isOpen = (this.combination == combination);
        return this.isOpen;
    }

    public boolean changeCombination(int oldCombination, int newCombination) {
        if(open(oldCombination) && isCombinationValid(newCombination)){
            this.combination = newCombination;
            return true;
        }
        return false;
    }

    public void lock(){
        this.isOpen = false;
    }

    public boolean isOpen(){
        return isOpen;
    }

    static void main(String[] args) {
        CombinationLock validLock = new CombinationLock(234);
        System.out.println(validLock.changeCombination(234,123));
        validLock.lock();
        System.out.println(validLock.changeCombination(234,777));
        CombinationLock invalidLock = new CombinationLock(1231);
    }
}
