package app;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

//https://docs.oracle.com/middleware/12211/wcs/tag-ref/MISC/TimeZones.html
//https://garygregory.wordpress.com/2013/06/18/what-are-the-java-timezone-ids/
public class Main {
    public static void main(String[] args) {
        var datetimeNow = ZonedDateTime.now();

        System.out.println("Local datetime: %s".formatted(datetimeNow));

        var datetimeUtc = datetimeNow.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("Datetime utc: %s".formatted(datetimeUtc));

        var datetimeAmericaChicago = datetimeNow.withZoneSameInstant(ZoneId.of("America/Chicago"));
        System.out.println("Datetime america chicago: %s".formatted(datetimeAmericaChicago));

        var datetimeGmtMinus6 = datetimeNow.withZoneSameInstant(ZoneId.of("Etc/GMT+6"));
        System.out.println("Datetime gmt +6: %s".formatted(datetimeGmtMinus6));

        var datetimeSystemCST = datetimeNow.withZoneSameInstant(ZoneId.of("SystemV/CST6"));
        System.out.println("Datetime system cst: %s".formatted(datetimeSystemCST));

        System.out.println("========================= offset =========================");

        var datetimeOffset = datetimeUtc.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.ofHours(-6));
        System.out.println("Offset from utc: %s".formatted(datetimeOffset));
    }
}
