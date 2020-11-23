package com.mazhangjing.lab.util;

import javafx.concurrent.Task;

public interface Soup {
    String getVersion();
    Task cook(String...args);
}
