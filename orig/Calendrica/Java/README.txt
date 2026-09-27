.d8888b. 888 888 d8b 
d88P Y88b 888 888 Y8P 
888 888 888 888 
888 8888b. 888 .d88b. 88888b. .d88888 888d888 888 .d8888b 8888b. 
888 "88b 888 d8P Y8b 888 "88b d88" 888 888P" 888 d88P" "88b 
888 888 .d888888 888 88888888 888 888 888 888 888 888 888 .d888888 
Y88b d88P 888 888 888 Y8b. 888 888 Y88b 888 888 888 Y88b. 888 888 
"Y8888P" "Y888888 888 "Y8888 888 888 "Y88888 888 888 "Y8888P "Y888888 
_____ _ __ __ _ _ _ _ _____ _ _ _ _ 
|_ _| |__ ___ | \/ |_) | | ___ _ __ _ __ (_)_ _ _ __ ___ | ____| __| |_) |_(_) ___ _ __ 
| | | '_ \ / _ \ | |\/| | | | |/ _ \ '_ \| '_ \| | | | | '_ ` _ \ | _| / _` | | __| |/ _ \| '_ \ 
| | | | | | __/ | | | | | | | __/ | | | | | | | |_| | | | | | | | |___| (_| | | |_| | (_) | | | |
|_| |_| |_|\___| |_| |_|_|_|_|\___|_| |_|_| |_|_|\__,_|_| |_| |_| |_____|\__,_|_|\__|_|\___/|_| |_|

Calendrica Java 2.0, 20 March 2000

********************************************************************************

VERSION NOTES

2.0, 20 March 2000

Calendrica 2.0 has beeen completely updated to conform with
_Calendrical Calculations: The Millennium Edition_, Cambridge
Univesity Press, 2001. This includes such additions as support for
Egyptian, Armenian, Roman, Islamic Observational, Arithmetic Persian,
and Balinese calendars, as well as many improvements and bug fixes
over the previous version. Most functions are compatible with
Calendrica 1.0, but compatibility is not guaranteed.

********************************************************************************

GENERAL INFORMATION

Calendrica is a package for generalized manipulation and conversion of
dates through various modern and historical calendars. It is based on
the work of Nachum Dershowitz and Edward M. Reingold in their book
_Calendrical Calculations_, published by Cambridge University
Press. This Java translation was performed by Robert C. McNally
<calendrica@dangerousgames.com>.

********************************************************************************

LICENSE

;;;; The Functions (code, comments, and definitions) contained in this
;;;; file (the "Program") were written by Edward M. Reingold and Nachum
;;;; Dershowitz (the "Authors"), who retain all rights to them except as
;;;; granted in the License and subject to the warranty and liability
;;;; limitations below. These Functions are explained in the Authors'
;;;; book, "Calendrical Calculations: The Millennium Edition" (Cambridge
;;;; University Press, 2001), and are subject to an international
;;;; copyright and a Patent Pending on certain functions.
;;;;
;;;; The Authors' public service intent is more liberal than suggested
;;;; by the License below, as are their licensing policies for otherwise
;;;; nonallowed uses such as--without limitation--those in commercial,
;;;; web-site, and large-scale academic contexts. Please see the
;;;; web-site
;;;;
;;;; http://www.calendarists.com
;;;;
;;;; for all uses not authorized below; in case there is cause for doubt
;;;; about whether a use you contemplate is authorized, please contact
;;;; the Authors (email: reingold@iit.edu). For commercial
;;;; licensing information, contact the first author (E. M. Reingold) 
;;;; at the Department of Computer Science, Illinois Institute of Technology, 
;;;; Chicago, IL 60616-3729, U.S.A.
;;;;
;;;; 1. LICENSE. The Authors grant you a license for personal use only.
;;;; This means that for strictly personal use you may copy and use the
;;;; code, and keep a backup or archival copy also. Any other uses,
;;;; including without limitation, allowing the code or its output to be
;;;; accessed, used, or available to others, is not permitted.
;;;;
;;;; 2. WARRANTY.
;;;;
;;;; (a) THE AUTHORS PROVIDE NO WARRANTIES OF ANY KIND, EITHER
;;;; EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITING THE
;;;; GENERALITY OF THE FOREGOING, ANY IMPLIED WARRANTY OF
;;;; MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
;;;;
;;;; (b) THE AUTHORS SHALL NOT BE LIABLE TO YOU OR ANY THIRD PARTIES
;;;; FOR DAMAGES OF ANY KIND, INCLUDING WITHOUT LIMITATION, ANY LOST
;;;; PROFITS, LOST SAVINGS, OR ANY OTHER INCIDENTAL OR CONSEQUENTIAL
;;;; DAMAGES ARISING OUT OF OR RELATED TO THE USE, INABILITY TO USE,
;;;; OR INACCURACY OF CALCULATIONS, OF THE CODE AND FUNCTIONS
;;;; CONTAINED HEREIN, OR THE BREACH OF ANY EXPRESS OR IMPLIED
;;;; WARRANTY, EVEN IF THE AUTHORS OR PUBLISHER HAVE BEEN ADVISED OF
;;;; THE POSSIBILITY OF THOSE DAMAGES.
;;;;
;;;; (c) THE FOREGOING WARRANTY MAY GIVE YOU SPECIFIC LEGAL
;;;; RIGHTS WHICH MAY VARY FROM STATE TO STATE IN THE U.S.A.
;;;;
;;;; 3. LIMITATION OF LICENSEE REMEDIES. You acknowledge and agree that
;;;; your exclusive remedy (in law or in equity), and Authors' entire
;;;; liability with respect to the material herein, for any breach of
;;;; representation or for any inaccuracy shall be a refund of the license
;;;; fee or service and handling charge which you paid the Authors, if any.
;;;;
;;;; SOME STATES IN THE U.S.A. DO NOT ALLOW THE EXCLUSION OR LIMITATION OF
;;;; LIABILITY FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE
;;;; EXCLUSIONS OR LIMITATION MAY NOT APPLY TO YOU.
;;;;
;;;; 4. DISCLAIMER. Except as expressly set forth above, the Authors:
;;;;
;;;; (a) make no other warranties with respect to the material in the
;;;; Program and expressly disclaim any others;
;;;;
;;;; (b) do not warrant that the material contained in the Program will
;;;; meet your requirements or that their operation shall be
;;;; uninterrupted or error-free;
;;;;
;;;; (c) license this material on an "as is" basis, and the entire risk
;;;; as to the quality, accuracy, and performance of the Program is
;;;; yours, should the code prove defective (except as expressly
;;;; warranted herein). You alone assume the entire cost of all
;;;; necessary corrections.
;;;;
;;;; Sample values for the functions (useful for debugging) are given in
;;;; Appendix C of the book and on the accompanying CD.

********************************************************************************

JAVA IMPLEMENTATION REQUIREMENTS

This implmentation requires Java 1.1 or later.

********************************************************************************

ORGANIZATION

Inheritance Hierarchy

java.lang.Object
Time
Location
ProtoDate
Balinese
Ecclesiastical
MayanHaab
MayanTzolkin
Date
Bahai
Chinese
HinduLunar
ISO
MayanLongCount
OldHinduLunar
Roman
StandardDate
Armenian
Coptic
Egyptian
Ethiopic
French
Gregorian
Hebrew
HinduSolar
Islamic
ObservationalIslamic
Julian
ModifiedFrench
OldHinduSolar
Persian
ArithmeticPersian

--------------------------------------------------------------------------------

Calendar Classes

Each supported calendar has its own class that encapsulates behavior standard
among all the calendar classes, as well as behavior specific to the given
calendar class.

All calendar classes are descended from the abstract class ProtoDate, that
encapsulates a large number of constants and general utility routines relied on
by many of the calendars. The only date conversion behavior specified by
ProtoDate that subclasses must implement is that which converts fixed dates
(R.D.) to the calendar instance fields. The calendars MayanHaab, MayanTzolkin,
and Balinese, that have no ability to convert from their instance fields to a
single fixed date, inherit directly from ProtoDate.

The primary abstract subclass of ProtoDate is Date, that additionally specifies
that subclasses must implement behavior to convert from the calendar's instance
fields to a specific fixed date. Calendars such as Bahai and Chinese that do not
use the standard year, month, day format inherit directly from Date. All the
other calendars such as Gregorian and Hebrew that do use the standard year,
month, day format inherit from the abstract subclass of Date StandardDate, that
defines these fields and the other non-calendar-specific behaviors that operate
on them.

NOTE: Because the class calendrica.Date has the same base name as java.util.Date,
classes in Java source files that import both java.util.Date and calendrica.Date
need to fully qualify those names when referring to them.

--------------------------------------------------------------------------------

Member Visibility

All class members are declared public unless there is a specific reason to make
them protected or private. The only place protected visibility is used is in the
abstract classes described above. The only place private visibility is used is
for inner classes containing static final arrays used only by specific utility
routines.

--------------------------------------------------------------------------------

Instance Fields

Each calendar class encapsulates the instance fields used by a date of that
calendar. For example, the Gregorian calendar is supported by the class
Gregorian, and encapsulates the instance fields year, month and day that it
inherits from StandardDate. As another example, the class Chinese encapsulates
the instance fields cycle, year, month, leapMonth and day of the Chinese
calendar. The instance fields of a calendar class are declared public, and may be
accessed and manipulated directly.

Each calendar class is used 1) for storing the fields of a date on the given
calendar, 2) to encapsulate the standard behavior needed to convert from fixed
dates to those fields and back (if applicable), and 3) to encapsulate methods
related to the computation of holidays on that calendar. There is no instance
field to hold the fixed (R.D.) date corresponding to the other fields-- this
information is calculated on demand with other methods (see below).

--------------------------------------------------------------------------------

Naming Conventions

When converting from the original Lisp, I followed the following naming
transformation guidelines:

1) Lisp function names such as daylight-savings-start were changed to use the
Java convention of intercapitalization with the first letter in lower case, e.g.,
daylightSavingsStart, except for very short names consisting of initials such as
BCE, that remain unchanged.

2) Predicates such as short-kislev? are renamed using the Java convention of
prefixing the name with "is" or "has", e.g., hasShortKislev.

3) Constants such as arya-jovian-period are renamed using the Java convention of
all capitals with underscores for spaces, e.g. ARYA_JOVIAN_PERIOD.

4) In general, wherever a name includes the name of the calendar it deals with,
such as bahai-epoch, and where that item is declared in the class of the same
name, the calendar-specific part of the name is dropped. Thus, most of the
calendar classes have a constant named EPOCH, that is the epoch of that calendar,
and that may be referred to by clients and other calendars by its fully-qualified
name, e.g., Bahai.EPOCH. Another example is the function name sunrise, that is
declared in ProtoDate, and also in HinduSolar, where in Lisp it was called
hindu-sunrise. Thus, wherever the unqualified method name sunrise is called from
within the HinduSolar class, it refers to the function HinduSolar.sunrise().
Other classes that call sunrise() refer to ProtoDate.sunrise().

--------------------------------------------------------------------------------

Constants

Each class may define one or more constants. A few of the more useful constants
to clients are found in ProtoDate, and include SUNDAY through SATURDAY and
JANUARY through DECEMBER. Clients can refer to them as (e.g.) ProtoDate.SUNDAY or
Date.SUNDAY, since Date inherits from ProtoDate.

--------------------------------------------------------------------------------

Constructors

Each concrete calendar class contains four constructors:

1) a default constructor that initializes each instance field to zero (or false),

2) a constructor that takes a fixed (R.D.) date and that initializes the instance
fields with the values corresponding to that fixed date,

3) a constructor that takes any object of class calendrica.Date (see below),
converts it to a fixed date, and then initializes the instance fields with the
values corresponding to that fixed date, and

4) a constructor that takes individual parameters corresponding to the instance
fields, and assigns those parameters directly to the instance fields.

--------------------------------------------------------------------------------

Date Conversion Methods

All subclasses of ProtoDate support several instance methods whose names begin
with "from". These are fromFixed(), fromDate() and fromArray().

fromFixed() takes a fixed date, converts it into the calendar's format, and
assigns the result to the target object's fields.

fromDate() takes any subclass of Date, converts it to a fixed date and then into
the calendar's format, and assigns the result to the target object's fields.

fromArray() initializes the target object's fields from the values in a provided
int[]. There must be one item in the array for each instance field in the class,
and the instance fields are initialized from the array starting at element zero,
in the same order they appear in the class declaration. Classes that use boolean
fields take zero to mean false and any other value to mean true. Extra elements
of the array are ignored.

ProtoDate also defines the static method convert(), that converts any given
subclass of Date to any given subclass of ProtoDate.

All subclasses of Date also support two versions of the method toFixed(), a
static one that takes as its arguments the values of the fields for it's calendar
format, and that returns the corresponding fixed date, and an instance version
that uses the fields in the target object from which to compute the fixed date.

Date also defines the instance function convertTo(), that converts the target
object to any given subclass of ProtoDate.

--------------------------------------------------------------------------------

Support Methods

Each calendar class may define one or more "support" methods, that directly
support the conversion between calendar dates and fixed dates. These are declared
public as they may be of use to clients in their own right, especially those
defined by ProtoDate, and include (for example) methods for calculating the
number of days between dates, mathematical functions, and functions dealing with
time and astronomy.

--------------------------------------------------------------------------------

Auxiliary Methods

Each calendar class may define one or more "auxiliary" methods, that do not
directly support date conversion, but that provide examples of how holidays
pertinant to that calendar may be computed.

--------------------------------------------------------------------------------

Object Methods

Each calendar class provides certain object methods that support standard Java
idioms. These include toString(), format(), and equals(). In addition, all
calendar classes implement the Cloneable and Serializable interfaces.

--------------------------------------------------------------------------------

toString() and format()

Each calendar class and classes Time and Location support two instance methods
that return String class representations of the target object. toString() returns
a String suitable for serialization. For example:

calendrica.Gregorian[year=1965,month=5,day=15]

format() returns a string formatted for easy reading:

Saturday, May 15, 1965

Objects of different calendar classes have different formatting methods. For
example, the same date converted to the Chinese calendar and then formatted with
format() yields:

cycle 77, year Yi-Si, month 4, day 15

--------------------------------------------------------------------------------

Exceptions

Calendrica defines two exception classes: BogusDateException and
BogusTimeException. Currently the following routines directly throw
BogusDateException:

Hebrew.omer()
Chinese.age()
ChineseName.ChineseName(int,int)
MayanTzolkin.calendarRoundOnOrBefore()

Unlike Calendrica 1.0, none of the calendar date conversion routines in
Calendrica 2.0 throw BogusDateException, so handling this exception is much
simpler.

Only ProtoDate.momentFromDepression() directly throws BogusTimeException, when it
is asked to calculate an angle of the sun that never occurs on a given date at a
given locale (such as sunrise near the poles at certain times of the year.) Some
of the routines that rely on ProtoDate.momentFromDepression() may also indirectly
throw BogusTimeException.

--------------------------------------------------------------------------------

class Time

Time encapsulates the fields hour, minute, and second, and the behavior needed to
convert between these fields and a fixed fraction of a day.

--------------------------------------------------------------------------------

class Location

Location encapsulates the data needed to specify a location on the Earth's
surface: place name, latitude, longitude, elevation, and time zone.

--------------------------------------------------------------------------------

class FixedVector

Methods that return a list of zero or more fixed dates do so by returning a
FixedVector, that is a subclass of java.util.Vector with two additional methods:
addFixed(), that adds a fixed date element to the vector, and fixedAt(), that
retrieves the fixed date in the vector at the given index.

--------------------------------------------------------------------------------

Source Files

The source files for the Calendrica package include:

ArithmeticPersian.java
Armenian.java
Bahai.java
Balinese.java
BogusDateException.java
BogusTimeException.java
Chinese.java
Coptic.java
Date.java
Ecclesiastical.java
Egyptian.java
Ethiopic.java
FixedVector.java
French.java
FutureBahai.java
Gregorian.java
Hebrew.java
HinduLunar.java
HinduSolar.java
Islamic.java
ISO.java
Julian.java
Location.java
MayanHaab.java
MayanLongCount.java
MayanTzolkin.java
ModifiedFrench.java
ObservationalIslamic.java
OldHinduLunar.java
OldHinduSolar.java
Persian.java
ProtoDate.java
Roman.java
StandardDate.java
Time.java

These files must be compiled together as one project producing a single class
or jar file.

In addition, this release includes a stand-alone application CalendricaTest, in
the file "CalendricaTest.java", that is not part of the Calendrica package, but
tests Calendrica's date conversion algorithms and output formats. It also
generates a list of sample holidays (but does not check them for accuracy). The
amount of time CalendricaTest takes to run can vary widely depending on the
quality of your Java implementation and the speed of the hardware you run it on.
On a Macintosh PowerBook 540c that has no native floating point facilities it
took over 45 minutes to run, whereas on a Macintosh compatible running at 225MHz
it completes in less than 20 seconds.

********************************************************************************

CALENDAR CLASS TEMPLATES

--------------------------------------------------------------------------------

The following class template is used for calendars whose fields have the form
month, day, year.

--------------------------------------------------------------------------------
package calendrica;


public class Cal extends StandardDate {

//
// constructors
//

public Cal() { }

public Cal(long date) {
super(date);
}

public Cal(Date date) {
super(date);
}

public Cal(long year, int month, int day) {
super(year, month, day);
}

//
// constants
//

//
// date conversion methods
//

public static long toFixed(long year, int month, int day) {
return 0;
}

public long toFixed() {
return toFixed(month, day, year);
}

public void fromFixed(long date) {
month = 0;
day = 0;
year = 0;
}

//
// support methods
//

//
// auxiliary methods
//

//
// object methods
//

public String format() {
return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#}",
new Object[]{
nameFromDayOfWeek(toFixed(), dayOfWeekNames),
new Integer(day),
nameFromMonth(month, monthNames),
new Long(year)
}
);
}

public boolean equals(Object obj) {
if(!(obj instanceof Cal))
return false;

return internalEquals(obj);
}
}
--------------------------------------------------------------------------------

The following class template is used for calendars whose fields have a
non-standard arrangement.

--------------------------------------------------------------------------------
package calendrica;


public class Cal extends Date {

//
// fields
//

public int a;
public int b;
public int c;

//
// constructors
//

public Cal() { }

public Cal(long date) {
super(date);
}

public Cal(Date date) {
super(date);
}

public Cal(int a, int b, int c) {
this.a = a;
this.b = b;
this.c = c;
}

//
// constants
//

//
// date conversion methods
//

public static long toFixed(int a, int b, int c) {
return 0;
}

public long toFixed() {
return toFixed(a, b, c);
}

public void fromFixed(long date) {
a = 0;
b = 0;
c = 0;
}

public void fromArray(int[] a) {
this.a = a[0];
this.b = a[1];
this.c = a[2];
}

//
// support methods
//

//
// auxiliary methods
//

//
// object methods
//

protected String toStringFields() {
return "a=" + a + ",b=" + b + ",c=" + c;
}

public String format() {
return java.text.MessageFormat.format("{0}.{1}.{2}",
new Object[]{
new Integer(a),
new Integer(b),
new Integer(c)
}
);
}

public boolean equals(Object obj) {
if(this == obj)
return true;

if(!(obj instanceof Cal))
return false;

Cal o = (Cal)obj;

return
o.a == a &&
o.b == b &&
o.c == c ;
}
}
--------------------------------------------------------------------------------
