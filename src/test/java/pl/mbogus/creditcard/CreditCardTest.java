package pl.mbogus.creditcard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class CreditCardTest {
    @Test
    void itAllowsToAssignCreditLimit() {
        //Arrange
        CreditCard card = new CreditCard("1234-4567");
        //Act
        card.assignCredit(BigDecimal.valueOf(1000));
        //Assert
        assertEquals(BigDecimal.valueOf(1000), card.getBalance());
    }

    @Test
    void itAllowsToAssignDifferentCreditLimit() {
        //Arrange
        CreditCard card1 = new CreditCard("1234-4567");
        CreditCard card2 = new CreditCard("1234-4567");
        //Act
        card1.assignCredit(BigDecimal.valueOf(1000));
        card2.assignCredit(BigDecimal.valueOf(1100));
        //Assert
        assertEquals(BigDecimal.valueOf(1000), card1.getBalance());
        assertEquals(BigDecimal.valueOf(1100), card2.getBalance());
    }

    @Test
    void itCantAssignLimitBelowCertainThreshold(){
        CreditCard card1 = new CreditCard("1234-4567");

        try{
            card1.assignCredit(BigDecimal.valueOf(10));
            fail("Should throw exception");
        } catch (CreditLimitBelowException e) {
            assertTrue(true);
        }

        assertThrows(CreditLimitBelowException.class, () -> card1.assignCredit(BigDecimal.valueOf(10)));
        assertThrows(CreditLimitBelowException.class, () -> card1.assignCredit(BigDecimal.valueOf(99)));
        assertDoesNotThrow(() -> card1.assignCredit(BigDecimal.valueOf(100)));

    }

    @Test
    void itAllowsWithdraws(){
        CreditCard card1 = new CreditCard("1234-1234");
        card1.assignCredit(BigDecimal.valueOf(1000));
        card1.setLimit(BigDecimal.valueOf(1000));
        card1.withdraw(BigDecimal.valueOf(10));
        assertEquals(BigDecimal.valueOf(990), card1.getBalance());
    }

    @Test
    void itCantWithdrawOverTheLimit(){
        CreditCard card1 = new CreditCard("1234-1234");
        card1.setLimit(BigDecimal.valueOf(500));
        card1.assignCredit(BigDecimal.valueOf(1000));
        assertThrows(ExceedWithdrawLimit.class, () -> card1.withdraw(BigDecimal.valueOf(501)));
    }

    @Test
    void itDenyToAssignLimitTwice() {
        CreditCard card = new CreditCard("123-123");
        card.assignCredit(BigDecimal.valueOf(1000));

        assertThrows(
                CantAssignCreditTwiceException.class,
                () -> card.assignCredit(BigDecimal.valueOf(1100))
        );
    }

    @Test
    void itDenyToWithdrawOverCredit() {
        CreditCard card = new CreditCard("123-123");
        card.assignCredit(BigDecimal.valueOf(500));
        card.setLimit(BigDecimal.valueOf(1000));

        assertThrows(
                WithdrawOverTheLimitException.class,
                () -> card.withdraw(BigDecimal.valueOf(501)));
    }
    @Test
    void itDenyToWithdrawOverTenTimes() {
        CreditCard card = new CreditCard("123-123");
        card.assignCredit(BigDecimal.valueOf(500));
        card.setLimit(BigDecimal.valueOf(500));
        while (card.getBillingCycle() < 10) card.withdraw(BigDecimal.valueOf(1));
        assertThrows(
                WithdrawOverTenTimesException.class,
                () -> card.withdraw(BigDecimal.valueOf(1)));
    }
}