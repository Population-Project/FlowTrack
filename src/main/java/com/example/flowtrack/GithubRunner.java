package com.example.flowtrack;

import com.example.flowtrack.highwayroad.controller.HighwayController;
import com.example.flowtrack.seoulArea.controller.AreaController;
import com.example.flowtrack.seoulroad.controller.RoadController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("cli")
@RequiredArgsConstructor
public class GithubRunner implements ApplicationRunner {

    private final HighwayController highwayController;
    private final RoadController roadController;
    private final AreaController areaController;

    @Override
    public void run(ApplicationArguments args) {
        try {
            areaController.exportTraffic();
        } catch (Exception e) {
            log.error("❌ AreaController 오류: {}", e.getMessage());
        }

        try {
            highwayController.getHighwayInfo();
        } catch (Exception e) {
            log.error("❌ HighwayController 오류: {}", e.getMessage());
        }

        try {
            roadController.exportTraffic();
        } catch (Exception e) {
            log.error("❌ RoadController 오류: {}", e.getMessage());
        }

        log.info("✅ 모든 CLI 작업 완료");
    }

}
