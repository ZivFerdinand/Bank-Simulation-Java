package Account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateCustom {
    public Date localDateToDate(LocalDate localDate) {
        Date currDate = null;
        try {
            currDate = new SimpleDateFormat("dd/MM/yyyy").parse(String.format("%02d/%02d/%04d", 1, localDate.getMonthValue(), localDate.getYear()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return currDate;
    }
}
