package org.msila.treeexample.exception;

/**
 * Simple exception class made for logging errors
 */
public class TreeValidationException extends RuntimeException {

    public TreeValidationException(String message) {
        super(message);
    }
}
