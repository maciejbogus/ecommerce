package pl.mbogus.creditcard;

import java.math.BigDecimal;

public class CreditCard {
    private BigDecimal balance;
    private BigDecimal limit;
    private BigDecimal withdrawsAmount=BigDecimal.valueOf(0);
    private int billingCycle = 0;
    public CreditCard(String number) {
    }

    public void assignCredit(BigDecimal creditAmount) {
        if (isBelowThreshold(creditAmount)) {
            throw new CreditLimitBelowException();
        }

        if (creditIsAlreadyAssign()){
            throw new CantAssignCreditTwiceException();
        }

        this.balance = creditAmount;
    }

    public void setLimit(BigDecimal limit){
        this.limit = limit;
    }

    private boolean isBelowThreshold(BigDecimal creditAmount) {
        return creditAmount.compareTo(BigDecimal.valueOf(100)) < 0;
    }

    private boolean creditIsAlreadyAssign() {
            return this.balance != null;
    }

    private boolean exceedLimit(BigDecimal amount) {
        return this.withdrawsAmount.add(amount).compareTo(this.limit) == 1;
//        0 - "Both values are equal ";
//        1 - "First Value is greater ";
//        -1 - "Second value is greater";
    }

    public void withdraw(BigDecimal amount){
        if(exceedLimit(amount)){
            throw new ExceedWithdrawLimit();
        }

        if(this.balance.compareTo(amount) == -1){
            throw new WithdrawOverTheLimitException();
        }

        if(this.billingCycle == 10){
            throw new WithdrawOverTenTimesException();
        }
        this.withdrawsAmount = this.withdrawsAmount.add(amount);
        this.balance = this.balance.subtract(amount);
        this.billingCycle += 1;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public BigDecimal getWithdrawsAmount() {
        return withdrawsAmount;
    }

    public int getBillingCycle() {
        return billingCycle;
    }
}