package br.com.ascamaras.exceptions;

public class RelatorioException extends RuntimeException {
    public RelatorioException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelatorioException(String message) {
        super(message);
    }
}
