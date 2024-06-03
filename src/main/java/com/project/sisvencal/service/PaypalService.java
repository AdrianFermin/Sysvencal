package com.project.sisvencal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Interfaz que define los servicios relacionados con las transacciones de
 * PayPal.
 */
public interface PaypalService {

    /**
     * Crea un pago en PayPal.
     *
     * @param total Monto total del pago.
     * @param currency Moneda en la que se realiza el pago.
     * @param method Método de pago (por ejemplo, "paypal").
     * @param intent Intención del pago (por ejemplo, "sale").
     * @param description Descripción del pago.
     * @param cancelUrl URL a la que se redirige si el pago es cancelado.
     * @param successUrl URL a la que se redirige si el pago es exitoso.
     * @return Objeto Payment creado.
     * @throws PayPalRESTException Excepción en caso de error en la API de
     * PayPal.
     */
    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    /**
     * Ejecuta un pago en PayPal.
     *
     * @param paymentId Identificador del pago.
     * @param payerId Identificador del pagador.
     * @return Objeto Payment que representa el resultado de la ejecución del
     * pago.
     * @throws PayPalRESTException Excepción en caso de error en la API de
     * PayPal.
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
