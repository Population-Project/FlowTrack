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
        if (args.containsOption("task")) {
            String task = args.getOptionValues("task").get(0);
            log.info("🚀 [CLI 실행 시작] 실행할 작업: {}", task);

            try {
                switch (task) {
                    case "area" -> {
                        log.info("🗺️ 지역 교통 데이터 수집을 시작합니다.");
                        areaController.exportTraffic();
                        log.info("✅ 지역 교통 데이터 수집이 완료되었습니다.");
                    }
                    case "highway" -> {
                        log.info("🛣️ 고속도로 교통 데이터 수집을 시작합니다.");
                        highwayController.getHighwayInfo();
                        log.info("✅ 고속도로 교통 데이터 수집이 완료되었습니다.");
                    }
                    case "road" -> {
                        log.info("🚗 도로별 교통 데이터 수집을 시작합니다.");
                        roadController.exportTraffic();
                        log.info("✅ 도로별 교통 데이터 수집이 완료되었습니다.");
                    }
                    default -> log.warn("⚠️ 알 수 없는 작업: {}", task);
                }
            } catch (Exception e) {
                log.error("❌ [{}] 실행 중 오류 발생: {}", task, e.getMessage());
            }

        } else {
            log.info("ℹ️ 실행할 작업이 지정되지 않았습니다. (--task 인자 필요)");
            log.info("예시: --task=area | --task=highway | --task=road");
        }

        log.info("🏁 모든 CLI 작업이 종료되었습니다.");
    }


}
