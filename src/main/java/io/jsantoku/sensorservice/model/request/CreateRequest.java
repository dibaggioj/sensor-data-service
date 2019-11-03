package io.jsantoku.sensorservice.model.request;

import io.jsantoku.sensorservice.model.DataPoint;
import io.jsantoku.sensorservice.model.data.Humidity;
import io.jsantoku.sensorservice.model.data.Temperature;

import java.io.Serializable;

public class CreateRequest implements Serializable {

    private String device;
    private long timestamp;
    private Temperature temperature;
    private Humidity humidity;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public DataPoint buildDataPoint() {
        DataPoint dataPoint = new DataPoint(device, timestamp);
        if (temperature != null) {
            dataPoint.setTemperature(temperature);
        }
        if (humidity != null) {
            dataPoint.setHumidity(humidity);
        }
        return dataPoint;
    }
}

