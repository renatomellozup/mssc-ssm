package br.com.rrodrigues.msscssm.services;

import br.com.rrodrigues.msscssm.domain.Payment;
import br.com.rrodrigues.msscssm.domain.PaymentEvent;
import br.com.rrodrigues.msscssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
}
