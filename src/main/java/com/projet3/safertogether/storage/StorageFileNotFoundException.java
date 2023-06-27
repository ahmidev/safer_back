package com.projet3.safertogether.storage;

import java.net.MalformedURLException;

public class StorageFileNotFoundException extends Throwable {
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
