public class Account {
    private String accountNumber; // 계좌번호
    private String ownerName; // 소유자 이름
    private double balance; // 잔액

    // 생성자: 모든 속성을 초기화
    public Account(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // 입금 메소드
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("%.0f원이 입금되었습니다.\n", amount);
        } else {
            System.out.println("입금 금액은 0보다 커야 합니다.");
        }
    }

    // 출금 메소드
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.printf("%.0f원이 출금되었습니다.\n", amount);
        } else {
            System.out.println("잔액 부족으로 출금할 수 없습니다.");
        }
    }

    // 현재 잔액 출력
    public void displayBalance() {
        System.out.printf("계좌번호: %s\n", accountNumber);
        System.out.printf("%s님의 현재 잔액: %.0f원\n", ownerName, balance);
        System.out.println("------------------------");
    }
}

class Main {
    public static void main(String[] args) {
        // 계좌 1
        Account acc1 = new Account("1001", "전영서", 50000);
        acc1.displayBalance();

        acc1.deposit(20000);
        acc1.displayBalance();

        acc1.withdraw(30000);
        acc1.displayBalance();

        acc1.withdraw(50000); // 출금 실패의 경우
        acc1.displayBalance();

        // 계좌 2
        Account acc2 = new Account("1002", "김필권", 100000);
        acc2.displayBalance();

        acc2.deposit(50000);
        acc2.withdraw(30000);
        acc2.displayBalance();
    }
}
