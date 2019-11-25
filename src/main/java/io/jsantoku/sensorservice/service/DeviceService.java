package io.jsantoku.sensorservice.service;

import io.jsantoku.sensorservice.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private ApplicationContext applicationContext;

//    @Async
//    public CompletableFuture<List<Device>> getAllDevices() {
//        LOG.info("Request to get a list of devices");
//        final List<Device> cars = carRepository.findAll();
//        return CompletableFuture.completedFuture(cars);
//    }

}