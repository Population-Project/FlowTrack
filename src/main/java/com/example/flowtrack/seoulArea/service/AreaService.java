package com.example.flowtrack.seoulArea.service;

import com.example.flowtrack.common.config.SeoulProps;
import com.example.flowtrack.seoulArea.dto.AreaDto;
import com.example.flowtrack.seoulArea.dto.AreaResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final SeoulProps seoulProps;
    private final RestClient restClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    /**
     * 특정 지역 정보 조회 (개별 API 호출)
     */
    public AreaDto getAreaInfo(String areaName) throws Exception {
        String url = String.format("%s/%s/xml/citydata/1/5/%s",
                seoulProps.getBaseUrl(),
                seoulProps.getApiKey(),
                areaName);

        String xmlResponse = restClient.get().uri(url).retrieve().body(String.class);

        return parseXmlToAreaDto(xmlResponse);
    }


    public void saveAreas(List<AreaDto> areas, String outPath) throws Exception {
        // append=true → 기존 파일 뒤에 이어서 씀
        boolean append = true;
        boolean fileExists = new File(outPath).exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(outPath, append))) {

            // 파일이 처음 만들어지는 경우에만 헤더 추가
            if (!fileExists) {
                writer.println("지역명,지역코드,혼잡도지표,인구최소,인구최대,남성비율,여성비율,10대비율,20대비율,30대비율,40대비율,50대비율,60대비율,70대비율,상주인구비율,비상주인구비율,업데이트시간,상권현황,결제건수,상권남성비율,상권여성비율,상권10대비율,상권20대비율,상권30대비율,상권40대비율,상권50대비율,상권60대비율,상권업데이트시간");
            } else {
                // 기존 파일이 있으면 구분을 위해 한 줄 띄움
                writer.println();
            }

            // 데이터 작성 (카드 결제 데이터 추가)
            for (AreaDto area : areas) {
                String line = String.join(",",
                        nz(area.getAreaNm()),
                        nz(area.getAreaCd()),
                        nz(area.getAreaCongestLvl()),
                        nz(area.getAreaPpltnMin()),
                        nz(area.getAreaPpltnMax()),
                        nz(area.getMalePpltnRate()),
                        nz(area.getFemalePpltnRate()),
                        nz(area.getPpltnRate10()),
                        nz(area.getPpltnRate20()),
                        nz(area.getPpltnRate30()),
                        nz(area.getPpltnRate40()),
                        nz(area.getPpltnRate50()),
                        nz(area.getPpltnRate60()),
                        nz(area.getPpltnRate70()),
                        nz(area.getResntPpltnRate()),
                        nz(area.getNonResntPpltnRate()),
                        nz(area.getPpltnTime()),

                        nz(area.getAreaCmrclLvl()),
                        nz(area.getAreaShPaymentCnt()),
                        nz(area.getCmrclMaleRate()),
                        nz(area.getCmrclFemaleRate()),
                        nz(area.getCmrcl10Rate()),
                        nz(area.getCmrcl20Rate()),
                        nz(area.getCmrcl30Rate()),
                        nz(area.getCmrcl40Rate()),
                        nz(area.getCmrcl50Rate()),
                        nz(area.getCmrcl60Rate()),
                        nz(area.getCmrclTime())
                );
                writer.println(line);
            }
        }
    }

    private AreaDto parseXmlToAreaDto(String xml) throws Exception {
        AreaResponse response = xmlMapper.readValue(xml, AreaResponse.class);

        if (response.getCityData() != null) {
            return response.getCityData().toAreaDto();  // CityData에서 직접 호출
        }

        return new AreaDto(); // 기본값 반환
    }

    private String nz(Object o) {
        return (o == null) ? "" : o.toString().replace(",", " ");
    }
}