/**  CALENDRICA APPLET

This applet is to be used in conjunction with Calendrica software.

This stripped down version runs on Macs with Netscape.

Calendrica is a package for generalized manipulation and conversion of
dates through various modern and historical calendars. It is based on
the book _Calendrical Calculations: The Millennium Edition_, by
Nachum Dershowitz and Edward M. Reingold, published by Cambridge
University Press, (c) 2001.

See http://www.calendarists.com

The Java translation was performed by Robert C. McNally
<calendrica@dangerousgames.com>.

This applet was created by N. Dershowitz for demonstration purposes only.

It uses deprecated event handling. */


import java.text.*;
import java.awt.*;
import java.applet.*;
import java.util.*;
import calendrica.*;

public class Mac extends Applet {

    // format

    static NumberFormat form = NumberFormat.getInstance();

    // calendars

    public static final int NONE = -1;
    public static final int RD = 0;
    public static final int ASTRO = 1;
    public static final int GREGORIAN = 2;
    public static final int JULIAN = 3;
    public static final int COPTIC = 4;
    public static final int ETHIOPIC = 5;
    public static final int ISO = 6;
    public static final int BAHAI = 7;
    public static final int ISLAMIC = 8;
    public static final int HEBREW = 9;
    public static final int PERSIAN = 10;
    public static final int MAYAN_LONG_COUNT = 11;
    public static final int MAYAN_HAAB = 12;
    public static final int MAYAN_TZOLKIN = 13;
    public static final int OLD_HINDU_SOLAR = 14;
    public static final int OLD_HINDU_LUNAR = 15;
    public static final int FRENCH = 16;
    public static final int MODIFIED_FRENCH = 17;
    public static final int CHINESE = 18;
    public static final int HINDU_SOLAR = 19;
    public static final int HINDU_LUNAR = 20;
    public static final int MJD = 21;
    public static final int ARITH_PERSIAN = 22;
    public static final int FUTURE_BAHAI = 23;
    public static final int DOY = 24;
    public static final int PAWUKON = 25;
    public static final int ASTRO_ISLAMIC = 26;
    public static final int ARMENIAN = 27;
    public static final int ROMAN = 28;
    public static final int MAYAN_ROUND = 29;
    public static final int EGYPTIAN = 30;

    public static String [] calendarNames =
    {"R.D. (Fixed Date)",
     "JD (Julian Day)",
     "Gregorian",
     "Julian (Old Style)",
     "Coptic",
     "Ethiopic",
     "ISO",
     "Bahai (Western)",
     "Islamic (Arithmetic)",
     "Hebrew",
     "Persian (Astronomical)",
     "Mayan Long Count",
     "Mayan Haab",
     "Mayan Tzolkin",
     "Hindu Solar (Old)",
     "Hindu Lunar (Old)",
     "French",
     "French (Modified)",
     "Chinese",
     "Hindu Solar",
     "Hindu Lunar",
     "MJD",
     "Persian (Arithmetic)",
     "Bahai (Future)",
     "Day of year",
     "Balinese Pawukon",
     "Islamic (Observational)",
     "Armenian",
     "Roman",
     "Mayan Calendar Round",
     "Egyptian"};

    static int currentCalendar = RD;
    static int [] order // in calendar menu
        = {
            RD,
            GREGORIAN,
            ISO,
            ARMENIAN,
            BAHAI,
            FUTURE_BAHAI,
            CHINESE,
            COPTIC,
            EGYPTIAN,
            ETHIOPIC,
            FRENCH,
            MODIFIED_FRENCH,
            HEBREW,
            HINDU_LUNAR,
            OLD_HINDU_LUNAR,
            HINDU_SOLAR,
            OLD_HINDU_SOLAR,
            ISLAMIC,
            ASTRO_ISLAMIC,
            JULIAN,
            ASTRO,
            MJD,
            MAYAN_LONG_COUNT,
            PERSIAN,
            ARITH_PERSIAN
        };

    static int [][] panels //
        = {{GREGORIAN},
           {JULIAN,ROMAN},
           {RD,ASTRO,MJD},
           {ISO,DOY},
           {HEBREW},
           {ISLAMIC},
           {ARITH_PERSIAN},
           {EGYPTIAN,ARMENIAN},
           {COPTIC,ETHIOPIC},
           {OLD_HINDU_LUNAR},
           {OLD_HINDU_SOLAR},
           {BAHAI},
           {PAWUKON},
           {MODIFIED_FRENCH},
           {MAYAN_LONG_COUNT,MAYAN_ROUND}
        };

    // Displayed date

    static Calendar today = new GregorianCalendar();
    static long currentDate = 1;
    static long temp; //  RD date
    static long back = 1;
    static Gregorian gd;

    // Display Fields

    static Component [] noneFields = {};

    // Fixed

    static TextField rdField = new TextField(10);
    static Component [] rdFields = {rdField};

    // DOY

    static Component [] doyFields = {};

    // Astro

    static TextField jdField = new TextField(10);
    static Component [] jdFields = {jdField};

    // mjd

    static TextField mjdField = new TextField(10);
    static Component [] mjdFields = {mjdField};

    // Gregorian

    static Choice gregorianMonthField = new Choice();
    static Choice gregorianDayField = new Choice();
    static TextField gregorianYearField = new TextField(5);
    static Component [] gregorianFields
        = {gregorianDayField,gregorianMonthField,
           gregorianYearField};

    // Julian

    static Choice julianMonthField = new Choice();
    static Choice julianDayField = new Choice();
    static TextField julianYearField = new TextField(5);
    static Choice julianBceField = new Choice ();
    static Component [] julianFields
        = {julianDayField,julianMonthField,
           julianYearField,julianBceField};

    // Roman

    static Component [] romanFields = {};

    // Armenian

    static Choice armenianMonthField = new Choice();
    static Choice armenianDayField = new Choice();
    static TextField armenianYearField = new TextField(5);
    static Component [] armenianFields
        = {armenianDayField,armenianMonthField,armenianYearField};

    // Egyptian

    static Choice egyptianMonthField = new Choice();
    static Choice egyptianDayField = new Choice();
    static TextField egyptianYearField = new TextField(5);
    static Component [] egyptianFields
        = {egyptianDayField,egyptianMonthField,egyptianYearField};

    // Coptic

    static Choice copticMonthField = new Choice();
    static Choice copticDayField = new Choice();
    static TextField copticYearField = new TextField(5);
    static Component [] copticFields
        = {copticDayField,copticMonthField,copticYearField};

    // Ethiopic

    static Choice ethiopicMonthField = new Choice();
    static Choice ethiopicDayField = new Choice();
    static TextField ethiopicYearField = new TextField(5);
    static Component [] ethiopicFields
        = {ethiopicDayField,ethiopicMonthField,ethiopicYearField};

    // ISO

    static Choice isoDayField = new Choice();
    static Choice isoWeekField = new Choice();
    static TextField isoYearField = new TextField(5);
    static Component [] isoFields
        = {isoDayField,isoWeekField,isoYearField};

    // Bahai

    static Choice bahaiDayField = new Choice();
    static Choice bahaiMonthField = new Choice();
    static Choice bahaiYearField = new Choice();
    static Choice bahaiCycleField = new Choice();
    static TextField bahaiMajorField = new TextField(5);
    static Component [] bahaiFields
        = {bahaiDayField,bahaiMonthField,
           bahaiYearField,bahaiCycleField,bahaiMajorField};

    // Bahai

    static Component [] futureBahaiFields
        = {};

    // Islamic

    static Choice islamicMonthField = new Choice();
    static Choice islamicDayField = new Choice();
    static TextField islamicYearField = new TextField(5);
    static Component [] islamicFields
        = {islamicDayField,islamicMonthField,
           islamicYearField};

    // Astro Islamic

    static Component [] astroIslamicFields
        = {};

    // Hebrew

    static Choice hebrewDayField = new Choice();
    static Choice hebrewMonthField = new Choice();
    static TextField hebrewYearField = new TextField(5);
    static Component [] hebrewFields
        = {hebrewDayField,hebrewMonthField,
           hebrewYearField};

    // Persian

    static Component [] persianFields
        = {};

    // Arith Persian

    static Choice arithPersianDayField = new Choice();
    static Choice arithPersianMonthField = new Choice();
    static TextField arithPersianYearField = new TextField(5);
    static Component [] arithPersianFields
        = {arithPersianDayField,arithPersianMonthField,
           arithPersianYearField};

    // Mayan Long

    static Choice kinField = new Choice();
    static Choice uinalField = new Choice();
    static Choice tunField = new Choice();
    static Choice katunField = new Choice();
    static TextField baktunField = new TextField(5);
    static Component [] mayanLongCountFields
        = {baktunField, katunField, tunField, uinalField, kinField};

    // Mayan Calendar Round

    static Component [] roundFields = {};

    // Mayan Haab

    //  static Choice haabDayField = new Choice();
    //  static Choice haabMonthField = new Choice ();
    static Component [] haabFields = {};

    // Mayan Tzolkin

    //  static Choice tzolkinDayField = new Choice();
    //  static Choice tzolkinMonthField = new Choice ();
    static Component [] tzolkinFields = {};

    // Pawukon

    static Component [] pawukonFields = {};

    // Old Hindu Solar

    static Choice oldHinduSolarDayField = new Choice();
    static Choice oldHinduSolarMonthField = new Choice();
    static TextField oldHinduSolarYearField = new TextField(5);
    static Component [] oldHinduSolarFields
        = {oldHinduSolarDayField,
           oldHinduSolarMonthField,
           oldHinduSolarYearField};

    // Old Hindu Lunar

    static Choice oldHinduLunarDayField = new Choice();
    static Choice oldHinduLunarLeapField = new Choice();
    static Choice oldHinduLunarMonthField = new Choice();
    static TextField oldHinduLunarYearField = new TextField(5);
    static Component [] oldHinduLunarFields
        = {oldHinduLunarDayField,
           oldHinduLunarMonthField,
           oldHinduLunarLeapField,
           oldHinduLunarYearField};

    // French

    static Component [] frenchFields
        = {};

    // Modified French

    static Choice modifiedFrenchDayField = new Choice();
    static Choice modifiedFrenchWeekField = new Choice();
    static Choice modifiedFrenchMonthField = new Choice();
    static TextField modifiedFrenchYearField = new TextField(5);
    static Component [] modifiedFrenchFields
        = {modifiedFrenchWeekField,
           modifiedFrenchDayField,
           modifiedFrenchMonthField,
           modifiedFrenchYearField};

    // Chinese

    static Component [] chineseFields
        = {};

    // Hindu Solar

    static Component [] hinduSolarFields
        = {};

    // Hindu Lunar

    static Component [] hinduLunarFields
        = {};

    // All calendars

    static Component [] [] calendarFields =
    {noneFields,rdFields,jdFields,
     gregorianFields,julianFields,
     copticFields,ethiopicFields,
     isoFields,bahaiFields,
     islamicFields,hebrewFields,persianFields,
     mayanLongCountFields,
     haabFields,tzolkinFields,
     oldHinduSolarFields,oldHinduLunarFields,
     frenchFields,modifiedFrenchFields,
     chineseFields,
     hinduSolarFields,hinduLunarFields,
     mjdFields,arithPersianFields,futureBahaiFields,
     doyFields, pawukonFields, astroIslamicFields, armenianFields,
     romanFields, roundFields, egyptianFields
    };

    static Choice calendar = new Choice ();
    static String [] displayResults = new String [calendarNames.length];
    static Label astroResult = new Label("",Label.CENTER);
    static Choice monthLabel = new Choice();
    static TextField yearLabel = new TextField(5);

    static Panel
        // Display consists of
        topPanel = new Panel(),
        // which consists of
        movePanel = new Panel(),
        entryPanel = new Panel(),
        displayPanel = new Panel(),
        // which consists of
        astroPanel = new Panel(), // also for error notices
        resultPanels [] = new Panel [panels.length],
        ccPanel = new Panel(), // copyright notice
        // which consists of
        month = new Panel(),
        monthHead = new Panel(),
        monthMain = new Panel(),
        monthPanel = new Panel();

    static Button bToday = new Button("Today"),
        bBack = new Button("Back"),
        bPreviousDay = new Button("Prev. Day"),
        bFollowingDay = new Button("Next Day"),
        bFollowingMonth = new Button(">"),
        bPreviousMonth = new Button("<"),
        bFollowingYear = new Button(">>"),
        bPreviousYear = new Button("<<");

    static Button [] monthButtons = new Button [32]; // for days of month
    static Button [] resultButtons = new Button [calendarNames.length]; 

    static Color
        pale = Color.lightGray,
        fore = Color.cyan,
        blue = new Color(0,0,100),    // dark blue
        lite = Color.yellow,
        dark = new Color(100,100,180),   // indigo-ish
        fire = Color.red;

    static boolean first = true;

    public void init () {

        if (first) {
            first = false;

            // Calendar choices

            for (int i=0;i<order.length;i++)
                calendar.addItem(calendarNames[order[i]]);

            // Main Layout

            setLayout(new BorderLayout());
            add("North",topPanel);
            add("Center",month);

            topPanel.setLayout(new BorderLayout());
            topPanel.add("North", movePanel);
            topPanel.add("Center", entryPanel);
            topPanel.add("South", displayPanel);
            topPanel.setBackground(dark);
            topPanel.setForeground(fore);

            // Move Panel 

            movePanel.setBackground(blue);
            movePanel.setForeground(fore);
            movePanel.setLayout(new FlowLayout());
            movePanel.add(bBack);
            movePanel.add(new Label("    "));
            movePanel.add(bPreviousDay);
            movePanel.add(new Label("     CALENDRICAL CALCULATIONS     "));
            movePanel.add(bFollowingDay);
            movePanel.add(new Label("    "));
            movePanel.add(bToday);
            bBack.setBackground(blue);
            bBack.setForeground(fore);
            bFollowingDay.setBackground(blue);
            bFollowingDay.setForeground(fore);
            bPreviousDay.setBackground(blue);
            bPreviousDay.setForeground(fore);
            bToday.setBackground(blue);
            bToday.setForeground(fore);

            // entryPanel

            entryPanel.setBackground(pale);
            entryPanel.setForeground(fore);
            calendar.setBackground(blue);
            calendar.setForeground(fore);
            for (int c=0; c<calendarFields.length; c++) 
                for (int i=0; i<calendarFields[c].length; i++) {
                    calendarFields[c][i].setBackground(blue);
                    calendarFields[c][i].setForeground(fore);
                };

            // Choice fields

            for (int i=1; i<=31; i++)
                gregorianDayField.addItem(i+"");
            for (int i=0; i<Gregorian.monthNames.length; i++)
                gregorianMonthField.addItem(Gregorian.monthNames[i]);

            for (int i=1; i<=31; i++)
                julianDayField.addItem(i+"");
            for (int i=0; i<Gregorian.monthNames.length; i++)
                julianMonthField.addItem(Gregorian.monthNames[i]);
            julianBceField.addItem("B.C.E.");
            julianBceField.addItem("C.E.");

            for (int i=1; i<=30; i++)
                armenianDayField.addItem(i+"");
            for (int i=0; i<Armenian.monthNames.length; i++)
                armenianMonthField.addItem(Armenian.monthNames[i]);

            for (int i=1; i<=30; i++)
                egyptianDayField.addItem(i+"");
            for (int i=0; i<Egyptian.monthNames.length; i++)
                egyptianMonthField.addItem(Egyptian.monthNames[i]);

            for (int i=1; i<=30; i++)
                copticDayField.addItem(i+"");
            for (int i=0; i<Coptic.monthNames.length; i++)
                copticMonthField.addItem(Coptic.monthNames[i]);

            for (int i=1; i<=30; i++)
                ethiopicDayField.addItem(i+"");
            for (int i=0; i<Ethiopic.monthNames.length; i++)
                ethiopicMonthField.addItem(Ethiopic.monthNames[i]);

            for (int i=0; i<Gregorian.dayOfWeekNames.length; i++)
                isoDayField.addItem(Gregorian.dayOfWeekNames[(i+1)%7]);
            for (int i=1; i<=53; i++)
                isoWeekField.addItem(i+"");

            for (int i=0; i<Bahai.dayOfMonthNames.length; i++)
                bahaiDayField.addItem(Bahai.dayOfMonthNames[i]);
            for (int i=0; i<Bahai.monthNames.length; i++)
                bahaiMonthField.addItem(Bahai.monthNames[i]);
            for (int i=0; i<Bahai.yearNames.length; i++)
                bahaiYearField.addItem(Bahai.yearNames[i]);
            for (int i=1; i<=19; i++)
                bahaiCycleField.addItem(i+"");

            for (int i=1; i<=30; i++)
                islamicDayField.addItem(i+"");
            for (int i=0; i<Islamic.monthNames.length; i++)
                islamicMonthField.addItem(Islamic.monthNames[i]);

            for (int i=0; i<=19; i++)
                kinField.addItem(i+"");
            for (int i=0; i<=17; i++)
                uinalField.addItem(i+"");
            for (int i=0; i<=19; i++)
                tunField.addItem(i+"");
            for (int i=0; i<=19; i++)
                katunField.addItem(i+"");

            for (int i=1; i<=31; i++)
                oldHinduSolarDayField.addItem(i+"");
            for (int i=0; i<OldHinduSolar.monthNames.length; i++)
                oldHinduSolarMonthField.addItem(OldHinduSolar.monthNames[i]);

            for (int i=1; i<=30; i++)
                oldHinduLunarDayField.addItem(i+"");
            for (int i=0; i<OldHinduLunar.monthNames.length; i++)
                oldHinduLunarMonthField.addItem(OldHinduLunar.monthNames[i]);
            oldHinduLunarLeapField.addItem("");
            oldHinduLunarLeapField.addItem("leap");

            for (int i=1; i<=30; i++)
                hebrewDayField.addItem(i+"");
            for (int i=0; i<Hebrew.monthNames.length; i++)
                hebrewMonthField.addItem(Hebrew.monthNames[i]);
            hebrewMonthField.addItem(Hebrew.leapYearMonthNames[12]); // Adar II

            for (int i=1; i<=31; i++)
                arithPersianDayField.addItem(i+"");
            for (int i=0; i<ArithmeticPersian.monthNames.length; i++)
                arithPersianMonthField.addItem(ArithmeticPersian.monthNames[i]);

            for (int i=0; i<French.dayOfWeekNames.length; i++)
                modifiedFrenchDayField.addItem(French.dayOfWeekNames[i]);
            for (int i=0; i<French.specialDayNames.length; i++)
                modifiedFrenchDayField.addItem(French.specialDayNames[i]);
            modifiedFrenchWeekField.addItem("I");
            modifiedFrenchWeekField.addItem("II");
            modifiedFrenchWeekField.addItem("III");
            modifiedFrenchWeekField.addItem("");
            for (int i=0; i<French.monthNames.length; i++)
                modifiedFrenchMonthField.addItem(French.monthNames[i]);

            // Results

            displayPanel = new Panel();
            displayPanel.setLayout(new GridLayout(panels.length+2,1));
            displayPanel.setBackground(dark);
            displayPanel.setForeground(dark);
            displayPanel.setFont(new Font("Helvetica",Font.BOLD,10));

            displayPanel.add(astroPanel);
            astroPanel.add(astroResult);

            for (int i=0;i<resultButtons.length;i++)
                resultButtons[i] = new Button ("");

            for (int i=0;i<resultPanels.length;i++) {
                resultPanels[i] = new Panel ();
                resultPanels[i].setLayout(new FlowLayout());
                displayPanel.add(resultPanels[i]);
                for (int j=0;j<panels[i].length;j++)
                    resultPanels[i].add(resultButtons[panels[i][j]]);};

            displayPanel.add(ccPanel);
            ccPanel.setBackground(dark);
            ccPanel.setForeground(fore);
            ccPanel.add
              (new Label
               ("Based on Calendrical Calculations: The Millennium Ed.,"+
                " C.U.P., (c) 2001 E. M. Reingold and N. Dershowitz.",
                Label.CENTER));

            // Month Panel

            month.setLayout(new BorderLayout());
            month.add("North",monthHead);
            month.add("Center",monthMain);
            month.setBackground(dark);
            month.setForeground(blue);

            monthHead.setForeground(fore);
            monthHead.setBackground(blue);
            monthHead.setLayout(new FlowLayout());
            monthHead.add(new Label("    "));
            monthHead.add(bPreviousMonth);
            monthHead.add(monthLabel);
            monthHead.add(bFollowingMonth);
            monthHead.add(new Label("    "));
            monthHead.add(bPreviousYear);
            monthHead.add(yearLabel);
            monthHead.add(bFollowingYear);
            monthHead.add(new Label("    "));

            yearLabel.setBackground(blue);
            yearLabel.setForeground(fore);
            monthLabel.setBackground(blue);
            monthLabel.setForeground(fore);
            bFollowingMonth.setBackground(blue);
            bFollowingMonth.setForeground(fore);
            bFollowingYear.setBackground(blue);
            bFollowingYear.setForeground(fore);
            bPreviousMonth.setBackground(blue);
            bPreviousMonth.setForeground(fore);
            bPreviousYear.setBackground(blue);
            bPreviousYear.setForeground(fore);

            for (int i=0; i<Gregorian.monthNames.length; i++)
                monthLabel.addItem(Gregorian.monthNames[i]);

            monthMain.setLayout(new BorderLayout());
            monthMain.add("East",new Label("    "));
            monthMain.add("West",new Label("    "));
            monthMain.add("Center",monthPanel);

        }

        // Go

        selectCalendar(GREGORIAN);
        today();

    }

    private void selectCalendar (int c) {
        int d = 0; // place in list
        for (int i=0;i<order.length;i++)
            if (order[i]==c) d = i;
        if (currentCalendar != c) {
            calendar.select(d);
            resultButtons[currentCalendar].setBackground(blue);
            resultButtons[currentCalendar].setForeground(fore);
            topPanel.remove(entryPanel);
            entryPanel = new Panel();
            entryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            entryPanel.add(calendar);
            entryPanel.add(new Label("    "));
            for (int i=0; i<calendarFields[c+1].length; i++) {
                entryPanel.add(calendarFields[c+1][i]);
                calendarFields[c+1][i].setBackground(blue);
            }
            topPanel.add("Center", entryPanel);
            clearError(currentDate);
        };
        if (calendarFields[c+1].length==0)
            displayError("Sorry, not applicable!");
        currentCalendar = c;
        resultButtons[currentCalendar].setBackground(fore);
        resultButtons[currentCalendar].setForeground(blue);
        redo();
    }

    private void redo () {
        validate();
        repaint();
    }

    private void displayBogus (int c, long d) {
        displayError("Nonexistent "+calendarNames[c]+" date!");};

    private void displayBug (long d, calendrica.Date a) {
        displayError("Error: "+d+"="+a.toString()+"!="+a.toFixed() +
                     "; report if within 10K years!");};

    private void displayError (String s) {
        topPanel.remove(displayPanel);
        astroPanel.remove(astroResult);
        astroResult = new Label(s);
        astroPanel.add(astroResult);
        astroPanel.setBackground(fire);
        astroResult.setBackground(fire);
        astroResult.setForeground(lite);
        topPanel.add("South",displayPanel);
        redo();
    }

    private void clearError (long d) {
            topPanel.remove(displayPanel);
            astroPanel.remove(astroResult);
            astroResult = 
                new Label("");
            astroPanel.setBackground(dark);
            astroPanel.setForeground(dark);
            astroResult.setBackground(dark);
            astroResult.setForeground(fore);
            astroPanel.add(astroResult);
            topPanel.add("South",displayPanel);
            redo();
    }

    private void displayMonth (Gregorian g) {

        Gregorian t = new Gregorian(currentDate);
        if ((g.month == t.month)
            && (g.year == t.year)) {
            monthButtons[t.day].setBackground(blue);
            monthButtons[t.day].setForeground(fore);
            monthButtons[g.day].setBackground(fore);
            monthButtons[g.day].setForeground(blue);
        }
        else {
            Gregorian s = new Gregorian(g.year,g.month,1);
            int dow = calendrica.Date.mod((int)s.toFixed(),7);
            int lom = g.lastDayOfMonth();

            remove(month);
            month.remove(monthHead);
            monthLabel.select(g.month-1);
            yearLabel.setText(g.year+"");
            month.add("North",monthHead);
            add("Center",month);

            monthMain.remove(monthPanel);
            monthPanel = new Panel();
            monthPanel.setLayout(new GridLayout(7,7));
            monthPanel.setBackground(dark);
            monthPanel.setForeground(fore);
            monthMain.add("Center",monthPanel);

            for (int d=0;d<=6;d++)
                monthPanel.add(new Label(Gregorian.dayOfWeekNames[d],Label.CENTER));

            for (int w=0;w<=5;w++)
                for (int d=0;d<=6;d++) {
                    int l = w*7+d-dow+1;
                    if ((l>0)&&(l<=lom)) {
                        Button but = new Button(l+"");
                        monthButtons[l] = but;
                        monthPanel.add(but);
                        but.setForeground(fore);
                        if (l==g.day) {
                            but.setBackground(fore);
                            but.setForeground(blue);}
                        else {
                            but.setBackground(blue);
                            but.setForeground(fore);}
                    }
                    else {
                        Panel blank = new Panel();
                        blank.add(new Label(""));
                        monthPanel.add(blank);
                    };
                };

        };

        redo();

    }

    private void displayDate (int d, int m, long y) {
        displayDate(new Gregorian(y,m,d).toFixed()); };

    private void displayDate (long d) {

        clearError(d);

        rdField.setText(d+"");
        displayResults[RD] =
            "Fixed: "+d+" R.D.";

        long jd = (long)Math.ceil(ProtoDate.jdFromFixed(d));
        jdField.setText(jd+"");
        displayResults[ASTRO] =
            "Julian Day (at noon): "+jd+" j.d.";

        long mjd = (int)ProtoDate.mjdFromFixed(d);
        mjdField.setText(mjd+"");
        displayResults[MJD] =
            mjd +" (Modified)";

        gd = new Gregorian(d);
        if (d != gd.toFixed()) displayBug(d,gd);
        gregorianDayField.select(gd.day-1);
        gregorianMonthField.select(gd.month-1);
        gregorianYearField.setText(gd.year+"");
        displayResults[GREGORIAN] =
            gd.format() +" (Gregorian)";

        displayResults[DOY] =
            "Day "+gd.dayNumber()+" in "+gd.year;

        Julian juld = new Julian(d);
        if (d != juld.toFixed()) displayBug(d,juld);
        julianDayField.select(juld.day-1);
        julianMonthField.select(juld.month-1);
        if (juld.year < 0)
            julianBceField.select(0);
        else
            julianBceField.select(1);
        julianYearField.setText(Math.abs(juld.year)+"");
        displayResults[JULIAN] =
            "Julian: "+juld.format();

        Armenian ad = new Armenian(d);
        if (d != ad.toFixed()) displayBug(d,ad);
        armenianDayField.select(ad.day-1);
        armenianMonthField.select(ad.month-1);
        armenianYearField.setText(ad.year+"");
        displayResults[ARMENIAN] =
            "Armenian: "+ad.format();

        Egyptian yd = new Egyptian(d);
        if (d != yd.toFixed()) displayBug(d,yd);
        egyptianDayField.select(yd.day-1);
        egyptianMonthField.select(yd.month-1);
        egyptianYearField.setText(yd.year+"");
        displayResults[EGYPTIAN] =
            "Egyptian: "+yd.format();

        Coptic cd = new Coptic(d);
        if (d != cd.toFixed()) displayBug(d,cd);
        copticDayField.select(cd.day-1);
        copticMonthField.select(cd.month-1);
        copticYearField.setText(cd.year+"");
        displayResults[COPTIC] =
            "Coptic: "+cd.format();

        Ethiopic ed = new Ethiopic(d);
        if (d != ed.toFixed()) displayBug(d,ed);
        ethiopicDayField.select(ed.day-1);
        ethiopicMonthField.select(ed.month-1);
        ethiopicYearField.setText(ed.year+"");
        displayResults[ETHIOPIC] =
            "Ethiopic: "+ed.format();

        calendrica.ISO isd = new calendrica.ISO(d);
        if (d != isd.toFixed()) displayBug(d,isd);
        isoWeekField.select(isd.week-1);
        isoDayField.select(isd.day-1);
        isoYearField.setText(isd.year+"");
        displayResults[ISO] =
            "ISO: "+isd.format();

        Bahai bd = new Bahai(d);
        if (d != bd.toFixed()) displayBug(d,bd);
        bahaiDayField.select(bd.day-1);
        bahaiMonthField.select(bd.month);
        bahaiYearField.select(bd.year-1);
        bahaiCycleField.select(bd.cycle-1);
        bahaiMajorField.setText(bd.major+"");
        displayResults[BAHAI] =
            "Bah'a'i (until sunset): "+bd.format() +" (Western)";

        Islamic id = new Islamic(d);
        if (d != id.toFixed()) displayBug(d,id);
        islamicDayField.select(id.day-1);
        islamicMonthField.select(id.month-1);
        islamicYearField.setText(id.year+"");
        displayResults[ISLAMIC] =
            "Islamic (until sunset): "+id.format() +" (Arithmetic)";

        Hebrew hd = new Hebrew(d);
        if (d != hd.toFixed()) displayBug(d,hd);
        hebrewDayField.select(hd.day-1);
        hebrewMonthField.select(hd.month-1);
        hebrewYearField.setText(hd.year+"");
        displayResults[HEBREW] =
            "Hebrew (until sunset): "+hd.format();

        ArithmeticPersian apd = new ArithmeticPersian(d);
        if (d != apd.toFixed()) displayBug(d,apd);
        arithPersianDayField.select(apd.day-1);
        arithPersianMonthField.select(apd.month-1);
        arithPersianYearField.setText(apd.year+"");
        displayResults[ARITH_PERSIAN] =
            "Persian: "+apd.format() +" (Arithmetic)";

        OldHinduSolar ohsd = new OldHinduSolar(d);
        if (d != ohsd.toFixed()) displayBug(d,ohsd);
        oldHinduSolarDayField.select(ohsd.day-1);
        oldHinduSolarMonthField.select(ohsd.month-1);
        oldHinduSolarYearField.setText(ohsd.year+"");
        displayResults[OLD_HINDU_SOLAR] =
            "Hindu Solar: "+ohsd.format() + " (Old)";

        OldHinduLunar ohld = new OldHinduLunar(d);
        if (d != ohld.toFixed()) displayBug(d,ohld);
        oldHinduLunarDayField.select(ohld.day-1);
        oldHinduLunarMonthField.select(ohld.month-1);
        oldHinduLunarYearField.setText(ohld.year+"");
        if (ohld.leapMonth)
            oldHinduLunarLeapField.select(1);
        else
            oldHinduLunarLeapField.select(0);
        displayResults[OLD_HINDU_LUNAR] =
            "Hindu Lunar: "+ohld.format() + " (Old)";

        MayanLongCount mlc = new MayanLongCount(d);
        if (d != mlc.toFixed()) displayBug(d,mlc);
        kinField.select(mlc.kin);
        uinalField.select(mlc.uinal);
        tunField.select(mlc.tun);
        katunField.select(mlc.katun);
        baktunField.setText(mlc.baktun+"");
        displayResults[MAYAN_LONG_COUNT] = 
          "Mayan: "+mlc.format()+" (Long Count)";

        MayanHaab mhd = new MayanHaab(d);
        //        displayResults[MAYAN_HAAB] = mhd.format()+" (Haab)";
        MayanTzolkin mtd = new MayanTzolkin(d);
        //        displayResults[MAYAN_TZOLKIN] = mtd.format()+" (Tzolkin)";
        displayResults[MAYAN_ROUND] = mhd.format()+" "+
            mtd.format()+" (Calendar Round)";

        Balinese paw = new Balinese(d);
        displayResults[PAWUKON] = 
          "Balinese: "+paw.format();

        Roman rom = new Roman(d);
        displayResults[ROMAN] = 
          rom.format()+" (Roman style)";

        ModifiedFrench mfd = new ModifiedFrench(d);
        if (d != mfd.toFixed()) displayBug(d,mfd);
        modifiedFrenchMonthField.select(mfd.month-1);
        if (mfd.month<13) {
            modifiedFrenchDayField.select((mfd.day-1)%10);
            modifiedFrenchWeekField.select((mfd.day-1)/10);}
        else {
            modifiedFrenchDayField.select(mfd.day+9);
            modifiedFrenchWeekField.select(3);};
        modifiedFrenchYearField.setText(mfd.year+"");
        displayResults[MODIFIED_FRENCH] = 
          "French: "+mfd.format() +" (Modified)";

        for (int i=0;i<resultPanels.length;i++) {
            for (int j=0;j<panels[i].length;j++) {
                int p = panels[i][j];
                resultPanels[i].remove(resultButtons[p]);
                resultButtons[p] = new Button(displayResults[p]);
                resultButtons[p].setBackground(blue);
                resultButtons[p].setForeground(fore);
                resultPanels[i].add(resultButtons[p]);
            };
        };

        resultButtons[currentCalendar].setBackground(fore);
        resultButtons[currentCalendar].setForeground(blue);

        displayMonth(gd);

        back = currentDate;
        currentDate = d;

        // may be helpful on Mac
        // colors not right anyway
        remove(topPanel);
        topPanel.remove(movePanel);
        topPanel.remove(entryPanel);
        topPanel.remove(displayPanel);
        topPanel.add("North", movePanel);
        topPanel.add("Center", entryPanel);
        topPanel.add("South", displayPanel);
        topPanel.setBackground(dark);
        topPanel.setForeground(fore);
        add("North",topPanel);
        redo();

    }

    private void today () {
        gd = new Gregorian(today.get(Calendar.YEAR),
                           1+today.get(Calendar.MONTH),
                           today.get(Calendar.DATE));
        displayDate(gd.toFixed());
    }

    boolean check (Event e, int c) {
        for (int i=0; i<calendarFields[c+1].length; i++)
            if (e.target == calendarFields[c+1][i]) return true;
        return false;}

    boolean checkPanel (Event e, int c) {
        return (e.target == resultButtons[c]) ;}

    boolean checkDay (Event e, int d) {
        if (d > gd.lastDayOfMonth()) return false;
        return (e.target == monthButtons[d]);
    }

    public boolean action (Event e, Object o) {     // deprecated!

        calendrica.Date cd;
        if (e.target == calendar)
            selectCalendar(order[calendar.getSelectedIndex()]);
        else if (e.target == bToday)
            today();
        else if (e.target == bBack)
            displayDate(back);
        else if (e.target == monthLabel)
            displayDate
                (Math.min(gd.day,
                          new Gregorian
                              (gd.year,
                               monthLabel.getSelectedIndex()+1,
                               gd.day).lastDayOfMonth()),
                 monthLabel.getSelectedIndex()+1,gd.year);
        else if (e.target == yearLabel)
            displayDate
                (Math.min(gd.day,
                          new Gregorian
                              (Long.parseLong(yearLabel.getText()),
                               gd.month,gd.day).lastDayOfMonth()),
                 gd.month,Long.parseLong(yearLabel.getText()));
        else if (e.target == bFollowingDay)
            displayDate(currentDate+1);
        else if (e.target == bPreviousDay)
            displayDate(currentDate-1);
        else if (e.target == bFollowingYear)
            displayDate(Math.min(gd.day,
                                 new Gregorian
                                     (gd.year+1,gd.month,1).lastDayOfMonth()),
                        gd.month,gd.year+1);
        else if (e.target == bPreviousYear)
            displayDate(Math.min(gd.day,
                                 new Gregorian
                                     (gd.year-1,gd.month,1).lastDayOfMonth()),
                        gd.month,gd.year-1);
        else if (e.target == bFollowingMonth)
            {if (gd.month==Gregorian.DECEMBER)
                displayDate(gd.day,Gregorian.JANUARY,gd.year+1);
            else
                displayDate(Math.min(gd.day,
                                     new Gregorian
                                         (gd.year,gd.month+1,1).lastDayOfMonth()),
                            gd.month+1,gd.year);}
        else if (e.target == bPreviousMonth)
            {if (gd.month==Gregorian.JANUARY)
                displayDate(gd.day,Gregorian.DECEMBER,gd.year-1);
            else
                displayDate(Math.min(gd.day,
                                     new Gregorian
                                         (gd.year,gd.month-1,1).lastDayOfMonth()),
                            gd.month-1,gd.year);}
        else try {
            if (check(e,RD))
                displayDate(Long.parseLong(rdField.getText()));
            else if (check(e,ASTRO)) {
                double icd = (double)Long.parseLong(jdField.getText());
                displayDate(ProtoDate.fixedFromJD(icd));}
            else if (check(e,MJD)) {
                double icd = (double)Long.parseLong(mjdField.getText());
                displayDate(ProtoDate.fixedFromMJD(icd));}
            else if (check(e,GREGORIAN))
                {
                    try {
                        cd =
                            new Gregorian
                                (Long.parseLong(gregorianYearField.getText()),
                                 1+gregorianMonthField.getSelectedIndex(),
                                 1+gregorianDayField.getSelectedIndex());
                        temp = cd.toFixed();
                        if (!cd.equals(new Gregorian (temp)))
                            throw new BogusDateException();
                        displayDate(temp);}
                    catch (Exception b) {displayBogus(GREGORIAN,temp);};
                }
            else if (check(e,JULIAN))
                {
                    long year;
                    if (julianBceField.getSelectedIndex() == 0)
                        year = - Long.parseLong(julianYearField.getText());
                    else
                        year = Long.parseLong(julianYearField.getText());
                    try {
                        cd =
                            new Julian
                                (year,
                                 1+julianMonthField.getSelectedIndex(),
                                 1+julianDayField.getSelectedIndex());
                        temp = cd.toFixed();
                        if (!cd.equals(new Julian(temp)))
                            throw new BogusDateException();
                        displayDate(temp);}
                    catch (Exception b) {displayBogus(JULIAN,temp);};
                }
            else if (check(e,COPTIC))
                {
                    try { cd =
                              new Coptic
                                  (Long.parseLong(copticYearField.getText()),
                                   1+copticMonthField.getSelectedIndex(),
                                   1+copticDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Coptic(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(COPTIC,temp);};
                }
            else if (check(e,ETHIOPIC))
                {
                    try { cd =
                              new Ethiopic
                                  (Long.parseLong(ethiopicYearField.getText()),
                                   1+ethiopicMonthField.getSelectedIndex(),
                                   1+ethiopicDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Ethiopic(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(ETHIOPIC,temp);};
                }
            else if (check(e,ARMENIAN))
                {
                    try { cd =
                              new Armenian
                                  (Long.parseLong(armenianYearField.getText()),
                                   1+armenianMonthField.getSelectedIndex(),
                                   1+armenianDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Armenian(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(ARMENIAN,temp);};
                }
            else if (check(e,EGYPTIAN))
                {
                    try { cd =
                              new Egyptian
                                  (Long.parseLong(egyptianYearField.getText()),
                                   1+egyptianMonthField.getSelectedIndex(),
                                   1+egyptianDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Egyptian(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(EGYPTIAN,temp);};
                }
            else if (check(e,ISO))
                {
                    try { cd =
                              new calendrica.ISO
                                  (Long.parseLong(isoYearField.getText()),
                                   isoWeekField.getSelectedIndex()+1,
                                   isoDayField.getSelectedIndex()+1);
                    temp = cd.toFixed();
                    if (!cd.equals(new calendrica.ISO(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(ISO,temp);};
                }
            else if (check(e,BAHAI))
                {
                    try { cd =
                              new Bahai
                                  (Long.parseLong(bahaiMajorField.getText()),
                                   bahaiCycleField.getSelectedIndex()+1,
                                   bahaiYearField.getSelectedIndex()+1,
                                   bahaiMonthField.getSelectedIndex(),
                                   bahaiDayField.getSelectedIndex()+1);
                    temp = cd.toFixed();
                    if (!cd.equals(new Bahai(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(BAHAI,temp);};
                }
            else if (check(e,ISLAMIC))
                {
                    try { cd =
                              new Islamic
                                  (Long.parseLong(islamicYearField.getText()),
                                   1+islamicMonthField.getSelectedIndex(),
                                   1+islamicDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Islamic(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(ISLAMIC,temp);};
                }
            else if (check(e,MAYAN_LONG_COUNT))
                {
                    try { cd =
                              new MayanLongCount
                                  (Long.parseLong(baktunField.getText()),
                                   katunField.getSelectedIndex(),
                                   tunField.getSelectedIndex(),
                                   uinalField.getSelectedIndex(),
                                   kinField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new MayanLongCount(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(MAYAN_LONG_COUNT,temp);};
                }
            else if (check(e,OLD_HINDU_SOLAR))
                {
                    try { cd =
                              new OldHinduSolar
                                  (Long.parseLong(oldHinduSolarYearField.getText()),
                                   1+oldHinduSolarMonthField.getSelectedIndex(),
                                   1+oldHinduSolarDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new OldHinduSolar(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(OLD_HINDU_SOLAR,temp);};
                }
            else if (check(e,OLD_HINDU_LUNAR))
                {
                    try { cd =
                              new OldHinduLunar
                                  (Long.parseLong(oldHinduLunarYearField.getText()),
                                   1+oldHinduLunarMonthField.getSelectedIndex(),
                                   (oldHinduLunarLeapField.getSelectedIndex()==0)?
                                   false:true,
                                   1+oldHinduLunarDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new OldHinduLunar(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(OLD_HINDU_LUNAR,temp);};
                }
            else if (check(e,HEBREW))
                {
                    try { cd =
                              new Hebrew
                                  (Long.parseLong(hebrewYearField.getText()),
                                   1+hebrewMonthField.getSelectedIndex(),
                                   1+hebrewDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new Hebrew(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(HEBREW,temp);};
                }
            else if (check(e,ARITH_PERSIAN))
                {
                    try { cd =
                              new ArithmeticPersian
                                  (Long.parseLong(arithPersianYearField.getText()),
                                   1+arithPersianMonthField.getSelectedIndex(),
                                   1+arithPersianDayField.getSelectedIndex());
                    temp = cd.toFixed();
                    if (!cd.equals(new ArithmeticPersian(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(ARITH_PERSIAN,temp);};
                }
            else if (check(e,MODIFIED_FRENCH))
                {
                    try { cd =
                              new ModifiedFrench
                                  (Long.parseLong(modifiedFrenchYearField.getText()),
                                   1+modifiedFrenchMonthField.getSelectedIndex() ,
                                   (modifiedFrenchMonthField.getSelectedIndex()<12)?
                                   1+modifiedFrenchDayField.getSelectedIndex()+
                                   10*modifiedFrenchWeekField.getSelectedIndex():
                                   modifiedFrenchDayField.getSelectedIndex()-9);
                    temp = cd.toFixed();
                    if (!cd.equals(new ModifiedFrench(temp)))
                        throw new BogusDateException();
                    displayDate(temp);}
                    catch (Exception b) {displayBogus(MODIFIED_FRENCH,temp);};
                }
            ;             }
        catch (NumberFormatException ex) {displayError("Illegal Input");};

        for (int c=0;c<resultButtons.length;c++)
            if (checkPanel(e,c))
                selectCalendar(c);

        for (int i=1;i<=31;i++)
            if (checkDay(e,i))
                displayDate(i,gd.month,gd.year);

        return true;
    }

}
