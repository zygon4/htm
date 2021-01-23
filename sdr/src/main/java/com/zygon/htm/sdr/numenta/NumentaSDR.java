/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.sdr.numenta;

import org.numenta.nupic.Parameters;
import org.numenta.nupic.Parameters.KEY;
import org.numenta.nupic.algorithms.Anomaly;
import org.numenta.nupic.algorithms.SpatialPooler;
import org.numenta.nupic.network.Network;
import org.numenta.nupic.network.PublisherSupplier;
import org.numenta.nupic.network.sensor.ObservableSensor;
import org.numenta.nupic.network.sensor.Publisher;
import org.numenta.nupic.network.sensor.Sensor;
import org.numenta.nupic.network.sensor.SensorParams;
import org.numenta.nupic.network.sensor.SensorParams.Keys;
import rx.Observer;

import java.util.function.Supplier;

/**
 *
 * @author zygon
 */
public class NumentaSDR {

    public NumentaSDR() {

        Parameters p = NetworkDemoHarness.getParameters();
        p = p.union(NetworkDemoHarness.getNetworkDemoTestEncoderParams());

        Supplier<Publisher> supplier = PublisherSupplier.builder()
                .addHeader("dayOfWeek")
                .build();

        Sensor<ObservableSensor<String[]>> create = Sensor.create(ObservableSensor::create, SensorParams.create(Keys::obs, new Object[]{"name", supplier}));

        Network network = Network.create("Network API Demo", p) // Name the Network whatever you wish...
                .add(Network.createRegion("Region 1") // Name the Region whatever you wish...
                        .add(Network.createLayer("Layer 2/3", p) // Name the Layer whatever you wish...
                                .alterParameter(KEY.AUTO_CLASSIFY, Boolean.TRUE) // (Optional) Add a CLAClassifier
                                .add(Anomaly.create()) // (Optional) Add an Anomaly detector
                                //                                .add(new TemporalMemory()) // Core Component but also it's "optional"
                                .add(new SpatialPooler()) // Core Component, but also "optional"
                                .add(create)));  // Sensors automatically connect to your source data, but you may omit this and pump data direction in!

        network.start();

        Publisher publisher = supplier.get();
        publisher.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onComplete");
            }

            @Override
            public void onError(Throwable thrwbl) {
                thrwbl.printStackTrace();
            }

            @Override
            public void onNext(String t) {
                System.out.println("next: " + t);
            }
        });

    }
}
