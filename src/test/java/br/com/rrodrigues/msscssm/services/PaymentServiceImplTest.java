package br.com.rrodrigues.msscssm.services;

import br.com.rrodrigues.msscssm.domain.Payment;
import br.com.rrodrigues.msscssm.domain.PaymentEvent;
import br.com.rrodrigues.msscssm.domain.PaymentState;
import br.com.rrodrigues.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                    .amount(new BigDecimal("12.99"))
                    .build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        System.out.println("Should be NEW");
        System.out.println(savedPayment.getState());

        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuth(savedPayment.getId());

        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

        System.out.println("Should be PRE_AUTH");
        System.out.println(stateMachine.getState().getId());

        System.out.println(preAuthedPayment);
    }

    @Transactional
    @RepeatedTest(5)
    void testAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuth(savedPayment.getId());

        if (stateMachine.getState().getId() == PaymentState.PRE_AUTH) {
            System.out.println("Payment is Pre Authorized");
            StateMachine<PaymentState, PaymentEvent> stateAuth = paymentService.authorizePayment(savedPayment.getId());
            System.out.println("Result of Auth: " + stateAuth.getState().getId());
        } else {
            System.out.println("Payment failed pre-auth");
        }

    }
}