package com.project.sisvencal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import java.math.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio PaypalService.
 */
@Service
public class PaypalServiceImpl implements PaypalService {

    // Inyección de dependencia de APIContext
    @Autowired
    private APIContext apiContext;

    /**
     * Crea un objeto de pago utilizando la API de PayPal.
     *
     * @param total El monto total del pago.
     * @param currency La moneda del pago.
     * @param method El método de pago (ej. "paypal").
     * @param intent La intención del pago (ej. "sale").
     * @param description La descripción del pago.
     * @param cancelUrl La URL a la que se redirige si se cancela el pago.
     * @param successUrl La URL a la que se redirige si el pago es exitoso.
     * @return El objeto Payment creado.
     * @throws PayPalRESTException Si hay un error al interactuar con la API de
     * PayPal.
     */
    @Override
    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        // Configura el objeto Amount con la moneda y el monto total
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        // Configura el objeto Transaction con la descripción y el objeto Amount
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // Crea una lista de transacciones y agrega la transacción configurada
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Configura el objeto Payer con el método de pago
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Configura el objeto Payment con la intención, el objeto Payer y la lista de transacciones
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Configura las URL de redirección en caso de cancelación o éxito
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Crea el pago utilizando la APIContext proporcionada y retorna el objeto Payment creado
        return payment.create(apiContext);
    }

    /**
     * Ejecuta un pago utilizando la API de PayPal.
     *
     * @param paymentId El identificador del pago.
     * @param payerId El identificador del pagador.
     * @return El objeto Payment resultante de la ejecución del pago.
     * @throws PayPalRESTException Si hay un error al interactuar con la API de
     * PayPal.
     */
    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // Configura el objeto Payment con el identificador del pago
        Payment payment = new Payment();
        payment.setId(paymentId);

        // Configura el objeto PaymentExecution con el identificador del pagador
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        // Ejecuta el pago utilizando la APIContext proporcionada y retorna el objeto Payment resultante
        return payment.execute(apiContext, paymentExecution);
    }
}
