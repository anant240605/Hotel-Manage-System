package com.HotelBookingSystem.HBS.Services;
import com.HotelBookingSystem.HBS.Entity.PaymentMethod;
import com.HotelBookingSystem.HBS.ServiceImpl.CardPaymentGateway;
import com.HotelBookingSystem.HBS.ServiceImpl.NetBankingPaymentGateway;
import com.HotelBookingSystem.HBS.ServiceImpl.UpiPaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentGatewayFactory {

    private final UpiPaymentGateway upiPaymentGateway;
    private final CardPaymentGateway cardPaymentGateway;
    private final NetBankingPaymentGateway netBankingPaymentGateway;

    public PaymentGateway getPaymentGateway(PaymentMethod paymentMethod){

        return switch (paymentMethod) {
            case UPI -> upiPaymentGateway;
            case CARD -> cardPaymentGateway;
            case NET_BANKING -> netBankingPaymentGateway;
            default -> throw new RuntimeException("Invalid Payment Method");
        };

    }

}