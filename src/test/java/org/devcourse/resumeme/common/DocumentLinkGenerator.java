package org.devcourse.resumeme.common;

public interface DocumentLinkGenerator {

    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.htmlFileName, docUrl.codeName, "코드");
    }

    enum DocUrl {
        POSITION("position", "포지션"),
        EVENT_STATUS("eventStatus", "이벤트 상태"),
        PROGRESS("progress", "이벤트 참여 상태"),
        BLOCK_TYPE("blockType", "블럭 타입")
        ;

        private final String htmlFileName;

        private final String codeName;

        DocUrl(String htmlFileName, String codeName) {
            this.htmlFileName = htmlFileName;
            this.codeName = codeName;
        }

    }

}
