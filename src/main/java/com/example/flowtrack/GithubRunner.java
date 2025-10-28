package com.example.flowtrack;

import com.example.flowtrack.highwayroad.controller.HighwayController;
import com.example.flowtrack.seoulArea.controller.AreaController;
import com.example.flowtrack.seoulroad.controller.RoadController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
@RequiredArgsConstructor
public class GithubRunner implements ApplicationRunner {

    private final HighwayController highwayController;
    private final RoadController roadController;
    private final AreaController areaController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //highwayController.getHighwayInfo();
        //roadController.exportTraffic();
        areaController.exportTraffic();
    }
}
