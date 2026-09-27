;;;; The Functions (code, comments, and definitions) contained in this
;;;; file (the "Program") were written by Edward M. Reingold and Nachum
;;;; Dershowitz (the "Authors"), who retain all rights to them except as
;;;; granted in the License and subject to the warranty and liability
;;;; limitations below.  These Functions are explained in the Authors'
;;;; book, "Calendrical Calculations: The Millennium Edition" (Cambridge
;;;; University Press, 2001), and are subject to an international
;;;; copyright and a Patent Pending on certain functions.
;;;;
;;;; The Authors' public service intent is more liberal than suggested
;;;; by the License below, as are their licensing policies for otherwise
;;;; nonallowed uses such as--without limitation--those in commercial,
;;;; web-site, and large-scale academic contexts.  Please see the
;;;; web-site
;;;;
;;;;     http://www.calendarists.com
;;;;
;;;; for all uses not authorized below; in case there is cause for doubt
;;;; about whether a use you contemplate is authorized, please contact
;;;; the Authors (e-mail: reingold@cs.uiuc.edu).  For commercial
;;;; licensing information, contact the first author at the Department
;;;; of Computer Science, 1304 West Springfield Avenue, University of
;;;; Illinois at Urbana-Champaign, Urbana, IL 61801-2987, USA.
;;;;
;;;; 1. LICENSE.  The Authors grant you a license for personal use only.
;;;; This means that for strictly personal use you may copy and use the
;;;; code, and keep a backup or archival copy also.  Any other uses,
;;;; including without limitation, allowing the code or its output to be
;;;; accessed, used, or available to others, is not permitted.
;;;;
;;;; 2. WARRANTY.
;;;;
;;;; (a) THE AUTHORS PROVIDE NO WARRANTIES OF ANY KIND, EITHER
;;;;     EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITING THE
;;;;     GENERALITY OF THE FOREGOING, ANY IMPLIED WARRANTY OF
;;;;     MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
;;;;
;;;; (b) THE AUTHORS SHALL NOT BE LIABLE TO YOU OR ANY THIRD PARTIES
;;;;     FOR DAMAGES OF ANY KIND, INCLUDING WITHOUT LIMITATION, ANY LOST
;;;;     PROFITS, LOST SAVINGS, OR ANY OTHER INCIDENTAL OR CONSEQUENTIAL
;;;;     DAMAGES ARISING OUT OF OR RELATED TO THE USE, INABILITY TO USE,
;;;;     OR INACCURACY OF CALCULATIONS, OF THE CODE AND FUNCTIONS
;;;;     CONTAINED HEREIN, OR THE BREACH OF ANY EXPRESS OR IMPLIED
;;;;     WARRANTY, EVEN IF THE AUTHORS OR PUBLISHER HAVE BEEN ADVISED OF
;;;;     THE POSSIBILITY OF THOSE DAMAGES.
;;;;
;;;; (c) THE FOREGOING WARRANTY MAY GIVE YOU SPECIFIC LEGAL
;;;;     RIGHTS WHICH MAY VARY FROM STATE TO STATE IN THE U.S.A.
;;;;
;;;; 3. LIMITATION OF LICENSEE REMEDIES.  You acknowledge and agree that
;;;; your exclusive remedy (in law or in equity), and Authors' entire
;;;; liability with respect to the material herein, for any breach of
;;;; representation or for any inaccuracy shall be a refund of the license
;;;; fee or service and handling charge which you paid the Authors, if any.
;;;;
;;;; SOME STATES IN THE U.S.A. DO NOT ALLOW THE EXCLUSION OR LIMITATION OF
;;;; LIABILITY FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE
;;;; EXCLUSIONS OR LIMITATION MAY NOT APPLY TO YOU.
;;;;
;;;; 4. DISCLAIMER.  Except as expressly set forth above, the Authors:
;;;;
;;;;   (a) make no other warranties with respect to the material in the
;;;;   Program and expressly disclaim any others;
;;;;
;;;;   (b) do not warrant that the material contained in the Program will
;;;;   meet your requirements or that their operation shall be
;;;;   uninterrupted or error-free;
;;;;
;;;;   (c) license this material on an "as is" basis, and the entire risk
;;;;   as to the quality, accuracy, and performance of the Program is
;;;;   yours, should the code prove defective (except as expressly
;;;;   warranted herein).  You alone assume the entire cost of all
;;;;   necessary corrections.
;;;;
;;;; Sample values for the functions (useful for debugging) are given in
;;;; Appendix C of the book and on the accompanying CD.  
;;;; These sample values are not available electronically.

;;;; Last modified 21 March 2001.

(if (not (find-package "CC"))
    (defpackage "CC"))
(in-package "CC")

(export '(true false bogus sunday monday tuesday wednesday
          thursday friday saturday january february march april may
          june july august september october november december kalends
          nones ides ayyam-i-ha arya-solar-year arya-solar-month
          arya-lunar-month arya-lunar-day arya-jovian-period mecca
          spring summer autumn winter new first-quarter full
          last-quarter haifa islamic-locale jerusalem tehran paris
          ujjain hindu-locale quotient day-of-week-from-fixed
          standard-month standard-day standard-year time-of-day
          moment-from-jd jd-from-moment fixed-from-jd jd-from-fixed
          fixed-from-mjd mjd-from-fixed time-from-moment egyptian-date
          fixed-from-egyptian egyptian-from-fixed armenian-date
          fixed-from-armenian armenian-from-fixed gregorian-date
          gregorian-leap-year?  fixed-from-gregorian
          gregorian-year-from-fixed gregorian-from-fixed
          gregorian-date-difference day-number days-remaining
          alt-fixed-from-gregorian alt-gregorian-from-fixed
          alt-gregorian-year-from-fixed independence-day
          kday-on-or-before kday-on-or-after kday-nearest kday-after
          kday-before nth-kday first-kday last-kday labor-day
          memorial-day election-day daylight-saving-start
          daylight-saving-end christmas advent epiphany iso-date
          iso-week iso-day iso-year fixed-from-iso iso-from-fixed
          julian-date bce ce julian-leap-year? fixed-from-julian
          julian-from-fixed roman-date roman-year roman-month
          roman-event roman-count roman-leap ides-of-month
          nones-of-month fixed-from-roman roman-from-fixed
          julian-in-gregorian eastern-orthodox-christmas coptic-date
          coptic-leap-year? fixed-from-coptic coptic-from-fixed
          ethiopic-date fixed-from-ethiopic ethiopic-from-fixed
          coptic-in-gregorian coptic-christmas orthodox-easter
          alt-orthodox-easter easter pentecost islamic-date
          islamic-leap-year? fixed-from-islamic islamic-from-fixed
          islamic-in-gregorian mawlid-an-nabi bahai-date bahai-major
          bahai-cycle bahai-year bahai-month bahai-day
          fixed-from-bahai bahai-from-fixed bahai-new-year
          feast-of-ridvan hebrew-date hebrew-leap-year?
          last-month-of-hebrew-year last-day-of-hebrew-month
          hebrew-new-year molad fixed-from-hebrew hebrew-from-fixed
          yom-kippur passover omer purim ta-anit-esther tishah-be-av
          birkath-ha-hama sh-ela yom-ha-zikaron
          hebrew-birthday-in-gregorian yahrzeit-in-gregorian
          mayan-long-count-date mayan-haab-date mayan-tzolkin-date
          mayan-baktun mayan-katun mayan-tun mayan-uinal mayan-kin
          mayan-haab-month mayan-haab-day mayan-tzolkin-number
          mayan-tzolkin-name fixed-from-mayan-long-count
          mayan-long-count-from-fixed mayan-haab-from-fixed
          mayan-haab-on-or-before mayan-tzolkin-from-fixed
          mayan-tzolkin-on-or-before mayan-calendar-round-on-or-before
          old-hindu-lunar-date old-hindu-lunar-month
          old-hindu-lunar-leap old-hindu-lunar-day
          old-hindu-lunar-year hindu-solar-date hindu-day-count
          old-hindu-solar-from-fixed fixed-from-old-hindu-solar
          old-hindu-lunar-leap-year? old-hindu-lunar-from-fixed
          fixed-from-old-hindu-lunar jovian-year balinese-date
          bali-luang bali-dwiwara bali-triwara bali-caturwara
          bali-pancawara bali-sadwara bali-saptawara bali-asatawara
          bali-sangawara bali-dasawara bali-day-from-fixed
          bali-luang-from-fixed bali-dwiwara-from-fixed
          bali-triwara-from-fixed bali-caturwara-from-fixed
          bali-pancawara-from-fixed bali-sadwara-from-fixed
          bali-saptawara-from-fixed bali-asatawara-from-fixed
          bali-sangawara-from-fixed bali-dasawara-from-fixed
          bali-pawukon-from-fixed bali-week-from-fixed
          bali-on-or-before positions-in-interval
          kajeng-keliwon-in-gregorian tumpek-in-gregorian hr mt deg
          angle location latitude longitude elevation zone direction
          standard-from-universal
          universal-from-standard local-from-universal
          universal-from-local standard-from-local local-from-standard
          midday midnight local-from-apparent apparent-from-local
          moment-from-depression dawn dusk sunrise sunset islamic-sunrise
          islamic-sunset jewish-dusk jewish-sabbath-ends temporal-hour
          standard-from-sundial jewish-morning-end asr
          universal-from-dynamical dynamical-from-universal
          sidereal-from-moment equation-of-time solar-longitude
          solar-longitude-after lunar-longitude nth-new-moon
          new-moon-before new-moon-after lunar-phase
          lunar-phase-before lunar-phase-after
          future-bahai-new-year-on-or-before fixed-from-future-bahai
          future-bahai-from-fixed phasis-on-or-before
          fixed-from-observational-islamic
          observational-islamic-from-fixed astronomical-easter
          lunar-latitude lunar-altitude classical-passover-eve
          persian-date persian-new-year-on-or-before
          fixed-from-persian persian-from-fixed
          arithmetic-persian-leap-year?  fixed-from-arithmetic-persian
          arithmetic-persian-year-from-fixed
          arithmetic-persian-from-fixed naw-ruz french-date
          french-new-year-on-or-before fixed-from-french
          french-from-fixed modified-french-leap-year?
          fixed-from-modified-french modified-french-from-fixed
          chinese-date chinese-cycle chinese-year chinese-month
          chinese-leap chinese-day chinese-location
          chinese-solar-longitude-on-or-after current-major-solar-term
          major-solar-term-on-or-after current-minor-solar-term
          minor-solar-term-on-or-after chinese-new-year-on-or-before
          chinese-new-year chinese-from-fixed fixed-from-chinese
          chinese-name chinese-stem chinese-branch
          chinese-sexagesimal-name chinese-name-difference
          chinese-name-of-year chinese-name-of-month
          chinese-name-of-day chinese-day-name-on-or-before
          dragon-festival qing-ming chinese-age japanese-location
          hindu-lunar-date hindu-lunar-month hindu-lunar-leap-month
          hindu-lunar-day hindu-lunar-leap-day hindu-lunar-year
          lunar-day-after hindu-solar-from-fixed
          fixed-from-hindu-solar hindu-lunar-from-fixed
          fixed-from-hindu-lunar hindu-sunrise alt-hindu-sunrise
          lunar-station hindu-solar-longitude-after mesha-samkranti
          hindu-lunar-new-year karana yoga
          sacred-wednesdays-in-gregorian ))


;;;; Section: Basic Code

(defconstant true
  ;; TYPE boolean
  ;; Constant representing true.
  t)

(defconstant false
  ;; TYPE boolean
  ;; Constant representing false.
  nil)

(defconstant bogus
  ;; TYPE string
  ;; Used to denote nonexistent dates.
  "bogus")

(defun quotient (m n)
  ;; TYPE (real nonzero-real) -> integer
  ;; Whole part of $m$/$n$.
  (floor m n))

(defun adjusted-mod (x y)
  ;; TYPE (real real) -> real
  ;; The value of ($x$ mod $y$) with $y$ instead of 0.
  (+ y (mod x (- y))))

(defmacro next (index initial condition)
  ;; TYPE (* integer (integer->boolean)) -> integer
  ;; First integer greater or equal to $initial$ such that
  ;; $condition$ holds.
  `(do ((,index ,initial (1+ ,index)))
       (,condition ,index)))

(defmacro final (index initial condition)
  ;; TYPE (* integer (integer->boolean)) -> integer
  ;; Last integer greater or equal to $initial$ such that
  ;; $condition$ holds.
  `(do ((,index ,initial (1+ ,index)))
       ((not ,condition) (1- ,index))))

(defmacro sum (expression index initial condition)
  ;; TYPE ((integer->real) * integer (integer->boolean))
  ;; TYPE -> real
  ;; Sum $expression$ for $index$ = $initial$ and successive
  ;; integers, as long as $condition$ holds.
  (let* ((temp (gensym)))
    `(do ((,temp 0 (+ ,temp ,expression))
          (,index ,initial (1+ ,index)))
         ((not ,condition) ,temp))))

(defmacro binary-search (l lo h hi x test end)
  ;; TYPE (* real * real * (real->boolean)
  ;; TYPE  ((real real)->boolean)) -> real
  ;; Bisection search for $x$ in $lo$..$hi$ such that
  ;; $end$ holds.  $test$ determines when to go left.
  (let* ((left (gensym)))
    `(do* ((,x false (/ (+ ,h ,l) 2))
           (,left false ,test)
           (,l ,lo (if ,left ,l ,x))
           (,h ,hi (if ,left ,x ,h)))
          (,end (/ (+ ,h ,l) 2)))))

(defmacro sigma (list body)
  ;; TYPE (list-of-pairs (list-of-reals->real))
  ;; TYPE -> real
  ;; $list$ is of the form ((i1 l1)..(in ln)).
  ;; Sum of $body$ for indices i1..in
  ;; running simultaneously thru lists l1..ln.
  `(apply '+ (mapcar (function (lambda
                                 ,(mapcar 'car list)
                                 ,body))
                     ,@(mapcar 'cadr list))))

(defun poly (x a)
  ;; TYPE (real list-of-reals) -> real
  ;; Sum powers of $x$ with coefficients (from order 0 up)
  ;; in list $a$.
  (if (equal a nil)
      0
    (+ (first a) (* x (poly x (cdr a))))))

(defconstant sunday
  ;; TYPE day-of-week
  ;; Residue class for Sunday.
  0)

(defconstant monday
  ;; TYPE day-of-week
  ;; Residue class for Monday.
  (+ sunday 1))

(defconstant tuesday
  ;; TYPE day-of-week
  ;; Residue class for Tuesday.
  (+ sunday 2))

(defconstant wednesday
  ;; TYPE day-of-week
  ;; Residue class for Wednesday.
  (+ sunday 3))

(defconstant thursday
  ;; TYPE day-of-week
  ;; Residue class for Thursday.
  (+ sunday 4))

(defconstant friday
  ;; TYPE day-of-week
  ;; Residue class for Friday.
  (+ sunday 5))

(defconstant saturday
  ;; TYPE day-of-week
  ;; Residue class for Saturday.
  (+ sunday 6))

(defun day-of-week-from-fixed (date)
  ;; TYPE fixed-date -> day-of-week
  ;; The residue class of the day of the week of $date$.
  (mod date 7))

(defun standard-month (date)
  ;; TYPE standard-date -> standard-month
  ;; Month field of $date$ = (year month day).
  (second date))

(defun standard-day (date)
  ;; TYPE standard-date -> standard-day
  ;; Day field of $date$ = (year month day).
  (third date))

(defun standard-year (date)
  ;; TYPE standard-date -> standard-year
  ;; Year field of $date$ = (year month day).
  (first date))

(defun time-of-day (hour minute second)
  ;; TYPE (hour minute second) -> time
  (list hour minute second))

(defconstant jd-epoch
  ;; TYPE moment
  ;; Fixed time of start of the julian day number.
  -1721424.5l0)

(defun moment-from-jd (jd)
  ;; TYPE julian-day-number -> moment
  ;; Moment of julian day number $jd$.
  (+ jd jd-epoch))

(defun jd-from-moment (tee)
  ;; TYPE moment -> julian-day-number
  ;; Julian day number of moment $tee$.
  (- tee jd-epoch))

(defun fixed-from-jd (jd)
  ;; TYPE julian-day-number -> fixed-date
  ;; Fixed date of julian day number $jd$.
  (floor (moment-from-jd jd)))

(defun jd-from-fixed (date)
  ;; TYPE fixed-date -> julian-day-number
  ;; Julian day number of fixed $date$.
  (jd-from-moment date))

(defconstant mjd-epoch
  ;; TYPE fixed-date
  ;; Fixed time of start of the modified julian day number.
  678576)

(defun fixed-from-mjd (mjd)
  ;; TYPE julian-day-number -> fixed-date
  ;; Fixed date of modified julian day number $mjd$.
  (+ mjd mjd-epoch))

(defun mjd-from-fixed (date)
  ;; TYPE fixed-date -> julian-day-number
  ;; Modified julian day number of fixed $date$.
  (- date mjd-epoch))

(defun time-from-moment (tee)
  ;; TYPE moment -> time
  ;; Time of day (hour minute second) from moment $tee$.
  (let* ((hour (floor (mod (* tee 24) 24)))
         (minute (floor (mod (* tee 24 60) 60)))
         (second (mod (* tee 24 60 60l0) 60)))
    (time-of-day hour minute second)))


;;;; Section: Egyptian/Armenian Calendars

(defun egyptian-date (year month day)
  ;; TYPE (egyptian-year egyptian-month egyptian-day)
  ;; TYPE -> egyptian-date
  (list year month day))

(defconstant egyptian-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Egyptian (Nabonasser)
  ;; calendar.
  ;; JD 1448638 = February 26, 747 BCE (Julian).
  (fixed-from-jd 1448638))

(defun fixed-from-egyptian (e-date)
  ;; TYPE egyptian-date -> fixed-date
  ;; Fixed date of Egyptian date.
  (let* ((month (standard-month e-date))
         (day (standard-day e-date))
         (year (standard-year e-date)))
    (+ egyptian-epoch   ; Days before start of calendar
       (* 365 (1- year)); Days in prior years
       (* 30 (1- month)); Days in prior months this year
       day -1)))        ; Days so far this month

(defun egyptian-from-fixed (date)
  ;; TYPE fixed-date -> egyptian-date
  ;; Egyptian equivalent of fixed $date$.
  (let* ((days ; Elapsed days since epoch.
          (- date egyptian-epoch))
         (year ; Year since epoch.
          (1+ (quotient days 365)))
         (month; Calculate the month by division.
          (1+ (quotient (mod days 365)
                        30)))
         (day  ; Calculate the day by subtraction.
          (- days
             (* 365 (1- year))
             (* 30 (1- month))
             -1)))
    (egyptian-date year month day)))

(defun armenian-date (year month day)
  ;; TYPE (armenian-year armenian-month armenian-day)
  ;; TYPE -> armenian-date
  (list year month day))

(defconstant armenian-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Armenian calendar.
  ;; = July 11, 552 CE (Julian).
  201443)

(defun fixed-from-armenian (a-date)
  ;; TYPE armenian-date -> fixed-date
  ;; Fixed date of Armenian date.
  (let* ((month (standard-month a-date))
         (day (standard-day a-date))
         (year (standard-year a-date)))
    (+ armenian-epoch
       (- (fixed-from-egyptian
           (egyptian-date year month day))
          egyptian-epoch))))

(defun armenian-from-fixed (date)
  ;; TYPE fixed-date -> armenian-date
  ;; Armenian equivalent of fixed $date$.
  (egyptian-from-fixed
   (+ date (- egyptian-epoch armenian-epoch))))


;;;; Section: Gregorian Calendar

(defun gregorian-date (year month day)
  ;; TYPE (gregorian-year gregorian-month gregorian-day)
  ;; TYPE -> gregorian-date
  (list year month day))

(defconstant gregorian-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the (proleptic) Gregorian
  ;; calendar.
  1)

(defconstant january
  ;; TYPE standard-month
  ;; January on Julian/Gregorian calendar.
  1)

(defconstant february
  ;; TYPE standard-month
  ;; February on Julian/Gregorian calendar.
  (1+ january))

(defconstant march
  ;; TYPE standard-month
  ;; March on Julian/Gregorian calendar.
  (+ january 2))

(defconstant april
  ;; TYPE standard-month
  ;; April on Julian/Gregorian calendar.
  (+ january 3))

(defconstant may
  ;; TYPE standard-month
  ;; May on Julian/Gregorian calendar.
  (+ january 4))

(defconstant june
  ;; TYPE standard-month
  ;; June on Julian/Gregorian calendar.
  (+ january 5))

(defconstant july
  ;; TYPE standard-month
  ;; July on Julian/Gregorian calendar.
  (+ january 6))

(defconstant august
  ;; TYPE standard-month
  ;; August on Julian/Gregorian calendar.
  (+ january 7))

(defconstant september
  ;; TYPE standard-month
  ;; September on Julian/Gregorian calendar.
  (+ january 8))

(defconstant october
  ;; TYPE standard-month
  ;; October on Julian/Gregorian calendar.
  (+ january 9))

(defconstant november
  ;; TYPE standard-month
  ;; November on Julian/Gregorian calendar.
  (+ january 10))

(defconstant december
  ;; TYPE standard-month
  ;; December on Julian/Gregorian calendar.
  (+ january 11))

(defun gregorian-leap-year? (g-year)
  ;; TYPE gregorian-year -> boolean
  ;; True if $g-year$ is a leap year on the Gregorian
  ;; calendar.
  (and (= (mod g-year 4) 0)
       (not (member (mod g-year 400)
                    (list 100 200 300)))))

(defun fixed-from-gregorian (g-date)
  ;; TYPE gregorian-date -> fixed-date
  ;; Fixed date equivalent to the Gregorian date.
  (let* ((month (standard-month g-date))
         (day (standard-day g-date))
         (year (standard-year g-date)))
      (+ (1- gregorian-epoch); Days before start of calendar
         (* 365 (1- year)); Ordinary days since epoch
         (quotient (1- year)
                   4); Julian leap days since epoch...
         (-          ; ...minus century years since epoch...
          (quotient (1- year) 100))
         (quotient   ; ...plus years since epoch divisible...
          (1- year) 400)  ; ...by 400.
         (quotient        ; Days in prior months this year...
          (- (* 367 month) 362); ...assuming 30-day Feb
          12)
         (if (<= month 2) ; Correct for 28- or 29-day Feb
             0
           (if (gregorian-leap-year? year)
               -1
             -2))
         day)))          ; Days so far this month.

(defun gregorian-year-from-fixed (date)
  ;; TYPE fixed-date -> gregorian-year
  ;; Gregorian year corresponding to the fixed $date$.
  (let* ((d0        ; Prior days.
          (- date gregorian-epoch))
         (n400      ; Completed 400-year cycles.
          (quotient d0 146097))
         (d1        ; Prior days not in n400.
          (mod d0 146097))
         (n100      ; 100-year cycles not in n400.
          (quotient d1 36524))
         (d2        ; Prior days not in n400 or n100.
          (mod d1 36524))
         (n4        ; 4-year cycles not in n400 or n100.
          (quotient d2 1461))
         (d3        ; Prior days not in n400, n100, or n4.
          (mod d2 1461))
         (n1        ; Years not in n400, n100, or n4.
          (quotient d3 365))
         (year (+ (* 400 n400)
                  (* 100 n100)
                  (* 4 n4)
                  n1)))
    (if (or (= n100 4) (= n1 4))
        year      ; Date is day 366 in a leap year.
      (1+ year)))); Date is ordinal day (1+ (mod d3 365))
                  ; in (1+ year).

(defun gregorian-from-fixed (date)
  ;; TYPE fixed-date -> gregorian-date
  ;; Gregorian (year month day) corresponding to fixed $date$.
  (let* ((year (gregorian-year-from-fixed date))
         (prior-days; This year
          (- date (fixed-from-gregorian
                   (gregorian-date year january 1))))
         (correction; To simulate a 30-day Feb
          (if (< date (fixed-from-gregorian
                       (gregorian-date year march 1)))
              0
            (if (gregorian-leap-year? year)
                1
              2)))
         (month     ; Assuming a 30-day Feb
          (quotient
           (+ (* 12 (+ prior-days correction)) 373)
           367))
         (day       ; Calculate the day by subtraction.
          (1+ (- date
                 (fixed-from-gregorian
                  (gregorian-date year month 1))))))
    (gregorian-date year month day)))

(defun gregorian-date-difference (g-date1 g-date2)
  ;; TYPE (gregorian-date gregorian-date) -> integer
  ;; Number of days from Gregorian date $g-date1$ until
  ;; $g-date2$.
  (- (fixed-from-gregorian g-date2)
     (fixed-from-gregorian g-date1)))

(defun day-number (g-date)
  ;; TYPE gregorian-date -> positive-integer
  ;; Day number in year of Gregorian date $g-date$.
  (gregorian-date-difference
   (gregorian-date (1- (standard-year g-date)) december 31)
   g-date))

(defun days-remaining (g-date)
  ;; TYPE gregorian-date -> nonnegative-integer
  ;; Days remaining in year after Gregorian date $g-date$.
  (gregorian-date-difference
   g-date
   (gregorian-date (standard-year g-date) december 31)))

(defun alt-fixed-from-gregorian (g-date)
  ;; TYPE gregorian-date -> fixed-date
  ;; Alternative calculation of fixed date equivalent to the
  ;; Gregorian date.
  (let* ((month (standard-month g-date))
         (day (standard-day g-date))
         (year (standard-year g-date))
         (m (adjusted-mod (- month 2) 12))
         (y (+ year (quotient (+ month 9) 12))))
      (+ (1- gregorian-epoch)
         -306        ; Days in March...December.
         (* 365 (1- y)); Ordinary days since epoch.
         (quotient (1- y)
                   4); Julian leap days since epoch...
         (-          ; ...minus century years since epoch...
          (quotient (1- y) 100))
         (quotient   ; ...plus years since epoch divisible...
          (1- y) 400); ...by 400.
         (quotient   ; Days in prior months this year.
          (1- (* 3 m))
          5)
         (* 30 (1- m))
         day)))       ; Days so far this month.

(defun alt-gregorian-from-fixed (date)
  ;; TYPE fixed-date -> gregorian-date
  ;; Alternative calculation of Gregorian (year month day)
  ;; corresponding to fixed $date$.
  (let* ((y (gregorian-year-from-fixed
             (+ (1- gregorian-epoch)
                date
                306)))
         (prior-days
          (- date (fixed-from-gregorian
                   (gregorian-date (1- y) 3 1))))
         (month
          (adjusted-mod (+ (quotient
                            (+ (* 5 prior-days) 155)
                            153)
                           2)
                        12))
         (year (- y (quotient (+ month 9) 12)))
         (day
          (1+ (- date
                 (fixed-from-gregorian
                  (gregorian-date year month 1))))))
    (gregorian-date year month day)))

(defun alt-gregorian-year-from-fixed (date)
  ;; TYPE fixed-date -> gregorian-year
  ;; Gregorian year corresponding to the fixed $date$.
  (let* ((approx ; approximate year
          (quotient (- date gregorian-epoch -2)
                    146097/400))
         (start  ; start of next year
          (+ gregorian-epoch
             (* 365 approx)
             (quotient approx 4)
             (- (quotient approx 100))
             (quotient approx 400))))
    (if (< date start)
        approx
      (1+ approx))))

(defun independence-day (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of United States Independence Day in
  ;; Gregorian year.
  (fixed-from-gregorian (gregorian-date g-year july 4)))

(defun kday-on-or-before (date k)
  ;; TYPE (fixed-date weekday) -> fixed-date
  ;; Fixed date of the $k$-day on or before fixed $date$.
  ;; $k$=0 means Sunday, $k$=1 means Monday, and so on.
  (- date (day-of-week-from-fixed (- date k))))

(defun kday-on-or-after (date k)
  ;; TYPE (fixed-date weekday) -> fixed-date
  ;; Fixed date of the $k$-day on or after fixed $date$.
  ;; $k$=0 means Sunday, $k$=1 means Monday, and so on.
  (kday-on-or-before (+ date 6) k))

(defun kday-nearest (date k)
  ;; TYPE (fixed-date weekday) -> fixed-date
  ;; Fixed date of the $k$-day nearest fixed $date$.  
  ;; $k$=0 means Sunday, $k$=1 means Monday, and so on.
  (kday-on-or-before (+ date 3) k))

(defun kday-after (date k)
  ;; TYPE (fixed-date weekday) -> fixed-date
  ;; Fixed date of the $k$-day after fixed $date$.  
  ;; $k$=0 means Sunday, $k$=1 means Monday, and so on.
  (kday-on-or-before (+ date 7) k))

(defun kday-before (date k)
  ;; TYPE (fixed-date weekday) -> fixed-date
  ;; Fixed date of the $k$-day before fixed $date$.  
  ;; $k$=0 means Sunday, $k$=1 means Monday, and so on.
  (kday-on-or-before (- date 1) k))

(defun nth-kday (n k g-date)
  ;; TYPE (integer weekday gregorian-date) -> fixed-date
  ;; Fixed date of $n$-th $k$-day after Gregorian date.  If
  ;; $n$>0, return the $n$-th $k$-day on or after date.
  ;; If $n$<0, return the $n$-th $k$-day on or before date.
  ;; A $k$-day of 0 means Sunday, 1 means Monday, and so on.
  (if (> n 0)
      (+ (* 7 n)
         (kday-before (fixed-from-gregorian g-date) k))
    (+ (* 7 n)
       (kday-after (fixed-from-gregorian g-date) k))))

(defun first-kday (k g-date)
  ;; TYPE (weekday gregorian-date -> fixed-date
  ;; Fixed date of first $k$-day on or after Gregorian date.
  ;; A $k$-day of 0 means Sunday, 1 means Monday, and so on.
  (nth-kday 1 k g-date))

(defun last-kday (k g-date)
  ;; TYPE (weekday gregorian-date -> fixed-date
  ;; Fixed date of last $k$-day on or before Gregorian date.
  ;; A $k$-day of 0 means Sunday, 1 means Monday, and so on.
  (nth-kday -1 k g-date))

(defun labor-day (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of United States Labor Day in Gregorian
  ;; year--the first Monday in September.
  (first-kday monday (gregorian-date g-year september 1)))

(defun memorial-day (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of United States Memorial Day in Gregorian
  ;; year--the last Monday in May.
  (last-kday monday (gregorian-date g-year may 31)))

(defun election-day (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of United States Election Day in Gregorian
  ;; year--the Tuesday after the first Monday in November.
  (first-kday tuesday (gregorian-date g-year november 2)))

(defun daylight-saving-start (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of the start of United States daylight saving
  ;; time in Gregorian year--the first Sunday in April.
  (first-kday sunday (gregorian-date g-year april 1)))

(defun daylight-saving-end (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of the end of United States daylight saving
  ;; time in Gregorian year--the last Sunday in October.
  (last-kday sunday (gregorian-date g-year october 31)))

(defun christmas (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Christmas in Gregorian year.
  (fixed-from-gregorian (gregorian-date g-year december 25)))

(defun advent (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Advent in Gregorian year
  ;; --the Sunday closest to November 30.
  (kday-nearest (fixed-from-gregorian
                 (gregorian-date g-year november 30))
                sunday))

(defun epiphany (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Epiphany in U.S. in Gregorian year
  ;; --the first Sunday after January 1.
  (first-kday sunday (gregorian-date g-year january 2)))


;;;; Section: ISO Calendar

(defun iso-date (year week day)
  ;; TYPE (iso-year iso-week iso-day) -> iso-date
  (list year week day))

(defun iso-week (date)
  ;; TYPE iso-date -> iso-week
  (second date))

(defun iso-day (date)
  ;; TYPE iso-date -> day-of-week
  (third date))

(defun iso-year (date)
  ;; TYPE iso-date -> iso-year
  (first date))

(defun fixed-from-iso (i-date)
  ;; TYPE iso-date -> fixed-date
  ;; Fixed date equivalent to ISO (year week day).
  (let* ((week (iso-week i-date))
         (day (iso-day i-date))
         (year (iso-year i-date)))
    ;; Add fixed date of Sunday preceding date plus day
    ;; in week.
    (+ (nth-kday
        week sunday
        (gregorian-date (1- year) december 28)) day)))

(defun iso-from-fixed (date)
  ;; TYPE fixed-date -> iso-date
  ;; ISO (year week day) corresponding to the fixed $date$.
  (let* ((approx ; Year may be one too small.
          (gregorian-year-from-fixed (- date 3)))
         (year (if (>= date
                       (fixed-from-iso
                        (iso-date (1+ approx) 1 1)))
                   (1+ approx)
                 approx))
         (week (1+ (quotient
                    (- date
                       (fixed-from-iso (iso-date year 1 1)))
                    7)))
         (day (adjusted-mod date 7)))
    (iso-date year week day)))


;;;; Section: Julian Calendar

(defun julian-date (year month day)
  ;; TYPE (julian-year julian-month julian-day)
  ;; TYPE -> julian-date
  (list year month day))

(defconstant julian-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Julian calendar.
  (fixed-from-gregorian (gregorian-date 0 december 30)))

(defun bce (n)
  ;; TYPE standard-year -> julian-year
  ;; Negative value to indicate a BCE Julian year.
  (- n))

(defun ce (n)
  ;; TYPE standard-year -> julian-year
  ;; Positive value to indicate a CE Julian year.
  n)

(defun julian-leap-year? (j-year)
  ;; TYPE julian-year -> boolean
  ;; True if $j-year$ is a leap year on the Julian calendar.
  (= (mod j-year 4) (if (> j-year 0) 0 3)))

(defun fixed-from-julian (j-date)
  ;; TYPE julian-date -> fixed-date
  ;; Fixed date equivalent to the Julian date.
  (let* ((month (standard-month j-date))
         (day (standard-day j-date))
         (year (standard-year j-date))
         (y (if (< year 0)
                (1+ year) ; No year zero
              year)))
    (+ (1- julian-epoch)  ; Days before start of calendar
       (* 365 (1- y))     ; Ordinary days since epoch.
       (quotient (1- y) 4); Leap days since epoch...
       (quotient          ; Days in prior months this year...
        (- (* 367 month) 362); ...assuming 30-day Feb
        12)
       (if (<= month 2)   ; Correct for 28- or 29-day Feb
           0
         (if (julian-leap-year? year)
             -1
           -2))
       day)))             ; Days so far this month.

(defun julian-from-fixed (date)
  ;; TYPE fixed-date -> julian-date
  ;; Julian (year month day) corresponding to fixed $date$.
  (let* ((approx      ; Nominal year.
          (quotient (+ (* 4 (- date julian-epoch)) 1464)
                    1461))
         (year (if (<= approx 0)
                   (1- approx) ; No year 0.
                 approx))
         (prior-days; This year
          (- date (fixed-from-julian
                   (julian-date year january 1))))
         (correction; To simulate a 30-day Feb
          (if (< date (fixed-from-julian
                       (julian-date year march 1)))
              0
            (if (julian-leap-year? year)
                1
              2)))
         (month     ; Assuming a 30-day Feb
          (quotient
           (+ (* 12 (+ prior-days correction)) 373)
           367))
         (day       ; Calculate the day by subtraction.
          (1+ (- date
                 (fixed-from-julian
                  (julian-date year month 1))))))
    (julian-date year month day)))

(defconstant kalends
  ;; TYPE roman-event
  ;; Class of Kalends.
  1)

(defconstant nones
  ;; TYPE roman-event
  ;; Class of Nones.
  2)

(defconstant ides
  ;; TYPE roman-event
  ;; Class of Ides.
  3)

(defun roman-date (year month event count leap)
  ;; TYPE (roman-year roman-month roman-event roman-count
  ;; TYPE  roman-leap) -> roman-date
  (list year month event count leap))

(defun roman-year (date)
  ;; TYPE roman-date -> roman-year
  (first date))

(defun roman-month (date)
  ;; TYPE roman-date -> roman-month
  (second date))

(defun roman-event (date)
  ;; TYPE roman-date -> roman-event
  (third date))

(defun roman-count (date)
  ;; TYPE roman-date -> roman-count
  (fourth date))

(defun roman-leap (date)
  ;; TYPE roman-date -> roman-leap
  (fifth date))

(defun ides-of-month (month)
  ;; TYPE roman-month -> ides
  ;; Date of Ides in Roman $month$.
  (if (member month (list march may july october))
      15
    13))

(defun nones-of-month (month)
  ;; TYPE roman-month -> nones
  ;; Date of Nones in Roman $month$.
  (- (ides-of-month month) 8))

(defun fixed-from-roman (r-date)
  ;; TYPE roman-date -> fixed-date
  ;; Fixed date for Roman name.
  (let* ((leap (roman-leap r-date))
         (count (roman-count r-date))
         (event (roman-event r-date))
         (month (roman-month r-date))
         (year (roman-year r-date)))
    (+ (cond
        ((= event kalends)
         (fixed-from-julian (julian-date year month 1)))
        ((= event nones)
         (fixed-from-julian
          (julian-date year month (nones-of-month month))))
        ((= event ides)
         (fixed-from-julian
          (julian-date year month (ides-of-month month)))))
       (- count)
       (if (and (julian-leap-year? year)
                (= month march)
                (= event kalends)
                (>= 16 count 6))
           0 ; After Ides until leap day
         1) ; Otherwise
       (if leap 
           1 ; Leap day
         0)))) ; Non-leap day

(defun roman-from-fixed (date)
  ;; TYPE fixed-date -> roman-date
  ;; Roman name for fixed $date$.
  (let* ((j (julian-from-fixed date))
         (month (standard-month j))
         (day (standard-day j))
         (year (standard-year j))
         (month-prime (adjusted-mod (1+ month) 12))
         (year-prime (if (= month-prime 1) (1+ year) year))
         (kalends1 (fixed-from-roman
                    (roman-date year-prime month-prime
                                kalends 1 false))))
    (cond
     ((= day 1) (roman-date year month kalends 1 false))
     ((<= day (nones-of-month month))
      (roman-date year month nones
                  (1+ (- (nones-of-month month) day)) false))
     ((<= day (ides-of-month month))
      (roman-date year month ides
                  (1+ (- (ides-of-month month) day)) false))
     ((or (/= month february)
          (not (julian-leap-year? year)))
      ; After the Ides, in a month that is not February of a
      ; leap year
      (roman-date year-prime month-prime kalends
                  (1+ (- kalends1 date)) false))
     ((< day 25)
      ; February of a leap year, before leap day
      (roman-date year march kalends (- 30 day) false))
     (true
      ; February of a leap year, on or after leap day
      (roman-date year march kalends
                  (- 31 day) (= day 25))))))

(defun julian-in-gregorian (j-month j-day g-year)
  ;; TYPE (julian-month julian-day gregorian-year)
  ;; TYPE -> list-of-fixed-dates
  ;; List of the fixed dates of Julian month, day
  ;; that occur in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (y (standard-year (julian-from-fixed jan1)))
         (y-prime (if (= y -1)
                     1
                   (1+ y)))
         ;; The possible occurrences in one year are
         (date1 (fixed-from-julian
                 (julian-date y j-month j-day)))
         (date2 (fixed-from-julian
                 (julian-date y-prime j-month j-day))))
    (append
     (if ; date1 occurs in current year
         (<= jan1 date1 dec31)
         ;; Then that date; otherwise, none
         (list date1) nil)
     (if ; date2 occurs in current year
         (<= jan1 date2 dec31)
         ;; Then that date; otherwise, none
         (list date2) nil))))

(defun eastern-orthodox-christmas (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of zero or one fixed dates of Eastern Orthodox
  ;; Christmas in Gregorian year.
  (julian-in-gregorian december 25 g-year))


;;;; Section: Coptic and Ethiopic Calendars

(defun coptic-date (year month day)
  ;; TYPE (coptic-year coptic-month coptic-day) -> coptic-date
  (list year month day))

(defconstant coptic-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Coptic calendar.
  (fixed-from-julian (julian-date (ce 284) august 29)))

(defun coptic-leap-year? (c-year)
  ;; TYPE coptic-year -> boolean
  ;; True if $c-year$ is a leap year on the Coptic calendar.
  (= (mod c-year 4) 3))

(defun fixed-from-coptic (c-date)
  ;; TYPE coptic-date -> fixed-date
  ;; Fixed date of Coptic date.
  (let* ((month (standard-month c-date))
         (day (standard-day c-date))
         (year (standard-year c-date)))
    (+ coptic-epoch -1  ; Days before start of calendar
       (* 365 (1- year)); Ordinary days in prior years
       (quotient year 4); Leap days in prior years
       (* 30 (1- month)); Days in prior months this year
       day)))           ; Days so far this month

(defun coptic-from-fixed (date)
  ;; TYPE fixed-date -> coptic-date
  ;; Coptic equivalent of fixed $date$.
  (let* ((year ; Calculate the year by cycle-of-years formula
          (quotient (+ (* 4 (- date coptic-epoch)) 1463)
                    1461))
         (month; Calculate the month by division.
          (1+ (quotient
               (- date (fixed-from-coptic
                        (coptic-date year 1 1)))
               30)))
         (day  ; Calculate the day by subtraction.
          (- date -1
             (fixed-from-coptic
              (coptic-date year month 1)))))
    (coptic-date year month day)))

(defun ethiopic-date (year month day)
  ;; TYPE (ethiopic-year ethiopic-month ethiopic-day)
  ;; TYPE -> ethiopic-date
  (list year month day))

(defconstant ethiopic-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Ethiopic calendar.
  (fixed-from-julian (julian-date (ce 8) august 29)))

(defun fixed-from-ethiopic (e-date)
  ;; TYPE ethiopic-date -> fixed-date
  ;; Fixed date of Ethiopic date.
  (let* ((month (standard-month e-date))
         (day (standard-day e-date))
         (year (standard-year e-date)))
    (+ ethiopic-epoch
       (- (fixed-from-coptic
           (coptic-date year month day))
          coptic-epoch))))

(defun ethiopic-from-fixed (date)
  ;; TYPE fixed-date -> ethiopic-date
  ;; Ethiopic equivalent of fixed $date$.
  (coptic-from-fixed
   (+ date (- coptic-epoch ethiopic-epoch))))

(defun coptic-in-gregorian (c-month c-day g-year)
  ;; TYPE (coptic-month coptic-day gregorian-year)
  ;; TYPE -> list-of-fixed-dates
  ;; List of the fixed dates of Coptic month, day
  ;; that occur in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (y (standard-year (coptic-from-fixed jan1)))
         ;; The possible occurrences in one year are
         (date1 (fixed-from-coptic
                 (coptic-date y c-month c-day)))
         (date2 (fixed-from-coptic
                 (coptic-date (1+ y) c-month c-day))))
    (append
     (if ; date1 occurs in current year
         (<= jan1 date1 dec31)
         ;; Then that date; otherwise, none
         (list date1) nil)
     (if ; date2 occurs in current year
         (<= jan1 date2 dec31)
         ;; Then that date; otherwise, none
         (list date2) nil))))

(defun coptic-christmas (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of zero or one fixed dates of Coptic Christmas
  ;; in Gregorian year.
  (coptic-in-gregorian 4 29 g-year))


;;;; Section: Ecclesiastical Calendars

(defun orthodox-easter (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Orthodox Easter in Gregorian year.
  (let* ((shifted-epact ; Age of moon for April 5.
          (mod (+ 14 (* 11 (mod g-year 19)))
               30))
         (j-year (if (> g-year 0); Julian year number.
                     g-year
                   (1- g-year)))
         (paschal-moon  ; Day after full moon on
                        ; or after March 21.
          (- (fixed-from-julian (julian-date j-year april 19))
             shifted-epact)))
    ;; Return the Sunday following the Paschal moon.
    (kday-after paschal-moon sunday)))

(defun alt-orthodox-easter (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Alternate calculation of fixed date of Orthodox Easter 
  ;; in Gregorian year.
  (let* ((paschal-moon  ; Day after full moon on
                        ; or after March 21.
          (+ (* 354 g-year)
             (* 30 (quotient (+ (* 7 g-year) 8) 19))
             (quotient g-year 4)
             (- (quotient g-year 19))
             -272)))
    ;; Return the Sunday following the Paschal moon.
    (kday-after paschal-moon sunday)))

(defun easter (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Easter in Gregorian year.
  (let* ((century (1+ (quotient g-year 100)))
         (shifted-epact        ; Age of moon for April 5...
          (mod
           (+ 14 (* 11 (mod g-year 19));   ...by Nicaean rule
              (- ;...corrected for the Gregorian century rule
               (quotient (* 3 century) 4))
              (quotient; ...corrected for Metonic
                       ; cycle inaccuracy.
               (+ 5 (* 8 century)) 25))
           30))
         (adjusted-epact       ;  Adjust for 29.5 day month.
          (if (or (= shifted-epact 0)
                  (and (= shifted-epact 1)
                       (< 10 (mod g-year 19))))
              (1+ shifted-epact)
            shifted-epact))
         (paschal-moon; Day after full moon on
                      ; or after March 21.
          (- (fixed-from-gregorian
              (gregorian-date g-year april 19))
             adjusted-epact)))
    ;; Return the Sunday following the Paschal moon.
    (kday-after paschal-moon sunday)))

(defun pentecost (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Pentecost in Gregorian year.
  (+ (easter g-year) 49))


;;;; Section: Islamic Calendar

(defun islamic-date (year month day)
  ;; TYPE (islamic-year islamic-month islamic-day)
  ;; TYPE -> islamic-date
  (list year month day))

(defconstant islamic-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Islamic calendar.
  (fixed-from-julian (julian-date (ce 622) july 16)))

(defun islamic-leap-year? (i-year)
  ;; TYPE islamic-year -> boolean
  ;; True if $i-year$ is an Islamic leap year.
  (< (mod (+ 14 (* 11 i-year)) 30) 11))

(defun fixed-from-islamic (i-date)
  ;; TYPE islamic-date -> fixed-date
  ;; Fixed date equivalent to Islamic date.
  (let* ((month (standard-month i-date))
         (day (standard-day i-date))
         (year (standard-year i-date)))
    (+ day                    ; Days so far this month.
       (* 29 (1- month))      ; Days in prior months.
       (quotient (1- (* 6 month)) 11)
       (* (1- year) 354)      ; Nonleap days in prior years.
       (quotient              ; Leap days in prior years.
        (+ 3 (* 11 year)) 30)
       islamic-epoch          ; Days before start of calendar.
       -1)))

(defun islamic-from-fixed (date)
  ;; TYPE fixed-date -> islamic-date
  ;; Islamic date (year month day) corresponding to fixed
  ;; $date$.
  (let* ((year
          (quotient
           (+ (* 30 (- date islamic-epoch)) 10646)
           10631))
         (prior-days
          (- date (fixed-from-islamic
                   (islamic-date year 1 1))))
         (month
           (quotient
            (+ (* 11 prior-days) 330)
            325))
         (day
          (1+ (- date (fixed-from-islamic
                       (islamic-date year month 1))))))
      (islamic-date year month day)))

(defun islamic-in-gregorian (i-month i-day g-year)
  ;; TYPE (islamic-month islamic-day gregorian-year)
  ;; TYPE -> list-of-fixed-dates
  ;; List of the fixed dates of Islamic month, day
  ;; that occur in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (y (standard-year (islamic-from-fixed jan1)))
         ;; The possible occurrences in one year are
         (date1 (fixed-from-islamic
                 (islamic-date y i-month i-day)))
         (date2 (fixed-from-islamic
                 (islamic-date (1+ y) i-month i-day)))
         (date3 (fixed-from-islamic
                 (islamic-date (+ y 2) i-month i-day))))
    ;; Combine in one list those that occur in current year
    (append
     (if (<= jan1 date1 dec31)
         (list date1) nil)
     (if (<= jan1 date2 dec31)
         (list date2) nil)
     (if (<= jan1 date3 dec31)
         (list date3) nil))))

(defun mawlid-an-nabi (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of fixed dates of Mawlid-an-Nabi occurring in
  ;; Gregorian year.
  (islamic-in-gregorian 3 12 g-year))


;;;; Section: Bahai Calendar

(defun bahai-date (major cycle year month day)
  ;; TYPE (bahai-major bahai-cycle bahai-year
  ;; TYPE  bahai-month bahai-day) -> bahai-date
  (list major cycle year month day))

(defun bahai-major (date)
  ;; TYPE bahai-date -> bahai-major
  (first date))

(defun bahai-cycle (date)
  ;; TYPE bahai-date -> bahai-cycle
  (second date))

(defun bahai-year (date)
  ;; TYPE bahai-date -> bahai-year
  (third date))

(defun bahai-month (date)
  ;; TYPE bahai-date -> bahai-month
  (fourth date))

(defun bahai-day (date)
  ;; TYPE bahai-date -> bahai-day
  (fifth date))

(defconstant bahai-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of Bahai calendar.
  (fixed-from-gregorian (gregorian-date 1844 march 21)))

(defconstant ayyam-i-ha
  ;; TYPE bahai-month
  ;; Signifies intercalary period of 4 or 5 days.
  0)

(defun fixed-from-bahai (b-date)
  ;; TYPE bahai-date -> fixed-date
  ;; Fixed date equivalent to the Bahai date.
  (let* ((major (bahai-major b-date))
         (cycle (bahai-cycle b-date))
         (year (bahai-year b-date))
         (month (bahai-month b-date))
         (day (bahai-day b-date))
         (g-year; Corresponding Gregorian year.
          (+ (* 361 (1- major))
             (* 19 (1- cycle)) year -1
             (gregorian-year-from-fixed bahai-epoch))))
    (+ (fixed-from-gregorian ; Prior years.
        (gregorian-date g-year march 20))
       (cond ((= month ayyam-i-ha) ; Intercalary period.
              342) ; 18 months have elapsed.
             ((= month 19); Last month of year.
              (if (gregorian-leap-year? (1+ g-year))
                  347  ; Long ayyam-i-ha.
                346)); Ordinary ayyam-i-ha.
             (t (* 19 (1- month)))); Elapsed months.
       day))) ; Days of current month.

(defun bahai-from-fixed (date)
  ;; TYPE fixed-date -> bahai-date
  ;; Bahai (major cycle year month day) corresponding to fixed
  ;; $date$.
  (let* ((g-year (gregorian-year-from-fixed date))
         (start   ; 1844
          (gregorian-year-from-fixed bahai-epoch))
         (years ; Since start of Bahai calendar.
          (- g-year start
             (if (<= date
                     (fixed-from-gregorian
                      (gregorian-date g-year march 20)))
                 1 0)))
         (major (1+ (quotient years 361)))
         (cycle (1+ (quotient (mod years 361) 19)))
         (year (1+ (mod years 19)))
         (days; Since start of year
          (- date (fixed-from-bahai
                   (bahai-date major cycle year 1 1))))
         (month
          (cond ((>= date
                     (fixed-from-bahai
                      (bahai-date major cycle year 19 1)))
                 19) ; Last month of year.
                ((>= date ; Intercalary days.
                     (fixed-from-bahai
                      (bahai-date major cycle year
                                  ayyam-i-ha 1)))
                 ayyam-i-ha) ; Intercalary period.
                (t (1+ (quotient days 19)))))
         (day (- date -1
                 (fixed-from-bahai
                  (bahai-date major cycle year month 1)))))
    (bahai-date major cycle year month day)))

(defun bahai-new-year (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Bahai New Year in Gregorian year.
  (fixed-from-gregorian
   (gregorian-date g-year march 21)))

(defun feast-of-ridvan (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Feast of Ridvan in Gregorian year.
  (let* ((years (- g-year
                   (gregorian-year-from-fixed
                    bahai-epoch)))
         (major (1+ (quotient years 361)))
         (cycle (1+ (quotient (mod years 361) 19)))
         (year (1+ (mod years 19))))
    (fixed-from-future-bahai
     (bahai-date major cycle year 2 13))))


;;;; Section: Hebrew Calendar

(defun hebrew-date (year month day)
  ;; TYPE (hebrew-year hebrew-month hebrew-day) -> hebrew-date
  (list year month day))

(defconstant tishri
  ;; TYPE hebrew-month
  ;; Tishri is month number 7.
  7)

(defconstant nisan
  ;; TYPE hebrew-month
  ;; Nisan is month number 1.
  1)

(defconstant hebrew-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Hebrew calendar, that is,
  ;; Tishri 1, 1 AM.
  (fixed-from-julian (julian-date (bce 3761) october 7)))

(defun hebrew-leap-year? (h-year)
  ;; TYPE hebrew-year -> boolean
  ;; True if $h-year$ is a leap year on Hebrew calendar.
  (< (mod (1+ (* 7 h-year)) 19) 7))

(defun last-month-of-hebrew-year (h-year)
  ;; TYPE hebrew-year -> hebrew-month
  ;; Last month of Hebrew year.
  (if (hebrew-leap-year? h-year)
      13
    12))

(defun last-day-of-hebrew-month (h-month h-year)
  ;; TYPE (hebrew-month hebrew-year) -> hebrew-day
  ;; Last day of month in Hebrew year.
  (if (or (member h-month (list 2 4 6 10 13))
          (and (= h-month 12)
               (not (hebrew-leap-year? h-year)))
          (and (= h-month 8) (not (long-marheshvan? h-year)))
          (and (= h-month 9) (short-kislev? h-year)))
      29
    30))

(defun molad (h-month h-year)
  ;; TYPE (hebrew-month hebrew-year) -> rational-moment
  ;; Moment of mean conjunction of $h-month$ in Hebrew
  ;; $h-year$.
  (let* ((y ;; Treat Nisan as start of year.
          (if (< h-month tishri)
              (1+ h-year)
            h-year))
         (months-elapsed
          (+ (- h-month tishri)  ;; Months this year.
             (quotient ;; Months until New Year.
               (- (* 235 y) 234) 
               19))))
    (+ hebrew-epoch
       -876/25920
       (* months-elapsed (+ 29 (hr 12) 793/25920)))))

(defun hebrew-calendar-elapsed-days (h-year)
  ;; TYPE hebrew-year -> integer
  ;; Number of days elapsed from the (Sunday) noon prior
  ;; to the epoch of the Hebrew calendar to the mean
  ;; conjunction (molad) of Tishri of Hebrew year $h-year$,
  ;; or one day later.
  (let* ((months-elapsed  ; Since start of Hebrew calendar.
          (quotient (- (* 235 h-year) 234) 19))
         (parts-elapsed; Fractions of days since prior noon.
          (+ 12084 (* 13753 months-elapsed)))
         (day  ; Whole days since prior noon.
          (+ (* 29 months-elapsed)
             (quotient parts-elapsed 25920)))
      ;; If (* 13753 months-elapsed) causes integers that
      ;; are too large, use instead:
      ;; (parts-elapsed
      ;;  (+ 204 (* 793 (mod months-elapsed 1080))))
      ;; (hours-elapsed
      ;;  (+ 11 (* 12 months-elapsed)
      ;;     (* 793 (quotient months-elapsed 1080))
      ;;     (quotient parts-elapsed 1080)))
      ;; (day
      ;;  (+ (* 29 months-elapsed)
      ;;     (quotient hours-elapsed 24)))
         )
    (if (< (mod (* 3 (1+ day)) 7) 3); Sun, Wed, or Fri
        (+ day 1) ; Delay one day.
      day)))

(defun hebrew-new-year (h-year)
  ;; TYPE hebrew-year -> fixed-date
  ;; Fixed date of Hebrew new year $h-year$.
  (+ hebrew-epoch
     (hebrew-calendar-elapsed-days h-year)
     (hebrew-new-year-delay h-year)))

(defun hebrew-new-year-delay (h-year)
  ;; TYPE hebrew-year -> {0,1,2}
  ;; Delays to start of Hebrew year to keep ordinary year in
  ;; range 353-356 and leap year in range 383-386.
  (let* ((ny0 (hebrew-calendar-elapsed-days (1- h-year)))
         (ny1 (hebrew-calendar-elapsed-days h-year))
         (ny2 (hebrew-calendar-elapsed-days (1+ h-year))))
    (cond
     ((= (- ny2 ny1) 356) ; Next year would be too long.
      2)
     ((= (- ny1 ny0) 382) ; Previous year too short.
      1)
     (t 0))))

(defun days-in-hebrew-year (h-year)
  ;; TYPE hebrew-year -> {353,354,355,383,384,385}
  ;; Number of days in Hebrew year.
  (- (hebrew-new-year (1+ h-year))
     (hebrew-new-year h-year)))

(defun long-marheshvan? (h-year)
  ;; TYPE hebrew-year -> boolean
  ;; True if Marheshvan is long in Hebrew year.
  (member (days-in-hebrew-year h-year) (list 355 385)))

(defun short-kislev? (h-year)
  ;; TYPE hebrew-year -> boolean
  ;; True if Kislev is short in Hebrew year.
  (member (days-in-hebrew-year h-year) (list 353 383)))

(defun fixed-from-hebrew (h-date)
  ;; TYPE hebrew-date -> fixed-date
  ;; Fixed date of Hebrew date.
  (let* ((month (standard-month h-date))
         (day (standard-day h-date))
         (year (standard-year h-date)))
    (+ (hebrew-new-year year)
       day -1               ; Days so far this month.
       (if ;; before Tishri
           (< month tishri)
           ;; Then add days in prior months this year before
           ;; and after Nisan.
           (+ (sum (last-day-of-hebrew-month m year)
                   m tishri
                   (<= m (last-month-of-hebrew-year year)))
              (sum (last-day-of-hebrew-month m year)
                   m nisan (< m month)))
         ;; Else add days in prior months this year
         (sum (last-day-of-hebrew-month m year)
              m tishri (< m month))))))

(defun hebrew-from-fixed (date)
  ;; TYPE fixed-date -> hebrew-date
  ;; Hebrew (year month day) corresponding to fixed $date$.
  ;; The fraction can be approximated by 365.25.
  (let* ((approx    ; Approximate year
          (1+
           (quotient (- date hebrew-epoch) 35975351/98496)))
         ;; The value 35975351/98496, the average length of
         ;; a Hebrew year, can be approximated by 365.25
         (year      ; Search forward.
          (final y (1- approx)
                 (<= (hebrew-new-year y) date)))
         (start     ; Starting month for search for month.
          (if (< date (fixed-from-hebrew
                       (hebrew-date year nisan 1)))
              tishri
            nisan))
         (month ; Search forward from either Tishri or Nisan.
          (next m start
                (<= date
                    (fixed-from-hebrew
                     (hebrew-date
                      year
                      m
                      (last-day-of-hebrew-month m year))))))
         (day   ; Calculate the day by subtraction.
          (1+ (- date (fixed-from-hebrew
                       (hebrew-date year month 1))))))
    (hebrew-date year month day)))

(defun yom-kippur (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Yom Kippur occurring in Gregorian year.
  (let* ((hebrew-year
          (1+ (- g-year
                 (gregorian-year-from-fixed
                  hebrew-epoch)))))
    (fixed-from-hebrew (hebrew-date hebrew-year tishri 10))))

(defun passover (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Passover occurring in Gregorian year.
  (let* ((hebrew-year
          (- g-year
             (gregorian-year-from-fixed hebrew-epoch))))
    (fixed-from-hebrew (hebrew-date hebrew-year nisan 15))))

(defun omer (date)
  ;; TYPE fixed-date -> omer-count
  ;; Number of elapsed weeks and days in the omer at $date$.
  ;; Returns bogus if that date does not fall during the
  ;; omer.
  (let* ((c (- date
               (passover
                (gregorian-year-from-fixed date)))))
    (if (<= 1 c 49)
        (list (quotient c 7) (mod c 7))
      bogus)))

(defun purim (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Purim occurring in Gregorian year.
  (let* ((hebrew-year
          (- g-year
             (gregorian-year-from-fixed hebrew-epoch)))
         (last-month  ; Adar or Adar II
          (last-month-of-hebrew-year hebrew-year)))
    (fixed-from-hebrew
     (hebrew-date hebrew-year last-month 14))))

(defun ta-anit-esther (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Ta'anit Esther occurring in
  ;; Gregorian year.
  (let* ((purim-date (purim g-year)))
    (if ; Purim is on Sunday
        (= (day-of-week-from-fixed purim-date) sunday)
        ;; Then prior Thursday
        (- purim-date 3)
      ;; Else previous day
      (1- purim-date))))

(defun tishah-be-av (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Tishah be-Av occurring in Gregorian year.
  (let* ((hebrew-year
          (- g-year
             (gregorian-year-from-fixed hebrew-epoch)))
         (av9
          (fixed-from-hebrew
           (hebrew-date hebrew-year 5 9))))
    (if ; Ninth of Av is Saturday
        (= (day-of-week-from-fixed av9) saturday)
        ;; Then the next day
        (1+ av9)
      av9)))

(defun birkath-ha-hama (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of fixed date of Birkath ha-Hama occurring in
  ;; Gregorian year, if it occurs.
  (let* ((dates (coptic-in-gregorian 7 30 g-year)))
    (if (and (not (equal dates nil))
             (= (mod (standard-year
                      (coptic-from-fixed (first dates)))
                     28)
                17))
        dates
      nil)))

(defun sh-ela (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of fixed dates of Sh'ela occurring in
  ;; Gregorian year.
  (coptic-in-gregorian 3 26 g-year))

(defun yom-ha-zikkaron (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Yom ha-Zikkaron occurring in Gregorian
  ;; year.
  (let* ((hebrew-year
          (- g-year
             (gregorian-year-from-fixed hebrew-epoch)))
         (iyyar4; Ordinarily Iyyar 4
          (fixed-from-hebrew
           (hebrew-date hebrew-year 2 4))))
    (if (< wednesday (day-of-week-from-fixed iyyar4))
        ;; But prior Wednesday if Iyyar 5 is Friday or
        ;; Saturday
        (kday-before iyyar4 wednesday)
      iyyar4)))

(defun hebrew-birthday (birthdate h-year)
  ;; TYPE (hebrew-date hebrew-year) -> fixed-date
  ;; Fixed date of the anniversary of Hebrew $birthdate$
  ;; occurring in Hebrew $h-year$.
  (let* ((birth-day (standard-day birthdate))
         (birth-month (standard-month birthdate))
         (birth-year (standard-year birthdate)))
    (if ; It's Adar in a normal Hebrew year or Adar II
        ; in a Hebrew leap year,
        (= birth-month (last-month-of-hebrew-year birth-year))
        ;; Then use the same day in last month of Hebrew year.
      (fixed-from-hebrew
       (hebrew-date h-year (last-month-of-hebrew-year h-year)
                    birth-day))
      ;; Else use the normal anniversary of the birth date,
      ;; or the corresponding day in years without that date
      (+ (fixed-from-hebrew
          (hebrew-date h-year birth-month 1))
         birth-day -1))))

(defun hebrew-birthday-in-gregorian (birthdate g-year)
  ;; TYPE (hebrew-date gregorian-year)
  ;; TYPE -> list-of-fixed-dates
  ;; List of the fixed dates of Hebrew $birthday$
  ;; that occur in Gregorian $g-year$.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (y (standard-year (hebrew-from-fixed jan1)))
         ;; The possible occurrences in one year are
         (date1 (hebrew-birthday birthdate y))
         (date2 (hebrew-birthday birthdate (1+ y))))
    ;; Combine in one list those that occur in current year.
    (append
     (if (<= jan1 date1)
       (list date1) nil)
     (if (<= date2 dec31)
       (list date2) nil))))

(defun yahrzeit (death-date h-year)
  ;; TYPE (hebrew-date hebrew-year) -> fixed-date
  ;; Fixed date of the anniversary of Hebrew $death-date$
  ;; occurring in Hebrew $h-year$.
  (let* ((death-day (standard-day death-date))
         (death-month (standard-month death-date))
         (death-year (standard-year death-date)))
    (cond
     ;; If it's Marheshvan 30 it depends on the first
     ;; anniversary; if that was not Marheshvan 30, use
     ;; the day before Kislev 1.
     ((and (= death-month 8)
           (= death-day 30)
           (not (long-marheshvan? (1+ death-year))))
      (1- (fixed-from-hebrew
           (hebrew-date h-year 9 1))))
     ;; If it's Kislev 30 it depends on the first
     ;; anniversary; if that was not Kislev 30, use
     ;; the day before Tevet 1.
     ((and (= death-month 9)
           (= death-day 30)
           (short-kislev? (1+ death-year)))
      (1- (fixed-from-hebrew
           (hebrew-date h-year 10 1))))
     ;; If it's Adar II, use the same day in last
     ;; month of Hebrew year (Adar or Adar II).
     ((= death-month 13)
      (fixed-from-hebrew
       (hebrew-date
             h-year (last-month-of-hebrew-year h-year)
             death-day)))
     ;; If it's the 30th in Adar I and Hebrew year is not a
     ;; Hebrew leap year (so Adar has only 29 days), use the
     ;; last day in Shevat.
     ((and (= death-day 30)
           (= death-month 12)
           (not (hebrew-leap-year? h-year)))
      (fixed-from-hebrew (hebrew-date h-year 11 30)))
     ;; In all other cases, use the normal anniversary of
     ;; the date of death.
     (t (+ (fixed-from-hebrew
            (hebrew-date h-year death-month 1))
           death-day -1)))))

(defun yahrzeit-in-gregorian (death-date g-year)
  ;; TYPE (hebrew-date gregorian-year)
  ;; TYPE -> list-of-fixed-dates
  ;; List of the fixed dates of yahrzeit
  ;; that occur in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (y (standard-year (hebrew-from-fixed jan1)))
         ;; The possible occurrences in one year are
         (date1 (yahrzeit death-date y))
         (date2 (yahrzeit death-date (1+ y))))
    ;; Combine in one list those that occur in current year
    (append
     (if (<= jan1 date1)
       (list date1) nil)
     (if (<= date2 dec31)
       (list date2) nil))))


;;;; Section: Mayan Calendars

(defun mayan-long-count-date (baktun katun tun uinal kin)
  ;; TYPE (mayan-baktun mayan-katun mayan-tun mayan-uinal
  ;; TYPE  mayan-kin) -> mayan-long-count-date
  (list baktun katun tun uinal kin))

(defun mayan-haab-date (month day)
  ;; TYPE (mayan-haab-month mayan-haab-day) -> mayan-haab-date
  (list month day))

(defun mayan-tzolkin-date (number name)
  ;; TYPE (mayan-tzolkin-number mayan-tzolkin-name)
  ;; TYPE -> mayan-tzolkin-date
  (list number name))

(defun mayan-baktun (date)
  ;; TYPE mayan-long-count-date -> mayan-baktun
  (first date))

(defun mayan-katun (date)
  ;; TYPE mayan-long-count-date -> mayan-katun
  (second date))

(defun mayan-tun (date)
  ;; TYPE mayan-long-count-date -> mayan-tun
  (third date))

(defun mayan-uinal (date)
  ;; TYPE mayan-long-count-date -> mayan-uinal
  (fourth date))

(defun mayan-kin (date)
  ;; TYPE mayan-long-count-date -> mayan-kin
  (fifth date))

(defun mayan-haab-month (date)
  ;; TYPE mayan-haab-date -> mayan-haab-month
  (first date))

(defun mayan-haab-day (date)
  ;; TYPE mayan-haab-date -> mayan-haab-day
  (second date))

(defun mayan-tzolkin-number (date)
  ;; TYPE mayan-tzolkin-date -> mayan-tzolkin-number
  (first date))

(defun mayan-tzolkin-name (date)
  ;; TYPE mayan-tzolkin-date -> mayan-tzolkin-name
  (second date))

(defconstant mayan-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Mayan calendar, according
  ;; to the Goodman-Martinez-Thompson correlation.
  ;; That is, August 11, -3113.
  (fixed-from-jd 584283))

(defun fixed-from-mayan-long-count (count)
  ;; TYPE mayan-long-count-date -> fixed-date
  ;; Fixed date corresponding to the Mayan long $count$,
  ;; which is a list (baktun katun tun uinal kin).
  (let* ((baktun (mayan-baktun count))
         (katun (mayan-katun count))
         (tun (mayan-tun count))
         (uinal (mayan-uinal count))
         (kin (mayan-kin count)))
    (+ mayan-epoch      ; Fixed date at Mayan 0.0.0.0.0
       (* baktun 144000); Baktun.
       (* katun 7200)   ; Katun.
       (* tun 360)      ; Tun.
       (* uinal 20)     ; Uinal.
       kin)))           ; Kin (days).

(defun mayan-long-count-from-fixed (date)
  ;; TYPE fixed-date -> mayan-long-count-date
  ;; Mayan long count date of fixed $date$.
  (let* ((long-count (- date mayan-epoch))
         (baktun (quotient long-count 144000))
         (day-of-baktun (mod long-count 144000))
         (katun (quotient day-of-baktun 7200))
         (day-of-katun (mod day-of-baktun 7200))
         (tun (quotient day-of-katun 360))
         (day-of-tun (mod day-of-katun 360))
         (uinal (quotient day-of-tun 20))
         (kin (mod day-of-tun 20)))
    (mayan-long-count-date baktun katun tun uinal kin)))

(defun mayan-haab-ordinal (h-date)
  ;; TYPE mayan-haab-date -> nonnegative-integer
  ;; Number of days into cycle of Mayan haab date.
  (let* ((day (mayan-haab-day h-date))
         (month (mayan-haab-month h-date)))
    (+ (* (1- month) 20) day)))

(defconstant mayan-haab-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of haab cycle.
  (- mayan-epoch
     (mayan-haab-ordinal (mayan-haab-date 18 8))))

(defun mayan-haab-from-fixed (date)
  ;; TYPE fixed-date -> mayan-haab-date
  ;; Mayan haab date of fixed $date$.
  (let* ((count
          (mod (- date mayan-haab-epoch) 365))
         (day (mod count 20))
         (month (1+ (quotient count 20))))
    (mayan-haab-date month day)))

(defun mayan-haab-on-or-before (haab date)
  ;; TYPE (mayan-haab-date fixed-date) -> fixed-date
  ;; Fixed date of latest date on or before fixed $date$
  ;; that is Mayan haab date $haab$.
  (- date
     (mod (- date mayan-haab-epoch
             (mayan-haab-ordinal haab))
          365)))

(defun mayan-tzolkin-ordinal (t-date)
  ;; TYPE mayan-tzolkin-date -> nonnegative-integer
  ;; Number of days into Mayan tzolkin cycle of $t-date$.
  (let* ((number (mayan-tzolkin-number t-date))
         (name (mayan-tzolkin-name t-date)))
    (mod (+ number -1
            (* 39 (- number name)))
         260)))

(defconstant mayan-tzolkin-epoch
  ;; TYPE fixed-date
  ;; Start of tzolkin date cycle.
  (- mayan-epoch
     (mayan-tzolkin-ordinal (mayan-tzolkin-date 4 20))))

(defun mayan-tzolkin-from-fixed (date)
  ;; TYPE fixed-date -> mayan-tzolkin-date
  ;; Mayan tzolkin date of fixed $date$.
  (let* ((count (- date mayan-tzolkin-epoch -1))
         (number (adjusted-mod count 13))
         (name (adjusted-mod count 20)))
    (mayan-tzolkin-date number name)))

(defun mayan-tzolkin-on-or-before (tzolkin date)
  ;; TYPE (mayan-tzolkin-date fixed-date) -> fixed-date
  ;; Fixed date of latest date on or before fixed $date$
  ;; that is Mayan tzolkin date $tzolkin$.
  (- date 
     (mod (- date 
             mayan-tzolkin-epoch
             (mayan-tzolkin-ordinal tzolkin))
          260)))

(defun mayan-calendar-round-on-or-before (haab tzolkin date)
  ;; TYPE (mayan-haab-date mayan-tzolkin-date fixed-date)
  ;; TYPE -> fixed-date
  ;; Fixed date of latest date on or before $date$, that is
  ;; Mayan haab date $haab$ and tzolkin date $tzolkin$.
  (let* ((haab-count
          (+ (mayan-haab-ordinal haab) mayan-haab-epoch))
         (tzolkin-count
          (+ (mayan-tzolkin-ordinal tzolkin) 
             mayan-tzolkin-epoch))
         (diff (- tzolkin-count haab-count)))
    (if (= (mod diff 5) 0)
        (- date
           (mod (- date haab-count (* 365 diff))
                18980))
      bogus)));  haab-tzolkin combination is impossible.


;;;; Section: Old Hindu Calendars

(defun old-hindu-lunar-date (year month leap day)
  ;; TYPE (old-hindu-lunar-year old-hindu-lunar-month
  ;; TYPE  old-hindu-lunar-leap old-hindu-lunar-day)
  ;; TYPE -> old-hindu-lunar-date
  (list year month leap day))

(defun old-hindu-lunar-month (date)
  ;; TYPE old-hindu-lunar-date -> old-hindu-lunar-month
  (second date))

(defun old-hindu-lunar-leap (date)
  ;; TYPE old-hindu-lunar-date -> old-hindu-lunar-leap
  (third date))

(defun old-hindu-lunar-day (date)
  ;; TYPE old-hindu-lunar-date -> old-hindu-lunar-day
  (fourth date))

(defun old-hindu-lunar-year (date)
  ;; TYPE old-hindu-lunar-date -> old-hindu-lunar-year
  (first date))

(defun hindu-solar-date (year month day)
  ;; TYPE (hindu-solar-year hindu-solar-month hindu-solar-day)
  ;; TYPE -> hindu-solar-date
  (list year month day))

(defconstant hindu-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Hindu calendar (Kali Yuga).
  (fixed-from-julian (julian-date (bce 3102) february 18)))

(defun hindu-day-count (date)
  ;; TYPE fixed-date -> rational-moment
  ;; Elapsed days (Ahargana) to $date$ since Hindu epoch (KY).
  (- date hindu-epoch))

(defconstant arya-solar-year
  ;; TYPE rational
  ;; Length of Old Hindu solar year.
  1577917500/4320000)

(defconstant arya-solar-month
  ;; TYPE rational
  ;; Length of Old Hindu solar month.
  (/ arya-solar-year 12))

(defun old-hindu-solar-from-fixed (date)
  ;; TYPE fixed-date -> hindu-solar-date
  ;; Old Hindu solar date equivalent to fixed $date$.
  (let* ((sun ; Sunrise on Hindu date.
          (+ (hindu-day-count date) 1/4))
         (year    ; Elapsed years.
          (quotient sun arya-solar-year))
         (month (1+ (mod (quotient sun arya-solar-month)
                         12)))
         (day (1+ (floor (mod sun arya-solar-month)))))
    (hindu-solar-date year month day)))

(defun fixed-from-old-hindu-solar (s-date)
  ;; TYPE hindu-solar-date -> fixed-date
  ;; Fixed date corresponding to Old Hindu solar date.
  (let* ((month (standard-month s-date))
         (day (standard-day s-date))
         (year (standard-year s-date)))
    (floor
     (+ hindu-epoch ; Since start of era.
        (* year arya-solar-year) ; Days in elapsed years
        (* (1- month) arya-solar-month) ; ...in months.
        day -1/4)))) ; Midnight of day.

(defconstant arya-lunar-month
  ;; TYPE rational
  ;; Length of Old Hindu lunar month.
  1577917500/53433336)

(defconstant arya-lunar-day
  ;; TYPE rational
  ;; Length of Old Hindu lunar day.
  (/ arya-lunar-month 30))

(defun old-hindu-lunar-leap-year? (l-year)
  ;; TYPE old-hindu-lunar-year -> boolean
  ;; True if $l-year$ is a leap year on the
  ;; old Hindu calendar.
  (>= (mod (- (* l-year arya-solar-year)
                 arya-solar-month)
           arya-lunar-month)
      23902504679/1282400064))

(defun old-hindu-lunar-from-fixed (date)
  ;; TYPE fixed-date -> old-hindu-lunar-date
  ;; Old Hindu lunar date equivalent to fixed $date$.
  (let* ((sun ; Sunrise on Hindu date.
          (+ (hindu-day-count date) 1/4))
         (new-moon ; Beginning of lunar month.
          (- sun (mod sun arya-lunar-month)))
         (leap ; If lunar contained in solar.
          (and (>= (- arya-solar-month arya-lunar-month)
                   (mod new-moon arya-solar-month))
               (> (mod new-moon arya-solar-month) 0)))
         (month ; Next solar month's name.
          (1+ (mod (ceiling (/ new-moon
                               arya-solar-month))
                   12)))
         (day ; Lunar days since beginning of lunar month.
          (1+ (mod (quotient sun arya-lunar-day) 30)))
         (year ; Solar year at end of lunar month(s).
          (1- (ceiling (/ (+ new-moon arya-solar-month)
                          arya-solar-year)))))
    (old-hindu-lunar-date year month leap day)))

(defun fixed-from-old-hindu-lunar (l-date)
  ;; TYPE old-hindu-lunar-date -> fixed-date
  ;; Fixed date corresponding to Old Hindu lunar date.
  (let* ((year (old-hindu-lunar-year l-date))
         (month (old-hindu-lunar-month l-date))
         (leap (old-hindu-lunar-leap l-date))
         (day (old-hindu-lunar-day l-date))
         (mina ; One solar month before solar new year.
          (* (1- (* 12 year)) arya-solar-month))
         (lunar-new-year ; New moon after mina.
          (* arya-lunar-month
             (1+ (quotient mina arya-lunar-month)))))
    (floor
     (+ hindu-epoch
        lunar-new-year
        (* arya-lunar-month
           (if ; If there was a leap month this year.
               (and (not leap)
                    (<= (ceiling (/ (- lunar-new-year mina)
                                    (- arya-solar-month
                                       arya-lunar-month)))
                        month))
               month
             (1- month)))
        (* (1- day) arya-lunar-day) ; Lunar days.
        3/4)))) ; Add 1 if phase begins after sunrise.

(defconstant arya-jovian-period
  ;; TYPE rational
  ;; Number of days in one revolution of Jupiter around the
  ;; Sun.
  1577917500/364224)

(defun jovian-year (date)
  ;; TYPE fixed-date -> {1-60}
  ;; Year of Jupiter cycle at fixed $date$.
  (1+ (mod (quotient (hindu-day-count date)
                     (/ arya-jovian-period 12))
           60)))


;;;; Section: Balinese Calendar

(defun balinese-date (b1 b2 b3 b4 b5 b6 b7 b8 b9 b0)
  ;; TYPE (boolean 1-2 1-3 1-4 1-5 1-6 1-7 1-8 1-9 0-9)
  ;; TYPE -> balinese-date
  (list b1 b2 b3 b4 b5 b6 b7 b8 b9 b0))

(defun bali-luang (b-date)
  ;; TYPE balinese-date -> boolean
  (first b-date))

(defun bali-dwiwara (b-date)
  ;; TYPE balinese-date -> 1-2
  (second b-date))

(defun bali-triwara (b-date)
  ;; TYPE balinese-date -> 1-3
  (third b-date))

(defun bali-caturwara (b-date)
  ;; TYPE balinese-date -> 1-4
  (fourth b-date))

(defun bali-pancawara (b-date)
  ;; TYPE balinese-date -> 1-5
  (fifth b-date))

(defun bali-sadwara (b-date)
  ;; TYPE balinese-date -> 1-6
  (sixth b-date))

(defun bali-saptawara (b-date)
  ;; TYPE balinese-date -> 1-7
  (seventh b-date))

(defun bali-asatawara (b-date)
  ;; TYPE balinese-date -> 1-8
  (eighth b-date))

(defun bali-sangawara (b-date)
  ;; TYPE balinese-date -> 1-9
  (ninth b-date))

(defun bali-dasawara (b-date)
  ;; TYPE balinese-date -> 0-9
  (tenth b-date))

(defconstant bali-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of a Balinese Pawukon cycle.
  (fixed-from-jd 146))

(defun bali-day-from-fixed (date)
  ;; TYPE fixed-date -> 0-209
  ;; Position of $date$ in 210-day Pawukon cycle.
  (mod (- date bali-epoch) 210))

(defun bali-luang-from-fixed (date)
  ;; TYPE fixed-date -> boolean
  ;; Membership of $date$ in "1-day" Balinese cycle.
  (evenp (bali-dasawara-from-fixed date)))

(defun bali-dwiwara-from-fixed (date)
  ;; TYPE fixed-date -> 1-2
  ;; Position of $date$ in 2-day Balinese cycle.
  (adjusted-mod (bali-dasawara-from-fixed date) 2))

(defun bali-triwara-from-fixed (date)
  ;; TYPE fixed-date -> 1-3
  ;; Position of $date$ in 3-day Balinese cycle.
  (1+ (mod (bali-day-from-fixed date) 3)))

(defun bali-caturwara-from-fixed (date)
  ;; TYPE fixed-date -> 1-4
  ;; Position of $date$ in 4-day Balinese cycle.
  (adjusted-mod (bali-asatawara-from-fixed date) 4))

(defun bali-pancawara-from-fixed (date)
  ;; TYPE fixed-date -> 1-5
  ;; Position of $date$ in 5-day Balinese cycle.
  (1+ (mod (1+ (bali-day-from-fixed date)) 5)))

(defun bali-sadwara-from-fixed (date)
  ;; TYPE fixed-date -> 1-6
  ;; Position of $date$ in 6-day Balinese cycle.
  (1+ (mod (bali-day-from-fixed date) 6)))

(defun bali-saptawara-from-fixed (date)
  ;; TYPE fixed-date -> 1-7
  ;; Position of $date$ in Balinese week.
  (1+ (mod (bali-day-from-fixed date) 7)))

(defun bali-asatawara-from-fixed (date)
  ;; TYPE fixed-date -> 1-8
  ;; Position of $date$ in 8-day Balinese cycle.
  (let* ((day (bali-day-from-fixed date)))
    (1+ (mod
         (max 6
              (+ 4 (mod (- day 70)
                        210)))
         8))))

(defun bali-sangawara-from-fixed (date)
  ;; TYPE fixed-date -> 1-9
  ;; Position of $date$ in 9-day Balinese cycle.
  (1+ (mod (max 0
                (- (bali-day-from-fixed date) 3))
           9)))

(defun bali-dasawara-from-fixed (date)
  ;; TYPE fixed-date -> 0-9
  ;; Position of $date$ in 10-day Balinese cycle.
  (let* ((i ; Position in 5-day cycle.
          (1- (bali-pancawara-from-fixed date)))
         (j ; Weekday.
          (1- (bali-saptawara-from-fixed date))))
    (mod (+ 1 (nth i (list 5 9 7 4 8))
            (nth j (list 5 4 3 7 8 6 9)))
         10)))

(defun bali-pawukon-from-fixed (date)
  ;; TYPE fixed-date -> balinese-date
  ;; Positions in ten cycles of Balinese Pawukon calendar.
  (balinese-date (bali-luang-from-fixed date)
                 (bali-dwiwara-from-fixed date)
                 (bali-triwara-from-fixed date)
                 (bali-caturwara-from-fixed date)
                 (bali-pancawara-from-fixed date)
                 (bali-sadwara-from-fixed date)
                 (bali-saptawara-from-fixed date)
                 (bali-asatawara-from-fixed date)
                 (bali-sangawara-from-fixed date)
                 (bali-dasawara-from-fixed date)))

(defun bali-week-from-fixed (date)
  ;; TYPE fixed-date-> 1-30
  ;; Week number of $date$ in Balinese cycle.
  (1+ (quotient (bali-day-from-fixed date) 7)))

(defun bali-on-or-before (b-date date)
  ;; TYPE (balinese-date fixed-date) -> fixed-date
  ;; Last fixed date on or before $date$
  ;; with Pawukon $b-date$.
  (let* ((a5 ; Position in 5-day subcycle.
          (1- (bali-pancawara b-date)))
         (a6 ; Position in 6-day subcycle.
          (1- (bali-sadwara b-date)))
         (b7 ; Position in 7-day subcycle.
          (1- (bali-saptawara b-date)))
         (b35 ; Position in 35-day subcycle.
          (mod (+ a5 14 (* 15 (- b7 a5))) 35))
         (days ; Position in full cycle.
          (+ a6 (* 36 (- b35 a6))))
         (cap-Delta (bali-day-from-fixed 0)))
    (- date (mod (- (+ date cap-Delta) days) 210))))

(defun positions-in-interval (n c cap-Delta start end)
  ;; TYPE (positive-integer positive-integer
  ;; TYPE nonnegative-integer fixed-date fixed-date)
  ;; TYPE -> list-of-fixed-dates
  ;; List of occurrences of $n$-th day of $c$-day cycle
  ;; between $start$ and $end$ dates (inclusive).
  ;; $cap-Delta$ is position in cycle of RD 0.
  (let* ((pos (+ start
                 (mod (- n start cap-Delta 1) c))))
    (if (> pos end)
      nil
      (append (list pos)
              (positions-in-interval n c cap-Delta
                                     (1+ pos) end)))))

(defun kajeng-keliwon-in-gregorian (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; Occurrences of Kajeng Keliwon (9th day of
  ;; each 15-day subcycle of Pawukon) in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (cap-Delta (bali-day-from-fixed 0)))
    (positions-in-interval 9 15 cap-Delta jan1 dec31)))

(defun tumpek-in-gregorian (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; Occurrences of Tumpek (14th day of Pawukon and
  ;; every 35th subsequent day) within Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (dec31 (fixed-from-gregorian
                 (gregorian-date g-year december 31)))
         (cap-Delta (bali-day-from-fixed 0)))
    (positions-in-interval 14 35 cap-Delta jan1 dec31)))


;;;; Section: Time and Astronomy

(defun hr (x)
  ;; TYPE real -> interval
  ;; $x$ hours.
  (/ x 24))

(defun mt (x)
  ;; TYPE real -> distance
  ;; $x$ meters.
  x)

(defun deg (x)
  ;; TYPE real -> angle
  ;; TYPE list-of-reals -> list-of-angles
  ;; $x$ degrees.
  x)

(defun angle (d m s)
  ;; TYPE (nonnegative-integer
  ;; TYPE nonnegative-integer real) -> angle
  ;; $d$ degrees, $m$ arcminutes, $s$ arcseconds.
  (+ d (/ (+ m (/ s 60)) 60)))

(defun degrees (theta)
  ;; TYPE real -> angle
  ;; Normalize angle $theta$ to range 0-360 degrees.
  (mod theta 360))

(defun radians-to-degrees (theta)
  ;; TYPE radian -> angle
  ;; Convert angle $theta$ from radians to degrees.
  (degrees (/ theta pi 1/180)))

(defun degrees-to-radians (theta)
  ;; TYPE real -> radian
  ;; Convert angle $theta$ from degrees to radians.
  (* (degrees theta) pi 1/180))

(defun sin-degrees (theta)
  ;; TYPE angle -> amplitude
  ;; Sine of $theta$ (given in degrees).
  (sin (degrees-to-radians theta)))

(defun cosine-degrees (theta)
  ;; TYPE angle -> amplitude
  ;; Cosine of $theta$ (given in degrees).
  (cos (degrees-to-radians theta)))

(defun tangent-degrees (theta)
  ;; TYPE angle -> real
  ;; Tangent of $theta$ (given in degrees).
  (tan (degrees-to-radians theta)))

(defun arctan-degrees (x quad)
  ;; TYPE (real quadrant) -> angle
  ;; Arctangent of $x$ in degrees in quadrant $quad$.
  (let* ((alpha (radians-to-degrees (atan x))))
    (mod (if (or (= quad 1) (= quad 4))
             alpha
           (+ alpha (deg 180l0)))
         360)))

(defun arcsin-degrees (x)
  ;; TYPE amplitude -> angle
  ;; Arcsine of $x$ in degrees.
  (radians-to-degrees (asin x)))

(defun arccos-degrees (x)
  ;; TYPE amplitude -> angle
  ;; Arccosine of $x$ in degrees.
  (radians-to-degrees (acos x)))

(defun location (latitude longitude elevation zone)
  ;; TYPE (angle angle distance real) -> location
  (list latitude longitude elevation zone))

(defun latitude (locale)
  ;; TYPE location -> angle
  (first locale))

(defun longitude (locale)
  ;; TYPE location -> angle
  (second locale))

(defun elevation (locale)
  ;; TYPE location -> distance
  (third locale))

(defun zone (locale)
  ;; TYPE location -> real
  (fourth locale))

(defconstant mecca
  ;; TYPE location
  ;; Location of Mecca.
  (location (angle 21 25 24) (angle 39 49 24) (mt 1000) 2))

(defun direction (locale focus)
  ;; TYPE (location location) -> angle
  ;; Angle (clockwise from North)
  ;; to face $focus$ when standing in $locale$.
  ;; Subject to errors near focus and its antipode.
  (let* ((phi (latitude locale))
         (phi-prime (latitude focus))
         (psi (longitude locale))
         (psi-prime (longitude focus))
         (denom
          (- (* (cosine-degrees phi)
                (tangent-degrees phi-prime))
             (* (sin-degrees phi)
                (cosine-degrees
                 (- psi psi-prime))))))
    (if (= denom 0)
        0
      (mod (arctan-degrees
            (/ (sin-degrees (- psi-prime psi))
               denom)
            (if (< denom 0) 2 1))
           360))))

(defun standard-from-universal (tee_rom-u locale)
  ;; TYPE (moment location) -> moment
  ;; Standard time from $tee_rom-u$ in universal time at
  ;; $locale$.
  (+ tee_rom-u (/ (zone locale) 24l0)))

(defun universal-from-standard (tee_rom-s locale)
  ;; TYPE (moment location) -> moment
  ;; Universal time from $tee_rom-s$ in standard time at
  ;; $locale$.
  (- tee_rom-s (/ (zone locale) 24l0)))

(defun local-from-universal (tee_rom-u locale)
  ;; TYPE (moment location) -> moment
  ;; Local time from universal $tee_rom-u$ at $locale$.
  (+ tee_rom-u (/ (longitude locale) (deg 360l0))))

(defun universal-from-local (tee_ell locale)
  ;; TYPE (moment location) -> moment
  ;; Universal time from local $tee_ell$ at $locale$.
  (- tee_ell (/ (longitude locale) (deg 360l0))))

(defun standard-from-local (tee_ell locale)
  ;; TYPE (moment location) -> moment
  ;; Standard time from local $tee_ell$ at $locale$.
  (standard-from-universal
   (universal-from-local tee_ell locale)
   locale))

(defun local-from-standard (tee_rom-s locale)
  ;; TYPE (moment location) -> moment
  ;; Local time from standard $tee_rom-s$ at $locale$.
  (local-from-universal
   (universal-from-standard tee_rom-s locale)
   locale))

(defun midday (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time on fixed $date$ of midday at $locale$.
  (standard-from-local
   (local-from-apparent (+ date (hr 12l0)))
   locale))

(defun midnight (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time on fixed $date$ of true (apparent)
  ;; midnight at $locale$.
  (standard-from-local
   (local-from-apparent date)
   locale))

(defun julian-centuries (tee)
  ;; TYPE moment -> real
  ;; Julian centuries since 2000 at moment $tee$.
  (/ (- (dynamical-from-universal tee) j2000)
     36525l0))

(defun obliquity (tee)
  ;; TYPE moment -> angle
  ;; Obliquity of ecliptic at moment $tee$.
  (let* ((c (julian-centuries tee)))
    (+ (angle 23 26 21.448l0)
       (poly c (list 0l0
                     (angle 0 0 -46.8150l0)
                     (angle 0 0 -0.00059l0)
                     (angle 0 0 0.001813l0))))))

(defun moment-from-depression (approx locale alpha)
  ;; TYPE (moment location angle) -> moment
  ;; Moment in Local Time near $approx$ when depression
  ;; angle of sun is $alpha$ (negative if above horizon) at
  ;; $locale$; bogus if never occurs.
  (let* ((phi (latitude locale))
         (tee (universal-from-local approx locale))
         (delta ; Declination of sun.
          (arcsin-degrees
           (* (sin-degrees (obliquity tee))
              (sin-degrees (solar-longitude tee)))))
         (morning (< (mod approx 1) 0.5l0))
         (sine-offset (+ (* (tangent-degrees phi)
                     (tangent-degrees delta))
                  (/ (sin-degrees alpha)
                     (* (cosine-degrees delta)
                        (cosine-degrees phi))))))
    (if (<= (abs sine-offset) 1) ; event occurs
        (local-from-apparent
         (+ (floor approx) 0.5l0
            (* (if morning -1 1)
               (- (mod (+ 0.5l0 
                          (/ (arcsin-degrees sine-offset) 
                             (deg 360l0)))
                       1)
                  0.25l0))))
      bogus)))

(defun dawn (date locale alpha)
  ;; TYPE (fixed-date location angle) -> moment
  ;; Standard time in morning of $date$ at $locale$
  ;; when depression angle of sun is $alpha$.
  (let* ((approx (moment-from-depression ; approximate time
                  (+ date 0.25l0) locale alpha))
         (result (moment-from-depression 
                  (if (equal approx bogus)
                      date
                    approx)
                  locale alpha)))
    (if (equal result bogus)
        bogus
      (standard-from-local result locale))))

(defun dusk (date locale alpha)
  ;; TYPE (fixed-date location angle) -> moment
  ;; Standard time in evening on $date$ at $locale$
  ;; when depression angle of sun is $alpha$.
  (let* ((approx (moment-from-depression ; approximate time
                  (+ date 0.75l0) locale alpha))
         (result (moment-from-depression 
                  (if (equal approx bogus)
                      (+ date 0.99l0)
                    approx)
                  locale alpha)))
    (if (equal result bogus)
      bogus
      (standard-from-local result locale))))

(defun sunrise (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time of sunrise on $date$ at $locale$.
  (let* ((h (max 0 (elevation locale)))
         (cap-R (mt 6.372d6)) ; Radius of Earth.
         (dip ; Depression of visible horizon.
          (arccos-degrees (/ cap-R (+ cap-R h))))
         (alpha (+ (angle 0 50 0) dip)))
    (dawn date locale alpha)))

(defun sunset (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time of sunset on fixed $date$ at $locale$.
  (let* ((h (max 0 (elevation locale)))
         (cap-R (mt 6.372d6)) ; Radius of Earth.
         (dip ; Depression of visible horizon.
          (arccos-degrees (/ cap-R (+ cap-R h))))
         (alpha (+ (angle 0 50 0) dip)))
    (dusk date locale alpha)))

(defun jewish-dusk (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time of Jewish dusk on fixed $date$
  ;; at $locale$ (as per Vilna Gaon).
  (dusk date locale (angle 4 40 0)))

(defun jewish-sabbath-ends (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time of end of Jewish sabbath on fixed $date$
  ;; at $locale$ (as per Berthold Cohn).
  (dusk date locale (angle 7 5 0))) 

(defun temporal-hour (date locale)
  ;; TYPE (fixed-date location) -> real
  ;; Length of daytime temporal hour on fixed $date$ at
  ;; $locale$.
  (if (or (equal (sunrise date locale) bogus)
          (equal (sunset date locale) bogus))
      bogus
    (/ (- (sunset date locale)
          (sunrise date locale))
       12)))

(defun standard-from-sundial (date hour locale)
  ;; TYPE (fixed-date real location) -> moment
  ;; Standard time on fixed $date$ of temporal $hour$ at
  ;; $locale$.
  (let ((tee (temporal-hour date locale)))
    (if (equal tee bogus)
        bogus
      (+ (sunrise date locale)
         (if (<= 6 hour 18)
             (* (- hour 6) tee)
           (* (- hour 6) (- 1/12 tee)))))))

(defun jewish-morning-end (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time on fixed $date$ at $locale$ of end of
  ;; morning according to Jewish ritual.
  (standard-from-sundial date 10 locale))

(defun asr (date locale)
  ;; TYPE (fixed-date location) -> moment
  ;; Standard time of asr on fixed $date$ at
  ;; $locale$.
  (let* ((noon; Time when sun nearest zenith.
          (universal-from-standard
           (midday date locale)
           locale))
         (phi (latitude locale))
         (delta; Solar declination at noon.
          (arcsin-degrees
           (* (sin-degrees (obliquity noon))
              (sin-degrees (solar-longitude noon)))))
         (altitude ; Solar altitude at noon.
          (arcsin-degrees
           (+ (* (sin-degrees phi)
                 (sin-degrees delta))
              (* (cosine-degrees phi)
                 (cosine-degrees delta)))))
         (h; Sun's altitude when shadow increases by
          (arctan-degrees ; ... double its length.
           (/ (tangent-degrees altitude)
              (1+ (* 2 (tangent-degrees altitude))))
            ; For Shafii use instead:
            ; (1+ (tangent-degrees altitude)))
           1)))
    (dusk date locale (- h))))

(defun universal-from-dynamical (tee)
  ;; TYPE moment -> moment
  ;; Universal moment from Dynamical time $tee$.
  (- tee (ephemeris-correction tee)))

(defun dynamical-from-universal (tee)
  ;; TYPE moment -> moment
  ;; Dynamical time at Universal moment $tee$.
  (+ tee (ephemeris-correction tee)))

(defconstant j2000
  ;; TYPE moment
  ;; Noon at start of Gregorian year 2000.
  (+ (hr 12l0)
     (fixed-from-gregorian
      (gregorian-date 2000 january 1))))

(defun sidereal-from-moment (tee)
  ;; TYPE moment -> angle
  ;; Mean sidereal time of day from moment $tee$
  ;; expressed as hour angle.
  ;; Adapted from "Astronomical Algorithms"
  ;; by Jean Meeus, Willmann-Bell, Inc., 1991.
  (let* ((c (/ (- tee j2000) 36525l0)))
    (mod (poly c 
               (deg (list 280.46061837l0
                          (* 36525l0 360.98564736629l0)
                          0.000387933l0 -1/38710000)))
         360)))

(defconstant mean-tropical-year
  ;; TYPE real
  365.242189l0)

(defconstant mean-synodic-month
  ;; TYPE real
  29.530588853l0)

(defun ephemeris-correction (tee)
  ;; TYPE moment -> fraction-of-day
  ;; Dynamical Time minus Universal Time (in days) for
  ;; fixed time $tee$.  Adapted from "Astronomical Algorithms"
  ;; by Jean Meeus, Willmann-Bell, Inc., 1991.
  (let* ((year (gregorian-year-from-fixed (floor tee)))
         (c (/ (gregorian-date-difference
                (gregorian-date 1900 january 1)
                    (gregorian-date year july 1))
                   36525l0)))
    (cond ((<= 1988 year 2019)
           (/ (- year 1933) 24l0 60l0 60l0))
          ((<= 1900 year 1987)
           (poly c 
                 (list -0.00002l0 0.000297l0 0.025184l0
                       -0.181133l0 0.553040l0 -0.861938l0
                       0.677066l0 -0.212591l0)))
          ((<= 1800 year 1899)
           (poly c 
                 (list -0.000009l0 0.003844l0 0.083563l0
                       0.865736l0 4.867575l0 15.845535l0
                       31.332267l0 38.291999l0 28.316289l0
                       11.636204l0 2.043794l0)))
          ((<= 1620 year 1799)
           (/ (poly (- year 1600)
                    (list 196.58333l0 -4.0675l0 0.0219167l0))
              24l0 60l0 60l0))
          (t (let* ((x (+ (hr 12l0)
                          (gregorian-date-difference
                           (gregorian-date 1810 january 1)
                           (gregorian-date year january 1)))))
               (/ (- (/ (* x x) 41048480l0) 15)
                  24l0 60l0 60l0))))))

(defun equation-of-time (tee)
  ;; TYPE moment -> fraction-of-day
  ;; Equation of time (as fraction of day) for moment $tee$.
  ;; Adapted from "Astronomical Algorithms" by Jean Meeus,
  ;; Willmann-Bell, Inc., 1991.
  (let* ((c (julian-centuries tee))
         (longitude
          (poly c
                (deg (list 280.46645l0 36000.76983l0
                           0.0003032l0))))
         (anomaly
          (poly c
                (deg (list 357.52910l0 35999.05030l0
                      -0.0001559l0 -0.00000048l0))))
         (eccentricity
          (poly c
                (deg (list 0.016708617l0 -0.000042037l0
                      -0.0000001236l0))))
         (varepsilon (obliquity tee))
         (y (expt (tangent-degrees (/ varepsilon 2)) 2))
         (equation
          (* (/ 1 2 pi)
             (+ (* y (sin-degrees (* 2 longitude)))
                (* -2 eccentricity (sin-degrees anomaly))
                (* 4 eccentricity y (sin-degrees anomaly)
                   (cosine-degrees (* 2 longitude)))
                (* -0.5 y y (sin-degrees (* 4 longitude)))
                (* -1.25 eccentricity eccentricity
                   (sin-degrees (* 2 anomaly)))))))
    (* (signum equation) (min (abs equation) (hr 12l0)))))

(defun local-from-apparent (tee)
  ;; TYPE moment -> moment
  ;; Local time from sundial time $tee$.
  (- tee (equation-of-time tee)))

(defun apparent-from-local (tee)
  ;; TYPE moment -> moment
  ;; Sundial time at local time $tee$.
  (+ tee (equation-of-time tee)))

(defun solar-longitude (tee)
  ;; TYPE moment -> season
  ;; Longitude of sun at moment $tee$.
  ;; Adapted from "Planetary Programs and Tables from -4000
  ;; to +2800" by Pierre Bretagnon and Jean-Louis Simon,
  ;; Willmann-Bell, Inc., 1986.
  (let* ((c       ; moment in Julian centuries
          (julian-centuries tee))
         (coefficients
          (list 403406 195207 119433 112392 3891 2819 1721
                660 350 334 314 268 242 234 158 132 129 114
                99 93 86 78 72 68 64 46 38 37 32 29 28 27 27
                25 24 21 21 20 18 17 14 13 13 13 12 10 10 10
                10))
         (multipliers
          (list 0.9287892l0 35999.1376958l0 35999.4089666l0
                35998.7287385l0 71998.20261l0 71998.4403l0
                36000.35726l0 71997.4812l0 32964.4678l0
                -19.4410l0 445267.1117l0 45036.8840l0 3.1008l0
                22518.4434l0 -19.9739l0 65928.9345l0
                9038.0293l0 3034.7684l0 33718.148l0 3034.448l0
                -2280.773l0 29929.992l0 31556.493l0 149.588l0
                9037.750l0 107997.405l0 -4444.176l0 151.771l0
                67555.316l0 31556.080l0 -4561.540l0
                107996.706l0 1221.655l0 62894.167l0
                31437.369l0 14578.298l0 -31931.757l0
                34777.243l0 1221.999l0 62894.511l0
                -4442.039l0 107997.909l0 119.066l0 16859.071l0
                -4.578l0 26895.292l0 -39.127l0 12297.536l0
                90073.778l0))
         (addends
          (list 270.54861l0 340.19128l0 63.91854l0 331.26220l0
                317.843l0 86.631l0 240.052l0 310.26l0 247.23l0
                260.87l0 297.82l0 343.14l0 166.79l0 81.53l0
                3.50l0 132.75l0 182.95l0 162.03l0 29.8l0
                266.4l0 249.2l0 157.6l0 257.8l0 185.1l0 69.9l0
                8.0l0 197.1l0 250.4l0 65.3l0 162.7l0 341.5l0
                291.6l0 98.5l0 146.7l0 110.0l0 5.2l0 342.6l0
                230.9l0 256.1l0 45.3l0 242.9l0 115.2l0 151.8l0
                285.3l0 53.3l0 126.6l0 205.7l0 85.9l0
                146.1l0))
         (longitude
          (+ (deg 282.7771834l0)
             (* 36000.76953744l0 c)
             (* 0.000005729577951308232l0
                (sigma ((x coefficients)
                        (y addends)
                        (z multipliers))
                       (* x (sin-degrees (+ y (* z c)))))))))
    (mod (+ longitude (aberration tee) (nutation tee))
         360)))

(defun nutation (tee)
  ;; TYPE moment -> angle
  ;; Longitudinal nutation at moment $tee$.
  (let* ((c       ; moment in Julian centuries
          (julian-centuries tee))
         (cap-A (poly c (deg (list 124.90l0 -1934.134l0
                                   0.002063l0))))
         (cap-B (poly c (deg (list 201.11l0 72001.5377l0
                                   0.00057l0)))))
    (+ (* (deg -0.004778l0) (sin-degrees cap-A))
       (* (deg -0.0003667l0) (sin-degrees cap-B)))))

(defun aberration (tee)
  ;; TYPE moment -> angle
  ;; Aberration at moment $tee$.
  (let* ((c       ; moment in Julian centuries
          (julian-centuries tee)))
    (- (* (deg 0.0000974l0)
          (cosine-degrees
           (+ (deg 177.63l0) (* (deg 35999.01848l0) c))))
       (deg 0.005575l0))))

(defun solar-longitude-after (tee phi)
  ;; TYPE (moment season) -> moment
  ;; Moment UT of the first time at or after $tee$
  ;; when the solar longitude will be $phi$ degrees.
  (let* ((varepsilon 1d-5) ; Accuracy of solar-longitude.
         (rate ; Mean days for 1 degree change.
          (/ mean-tropical-year (deg 360)))
         (tau ; Estimate (within 5 days).
          (+ tee
             (* rate
                (mod (- phi (solar-longitude tee)) 360))))
         (l (max tee (- tau 5))) ; At or after tee.
         (u (+ tau 5)))
    (binary-search ; Bisection search.
     l l
     u u
     x (< (mod (- (solar-longitude x) phi) 360)
          (deg 180l0))
     (< (- u l) varepsilon))))

(defconstant spring
  ;; TYPE season
  ;; Longitude of sun at vernal equinox.
  (deg 0l0))

(defconstant summer
  ;; TYPE season
  ;; Longitude of sun at summer solstice.
  (deg 90l0))

(defconstant autumn
  ;; TYPE season
  ;; Longitude of sun at autumnal equinox.
  (deg 180l0))

(defconstant winter
  ;; TYPE season
  ;; Longitude of sun at winter solstice.
  (deg 270l0))

(defun lunar-longitude (tee)
  ;; TYPE moment -> angle
  ;; Longitude of moon (in degrees) at moment $tee$.
  ;; Adapted from "Astronomical Algorithms" by Jean Meeus,
  ;; Willmann-Bell, Inc., 1991.
  (let* ((c (julian-centuries tee))
         (mean-moon
          (degrees
           (poly c
                 (deg (list 218.3164591l0 481267.88134236l0
                       -.0013268l0 1/538841 -1/65194000)))))
         (elongation
          (degrees
           (poly c
                 (deg (list 297.8502042l0 445267.1115168l0
                       -.00163l0 1/545868 -1/113065000)))))
         (solar-anomaly
          (degrees
           (poly c
                 (deg (list 357.5291092l0 35999.0502909l0
                       -.0001536l0 1/24490000)))))
         (lunar-anomaly
          (degrees
           (poly c
                 (deg (list 134.9634114l0 477198.8676313l0
                       0.008997l0 1/69699 -1/14712000)))))
         (moon-node
          (degrees
           (poly c
                 (deg (list 93.2720993l0 483202.0175273l0
                       -.0034029l0 -1/3526000 1/863310000)))))
         (cap-E (poly c (list 1 -0.002516l0 -0.0000074l0)))
         (args-lunar-elongation
          (list 0 2 2 0 0 0 2 2 2 2 0 1 0 2 0 0 4 0 4 2 2 1
                1 2 2 4 2 0 2 2 1 2 0 0 2 2 2 4 0 3 2 4 0 2
                2 2 4 0 4 1 2 0 1 3 4 2 0 1 2))
         (args-solar-anomaly
          (list 0 0 0 0 1 0 0 -1 0 -1 1 0 1 0 0 0 0 0 0 1 1
                0 1 -1 0 0 0 1 0 -1 0 -2 1 2 -2 0 0 -1 0 0 1
                -1 2 2 1 -1 0 0 -1 0 1 0 1 0 0 -1 2 1 0))
         (args-lunar-anomaly
          (list 1 -1 0 2 0 0 -2 -1 1 0 -1 0 1 0 1 1 -1 3 -2
                -1 0 -1 0 1 2 0 -3 -2 -1 -2 1 0 2 0 -1 1 0
                -1 2 -1 1 -2 -1 -1 -2 0 1 4 0 -2 0 2 1 -2 -3
                2 1 -1 3))
         (args-moon-node
          (list 0 0 0 0 0 2 0 0 0 0 0 0 0 -2 2 -2 0 0 0 0 0
                0 0 0 0 0 0 0 2 0 0 0 0 0 0 -2 2 0 2 0 0 0 0
                0 0 -2 0 0 0 0 -2 -2 0 0 0 0 0 0 0))
         (sine-coefficients
          (list 6288774 1274027 658314 213618 -185116 -114332
                58793 57066 53322 45758 -40923 -34720 -30383
                15327 -12528 10980 10675 10034 8548 -7888
                -6766 -5163 4987 4036 3994 3861 3665 -2689
                -2602 2390 -2348 2236 -2120 -2069 2048 -1773
                -1595 1215 -1110 -892 -810 759 -713 -700 691
                596 549 537 520 -487 -399 -381 351 -340 330
                327 -323 299 294))
         (correction
          (* (/ (deg 1) 1000000)
             (sigma ((v sine-coefficients)
                     (w args-lunar-elongation)
                     (x args-solar-anomaly)
                     (y args-lunar-anomaly)
                     (z args-moon-node))
                    (* v (expt cap-E (abs x))
                       (sin-degrees
                        (+ (* w elongation)
                           (* x solar-anomaly)
                           (* y lunar-anomaly)
                           (* z moon-node)))))))
         (venus (* (/ (deg 3958) 1000000)
                   (sin-degrees
                    (+ 119.75l0 (* c 131.849l0)))))
         (jupiter (* (/ (deg 318) 1000000)
                     (sin-degrees
                      (+ 53.09l0 (* c 479264.29l0)))))
         (flat-earth
          (* (/ (deg 1962) 1000000)
             (sin-degrees (- mean-moon moon-node)))))
    (mod (+ mean-moon correction venus jupiter flat-earth
            (nutation tee))
         360)))

(defun nth-new-moon (n)
  ;; TYPE integer -> moment
  ;; Moment of $n$-th new moon after (or before) the new moon
  ;; of January 11, 1.  Adapted from "Astronomical
  ;; Algorithms" by Jean Meeus, Willmann-Bell, Inc., 1991.
  (let* ((k (- n 24724)) ; Months since j2000.
         (c (/ k 1236.85l0)) ; Julian centuries.
         (approx (poly c (list 730125.59765l0
                            (* mean-synodic-month
                               1236.85l0)
                            0.0001337l0
                            -0.000000150l0
                            0.00000000073l0)))
         (cap-E (poly c (list 1 -0.002516l0 -0.0000074l0)))
         (solar-anomaly
          (poly c (deg (list 2.5534l0
                             (* 1236.85l0 29.10535669l0)
                             -0.0000218l0 -0.00000011l0))))
         (lunar-anomaly
          (poly c (deg (list 201.5643l0 (* 385.81693528l0
                                      1236.85l0)
                        0.0107438l0 0.00001239l0
                        -0.000000058l0))))
         (moon-argument ; Moon's argument of latitude.
          (poly c (deg (list 160.7108l0 (* 390.67050274l0
                                      1236.85l0)
                        -0.0016341l0 -0.00000227l0
                        0.000000011l0))))
         (cap-omega ; Longitude of ascending node.
          (poly c (list 124.7746l0 (* -1.56375580l0 1236.85l0)
                        0.0020691l0 0.00000215l0)))
         (E-factor (list 0 1 0 0 1 1 2 0 0 1 0 1 1 1 0 0 0 0
                         0 0 0 0 0 0))
         (solar-coeff (list 0 1 0 0 -1 1 2 0 0 1 0 1 1 -1 2
                            0 3 1 0 1 -1 -1 1 0))
         (lunar-coeff (list 1 0 2 0 1 1 0 1 1 2 3 0 0 2 1 2
                            0 1 2 1 1 1 3 4))
         (moon-coeff (list 0 0 0 2 0 0 0 -2 2 0 0 2 -2 0 0
                           -2 0 -2 2 2 2 -2 0 0))
         (sine-coeff
          (list -0.40720l0 0.17241l0 0.01608l0 0.01039l0
                0.00739l0 -0.00514l0 0.00208l0
                -0.00111l0 -0.00057l0 0.00056l0
                -0.00042l0 0.00042l0 0.00038l0
                -0.00024l0 -0.00007l0 0.00004l0
                0.00004l0 0.00003l0 0.00003l0
                -0.00003l0 0.00003l0 -0.00002l0
                -0.00002l0 0.00002l0))
         (correction
          (+ (* (deg -.00017l0) (sin-degrees cap-omega))
             (sigma ((v sine-coeff)
                     (w E-factor)
                     (x solar-coeff)
                     (y lunar-coeff)
                     (z moon-coeff))
                    (* v (expt cap-E w)
                       (sin-degrees
                        (+ (* x solar-anomaly)
                           (* y lunar-anomaly)
                           (* z moon-argument)))))))
         (add-const
          (list 251.88l0 251.83l0 349.42l0 84.66l0
                141.74l0 207.14l0 154.84l0 34.52l0 207.19l0
                291.34l0 161.72l0 239.56l0 331.55l0))
         (add-coeff
          (list 0.016321l0 26.641886l0
                36.412478l0 18.206239l0 53.303771l0
                2.453732l0 7.306860l0 27.261239l0 0.121824l0
                1.844379l0 24.198154l0 25.513099l0
                3.592518l0))
         (add-factor
          (list 0.000165l0 0.000164l0 0.000126l0
                0.000110l0 0.000062l0 0.000060l0 0.000056l0
                0.000047l0 0.000042l0 0.000040l0 0.000037l0
                0.000035l0 0.000023l0))
         (extra
          (* (deg 0.000325l0)
             (sin-degrees
              (poly c
                    (deg (list 299.77l0 132.8475848l0
                          -0.009173l0))))))
         (additional
          (sigma ((i add-const)
                  (j add-coeff)
                  (l add-factor))
                 (* l (sin-degrees (+ i (* j k)))))))
    (universal-from-dynamical
     (+ approx correction extra additional))))

(defun new-moon-before (tee)
  ;; TYPE moment -> moment
  ;; Moment UT of last new moon before $tee$.
  (let* ((n (round (- (/ tee mean-synodic-month)
                      (/ (lunar-phase tee) (deg 360))))))
    (nth-new-moon n)))

(defun new-moon-after (tee)
  ;; TYPE moment -> moment
  ;; Moment UT of first new moon at or after $tee$.
  (let* ((n (round (- (/ tee mean-synodic-month)
                      (/ (lunar-phase tee) (deg 360))))))
    (nth-new-moon (1+ n))))

(defun lunar-phase (tee)
  ;; TYPE moment -> phase
  ;; Lunar phase, as an angle in degrees, at moment $tee$.
  ;; An angle of 0 means a new moon, 90 degrees means the
  ;; first quarter, 180 means a full moon, and 270 degrees
  ;; means the last quarter.
  (mod (- (lunar-longitude tee) (solar-longitude tee))
       360))

(defun lunar-phase-before (tee phi)
  ;; TYPE (moment phase) -> moment
  ;; Moment UT of the last time at or before $tee$
  ;; when the lunar-phase was $phi$ degrees.
  (let* ((varepsilon 1d-5) ; Accuracy.
         (tau ; Estimate.
          (- tee
             (* mean-synodic-month 1/360
                (mod (- (lunar-phase tee) phi) (deg 360)))))
         (l (- tau 2))
         (u (min tee (+ tau 2)))) ; At or before tee.
    (binary-search ; Bisection search.
     l l
     u u
     x (< (mod (- (lunar-phase x) phi) 360)
          (deg 180l0))
     (< (- u l) varepsilon))))

(defconstant new
  ;; TYPE phase
  ;; Excess of lunar longitude over solar longitude at new
  ;; moon.
  (deg 0l0))

(defconstant first-quarter
  ;; TYPE phase
  ;; Excess of lunar longitude over solar longitude at first
  ;; quarter moon.
  (deg 90l0))

(defconstant full
  ;; TYPE phase
  ;; Excess of lunar longitude over solar longitude at full
  ;; moon.
  (deg 180l0))

(defconstant last-quarter
  ;; TYPE phase
  ;; Excess of lunar longitude over solar longitude at last
  ;; quarter moon.
  (deg 270l0))

(defun lunar-phase-after (tee phi)
  ;; TYPE (moment phase) -> moment
  ;; Moment UT of the next time at or after $tee$
  ;; when the lunar-phase is $phi$ degrees.
  (let* ((varepsilon 1d-5) ; Accuracy.
         (tau ; Estimate.
          (+ tee
             (* mean-synodic-month 1/360
                (mod (- phi (lunar-phase tee)) (deg 360)))))
         (l (max tee (- tau 2))) ; At or after tee.
         (u (+ tau 2)))
    (binary-search ; Bisection search.
     l l
     u u
     x (< (mod (- (lunar-phase x) phi) 360)
          (deg 180l0))
     (< (- u l) varepsilon))))

(defun lunar-latitude (tee)
  ;; TYPE moment -> angle
  ;; Latitude of moon (in degrees) at moment $tee$.
  ;; Adapted from "Astronomical Algorithms" by Jean Meeus,
  ;; Willmann-Bell, Inc., 1998.
  (let* ((c (julian-centuries tee))
         (longitude
          (degrees
           (poly c
                 (deg (list 218.3164591l0 481267.88134236l0
                       -.0013268l0 1/538841 -1/65194000)))))
         (elongation
          (degrees
           (poly c
                 (deg (list 297.8502042l0 445267.1115168l0
                       -.00163l0 1/545868 -1/113065000)))))
         (solar-anomaly
          (degrees
           (poly c
                 (deg (list 357.5291092l0 35999.0502909l0
                       -.0001536l0 1/24490000)))))
         (lunar-anomaly
          (degrees
           (poly c
                 (deg (list 134.9634114l0 477198.8676313l0
                       0.008997l0 1/69699 -1/14712000)))))
         (moon-node
          (degrees
           (poly c
                 (deg (list 93.2720993l0 483202.0175273l0
                       -.0034029l0 -1/3526000 1/863310000)))))
         (cap-E (poly c (list 1 -0.002516l0 -0.0000074l0)))
         (args-lunar-elongation
          (list 0 0 0 2 2 2 2 0 2 0 2 2 2 2 2 2 2 0 4 0 0 0
                1 0 0 0 1 0 4 4 0 4 2 2 2 2 0 2 2 2 2 4 2 2
                0 2 1 1 0 2 1 2 0 4 4 1 4 1 4 2))
         (args-solar-anomaly
          (list 0 0 0 0 0 0 0 0 0 0 -1 0 0 1 -1 -1 -1 1 0 1
                0 1 0 1 1 1 0 0 0 0 0 0 0 0 -1 0 0 0 0 1 1
                0 -1 -2 0 1 1 1 1 1 0 -1 1 0 -1 0 0 0 -1 -2))
         (args-lunar-anomaly
          (list 0 1 1 0 -1 -1 0 2 1 2 0 -2 1 0 -1 0 -1 -1 -1
                0 0 -1 0 1 1 0 0 3 0 -1 1 -2 0 2 1 -2 3 2 -3
                -1 0 0 1 0 1 1 0 0 -2 -1 1 -2 2 -2 -1 1 1 -2
                0 0))
         (args-moon-node
          (list 1 1 -1 -1 1 -1 1 1 -1 -1 -1 -1 1 -1 1 1 -1 -1
                -1 1 3 1 1 1 -1 -1 -1 1 -1 1 -3 1 -3 -1 -1 1
                -1 1 -1 1 1 1 1 -1 3 -1 -1 1 -1 -1 1 -1 1 -1
                -1 -1 -1 -1 -1 1))
         (sine-coefficients
          (list 5128122 280602 277693 173237 55413 46271 32573
                17198 9266 8822 8216 4324 4200 -3359 2463 2211
                2065 -1870 1828 -1794 -1749 -1565 -1491 -1475
                -1410 -1344 -1335 1107 1021 833 777 671 607
                596 491 -451 439 422 421 -366 -351 331 315
                302 -283 -229 223 223 -220 -220 -185 181
                -177 176 166 -164 132 -119 115 107))
         (latitude
          (* (/ (deg 1) 1000000)
             (sigma ((v sine-coefficients)
                     (w args-lunar-elongation)
                     (x args-solar-anomaly)
                     (y args-lunar-anomaly)
                     (z args-moon-node))
                    (* v (expt cap-E (abs x))
                       (sin-degrees
                        (+ (* w elongation)
                           (* x solar-anomaly)
                           (* y lunar-anomaly)
                           (* z moon-node)))))))
         (venus (* (/ (deg 175) 1000000)
                   (+ (sin-degrees
                       (+ (deg 119.75l0) (* c 131.849l0)
                          moon-node))
                      (sin-degrees
                       (+ (deg 119.75l0) (* c 131.849l0)
                          (- moon-node))))))
         (flat-earth
          (+ (* (/ (deg -2235) 1000000) 
                (sin-degrees longitude))
             (* (/ (deg 127) 1000000) (sin-degrees
                             (- longitude lunar-anomaly)))
             (* (/ (deg -115) 1000000) (sin-degrees
                              (+ longitude lunar-anomaly)))))
         (extra (* (/ (deg 382) 1000000)
                   (sin-degrees
                    (+ (deg 313.45l0) 
                       (* c (deg 481266.484l0)))))))
    (mod (+ latitude venus flat-earth extra) 360)))

(defun lunar-altitude (tee locale)
  ;; TYPE (fixed-date location) -> angle
  ;; Altitude of moon at $tee$ at $locale$,
  ;; ignoring parallax and refraction.
  ;; Adapted from "Astronomical Algorithms" by Jean Meeus,
  ;; Willmann-Bell, Inc., 1998.
  (let* ((phi ; Local latitude.
          (latitude locale))
         (psi ; Local longitude.
          (longitude locale))
         (varepsilon ; Obliquity of ecliptic.
          (obliquity tee))
         (lambda ; Lunar longitude.
          (lunar-longitude tee))
         (beta ; Lunar latitude.
          (lunar-latitude tee))
         (alpha ; Lunar right ascension.
          (arctan-degrees
           (/ (- (* (sin-degrees lambda)
                    (cosine-degrees varepsilon))
                 (* (tangent-degrees beta)
                    (sin-degrees varepsilon)))
              (cosine-degrees lambda))
           (1+ (quotient lambda (deg 90))))) ; Quadrant.
         (delta ; Lunar declination.
          (arcsin-degrees (+ (* (sin-degrees beta)
                                (cosine-degrees varepsilon))
                             (* (cosine-degrees beta)
                                (sin-degrees varepsilon)
                                (sin-degrees lambda)))))
         (theta0 ; Sidereal time.
          (sidereal-from-moment tee))
         (cap-H ; Local hour angle.
          (mod (- theta0 (- psi) alpha) 360))
         (altitude
          (arcsin-degrees (+ (* (sin-degrees phi)
                                (sin-degrees delta))
                             (* (cosine-degrees phi)
                                (cosine-degrees delta)
                                (cosine-degrees cap-H))))))
    (- (mod (+ altitude (deg 180)) 360) (deg 180))))
 
(defun estimate-prior-solar-longitude (tee phi)
  ;; TYPE (moment season) -> moment
  ;; Approximate $moment$ at or before $tee$
  ;; when solar longitude just exceeded $phi$ degrees.
  (let* ((rate ; Mean change of one degree.
          (/ mean-tropical-year (deg 360)))
         (tau ; First approximation.
          (- tee
             (* rate (mod (- (solar-longitude tee)
                             phi)
                          360))))
         (cap-Delta ; Difference in longitude.
          (- (mod (- (solar-longitude tau) phi (- (deg 180)))
                  360)
             (deg 180))))
    (min tee (- tau (* rate cap-Delta)))))

(defconstant haifa
  ;; TYPE location
  ;; Location of Haifa, Israel.
  (location (deg 32.82l0) (deg 35l0) (mt 0) 2))

(defun sunset-in-haifa (date)
  ;; TYPE fixed-date -> moment
  ;; Universal time of sunset of evening before fixed $date$
  ;; in Haifa.
  (universal-from-standard
    (sunset (- date 1) haifa)
    haifa))

(defun future-bahai-new-year-on-or-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date of Future Bahai New Year on or before
  ;; fixed $date$.
  (let* ((approx ; Approximate time of equinox.
          (estimate-prior-solar-longitude
           (sunset-in-haifa date)
           spring)))
    (next day (1- (floor approx))
          (<= (solar-longitude (sunset-in-haifa day))
              (+ spring (deg 2))))))

(defun fixed-from-future-bahai (b-date)
  ;; TYPE bahai-date -> fixed-date
  ;; Fixed date of Bahai date.
  (let* ((major (bahai-major b-date))
         (cycle (bahai-cycle b-date))
         (year (bahai-year b-date))
         (month (bahai-month b-date))
         (day (bahai-day b-date))
         (years; Years from epoch
          (+ (* 361 (1- major))
             (* 19 (1- cycle))
             year)))
    (cond ((= month 19); last month of year
           (+ (future-bahai-new-year-on-or-before
               (+ bahai-epoch
                  (floor (* mean-tropical-year 
                            (+ years 1/2)))))
              -19 day -1))
          ((= month ayyam-i-ha)
           ; intercalary month, between 18th & 19th
           (+ (future-bahai-new-year-on-or-before
               (+ bahai-epoch
                  (floor (* mean-tropical-year
                            (- years 1/2)))))
              342 day -1))
          (t (+ (future-bahai-new-year-on-or-before
                 (+ bahai-epoch
                    (floor (* mean-tropical-year
                              (- years 1/2)))))
                (* (1- month) 19)
                day -1)))))

(defun future-bahai-from-fixed (date)
  ;; TYPE fixed-date -> bahai-date
  ;; Future Bahai date corresponding to fixed $date$.
  (let* ((new-year (future-bahai-new-year-on-or-before date))
         (years (round (/ (- new-year bahai-epoch)
                          mean-tropical-year)))
         (major (1+ (quotient years 361)))
         (cycle (1+ (quotient (mod years 361) 19)))
         (year (1+ (mod years 19)))
         (days; Since start of year
          (- date new-year))
         (month
          (cond
           ((>= date (fixed-from-future-bahai
                      (bahai-date major cycle year 19 1)))
            ; last month of year
            19)
           ((>= date
                (fixed-from-future-bahai
                 (bahai-date major cycle year ayyam-i-ha 1)))
            ; intercalary month
            ayyam-i-ha)
           (t (1+ (quotient days 19)))))
         (day (- date -1
                 (fixed-from-future-bahai
                  (bahai-date major cycle year month 1)))))
    (bahai-date major cycle year month day)))

(defun visible-crescent (date locale)
  ;; TYPE (fixed-date location) -> boolean
  ;; S. K. Shaukat's criterion for likely
  ;; visibility of crescent moon on $date$ at $locale$.
  (let* ((tee ; Best viewing time.
          (universal-from-standard
           (dusk date locale (deg 4.5l0))
           locale))
         (phase (lunar-phase tee))
         (altitude (lunar-altitude tee locale))
         (arc-of-light ; Angular separation of ...
          (arccos-degrees ; ... sun and moon.
           (* (cosine-degrees (lunar-latitude tee))
              (cosine-degrees phase)))))
    (and (< new phase first-quarter)
         (<= (deg 10.6l0) arc-of-light (deg 90))
         (> altitude (deg 4.1l0)))))

(defun phasis-on-or-before (date locale)
  ;; TYPE (fixed-date location) -> fixed-date
  ;; Closest fixed date on or before $date$ when crescent
  ;; moon first became visible at $locale$.
  (let* ((mean ; Mean date of prior new moon.
          (- date
             (floor (* (/ (lunar-phase (1+ date)) (deg 360))
                       mean-synodic-month))))
         (tau ; Check if not visible yet on $date$.
          (if (and (<= (- date mean) 3)
                   (not (visible-crescent date locale)))
            (- mean 30) ; Must go back a month.
            (- mean 2))))
    (next d tau (visible-crescent d locale))))

(defconstant islamic-locale
  ;; TYPE location
  ;; Sample location for Observational Islamic calendar
  ;; (Cairo, Egypt).
  (location (deg 30.1l0) (deg 31.3l0) (mt 200) 2))

(defun fixed-from-observational-islamic (i-date)
  ;; TYPE islamic-date -> fixed-date
  ;; Fixed date equivalent to Observational Islamic date.
  (let* ((month (standard-month i-date))
         (day (standard-day i-date))
         (year (standard-year i-date))
         (midmonth ; Middle of given month.
          (+ islamic-epoch
             (floor (* (+ (* (1- year) 12)
                          month -1/2)
                       mean-synodic-month)))))
    (+ (phasis-on-or-before ; First day of month.
        midmonth islamic-locale)
       day -1)))

(defun observational-islamic-from-fixed (date)
  ;; TYPE fixed-date -> islamic-date
  ;; Observational Islamic date (year month day)
  ;; corresponding to fixed $date$.
  (let* ((crescent ; Most recent new moon.
          (phasis-on-or-before date islamic-locale))
         (elapsed-months
          (round (/ (- crescent islamic-epoch)
                    mean-synodic-month)))
         (year (1+ (quotient elapsed-months 12)))
         (month (1+ (mod elapsed-months 12)))
         (day (1+ (- date crescent))))
    (islamic-date year month day)))

(defconstant jerusalem
  ;; TYPE location
  ;; Location of Jerusalem.
  (location (deg 31.8l0) (deg 35.2l0) (mt 800) 2))

(defun astronomical-easter (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Date of (proposed) astronomical Easter in Gregorian
  ;; year.
  (let* ((jan1 ; Beginning of year.
          (fixed-from-gregorian
           (gregorian-date g-year january 1)))
         (equinox ; Spring equinox.
          (solar-longitude-after jan1 spring))
         (paschal-moon ; Date of next full moon.
          (floor (apparent-from-local
                  (local-from-universal
                   (lunar-phase-after equinox full)
                   jerusalem)))))
    ;; Return the Sunday following the Paschal moon.
    (kday-after paschal-moon sunday)))

(defun classical-passover-eve (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Classical (observational) Passover Eve
  ;; (Nisan 14) occurring in Gregorian year.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (equinox ; Date (UT) of spring of g-year.
          (solar-longitude-after
           jan1 spring))
         (new-moon ; First possible new moon.
          (phasis-on-or-before
           (+ (floor equinox) 10)
           jerusalem))
         (set ; Time (UT) of sunset at end of 15th.
          (universal-from-standard
           (sunset (+ new-moon 14) jerusalem)
           jerusalem))
         (nisan1 ; First day of Nisan.
          (if ; Spring starts before end of 15th.
              (< equinox set)
              new-moon
            (phasis-on-or-before ; Otherwise next month.
             (+ new-moon 45) 
             jerusalem))))
    (+ nisan1 13)))


;;;; Section: Persian Calendar

(defun persian-date (year month day)
  ;; TYPE (persian-year persian-month persian-day)
  ;; TYPE -> persian-date
  (list year month day))

(defconstant persian-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Persian calendar.
  (fixed-from-julian (julian-date (ce 622) march 19)))

(defconstant tehran
  ;; TYPE location
  ;; Location of Tehran, Iran.
  (location (deg 35.68l0) (deg 51.42l0) (mt 1100) 3.5l0))

(defun midday-in-tehran (date)
  ;; TYPE fixed-date -> angle
  ;; Universal time of midday on fixed $date$ in Tehran.
  (universal-from-standard
    (midday date tehran)
    tehran))

(defun persian-new-year-on-or-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date of Astronomical Persian New Year on or
  ;; before fixed $date$.
  (let* ((approx ; Approximate time of equinox.
          (estimate-prior-solar-longitude
           (midday-in-tehran date)
           spring)))
    (next day (- (floor approx) 1)
          (<= (solar-longitude (midday-in-tehran day))
              (+ spring (deg 2))))))

(defun fixed-from-persian (p-date)
  ;; TYPE persian-date -> fixed-date
  ;; Fixed date of Astronomical Persian date.
  (let* ((month (standard-month p-date))
         (day (standard-day p-date))
         (year (standard-year p-date))
         (new-year
          (persian-new-year-on-or-before
           (+ persian-epoch 180; Fall after epoch.
              (floor
               (* mean-tropical-year
                  (if (< 0 year)
                      (1- year)
                    year))))))); No year zero.
    (+ (1- new-year)     ; Days in prior years.
       (if (<= month 7)  ; Days in prior months this year.
           (* 31 (1- month))
         (+ (* 30 (1- month)) 6))
       day)))            ; Days so far this month.

(defun persian-from-fixed (date)
  ;; TYPE fixed-date -> persian-date
  ;; Astronomical Persian date (year month day)
  ;; corresponding to fixed $date$.
  (let* ((new-year
          (persian-new-year-on-or-before date))
         (y (1+ (round (/ (- new-year persian-epoch)
                          mean-tropical-year))))
         (year (if (< 0 y)
                   y
                 (1- y))); No year zero
         (day-of-year (1+ (- date
                             (fixed-from-persian
                              (persian-date year 1 1)))))
         (month (if (<= day-of-year 186)
                    (ceiling (/ day-of-year 31))
                  (ceiling (/ (- day-of-year 6) 30))))
         (day           ; Calculate the day by subtraction
          (- date (1- (fixed-from-persian
                       (persian-date year month 1))))))
    (persian-date year month day)))

(defun arithmetic-persian-leap-year? (p-year)
  ;; TYPE persian-year -> boolean
  ;; True if $p-year$ is a leap year on the Persian calendar.
  (let* ((y ; Years since start of 2820-year cycles
          (if (< 0 p-year)
              (- p-year 474)
            (- p-year 473))); No year zero
         (year ; Equivalent year in the range 474...3263
          (+ (mod y 2820) 474)))
    (< (mod (* (+ year 38)
               682)
            2816)
       682)))

(defun fixed-from-arithmetic-persian (p-date)
  ;; TYPE persian-date -> fixed-date
  ;; Fixed date equivalent to Persian date.
  (let* ((day (standard-day p-date))
         (month (standard-month p-date))
         (p-year (standard-year p-date))
         (y ; Years since start of 2820-year cycle
          (if (< 0 p-year)
              (- p-year 474)
            (- p-year 473))); No year zero
         (year ; Equivalent year in the range 474...3263
          (+ (mod y 2820) 474)))
    (+ (1- persian-epoch); Days before epoch
       (* 1029983        ; Days in 2820-year cycles
                         ; before Persian year 474
          (quotient y 2820))
       (* 365 (1- year)) ; Nonleap days in prior years this
                         ; 2820-year cycle
       (quotient         ; Leap days in prior years this
                         ; 2820-year cycle
        (- (* 682 year) 110) 2816)
       (if (<= month 7)  ; Days in prior months this year
           (* 31 (1- month))
         (+ (* 30 (1- month)) 6))
       day)))            ; Days so far this month

(defun arithmetic-persian-year-from-fixed (date)
  ;; TYPE fixed-date -> persian-year
  ;; Persian year corresponding to the fixed $date$.
  (let* ((d0      ; Prior days since start of 2820-year cycle
                  ; beginning in Persian year 474
          (- date (fixed-from-arithmetic-persian
                   (persian-date 475 1 1))))
         (n2820   ; Completed prior 2820-year cycles
          (quotient d0 1029983))
         (d1      ; Prior days not in n2820--that is, days
                  ; since start of last 2820-year cycle
          (mod d0 1029983))
         (y2820 ; Years since start of last 2820-year cycle
          (if (= d1 1029982)
              ;; Last day of 2820-year cycle
              2820
            ;; Otherwise use cycle of years formula
            (quotient (+ (* 2816 d1) 1031337)
                      1028522)
            ;; If (* 2816 d1) causes integers that are
            ;; too large, use instead:
            ;; (let ((a (floor d1 366))
            ;;       (b (mod d1 366)))
            ;;  (+ 1 a (quotient
            ;;          (+ (* 2134 a) (* 2816 b) 2815)
            ;;          1028522)))
            ))
         (year    ; Years since Persian epoch
          (+ 474     ; Years before start of 2820-year cycles
             (* 2820 n2820) ; Years in prior 2820-year cycles
             y2820))); Years since start of last 2820-year
                     ; cycle
    (if (< 0 year)
        year
      (1- year)))); No year zero

(defun arithmetic-persian-from-fixed (date)
  ;; TYPE fixed-date -> persian-date
  ;; Persian date corresponding to fixed $date$.
  (let* ((year (arithmetic-persian-year-from-fixed date))
         (day-of-year (1+ (- date
                             (fixed-from-arithmetic-persian
                              (persian-date year 1 1)))))
         (month (if (<= day-of-year 186)
                    (ceiling (/ day-of-year 31))
                  (ceiling (/ (- day-of-year 6) 30))))
         (day           ; Calculate the day by subtraction
          (- date (1- (fixed-from-arithmetic-persian
                       (persian-date year month 1))))))
    (persian-date year month day)))

(defun naw-ruz (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Persian New Year (Naw-Ruz) in Gregorian
  ;; year.
  (let* ((persian-year
          (1+ (- g-year
                 (gregorian-year-from-fixed
                  persian-epoch)))))
    (fixed-from-persian
     (persian-date (if (<= persian-year 0)
                           ;; No Persian year 0
                           (1- persian-year)
                         persian-year)
                   1 1))))


;;;; Section: French Revolutionary Calendar

(defun french-date (year month day)
  ;; TYPE (french-year french-month french-day) -> french-date
  (list year month day))

(defconstant french-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the French Revolutionary
  ;; calendar.
  (fixed-from-gregorian (gregorian-date 1792 september 22)))

(defconstant paris
  ;; TYPE location
  ;; Location of Paris.  Longitude corresponds to difference
  ;; of 9m 21s between Paris time zone and Universal Time.
   (location (deg 48.51l0) (angle 2 20 15) (mt 27) 1))

(defun midnight-in-paris (date)
  ;; TYPE fixed-date -> moment
  ;; Universal time of true midnight at end of fixed date
  ;; in Paris.
  (universal-from-standard
   (midnight (+ date 1) paris)
   paris))

(defun french-new-year-on-or-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date of French Revolutionary New Year on or
  ;; before fixed $date$.
  (let* ((approx ; Approximate time of solstice.
          (estimate-prior-solar-longitude
           (midnight-in-paris date)
           autumn)))
    (next day (- (floor approx) 1)
          (<= autumn (solar-longitude
                      (midnight-in-paris day))))))

(defun fixed-from-french (f-date)
  ;; TYPE french-date -> fixed-date
  ;; Fixed date of French Revolutionary date.
  (let* ((month (standard-month f-date))
         (day (standard-day f-date))
         (year (standard-year f-date))
         (new-year
          (french-new-year-on-or-before
           (floor (+ french-epoch 180; Spring after epoch.
                     (* mean-tropical-year
                        (1- year)))))))
    (+ new-year -1      ;  Days in prior years
       (* 30 (1- month));  Days in prior months
       day)))           ;  Days this month

(defun french-from-fixed (date)
  ;; TYPE fixed-date -> french-date
  ;; French Revolutionary date of fixed $date$.
  (let* ((new-year
          (french-new-year-on-or-before date))
         (year (1+ (round (/ (- new-year french-epoch)
                             mean-tropical-year))))
         (month (1+ (quotient (- date new-year) 30)))
         (day (1+ (mod (- date new-year) 30))))
    (french-date year month day)))

(defun modified-french-leap-year? (f-year)
  ;; TYPE french-year -> boolean
  ;; True if $f-year$ is a leap year on the French
  ;; Revolutionary calendar.
  (and (= (mod f-year 4) 0)
       (not (member (mod f-year 400) (list 100 200 300)))
       (not (= (mod f-year 4000) 0))))

(defun fixed-from-modified-french (f-date)
  ;; TYPE french-date -> fixed-date
  ;; Fixed date of French Revolutionary date.
  (let* ((month (standard-month f-date))
         (day (standard-day f-date))
         (year (standard-year f-date)))
    (+ french-epoch -1; Days before start of calendar.
       (* 365 (1- year)); Ordinary days in prior years.
       ; Leap days in prior years.
       (quotient (1- year) 4)
       (- (quotient (1- year) 100))
       (quotient (1- year) 400)
       (- (quotient (1- year) 4000))
       (* 30 (1- month)); Days in prior months this year.
       day))); Days this month.

(defun modified-french-from-fixed (date)
  ;; TYPE fixed-date -> french-date
  ;; French Revolutionary date (year month day) of fixed
  ;; $date$.
  (let* ((approx   ; Approximate year (may be off by 1).
          (1+ (quotient (- date french-epoch -2)
                        1460969/4000)))
         (year (if (< date
                      (fixed-from-modified-french
                       (french-date approx 1 1)))
                   (1- approx)
                 approx))
         (month    ; Calculate the month by division.
          (1+ (quotient
               (- date (fixed-from-modified-french
                         (french-date year 1 1)))
               30)))
         (day      ; Calculate the day by subtraction.
          (1+ (- date
                 (fixed-from-modified-french
                  (french-date year month 1))))))
    (french-date year month day)))


;;;; Section: Chinese Calendar

(defun chinese-date (cycle year month leap day)
  ;; TYPE (chinese-cycle chinese-year chinese-month
  ;; TYPE  chinese-leap chinese-day) -> chinese-date
  (list cycle year month leap day))

(defun chinese-cycle (date)
  ;; TYPE chinese-date -> chinese-cycle
  (first date))

(defun chinese-year (date)
  ;; TYPE chinese-date -> chinese-year
  (second date))

(defun chinese-month (date)
  ;; TYPE chinese-date -> chinese-month
  (third date))

(defun chinese-leap (date)
  ;; TYPE chinese-date -> chinese-leap
  (fourth date))

(defun chinese-day (date)
  ;; TYPE chinese-date -> chinese-day
  (fifth date))

(defun chinese-location (tee)
  ;; TYPE moment -> location
  ;; Location of Beijing; time zone varies with $tee$.
  (let* ((year (gregorian-year-from-fixed (floor tee))))
    (if (< year 1929)
        (location (deg 39.55l0) (angle 116 25 0)
                  (mt 43.5) 1397/180)
      (location (deg 39.55l0) (angle 116 25 0)
                (mt 43.5) 8))))

(defun chinese-solar-longitude-on-or-after (date theta)
  ;; TYPE (moment season) -> moment
  ;; Moment (Beijing time) of the first date on or after
  ;; fixed $date$ (Beijing time) when the solar longitude
  ;; will be $theta$ degrees.
  (let ((tee (solar-longitude-after
              (universal-from-standard
               date
               (chinese-location date))
              theta)))
    (standard-from-universal
     tee
     (chinese-location tee))))

(defun current-major-solar-term (date)
  ;; TYPE fixed-date -> integer
  ;; Last Chinese major solar term (zhongqi) before fixed
  ;; $date$.
  (let ((s (solar-longitude
            (universal-from-standard
             date
             (chinese-location date)))))
    (adjusted-mod (+ 2 (quotient s (deg 30))) 12)))

(defun major-solar-term-on-or-after (date)
  ;; TYPE fixed-date -> moment
  ;; Fixed date (in Beijing) of the first Chinese major
  ;; solar term (zhongqi) on or after fixed $date$.  The
  ;; major terms begin when the sun's longitude is a
  ;; multiple of 30 degrees.
  (let* ((l (mod (* 30
                    (ceiling (/ (solar-longitude
                                 (midnight-in-china date))
                                30)))
                 360)))
    (chinese-solar-longitude-on-or-after date l)))

(defun current-minor-solar-term (date)
  ;; TYPE fixed-date -> integer
  ;; Last Chinese minor solar term (jieqi) before $date$.
  (let* ((s (solar-longitude
             (midnight-in-china date))))
    (adjusted-mod (+ 3 (quotient (- s (deg 15)) (deg 30))) 
                  12)))

(defun minor-solar-term-on-or-after (date)
  ;; TYPE fixed-date -> moment
  ;; Moment (in Beijing) of the first Chinese minor solar
  ;; term (jieqi) on or after fixed $date$.  The minor terms
  ;; begin when the sun's longitude is an odd multiple of 15
  ;; degrees.
  (let* ((l (mod
             (+ (* 30
                   (ceiling
                    (/ (- (solar-longitude
                           (midnight-in-china date))
                          (deg 15))
                       30)))
                (deg 15))
             360)))
    (chinese-solar-longitude-on-or-after date l)))

(defun chinese-new-moon-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date (Beijing) of first new moon before
  ;; fixed $date$.
  (let ((tee (new-moon-before
              (midnight-in-china date))))
    (floor
     (standard-from-universal
      tee
      (chinese-location tee)))))

(defun chinese-new-moon-on-or-after (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date (Beijing) of first new moon on or after
  ;; fixed $date$.
  (let ((tee (new-moon-after
              (midnight-in-china date))))
    (floor
     (standard-from-universal
      tee
      (chinese-location tee)))))

(defconstant chinese-epoch
  ;; TYPE fixed-date
  ;; Fixed date of start of the Chinese calendar.
  (fixed-from-gregorian (gregorian-date -2636 february 15)))

(defun no-major-solar-term? (date)
  ;; TYPE fixed-date -> boolean
  ;; True if Chinese lunar month starting on $date$
  ;; has no major solar term.
  (= (current-major-solar-term date)
     (current-major-solar-term
      (chinese-new-moon-on-or-after (+ date 1)))))

(defun midnight-in-china (date)
  ;; TYPE fixed-date -> moment
  ;; Universal time of (clock) midnight at start of fixed
  ;; $date$ in China.
  (universal-from-standard date
                           (chinese-location date)))

(defun chinese-winter-solstice-on-or-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date, in the Chinese zone, of winter solstice
  ;; on or before fixed $date$.
  (let* ((approx ; Approximate time of solstice.
          (estimate-prior-solar-longitude
           (midnight-in-china (+ date 1))
           winter)))
    (next day (1- (floor approx))
          (<= winter (solar-longitude
                      (midnight-in-china (1+ day)))))))

(defun chinese-new-year-in-sui (date)
  ;; TYPE (fixed-date) -> fixed-date
  ;; Fixed date of Chinese New Year in sui (period from
  ;; solstice to solstice) containing $date$.
  (let* ((s1; prior solstice
          (chinese-winter-solstice-on-or-before date))
         (s2; following solstice
          (chinese-winter-solstice-on-or-before
           (+ s1 370)))
         (m12 ; month after 11th month--either 12 or leap 11
          (chinese-new-moon-on-or-after (1+ s1)))
         (m13 ; month after m12--either 12 (or leap 12) or 1
          (chinese-new-moon-on-or-after (1+ m12)))
         (next-m11 ; next 11th month
          (chinese-new-moon-before (1+ s2))))
    (if ; Either m12 or m13 is a leap month if there are
        ; 13 new moons (12 full lunar months) and
        ; either m12 or m13 has no major solar term
        (and (= (round (/ (- next-m11 m12)
                          mean-synodic-month))
                12)
             (or (no-major-solar-term? m12)
                 (no-major-solar-term? m13)))
        (chinese-new-moon-on-or-after (1+ m13))
      m13)))

(defun chinese-new-year-on-or-before (date)
  ;; TYPE fixed-date -> fixed-date
  ;; Fixed date of Chinese New Year on or before fixed $date$.
  (let* ((new-year (chinese-new-year-in-sui date)))
    (if (>= date new-year)
        new-year
      ; Got the New Year after--this happens if date is
      ; after the solstice but before the new year.
      ; So, go back half a year.
      (chinese-new-year-in-sui (- date 180)))))

(defun chinese-new-year (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Chinese New Year in Gregorian year.
  (chinese-new-year-on-or-before
   (fixed-from-gregorian
    (gregorian-date g-year july 1))))

(defun chinese-from-fixed (date)
  ;; TYPE fixed-date -> chinese-date
  ;; Chinese date (cycle year month leap day) of fixed
  ;; $date$.
  (let* ((s1; Prior solstice
          (chinese-winter-solstice-on-or-before date))
         (s2; Following solstice
          (chinese-winter-solstice-on-or-before (+ s1 370)))
         (m12     ; month after last 11th month
          (chinese-new-moon-on-or-after (1+ s1)))
         (next-m11; next 11th month
          (chinese-new-moon-before (1+ s2)))
         (m      ; start of month containing date
          (chinese-new-moon-before (+ date 1)))
         (leap-year; if there are 13 new moons (12 full
                   ; lunar months)
          (= (round (/ (- next-m11 m12)
                       mean-synodic-month))
             12))
         (month  ; month number
          (adjusted-mod
           (-
            ;; ordinal position of month in year
            (round (/ (- m m12) mean-synodic-month))
            ;; minus 1 during or after a leap month
            (if (and leap-year (prior-leap-month? m12 m))
                1
              0))
           12))
         (leap-month    ; it's a leap month if...
          (and leap-year; ...there are 13 months
               (no-major-solar-term? m); no major solar term
               (not (prior-leap-month?; and no prior leap
				      ; month
                     m12 (chinese-new-moon-before m)))))
         (elapsed-years  ; Approximate since the epoch
          (floor (+ 1.5l0  ; 18 months (because of truncation)
                    (- (/ month 12)); after at start of year
                    (/ (- date chinese-epoch)
                       mean-tropical-year))))
         (cycle (1+ (quotient (1- elapsed-years) 60)))
         (year (adjusted-mod elapsed-years 60))
         (day (1+ (- date m))))
    (chinese-date cycle year month leap-month day)))

(defun fixed-from-chinese (c-date)
  ;; TYPE chinese-date -> fixed-date
  ;; Fixed date of Chinese date (cycle year month leap day).
  (let* ((cycle (chinese-cycle c-date))
         (year (chinese-year c-date))
         (month (chinese-month c-date))
         (leap (chinese-leap c-date))
         (day (chinese-day c-date))
         (mid-year      ;  Middle of the Chinese year
          (floor
           (+ chinese-epoch
              (* (+ (* (1- cycle) 60); years in prior cycles
                    (1- year)        ; prior years this cycle
                    0.5l0)           ; half a year
                 mean-tropical-year))))
         (new-year (chinese-new-year-on-or-before mid-year))
         (p; new moon before date--a month too early if
           ; there was prior leap month that year
          (chinese-new-moon-on-or-after
           (+ new-year (* (1- month) 29))))
         (d (chinese-from-fixed p))
         (prior-new-moon
          (if  ; If the months match...
              (and (= month (chinese-month d))
                   (equal leap (chinese-leap d)))
              p; ...that's the right month
            ;; otherwise, there was a prior leap month that
            ;; year, so we want the next month
            (chinese-new-moon-on-or-after (1+ p)))))
    (+ prior-new-moon day -1)))

(defun prior-leap-month? (m-prime m)
  ;; TYPE (fixed-date fixed-date) -> boolean
  ;; True if there is a Chinese leap month on or after lunar
  ;; month starting on fixed day $m-prime$ and at or before
  ;; lunar month starting at fixed date $m$.
  (and (>= m m-prime)
       (or (no-major-solar-term? m)
           (prior-leap-month? m-prime
                              (chinese-new-moon-before m)))))

(defun chinese-name (stem branch)
  ;; TYPE (chinese-stem chinese-branch) -> chinese-name
  ;; Returns bogus if stem/branch combination is impossible.
  (if (= (mod stem 2) (mod branch 2))
      (list stem branch)
    bogus))

(defun chinese-stem (name)
  ;; TYPE chinese-name -> chinese-stem
  (first name))

(defun chinese-branch (name)
  ;; TYPE chinese-name -> chinese-branch
  (second name))

(defun chinese-sexagesimal-name (n)
  ;; TYPE integer -> chinese-name
  ;; The $n$-th name of the Chinese sexagesimal cycle.
  (chinese-name (adjusted-mod n 10)
                (adjusted-mod n 12)))

(defun chinese-name-difference (c-name1 c-name2)
  ;; TYPE (chinese-name chinese-name) -> integer
  ;; Number of names from Chinese name $c-name1$ to the
  ;; next occurrence of Chinese name $c-name2$.
  (let* ((stem1 (chinese-stem c-name1))
         (stem2 (chinese-stem c-name2))
         (branch1 (chinese-branch c-name1))
         (branch2 (chinese-branch c-name2))
         (stem-difference (- stem2 stem1))
         (branch-difference (- branch2 branch1)))
    (1+ (mod (+ (1- stem-difference)
                (* 25 (- branch-difference
                         stem-difference)))
             60))))

(defun chinese-name-of-year (year)
  ;; TYPE chinese-year -> chinese-name
  ;; Sexagesimal name for Chinese year $year$ of any cycle.
  (chinese-sexagesimal-name year))

(defconstant chinese-month-name-epoch
  ;; TYPE integer
  ;; Index of Chinese sexagesimal name of month 1 of Chinese
  ;; year 1.
  3)

(defun chinese-name-of-month (year month)
  ;; TYPE (chinese-year chinese-month) -> chinese-name
  ;; Sexagesimal name for month $month$ of Chinese year $year$.
  (let* ((elapsed-months (+ (* 12 (1- year))
                            (1- month))))
    (chinese-sexagesimal-name
     (+ elapsed-months chinese-month-name-epoch))))

(defconstant chinese-day-name-epoch
  ;; TYPE integer
  ;; Index of Chinese sexagesimal name of RD 1.
  15)

(defun chinese-name-of-day (date)
  ;; TYPE fixed-date -> chinese-name
  ;; Chinese sexagesimal name for $date$.
    (chinese-sexagesimal-name
     (+ date chinese-day-name-epoch)))

(defun chinese-day-name-on-or-before (name date)
  ;; TYPE (chinese-name fixed-date) -> fixed-date
  ;; Fixed date of latest date on or before fixed $date$
  ;; that has Chinese $name$.
  (- date
     (mod (+ date
             (chinese-name-difference
              name
              (chinese-sexagesimal-name
               chinese-day-name-epoch)))
          60)))

(defun dragon-festival (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of the Dragon Festival occurring in
  ;; Gregorian year.
  (let* ((elapsed-years
          (1+ (- g-year
                 (gregorian-year-from-fixed
                  chinese-epoch))))
         (cycle (1+ (quotient (1- elapsed-years) 60)))
         (year (adjusted-mod elapsed-years 60)))
    (fixed-from-chinese (chinese-date cycle year 5 false 5))))

(defun qing-ming (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Qingming occurring in Gregorian year.
  (floor
   (minor-solar-term-on-or-after
    (fixed-from-gregorian
     (gregorian-date g-year march 30)))))

(defun chinese-age (birthdate date)
  ;; TYPE (chinese-date fixed-date) -> nonnegative-integer
  ;; Age at fixed $date$, given Chinese $birthdate$,
  ;; according to the Chinese custom.
  (let* ((today (chinese-from-fixed date)))
    (if (>= date (fixed-from-chinese birthdate))
        (+ (* 60 (- (chinese-cycle today)
                    (chinese-cycle birthdate)))
           (- (chinese-year today)
              (chinese-year birthdate))
           1)
      bogus)))

(defun japanese-location (tee)
  ;; TYPE moment -> location
  ;; Location for Japanese calendar; varies with $tee$.
  (let* ((year (gregorian-year-from-fixed (floor tee))))
    (if (< year 1888)
        ; Tokyo (139 deg 46 min east) local time
        (location (deg 35.7l0) (angle 139 46 0)
                  (mt 24) (+ 9 143/450))
      ; Longitude 135 time zone
      (location (deg 35) (deg 135)
                (mt 0) 9))))


;;;; Section: Modern Hindu Calendars

(defun hindu-lunar-date (year month leap-month day leap-day)
  ;; TYPE (hindu-lunar-year hindu-lunar-month
  ;; TYPE  hindu-lunar-leap-month hindu-lunar-day
  ;; TYPE  hindu-lunar-leap-day) -> hindu-lunar-date
  (list year month leap-month day leap-day))

(defun hindu-lunar-month (date)
  ;; TYPE hindu-lunar-date -> hindu-lunar-month
  (second date))

(defun hindu-lunar-leap-month (date)
  ;; TYPE hindu-lunar-date -> hindu-lunar-leap-month
  (third date))

(defun hindu-lunar-day (date)
  ;; TYPE hindu-lunar-date -> hindu-lunar-day
  (fourth date))

(defun hindu-lunar-leap-day (date)
  ;; TYPE hindu-lunar-date -> hindu-lunar-leap-day
  (fifth date))

(defun hindu-lunar-year (date)
  ;; TYPE hindu-lunar-date -> hindu-lunar-year
  (first date))

(defun hindu-sine-table (entry)
  ;; TYPE integer -> rational-amplitude
  ;; This simulates the Hindu sine table.
  ;; $entry$ is an angle given as a multiplier of 225'.
  (let* ((exact (* 3438 (sin-degrees (* entry 225/60))))
         (error (* 0.215 (signum exact)
                   (signum (- (abs exact) 1716)))))
    (/ (round (+ exact error)) 3438)))

(defun hindu-sine (theta)
  ;; TYPE angle -> rational-amplitude
  ;; Linear interpolation in Hindu table is used.
  (let* ((entry (* theta 60/225)) ; Interpolate in table.
         (fraction (mod entry 1)))
    (+ (* fraction
          (hindu-sine-table (ceiling entry)))
       (* (- 1 fraction)
          (hindu-sine-table (floor entry))))))

(defun hindu-arcsin (amp)
  ;; TYPE rational-amplitude -> rational-angle
  ;; Inverse of Hindu sine function.
  (if (< amp 0) (- (hindu-arcsin (- amp)))
    (let* ((pos (next k 0 (<= amp (hindu-sine-table k))))
           (below ; Lower value in table.
            (hindu-sine-table (1- pos))))
      (* 225/60
         (+ pos -1  ; Interpolate.
            (/ (- amp below)
               (- (hindu-sine-table pos) below)))))))

(defconstant hindu-sidereal-year
  ;; TYPE rational
  ;; Mean length of Hindu sidereal year.
  (+ 365 279457/1080000))

(defconstant hindu-creation
  ;; TYPE fixed-date
  ;; Fixed date of Hindu creation.
  (- hindu-epoch (* 1955880000 hindu-sidereal-year)))

(defun mean-position (tee period)
  ;; TYPE (rational-moment rational) -> rational-angle
  ;; Position in degrees at moment $tee$ in uniform circular
  ;; orbit of $period$ days.
  (* (deg 360) (mod (/ (- tee hindu-creation) period) 1)))

(defconstant hindu-sidereal-month
  ;; TYPE rational
  ;; Mean length of Hindu sidereal month.
  (+ 27 4644439/14438334))

(defconstant hindu-synodic-month
  ;; TYPE rational
  ;; Mean time from new moon to new moon.
  (+ 29 7087771/13358334))

(defconstant hindu-anomalistic-year
  ;; TYPE rational
  ;; Time from aphelion to aphelion.
  (/ 1577917828000 (- 4320000000 387)))

(defconstant hindu-anomalistic-month
  ;; TYPE rational
  ;; Time from apogee to apogee, with bija correction.
  (/ 1577917828 (- 57753336 488199)))

(defun true-position (tee period size anomalistic change)
  ;; TYPE (rational-moment rational rational rational
  ;; TYPE  rational) -> rational-angle
  ;; Longitudinal position at moment $tee$.  $period$ is
  ;; period of mean motion in days.  $size$ is ratio of
  ;; radii of epicycle and deferent.  $anomalistic$ is the
  ;; period of retrograde revolution about epicycle.
  ;; $change$ is maximum decrease in epicycle size.
  (let* ((long ; Position of epicycle center
          (mean-position tee period))
         (offset ; Sine of anomaly
          (hindu-sine (mean-position tee anomalistic)))
         (contraction (* (abs offset) change size))
         (equation ; Equation of center
          (hindu-arcsin (* offset (- size contraction)))))
    (mod (- long equation) 360)))

(defun hindu-solar-longitude (tee)
  ;; TYPE rational-moment -> rational-angle
  ;; Solar longitude at moment $tee$.
  (true-position tee hindu-sidereal-year
                 14/360 hindu-anomalistic-year 1/42))

(defun hindu-zodiac (tee)
  ;; TYPE rational-moment -> hindu-solar-month
  ;; Zodiacal sign of the sun, as integer in range 1..12,
  ;; at moment $tee$.
  (1+ (quotient (hindu-solar-longitude tee) (deg 30))))

(defun hindu-lunar-longitude (tee)
  ;; TYPE rational-moment -> rational-angle
  ;; Lunar longitude at moment $tee$.
  (true-position tee hindu-sidereal-month
                 32/360 hindu-anomalistic-month 1/96))

(defun hindu-lunar-phase (tee)
  ;; TYPE rational-moment -> rational-angle
  ;; Longitudinal distance between the sun and moon
  ;; at moment $tee$.
  (mod (- (hindu-lunar-longitude tee)
          (hindu-solar-longitude tee))
       360))

(defun lunar-day (tee)
  ;; TYPE rational-moment -> hindu-lunar-day
  ;; Phase of moon (tithi) at moment $tee$, as an integer in
  ;; the range 1..30.
  (1+ (quotient (hindu-lunar-phase tee) (deg 12))))

(defun hindu-new-moon-before (tee)
  ;; TYPE rational-moment -> rational-moment
  ;; Approximate moment of last new moon preceding
  ;; moment $tee$.
  (let* ((varepsilon (expt 2 -1000)) ; Safety margin.
         (tau  ; Can be off by almost a day.
          (- tee (* (/ 1 (deg 360)) (hindu-lunar-phase tee)
                    hindu-synodic-month))))
    (binary-search ; Search for phase start.
     l (1- tau)
     u (min tee (1+ tau))
     x (< (hindu-lunar-phase x) (deg 180))
     (or (= (hindu-zodiac l)
            (hindu-zodiac u))
         (< (- u l) varepsilon)))))

(defun lunar-day-after (tee k)
  ;; TYPE (rational-moment rational) -> rational-moment
  ;; Time lunar-day (tithi) number $k$ begins at or after
  ;; moment $tee$.  $k$ can be fractional (for karanas).
  (let* ((varepsilon (expt 2 -17))
         (phase ; Degrees corresponding to k.
          (* (1- k) 12))
         (tau ; Mean occurrence of lunar-day.
          (+ tee (* 1/360
                    (mod (- phase (hindu-lunar-phase tee))
                         (deg 360))
                    hindu-synodic-month))))
    (binary-search ; Search for phase start.
     l (max tee (- tau 2))
     u (+ tau 2)
     x (< (mod (- (hindu-lunar-phase x) phase) 360)
          (deg 180))
     (< (- u l) varepsilon))))
   
(defun hindu-calendar-year (tee)
  ;; TYPE rational-moment -> hindu-solar-year
  ;; Determine solar year at given moment $tee$.
  (round (- (/ (- tee hindu-epoch)
               hindu-sidereal-year)
            (/ (hindu-solar-longitude tee)
               (deg 360)))))

(defconstant hindu-solar-era
  ;; TYPE standard-year
  ;; Years from Kali Yuga until Saka era.
  3179)

(defun hindu-solar-from-fixed (date)
  ;; TYPE fixed-date -> hindu-solar-date
  ;; Hindu (Orissa) solar date equivalent to fixed $date$.
  (let* ((critical    ; Sunrise on Hindu date.
          (hindu-sunrise (1+ date)))
         (month (hindu-zodiac critical))
         (year (- (hindu-calendar-year critical)
                  hindu-solar-era))
         (approx ; 3 days before start of mean month.
          (- date 3
             (mod (floor (hindu-solar-longitude critical))
                  (deg 30))))
         (begin ; Search forward for beginning...
          (next i approx ; ... of month.
                (= (hindu-zodiac (hindu-sunrise (1+ i)))
                   month)))
         (day (- date begin -1)))
    (hindu-solar-date year month day)))

(defun hindu-solar-on-or-before? (s-date1 s-date2)
  ;; TYPE (hindu-solar-date hindu-solar-date) -> boolean
  ;; True if Hindu solar $s-date1$ is on or before $s-date2$.
  (let* ((month1 (standard-month s-date1))
         (month2 (standard-month s-date2))
         (day1 (standard-day s-date1))
         (day2 (standard-day s-date2))
         (year1 (standard-year s-date1))
         (year2 (standard-year s-date2)))
    (or (< year1 year2)
        (and (= year1 year2)
             (or (< month1 month2)
                 (and (= month1 month2)
                      (<= day1 day2)))))))

(defun fixed-from-hindu-solar (s-date)
  ;; TYPE hindu-solar-date -> fixed-date
  ;; Fixed date corresponding to Hindu solar date (Saka era).
  (let* ((month (standard-month s-date))
         (day (standard-day s-date))
         (year (standard-year s-date))
         (approx; Approximate date from below
                ; by adding days...
          (+ (floor (* (+ year hindu-solar-era
                          (/ (1- month) 12))   ; in months...
                       hindu-sidereal-year))   ; ... and years
             hindu-epoch   ; and days before RD 0.
             day -1))
         (rate (/ (deg 360) hindu-sidereal-year))
         (phi ; Estimated longitude.
          (+ (* (1- month) (deg 30)) (* (1- day) rate)))
         (cap-Delta ; Difference in longitude.
          (- (mod (- (hindu-solar-longitude
                      (+ approx 1/4))
                     phi (- (deg 180)))
                  360)
             (deg 180)))
         (tau ; Better approximation.
          (- approx (ceiling (/ cap-Delta rate)))))
    ;; Search forward to correct date or just past.
    (next d (1- tau)
          (hindu-solar-on-or-before?
           s-date
           (hindu-solar-from-fixed d)))))

(defconstant hindu-lunar-era
  ;; TYPE standard-year
  ;; Years from Kali Yuga until Vikrama era.
  3044)

(defun hindu-lunar-from-fixed (date)
  ;; TYPE fixed-date -> hindu-lunar-date
  ;; Hindu lunar date equivalent to fixed $date$.
  (let* ((critical (hindu-sunrise date)) ; Sunrise that day.
         (day (lunar-day critical)); Day of month.
         (leapday             ; If previous day the same.
          (= day (lunar-day (hindu-sunrise (- date 1)))))
         (last-new-moon
          (hindu-new-moon-before critical))
         (next-new-moon
          (hindu-new-moon-before
           (+ (floor last-new-moon) 35)))
         (solar-month         ; Solar month name.
          (hindu-zodiac last-new-moon))
         (leapmonth       ; If begins and ends in same sign.
          (= solar-month (hindu-zodiac next-new-moon)))
         (month                     ; Month of lunar year.
          (adjusted-mod (1+ solar-month) 12))
         (year ; Solar year at next new moon.
          (- (hindu-calendar-year next-new-moon)
             hindu-lunar-era ; Era
             ;; If month is leap, it belongs to next month's
             ;; year.
             (if (and leapmonth (= month 1)) -1 0))))
    (hindu-lunar-date year month leapmonth day leapday)))

(defun hindu-lunar-on-or-before? (l-date1 l-date2)
  ;; TYPE (hindu-lunar-date hindu-lunar-date) -> boolean
  ;; True if Hindu lunar date $l-date1$ is on or before
  ;; Hindu lunar date $l-date2$.
  (let* ((month1 (hindu-lunar-month l-date1))
         (month2 (hindu-lunar-month l-date2))
         (leap1 (hindu-lunar-leap-month l-date1))
         (leap2 (hindu-lunar-leap-month l-date2))
         (day1 (hindu-lunar-day l-date1))
         (day2 (hindu-lunar-day l-date2))
         (leapday1 (hindu-lunar-leap-day l-date1))
         (leapday2 (hindu-lunar-leap-day l-date2))
         (year1 (hindu-lunar-year l-date1))
         (year2 (hindu-lunar-year l-date2)))
    (or (< year1 year2)
        (and (= year1 year2)
             (or (< month1 month2)
                 (and (= month1 month2)
                      (or (and leap1 (not leap2))
                          (and (equal leap1 leap2)
                               (or (< day1 day2)
                                   (and (= day1 day2)
                                        (or (not leapday1)
                                            leapday2)))))
                      ))))))

(defun fixed-from-hindu-lunar (l-date)
  ;; TYPE hindu-lunar-date -> fixed-date
  ;; Fixed date corresponding to Hindu lunar date $l-date$.
  (let* ((year (hindu-lunar-year l-date))
         (month (hindu-lunar-month l-date))
         (leap (hindu-lunar-leap-month l-date))
         (day (hindu-lunar-day l-date))
         (approx
          (+ hindu-epoch
             (* hindu-sidereal-year
                (+ year hindu-lunar-era
                   (/ (1- month) 12)))))
         (s (floor
             (- approx
                (* (/ 1 (deg 360)) hindu-sidereal-year
                   (- (mod (- (hindu-solar-longitude approx)
                              (* (1- month) (deg 30))
                              (- (deg 180)))
                           (deg 360))
                      180)))))
         (k (lunar-day (+ s 1/4)))
         (est
          (- s (- day)
             (cond
              ((< 3 k 27) ; Not borderline case.
               k)
              ((let* ((mid ; Middle of preceding solar month.
                       (hindu-lunar-from-fixed
                        (- s 15))))
                 (or ; In month starting near $s$.
                  (< (hindu-lunar-month mid) month)
                  (and (hindu-lunar-leap-month mid)
                       (not leap))))
               (- (mod (+ k 15) 30) 15))
              (t ; In preceding month.
               (+ (mod (- k 15) 30) 15)))))
         (tau ; Refined estimate.
          (- est (mod (- (lunar-day (+ est 1/4))
                         day -15)
                      30)
             -15)))
    (next d (1- tau)
          (hindu-lunar-on-or-before?
           l-date
           (hindu-lunar-from-fixed d)))))

(defun hindu-equation-of-time (date)
  ;; TYPE fixed-date -> rational-moment
  ;; Time from mean to true midnight of $date$.
  ;; (This is a gross approximation to the correct value.)
  (let* ((offset (hindu-sine
                  (mean-position date
                                 hindu-anomalistic-year)))
         (equation-sun ; Sun's equation of center
              ; Arcsin is not needed since small
          (* offset (/ (deg 3438) 60)
             (- (/ (abs offset) 1080) 14/360))))
    (* (daily-motion date) (/ 1 (deg 360))
       equation-sun (/ 1 (deg 360))
       hindu-sidereal-year)))

(defun ascensional-difference (date locale)
  ;; TYPE (fixed-date location) -> rational-angle
  ;; Difference between right and oblique ascension
  ;; of sun on $date$ at $locale$.
  (let* ((sin-decl
          (* 1397/3438 ; Sine of inclination.
             (hindu-sine (hindu-tropical-longitude date))))
         (lat (latitude locale))
         (diurnal-radius
          (hindu-sine (+ (deg 90) (hindu-arcsin sin-decl))))
         (tan-lat ; Tangent of latitude as rational number.
          (/ (hindu-sine lat)
             (hindu-sine (+ (deg 90) lat))))
         (earth-sine (* sin-decl tan-lat)))
    (hindu-arcsin (- (/ earth-sine diurnal-radius)))))

(defun hindu-tropical-longitude (date)
  ;; TYPE fixed-date -> rational-angle
  ;; Hindu tropical longitude on fixed $date$.
  ;; Assumes precession with maximum of 27 degrees
  ;; and period of 7200 sidereal years
  ;; (= 1577917828/600 days).
  (let* ((days (floor (- date hindu-epoch))) ; Whole days.
         (precession
          (- (deg 27)
             (abs
              (- (deg 54)
                 (mod (+ (deg 27)
                         (* (deg 108) 600/1577917828 days))
                      108))))))
    (mod (- (hindu-solar-longitude date) precession)
         360)))

(defun rising-sign (date)
  ;; TYPE fixed-date -> rational-amplitude
  ;; Tabulated speed of rising of current zodiacal sign on
  ;; $date$.
  (let* ((i  ; Index.
          (quotient (hindu-tropical-longitude date)
                    (deg 30))))
    (nth (mod i 6)
         (list 1670/1800 1795/1800 1935/1800 1935/1800
               1795/1800 1670/1800))))

(defun daily-motion (date)
  ;; TYPE fixed-date -> rational-angle
  ;; Sidereal daily motion of sun on $date$.
  (let* ((mean-motion ; Mean daily motion in degrees.
          (/ (deg 360) hindu-sidereal-year))
         (anomaly
          (mean-position date hindu-anomalistic-year))
         (epicycle ; Current size of epicycle.
          (- 14/360 (/ (abs (hindu-sine anomaly)) 1080)))
         (entry (quotient anomaly (/ (deg 225) 60)))
         (sine-table-step ; Marginal change in anomaly
          (- (hindu-sine-table (1+ entry))
             (hindu-sine-table entry)))
         (factor
          (* sine-table-step -3438/225 epicycle)))
    (* mean-motion (1+ factor))))

(defun solar-sidereal-difference (date)
  ;; TYPE fixed-date -> rational-angle
  ;; Difference between solar and sidereal day on $date$.
  (* (daily-motion date) (rising-sign date)))

(defconstant ujjain
  ;; TYPE location
  ;; Location of Ujjain.
  (location (angle 23 9 0) (angle 75 46 6)
            (mt 0) (+ 5 461/9000)))

(defconstant hindu-locale
  ;; TYPE location
  ;; Location (Ujjain) for determining Hindu calendar.
  ujjain)

(defun hindu-sunrise (date)
  ;; TYPE fixed-date -> rational-moment
  ;; Sunrise at hindu-locale on $date$.
  (+ date 1/4 ; Mean sunrise.
     (/ (- (longitude ujjain) (longitude hindu-locale))
        (deg 360)) ; Difference from longitude.
     (hindu-equation-of-time date) ; Apparent midnight.
     (* ; Convert sidereal angle to fraction of civil day.
      (/ 1577917828/1582237828 (deg 360))
      (+ (ascensional-difference date hindu-locale)
         (* 1/4 (solar-sidereal-difference date))))))

(defun alt-hindu-sunrise (date)
  ;; TYPE fixed-date -> rational-moment
  ;; Astronomical sunrise at Hindu locale on $date$,
  ;; rounded to nearest minute, as a rational number.
  (let* ((rise (sunrise date hindu-locale)))
    (* 1/24 1/60 (round (* rise 24 60)))))

(defun lunar-station (date)
  ;; TYPE fixed-date -> nakshatra
  ;; Hindu lunar station (nakshatra) at sunrise on $date$.
  (let* ((critical (hindu-sunrise date)))
    (1+ (quotient (hindu-lunar-longitude critical) 
                  (/ (deg 800) 60)))))

(defun hindu-solar-longitude-after (tee phi)
  ;; TYPE (moment season) -> moment
  ;; Moment UT of the first time at or after $tee$
  ;; when Hindu solar longitude will be $phi$ degrees.
  (let* ((varepsilon 1/1000000) ; Accuracy.
         (tau ; Estimate (within 5 days).
          (+ tee
             (* hindu-sidereal-year 1/360
                (mod (- phi (hindu-solar-longitude tee))
                     (deg 360)))))
         (l (max tee (- tau 5))) ; At or after tee.
         (u (+ tau 5)))
    (binary-search ; Bisection search.
     l l
     u u
     x (< (mod (- (hindu-solar-longitude x) phi) 360)
          (deg 180))
     (< (- u l) varepsilon))))

(defun mesha-samkranti (g-year)
  ;; TYPE gregorian-year -> rational-moment
  ;; Fixed moment of Mesha samkranti (Vernal equinox)
  ;; in Gregorian $g-year$.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1))))
    (hindu-solar-longitude-after jan1 (deg 0))))

(defun hindu-lunar-new-year (g-year)
  ;; TYPE gregorian-year -> fixed-date
  ;; Fixed date of Hindu lunisolar new year
  ;; in Gregorian $g-year$.
  (let* ((jan1 (fixed-from-gregorian
                (gregorian-date g-year january 1)))
         (mina ; Fixed moment of solar longitude 330.
          (hindu-solar-longitude-after jan1 (deg 330)))
         (new-moon ; Next new moon.
          (lunar-day-after mina 1))
         (h-day (floor new-moon))
         (critical ; Sunrise that day.
          (hindu-sunrise h-day)))
    (+ h-day
       ;; Next day if new moon after sunrise,
       ;; unless lunar day ends before next sunrise.
       (if (or (< new-moon critical)
               (= (lunar-day
                   (hindu-sunrise (1+ h-day))) 2))
           0 1))))

(defun karana (n)
  ;; TYPE {1-60} -> {0-10}
  ;; Number (0-10) of the name of the $n$-th (1-60)
  ;; Hindu karana.
  (cond ((= n 1) 0)
        ((> n 57) (- n 50))
        (t (adjusted-mod (1- n) 7))))

(defun yoga (date)
  ;; TYPE fixed-date -> {1-27}
  ;; Hindu yoga on $date$.
  (1+ (floor (mod (* (+ (hindu-solar-longitude date)
                        (hindu-lunar-longitude date))
                     60/800)
                  (deg 27)))))

(defun sacred-wednesdays-in-gregorian (g-year)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of Wednesdays in Gregorian year $g-year$
  ;; that are day 8 of Hindu lunar months.
  (sacred-wednesdays
   (fixed-from-gregorian ; From beginning of year.
    (gregorian-date g-year january 1))
   (fixed-from-gregorian ; To end.
    (gregorian-date g-year december 31))))

(defun sacred-wednesdays (start end)
  ;; TYPE gregorian-year -> list-of-fixed-dates
  ;; List of Wednesdays between fixed dates $start$ and
  ;; $end$ (inclusive) that are day 8 of Hindu lunar months.
  (if (> start end)
      nil
    (let* ((wed (kday-on-or-after start wednesday))
           (h-date (hindu-lunar-from-fixed wed)))
      (append
       (if (= (hindu-lunar-day h-date) 8)
           (list wed)
         nil)
       (sacred-wednesdays (1+ wed) end)))))
