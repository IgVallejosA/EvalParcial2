package com.esports.msrankings.exception;

/**
 * Excepción para fallos de comunicación con otros microservicios.
 * Se mapea a HTTP 503 SERVICE UNAVAILABLE en el GlobalExceptionHandler.
 */
public class ComunicacionMicroservicioException extends RuntimeException {
    public ComunicacionMicroservicioException(String mensaje) { super(mensaje); }
}
