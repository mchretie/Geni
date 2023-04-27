String getSeconds(List<String> time) {
  String seconds = time[2];
  seconds = seconds.replaceAll(" AM", "");
  seconds = seconds.replaceAll(" PM", "");
  return seconds;
}

String getMinutes(List<String> time) {
  return time[1];
}

bool isPM(List<String> time) {
  return time[2].contains('PM');
}

int getHours(List<String> time) {
  int hours = int.parse(time[0]);
  if (isPM(time)) {
    hours += 12;
  }
  return hours;
}

String getDay(List<String> date) {
  return date[1];
}

String? getMonth(List<String> date) {
  Map<String, String> months = {"Jan" : "01",
    "Feb" : "02",
    "Mar" : "03",
    "Apr" : "04",
    "May" : "05",
    "Jun" : "06",
    "Jul" : "07",
    "Aug" : "08",
    "Sep" : "09",
    "Oct" : "10",
    "Nov" : "11",
    "Dec" : "12",
  };

  return months[date[0]];
}

List<String> getTime(String timeString) {
  return timeString.split(":");
}

List<String> getDate(String dateString) {
  return dateString.split(" ");
}

String getYear(List<String> timeInfos) {
  return timeInfos[1];
}

DateTime timestampToDateTime(String timestamp) {
  //  timestamp : Apr 23, 2023, 5:33:06 PM

  List<String> timeInfos = timestamp.split(', ');
  List<String> date = getDate(timeInfos[0]);
  List<String> time = getTime(timeInfos[2]);

  String year = getYear(timeInfos);

  String? month = getMonth(date);
  String day = getDay(date);
  int hours = getHours(time);
  String minutes = getMinutes(time);
  String seconds = getSeconds(time);

  // DateTime : 2023-04-23 17:33:06

  String formattedString = "$year-$month-$day $hours:$minutes:$seconds";
  return DateTime.parse(formattedString);
}