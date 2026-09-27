import calendrica.*;
import java.util.*;
import java.awt.*;

public class CalendricaTest {
	static Frame f;
	static TextArea t;
	
	public static void main(String args[]) 
		throws InstantiationException, IllegalAccessException
	{
		f = new Frame();
		t = new TextArea(4, 40);
		f.add(t);
		f.setSize(800, 400);
		f.show();
		
		appendln("Current date and time (universal): " +
			formatFixed(ProtoDate.currentDate()) + ",  " +
			new Time(ProtoDate.currentMoment()).format() );
		appendln();
	
		testConversions();
		appendln();
		
		testFormats();
		appendln();
		
		testTime();
		appendln();
		
		sampleHolidays();
		
		appendln("\nDone!");
	}

	
	static void testConversions()
		throws InstantiationException, IllegalAccessException
	{
		Vector errors = new Vector();
		
		appendln("Arithmetic Calendars:\n");
		
		for(int c = 0; c < testClassesArithmetic.length; ++c) {
			Class cl = testClassesArithmetic[c];
			int[][] td = testDatesArithmetic[c];
			
			testToFixed(cl, td, errors);
			appendln(formatErrors(errors));

			testFromFixed(cl, td, errors);
			appendln(formatErrors(errors));
		}

		appendln();
		
		for(int c = 0; c < testProtoDateClasses.length; ++c) {
			Class cl = testProtoDateClasses[c];
			int[][] td = testProtoDates[c];

			testFromFixed(cl, td, errors);
			appendln(formatErrors(errors));
		}

		appendln("\nAstronomical Calendars:\n");
		
		for(int c = 0; c < testClassesAstronomical.length; ++c) {
			Class cl = testClassesAstronomical[c];
			int[][] td = testDatesAstronomical[c];
			
			testToFixed(cl, td, errors);
			appendln(formatErrors(errors));

			testFromFixed(cl, td, errors);
			appendln(formatErrors(errors));
		}
	}
	
	static void append(String s) {
		t.append(s);
	}
	
	static void appendln(String s) {
		t.append(s + "\n");
	}

	static void appendln() {
		t.append("\n");
	}
	
	static void sampleHolidays() {

		appendln("Some holidays:\n");

		appendln("Election Day: " + formatFixed(Gregorian.electionDay(2000)) );
		appendln("Daylight Saving Start: " + formatFixed(Gregorian.daylightSavingStart(2000)) );
		appendln("Daylight Saving End: " + formatFixed(Gregorian.daylightSavingEnd(2000)) );
		appendln("Advent: " + formatFixed(Gregorian.advent(2000)) );
		appendln("Orthodox Easter: " + formatFixed(Ecclesiastical.orthodoxEaster(2000)) );
		appendln("Easter: " + formatFixed(Ecclesiastical.easter(2000)) );
		appendln("Pentecost: " + formatFixed(Ecclesiastical.pentecost(2000)) );
		appendln("Eastern Orthodox Christmas: " + formatFixedVector(Julian.easternOrthodoxChristmas(2000)) );
		appendln("Coptic Christmas: " + formatFixedVector(Coptic.christmas(2000)) );
		appendln("Mawlid-an-Nabi: " + formatFixedVector(Islamic.mawlidAnNabi(2000)) );
		appendln("Feast of Ridvan: " + formatFixed(FutureBahai.feastOfRidvan(2000)) );
		appendln("Naw-Ruz: " + formatFixed(Persian.nawRuz(2000)) );
		appendln("Yom Kippur: " + formatFixed(Hebrew.yomKippur(2000)) );
		appendln("Passover: " + formatFixed(Hebrew.passover(2000)) );
		appendln("Purim: " + formatFixed(Hebrew.purim(2000)) );
		appendln("Ta'anit Esther: " + formatFixed(Hebrew.taAnitEsther(2000)) );
		appendln("Tishah be-Av: " + formatFixed(Hebrew.tishahBeAv(2000)) );
		appendln("Sh'ela: " + formatFixedVector(Hebrew.shEla(2000)) );
		appendln("Yom ha-Zikkaron: " + formatFixed(Hebrew.yomHaZikkaron(2000)) );
		appendln("Birkath HaHama: " + formatFixedVector(Hebrew.birkathHaHama(2009)) );
		appendln("Dragon Festival: " + formatFixed(Chinese.dragonFestival(2000)) );
		appendln("Hindu Lunar New Year: " + formatFixed(HinduLunar.newYear(2000)) );
		appendln("Sacred Wednesdays: " + formatFixedVector(HinduLunar.sacredWednesdaysInGregorian(2000)) );
		appendln("Kajeng Keliwon: " + formatFixedVector(Balinese.kajengKeliwonInGregorian(2000)) );
	}
	
	static String formatFixed(long date) {
		return new Gregorian(date).format();
	}
	
	static String formatFixedVector(FixedVector v) {
		StringBuffer s = new StringBuffer();
		s.append("{");
		for(int i = 0; i < v.size(); ++i) {
			s.append(formatFixed(v.fixedAt(i)));
			if(i < (v.size() - 1))
				s.append(", ");
		}
		s.append("}");
		
		return s.toString();
	}
	
	static void testFromFixed(Class testClass, int[][] testDates, Vector errors)
		throws InstantiationException, IllegalAccessException
	{
		calendrica.ProtoDate toDate = (calendrica.ProtoDate)testClass.newInstance();
		calendrica.ProtoDate compareDate = (calendrica.ProtoDate)testClass.newInstance();
		
		append("fixed -> " + testClass.getName() + "...");
		errors.removeAllElements();
		for(int i = 0; i < testDatesFixed.length; ++i) {
			long fromDate = testDatesFixed[i];
			toDate.fromFixed(fromDate);
			compareDate.fromArray(testDates[i]);
			//append(".");
			if(!toDate.equals(compareDate)) {
				errors.addElement("{" + fromDate + ", " + compareDate + ", " + toDate + "}");
			}
		}
	}
	
	static void testToFixed(Class testClass, int[][] testDates, Vector errors)
		throws InstantiationException, IllegalAccessException
	{
		calendrica.Date fromDate = (calendrica.Date)testClass.newInstance();

		append(testClass.getName() + " -> fixed...");
		errors.removeAllElements();
		for(int i = 0; i < testDatesFixed.length; ++i) {
			fromDate.fromArray(testDates[i]);
			long compareDate = testDatesFixed[i];
			long toDate = fromDate.toFixed();
			if(toDate != compareDate) {
				errors.addElement("{" + fromDate + ", " + compareDate + ", " + toDate + "}");
			}
			//append(".");
		}
	}

	static String formatErrors(Vector errors) {
		StringBuffer s = new StringBuffer();
		
		if(errors.size() == 0) {
			s.append("OK");
		} else {
			s.append("{");
			for(Enumeration e = errors.elements(); e.hasMoreElements();) {
				s.append((String)e.nextElement());
				if(e.hasMoreElements()) {
					s.append(", ");
				}
			}
			s.append("}");
		}
		
		return s.toString();
	}
	
	static void testFormats()
		throws InstantiationException, IllegalAccessException
	{
		appendln("Date formats:\n");

		final long testDate = 717471;
		
		for(int c = 0; c < testClassesArithmetic.length; ++c) {
			testFormat(testClassesArithmetic[c], testDate);
		}
		
		for(int c = 0; c < testProtoDateClasses.length; ++c) {
			testFormat(testProtoDateClasses[c], testDate);
		}
		
		for(int c = 0; c < testClassesAstronomical.length; ++c) {
			testFormat(testClassesAstronomical[c], testDate);
		}
	}
	
	static void testFormat(Class testClass, long testDate)
		throws InstantiationException, IllegalAccessException
	{
		append(testClass.getName() + ": ");
		calendrica.ProtoDate date = (calendrica.ProtoDate)testClass.newInstance();
		date.fromFixed(testDate);
		appendln(date.format());
	}
	
	static void testTime() {
		appendln("Time formats:\n");

		Time t = new Time(0,0,45);
		appendln(t.format());
		t = new Time(12,5,30.5);
		appendln(t.format());
		t = new Time(24,30,15);
		appendln(t.format());
		t = new Time(25,45,0);
		appendln(t.format());
	}

	static final int TRUE = 1;
	static final int FALSE = 0;

	static int[] testDatesFixed = {
		-214193, -61387, 25469, 49217, 171307, 210155, 253427, 369740, 400085, 434355, 452605,
		470160, 473837, 507850, 524156, 544676, 567118, 569477, 601716, 613424, 626596, 645554,
		664224, 671401, 694799, 704424, 708842, 709409, 709580, 727274, 728714, 744313, 764652};

	static int[][] testDatesEgyptian = {{161, 7, 15}, {580, 3, 6}, {818, 2, 22}, {883, 3, 15}, 
		{1217, 9, 15}, {1324, 2, 18}, {1442, 9, 10}, {1761, 5, 8}, 
		{1844, 6, 28}, {1938, 5, 18}, {1988, 5, 18}, {2036, 6, 23}, 
		{2046, 7, 20}, {2139, 9, 28}, {2184, 5, 29}, {2240, 8, 19}, 
		{2302, 2, 11}, {2308, 7, 30}, {2396, 11, 29}, {2428, 12, 27}, 
		{2465, 1, 24}, {2517, 1, 2}, {2568, 2, 27}, {2587, 10, 29}, 
		{2651, 12, 7}, {2678, 4, 17}, {2690, 5, 25}, {2691, 12, 17}, 
		{2692, 6, 3}, {2740, 11, 27}, {2744, 11, 7}, {2787, 8, 1}, 
		{2843, 4, 20}};

	static int[][] testDatesArmenian = {{-1138, 4, 10}, {-720, 12, 6}, {-482, 11, 22}, {-417, 12, 15}, {-82, 6, 10}, {24, 11, 18}, 
		{143, 6, 5}, {462, 2, 3}, {545, 3, 23}, {639, 2, 13}, {689, 2, 13}, {737, 3, 18}, {747, 4, 15}, 
		{840, 6, 23}, {885, 2, 24}, {941, 5, 14}, {1002, 11, 11}, {1009, 4, 25}, {1097, 8, 24}, 
		{1129, 9, 22}, {1165, 10, 24}, {1217, 10, 2}, {1268, 11, 27}, {1288, 7, 24}, {1352, 9, 2}, 
		{1379, 1, 12}, {1391, 2, 20}, {1392, 9, 12}, {1393, 2, 28}, {1441, 8, 22}, {1445, 8, 2}, 
		{1488, 4, 26}, {1544, 1, 15}};

	static int[][] testDatesGregorian = {
		{-586, 7, 24}, {-168, 12, 5}, {70, 9, 24}, {135, 10, 2}, {470, 1, 8}, {576, 5, 20}, {694, 11, 10}, {1013, 4, 25}, {1096, 5, 24}, {1190, 3, 23}, {1240, 3, 10},
		{1288, 4, 2}, {1298, 4, 27}, {1391, 6, 12}, {1436, 2, 3}, {1492, 4, 9}, {1553, 9, 19}, {1560, 3, 5}, {1648, 6, 10}, {1680, 6, 30}, {1716, 7, 24}, {1768, 6, 19},
		{1819, 8, 2}, {1839, 3, 27}, {1903, 4, 19}, {1929, 8, 25}, {1941, 9, 29}, {1943, 4, 19}, {1943, 10, 7}, {1992, 3, 17}, {1996, 2, 25}, {2038, 11, 10}, {2094, 7, 18}};

	static int[][] testDatesJulian = {
		{-587, 7, 30}, {-169, 12, 8}, {70, 9, 26}, {135, 10, 3}, {470, 1, 7}, {576, 5, 18}, {694, 11, 7}, {1013, 4, 19}, {1096, 5, 18}, {1190, 3, 16}, {1240, 3, 3}, 
		{1288, 3, 26}, {1298, 4, 20}, {1391, 6, 4}, {1436, 1, 25}, {1492, 3, 31}, {1553, 9, 9}, {1560, 2, 24}, {1648, 5, 31}, {1680, 6, 20}, {1716, 7, 13}, {1768, 6, 8}, 
		{1819, 7, 21}, {1839, 3, 15}, {1903, 4, 6}, {1929, 8, 12}, {1941, 9, 16}, {1943, 4, 6}, {1943, 9, 24}, {1992, 3, 4}, {1996, 2, 12}, {2038, 10, 28}, {2094, 7, 5}};

	static int[][] testDatesRoman = {
		{-587, 8, 1, 3, 0}, {-169, 12, 3, 6, 0}, {70, 10, 1, 6, 0}, {135, 10, 2, 5, 0}, {470, 1, 3, 7, 0}, {576, 6, 1, 15, 0}, 
		{694, 11, 3, 7, 0}, {1013, 5, 1, 13, 0}, {1096, 6, 1, 15, 0}, {1190, 4, 1, 17, 0}, {1240, 3, 2, 5, 0}, {1288, 4, 1, 7, 0}, 
		{1298, 5, 1, 12, 0}, {1391, 6, 2, 2, 0}, {1436, 2, 1, 8, 0}, {1492, 4, 1, 2, 0}, {1553, 9, 3, 5, 0}, {1560, 3, 1, 6, 0}, 
		{1648, 6, 1, 2, 0}, {1680, 7, 1, 12, 0}, {1716, 7, 3, 3, 0}, {1768, 6, 3, 6, 0}, {1819, 8, 1, 12, 0}, {1839, 3, 3, 1, 0}, 
		{1903, 4, 3, 8, 0}, {1929, 8, 3, 2, 0}, {1941, 10, 1, 16, 0}, {1943, 4, 3, 8, 0}, {1943, 10, 1, 8, 0}, {1992, 3, 2, 4, 0}, 
		{1996, 2, 3, 2, 0}, {2038, 11, 1, 5, 0}, {2094, 7, 2, 3, 0}};

	static int[][] testDatesCoptic = {
		{-870, 12, 6}, {-451, 4, 12}, {-213, 1, 29}, {-148, 2, 5}, {186, 5, 12}, {292, 9, 23}, {411, 3, 11}, {729, 8, 24}, {812, 9, 23}, {906, 7, 20}, {956, 7, 7}, 
		{1004, 7, 30}, {1014, 8, 25}, {1107, 10, 10}, {1152, 5, 29}, {1208, 8, 5}, {1270, 1, 12}, {1276, 6, 29}, {1364, 10, 6}, {1396, 10, 26}, {1432, 11, 19}, {1484, 10, 14}, 
		{1535, 11, 27}, {1555, 7, 19}, {1619, 8, 11}, {1645, 12, 19}, {1658, 1, 19}, {1659, 8, 11}, {1660, 1, 26}, {1708, 7, 8}, {1712, 6, 17}, {1755, 3, 1}, {1810, 11, 11}};

	static int[][] testDatesEthiopic = {
		{-594, 12, 6}, {-175, 4, 12}, {63, 1, 29}, {128, 2, 5}, {462, 5, 12}, {568, 9, 23}, {687, 3, 11}, {1005, 8, 24}, {1088, 9, 23}, {1182, 7, 20}, {1232, 7, 7}, 
		{1280, 7, 30}, {1290, 8, 25}, {1383, 10, 10}, {1428, 5, 29}, {1484, 8, 5}, {1546, 1, 12}, {1552, 6, 29}, {1640, 10, 6}, {1672, 10, 26}, {1708, 11, 19}, {1760, 10, 14}, 
		{1811, 11, 27}, {1831, 7, 19}, {1895, 8, 11}, {1921, 12, 19}, {1934, 1, 19}, {1935, 8, 11}, {1936, 1, 26}, {1984, 7, 8}, {1988, 6, 17}, {2031, 3, 1}, {2086, 11, 11}};

	static int[][] testDatesISO = {
		{-586, 29, 7}, {-168, 49, 3}, {70, 39, 3}, {135, 39, 7}, {470, 2, 3}, {576, 21, 1}, {694, 45, 6}, {1013, 16, 7}, {1096, 21, 7}, {1190, 12, 5}, {1240, 10, 6},
		{1288, 14, 5}, {1298, 17, 7}, {1391, 23, 7}, {1436, 5, 3}, {1492, 14, 6}, {1553, 38, 6}, {1560, 9, 6}, {1648, 24, 3}, {1680, 26, 7}, {1716, 30, 5}, {1768, 24, 7},
		{1819, 31, 1}, {1839, 13, 3}, {1903, 16, 7}, {1929, 34, 7}, {1941, 40, 1}, {1943, 16, 1}, {1943, 40, 4}, {1992, 12, 2}, {1996, 8, 7}, {2038, 45, 3}, {2094, 28, 7}};

	static int[][] testDatesIslamic = {
		{-1245, 12, 9}, {-813, 2, 23}, {-568, 4, 1}, {-501, 4, 6}, {-157, 10, 17}, {-47, 6, 3}, {75, 7, 13}, {403, 10, 5}, {489, 5, 22}, {586, 2, 7}, {637, 8, 7}, 
		{687, 2, 20}, {697, 7, 7}, {793, 7, 1}, {839, 7, 6}, {897, 6, 1}, {960, 9, 30}, {967, 5, 27}, {1058, 5, 18}, {1091, 6, 2}, {1128, 8, 4}, {1182, 2, 3}, 
		{1234, 10, 10}, {1255, 1, 11}, {1321, 1, 21}, {1348, 3, 19}, {1360, 9, 8}, {1362, 4, 13}, {1362, 10, 7}, {1412, 9, 13}, {1416, 10, 5}, {1460, 10, 12}, {1518, 3, 5}};

	static int[][] testDatesHebrew = {
		{3174, 5, 10}, {3593, 9, 25}, {3831, 7, 3}, {3896, 7, 9}, {4230, 10, 18}, {4336, 3, 4}, {4455, 8, 13}, {4773, 2, 6}, {4856, 2, 23}, {4950, 1, 7}, {5000, 13, 8}, 
		{5048, 1, 21}, {5058, 2, 7}, {5151, 4, 1}, {5196, 11, 7}, {5252, 1, 3}, {5314, 7, 1}, {5320, 12, 27}, {5408, 3, 20}, {5440, 4, 3}, {5476, 5, 5}, {5528, 4, 4}, 
		{5579, 5, 11}, {5599, 1, 12}, {5663, 1, 22}, {5689, 5, 19}, {5702, 7, 8}, {5703, 1, 14}, {5704, 7, 8}, {5752, 13, 12}, {5756, 12, 5}, {5799, 8, 12}, {5854, 5, 5}};

	static int[][] testDatesOldHinduSolar = {
		{2515, 5, 19}, {2933, 9, 26}, {3171, 7, 11}, {3236, 7, 17}, {3570, 10, 19}, {3677, 2, 28}, {3795, 8, 17}, {4114, 1, 26}, {4197, 2, 24}, {4290, 12, 20}, {4340, 12, 7}, 
		{4388, 12, 30}, {4399, 1, 24}, {4492, 3, 7}, {4536, 10, 28}, {4593, 1, 3}, {4654, 6, 12}, {4660, 11, 27}, {4749, 3, 1}, {4781, 3, 21}, {4817, 4, 13}, {4869, 3, 8}, 
		{4920, 4, 20}, {4939, 12, 13}, {5004, 1, 4}, {5030, 5, 11}, {5042, 6, 15}, {5044, 1, 4}, {5044, 6, 23}, {5092, 12, 2}, {5096, 11, 11}, {5139, 7, 26}, {5195, 4, 2}};

	static int[][] testDatesOldHinduLunar = {
		{2515, 6, FALSE, 11}, {2933, 9, FALSE, 26}, {3171, 8, FALSE, 3}, {3236, 8, FALSE, 9}, {3570, 11, TRUE, 19}, 
		{3677, 3, FALSE, 5}, {3795, 9, FALSE, 15}, {4114, 2, FALSE, 7}, {4197, 2, FALSE, 24}, {4291, 1, FALSE, 9}, 
		{4340, 12, FALSE, 9}, {4389, 1, FALSE, 23}, {4399, 2, FALSE, 8}, {4492, 4, FALSE, 2}, {4536, 11, FALSE, 7}, 
		{4593, 1, FALSE, 3}, {4654, 7, FALSE, 2}, {4660, 11, FALSE, 29}, {4749, 3, FALSE, 20}, {4781, 4, FALSE, 4}, 
		{4817, 5, FALSE, 6}, {4869, 4, FALSE, 5}, {4920, 5, FALSE, 12}, {4940, 1, TRUE, 13}, {5004, 1, FALSE, 23}, 
		{5030, 5, FALSE, 21}, {5042, 7, FALSE, 9}, {5044, 1, FALSE, 15}, {5044, 7, FALSE, 9}, {5092, 12, FALSE, 14}, 
		{5096, 12, FALSE, 7}, {5139, 8, FALSE, 14}, {5195, 4, FALSE, 6}};

	static int[][] testDatesMayanLongCount = {
		{6, 8, 3, 13, 9}, {7, 9, 8, 3, 15}, {8, 1, 9, 8, 11}, {8, 4, 15, 7, 19}, {9, 1, 14, 10, 9}, 
		{9, 7, 2, 8, 17}, {9, 13, 2, 12, 9}, {10, 9, 5, 14, 2}, {10, 13, 10, 1, 7}, {10, 18, 5, 4, 17}, 
		{11, 0, 15, 17, 7}, {11, 3, 4, 13, 2}, {11, 3, 14, 16, 19}, {11, 8, 9, 7, 12}, {11, 10, 14, 12, 18}, 
		{11, 13, 11, 12, 18}, {11, 16, 14, 1, 0}, {11, 17, 0, 10, 19}, {12, 1, 10, 2, 18}, {12, 3, 2, 12, 6}, 
		{12, 4, 19, 4, 18}, {12, 7, 11, 16, 16}, {12, 10, 3, 14, 6}, {12, 11, 3, 13, 3}, {12, 14, 8, 13, 1},
		{12, 15, 15, 8, 6}, {12, 16, 7, 13, 4}, {12, 16, 9, 5, 11}, {12, 16, 9, 14, 2}, {12, 18, 18, 16, 16}, 
		{12, 19, 2, 16, 16}, {13, 1, 6, 4, 15}, {13, 4, 2, 13, 14}};

	static int[][] testDatesBahai = {
		{-6, 6, 3, 7, 12}, {-5, 9, 3, 14, 13}, {-4, 2, 13, 10, 17}, {-4, 6, 2, 11, 6}, {-3, 4, 13, 16, 9},
		{-3, 10, 6, 4, 4}, {-3, 16, 10, 13, 7}, {-2, 14, 6, 2, 17}, {-2, 18, 13, 4, 8}, {-1, 4, 12, 1, 3},
		{-1, 7, 4, 19, 9}, {-1, 9, 15, 1, 13}, {-1, 10, 6, 2, 19}, {-1, 15, 4, 5, 8}, {-1, 17, 10, 17, 16},
		{0, 1, 10, 2, 1}, {0, 4, 14, 10, 12}, {0, 5, 1, 19, 4}, {0, 9, 14, 5, 6}, {0, 11, 8, 6, 7},
		{0, 13, 6, 7, 12}, {0, 16, 1, 5, 15}, {0, 18, 14, 8, 2}, {0, 19, 15, 1, 7}, {1, 4, 3, 2, 11},
		{1, 5, 10, 9, 6}, {1, 6, 3, 11, 3}, {1, 6, 5, 2, 11}, {1, 6, 5, 11, 11}, {1, 8, 15, 19, 16},
		{1, 8, 19, 18, 19}, {1, 11, 5, 13, 7}, {1, 14, 4, 7, 6}};

	static int[][] testDatesArithmeticPersian = {
		{-1208, 5, 1}, {-790, 9, 14}, {-552, 7, 2}, {-487, 7, 9}, {-153, 10, 18}, {-46, 2, 30}, {73, 8, 19}, {392, 2, 5}, {475, 3, 3}, {569, 1, 3}, {618, 12, 20}, 
		{667, 1, 14}, {677, 2, 8}, {770, 3, 22}, {814, 11, 13}, {871, 1, 21}, {932, 6, 28}, {938, 12, 14}, {1027, 3, 21}, {1059, 4, 10}, {1095, 5, 2}, {1147, 3, 30}, 
		{1198, 5, 10}, {1218, 1, 7}, {1282, 1, 29}, {1308, 6, 3}, {1320, 7, 7}, {1322, 1, 29}, {1322, 7, 14}, {1370, 12, 27}, {1374, 12, 6}, {1417, 8, 19}, {1473, 4, 28}};

	static int[][] testDatesModifiedFrench = {
		{-2378, 11, 4}, {-1959, 3, 13}, {-1721, 1, 2}, {-1656, 1, 10}, {-1322, 4, 18}, {-1216, 9, 1}, {-1097, 2, 19}, {-779, 8, 4}, {-696, 9, 5}, {-602, 7, 1}, {-552, 6, 20}, 
		{-504, 7, 13}, {-494, 8, 8}, {-401, 9, 23}, {-356, 5, 13}, {-300, 7, 19}, {-239, 13, 1}, {-232, 6, 14}, {-144, 9, 22}, {-112, 10, 12}, {-76, 11, 6}, {-24, 10, 1}, 
		{27, 11, 14}, {47, 7, 6}, {111, 7, 29}, {137, 12, 7}, {150, 1, 7}, {151, 7, 29}, {152, 1, 15}, {200, 6, 27}, {204, 6, 7}, {247, 2, 20}, {302, 11, 1}};

	static int[][] testDatesMayanHaab = {
		{11, 12}, {5, 3}, {4, 9}, {5, 12}, {14, 12}, {4, 5}, {14, 7}, {8, 5}, {10, 15}, {8, 15}, {8, 15}, 
		{10, 10}, {11, 17}, {15, 5}, {9, 6}, {13, 6}, {3, 18}, {12, 7}, {18, 6}, {1, 9}, {3, 1}, {1, 19}, 
		{4, 14}, {16, 16}, {18, 14}, {7, 4}, {9, 2}, {19, 4}, {9, 10}, {18, 4}, {17, 4}, {12, 8}, {7, 7}};

	static int[][] testDatesMayanTzolkin = {
		{5, 9}, {9, 15}, {12, 11}, {9, 19}, {3, 9}, {7, 17}, {2, 9}, {4, 2}, {7, 7}, {9, 17}, {7, 7},
		{12, 2}, {10, 19}, {2, 12}, {6, 18}, {12, 18}, {3, 20}, {9, 19}, {8, 18}, {3, 6}, {6, 18}, {10, 16},
		{12, 6}, {13, 3}, {11, 1}, {3, 6}, {1, 4}, {9, 11}, {11, 2}, {12, 16}, {9, 16}, {8, 15}, {2, 14}};

	static int[][] testDatesBalinese = {
		{0, 1, 1, 1, 3, 1, 1, 5, 7, 3}, {1, 2, 2, 1, 4, 5, 4, 5, 5, 2}, 
		{1, 2, 2, 1, 5, 5, 4, 1, 5, 6}, {0, 1, 2, 3, 3, 5, 1, 3, 5, 3}, 
		{0, 1, 1, 3, 3, 1, 4, 3, 1, 5}, {1, 2, 2, 1, 1, 5, 2, 1, 8, 0}, 
		{0, 1, 2, 3, 3, 5, 7, 3, 2, 7}, {0, 1, 2, 2, 1, 2, 1, 2, 2, 1}, 
		{0, 1, 2, 1, 1, 5, 1, 1, 8, 1}, {1, 2, 3, 1, 1, 3, 6, 1, 3, 2}, 
		{0, 1, 1, 1, 1, 1, 7, 5, 1, 5}, {1, 2, 3, 4, 1, 6, 6, 8, 6, 2}, 
		{0, 1, 2, 3, 3, 5, 1, 3, 5, 3}, {0, 1, 1, 4, 1, 4, 1, 4, 7, 1}, 
		{0, 1, 2, 2, 2, 2, 4, 2, 5, 7}, {0, 1, 2, 4, 2, 2, 7, 8, 8, 9}, 
		{1, 2, 1, 4, 4, 4, 7, 4, 7, 4}, {0, 1, 2, 3, 3, 5, 7, 3, 2, 7}, 
		{0, 1, 3, 4, 2, 6, 4, 8, 3, 7}, {1, 2, 2, 4, 5, 2, 1, 4, 5, 4}, 
		{1, 2, 1, 2, 2, 4, 6, 2, 1, 6}, {1, 2, 2, 4, 5, 2, 1, 4, 5, 4}, 
		{0, 1, 3, 4, 5, 6, 2, 8, 3, 3}, {0, 1, 1, 1, 2, 1, 4, 5, 4, 7}, 
		{1, 2, 2, 1, 5, 5, 1, 5, 8, 4}, {1, 2, 3, 2, 5, 6, 1, 2, 3, 4}, 
		{1, 2, 2, 2, 3, 2, 2, 2, 1, 2}, {0, 1, 2, 3, 5, 5, 2, 3, 2, 3}, 
		{1, 2, 2, 4, 1, 2, 5, 4, 8, 4}, {1, 2, 2, 2, 5, 2, 3, 2, 8, 2}, 
		{1, 2, 2, 4, 5, 2, 1, 4, 5, 4}, {1, 2, 1, 3, 4, 1, 4, 7, 1, 2}, 
		{0, 1, 3, 4, 3, 6, 1, 8, 6, 3}};

	static int[][] testDatesObservationalIslamic = {
		{-1245, 12, 12}, {-813, 2, 26}, {-568, 4, 3}, {-501, 4, 8}, {-157, 10, 19}, {-47, 6, 4}, {75, 7, 14}, {403, 10, 6}, {489, 5, 23}, {586, 2, 8}, {637, 8, 8}, 
		{687, 2, 22}, {697, 7, 8}, {793, 7, 1}, {839, 7, 7}, {897, 6, 3}, {960, 10, 1}, {967, 5, 28}, {1058, 5, 19}, {1091, 6, 4}, {1128, 8, 5}, {1182, 2, 5}, 
		{1234, 10, 11}, {1255, 1, 12}, {1321, 1, 21}, {1348, 3, 20}, {1360, 9, 8}, {1362, 4, 15}, {1362, 10, 8}, {1412, 9, 13}, {1416, 10, 6}, {1460, 10, 13}, {1518, 3, 6}};

	static int[][] testDatesPersian = {
		{-1208, 5, 1}, {-790, 9, 14}, {-552, 7, 2}, {-487, 7, 9}, {-153, 10, 19}, {-46, 2, 31}, {73, 8, 19}, {392, 2, 5}, {475, 3, 4}, {569, 1, 3}, {618, 12, 20}, 
		{667, 1, 14}, {677, 2, 8}, {770, 3, 22}, {814, 11, 13}, {871, 1, 21}, {932, 6, 28}, {938, 12, 14}, {1027, 3, 21}, {1059, 4, 10}, {1095, 5, 2}, {1147, 3, 30}, 
		{1198, 5, 10}, {1218, 1, 7}, {1282, 1, 29}, {1308, 6, 3}, {1320, 7, 7}, {1322, 1, 29}, {1322, 7, 14}, {1370, 12, 27}, {1374, 12, 6}, {1417, 8, 19}, {1473, 4, 28}};

	static int[][] testDatesFutureBahai = {
		{-6, 6, 3, 7, 10}, {-5, 9, 3, 14, 12}, {-4, 2, 13, 10, 17}, {-4, 6, 2, 11, 5},
		{-3, 4, 13, 16, 9}, {-3, 10, 6, 4, 4}, {-3, 16, 10, 13, 6}, {-2, 14, 6, 2, 16}, {-2, 18, 13, 4, 8},
		{-1, 4, 12, 1, 2}, {-1, 7, 4, 19, 9}, {-1, 9, 15, 1, 13}, {-1, 10, 6, 2, 19}, {-1, 15, 4, 5, 7},
		{-1, 17, 10, 17, 15}, {0, 1, 10, 2, 1}, {0, 4, 14, 10, 11}, {0, 5, 1, 19, 3}, {0, 9, 14, 5, 6},
		{0, 11, 8, 6, 7}, {0, 13, 6, 7, 12}, {0, 16, 1, 5, 15}, {0, 18, 14, 8, 1}, {0, 19, 15, 1, 6},
		{1, 4, 3, 2, 9}, {1, 5, 10, 9, 5}, {1, 6, 3, 11, 2}, {1, 6, 5, 2, 10}, {1, 6, 5, 11, 10},
		{1, 8, 15, 19, 16}, {1, 8, 19, 18, 18}, {1, 11, 5, 13, 7}, {1, 14, 4, 7, 6}};

	static int[][] testDatesFrench = {
		{-2378, 11, 5}, {-1959, 3, 14}, {-1721, 1, 2}, {-1656, 1, 10}, {-1322, 4, 19}, {-1216, 9, 1}, {-1097, 2, 19}, {-779, 8, 5}, {-696, 9, 5}, {-602, 7, 2}, {-552, 6, 20}, 
		{-504, 7, 13}, {-494, 8, 8}, {-401, 9, 23}, {-356, 5, 14}, {-300, 7, 20}, {-239, 13, 2}, {-232, 6, 15}, {-144, 9, 22}, {-112, 10, 12}, {-76, 11, 6}, {-24, 10, 1}, 
		{27, 11, 14}, {47, 7, 6}, {111, 7, 28}, {137, 12, 7}, {150, 1, 7}, {151, 7, 29}, {152, 1, 15}, {200, 6, 27}, {204, 6, 6}, {247, 2, 20}, {302, 10, 30}};

	static int[][] testDatesChinese = {
		{35, 11, 6, FALSE, 12}, {42, 9, 10, FALSE, 27}, {46, 7, 8, FALSE, 4}, {47, 12, 8, FALSE, 9}, {52, 46, 11, FALSE, 20},
		{54, 33, 4, FALSE, 5}, {56, 31, 10, FALSE, 15}, {61, 50, 3, FALSE, 7}, {63, 13, 4, FALSE, 24}, {64, 47, 2, FALSE, 9},
		{65, 37, 2, FALSE, 9}, {66, 25, 2, FALSE, 23}, {66, 35, 3, FALSE, 9}, {68, 8, 5, FALSE, 2}, {68, 53, 1, FALSE, 8},
		{69, 49, 3, FALSE, 4}, {70, 50, 8, FALSE, 2}, {70, 57, 1, FALSE, 29}, {72, 25, 4, TRUE, 20}, {72, 57, 6, FALSE, 5},
		{73, 33, 6, FALSE, 6}, {74, 25, 5, FALSE, 5}, {75, 16, 6, FALSE, 12}, {75, 36, 2, FALSE, 13}, {76, 40, 3, FALSE, 22},
		{77, 6, 7, FALSE, 21}, {77, 18, 8, FALSE, 9}, {77, 20, 3, FALSE, 15}, {77, 20, 9, FALSE, 9}, {78, 9, 2, FALSE, 14},
		{78, 13, 1, FALSE, 7}, {78, 55, 10, FALSE, 14}, {79, 51, 6, FALSE, 7}};

	static int[][] testDatesHinduSolar = {
		{-664, 5, 19}, {-246, 9, 26}, {-8, 7, 9}, {57, 7, 16}, {391, 10, 21}, {498, 2, 31}, {616, 8, 16},
		{935, 1, 28}, {1018, 2, 26}, {1111, 12, 23}, {1161, 12, 10}, {1210, 1, 2}, {1220, 1, 27}, {1313, 3, 8},
		{1357, 10, 30}, {1414, 1, 5}, {1475, 6, 10}, {1481, 11, 29}, {1570, 3, 3}, {1602, 3, 22}, {1638, 4, 13},
		{1690, 3, 10}, {1741, 4, 20}, {1760, 12, 16}, {1825, 1, 7}, {1851, 5, 10}, {1863, 6, 14}, {1865, 1, 7},
		{1865, 6, 21}, {1913, 12, 4}, {1917, 11, 13}, {1960, 7, 24}, {2016, 4, 2}};

	static int[][] testDatesHinduLunar = {
		{-529, 6, FALSE, 11, FALSE}, {-111, 9, FALSE, 27, FALSE}, {127, 8, FALSE, 3, FALSE}, {192, 8, FALSE, 9, FALSE}, {526, 11, FALSE, 19, FALSE}, 
		{633, 3, FALSE, 5, FALSE}, {751, 9, FALSE, 15, FALSE}, {1070, 2, FALSE, 6, FALSE}, {1153, 3, TRUE, 23, FALSE}, {1247, 1, FALSE, 8, FALSE}, 
		{1297, 1, FALSE, 8, FALSE}, {1345, 1, FALSE, 22, FALSE}, {1355, 2, FALSE, 8, FALSE}, {1448, 4, FALSE, 1, FALSE}, {1492, 11, FALSE, 7, FALSE}, 
		{1549, 2, TRUE, 3, FALSE}, {1610, 7, FALSE, 2, FALSE}, {1616, 11, FALSE, 28, TRUE}, {1705, 3, FALSE, 20, FALSE}, {1737, 4, FALSE, 4, FALSE}, 
		{1773, 5, FALSE, 6, FALSE}, {1825, 4, FALSE, 5, FALSE}, {1876, 5, FALSE, 11, FALSE}, {1896, 1, FALSE, 13, FALSE}, {1960, 1, FALSE, 22, FALSE}, 
		{1986, 5, FALSE, 20, FALSE}, {1998, 7, FALSE, 9, FALSE}, {2000, 1, FALSE, 14, FALSE}, {2000, 7, FALSE, 8, FALSE}, {2048, 12, FALSE, 14, FALSE}, 
		{2052, 12, FALSE, 7, FALSE}, {2095, 8, FALSE, 14, FALSE}, {2151, 4, FALSE, 6, FALSE}};

	static Class[] testClassesArithmetic = {
		Egyptian.class,
		Armenian.class,
		Gregorian.class,
		Julian.class,
		Roman.class,
		Coptic.class,
		Ethiopic.class,
		ISO.class,
		Islamic.class,
		Hebrew.class,
		OldHinduSolar.class,
		OldHinduLunar.class,
		MayanLongCount.class,
		Bahai.class,
		ArithmeticPersian.class,
		ModifiedFrench.class
	};
		
	static Class[] testProtoDateClasses = {
		MayanHaab.class,
		MayanTzolkin.class,
		Balinese.class
	};

	static Class[] testClassesAstronomical = {
		ObservationalIslamic.class,
		Persian.class,
		FutureBahai.class,
		French.class,
		Chinese.class,
		HinduSolar.class,
		HinduLunar.class
	};

	static int[][][] testDatesArithmetic = {
		testDatesEgyptian,
		testDatesArmenian,
		testDatesGregorian,
		testDatesJulian,
		testDatesRoman,
		testDatesCoptic,
		testDatesEthiopic,
		testDatesISO,
		testDatesIslamic,
		testDatesHebrew,
		testDatesOldHinduSolar,
		testDatesOldHinduLunar,
		testDatesMayanLongCount,
		testDatesBahai,
		testDatesArithmeticPersian,
		testDatesModifiedFrench
	};
		
	static int[][][] testProtoDates = {
		testDatesMayanHaab,
		testDatesMayanTzolkin,
		testDatesBalinese
	};

	static int[][][] testDatesAstronomical = {
		testDatesObservationalIslamic,
		testDatesPersian,
		testDatesFutureBahai,
		testDatesFrench,
		testDatesChinese,
		testDatesHinduSolar,
		testDatesHinduLunar
	};
}
