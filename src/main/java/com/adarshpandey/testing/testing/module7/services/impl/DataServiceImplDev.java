package com.adarshpandey.testing.testing.module7.services.impl;

import com.adarshpandey.testing.testing.module7.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DataServiceImplDev implements DataService {

    @Override
    public String getData() {
        return "Dev Data";
    }

}
