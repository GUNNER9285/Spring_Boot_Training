package com.training.platform.services;

import java.util.concurrent.Executor;

public interface UtilsService {
    String encrytePassword(String password);
    Executor getExecutor();
}
