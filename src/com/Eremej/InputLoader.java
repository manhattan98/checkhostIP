package com.Eremej;

import java.io.IOException;

public interface InputLoader {
    String loadRaw(String query) throws IOException, InterruptedException;
}
