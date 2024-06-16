package org.devcourse.resumeme.common;

import org.devcourse.resumeme.common.controller.EnumController.EnumDocsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnumControllerTest extends ControllerUnitTest {

    @Test
    void 상태_코드값들을_api문서에_나타낸다() throws Exception {
        //when
        ResultActions result = mvc.perform(get("/enums")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        EnumDocsResponse response = parseResult(result.andReturn());

        result.andExpect(status().isOk())
                .andDo(
                        document("enum-response",
                                customResponseFields("position", enumConvertFieldDescriptor(response.position())),
                                customResponseFields("eventStatus", enumConvertFieldDescriptor(response.eventStatus())),
                                customResponseFields("progress", enumConvertFieldDescriptor(response.progress())),
                                customResponseFields("exceptionCode", enumConvertFieldDescriptor(response.exceptionCode())),
                                customResponseFields("role", enumConvertFieldDescriptor(response.role())),
                                customResponseFields("provider", enumConvertFieldDescriptor(response.provider()))
                        )
                );

    }

    private EnumDocsResponse parseResult(MvcResult result) throws IOException {
        return mapper.readValue(result.getResponse().getContentAsByteArray(), EnumDocsResponse.class);
    }

    private CustomResponseFieldsSnippet customResponseFields(String enumName, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet("enum-response", beneathPath(enumName).withSubsectionId(enumName),
                Arrays.asList(descriptors), attributes(key("enumTypeName").value(enumName)), true);
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    public static class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

        public CustomResponseFieldsSnippet(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
                List<FieldDescriptor> descriptors, Map<String, Object> attributes,
                boolean ignoreUndocumentedFields) {
            super(type, descriptors, attributes, ignoreUndocumentedFields,
                    subsectionExtractor);
        }

        @Override
        protected MediaType getContentType(Operation operation) {
            return operation.getResponse().getHeaders().getContentType();
        }

        @Override
        protected byte[] getContent(Operation operation) {
            return operation.getResponse().getContent();
        }

    }

}
